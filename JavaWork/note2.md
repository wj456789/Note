# Note

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



## JVM

### 类加载

在同一目录下的jar包，JVM是按照jar包的先后顺序进行加载，一旦一个全路径名相同的类被加载之后，后面再有相同的类便不会进行加载了。

#### Java类加载的过程

Java代码从编码完成到运行，包含两个步骤：

编译：把写好的java文件通过javac命令编译成字节码（.class文件）。
运行：把字节码文件交给JVM执行。
类加载的过程就是JVM把.class文件中类信息加载进内存，并解析生成class对象的过程。这个过程主要为3步：加载、链接、初始化，而链接可以分为3小步：验证、准备、解析，每个过程主要过程如下： 

1. 加载：把各个来源的的class字节码文件通过不同类加载器载入内存。
2. 验证：保证加载进来的字节流符合虚拟机规范，不会造成安全规范。验证包括对于文件格式的验证，比如常量中是否有不被支持的常量？文件中是否有不规范的或者附加的其他信息？对于元数据的验证，比如该类是否继承了被final修饰的类？类中的字段，方法是否与父类冲突？是否出现了不合理的重载；对于字节码的验证，保证程序语义的合理性，比如要保证类型转换的合理性。对于符号引用的验证，比如校验符号引用中通过全限定名是否能够找到对应的类？校验符号引用中的访问性（private，public等）是否可被当前类访问？
3. 准备：为变量分配内存，并且赋予初值，初值不是代码中的初始化的值而是根据不同变量设置默认值，其中引用类型为null。
4. 解析：常量池内的符号引用替换为直接引用的过程。例如调用hello()方法，替换为方法的内存地址。
5. 初始化：对static修饰的变量或语句进行初始化。

#### 类加载器的隔离问题

每个类装载器都有一个自己的命名空间用来保存已装载的类。当一个类装载器装载一个类时，它会通过保存在命名空间里的类全局限定名进行搜索来检测这个类是否已经被加载了。

JVM 对类唯一的识别是 ClassLoader id + PackageName + ClassName，所以一个运行程序中是有可能存在两个包名和类名完全一致的类的。并且如果这两个类不是由一个 ClassLoader 加载，是无法将一个类的实例强转为另外一个类的，这就是 ClassLoader 隔离性。

为了解决类加载器的隔离问题，JVM引入了双亲委派机制。

#### 双亲委派机制

双亲委派机制的核心有两点：第一，自底向上检查类是否已加载；其二，自顶向下尝试加载类。 

类加载器通常有四类：启动类加载器、拓展类加载器、应用程序类加载器和自定义类加载器。

暂且不考虑自定义类加载器，JDK自带类加载器具体执行过程如下：

第一：当AppClassLoader加载一个class时，会把类加载请求委派给父类加载器ExtClassLoader去完成；

第二：当ExtClassLoader加载一个class时，会把类加载请求委派给BootStrapClassLoader去完成；

第三：如果BootStrapClassLoader加载失败（例如在%JAVA_HOME%/jre/lib里未查找到该class），会使用ExtClassLoader来尝试加载；

第四：如果ExtClassLoader也加载失败，则会使用AppClassLoader来加载，如果AppClassLoader也加载失败，则会报出异常ClassNotFoundException。

#### ClassLoader的双亲委派实现

ClassLoader通过loadClass()方法实现了双亲委托机制，用于类的动态加载。

该方法的源码如下：

```java
protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException{
    synchronized (getClassLoadingLock(name)) {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            long t0 = System.nanoTime();
            try {
                if (parent != null) {
                    c = parent.loadClass(name, false);
                } else {
                    c = findBootstrapClassOrNull(name);
                }
            } catch (ClassNotFoundException e) {
                // ClassNotFoundException thrown if class not found
                // from the non-null parent class loader
            }

            if (c == null) {
                // If still not found, then invoke findClass in order
                // to find the class.
                long t1 = System.nanoTime();
                c = findClass(name);

                // this is the defining class loader; record the stats
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }
}
```

双亲是层级关系的称呼方式，层级关系并不是继承的关系，而是组合，每个类加载器都有parent字段来定义上级。 

loadClass方法本身是一个递归向上调用的过程，上述代码中从parent.loadClass的调用就可以看出。

在执行其他操作之前，首先通过findLoadedClass方法从最底端的类加载器开始检查是否已经加载指定的类。如果已经加载，则根据resolve参数决定是否要执行连接过程，并返回Class对象。

而Jar包冲突往往发生在这里，当第一个同名的类被加载之后，在这一步检查时就会直接返回，不会再加载真正需要的类。那么，程序用到该类时就会抛出找不到类，或找不到类方法的异常。

#### Jar包的加载顺序

上面已经看到一旦一个类被加载之后，全局限定名相同的类可能就无法被加载了。而Jar包被加载的顺序直接决定了类加载的顺序。

决定Jar包加载顺序通常有以下因素：

- 第一，Jar包所处的加载路径。也就是加载该Jar包的类加载器在JVM类加载器树结构中所处层级。上面讲到的四类类加载器加载的Jar包的路径是有不同的优先级的。
- 第二，文件系统的文件加载顺序。因Tomcat、Resin等容器的ClassLoader获取加载路径下的文件列表时是不排序的，这就依赖于底层文件系统返回的顺序，当不同环境之间的文件系统不一致时，就会出现有的环境没问题，有的环境出现冲突。

#### Jar包冲突的通常表现

Jar包冲突往往是很诡异的事情，也很难排查，但也会有一些共性的表现。

- 抛出java.lang.ClassNotFoundException：典型异常，主要是依赖中没有该类。导致原因有两方面：第一，的确没有引入该类；第二，由于Jar包冲突，Maven仲裁机制选择了错误的版本，导致加载的Jar包中没有该类。
- 抛出java.lang.NoSuchMethodError：找不到特定的方法。Jar包冲突，导致选择了错误的依赖版本，该依赖版本中的类对不存在该方法，或该方法已经被升级。
- 抛出java.lang.NoClassDefFoundError，java.lang.LinkageError等，原因同上。
- 没有异常但预期结果不同：加载了错误的版本，不同的版本底层实现不同，导致预期结果不一致。

#### Tomcat启动时Jar包和类的加载顺序

最后，梳理一下Tomcat启动时，对Jar包和类的加载顺序，其中包含上面提到的不同种类的类加载器默认加载的目录：

- $java_home/lib 目录下的java核心api；
- $java_home/lib/ext 目录下的java扩展jar包；
- java -classpath/-Djava.class.path所指的目录下的类与jar包；
- $CATALINA_HOME/common目录下按照文件夹的顺序从上往下依次加载；
- $CATALINA_HOME/server目录下按照文件夹的顺序从上往下依次加载；
- $CATALINA_BASE/shared目录下按照文件夹的顺序从上往下依次加载；
- 项目路径/WEB-INF/classes下的class文件；
- 项目路径/WEB-INF/lib下的jar文件；

上述目录中，同一文件夹下的Jar包，按照顺序从上到下一次加载。如果一个class文件已经被加载到JVM中，后面相同的class文件就不会被加载了。

[Maven Jar包冲突？看看高手是怎么解决的](https://ost.51cto.com/posts/16325)

[java同时引用不同版本同一个jar包](https://blog.csdn.net/white_grimreaper/article/details/120921270)

[Java破坏双亲委派实现自定义加载器加载不同版本类](https://blog.csdn.net/u011943534/article/details/89204709)

[Java 自定义 ClassLoader 实现隔离运行不同版本jar包的方式](https://blog.csdn.net/t894690230/article/details/73252331?spm=1001.2101.3001.6650.7&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&utm_relevant_index=13)

[Java插件开发之类加载隔离](https://www.cnblogs.com/xmzpc/p/15187495.html)

[java的类加载器以及如何自定义类加载器](https://blog.csdn.net/blueheartstone/article/details/127784519)





https://cloud.tencent.com/developer/article/1832222

https://cloud.tencent.com/developer/article/1915650

https://www.zhihu.com/question/466696410

https://cloud.tencent.com/developer/article/1890187

https://juejin.cn/post/6865572557329072141
