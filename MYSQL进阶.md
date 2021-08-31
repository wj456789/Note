# MYSQL进阶

## 每门课都大于x分

**请用一条SQL筛选出所有课程的成绩都大于等于85的学生**

```java
>select * from score;
Name Course Grade
张三 语文 89
张三 数学 77
张三 计算机 65
李四 语文 95
李四 数学 90
李四 计算机 88
王五 语文 66
王五 数学 85
王五 计算机 94
```

```java
1、只要最低分数>=85就意味着所有课程成绩>=85
>select name from score group by name having min(grade)>=85;    
    
2、排除法，只要有一门课的成绩小于85就不满足条件
>select distinct name from score A where not exists(
	select * from score B where A.name=B.name and grade<85
);
    
3、所有成绩的个数与>=85的成绩个数相等 
>select name from acore where grade >=85 
    group by name having count(*) = select count(distinct course) from score;
```

## 课程选修问题

**查询至少选修了202002学生选修的全部课程的学生学号**

```java
>select * from score;
学号sno 课程号cno 分数score
202001 01 85
202001 03 65
202002 02 80
202002 04 81
202002 01 68
202003 06 74
202003 01 78
202003 02 70
202003 04 72
202004 01 55
202004 02 44
202004 03 0
    
1）找出学号=202002的学生选修的所有课程
2）根据1）的结果，找出所有跟202002选修了同一门课（任意一门）的数据
3）按学号进行分组，从2）的结果中找出选课数量等于202002学生的选课数量的所有学生

select B.sno from score B where B.cno in(      
	select A.cno from score A where A.sno='202002'
)
group by B.sno
having count(*)=(select count(*) from score C where c.sno='202002');    
```

## 生成比赛对阵表

**有A、B、C、D四个球队进行比赛，用一条SQL显示出所有可能的比赛组合。**

其中一共有4x4=16种组合，但是AA不能比赛，AB匹配之后BA重复

```java
>select * from team;
id name
 1  A
 2  B
 3  C
 4  D
    
1）表和它自身（A、B两张表）做笛卡尔积
2）再筛选出 A.name>B.name 或 A.name < B.name 的所有记录
>select A.name,B.name from team A,team B where A.name < B.name;
```

## 删除冗余记录

**学生表中产生了冗余记录，请用一条SQL删除所有冗余记录。**

```java
>select * from student;
自动编号 学号 姓名 年龄
1 202001 张三 18
2 202002 李四 19
3 202002 李四 19
4 202003 王五 18
5 202004 赵六 21
6 202004 赵六 21
7 202004 赵六 21
8 202005 田七 20
```

```java
排除法，将需要保留的记录排除
1）按照学号、姓名、年龄进行分组，找出最大或最小的所有 id
2）将不在1）的结果之内的所有id对应的记录都删除
        
>delete from student where id not in {
    select max(id) from student group by sno,name,age
};

>delete from student where id in( 
    select id from student where id not in (
        select max(id) from student group by sno,name,age
    )
);

在实际使用中，根据指定条件进行删除或修改数据时，经常会遇到如下错误，意思是不允许在子查询中出现
update或delete要操作的表。
ERROR 1093 (HY000): You can't specify target table 'sql04_student' for update in FROM clause
    
所以mysql中以上两种方式都会报错，这里需要做一个嵌套生成衍生表temp再删除    
>delete from student where id in(
    select * from (
        select id from student where id not in (
            select max(id) from student group by sno,name,age
        )
    ) temp
);
```

## 横表与纵表的概念、场景及互换

### 概念

- 横表：平时接触的绝大多数表都是横表，如(主键，字段1，字段2，…，字段n)
- 纵表：key-value存储方式，如(主键，字段编码，字段值)

### 横表

sql_h

| name | sex  | chinese | math |
| ---- | ---- | ------- | ---- |
| 张三 | 男   | 89      | 56   |
| 李四 | 女   | 78      | 23   |

### 纵表

sql_v

| name | key  | value |
| ---- | ---- | ----- |
| 张三 | 性别 | 男    |
| 张三 | 语文 | 89    |
| 张三 | 数学 | 56    |
| 李四 | 性别 | 女    |
| 李四 | 语文 | 78    |
| 李四 | 数学 | 23    |

### 对比

| 对比项   | 横表                             | 纵表                               |
| -------- | -------------------------------- | ---------------------------------- |
| 数据量   | 一行表示一个实体                 | 多行表示一个实体                   |
| 业务描述 | 符合常规，易于理解               | 不直观                             |
| 代码方面 | 业务代码清晰，简洁明了           | 通常需要转换成横表，较复杂         |
| 性能方面 | 跟业务报表吻合，查询开销小       | 查询开销大                         |
| 字段类型 | 每个字段根据实际情况设计专用类型 | 值字段通常是varchar，兼容各种情况  |
| 数据冗余 | 有一定的冗余                     | 有一定的冗余                       |
| 可扩展性 | 对实体增加属性，需要动表结构     | 对实体增加属性，只需要增加记录即可 |

通过上面的对比，不难发现，相对于横表，纵表主要的优势是可扩展性强，适用于表结构不固定、经常扩展的场景，如电信行业的用户账单明细，金融行业的收支明细。

### 转换

```java
--横表转纵表 union all
select A.name, '性别' as 'key', A.sex as 'value' from sql_h A
union all
select A.name, '语文' as 'key', A.chinese as 'value' from sql_h A
union all
select A.name, '数学' as 'key', A.math as 'value' from sql_h A;
```

```java
--纵表转横表 
select
    A.name, 
	max(case when A.key='性别' then A.value end) as sex, 
	max(case when A.key='语文' then A.value end) as chinese, 
	max(case when A.key='数学' then A.value end) as math 
from sql_v A group by A.name;
```

## MySQL索引原理之B+Tree

```java
CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
    [index_type]
    ON tbl_name (key_part,...)
    [index_option]
    [algorithm_option | lock_option] ...

key_part: {col_name [(length)] | (expr)} [ASC | DESC]

index_option: {
    KEY_BLOCK_SIZE [=] value
  | index_type
  | WITH PARSER parser_name
  | COMMENT 'string'
  | {VISIBLE | INVISIBLE}
  | ENGINE_ATTRIBUTE [=] 'string'
  | SECONDARY_ENGINE_ATTRIBUTE [=] 'string'
}

index_type:
    USING {BTREE | HASH}

algorithm_option:
    ALGORITHM [=] {DEFAULT | INPLACE | COPY}

lock_option:
    LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}
```

参考：[MYSQL官网8.0文档](https://dev.mysql.com/doc/refman/8.0/en/create-index.html)

### 索引类型  

索引是存储引擎用于快速查找记录的一种数据结构  ，不同的存储引擎支持的索引类型不一样  ，MySQL中主要使用的索引结构类型是Hash和B+Tree。  

| 存储引擎    | 支持的索引类型 |
| ----------- | -------------- |
| InnoDB      | BTREE          |
| MyISAM      | BTREE          |
| MEMORY/HEAP | HASH, BTREE    |
| NDB         | HASH, BTREE    |

### 哈希索引

哈希索引基于哈希表实现，它根据给定的哈希函数Hash(Key)和处理冲突（不同索引列值有相同的哈希值）方法将每一个索引列值都映射到一个固定长度的地址，哈希索引只存储哈希值和行指针。
【结论】
1）哈希索引只支持等值比较，包括=、in()、<=>，查询速度非常快。
2）哈希索引不支持范围查询。  

![image-20210812071345842](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071345842.png)

### 二叉查找树  

二叉查找树，也称之为二叉搜索树、二叉排序树，它的每个节点最多有两个子节点，左子树上的节点值均小于它的根节点值，右子树上的节点值均大于它的根节点值，左右子树也分别是二叉排序树

【结论】
1）二叉查找树可以做范围查询。
2）但是极端情况下，二叉树会退化成线性链表，二分查找也会退化成遍历查找。  

![image-20210812071422258](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071422258.png)

### 红黑树

二叉查找树存在不平衡的问题，因此就有了自平衡二叉树，能够自动旋转和调整，让树始终处于平衡状态，常见的自平衡二叉树有红黑树和AVL树。红黑树是一种自平衡的二叉查找树。
【结论】
1）通过自平衡解决了二叉查找树有可能退化成线性链表的问题。
2）但是极端情况下，红黑树有“右倾”趋势，并没有真正解决树的平衡问题  

![image-20210812071511121](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071511121.png)



### 平衡二叉树

平衡二叉树，又称AVL树，指的是左子树上的所有节点的值都比根节点的值小，而右子树上的所有节点的值都比根节点的值大，且左子树与右子树的高度差最大为1。
【结论】
1）AVL树从根本上解决了红黑树的“右倾”问题，查找效率得到提升，无极端低效情况。
2）数据库查询的瓶颈在磁盘I/O，数据量很大的情况下，AVL树的高度会很高，查询需要更多I/O。  

![image-20210812071613266](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071613266.png)

### B-Tree

B-Tree，即B树（不要读成B减树），它是一种多路搜索树（多叉树），可以在平衡二叉树的基础上降低树的高度，从而提升查找效率。
【结论】
1）B树通过多叉、一个节点可有多个值，有效地控制了树的高度，比平衡二叉树查询效率高。
2）但是范围查询的效率仍然有待提升  

![image-20210812071840252](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071840252.png)

### B+Tree

B+Tree是B树的变体，比B树有更广泛的应用。
【结论】
1）B+Tree在叶子节点增加了有序链表，包含了所有节点，非常适合范围查询。
2）非叶子节点存在部分冗余。  

![image-20210812071923974](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812071923974.png)

参考：[数据结构在线模拟工具](https://www.cs.usfca.edu/~galles/visualization/Algorithms.html)

## MyISAM与InnoDB  

```java
//对两种引擎的表分别进行插入操作
insert into t_myisam(id, a, b) values(5, 2, 7);
insert into t_myisam(id, a, b) values(8, 11, 5);
insert into t_myisam(id, a, b) values(4, 9, 3);
insert into t_myisam(id, a, b) values(2, 5, 1);
insert into t_myisam(id, a, b) values(6, 0, 2);
insert into t_myisam(id, a, b) values(1, 4, 4);
insert into t_myisam(id, a, b) values(7, 3, 8);
insert into t_myisam(id, a, b) values(3, 6, 9);
//使用select * from table发现MyISAM中的数据会按照插入的顺序排序，InnoDB会按照主键顺序排序。
```



### MyISAM的索引结构

MyISAM存储引擎使用B+Tree作为索引结构，叶子节点的data域存放的是数据记录的地址。因此索引检索会按照B+Tree的检索算法检索索引，如果指定的Key存在，则取出其data域的值（地址），然后根据地址读取相应的数据记录。

![image-20210812073355495](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812073355495.png)

### InnoDB的索引结构

InnoDB存储引擎也使用B+Tree作为索引结构，索引的key是数据表的主键，叶子节点的data域保存了完整的数据记录。MyISAM索引文件和数据文件是分离的，索引文件仅保存数据记录的地址；InnoDB只有一个表数据文件，它本身就是主索引文件。  

其中主键索引data存放的是数据，第二索引data存放的是主键。

![image-20210812073959262](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812073959262.png)

### 复合索引的底层结构

假定，对people表创建复合索引(last_name, first_name, birthday) ，索引的多个值会按照定义索引时字段的顺序进行排序。

1. 复合索引先按照第一列 last_name 进行排序存储；当 last_name 相同时，则根据 first_name 进行排序；当 last_name 和 first_name 都相同时，则根据 birthday 进行排序。
2. 从图不难看出，该索引结构对于全值匹配、匹配最左前缀、匹配列前缀、匹配范围值、精确匹配某一列并范围匹配另外一列等类型的查询都是有效的。  

![image-20210812075212040](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812075212040.png)

### MySQL的索引分类  

其中，索引的顺序和数据记录的顺序(物理顺序)一致称为聚集索引，又称为聚簇索引。

![image-20210812075313482](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210812075313482.png)

## 降序索引  

在MySQL 8.0之前，索引都是按升序创建的，虽然语法上支持DESC，但创建的仍然是升序索引。  

```java
>CREATE INDEX indexName ON tableName(columnName1 asc, columnName2 desc, …);
```

如果某个查询需要对多个列进行排序（有降序、也有升序），并且排序条件与索引列不一致，或没有对排序列创建索引，数据库都会进行额外的排序filesort，此时就可以考虑使用降序索引进行优化。  

```java
//当不存在a desc,b asc这样的索引，数据库就会进行额外排序，使用explain中Extra：Using filesort
>select * from tableName order by a desc,b asc\G;

//创建索引加快查询效率
>create table t1(
	a int,
	b int,
	index a_desc_b_asc(a desc, b asc)
);
```

## like '%xxx'不会使用索引  

B+Tree索引可以用于在表达式中对字段进行比较，如=、>、>=、<、<=和Between。索引同样也可以用在LIKE语句中，只要参数不是以通配符开头的字符串。  

```java
//如：
SELECT * FROM tbl_name WHERE key_col LIKE 'Patrick%';
SELECT * FROM tbl_name WHERE key_col LIKE 'Pat%_ck%';
```

下面的SELECT语句不会用到索引：  

```java
SELECT * FROM tbl_name WHERE key_col LIKE '%Patrick';
SELECT * FROM tbl_name WHERE key_col LIKE '%Patrick%';
```

索引是一种有序的数据结构，**会根据索引字段顺序进行排序**，当字段开头模糊匹配无法使用索引查找

```java
//特例
explain select name from tableName where name like '%龙%';
```

## 列参与运算不会使用索引  

索引列作为表达式的一部分，或者将索引列作为函数的参数无法使用索引；

```java
//不会使用索引
#找出id=4的人
>select * from sql10_people where id+1=5;
#找出92年及之前生的人
>select * from sql10_people where year(birthday)<=1992;
```

```mysql
//会使用索引
#找出id=4的人
>select * from sql10_people where id=5-1;
#找出92年及之前生的人
>select * from sql10_people where birthday<=DATE_FORMAT('1992-12-31','%Y-%M-%d');
```

## 隐式类型转换会导致索引失效  

```mysql
create table sql11_people(
    id int not null auto_increment primary key,
    name varchar(30) comment '姓名',
    phone varchar(15) comment '联系电话'
)engine=InnoDB default charset=utf8;
create index idx_11_phone on sql11_people(phone);
```

对于SQL中的值为数值的字符串，不加单引号MySQL也不会报错，但是存在隐式类型转换会导致索引失效  

```mysql
#标准写法（会使用索引）
select * from sql11_people where phone='18733334444';
#非标准写法（全表扫描，不推荐）
select * from sql11_people where phone=18733334444;
#对于int类型的值加了单引号，会走索引但不推荐
select * from sql11_people where id='5'
```

## or条件会导致索引失效  

- 如果查询条件中使用了or，只要其中一个条件没有索引，其他条件（字段）有索引也不会使用 。or 和 and 不一样，添加复合索引 (id, name) 也不能解决问题 。
- 给or的每个字段单独添加索引或者使用union或union all解决这个问题。

```mysql
#当只有id有索引，name没有索引时
#不会使用索引
select * from sql_p where id='5' or name='张三';

#会使用id索引
select * from sql_p where id='5' 
union 
select * from sql_p where name='张三';
```

## 全文索引  

全文索引是搜索引擎的关键技术。

### 倒排索引  

倒排索引通常也称之为反向索引，它是搜索引擎主要使用的索引方式。倒排索引是一种面向单词的索引机制，每个文档都可以用一系列单词来表示，可以很方便地通过单词找到对应的文档。  

- 正向索引：文档-->单词
- 倒排索引：单词-->文档  

```mysql
#对以下文档内容建索引，得到的结果如下图。
MySQL is the most popular relational database on the internet.
I like MySQL very much.
```

![image-20210831072122404](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210831072122404.png)

### 全文索引的使用  

```mysql
CREATE TABLE ft_en(
    id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
    title VARCHAR(200),
    body TEXT,
    FULLTEXT (title,body)
) ENGINE=InnoDB;


INSERT INTO ft_en(title,body) VALUES
('MySQL Tutorial','DBMS stands for DataBase ...'),
('How To Use MySQL Well','After you went through a ...'),
('Optimizing MySQL','In this tutorial we will show ...'),
('1001 MySQL Tricks','1. Never run mysqld as root. 2. ...'),
('MySQL vs. YourSQL','In the following database comparison ...'),
('MySQL Security','When configured properly, MySQL ...');


#如果建表时未创建全文索引，也可以使用create fulltext index创建
CREATE FULLTEXT INDEX ft_title_body ON ft_en(title,body);
```

#### 三种模式  

MySQL支持三种模式的全文检索，自然语言模式、布尔模式和查询扩展模式。  

- 自然语言模式  

```mysql
#将搜索字符串解释为自然人类语言，除双引号外，没有特殊的运算符
#未指定查询模式或指定为 IN NATURAL LANGUAGE MODE，都表示自然语言搜索
SELECT * FROM ft_en WHERE MATCH (title, body) AGAINST ('database');
```

```mysql
#score越大表示相关度越高
select *,MATCH (title, body) AGAINST ('database') as score from ft_en;
```

![image-20210831074441897](img_MYSQL%E8%BF%9B%E9%98%B6/image-20210831074441897.png)

- 布尔模式  

```mysql
#使用特殊规则解释搜索字符串。该字符串包含要搜索的单词，还可以包含指定要求的运算符
#查询模式 IN BOOLEAN MODE 表示布尔搜索，+database -dbms表示有database没有dbms字样
SELECT * FROM ft_en WHERE MATCH (title, body) AGAINST ('+database -dbms' IN BOOLEAN MODE);
```

- 查询扩展模式  

```mysql
#是对自然语言搜索的修改。搜索字符串用于执行自然语言搜索，将搜索返回的最相关行中的单词添加到搜索字符串中，然后再次执行搜索
#IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION 或 WITH QUERY EXPANSION
SELECT * FROM ft_en WHERE MATCH (title, body) AGAINST ('database' WITH QUERY EXPANSION);
```

### 忽略的单词

在使用MySQL全文索引时，会发现有些单词检索不出结果，如for、how、in等，如：

```mysql
#查询结果为空
select * from ft_en where match(title,body) against('for');
```

主要原因有两个：

- InnoDB存储引擎默认只对长度>=3的单词建索引。查看参数设置： `SHOW VARIABLES LIKE 'innodb_ft%';`

- 使用了停用词。查看停用词表： `select * from information_schema.INNODB_FT_DEFAULT_STOPWORD;  `

### 查看分词结果

```mysql
SET GLOBAL innodb_ft_aux_table="liufeng/ft_en";
SELECT * FROM INFORMATION_SCHEMA.INNODB_FT_INDEX_CACHE;
```





















































