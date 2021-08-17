# Log日志

## Log4j

### 依赖

```java
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
```

### log4j.properties

默认查找classpath下的log4j.properties文件

```java
### 设置###
log4j.rootLogger = info,stdout,logfile,D,E

### 输出信息到控制台 ###
log4j.appender.stdout = org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target = System.out
log4j.appender.stdout.layout = org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern = [%-5p] %d{yyyy-MM-dd HH:mm:ss,SSS} method:%l%n%m%n

#常用设置:输出INFO 级别以上的日志到=fastwork.log并且每天归档
log4j.appender.logfile=org.apache.log4j.DailyRollingFileAppender
log4j.appender.logfile.Threshold=INFO
log4j.appender.logfile.File=${catalina.home}/fastwork/fastwork.log
log4j.appender.logfile.DatePattern='.'yyyy-MM-dd'.log' 
log4j.appender.logfile.Append=true 
log4j.appender.logfile.Encoding=UTF-8     
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=>>>>>>>LOG MSG<<<<<<< %r %d %p [%c] - %m%n    
    
    
### 输出DEBUG 级别以上的日志到=debug.log ###
log4j.appender.D = org.apache.log4j.DailyRollingFileAppender
log4j.appender.D.File = debug.log
log4j.appender.D.Append = true
log4j.appender.D.Threshold = DEBUG 
log4j.appender.D.layout = org.apache.log4j.PatternLayout
log4j.appender.D.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n

### 输出ERROR 级别以上的日志到=error.log ###
log4j.appender.E = org.apache.log4j.DailyRollingFileAppender
log4j.appender.E.File =error.log 
log4j.appender.E.Append = true
log4j.appender.E.Threshold = ERROR 
log4j.appender.E.layout = org.apache.log4j.PatternLayout
log4j.appender.E.layout.ConversionPattern = %d{yyyy-MM-dd HH:mm:ss}  [ %t:%r ] - [ %p ]  %m%n
```

### 配置文件解析

```java
log4j.appender.logfile=org.apache.log4j.DailyRollingFileAppender
# 日志级别，info及以上级别的均会输出
log4j.appender.logfile.Threshold=INFO
# 当前记录文档fastwork_log.txt
log4j.appender.logfile.File=${catalina.home}/fastwork/fastwork_log.txt
# 每天归档如：fastwork.log.2020.12.12.log    
log4j.appender.logfile.DatePattern='.'yyyy-MM-dd'.log' 
log4j.appender.logfile.Append=true 
log4j.appender.logfile.Encoding=UTF-8  
# 日志输出格式    
log4j.appender.logfile.layout=org.apache.log4j.PatternLayout
log4j.appender.logfile.layout.ConversionPattern=>>>>>>>LOG MSG<<<<<<< %r %d %p [%c] - %m%n    
```



#### 配置根rootLogger

```java
log4j.rootLogger = [ level ] , appenderName, appenderName, …
```


level 就是日志的优先级，从高到低依次是 FATAL 、ERROR、WARN、INFO、DEBUG。如果这里定义的是 INFO，那么低级别的 DEBUG 日志信息将不会打印出来。

appenderName 就是指把日志信息输出到什么地方，可以指定多个地方，当前的配置文件中有 3 个地方，分别是 stdout、D、E。

#### 配置日志输出的目的地

```java
log4j.appender.appenderName = fully.qualified.name.of.appender.class  
log4j.appender.appenderName.option1 = value1  
…  
log4j.appender.appenderName.optionx = valueX
```

Log4j 提供的目的地(fully.qualified.name.of.appender.class)有下面 5 种：

- `org.apache.log4j.ConsoleAppender`：控制台
- `org.apache.log4j.FileAppender`：文件
- `org.apache.log4j.DailyRollingFileAppender`：每天产生一个文件
- `org.apache.log4j.RollingFileAppender`：文件大小超过阈值时产生一个新文件
- `org.apache.log4j.WriterAppender`：将日志信息以流格式发送到任意指定的地方

#### 配置日志输出格式

自定义格式的参数如下所示：

- `%m`：输出代码中指定的消息
- `%p`：输出优先级
- `%r`：输出应用启动到输出该日志信息时花费的毫秒数
- `%c`：输出所在类的全名
- `%t`：输出该日志所在的线程名
- `%n`：输出一个回车换行符
- `%d`：输出日志的时间点
- `%l`：输出日志的发生位置，包括类名、线程名、方法名、代码行数，比如：method:com.itwanger.Log4jDemo.main(Log4jDemo.java:14)

### 打印日志

```java
import org.apache.log4j.Logger;

public class Log4jDemo {
    private static final Logger logger = Logger.getLogger(Log4jDemo.class);

    public static void main(String[] args) {
        // 记录debug级别的信息
        logger.debug("debug.");

        // 记录info级别的信息
        logger.info("info.");

        // 记录error级别的信息
        logger.error("error.");
    }
}
```



### 打印 DEBUG 级别的日志

```java
if(logger.isDebugEnabled()) {
    logger.debug("用户名是：" + getName());
}
```

`logger.debug("用户名是：" + getName());`尽管配置文件里的日志级别定义的是 INFO，但是`getName()`方法依然会执行，所以此时需要使用`isDebugEnabled()`方法判断，如果`debug()`方法没有传参，也可以不使用

### SpringMVC集成

**web.xml**

```java
<context-param>
    <param-name>log4jConfigLocation</param-name>
    <param-value>classpath:properties/log4j.properties</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
</listener>
```



## SLF4J

### 日志概述

日志系统有JUL、Log4j、Logback、JCL，日志框架有SLF4J

#### JUC( java.util.logging)

```java
<dependency>
    <groupId>commons-logging</groupId>
    <artifactId>commons-logging</artifactId>
    <version>1.2</version>
</dependency>
```

```java
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class Demo {
    private static Log logger = LogFactory.getLog(Demo.class);
    public static void main(String[] args) {
        logger.info("jcl");
    }
}
```

####  JCL（JakartaCommons Logging）

它在 JUL 和 Log4j 的基础上提供了⼀个抽象层的接口，⽅便使⽤者在 JUL 和 Log4j 之间切换

#### SLF4J (Simple Logging Facade for Java)

简易的⽇志⻔⾯，提供了接口，以外观模式（Facade pattern，⼀种设计模式，为⼦系统中的⼀组接⼝提供⼀个统⼀的⾼层接⼝，使得⼦系统更容易使⽤）实现，⽀持 JUL、Log4J 和 Logback。

### SLF4J的使用

将日志系统转换为SLF4J时需要添加依赖，但是配置文件依然使用原来的log4j.properties或logback.properties

#### 依赖

**SLF4J绑定 Log4j 替换 JUL 和 JCL**

```java
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
    <version>1.7.25</version>
</dependency> 
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jul-to-slf4j</artifactId>
    <version>1.7.29</version>
</dependency> 
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.25</version>
</dependency>
```

**绑定 Logback 替换 JUL、JCL、Log4j** 

- jul-to-slf4j.jar
- jcl-over-slf4j.jar
- log4j-over-slf4j.jar
- logback-classic.jar



#### 打印⽇志

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class SLF4JDemo {
    private static final Logger logger = LoggerFactory.getLogger(SLF4JDemo.class);
    public static void main(String[] args) {
        logger.debug("{}是晴朗的一天","今天");
    }
}
```



SLF4J 在打印⽇志的时候可以使用占位符 {} ，并且此时不再需要 isDebugEnabled() 先进⾏判

断， debug() ⽅法会在字符串拼接之前执⾏。

### 总结

- 在使⽤⽇志系统的时候，⼀定要使⽤ SLF4J 作为⻔⾯担当。
- SLF4J 可以统⼀⽇志系统，作为上层的抽象接⼝，不需要关注底层的⽇志实现，可以是 Log4j，也可以是 Logback，或者 JUL、JCL。 
- SLF4J 在打印⽇志的时候可以使⽤占位符，既提⾼了程序性能（临时字符串少了，垃圾回收的⼯作量就⼩），⼜让代码变得美观统⼀。

## Logback

### 依赖

```java
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
```

### logback.xml

默认会在 classpath 路径下先寻找 logback-test.xml ⽂件，没有找到的话，寻找logback.groovy ⽂件，还没有的话，寻找 logback.xml ⽂件，都找不到的话，就输出到控制台。

```java
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    
    <!--定义日志文件的存储地址 推荐在 LogBack 的配置中使用绝对路径-->
    <property name="LOG_HOME" value="/var/log" />
    
    <!-- Console 输出设置 -->
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <Target>System.out</Target>
        <encoder charset="UTF-8" class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
        	<pattern>%d{HH:mm:ss.SSS} [%thread] %level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
        
    <appender name="logfile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <Append>true</Append>
        <File>${LOG_HOME}/logfile.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/logfile.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
     </appender>
            
     <appender name="error_file" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${LOG_HOME}/error.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
            <fileNamePattern>${LOG_HOME}/error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [ %t:%r ] - [ %p ] %m%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
     </appender>
            
     <!-- 指定包下或指定类下的日志级别 -->       
     <!-- <logger name="com.boot" level="INFO"></logger> -->
            
     <root level="debug">
        <appender-ref ref="stdout"/>
        <appender-ref ref="logfile"/>
        <appender-ref ref="error_file"/>
     </root>
</configuration>
```

### 配置文件解析

#### xml文件结构

```java
<configuration>
    <!-- 0或多个 -->
    <appender>...</appender>
    <!-- 0或多个 -->
    <logger>...</logger>
    <!-- 最多一个 -->
    <root>...</root>
</configuration>
```

#### 配置 appender

```java
<!--  name属性指定名字，class属性指定⽬的地 -->        
<appender name="debug_file" class="ch.qos.logback.core.rolling.RollingFileAppender">
	<Append>true</Append>
	<!-- 正在记录的日志文件的路径及文件名 -->
	<File>${LOG_HOME}/debug.log</File>
	<rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
		<!-- 日志归档路径以及格式,按天滚动 -->
		<fileNamePattern>${LOG_HOME}/debug.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
        <!-- 保存 30 天的历史记录，最⼤⼤⼩为 3GB -->
        <maxHistory>30</maxHistory>
        <totalSizeCap>3GB</totalSizeCap>
        <!--配置单个日志文件不能超过100M，若超过100M，日志文件会以索引0开始，命名日志文件-->
        <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
			<maxFileSize>100MB</maxFileSize>
		</timeBasedFileNamingAndTriggeringPolicy>
	</rollingPolicy>
	<!-- encoder负责把⽇志信息转换成字节数组，并且把字节数组写到输出流。
            class属性可省，PatternLayoutEncoder类表示自定义日志输出格式 -->        
    <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
		<pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        <charset>UTF-8</charset> <!-- 此处设置字符集 -->
    </encoder>  
    <!-- 过滤策略：
        LevelFilter ： 只打印level标签设置的日志级别
        ThresholdFilter：打印大于等于level标签设置的级别，小的舍弃
    -->
    <!--<filter class="ch.qos.logback.classic.filter.LevelFilter">
        <!-- 过滤的日志级别 -->
        <level>debug</level>
        <!--匹配到就允许-->
        <onMatch>ACCEPT</onMatch>
        <!--没有匹配到就禁止-->
        <onMismatch>DENY</onMismatch>
	</filter>-->    
</appender>        
```

**日志的输出⽬的地：**

- `ch.qos.logback.core.ConsoleAppender`：输出到控制台。

- `ch.qos.logback.core.FileAppender`：输出到⽂件。

- `ch.qos.logback.core.rolling.RollingFileAppender`：滚动记录文件，当符合某个条件时，产⽣⼀个新⽂件。

- 除了输出到本地，还可以通过 SocketAppender 和 SSLSocketAppender 输出到远程设备，通过SMTPAppender 输出到邮件。甚⾄可以通过 DBAppender 输出到数据库中。



**日志的输出格式：**

- `%d` ：输出的时间格式。

- `%thread` ：⽇志的线程名。

- `%-5level` ：⽇志的输出级别，填充到 5 个字符。⽐如说 info 只有 4 个字符，就填充⼀个空格，这样⽇志信息就对⻬了。

- `%logger{length}` ：logger 的名称，length ⽤来缩短名称。没有指定表示完整输出；0 表示只输出 logger 最右边点号之后的字符串；其他数字表示输出⼩数点最后边点号之前的字符数量。(logger对象名称，`private static final Logger logger = LoggerFactory.getLogger(LogbackDemo.class);`中logger对象全称为`com.itan.application.demo.LogbackDemo`)

- `%msg` ：⽇志的具体信息。

- `%n` ：换⾏符。

- `%relative` ：输出从程序启动到创建⽇志记录的时间，单位为毫秒。

**日志的输出策略**：

RollingFileAppender 需要指定 RollingPolicy 和 TriggeringPolicy，前者负责⽇志的滚动功能，后者负责⽇志滚动的时机。如果 RollingPolicy 也实现了 TriggeringPolicy 接⼝，那么只需要设置RollingPolicy 就好了。

TimeBasedRollingPolicy 和 SizeAndTimeBasedRollingPolicy 是两种最常⽤的滚动策略。

TimeBasedRollingPolicy 同时实现了 RollingPolicy 与 TriggeringPolicy 接⼝，因此使⽤TimeBasedRollingPolicy 的时候就可以不指定 TriggeringPolicy。



`TimeBasedRollingPolicy` 可以指定以下属性：

- `fileNamePattern`，⽤来定义⽂件的名字（必选项）。它的值应该由⽂件名加上⼀个 %d 的占位符。 %d 应该包含 java.text.SimpleDateFormat 中规定的⽇期格式，缺省是 yyyy-MM-dd 。滚动周期是通过 fileNamePattern 推断出来的。

- `maxHistory`，最多保留多少数量的⽇志⽂件（可选项），将会通过异步的⽅式删除旧的⽂件。⽐如，你指定按⽉滚动，指定maxHistory = 6 ，那么 6 个⽉内的⽇志⽂件将会保留，超过 6 个⽉的将会被删除。

- `totalSizeCap`，所有⽇志⽂件的⼤⼩（可选项）。超出这个⼤⼩时，旧的⽇志⽂件将会被异步删除。需要配合 maxHistory 属性⼀起使⽤，并且是第⼆条件。

SizeAndTimeBasedRollingPolicy ⽐ TimeBasedRollingPolicy 多了⼀个⽇志⽂件⼤⼩设定的属性：maxFileSize，其他完全⼀样。

```java
<rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
	<fileNamePattern>${LOG_HOME}/error.%d{yyyy-MM-dd}.%i.log</fileNamePattern>
	<maxFileSize>100MB</maxFileSize>
	<maxHistory>30</maxHistory>
	<totalSizeCap>3GB</totalSizeCap>
</rollingPolicy>
```



#### 配置 **root**

```java
<!-- 只⽀持⼀个属性——level，值可以为：TRACE、DEBUG、INFO、WARN、ERROR、ALL、OFF -->
<root level="debug">
    <!-- appender-ref⽤来指定具体的appender -->
    <appender-ref ref="stdout"/>
    <appender-ref ref="D"/>
    <appender-ref ref="E"/>
</root>
```

#### 日志异步到数据库

```java
<appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
    <connectionSource class="ch.qos.logback.core.db.DriverManagerConnectionSource">
        <!–连接池 –>
        <dataSource class="com.mchange.v2.c3p0.ComboPooledDataSource">
            <driverClass>com.mysql.jdbc.Driver</driverClass>-->
            <url>jdbc:mysql://127.0.0.1:3306/databaseName</url>-->
			<user>root</user>
    		<password>root</password>
    	</dataSource>
    </connectionSource>
</appender>
```



#### ⾃动重载配置

```java
<!-- scanPeriod设定扫描时间间隔，默认一分钟一次，单位可以是毫秒（milliseconds）、秒（seconds）、分钟（minutes）或者⼩时（hours），默认的时间单位为毫秒。 -->
<configuration scan="true" scanPeriod="30 seconds">
 ...
</configuration>
```

#### 查看内部状态信息

可以在代码中通过 StatusPrinter 来打印 Logback 内部状态信息，也可以通过在 configuration 上开启debug 来打印内部状态信息。

```java
<!-- debug:当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false -->
<configuration debug="true">
 ...
</configuration>
```



### 打印日志

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class LogbackDemo {
    private static final Logger logger = LoggerFactory.getLogger(LogbackDemo.class);
    public static void main(String[] args) {
        logger.debug("{}是晴朗的一天","今天");
    }
}
```

### SpringBoot集成

类路径下默认加载logback.xml或者logback-spring.xml文件，也可以手动指定配置文件`logging.config=classpath:logback-spring.xml` 

```java
//在logback.xml文件中指定多环境日志级别(配合SpringBoot配置文件中spring.profiles.active=dev使用)
<configuration>
    <!--开发环境:输出到文件-->
    <springProfile name="dev">
        <!-- 使用mybatis的时候，sql语句是debug下才会打印，可以单独给dao下目录配置debug模式 -->
        <logger name="com.cic.analysis.business.dao" level="debug" />
        <root level="DEBUG">
            <appender-ref ref="CONSOLE" />
            <appender-ref ref="DEBUG_FILE" />
        </root>
    </springProfile>

    <!--生产环境:输出到文件-->
    <springProfile name="pro">
        <logger name="com.example" level="WARN" />
        <root level="INFO">
            <appender-ref ref="INFO_FILE" />
        </root>
    </springProfile>
</configuration>
        
//可以引入SpringBoot配置文件中参数application.properties
log.path=logs/
//logback.xml
<configuration>
    <!-- 使用${logPath}获取属性值 -->    
	<springProperty scope="context" name="logPath" source="log.path"/>   
        ...
</configuration>        
```

## Log4j 2

### 依赖

```java
<!-- 在version中体现是log4j 2 -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.5</version>
</dependency> 
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.5</version>
</dependency>
```

###  log4j2.xml

默认寻找 4 种类型的配置⽂件，后缀分别是 properties、yaml、json 和 xml。前缀是 log4j2-test 或者 log4j2。

```java
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
	<Appenders>
        <Console name="Console" target="SYSTEM_OUT">
        	<PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    
    	<File name="DebugFile" fileName="debug.log">
            <PatternLayout>
                <Pattern>%d %p %c [%t] %m%n</Pattern>
            </PatternLayout>
        </File>
    
    	<!-- 以配合⽂件的⽅式来异步写⼊ -->
    	<Async name="Async">
        	<AppenderRef ref="DebugFile"/>
        </Async>
    
    	<RollingFile name="RollingFile" fileName="rolling.log" filePattern="rolling-%d{yyyy-MM-dd}-%i.log">
            <PatternLayout>
            	<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
            </PatternLayout>
            <Policies>
            	<SizeBasedTriggeringPolicy size="1 KB"/>
            </Policies>
        </RollingFile>
    
    	<!-- fileName 的属性值中包含了⼀个⽬录 gz，也就是说⽇志⽂件都将放在这个⽬录下。
    		 filePattern 的属性值中增加了⼀个 gz 的后缀，这就表明⽇志⽂件要压缩为.gz文件。也可以是 zip 格式 -->
    	<RollingFile name="RollingFileGZ" fileName="gz/rolling.log" filePattern="gz/%d{yyyy-MM-dd-HH}-%i.rolling.gz">
            <PatternLayout>
            	<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
            </PatternLayout>
            <Policies>
            	<SizeBasedTriggeringPolicy size="1 KB"/>
            </Policies>
        </RollingFile>
    
    </Appenders>
    <!-- 指定Root的⽇志级别，并且指定具体启⽤哪⼀个Appenders -->
    <Loggers>
        <Root level="DEBUG">
        	<AppenderRef ref="Console"/>
    		<AppenderRef ref="Async"/>
    		<AppenderRef ref="RollingFile"/>
        </Root>
    </Loggers>
</Configuration>
```



### 配置文件解析

#### xml文件结构

```java
<Configuration>
    <!-- 0或多个 -->
    <Appenders>...</Appenders>
    <!-- 0或多个 -->
    <Loggers>...</Loggers>
    <!-- 最多一个 -->
    <Root>...</Root>
</Configuration>
```

#### 配置appender

```java
<Appenders>
    <RollingFile name="RollingFile" fileName="rolling.log" filePattern="rolling-%d{yyyy-MM-dd}-%i.log">
    	<PatternLayout>
    		<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
    	</PatternLayout>
    	<Policies>
    		<SizeBasedTriggeringPolicy size="1 KB"/>
    	</Policies>
    </RollingFile>
</Appenders>
```

**日志的输出格式**

- `%d{HH:mm:ss.SSS}` 表示输出到毫秒的时间

- `%t` 输出当前线程名称

- `%-5level` 输出⽇志级别，-5 表示左对⻬并且固定输出 5 个字符，如果不⾜在右边补空格

- `%logger` 输出 logger 名称，最多 36 个字符

- `%msg` ⽇志⽂本

- `%n` 换⾏

- `%F` 输出所在的类⽂件名，如 Demo.java

- `%L` 输出⾏号

- `%M` 输出所在⽅法名

- `%l` 输出语句所在的⾏数, 包括类名、⽅法名、⽂件名、⾏数

- `%p` 输出⽇志级别

- `%c` 输出包名，如果后⾯跟有` {length.}` 参数，⽐如说 `%c{1.}` ，它将输出报名的第⼀个字符，如 com.itwanger 的实际报名将只输出 c.i

**日志的输出策略**：

RollingFile 会根据Triggering（触发）策略和 Rollover（过渡）策略来进⾏⽇志⽂件滚动。如果没有配置 Rollover，则使⽤ DefaultRolloverStrategy 来作为 RollingFile 的默认配置。

触发策略包含有，基于 cron 表达式的 CronTriggeringPolicy；基于⽂件⼤⼩的 SizeBasedTriggeringPolicy；基于时间的TimeBasedTriggeringPolicy。

过渡策略包含有，默认的过渡策略 DefaultRolloverStrategy，直接写⼊的DirectWriteRolloverStrategy。⼀般情况下，采⽤默认的过渡策略即可，它已经⾜够强⼤。

RollingFile可以指定如下属性：

- fileName ⽤来指定⽂件名。

- filePattern ⽤来指定⽂件名的模式，它取决于过渡策略。第⼀个⽇志⽂件名为 rolling.log（最近的⽇志放在这个⾥⾯），第⼆个⽂件名除去⽇期为 rolling-1.log，第⼆个⽂件名除去⽇期为 rolling-2.log，根据DefaultRolloverStrategy的max属性，直到rolling-8.log 要⽣成的时候，删除 rolling-1.log

DefaultRolloverStrategy的属性：

- fileIndex 默认值是max，索引值较高的比较小的先更新，如果是min，则相反
- min 计数器的最小值，默认为1
- max 计数器的最大值，默认为7，达到这个值后，旧的日志文件被删除
- compressionLevel 压缩级别，从0-9，0为无，1为最佳速度，9为最佳压缩，仅针对zip文件
- tempCompressionFilePatten 日志文件压缩时的文件名模式

SizeBasedTriggeringPolicy，基于⽇志⽂件⼤⼩的时间策略，⼤⼩以字节为单位，后缀可以是 KB，MB 或 GB，例如 20 MB。

#### 自动重载配置

```java
<!-- 注意值要设置成⾮零，⾄少30秒后检查配置⽂件中的更改。最⼩间隔为5秒。 -->
<Configuration monitorInterval="30">
...
</Configuration>
```



### 打印日志

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class Demo {
    private static final Logger logger = LogManager.getLogger(Demo.class);
    public static void main(String[] args) {
        logger.debug("log4j2");
    }
}
```















