# Oracle

### 表空间操作

#### 创建

```mysql
【语法】
CREATE TABLESPACE 表空间名 DATAFILE '数据文件路径' SIZE 大小 [AUTOEXTEND ON] [NEXT 大小] [MAXSIZE 大小];

【说明】[]里面内容可选项；数据文件路径中若包含目录需要先创建
SIZE为初始表空间大小，单位为K或者M
AUTOEXTEND ON 是否自动扩展
NEXT为文件满了后扩展大小
MAXSIZE为文件最大大小，值为数值或UNLIMITED（表示不限大小）
```

```mysql
> create tablespace DAMSDB_DATA datafile '/opt/gdbservice/data/data/DAMSDB_DATA.dbf' size 1024M autoextend on next 1024M MAXSIZE 102400M;
```

[表空间使用方法 （Oracle）](https://blog.csdn.net/qq_41548307/article/details/84029864)

```mysql
--表空间
CREATE TABLESPACE sdt
DATAFILE 'F:\tablespace\demo' size 800M
         EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO; 
--索引表空间
CREATE TABLESPACE sdt_Index
DATAFILE 'F:\tablespace\demo' size 512M         
         EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;     
 
--2.建用户
create user demo identified by demo 
default tablespace sdt;
 
--3.赋权
grant connect,resource to demo;
grant create any sequence to demo;
grant create any table to demo;
grant delete any table to demo;
grant insert any table to demo;
grant select any table to demo;
grant unlimited tablespace to demo;
grant execute any procedure to demo;
grant update any table to demo;
grant create any view to demo;
```

### instr()

```sql
instr( string1, string2 [, start_position [, nth_appearance ] ] )
在string1中查找string2，在string1中start_position位置处开始检索，检索第nth_appearance（几）次出现string2。下标从1开始，Oracle中字符串位置是从 1 开始，而不是0。

select instr('helloworld','lo') from dual; --返回结果：4  即“lo”同时(连续)出现，“l”的位置
select instr('helloworld','l',4,2) from dual; --返回结果：9  也就是说：在"helloworld"的第4(l)号位置开始，查找第二次出现的“l”的位置
select instr('helloworld','l',-1,1) from dual; --返回结果：9  也就是说：在"helloworld"的倒数第1(d)号位置开始，往回查找第一次出现的“l”的位置
select instr('helloworld','y') from dual; --返回结果：0



MySQL中的模糊查询 like 和 Oracle中的 instr() 函数有同样的查询效果； 如下所示：
MySQL： select * from tableName where name like '%helloworld%';
Oracle：select * from tableName where instr(name,'helloworld')>0; --这两条语句的效果是一样的
```

### 模糊查询

```sql
select * from tb where name like '%xx%';
select * from tb where name like '%' || 'xx' || '%';
select * from tb where name like concat('%',concat('xx','%'));
使用instr()函数
```

### 分区

分区表分区的操作可能引起全局索引失效

分区表的分区索引分为：全局索引和本地索引两种。

- **本地索引**：其分区形式与表的分区完全相同，依赖列相同，存储属性也相同，在对分区进行操作时，会自动维护本地索引。
- **全局索引**：可建于分区表，又可创建于非分区表上，就是说，全局索引是完全独立的，因此它也需要我们更多的维护操作。

通常我们在创建分区表的索引时会有如下要求：

**分区表索引需要建立本地索引，不要使用全局索引，否则在执行分区的 truncate ，drop 时，可能造成索引失效，导致查询报错。** 

**正确**使用方法举例说明：

```sql
create index ix_userpackagelog_packstyle on t_ums_userpackagelog(packagestyle,logtime) local tablespace ringidx;
```

**反例**举例说明：

```sql
创建一个分区表如下：
CREATE TABLE parti_test
(
    USERID            NUMBER(6) NOT NULL,
    NAME              VARCHAR2(8)
)
PARTITION BY HASH (USERID)(
    PARTITION T_HASH_U1 TABLESPACE RING,
    PARTITION T_HASH_U2 TABLESPACE RING,
    PARTITION T_HASH_U3 TABLESPACE RING
);


创建全局索引如下：
CREATE INDEX IDX_RM_CUST_RANGE_ID ON parti_test(USERID);


插入数据，HASH分区基本能使得数据平均的分散到各个分区表中：
insert into  parti_test values(1,'aa');
insert into  parti_test values(2,'bb');
insert into  parti_test values(3,'cc');


使用如下语句使用索引进行查询。
SELECT /*+index(a IDX_RM_CUST_RANGE_ID)*/* FROM PARTI_TEST a WHERE USERID <10;
可以正确查询到数据，同时查看执行计划是正确走索引的。

如果执行分区 truncate 的操作，
alter table PARTI_TEST truncate partition T_HASH_U2;
此时再次执行上述查询语句就会报错：
```

结果说明：

如果在分区表上建立全局索引，对表分区的 truncate，drop 等操作，不会对全局索引进行维护，从而导致整个全局索引失效，当执行的 sql 语句走到该索引上时，都会造成 sql 语句异常错误。



```sql
当然，在对表分区进行操作时，可以指定 UPDATE GLOBAL INDEXES 来对全局索引进行重建来避免这个错误，举例如下：
执行如下分区清除语句：
alter table PARTI_TEST truncate partition T_HASH_U2 update global indexes;
此时再执行查询语句
SELECT /*+index(a IDX_RM_CUST_RANGE_ID)*/* FROM PARTI_TEST a WHERE USERID <10;
就不会有任何异常。

但是重建索引对于大表（分区表通常都是大表）是耗时相当大的操作，索引不建议执行索引重建的操作。
```

### sequence

在Oracle中sequence就是序号，每次提取完都会自动增加，步幅固定，它与表没有直接关系 

序列(SEQUENCE)其实是序列号生成器，可以为表中的行自动生成序列号，产生一组等间隔的数值(类型为数字)。其主要的用途是生成表的主键值，可以在插入语句中引用，也可以通过查询检查当前值，或使序列增至下一个值。

创建序列需要CREATE SEQUENCE系统权限。

```sql
-- 创建sequence语句：
CREATE SEQUENCE seq_name-- seq_name为计数器的名字，自定；
INCREMENT BY 1 -- 每次加幅度:1,2,3,....；
START WITH 1 -- 起始序号，以实际生产情况而定；
MINVALUE 0 -- 最小值
NOMAXvalue -- 不设置最大值，或设定最大值： maxvalue 9999;
NOCYCLE -- 一直累加，不循环; 或循环使用 cycle ;
CACHE 10; --设置缓存序列个数，如果系统down掉了或者其它情况将会导致序列不连续，也可以设置为--NOCACHE



-- 修改sequence：
-- Alter sequence 可以修改sequence（除起始值）步幅、最大/最小值、是否循环、缓存个数 这些参数；
例：
Alter Sequence seq_name  
Increment  By  2 
Maxvalue 9999
Cycle
Cache 5;
-- 需要修改sequence的起始值，则需要删除原有sequence，重新创建；



-- 应用sequence：
-- sequence创建完成后，就可以使用sequence的两个参数 currval、nextval；
-- currval查询sequence的当前值：
select seq_name.currval from dual;
-- nextval增加sequence的值，然后返回sequence值：
select seq_name.nextval from dual;
-- 例：对某一张表使用：
insert into tb_name(id,name) values(seq_name.nextval,'下一个计数');



-- 删除sequence ：
drop sequence seq_name;
```

**注：**

- currval是取当前值，所以一个新的计数器sequence必须先使用nextval后才可以使用currval否则会报错;
- nextval是取下一个值，但第一次使用时取的是初始值，之后正常取下一个，且如果一个语句(不同的子句)里面有多个nextval，它们的取值可能是不同的；
- 如果指定CACHE值，ORACLE就可以预先在内存里面放置一些sequence，cache里面的取完后，oracle自动再取一组到cache。 
  - 优点：存取的快些，尤其是并发访问时。
  - 缺点：使用cache或许会跳号， 比如数据库突然不正常down掉（shutdown abort),cache中的sequence就会丢失. 所以可以在创建的时候用nocache防止这种情况。

**什么时候使用sequence?**

- 不包含子查询、snapshot/view的select的语句
- insert语句的子查询中
- insert语句的values中
- update的set中

如：update 表名 列值=序列名.nextval where 条件;

 

**在sqlserver和mysql中都可以在定义表的时候，直接给指定自增长。**

sqlserver中设置自增长

create table 表名(id int primary key identity(1,1),name varchar(32));

mysql中设置自增长

create table 表名(id int primary key auto_incrment,name varchar(32));

[数据库Sequence创建与使用](https://www.cnblogs.com/klb561/p/11333643.html)

## 