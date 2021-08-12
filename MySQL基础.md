# MySQL基础

SQL（Structure Query Language）结构化查询语言

- DQL（data query language）数据查询语言 select操作
- DML（data manipulation language）数据操作语言，主要是数据库增删改三种操作
- DDL（data defination language）数据库定义语言，主要是建表、删除表、修改表字段等操作
- DCL（data control language）数据库控制语言，如commit，revoke之类的

## 数据定义语言DDL

```java
create table score(
    id int not null auto_increment primary key, 
    name varchar(30) not null comment '学生姓名', 
    course varchar(50) comment '课程名称', 
    grade int comment '成绩' 
)engine=InnoDB default charset=utf8;
```

## 数据操作语言DML

```java
insert into score(name, course, grade) values('张三', '语文', 89);
```



## 数据查询语言DQL

### GROUP BY和HAVING的使用

#### 语法	

```java
//aggregate_function表示聚合函数
SELECT column_name, aggregate_function(column_name)
FROM table_name
WHERE column_name operator value
GROUP BY column_name1,column_name2,...
HAVING aggregate_function(column_name) operator value
```

#### 说明

1. group by可以对一列或多列进行分组

2. SELECT一般只能跟上分组字段和聚合函数
3. WHERE 关键字无法与聚合函数一起使用，HAVING 子句可以对分组后的各组数据进行筛选，也可以对聚合函数进行判断

### group_concat

group_concat配合group by一起使用，用于将某一列的值按指定的分隔符进行拼接，MySQL默认的分隔符为逗号。

#### 语法

```java
group_concat([distinct] column_name [order by column_name asc/desc ] [separator '分隔符'])
```

#### 实例

```java
>select * from employee;
id name dept
1 张三 部门A
2 李洁 部门C
3 李小梅 部门A
4 欧阳辉 部门C
5 李芳 部门A

>select dept,group_concat(name) from employee group by dept;
dept group_concat(name)
部门A  张三,李小梅,李芳   
部门C  李洁,欧阳辉
```

### distinct

distinct用于在查询中返回列的唯一不同值（去重复），支持单列或多列。

#### 语法

```java
//DISTINCT直接放在SELECT后面且DISTINCT后面跟的所有列都会加入去重，此时当column_name1, column_name2,...有任何一个不一样都会查出一行数据
SELECT DISTINCT column_name1, column_name2,... FROM table_name;
```

### 表连接

#### 语法

```java
//where
SELECT table1.column, table2.column 
FROM table1, table2 
WHERE table1.column1 = table2.column2;
```

#### 分类

- 内连接：使用where或 join、inner join，只连接匹配的行
- 外连接：
  - 左外连接，left join，包含左表的全部行（不管右表是否存在与之匹配的行），以及右表中全部匹配的行
  - 右外连接，right join，包含右表的全部行（不管左表是否存在与之匹配的行），以及左表中全部匹配的行
  - 全外连接，full join，包含左右两个表的全部行（不管在另一个表中是否存在与之匹配的行）
- 自连接：同一张表内的连接，相互连接的表在物理上同为一张表，但是逻辑上是多张表

交叉连接（cross join）：没有用where子句的交叉连接将产生笛卡尔积，第一个表的行数乘以第二个表的行数等于笛卡尔积
和结果集的大小。这种连接需要尽量避免。比如：`SELECT table1.column, table2.column FROM table1, table2; `

### 子查询

#### 子查询in

```java
//子查询in语法
SELECT column_name FROM table_name 
WHERE column_name IN(
 SELECT column_name FROM table_name [WHERE]
);
```

#### 子查询exists

EXISTS是子查询中用于测试内部查询是否返回任何行的布尔运算符。将主查询的数据放到子查询中做条件验证，根据验证结果（TRUE 或 FALSE）来决定主查询的数据结果是否保留。

```java
//where子句使用exists语法
SELECT t1.* 
FROM table_name1 t1
WHERE EXISTS (SELECT * FROM table_name2 t2 WHERE t1.column_name=t2.column_name);
```

```java
//查找有成绩的学生
>select A.* from student A where exists(select * from score B where A.stu_id=B.stu_id);

stu_id name address
 1     张三	贵州
 2     李四	陕西
```



## 数据控制语言DCL

### 用户管理

```java
#创建用户，用户创建后没有任何权限
>CREATE USER '用户名' [@'主机名'] [IDENTIFIED BY '密码'];
#注意：MySQL的用户账号由两部分组成：用户名和主机名，即用户名@主机名，主机名可以是IP或机器名称,主机名为%表示允许任何地址的主机远程登录MySQL数据库。
#删除用户
>DROP USER '用户名' [@'主机名'];
#修改密码
>ALTER USER '用户名'@'主机名' IDENTIFIED BY '新密码';
```

### 权限体系

MySQL的权限体系大致分为5个层级，全局层级、数据库层级、表层级、列层级和子程序层级。

- **全局层级** 适用于一个给定服务器中的所有数据库。这些权限存储在mysql.user表中。`GRANT ALL ON *.*`和`REVOKE ALL ON *.*`只授予和撤销全局权限。

- **数据库层级** 适用于一个给定数据库中的所有目标。这些权限存储在mysql.db和mysql.host表中。
  `GRANT ALL ON db_name.*`和`REVOKE ALL ON db_name.*`只授予和撤销数据库权限

- **表层级** 适用于一个给定表中的所有列。这些权限存储在mysql.talbes_priv表中。
  `GRANT ALL ON db_name.tbl_name`和`REVOKE ALL ON db_name.tbl_name`只授予和撤销表权限

- **列层级** 适用于一个给定表中的单一列。这些权限存储在mysql.columns_priv表中。当使用REVOKE时，您必须指定与被授权列相同的列

- **子程序层级** CREATE ROUTINE, ALTER ROUTINE, EXECUTE和GRANT权限适用于已存储的子程序。这些权限可以被授予为全局层级和数据库层级。而且，除了CREATE ROUTINE外，这些权限可以被授予为子程序层级，并存储在mysql.procs_priv表中。

MySQL的权限信息主要存储在以下几张表中，当用户连接数据库时，MySQL会根据这些表对用户
进行权限验证。

- **user** 用户权限表，记录账号、密码及全局性权限信息
- **db** 记录数据库相关权限
- **table_priv** 用户对某个表拥有的权限
- **column_priv** 用户对某表的某个列所拥有的权限
- **procs_priv** 用户对存储过程及存储函数的操作权限

### 权限管理

```java
#授权
>grant all privileges on databaseName.tableName to '用户名' [@'主机名'] ;
#撤销授权
>revoke all privileges on databaseName.tableName from '用户名' [@'主机名'] ;
#刷新权限
>FLUSH PRIVILEGES;
#查看权限
>show grants for '用户名' [@'主机名'] ;
```

### 权限列表

使用grant和revoke进行授权、撤销授权时，需要指定具体是哪些权限，这些权限大体可以分为3类，
数据类、结构类和管理类。

- **数据** 	SELECT	INSERT	UPDATE	DELETE	FILE
- **结构**	CREATE	ALTER	INDEX	DROP	CREATE TEMPORARY TABLES	SHOW VIEW	CREATE ROUTINE	ALTER ROUTINE	EXECUTE	CREATE VIEW	EVENT	TRIGGER
- **管理**	USAGE	GRANT	SUPER	PROCESS	RELOAD	SHUTDOWN	SHOW DATABASES	LOCK TABLES	REFERENCES	REPUCATION CUENT	REPUCATION SLAVE	CREATE USER

### 禁止root用户远程登录

```java
#保证用户表中不存在root@%用户即可
>use mysql;
>select user,host from user;
```

### 忘记root密码

windows+mysql8

```java
#关闭权限验证
#mysqld --defaults-file="C:\ProgramData\MySQL\MySQL Server 8.0\my.ini" --console --skip-grant-tables --shared-memory
```

说明：参数--defaults-file的值为配置文件my.ini的完整路径。



MySQL关闭权限验证后，直接通过 mysql 命令即可连接到数据库，并可正常执行各类操作。

```java
#刷新权限
>FLUSH PRIVILEGES;
#修改root用户的密码
>ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
```



## 索引

索引类似**目录**，是对数据库表中一列或多**列的值**进行**排序**的一种结构，使用索引可快速**查询**数据库表中的特定记录。如果不加索引，查找任何一条特定的数据都会进行一次全表扫描。

### 索引种类

- **普通索引** 最基本的索引，没有任何限制，仅加速查询。
- **唯一索引** 索引列的值必须唯一，但允许有空值。
- **主键索引** 一种特殊的唯一索引，不允许有空值。一般是在建表的同时自动创建主键索引。
- **复合索引** 两个或多个列上的索引被称作复合索引。
- **全文索引** 对文本内容进行分词索引。

### 索引使用

```java
1、创建索引
# 创建普通索引
>CREATE INDEX indexName ON tableName(columnName(length)); 
# 创建唯一索引
>CREATE UNIQUE INDEX indexName ON tableName(columnName(length)); 
# 创建复合索引
>CREATE INDEX indexName ON tableName(columnName1, columnName2, …);

2、删除索引
>DROP INDEX [indexName] ON tableName; 

3、查看索引
>SHOW INDEX FROM tableName;
```

```java
>create table tableName(
	a int,
    ...
    index indexName(columnName1, columnName2, …)
);

>show create table tableName\G;
CTEATE TABLE tableName(
	a int(11) DEFAULT NULL,
    ...
    KEY indexName(columnName1, columnName2, …)
)engine=InnoDB default charset=utf8;
```



### 建立索引注意点

- 选择区分度高的列建立索引
- 每次查询每张表仅能使用一个索引
- 避免对索引列进行计算，例如：对create_time列建立索引，但是`from_unixtime(create_time)='2014-05-29'`不会用到索引

### 复合索引前导列特性

在MySQL中，如果创建了复合索引(name, salary, dept)，就相当于创建了(name, salary, dept)、(name, salary)和(name)三个索引，这被称为复合索引前导列特性，因此在创建复合索引时应该将最常用作查询条件的列放在最左边，依次递减。

```java
#未使用索引
>select * from employee where salary=8800;
>select * from employee where dept='部门A';
>select * from employee where salary=8800 and dept='部门A';
#使用索引
>select * from employee where name='liufeng';
>select * from employee where name='liufeng' and salary=8800;
>select * from employee where name='liufeng' and salary=8800 and dept='部门A';
>select * from employee where name='liufeng' and dept='部门A';#这里的name也使用了索引
```

### 覆盖索引

**查询索引列**

覆盖索引中，select的数据列只从索引中就能得到，不用再扫描数据表，也就是只需扫描索引就可以得到查询结果。但是并非所有类型的索引都可以作为覆盖索引，覆盖索引必须要存储索引列的值。像哈希索引、空间索引、全文索引等并不会真正存储索引列的值。



当一个查询使用了覆盖索引，在查询分析器EXPLAIN的Extra列可以看到`“Using index”` 。



## 视图

视图是一个**虚拟表**，其内容由**select查询语句**定义。和真实的表一样，视图也包含行和列，对视图的操作与对表的操作基本一致。视图中的数据是在使用视图时动态生成，**视图中的数据都存储在基表中**。  视图表的数据变化会影响到基表，基表的数据变化也会影响视图表。  其中单表创建的视图可以直接修改视图，数据变化会影响到原表数据，但是多表创建的视图不可以修改数据

- 可读性  ：简化了复杂的查询，使复杂的查询更易于理解和使用  
- 重用性  ：视图是对复杂查询语句的封装，对数据库重构，不会影响程序的运行  
- 安全性  ：视图可以隐藏一些敏感的信息，可以把权限限定到行列级别  

```java
#创建视图
CREATE VIEW view_name AS SELECT…;
#修改视图
ALTER VIEW view_name AS SELECT…;
#查看视图创建语句
SHOW CREATE VIEW view_name;
#查看有哪些视图
SHOW TABLE STATUS WHERE comment='view';
#删除视图
DROP VIEW view_name;
```







## 事务

事务（Transaction）是指作为一个逻辑工作单元执行的一系列操作，这些操作要么全部成功，要么全部失败。事务确保对多个数据的修改作为一个单元来处理。

- 在MySQL中，只有使用了Innodb存储引擎的数据库或表才支持事务。
- 事务用于维护数据库的完整性，保证成批的sql语句要么都执行，要么都不执行。
- 事务用于管理INSERT、UPDATE和DELETE语句，不会管理SELECT语句。



例如，张三在ATM机上给李四转账100元，在银行的业务系统中，主要会执行两步数据变更操作：
①从张三的账户减去1000-100元；
②给李四的账户增加0+100元。
试问，如果操作①执行成功，操作②执行失败会发生什么情况？

### 事务特性

事务拥有ACID四个特性，即Atomicity（原子性）、Consistency（一致性）、Isolation（隔离性）和Durability（持久性）。

- **原子性** 事务必须是原子工作单元，事务中包含的各操作要么都做，要么都不做
- **一致性** 事务在执行完成时，必须使所有的数据都保持一致状态。如上例数据要么是1000和0，要么是900和100
- **持续性** 事务执行完成之后，它对系统的影响是永久性的。比如不存在断电数据消失
- **隔离性** 事务独立运行。多个事务之间相互隔离，互不干扰。事务的100%隔离，会牺牲速度

### 事务控制

在默认情况下，MySQL是自动提交事务的，即每一条INSERT、UPDATE、DELETE的SQL语句提交后会立即执行COMMIT操作。因此，要开启一个事务，可以使用start transaction或begin，或者将autocommit的值设置为0

```java
#使用start transaction或begin开启事务。
>start transaction
>begin
#回滚到事务之前
>rollback;    
#提交
>commit;    
```

```java
#默认情况下，autocommit的值为1，表示自动提交事务
>select @@autocommit;
>set autocommit=0;
```

### 隔离级别

- Read Uncommitted（未提交读）
- Read Committed（已提交读）：大多数据库默认事务隔离级别，比如oracle、sqlserver
- Repeatable Read（可重复读）：MySQL的默认事务隔离级别
- Serializable（可串行化）

事务一般隔离级别使用已提交读，代码中个别问题使用悲观锁和乐观锁处理，但是事务不会管理SELECT语句，所以即便是可串行化，也不能保证读取数据更新数据操作的原子性

#### 读取数据问题

-  脏读(Drity Read)
- 不可重复读(Non-repeatable read)
- 幻读(Phantom Read)

参考：[脏读、幻读、不可重复读](https://www.cnblogs.com/l-y-h/p/12458777.html#_label0_3)

#### 操作

```java
//设定隔离级别为READ-UNCOMMITTED
>SET tx_isolation='READ-UNCOMMITTED';
//查询隔离级别
>select @@tx_isolation;    
```



## 日志

日志用于记录数据库的运行情况，以及用户对数据库执行的各类操作。当数据库发生故障时，可以根据日志分析和解决问题，从而对数据库进行恢复。

- **重做日志（redo log）** 

  重做日志是一种物理格式的日志，记录的是物理数据页面的修改的信息，其redo log是顺序写入redo log file的物理文件中去的。

- **回滚日志（undo log）**

  回滚日志是一种逻辑格式的日志，在执行undo的时候，仅仅是将数据从逻辑上恢复至事务之前的状态，而不是从物理页面上操作实现的，这一点是不同于redo log的。

- **二进制日志（binlog）**

  二进制日志是一种逻辑格式的日志，以二进制文件的形式记录了数据库中的操作，但不记录查询语句。

- **错误日志（errorlog）** 

  错误日志记录着mysqld启动和停止，以及服务器在运行过程中发生的错误的相关信息。

- **慢查询日志（slow query log）** 

  慢查询日志记录执行时间过长和没有使用索引的查询语句。

- **一般查询日志（general log）**

  记录了服务器接收到的每一个查询或是命令，无论这些查询或是命令是否正确甚至是否包含语法错误，general log都会将其记录下来。

- **中继日志（relay log）** 

  中继日志类似二进制；可用于复制架构中，使从服务器和主服务器的数据保持一致

### 慢查询日志

用于记录MySQL数据库中响应时间超过指定阈值的语句。它不仅仅只针对**SELECT**语句，像**INSERT、UPDATE、DELETE**等语句，只要响应时间超过所设定阈值都会记录在慢查询日志中。

#### 日志参数

- **slow_query_log** 是否开启慢查询日志，1表示开启，0表示关闭。
- **slow_query_log_file** 慢查询日志存储路径，可选。注意：MySQL 5.6之前的版本，参数名为 log-slow-queries
- **long_query_time** 阈值，当SQL语句的响应时间超过该阈值就会被记录到日志中
- **log_queries_not_using_indexes** 未使用索引的查询也被记录到慢查询日志中，可选。
- **log_output** 日志存储方式，默认为FILE。
  - log_output=‘FILE’表示将日志存入文件
  - log_output=‘TABLE’表示将日志存入数据库
  - log_output=‘FILE,TABLE’表示同时将日志存入文件和数据库

```java
#查看是否开启慢查询日志,其中show variables可以查看mysql中参数值
>show variables like 'slow%';
#临时开启慢查询日志
>set slow_query_log='ON';
>set long_query_time=1;
#慢查询日志文件所在位置
>show variables like '%datadir%';
```

### binlog日志

MySQL的二进制日志binlog可以说是MySQL最重要的日志，它记录了所有的DDL和DML语句（除了数据查询语句select），以事件形式记录，还包含语句所执行的消耗的时间，MySQL的二进制日志是事务安全型的。

万一遇到数据丢失的紧急情况下，可以使用binlog日志进行数据恢复（定时全备份+binlog日志恢复增量数据部分）。

```java
#查看binlog日志参数
>show variables like 'log_bin%'  
log_bin								ON
log_bin_basename  					/usr/...	
log_bin_index						/usr/...
log_bin_trust_function_creators		OFF
log_bin_use_v1_row_events			OFF
```



```java
#查看所有二进制日志列表
show master logs;
#查看正在使用的二进制日志
show master status;
#刷新日志（重新开始新的binlog日志文件）
flush logs

#binlog日志是二进制文件不能直接打开，需要用如下方式查看    
#查询指定的binlog
show binlog events in 'WQ-20160826MDKU-bin.000050' from 10668\G;

#导出sql在查看，同时可以用sql文件恢复误删数据
#(半夜备份文件，先找到日志中半夜行数到当前行数，导出sql文件，在使用source命令导入)    

mysqlbinlog "C:\ProgramData\MySQL\MySQL Server 8.0\Data\WQ-20160826MDKU-bin.000057" --
start-position 528 --stop-position 1191 > d:\backup\test.sql
#sql文件本质上就是数据库语句集    
```



## 优化

### 查询分析器explain

explain命令可以查看SQL语句的执行计划。MySQL解释了它将如何处理语句，包括有关如何联接表以及以何种顺序联接表的信息。

explain的使用很简单，只需要在SQL语句之前加上explain命令即可。

#### 参数

- id	执行select子句或操作表的顺序
- select_type	查询的类型，如SIMPLE、PRIMARY、SUBQUERY、DERIVED、UNION等
- table	当前行使用的表名
- partitions	匹配的分区
- **type** 	连接类型，如system、const、eq_ref、ref、range、index、all等，越往后语句越差
- possible_keys	可能使用的索引
- **key**	实际使用的索引，NULL表示未使用索引
- key_len	查询中使用的索引长度
- ref	列与索引的比较
- **rows**	扫描的行数
- **filtered**	选取的行数占扫描的行数的百分比，理想的结果是100
- extra	其他额外信息

## 分区表

分区表就是按照某种规则将同一张表的数据分段划分到多个位置存储。对数据的分区存储提高了数据库的性能，被分区存储的数据在**物理上是多个文件， 但在逻辑上仍然是一个表**，对表的任何操作都跟没分区之前一样。在执行增、删、改、查等操作时， 数据库会自动找到对应的分区，然后执行操作。

- MySQL从5.1.3开始支持分区（ Partition）。
- 在MySQL 8.0中，只有InnoDB和NDB两个存储引擎支持分区  

优点：

- 存储更多  ：与单个磁盘或文件系统分区相比，可以存储更多的数据。   
- 便于管理  ：很容易根据分区删除失去保存意义的历史数据  
- 提升查询效率  ：一些查询可以极大地优化，查询仅从某个或某几个分区中获取数据。   
- 并行处理  ：涉及到sum()、 count()等聚合函数的查询，可以很容易进行并行处理  
- 提高查询吞吐  ：通过跨多个磁盘来分散数据查询，来获得更大的查询吞吐量  

### 分区类型

MySQL支持的分区类型包括Range、 List、 Hash和Key，其中Range最常用。  

- Range分区  ：允许将数据划分不同范围。例如可以将一个表通过年份划分成若干个分区  
- List分区  ：允许系统通过预定义的列表的值来对数据进行分割  
- Hash分区   ：允许通过对表的一个或多个列的HashKey进行计算，最后通过这个Hash码不同数值对应的数据区域进行分区。  
- Key分区  ：对Hash模式的一种延伸，这里的HashKey是MySQL系统产生的。  

### Range分区

Range分区是基于属于一个给定连续区间的列值，把多行分配给分区。**范围值**

```java
create table user_range(
    id int not null auto_increment,
    name varchar(30) ,
    age int,
    birthday date,
    province int,
    //主键必须包含分区字段
    primary key(id, age)
)
partition by RANGE(age) (
    partition p1 VALUES LESS THAN (20) DATA DIRECTORY = 'c:/data/p1',
    partition p2 VALUES LESS THAN (40) DATA DIRECTORY = 'c:/data/p2',
    partition p3 VALUES LESS THAN (60) DATA DIRECTORY = 'c:/data/p3',
    partition p4 VALUES LESS THAN MAXVALUE DATA DIRECTORY = 'c:/data/p4'
);  
```

### List分区

List分区是基于列值匹配一个离散值集合中的某个值来进行选择。**一系列值**

```java
create table user_list(
    id int not null auto_increment,
    name varchar(30) ,
    age int,
    birthday date,
    province int,
    primary key(id, province)
)
partition by List(province) (
    partition p1 VALUES IN (1,3,5,7,9,11,13,15,17,19,21),
    partition p2 VALUES IN (2,4,6,8,10,12,14,16,18,20,22),
    partition p3 VALUES IN (23,24,25,26,27,28,29,30,31,32,33,34)
);  
```

### Hash分区

Hash分区是基于用户定义的表达式的返回值来进行选择的分区。

```java
create table user_hash(
    id int not null auto_increment,
    name varchar(30) ,
    age int,
    birthday date,
    province int,
    primary key(id, birthday)
)
partition by HASH(YEAR(birthday))
partitions 5;  
```

### Key分区

Key分区类似于Hash分区，但这里的Hash Key是由MySQL系统产生的。

```java
create table user_key(
    id int not null auto_increment,
    name varchar(30) ,
    age int,
    birthday date,
    province int,
    primary key(id, age)
)
partition by KEY (age)
partitions 5;  
```

### 分区操作

```java
#新增分区
alter table `user` add partition(partition p5 VALUES LESS THAN MAXVALUE);

#对已存在的表进行分区
alter table `user` partition by RANGE(age) (
    partition p1 VALUES LESS THAN (20) DATA DIRECTORY = 'c:/data/p1',
    partition p2 VALUES LESS THAN (40) DATA DIRECTORY = 'c:/data/p2',
    partition p3 VALUES LESS THAN (60) DATA DIRECTORY = 'c:/data/p3',
    partition p4 VALUES LESS THAN MAXVALUE DATA DIRECTORY = 'c:/data/p4'
);

#删除分区（分区下的数据也会被删除）
alter table `user` drop partition p5;

#移除分区（数据不会被删除）
ALTER TABLE `user` REMOVE PARTITIONING ;
```



## 存储过程

存储过程（ Stored Procedure）是为了完成特定功能的**SQL语句集**，经编译创建并保存在数据库中，用户可通过指定存储过程的名字并给定参数(需要时)来调用执行，类似于编程语言中的方法或函数。  

**存储过程的优点：**

- 存储过程是对SQL语句的封装，增强可复用性
- 存储过程可以隐藏复杂的业务逻辑、商业逻辑
- 存储过程支持接收参数，并返回运算结果

**存储过程的缺点：**

- 存储过程的可移植性较差，如果更换数据库，要重写存储过程
- 存储过程难以调试和扩展
- 无法使用Explain对存储过程进行分析
- 《阿里巴巴Java开发手册》 中禁止使用存储过程  

```java
#存储过程定义：求两数之和
#delimiter声明语句结束符
delimiter //
#in定义入参 out定义出参    
create procedure my_sum(in a int, in b int, out result int)
#存储过程begin开始与end结束
begin
	set result = a + b;
end
//
delimiter ;

#存储过程调用
call my_sum(10, 20, @result);
select @result;
```

```java
#存储过程定义：计算1+2+...+n的和
delimiter //
create procedure my_n_sum(in n int, out result int)
begin
    declare i int default 1;
    declare sum int default 0;
    while i<=n do
        set sum = sum + i;
        set i = i + 1;
    end while;
    set result = sum;
end;
//
delimiter ;
```

```java
drop table if exists user_info;
drop table if exists email_info;

create table user_info(
    id int not null auto_increment primary key,
    name varchar(30),
    email varchar(50)
);

insert into user_info(id, name, email) values(1, '柳峰', 'liufeng@qq.com');
insert into user_info(id, name, email) values(2, '张三', 'zhangsan@qq.com');

create table email_info(
    id int not null auto_increment primary key,
    email varchar(50),
    content text,
    send_time datetime
);


#存储过程示例：根据用户id和邮件内容content给用户发邮件
delimiter //
create procedure send_email(in user_id int, in content text)
begin
    /* 根据用户id查询邮箱email */
    set @user_email=(select email from user_info where id=user_id);
    /* 模拟发送邮件 */
    insert into email_info(email, content, send_time) values(@user_email, content, now());
end;
//
delimiter ;



call send_email(1, '欢迎加入MySQL阵营！ ');
```



## 触发器

触发器（ trigger）用于监视某种情况并触发某种操作，它是与**表事件**相关的特殊的存储过程， 它的执行不是由程序调用，而是由事件来触发。 例如，当对某张表进行**insert、 delete、 update操作**时就会触发执行它。  

```java
#创建触发器语法
CREATE TRIGGER trigger_name trigger_time trigger_event ON table_name FOR EACH ROW trigger_stmt
```

参数说明：

- trigger_name：触发器名称
- trigger_time：触发时间，取值有before、 after
- trigger_event：触发事件，取值有insert、 update、 delete
- table_name：触发器监控的表名
- trigger_stmt：触发执行的语句，可以使用OLD、 NEW来引用变化前后的记录内容
- NEW.columnName：获取INSERT触发事件中新插入的数据
  OLD.columnName：获取UPDATE和DELETE触发事件中被更新、删除的数据  

```java
drop table if exists user_info;
drop table if exists email_info;

create table user_info(
    id int not null auto_increment primary key,
    name varchar(30),
    email varchar(50)
);

insert into user_info(id, name, email) values(1, '柳峰', 'liufeng@qq.com');
insert into user_info(id, name, email) values(2, '张三', 'zhangsan@qq.com');

create table email_info(
    id int not null auto_increment primary key,
    email varchar(50),
    content text,
    send_time datetime
);

#当有新用户插入时，自动给用户发送邮件。
delimiter //
#触发时间AFTER  监控的表名user_info
CREATE TRIGGER send_email_trigger AFTER INSERT ON user_info FOR EACH ROW
BEGIN
	#获取新插入的数据NEW.email
	insert into email_info(email, content, send_time) values(NEW.email, '欢迎加入MySQL阵营！ ', now());
END
//
delimiter ;
```

## 表常用操作

### 复制表

```java
#只复制表结构，包括主键、索引，但不会复制表数据
create table tableName like someTable;
#复制表的大体结构及全部数据，不会复制主键、索引等
create table tableName select * from someTable;
#完整复制，分两步完成，先复制表结构，再插入数据
create table tableName like someTable;
insert into tableName select * from someTable;
```

### 导出数据

#### SELECT...INTO OUTFILE

在MySQL中，可以使用SELECT...INTO OUTFILE语句将查询结果数据导出到文本文件。

```java
#文件存储路径
SELECT * FROM employee INTO OUTFILE 'D:\\employee.txt' 
#字段间分隔符
FIELDS TERMINATED BY ','  
#值用双引号引起
ENCLOSED BY '"' 
#行间分隔符,\r\n在mysql中表示换行
LINES TERMINATED BY '\r\n';
```

```java
#mysql中secure_file_priv参数控制导出
#null 表示不允许导入导出		空 表示没有任何限制		指定路径 表示导入导出只能在指定路径下完成    
show variables like "secure_file_priv";    
```

#### mysql命令

通常，我们使用mysql命令连接数据库，mysql命令有一个-e选项，可以执行指定的SQL语句，再结合DOS的重定向操作符”>”可以将查询结果导出到文件

```java
mysql -h localhost -u root -p -D mydb -e "select * from employee" > E:\employee.txt
```

#### mysqldump命令

mysqldump是MySQL用于转存储数据库的实用程序，它主要产生一个SQL脚本，其中包含创建数据库、创建数据表、插入数据所必需的SQL语句。

```java
# 导出mydb数据库（含数据）
mysqldump -h localhost -u root -p mydb > d:/mydb.sql
# 导出mydb数据库（不含数据）
mysqldump -h localhost -u root -p mydb --no-data > d:/mydb.sql
# 导出mydb.employee数据表
mysqldump -h localhost -u root -p mydb employee > d:/employee.sql
# 导出mydb数据库，忽略contacts表
mysqldump -h localhost -u root -p mydb --ignore-table mydb.contacts > d:/employee.sql
```

### 定时备份数据库

定时调度器-->数据库备份脚本

定时调度器有：

- Windows的定时计划
- Linux的Crontab
- 编程语言的Timer

数据库备份脚本有：

- DOS批处理脚本
- Linux Shell脚本

#### Windows

schtasks.exe用于安排命令和程序在指定时间内运行或定期运行，它可以从计划表中添加和删除任务、按需要启动和停止任务、显示和更改计划任务。

```java
#备份数据库的脚本mysql_mydb_backup.bat
mysqldump -h localhost -uroot -p123456 mydb > d:\backup\mydb.sql
#创建计划任务（每隔指定时间备份一次MySQL）
schtasks /create /sc minute /mo 1 /tn 定期备份MySQL /tr d:\backup\mysql_mydb_backup.bat
#删除计划任务
schtasks /delete /tn 定期备份MySQL
```

#### Linux

```java
#!/bin/bash
#备份目录
backup_dir=/home/liufeng/backup
#备份文件名
backup_filename=“mydb-`date +%Y%m%d`.sql“
#进入备份目录
cd $backup_dir
#备份数据库
mysqldump -h localhost -uroot -p123456 mydb > ${backup_dir}/${backup_filename}
#删除7天以前的备份
find ${backup_dir} -mtime +7 -name "*.sql" -exec rm -rf {} \;
```

crontab是一个命令，常见于Unix和类Unix的操作系统之中，用于周期性执行任务。

```java
#每天凌晨01:30执行shell脚本（备份数据库）
30 1 * * * bash /home/liufeng/backup/mysql_mydb_backup.sh
```

### 导入数据

#### LOAD DATA

在MySQL中，可以使用LOAD DATA语句将文本文件数据导入到对应的数据库表中，可以将LOAD DATA语句看成是SELECT…INTO OUTFILE的反操作

```java
#数据文件位置
>LOAD DATA INFILE 'D:\\employee.txt' INTO TABLE employee character set utf8 
#字段间分隔符
>FIELDS TERMINATED BY ',' 
#值用双引号引起    
>ENCLOSED BY '"' 
#行间分隔符    
>LINES TERMINATED BY '\r\n';
```

#### source命令

在MySQL中，可以使用source命令导入较大的SQL文件。source命令可以导入使用mysqldump备份的sql文件。

```java
>source d:/mydb.sql
```

## 字符集

**字符（Character）**是各种文字和符号的总称，包括各国家文字、标点符号、图形符号、数字等。
**字符集（Character set）**是多个字符的集合，字符集种类较多，每个字符集包含的字符个数不同，常见的字符集有ASCII、GB2312、GBK、 GB18030、Unicode等。计算机要准确的处理各种字符集文字，就需要进行字符编码，以便计算机能够识别和存储各种文字。

- **ASCII** 最简单的西文编码方案，主要用于显示现代英语和其他西欧语言。

  使用1个字节表示，可表示128个字符。

- **GB2312** 国家标准简体中文字符集，兼容ASCII。

  使用2个字节表示，能表示7445个符号，包括6763个汉字，几乎覆盖所有高频率汉字。

- **GBK** GB2312的扩展，加入对繁体字的支持，兼容GB2312。

  使用2个字节表示，可表示21886个字符。

- **GB18030** 解决了中文、日文、朝鲜语等的编码，兼容GBK。

  采用变字节表示(1 ASCII，2，4字节)。可表示27484个文字。

- **Unicode** Unicode是国际标准编码字符集，为世界650种语言进行统一编码，兼容ISO-8859-1。
  Unicode字符集有多个编码方式，分别是UTF-8，UTF-16和UTF-32。

```java
#查看mysql数据库支持的字符集
>show character set;
```

### 设置字符集

```java
1、数据库
# 创建数据库时指定字符集,COLLATE为排序使用的字符集
CREATE DATABASE databaseName CHARSET utf8 COLLATE utf8_general_ci;
# 查看数据库的字符集
SHOW CREATE DATABASE databaseName;
2、表
# 创建表时指定字符集
CREATE TABLE tableName(…) DEFAULT CHARSET=utf8;
# 查看数据库的字符集
SHOW CREATE TABLE tableName;
3、字段
CREATE TABLE tableName(…, name varchar(50) not null CHARSET utf8, …);
```

```java
#举例
>create table t1(name varchar(30)) default charset=gb2312
#gb2312不支持繁体字，这里会报错，需要换成gbk或者unicode编码等    
>insert into t1 values('陶喆')    
```

## 预处理

从MySQL 4.1开始，就支持预处理语句（Prepared statement），这大大提高了客户端和服务器端数据传输的效率。当创建一个预定义SQL时，客户端向服务器发送一个SQL语句的原型；服务器端接收到这个SQL语句后，解析并存储这个SQL语句的**部分执行计划**，返回给客户端一个SQL语句处理句柄，以后每次执行这条SQL，客户端都指定使用这个句柄。

- **高效执行重复SQL**  在服务器端只需要解析一次SQL，在服务器端某些优化器的工作只需要执行一次，它会缓存一部分执行计划
- **减少网络开销** 对于重复执行的SQL，后续只需要将参数发送到服务器端，而不是整个SQL语句，因此网络开销会更小。
- **更加安全** 使用预处理语句，无须在应用程序中处理转义，也大大减少了SQL注入和攻击的风险

### 基本使用

MySQL支持SQL接口的预处理，即不使用二进制传输协议也可以直接以SQL的方式使用预处理。预处理的语法如下。

```java
# 定义预处理语句
PREPARE stmt_name FROM preparable_stmt;
# 执行预处理语句
EXECUTE stmt_name [USING @var_name [, @var_name] ...];
# 删除(释放)定义
{DEALLOCATE | DROP} PREPARE stmt_name;
```

```java
>prepare pre_employee from 'select * from employee where name=?';
>set @name1='张三'
>execute pre_employee using @name1;    
```

## 查询缓存(过期)

很多数据库产品都能够缓存查询的执行计划，对于相同类型的SQL就可以跳过SQL解析和执行计划分成阶段。MySQL在某种场景下也可以实现，但是MySQL还有另一种不同的缓存类型：**缓存完成的SELECT查询结果**，也就是查询缓存。
MySQL将缓存存放在一个引用表中，类似于HashMap的数据结构，Key查询SQL语句，Value则是查询结果。当发起查询时，会使用SQL语句去缓存中查询，如果命中则立即返回缓存的结果集

```java
# 查询缓存参数，8.0中数值为no表示已弃用
show variables like "%query_cache%";
```

- 可以使用 SQL_NO_CACHE 在 SELECT 中禁止缓存查询结果，例如：SELECT SQL_NO_CACHE ...
- MySQL 8.0已删除查询缓存功能

## SQL注入

SQL注入（SQL Injection）是指应用程序对用户输入数据的合法性没有判断、没有过滤，攻击者可以在应用程序中通过表单提交特殊的字符串，该特殊字符串会改变SQL的运行结果，从而在管理员毫不知情的情况下实现非法操作，以此来实现欺骗数据库执行非授权的任意查询。

**广泛性** 任何一个基于SQL语言的数据库都可能受到SQL注入攻击。很多开发人员都为了省事不对表单参数进行校验。

**隐蔽性** SQL注入语句一般都嵌入在普通的HTTP请求中，很难与正常语句区分开，SQL注入也有很多变种。

**操作简单** 互联网上有很多SQL注入工具，简单易懂，攻击过程简单，不需要太多专业知识

**危害大** 攻击者通过SQL注入能够获取到更多数据，如管理员密码、整个系统的用户数据、他人的隐私数据、完整的数据库。

```java
#账号密码：adc/' or '1'='1，如下会查出所有数据
select * from user where username='abc' and password='' or '1'='1';
```

## 文件编码

在Windows中，MySQL 8.0的配置文件my.ini的编码为ANSI，但是修改配置文件后默认保存的编码为UTF-8，这会导致MySQL解析配置文件错误，无法启动。只需要将配置文件另存为ANSI编码即可。

## 文档存储

在关系数据库中，需要先定义表才能存储数据。文档存储更加灵活，不需要事先定义数据结构、数据约束等就可以直接存储数据。将MySQL用作文档存储时，集合是容器，集合包含可以添加、查找、更新和删除的JSON文档

| 关系数据库 | MySQL文档存储 | 说明    |
| ---------- | ------------- | ------- |
| database   | database      | 数据库  |
| table      | collection    | 表/集合 |
| row        | document      | 行/文档 |



表中

| id   | name | age  |
| ---- | ---- | ---- |
| 1    | 张三 | 10   |

文档中

{
    "id":1,
    "name":"张三",
    "age":"10"
}

```java
#使用MySQL Shell连接数据库（支持文档存储）
mysqlsh root@localhost:33060/mydb
#查看当前数据库
db
#查看当前数据库有哪些集合
db.getCollections()
#创建集合
db.createCollection("employee_doc")
#删除集合
db.dropCollection("employee_doc")
#添加文档
db.employee_doc.add({
    "id":1,
    "name":"张三",
    "sex":"男",
    "salary":5500,
    "dept":"部门A"
})
#查询文档
db.employee_doc.find("name='张三'")
#删除文档
db.employee_doc.remove("name='张三'")
#删除所有文档
db.employee_doc.remove("true")
```



## MySQL8.0的新特性

| 新特性/改进                | 描述                                                         |
| -------------------------- | ------------------------------------------------------------ |
| 默认字符集变为utf8mb4      | 移动端有大量的表情符号需要存储，默认的字符集从 latin-1 转为 utf8mb4。 |
| 系统表存储引擎全部为InnoDB | 不再采用MyISAM。                                             |
| DDL原子化                  | 在MySQL 8.0之前，DDL操作是非原子型操作，在执行过程中遇到实例故障重启，可能导<br/>致DDL没有完成也没有回滚。 |
| 持久化系统参数             | 可以用 SET PERSIST 来设置持久性的全局变量，即便服务器重启也会保持 |
| 不可见索引                 | 可以将一些索引设置为不可见，这样 SQL 优化器就不会用到它，但是它会继续在后台保持更新。当有需要时，可以随时恢复可见。不可见的索引可以测试删除索引对查询性能的影响，而无需进行破坏性的更改 |
| 全新的身份认证方式         | 身份认证方式由以前的mysql_native_password改为caching_sha2_password |
| 通用表表达式               | 通用表表达式（CTE）是一个在语句级别定义的临时结果集。定义之后，可以在当前语句中多次引用该 CTE。CTE有两种用法，非递归的CTE和递归的CTE。 |
| 窗口函数                   | 类似于聚合函数，可降低代码复杂性并帮助开发人员提高工作效率   |
| JSON扩展                   | 从版本5.7.8开始，MySQL开始支持JSON数据类型。MySQL8.0新增了JSON_TABLE()函数，可以将JSON数据转换成表 |
| GIS增强                    | GIS得到了增强，可支持地理和空间参考系统（SRS）。             |
| 文档存储                   | 可以使用同一种解决方案处理 SQL 和 NoSQL，也可以将两种的优势结合起来 |

### 问题

#### 身份认证方式

老版本的Navicat连接能正常连接MySQL 5.x，但是连接MySQL 8.0却报错，错误提示：

```java
Client does not support authentication protocol requested by server;consider upgrading MySQL client
客户端不支持服务器请求的身份验证协议；请考虑升级MySQL客户端    
```

MySQL 5.x的身份认证方式为 mysql_native_password，也就是Navicat客户端支持的认证方式。但是MySQL 8.0升级了身份认证方式，默认为 caching_sha2_password。因此，在不升级Navicat版本的情况下，可以将MySQL 8.0的身份认证方式修改为 mysql_native_password。

```java
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
flush privileges;
```

#### 编码格式

MySQL 8.0将utf8mb4作为默认字符集，mb4是most bytes 4的缩写，专门用于兼容四字节字符，如Emoji表情。MySQL中的utf8是utf8mb3的别名，utf8mb4兼容utf8。

### 原子DDL

MySQL 8.0 开始支持原子性的数据定义语言（DDL），也称为原子 DDL。一个原子 DDL 语句将相关的数据字典更新、存储引擎操作以及写入二进制日志组合成单一的原子事务。当事务正在处理时出现服务器故障，该事务可能被提交，相应的变更会保存到数据字典更新、存储引擎更改以及二进制日志中；也可能被整体回滚。目前，只有 InnoDB 存储引擎支持原子 DDL。

支持原子DDL
1. 数据库、表空间、表、索引的 CREATE、ALTER 以及 DROP 语句，以及 TRUNCATE TABLE 语句
2. 存储过程、触发器、视图以及用户定义函数（UDF）的 CREATE 和 DROP 语句，以及适用的 ALTER 语句
3. 用户和角色的 CREATE、ALTER、DROP 语句，以及 GRANT 和 REVOKE 语句

不支持原子DDL

1. 非 InnoDB 存储引擎上的表相关 DDL 语句
2. INSTALL PLUGIN 和 UNINSTALL PLUGIN 语句
3. INSTALL COMPONENT 和 UNINSTALL COMPONENT 语句
4. CREATE SERVER、ALTER SERVER 以及 DROP SERVER 语句



任何 DDL 语句，包括原子性或其他的 DDL，都会隐式地结束当前事务，就像在执行语句之前执行了COMMIT 操作一样。这就意味着 DDL 语句不能位于其他事务之中，不能位于事务控制语句（如START TRANSACTION … COMMIT）之中，也不能与同一个事务中的其他语句组合使用。

```java
#只创建一张表
>create table test1(id int)
#无test2表报错，回退test1表未删除    
>drop table test1,test2;
```

# NoSQL

NoSQL是Not Only SQL的简称，意思是“不仅仅是SQL”。

NoSQL，指的是非关系型的数据库，它是对不同于传统的关系型数据库的数据库管理系统的统称。

NoSQL用于超大规模数据的存储。

- **键值存储**
  - Tokyo Cabinet/Tyrant
  - Berkeley DB
  - MemcacheDB
  - Redis

- **列存储**
  - Hbase
  - Cassandra
  - Hypertable

- **文档存储**

  - MongoDB

  - CouchDB

- **图形存储**
  - Neo4J
    - FlockDB