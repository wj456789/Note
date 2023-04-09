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
