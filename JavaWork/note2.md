# Note

## SQL

### 日期类型

```sql
-- 日期类型默认输出格式
> SELECT sysdate, systimestamp [from dual];
SYSTIMESTAMP			SYSDATE
2023-05-09 20:18:12		2023-05-09 20:18:12

-- 使用格式控制符将一个字符串转化为日期类型：
> SELECT to_date('07-JAN-2018', 'DD-MON-YYYY') [FROM dual];
TO_DATE('07-JAN-2018', 'DD-MON-YYYY')
2018-01-07 00:00:00

-- 通过格式控制符的描述，可以用to_char函数指定时期类型的输出格式
> SELECT to_char(sysdate, 'MON-YY-DD') [FROM dual];
TO_CHAR(SYSDATE, 'MON-YY-DD')
MAY-23-09

> SELECT to_char(sysdate, 'MON-YY-DD HH:MI:SS AM') [FROM dual];
TO_CHAR(SYSDATE, 'MON-YY-DD HH:MI:SS AM')
MAY-23-09 08:22:27 PM
```

Gauss100 目前支持的日期/时间类型包括DATE、带时区和不带时区的时间戳以及时间间隔。

#### DATETIME/DATE

**语法**：

```
DATETIME
```

**功能**：存储不带时区的日期类型的数据。

保存年、月、日、时、分、秒。

**取值范围**：公元0001年01月01日 00:00:00至公元9999年12月31日 23:59:59。

**占用空间**：8字节。

**对应关键字**：

- DATE
- DATETIME

#### TIMESTAMP

**语法**：

```
TIMESTAMP[(n)]
```

**功能**：存储不带时区的时间戳类型的数据。

- 保存年、月、日、时、分、秒，微妙。
- n取值范围[0,6]，表示秒后面的精度。TIMESTAMP[(n)]也可以不带参数，即写为TIMESTAMP，这时默认为6。

**取值范围**：公元0001年01月01日 00:00:00.000000至公元9999年12月31日 23:59:59.999999。

**占用空间**：8字节。

**对应关键字**：TIMESTAMP

#### TIMESTAMP(n) WITH TIME ZONE

**语法**：

```
TIMESTAMP(n) WITH TIME ZONE
```

**功能**：存储带时区的时间戳类型的数据。

- 保存年、月、日、时、分、秒，微妙。
- n取值范围[0,6]，表示秒后面的精度。TIMESTAMP[(n)]也可以不带参数，即写为TIMESTAMP，这时默认为6。

**取值范围**：公元0001年01月01日 00:00:00.000000至公元9999年12月31日 23:59:59.999999。

**占用空间**：12字节。

**对应关键字**：TIMESTAMP(n) WITH TIME ZONE

#### TIMESTAMP(n) WITH LOCAL TIME ZONE

**语法**：

```
TIMESTAMP(n) WITHLOCAL TIME ZONE
```

**功能**：带时区的时间戳类型的数据。不存储时区，存储时转换为数据库时区的TIMESTAMP，用户查看时转换为当前会话的时区的TIMESTAMP。

**占用空间**：8字节。

**对应关键字**：TIMESTAMP(n) WITHLOCAL TIME ZONE

#### 日期类型的格式控制符

| 符号                                                | 说明                                                         | 转换是否可逆 | 示例                                                         |
| --------------------------------------------------- | ------------------------------------------------------------ | ------------ | ------------------------------------------------------------ |
| " "(空格)、"-"(减号)、"\"、"/" 、":"、","、"."、";" | 分隔符                                                       | 是           | -                                                            |
| "text"                                              | 文本类型                                                     | 是           | 文本类型，作为输出参数时，输出引号中包含的内容；作为输入参数时，跳过引号中的内容，忽略空格。`select to_char(sysdate, '"Hello world!"') from dual;` |
| AM、PM                                              | 上午和下午指示符                                             | 否           | `select to_char(systimestamp, 'HH12:MI:SS AM') from dual;`   |
| CC                                                  | 世纪                                                         | 否           | `select to_char(systimestamp, 'CC') from dual;`              |
| DAY                                                 | 星期天全称                                                   | 否           | `select to_char(systimestamp, 'DAY') from dual;`             |
| DY                                                  | 星期天简称                                                   | 否           | `select to_char(systimestamp, 'DY') from dual;`              |
| DDD                                                 | 一年中的第几天                                               | 否           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'DDD') from dual;` |
| DD                                                  | 当前月中的第几天                                             | 是           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'DD') from dual;` |
| D                                                   | 当前周中的第几天                                             | 否           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'D') from dual;` |
| FF3、FF6、FF（默认FF6）                             | 秒的小数部分                                                 | 是           | `select to_char(systimestamp, 'FF3') from dual;`             |
| HH12、HH24 、HH（默认HH12）                         | 12小时制/24小时制                                            | 是           | `select to_char(systimestamp, 'HH,HH12,HH24') from dual;`    |
| MI                                                  | 时间的分钟数(0 ~ 59)                                         | 是           | -                                                            |
| MM                                                  | 日期的月份(1 ~ 12)                                           | 是           | -                                                            |
| MONTH                                               | 日期中月份全称                                               | 是           | `select to_char(systimestamp, 'MONTH, MON') from dual;`      |
| MON                                                 | 日期中月份简称                                               | 是           | -                                                            |
| Q                                                   | 当前日期的季度(1 ~ 4)                                        | 否           | -                                                            |
| SSSSS                                               | 一天中已经逝去的秒数(0 ~ 86400 - 1)                          | 否           | -                                                            |
| SS                                                  | 时间中的秒数(0 ~ 59)                                         | 是           | -                                                            |
| WW                                                  | 当前日期为该年份的week数，即当年的第几周，第一周从当年第一天计算起，每周7天 | 否           | -                                                            |
| W                                                   | 当前日期为该月份的week数，即当月的第几周，第一周从当月第一天计算起，每周7天 | 否           | -                                                            |
| YYYY                                                | 四位年份                                                     | 是           | -                                                            |
| YYY                                                 | 三位年份，如2018年可以写作018                                | 否           | -                                                            |
| YY                                                  | 两位年份，如2018年可以写作18                                 | 否           | -                                                            |
| Y                                                   | 一位年份，如2018年可以写作8                                  | 否           | `select to_char(systimestamp, 'Y') from dual;`               |



| 日期类型                       | 默认输出格式                     |
| ------------------------------ | -------------------------------- |
| DATETIME                       | YYYY-MM-DD HH24:MI:SS            |
| TIMESTAMP                      | YYYY-MM-DD HH24:MI:SS.FF         |
| TIMESTAMP WITH TIME ZONE       | YYYY-MM-DD HH24:MI:SS.FF TZH:TZM |
| TIMESTAMP WITH LOCAL TIME ZONE | YYYY-MM-DD HH24:MI:SS.FF         |

## 多线程

获取线程安全的List我们可以通过Vector、Collections.synchronizedList()方法和CopyOnWriteArrayList三种方式

- 读多写少的情况下，推荐使用CopyOnWriteArrayList方式
- 读少写多的情况下，推荐使用Collections.synchronizedList()的方式

[三种线程安全的List](https://blog.csdn.net/weixin_45668482/article/details/117396603)

## 分区表

把一张几千级的表TEST1改为分区表一般有以下几种方法：

**方法一**   **利用原表重建分区表**

根据原表TEST1表结构定义来新建分区表TEST1_TMP；

把TEST1的数据insert进表TEST1_TMP； --也可以用**CTAS**的方法      

rename表名，检查数据一致性。

优点：简单

缺点：考虑数据一致性，建议闲时 或 停机操作

 

**方法二** **使用数据泵导出导入**

根据原表TEST1表结构定义来新建分区表TEST1_TMP；

expdp并行导出TEST1，impdp并行导入TEST1_TMP；

rename表名，检查数据一致性。

优点：简单；若表在上百G，效率也算蛮高

缺点：考虑数据一致性，建议闲时 或 停机操作

表越大，越建议用这种方式，停机做

 

**方法三** **使用分区交换**

优点：只是对数据字典中的分区和表定义进行修改，没有数据复制，效率最高

缺点：全部表的数据都在一个分区内

这个缺点太致命，所以效率最高的方法，反而不用

```sql
--1、创建一个测试表
CREATE TABLE p1 AS SELECT t.OBJECT_NAME,t.CREATED FROM User_Objects t;
CREATE INDEX idx_p1_created ON p1(created);
--2、创建分区表，结构要与原表一致
CREATE TABLE p2(object_name VARCHAR2(32),created DATE) PARTITION BY RANGE(created) 
(
       PARTITION p_202204 VALUES LESS THAN (DATE'2022-05-01'),
       PARTITION p_202205 VALUES LESS THAN (DATE'2022-06-01')
);
--3、创建局部分区索引
CREATE INDEX idx_p2_created ON p2(created) LOCAL;
--4、交换分区与表
ALTER TABLE p2 EXCHANGE PARTITION p_202204 WITH TABLE p1 INCLUDING INDEXES WITHOUT validation;
--5、查看数据
SELECT * FROM p1;
SELECT * FROM p2 PARTITION (p_202204);
--6、查看索引状态
SELECT * FROM User_Indexes t WHERE t.table_name='P2';
SELECT * FROM User_Ind_Partitions t WHERE t.index_name=UPPER('idx_p2_created');

	总结：采用交换分区的办法，其原理仅仅是修改ORACLE系统中表定义的数据字典。不需要额外的表空间，速度非常快速，原表上的索引也不需要重建。当然，此种方法是将原表所有原来的数据放入了一个分区之中，新增数据才会进入新的分区。这样对于历史数据的查询使用不到分区排除带来的效率提高，有一定的局限性。
```

**方法四** **使用在线重定义**

11g的在线重定义其实已经非常好用，也支持rows的方式来重定义，不必有主键。

优点：可以在线做，不用担心数据一致性

缺点：效率一般

这个优点太突出，建议几十G内的表都可以选用这种方式做

## Mysql

```
SHOW GLOBAL VARIABLES LIKE 'innodb_lock_wait_timeout';
SET GLOBAL innodb_lock_wait_timeout=500;
SHOW GLOBAL VARIABLES LIKE 'innodb_lock_wait_timeout';
```



## oracle

```
merge into 目标表 t
using (源表) ti
on (原表与目标表的关联条件) -- 一般用主键或者能确认唯一的组合条件
when matched then
update set t.xxxx = ti.xxx
when not matched then
insert
(xxxx, xxx, xx, xxx, xxxxxxxx, xxx) -- 目标表字段
values
(ti.xxx, ....) -- 源表字段


merge into会将源表的每一条记录和目标表按关联字段匹配，目标表被匹配到的记录会更新数据，匹配不到的记录话就会把源表这些数据插入目标表，匹配的前提是关联字段要是目标表与源表的主键或唯一匹配条件。


直接根据 on的条件去匹配，在数据库层次就实现了 新增or更新操作。不需要将大量的数据查出来进行操作，同时也不会造成锁表问题，最终实现了500w数据3分钟同步完成（因为这张表上百个字段比较大）
```



## 分布式锁

[分布式锁的实现方式](https://blog.csdn.net/fuzh19920202/article/details/127999788)

[从spring管理的datasource中获取connection](https://blog.csdn.net/qq_18671415/article/details/119112362)

## Java基础

[Arrays.toList() 和Collections.singletonList()的区别](https://blog.csdn.net/wz1159/article/details/86704752?ydreferer=aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS5oay8%3D?ydreferer=aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS5oay8%3D)

## Java Web

在web.xml中定义 contextConfigLocation参数，spring会使用这个参数加载所有逗号分割的xml，如果没有这个参数，spring默认加载WEB-INF/applicationContext.xml文件。

[spring如何使用多个xml配置文件 【转】](https://www.cnblogs.com/secret1998/archive/2010/05/24/1742555.html)

实现Servlet的Filter接口 + web.xml配置过滤器

实现Servlet的Filter接口 + @WebFilter注解 + 服务主类上增加@ServletComponentScan注解，basePackages需要包含过滤器类所在的包，过滤器的执行顺序按照过滤器的类名升序排序 

实现Servlet的Filter接口 + @Configuration实现WebMvcConfigurerAdapter接口配置类，过滤器的执行顺序由setOrder的参数决定，值越小，优先级越高，如果两个过滤器的order值相同，则执行顺序按过滤器类名排序



在Spring容器中，servlet过滤器的的创建早于spring bean的初始化，所以在过滤器中用@Autowired、@Inject等注解注入bean，以及用@Value注解注入环境参数，都只能取得null。我们必须手动在过滤器的init方法中进行bean的初始化：

![img](note2.assets/59dc31552657a.jpg) 

[@WebFilter两种使用方法和失效解决方案](https://blog.csdn.net/z69183787/article/details/127808802)

[Java Web之过滤器Filter（@WebFilter）](https://blog.csdn.net/weixin_44989630/article/details/121357652)

[Servlet3.0模块化支持](https://www.iteye.com/blog/elim-2017099)



## IDEA

**查看变量调用链**

选中变量，右键选择 Analyze ->  Analyze Data Flow to Here/Analyze Data Flow from Here 

## Hbase

### cache 和 batch

缓存是服务端缓存 result 个数

批量处理是处理每行列数，默认是全列，当数据比每行列数小，则分批次处理，比每行列数大还是一行。比如默认一行一个 result ，一行数据有23列，批量处理为10，需要分三个批次读取这行数据，会放在3个 result 中，第三个 result 只有3列。

每次 rpc 请求可以获取缓存中的所有 result ，每次调用 next 都会触发一次rpc请求

RowFilter用于过滤row key

| Operator         | Description |
| ---------------- | ----------- |
| LESS             | 小于        |
| LESS_OR_EQUAL    | 小于等于    |
| [EQUAL           | 等于        |
| NOT_EQUAL        | 不等于      |
| GREATER_OR_EQUAL | 大于等于    |
| GREATER          | 大于        |
| NO_OP            | 排除所有    |

[Hbase](https://www.cnblogs.com/dengrenning/articles/9520924.html)

[Hadoop、HDFS、Hive、Hbase之间的关系](https://www.cnblogs.com/liyuanhong/articles/14518037.html)

[HBase高级特性之过滤器](http://3ms.huawei.com/km/groups/388/blogs/details/10461377) 

[HBase - Filter - 过滤器的介绍以及使用](https://blog.csdn.net/qq_36864672/article/details/78624856)

[HBase的二级索引](https://www.cnblogs.com/tesla-turing/p/11515351.html)

[hbase的cache与batch的理解](https://blog.csdn.net/luxiangzhou/article/details/83615993)

[HBase基础知识(8):扫描操作之缓存与批量处理](http://pangjiuzala.github.io/2015/08/20/HBase%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86-8-%E6%89%AB%E6%8F%8F%E6%93%8D%E4%BD%9C%E4%B9%8B%E7%BC%93%E5%AD%98%E4%B8%8E%E6%89%B9%E9%87%8F%E5%A4%84%E7%90%86/)

[hbase 程序优化 参数调整方法](https://blog.51cto.com/u_15127532/4045809) 

[hbase零碎小记](https://blog.csdn.net/bryce123phy/article/details/52943434)

## CURL

查看所有curl命令： man curl或者curl -h 

请求头：H,A,e 

响应头：I,i,D 

cookie：b,c,j 

传输：F(POST),G(GET),T(PUT),X 

输出：o,O,w 

断点续传：r 

调试：v,--trace,--trace-ascii,--trace-time 

[CURL详解](https://www.cnblogs.com/lei-z/p/16543184.html#_label0)

```sh
# -v 显示更详细的信息，调试时使用；
# -k (SSL)设置此选项将允许使用无证书的不安全SSL进行连接和传输。
$ curl -vk https://7.220.28.99:9011/sso/isAlive.jsp

* About to connect() to 7.220.28.99 port 9011 (#0)
*   Trying 7.220.28.99...
* Connected to 7.220.28.99 (7.220.28.99) port 9011 (#0)
* Initializing NSS with certpath: sql:/etc/pki/nssdb
* skipping SSL peer certificate verification
* SSL connection using TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
* Server certificate:
*       subject: CN=DigitalServicePlatform,O=Huawei,C=CN
*       start date: Mar 07 06:14:52 2019 GMT
*       expire date: Mar 04 06:14:52 2029 GMT
*       common name: DigitalServicePlatform
*       issuer: CN=HUAWEI Software Product CA,O=Huawei,C=CN
> GET /sso/isAlive.jsp HTTP/1.1
> User-Agent: curl/7.29.0
> Host: 7.220.28.99:9011
> Accept: */*
>
< HTTP/1.1 200
< Date: Fri, 24 Mar 2023 06:25:09 GMT
< Content-Type: text/html;charset=ISO-8859-1
< Content-Length: 11
< Connection: keep-alive
< Keep-Alive: timeout=30
< Set-Cookie: JSESSIONID=9139F6861719ECEE9E4890DB2D9B715C94CE21414FDE19A0; Path=/sso; Secure; HttpOnly; SameSite=None
< Set-Cookie: route=44432b10ef109634d5027b1b70a2ac3a; Path=/sso
< Server: lb
< Expires: 0
< Referrer-Policy: origin
<
* Connection #0 to host 7.220.28.99 left intact
ssoserverok
```

## tomcat

springboot使用外置tomcat

```java
@SpringBootApplication(scanBasePackages = {"com.huawei.iop.*"})
@MapperScan({"com.huawei.iop.mapper"})
@ServletComponentScan
public class IopApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(IopApplication.class, args);
    }

    @Override // 为了打包springboot项目
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IopApplication.class);
    }
}
```

```xml
<packaging>war</packaging>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- 去掉自身tomcat避免冲突 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <!--打包的时候可以不用包进去，别的设施会提供。事实上该依赖理论上可以参与编译，测试，运行等周期。
                相当于compile，但是打包阶段做了exclude操作-->
    <scope>provided</scope>
</dependency>
```

```xml
${project.basedir}理解：${basedir}项目的根目录(包含pom.xml文件的目录)
<build>
    <!-- 应与application.properties(或application.yml)中context-path保持一致 -->
    <finalName>unifiedManage</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <configuration>
                <failOnMissingWebXml>false</failOnMissingWebXml>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.huawei.iop.IopApplication</mainClass>
                <includeSystemScope>true</includeSystemScope>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>8</source>
                <target>8</target>
            </configuration>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <excludes>
                <exclude>**/*.java</exclude>
            </excludes>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.*</include>
            </includes>
        </resource>
        <resource>
            <directory>src/lib</directory>
            <targetPath>${basedir}/target/unifiedManage/WEB-INF/lib</targetPath>
            <includes>
                <include>EncryptUtilsGCM.jar</include>
            </includes>
        </resource>
    </resources>
</build>





```



## maven

```xml
IDEA 在 maven 项目打 war 包时将外部第三方引入的 jar 包

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    	<!-- 导入系统之外的 Jar 包 -->
        <includeSystemScope>true</includeSystemScope>
    </configuration>
</plugin>
```

~~~xml
maven-jar-plugin是Maven的一个插件，用于将项目打包成jar包。

常用的配置选项及含义如下：

- `<archive>`：指定打包时要包含的文件和目录。
- `<manifest>`：指定manifest文件的信息。
- `<excludes>`：指定不包含在打包文件中的文件和目录。
- `<includes>`：指定需要包含在打包文件中的文件和目录。
- `<classifier>`：为打包文件指定附加分类器。
- `<finalName>`：指定打包文件的最终名称。

其中，archive 元素指定了生成的 Jar 包需要包含哪些文件以及如何打包这些文件。它具体包含以下配置项：
- manifest：用于指定 Manifest 文件(MANIFEST.MF)的位置和内容。
- manifestEntries：用于指定 Manifest 文件中的条目。
- addMavenDescriptor：是否将 Maven 项目描述文件 pom.xml 添加到生成的 Jar 包中。
- index：是否创建一个包含索引信息的 JAR 文件。
- compress：是否压缩生成的 Jar 包。
- forced：是否强制覆盖已存在的 Jar 包。
    
举个例子：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.0.2</version>
            <configuration>
                <archive> 
                    <manifest>
                        <addClasspath>true</addClasspath>
                        <mainClass>com.example.MainClass</mainClass>
                    </manifest>
                    <manifestEntries>
                        <Built-By>${user.name}</Built-By>
                        <Build-Jdk>${java.version}</Build-Jdk>
                    </manifestEntries>
                </archive>
                <excludes>
                    <exclude>**/*.txt</exclude>
                    <exclude>**/*.properties</exclude>
                </excludes>
                <includes>
                    <include>**/*.java</include>
                </includes>
                <classifier>exec</classifier>
                <finalName>my-app</finalName>
            </configuration>
        </plugin>
    </plugins>
</build>
```

在上述例子中，配置了打包的manifest信息，排除了txt和properties文件，添加了java文件，增加了分类器为exec，最终生成的文件名为my-app。
~~~



## path

```java
// 获取jar包内filePath下的文件名称集合
public static List<String> getJarPathFile(String filePath) {
    URL url = StringUtil.class.getClassLoader().getResource("/");
    String jarPath = url.toString().substring(0, url.toString().indexOf("!/") + 2);
    URL jarURL = null;
    List<String> fileNameLists = new ArrayList<>();
    try {
        jarURL = new URL(jarPath);
        JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
        JarFile jarFile = jarCon.getJarFile();
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            if (name.contains(filePath)){
                name = name.substring(name.lastIndexOf("/") + 1);
                if (StringUtils.isNotBlank(name)){
                    fileNameLists.add(name);
                }
            }
        }
    } catch (MalformedURLException e) {
        LOGGER.error("read path fail:" + e.getMessage());
    } catch (IOException e) {
        LOGGER.error("read path fail:" + e.getMessage());
    }
    return fileNameLists;
}
```

## Linux

### sed

```
s：替换，替换指定字符
d：删除，删除指定的行
a：增加，在当前行的下一行增加一行指定内容
i：插入，在指定行上面插入一行指定内容
s：替换，将选定行替换成指定内容
y：字符转换，转换前后的字符长度必须相同
p：打印，如果同时指定行，表示打印指定行；如果不指定行，则表示打印所有内容；如果有非打印字符，则以ASCII码输出。通常与-n一起使用
=：打印行数
l：打印数据流中的文本和不可打印的ASCII字符
H:复制到剪切板
G:粘贴

sed命令格式
sed -e '具体操作' 文件名1 文件名2 ...
sed -n -e '具体操作' 文件名1 文件名2 ...
sed -f 脚本文件 文件名1 文件名2 ...
sed -i -e '具体操作' 文件名1 文件名2 ...

查看文件指定行
sed -n '1p' /etc/passwd
5、查看一到三行
sed -n '1,3p' /etc/passwd
6、查看奇数行（n的作用是跳过当前行的下一行）
sed -n 'p;n' num
7、查看偶数行
sed -n 'n;p' num
8、sed 的查找与替换（g标识符表示全局查找替换）
sed 's/要被取代的字串/新的字串/g'
9、匹配包含相关字符的行
sed -n '/cao/p' /etc/passwd
10、匹配以指定字符为首的行
sed -n '/^root/p' /etc/passwd
11、匹配以指定字符结尾的行
sed -n '/bash$/p' /etc/passwd
12、使用-r选项支持扩展正则表达式
sed -n -r '/ro+t/p' /etc/passwd
13、删除指定行
sed -n '1d;p' num
14、删除范围内的行
sed -n '1,3d;p' num
15、取反删除，表示删除匹配项以外的行
sed -n '1,3!d;p' num
16、在每行行首加#号
sed -n 's/^/#/;p' num
17、删除行首的#号
sed -n 's/#//;p' num
18、在第一行下方插入123
sed '1a 123' num
19、在第一行行前插入123
sed '1i 123' num
20、剪切
sed '1{H;d};$G' num
21、复制粘贴
sed '1H；$G' num

$ cat example.txt
123

456
789

输出第一行直到空行为止
$ sed -n '1,/^$/p' example.txt
123
(空格也会输出)

# 删除第一行直到空行，输出剩余
$ sed '1,/^$/d' example.txt
456
789
```

```
-C<显示行数> 或 --context=<显示行数>或-<显示行数> : 除了显示符合样式的那一行之外，并显示该行之前后的内容。
-A<显示行数> 或 --after-context=<显示行数> : 除了显示符合范本样式的那一列之外，并显示该行之后的内容。
cat example.txt | grep "strpattern" -C 10
cat example.txt | grep "strpattern" -A 10
```

### curl

在使用curl命令发送HTTP请求时，可以使用 `-H` 参数来添加请求头。请求头的格式是`HeaderName:HeaderValue`，其中HeaderName表示请求头的名称，HeaderValue表示请求头的值。例如，要添加一个名为`Authorization`，值为`Bearer abcdefg`的请求头，可以这样使用curl命令：

```
curl -H "Authorization: Bearer abcdefg" https://example.com/api
复制代码
```

其中`https://example.com/api`是要访问的API的URL。

**如果要添加多个请求头，可以使用多个 `-H` 参数**，例如：

```
curl -H "Authorization: Bearer abcdefg" -H "Content-Type: application/json" https://example.com/api
```

这样会添加两个请求头，一个是`Authorization: Bearer abcdefg`，另一个是`Content-Type: application/json`。

### vim

:set fileencoding查看文件当前展示的编码格式 

## JVM

### 类加载

[java同时引用不同版本同一个jar包](https://blog.csdn.net/white_grimreaper/article/details/120921270)

[Java破坏双亲委派实现自定义加载器加载不同版本类](https://blog.csdn.net/u011943534/article/details/89204709)

[Java 自定义 ClassLoader 实现隔离运行不同版本jar包的方式](https://blog.csdn.net/t894690230/article/details/73252331?spm=1001.2101.3001.6650.7&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&utm_relevant_index=13)

[Java插件开发之类加载隔离](https://www.cnblogs.com/xmzpc/p/15187495.html)

[java的类加载器以及如何自定义类加载器](https://blog.csdn.net/blueheartstone/article/details/127784519)

<https://cloud.tencent.com/developer/article/1832222>

<https://cloud.tencent.com/developer/article/1915650>

<https://www.zhihu.com/question/466696410>

<https://cloud.tencent.com/developer/article/1890187>

<https://juejin.cn/post/6865572557329072141>

























































```
出现上传到Hadoop HDFS文件乱码的问题，通常是因为编码格式不一致导致的。在Java上传Hadoop HDFS文件时，需要讲本地文件和HDFS文件都设置为相同的编码格式。常见的编码格式有UTF-8和GBK两种。下面给出一个示例代码，在上传文件时将编码格式设置为UTF-8。

​```
Configuration conf = new Configuration();
conf.set("fs.defaultFS", "hdfs://localhost:9000");
FileSystem fs = FileSystem.get(conf);
InputStream in = new FileInputStream(new File("localFilePath"));
OutputStream out = fs.create(new Path("hdfsFilePath"));
IOUtils.copyBytes(in, out, 4096, true);
​``` 

如果上传的文件的编码格式为GBK，那么需要在代码中加上如下两行代码：

​```
conf.set("fs.defaultFS", "hdfs://localhost:9000");
conf.set("dfs.client.use.datanode.hostname", "true");
conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
conf.set("hadoop.job.ugi", "hadoop");
FileSystem fs = FileSystem.get(conf);
FSDataOutputStream outputStream = fs.create(new Path("hdfsFilePath"));
InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("localFilePath")), "GBK");
BufferedReader br = new BufferedReader(isr);
String line = null;
while ((line = br.readLine()) != null) {
    outputStream.write(line.getBytes("GBK"));
    outputStream.write("
".getBytes());
    outputStream.flush();
}
outputStream.close();
​``` 

在以上代码中，需要注意以下几点：

1. 首先需要将文件的编码方式设置为GBK

2. 需要在代码中设置hadoop.job.ugi参数，设置为hadoop，表示Hadoop用户。

3. 将FileSystem设置为DistributedFileSystem的实现，添加如下代码：

​```
conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
​``` 

4. 如果是上传一个多行的文本文件，则需要对每一行的数据进行编码，并加上换行符。

希望这些内容对你有所帮助。
不再接收
```

```
Hadoop HDFS使用两种编码格式：UTF-8和UTF-16。UTF-8是一种变长编码，用于表示Unicode字符集中的所有字符。而UTF-16是一种固定长度的编码格式，也用于表示Unicode字符集中的字符。

在Hadoop HDFS中，文件名、目录名、路径、文件内容等都是以UTF-8编码格式存储的。另外，Hadoop HDFS在存储数据块时也使用UTF-8编码格式进行编码。但是，Hadoop HDFS在存储元数据时使用UTF-16编码格式进行编码，因为UTF-16编码格式在处理字符时更加高效。

需要注意的是，Hadoop HDFS使用的UTF-8编码格式和普通的UTF-8编码格式并不完全一样。因为Hadoop HDFS需要处理大量文件名和路径信息，而这些信息通常包含大量的无效字符和控制字符，因此Hadoop HDFS进行了一些扩展和修改，以兼容各种不同的文件系统和操作系统。
不再接收
```
