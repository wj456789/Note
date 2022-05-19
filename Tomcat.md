# tomcat

## 内存配置

在`bin/catalina.sh`的开头添加

```sh
JAVA_OPTS="$JAVA_OPTS -Xms2048m -Xmx2048m -Xss256K -XX:PermSize=256m -XX:MaxPermSize=1024m"
```

参考：
[Linux下设置export JAVA_OPTS选项进行tomcat JVM内存设置](https://blog.csdn.net/buster2014/article/details/46423135)

## JDK配置

在`catalina.sh`开头添加

```sh
export JRE_HOME=/usr/java/jdk1.7.0_79/jre
export JAVA_HOME=/usr/java/jdk1.7.0_79
export TOMCAT_HOME=/usr/local/tomcat
#配置CATALINA_HOME(tomcat安装的根目录)    
export CATALINA_HOME=/usr/local/tomcat
```

参考：
[Tomcat启动分析(我们为什么要配置CATALINA_HOME环境变量)](https://www.cnblogs.com/heshan664754022/archive/2013/03/27/2984357.html)

## server.xml配置

```xml
<Server port="8005" shutdown="SHUTDOWN" debug="0">
    <Service name="Catalina">
        <Connector port="8080" 
                   maxHttpHeaderSize="8192" 
                   maxThreads="150" 
                   minSpareThreads="25" 	
                   maxSpareThreads="75" 
                   enableLookups="false" 
                   redirectPort="8443" 
                   acceptCount="100" 	
                   connectionTimeout="20000" 
                   disableUploadTimeout="true" />
        <Engine name="Catalina" defaultHost="localhost" debug="0">
               <Host name="localhost" debug="0" appBase="webapps" unpackWARs="true" autoDeploy="false">
                   <Logger />
                   <Context path="" docBase="app" debug="0" reloadable="false"/>
               </host>
        </Engine>
    </Service>
</Server>
```

- Service(可以有多个)

  - name：指定service的名字

- Connector(表示客户端和service之间的连接，负责接收客户的请求，分配线程让Engine处理请求，以及向客户端回送响应的消息，可以有多个)

  - port：指定服务器端要创建的端口号，并在这个端口监听来自客户端的请求

- Engine(表示指定service中的请求处理机，也就是Servlet容器，负责接收和处理来自Connector的请求，只能有一个)

  - defaultHost：指定处理请求的主机名，它至少与其中的一个host元素的name属性值是一样的

- host(表示一个虚拟主机)

  - name：指定主机名
  - appBase：虚拟主机存放web应用的目录,相对于CATALINA_HOME的路径
  - unpackWARs：设置为true，部署时war包会自动解压
  - autoDeploy：在webapps目录中增加新的目录、war文件时，设置为true会重新部署，建议关闭

- Context(表示一个web应用程序)

  - docBase：每个web应用的存放目录，相对于appBase路径，注意classpath是WEB-INF下的classes目录
  - path	表示此web应用程序的url的前缀，这样请求的url为`http://localhost:8080/path/`

- reloadable

  如果为true，则tomcat会自动检测应用程序的/WEB-INF/lib 和/WEB-INF/classes目录的变化，自动装载新的应用程序,我们可以在不重起tomcat的情况下改变应用程序，建议关闭

### Connector配置详解

根据协议的不同，Connector可以分为HTTP Connector、AJP Connector等，比如下面三种连接器：

```xml
<!-- 1. HTTP:这个连接器监听8080端口，负责建立HTTP连接。在通过浏览器访问Tomcat服务器的Web应用时，使用的就是这个连接器。 -->
<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />

<!-- 2. HTTPS -->
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true" scheme="https" secure="true"
           clientAuth="false" sslProtocol="TLS" />

<!-- 3. AJP：这个连接器监听8009端口，AJP连接器可以通过AJP协议和一个web容器进行交互。当你想让Apache和Tomcat结合并且你想让Apache处理静态内容，或者你想利用Apache的SSL处理能力时，就需要用到这个连接器。 -->
<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

Connector初始化/启动的时候,将初始化/启动内部的ProtocolHandler。每一个ProtocolHandler内部都有一个Endpoint，ProtocolHandler初始化/启动的时候，将初始化/启动其内部的Endpoint。

**Tomcat有三种Endpoint：JIoEndpoint、NioEndpoint、AprEndpoint**

- JIoEndpoint：使用java io(也称为bio)技术，即一个请求相应一个线程。缺点：并发量高时，线程数较多，占资源
- NioEndpoint：使用java nio技术，能够通过少量的线程处理大量的请求
- AprEndpoint：Apr即Apache Portable Runtime，从操作系统层面解决io堵塞问题。使用Apr时将用到Tomcat\bin\tcnative-1.dll (使用Apr时，应用了jni技术)。

Tomcat 8默认配置下，HTTP Connector和AJP Connector均使用Apr。

**ProtocolHandler与Endpoint的相应关系**

- Http11Protocol/AjpProtocol使用JIoEndpoint
- Http11NioProtocol/AjpNioProtocol使用NioEndpoint
- Http11AprProtocol/AjpAprProtocol使用AprEndpoint

参考：

[HTTP/AJP Connector、Bio/Nio/Apr性能对照](https://www.cnblogs.com/gcczhongduan/p/4494509.html)

#### HTTP连接器详解

```xml
<Connector port="8080"   
	protocol="HTTP/1.1"   
	maxThreads="1000"   
	minSpareThreads="50"  
    maxConnections="1000"  
	acceptCount="1000"  
	connectionTimeout="20000"   
	maxHttpHeaderSize="8192"  
	tcpNoDelay="true"  
	compression="on"  
	compressionMinSize="2048"  
	disableUploadTimeout="true"  
	redirectPort="8443"  
	enableLookups="false"  
	URIEncoding="UTF-8" /> 
```

1. port：代表Tomcat监听端口，也就是网站的访问端口
2. protocol：协议类型，可选类型有四种，分别为BIO(阻塞型IO)，NIO，NIO2和APR。Tomcat7中支持BIO、NIO和APR，Tomcat8增加了对NIO2的支持，而到了Tomcat8.5和Tomcat9.0，则去掉了对BIO的支持。
   - BIO：`protocol="HTTP/1.1"` 
   - NIO：`protocol="org.apache.coyote.http11.Http11NioProtocol" ` 
   - NIO2：`protocol="org.apache.coyote.http11.Http11Nio2Protocol"`  
   - APR：`protocol="org.apache.coyote.http11.Http11AprProtocol"`，是Apache HTTP服务器的支持库,为上层的应用程序提供一个可以跨越多操作系统平台使用的底层支持接口库,Tomcat将以JNI的形式调用 Apache HTTP服务器的核心动态链接库来处理文件读取或网络传输操作

3. maxThreads：由该连接器创建的处理请求线程的最大数目，也就是可以处理的同时请求的最大数目。如果未配置默认值为200。一般情况下设置成1000即可。
4. minSpareThreads：线程的最小运行数目，这些始终保持运行。如果未指定，默认值为10。
5. maxConnections：在任何给定的时间内，服务器将接受和处理的最大连接数。当这个数字已经达到时，服务器将接受但不处理，等待进一步连接。NIO与NIO2的默认值为10000，APR默认值为8192。
6. acceptCount：当所有的请求处理线程都在使用时传入连接请求等待的最大队列长度。如果未指定，默认值为100。一般是设置的跟 maxThreads一样或一半，此值设置的过大会导致排队的请求超时而未被处理。所以这个值应该是主要根据应用的访问峰值与平均值来权衡配置。
7. connectionTimeout：当请求已经被接受，但未被处理，也就是等待中的超时时间。单位为毫秒，默认值为60000。通常情况下设置为30000。
8. maxHttpHeaderSize：请求和响应的HTTP头的最大大小，以字节为单位指定。如果没有指定，这个属性被设置为8192（8 KB）。
9. tcpNoDelay：如果为true，服务器socket会设置TCP_NO_DELAY选项，在大多数情况下可以提高性能。缺省情况下设为true。
10. compression：是否启用gzip压缩，默认为关闭状态。这个参数的可接受值为“off”（不使用压缩），“on”（压缩文本数据），“force”（在所有的情况下强制压缩）。
11. compressionMinSize：如果compression="on"，则启用此项。被压缩前数据的最小值，也就是超过这个值后才被压缩。如果没有指定，这个属性默认为“2048”（2K），单位为byte。
12. disableUploadTimeout：这个标志允许servlet的Container(容器)在一个servlet执行的时候，使用一个不同的，更长的连接超时。最终的结果是给servlet更长的时间以便完成其执行，或者在数据上载的时候更长的超时时间。如果没有指定，设为false。
13. enableLookups：关闭DNS反向查询。
14. URIEncoding：URL编码字符集。

参考：
[Tomcat优化详细教程](https://blog.csdn.net/qq_23994787/article/details/79479686)

### 项目部署

- 将应用文件夹或war文件直接放到tomcat的appBase目录下，这样tomcat启动的时候会将appBase目录下的文件夹或war文件的内容当成应用部署。这种方式最简单且无须书写任何配置文件,但是不能指定特定的context path,访问路径为`http://host:port/文件夹名称`。

- 在tomcat的server.xml配置文件中的Host节点下增加Context子节点，如：

  ```xml
  <!-- 当项目为war包并且unpackWARs="true"时，部署文件夹为CATALINA_HOME/appBase/path",当path=""时，部署文件夹为CATALINA_HOME/appBase/ROOT"，此时需要将原有ROOT目录删除；当项目为文件夹时，部署文件夹即为此文件夹； -->
  <Context path="/test" docBase="D:\private\tomcat\test.war" reloadable="false"/>
  ```

  访问路径为`http://host:port/path`, 其中，path即contextpath；docBase指向应用所在的文件夹或war文件，可以是绝对路径，也可以是相对路径（相对该Context所在的Host的appBase属性值）。

- 在tomcat的conf/[Engine]/[Host]目录下新建xml文件，文件名为context path，当path=""时，文件命名为ROOT.xml,内容如下：

  ```xml
  <Context docBase="D:\private\tomcat\test.war" privileged="true" antiResourceLocking="false" antiJARLocking="false"/>
  ```

使用后面两种方法时，如果将应用文件夹或war文件放在webapps目录下，第一种方法也会实行，会造成重复部署项目导致webapp.root重复错误

### 静态资源配置

- 在webapps下面新建文件夹picture/,访问路径`http://ip:port/picture/1.png`可以直接访问


- 配置server.xml文件

  ```xml
  <Context path="picture" docBase="/usr/local/picture" reloadable="true" ></Context>
  ```

   其中path是映射的虚拟路径，docBase是静态资源存放的真实物理路径，reloadable指有文件更新时，是否重新加载，一般设置为 true后，tomcat不需要重启启动，自动热加载。访问路径`http://ip:port/picture/1.png`也可以访问

### 访问首页

web项目默认访问首页是index.html、index.htm或index.jsp，页面放在项目根目录下

可以在web.xml文件中配置默认访问首页：

```xml
<web-app>
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
    ...
</web-app>
```





server.xml配置参考:
[tomcat中server.xml配置详解](https://www.cnblogs.com/aaron911/p/11006493.html)

[Tomcat的context.xml说明、Context标签讲解](https://www.cnblogs.com/wangxiaoyang/p/3627799.html)

[tomcat deploy部署项目三种方法](https://www.cnblogs.com/youxin/p/5274858.html)

## 常见BUG

**项目部署在linux下root目录**
将项目部署在root目录下可以直接省略上下文，此时webapps 下如果有tomcat自带的项目应该删除，如果不删除的话root下的项目不应该有 manager之类的tomcat自带项目的访问路径 否则的话使用此类项目会抛出404的异常



**IDEA中的tomcat配置**
参考：
[idea建立web项目servlet映射的地址/jsp访问不到](https://www.cnblogs.com/Jacck/p/7704225.html)

**无法在web.xml或使用此应用程序部署的jar文件中解析绝对uri：[http://java.sun.com/jsp/jstl/core]**
原因：
找不到jstl类库中的.tld文件
解决办法：
WEB/INF的lib下，除了导入jstl.jar包，还要导入standard.jar包。另外，解压standard.jar包，把.tld文件放在WEB/INF下。



## 附录

环境centos7 tomcat8.5.59，对于server.xml不同配置，部署后localhost:8080访问测试实例

```xml
<Host name="localhost"  appBase="webapps"  unpackWARs="true" autoDeploy="true">
            <Context path="" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ROOT		tomcat原有的猫，localhost:8080访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH		项目解压文件夹
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH.war	项目war包放这里

改为：
<Host name="localhost"  appBase=""  unpackWARs="true" autoDeploy="true">
            <Context path="" docBase="webapps/ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/ROOT		重新生成的ROOT，里面是项目，localhost:8080访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH.war	项目war包放这里


改为：
<Host name="localhost"  appBase=""  unpackWARs="true" autoDeploy="true">
            <Context path="" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/ROOT		重新生成的ROOT，里面是项目，localhost:8080访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH		项目解压文件夹
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH.war	项目war包放这里








改为：
<Host name="localhost"  appBase=""  unpackWARs="true" autoDeploy="true">
            <Context path="ZXEPWH" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH		项目解压文件夹，localhost:8080/ZXEPWH访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH.war	项目war包放这里

改为：
<Host name="localhost"  appBase=""  unpackWARs="true" autoDeploy="true">
            <Context path="ZXEPWH" docBase="webapps/ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH		项目解压文件夹，localhost:8080/ZXEPWH访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH.war	项目war包放这里

改为：
<Host name="localhost"  appBase="webapps"  unpackWARs="true" autoDeploy="true">
            <Context path="ZXEPWH" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH	项目解压文件夹，localhost:8080/ZXEPWH访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH.war	项目war包放这里




改为：
<Host name="localhost"  appBase="webapps"  unpackWARs="true" autoDeploy="true">
            <Context path="ZXEP" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEP	项目文件夹，localhost:8080/ZXEP访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH	项目解压文件夹
/usr/local/tomcat/apache-tomcat-8.5.59/webapps/ZXEPWH.war	项目war包放这里

改为：
<Host name="localhost"  appBase=""  unpackWARs="true" autoDeploy="true">
            <Context path="ZXEP" docBase="ZXEPWH" debug="0" reloadable="true" />
</Host>
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEP	项目文件夹，localhost:8080/ZXEP访问的是这个
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH	项目解压文件夹
/usr/local/tomcat/apache-tomcat-8.5.59/ZXEPWH.war	项目war包放这里
```

参考:
[tomcat部署应用时设置context path为空的上下文路径问题](https://blog.csdn.net/weixin_30247781/article/details/95515418)











