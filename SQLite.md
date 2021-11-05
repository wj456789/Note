# SQLite

[sqlite的事务和锁](https://www.cnblogs.com/frankliiu-java/archive/2010/05/31/1748160.html)

[SQLite中的PRAGMA语句攻略](https://www.cnblogs.com/songxingzhu/p/3992884.html)

## 命令

设置正确格式化的输出
.header ON		开启表头显示
.mode column	左对齐的列

.quit	退出 SQLite 提示符
.show	显示各种设置的当前值

## 语法

不区分大小写的，但也有一些命令是大小写敏感的，比如 GLOB 和 glob 在 SQLite 的语句中有不同的含义。


数据类型
存储类	描述
NULL	值是一个 NULL 值。
INTEGER	值是一个带符号的整数，根据值的大小存储在 1、2、3、4、6 或 8 字节中。
REAL	值是一个浮点值，存储为 8 字节的 IEEE 浮点数字。
TEXT	值是一个文本字符串，使用数据库编码（UTF-8、UTF-16BE 或 UTF-16LE）存储。
BLOB	值是一个 blob 数据，完全根据它的输入存储。

创建或打开数据库
#sqlite3 DatabaseName.db

查看数据库
sqlite>.databases
seq  name             file
---  ---------------  ----------------------
0    main             /home/sqlite/testDB.db

导出数据库
$sqlite3 testDB.db .dump > testDB.sql

导入数据库
$sqlite3 testDB.db < testDB.sql

绑定逻辑名称
将数据库文件名称与逻辑数据库名称绑定，可以为一个文件添加多个逻辑名称。
数据库逻辑名称 main 和 temp 被保留用于主数据库和存储临时表及其他临时数据对象的数据库。
sqlite> ATTACH DATABASE 'testDB.db' as 'TEST';

删除逻辑名称
sqlite> DETACH DATABASE 'TEST';

创建表
sqlite>CREATE TABLE database_name.table_name(
   column1 datatype  PRIMARY KEY(one or more columns),
   column2 datatype,
   column3 datatype,
   .....
   columnN datatype,
);

查看表名
sqlite>.tables		//命令
sqlite>select name from sqlite_master where type='table' order by name;		//语言

重命名表
sqlite> ALTER TABLE COMPANY RENAME TO OLD_COMPANY;

添加列：新添加的列是以 NULL 值来填充的。
sqlite> ALTER TABLE OLD_COMPANY ADD COLUMN SEX char(1);

查看表结构，sqlite_master 表格,主表中保存数据库表的关键信息
sqlite>.schema sqlite_master
CREATE TABLE sqlite_master (
  type text,
  name text,
  tbl_name text,
  rootpage integer,
  sql text
);

查看表字段信息
sqlite>PRAGMA table_info(COMPANY)		//语言
cid	name		type		notnull	dflt_value	pk(主键)
0	COMPANY_id	varchar(32)	1					1


删除表
sqlite>DROP TABLE COMPANY;

插入数据
INSERT INTO first_table_name [(column1, column2, ... columnN)] 
   SELECT column1, column2, ...columnN 
   FROM second_table_name
   [WHERE condition];

Like 子句
百分号%		0个或多个
下划线_		1个

Glob 子句作用同上，但是大小写敏感
星号*		0个或多个
问号?		1个

LIMIT查询个数	OFFSET偏移量
SELECT column1, column2, columnN 
FROM table_name
LIMIT [no of rows] OFFSET [row num]

约束
CREATE TABLE COMPANY(
   ID 			  INT     NOT NULL PRIMARY KEY,
   NAME           TEXT    NOT NULL,
   AGE            INT     NOT NULL UNIQUE,
   ADDRESS        CHAR(50),
   SALARY         REAL    DEFAULT 50000.00 CHECK(SALARY > 0)
);

JOIN
交叉连接 - CROSS JOIN
SELECT ... FROM table1 CROSS JOIN table2 ...
把第一个表的每一行与第二个表的每一行进行匹配。如果两个输入表分别有 x 和 y 行，则结果表有 x*y 行

内连接 - INNER JOIN
是默认的连接类型
SELECT ... FROM table1 [INNER] JOIN table2 ON conditional_expression ...

外连接 - OUTER JOIN
只支持 左外连接（LEFT OUTER JOIN）
SELECT ... FROM table1 LEFT OUTER JOIN table2 ON conditional_expression ...

子查询
查询返回一列可以用多值运算符，返回多列相当于一个新表

## 索引

索引是一个指向表中数据的指针，创建索引需要考虑作为查询过滤条件的 WHERE 子句中使用非常频繁的列
索引有助于加快 SELECT 查询和 WHERE 子句，但它会减慢使用 UPDATE 和 INSERT语句时的数据输入。索引可以创建或删除，但不会影响数据。
CREATE INDEX index_name ON table_name;

单列索引
CREATE INDEX index_name ON table_name (column_name);

唯一索引：唯一索引不允许任何重复的值插入到表中
CREATE UNIQUE INDEX index_name on table_name (column_name);

组合索引
CREATE INDEX index_name on table_name (column1, column2);

隐式索引
隐式索引是在创建对象时，由数据库服务器自动创建的索引。索引自动创建为主键约束和唯一约束。

创建索引
sqlite> CREATE INDEX salary_index ON COMPANY (salary);

查看索引
sqlite> .indices COMPANY

删除索引
sqlite> DROP INDEX salary_index;

必须使用命名的索引来查找表中的值
sqlite> SELECT * FROM COMPANY INDEXED BY salary_index WHERE salary > 5000;

访问表时不使用索引
sqlite> SELECT * FROM COMPANY NOT INDEXED WHERE salary > 5000;

## 视图

视图用特定名称命名，是用一个 SQLite 查询语句查出来的虚表，并保存在数据库中，并且是只读的

创建视图
sqlite> CREATE VIEW COMPANY_VIEW AS SELECT ID, NAME, AGE FROM  COMPANY;

删除视图
sqlite> DROP VIEW COMPANY_VIEW;