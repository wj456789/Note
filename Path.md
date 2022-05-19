# path

> 接收一个路径参数，返回一个URL对象或者文件流，表示的是name指向的资源(文件或文件夹)或者资源流；

**类加载**

- `URL this.getClass().getResource(name)`					

- `InputStream this.getClass().getResourceAsStream(name)`

**类加载器加载**

- `URL this.getClass().getClassLoader().getResource(name)`	

- `InputStream this.getClass().getClassLoader().getResourceAsStream(name)`

类加载的绝对路径和类加载器加载的相对路径保持一致，都是文件编译之后的根目录classes/，也就是类路径；类加载底层都是类加载器加载，`class.getResource`最终调用是`ClassLoader.getResource`，只是在这之前对参数进行了调整，如果类加载路径以"/"开头，则去除"/"；若无"/"，则把当前类的包名加在参数的前面

### 类加载

> 路径以"/"开头，表示绝对路径，指的是文件编译之后的根目录
> 路径不以"/"开头，表示相对路径，指的是文件所在的底层目录

```java
this.getClass().getResource("/")
this.getClass().getResource("/").getPath()
this.getClass().getResource("").getPath()

输出:
file:/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/com/iot/application/device/basics/remotepro/service/
```

### 类加载器加载

> 路径不能以"/"开头，只能是相对路径，指的是文件编译之后的根目录

```java
this.getClass().getClassLoader().getResource("").getPath()
Thread.currentThread().getContextClassLoader().getResource("").getPath()

输出：
/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/
```

### servlet加载

> servlet默认从web应用根目录下取资源，Tomcat下path是否以"/"开头无所谓

```java
URL servletContext.getResource(name)
InputStream servletContext.getResourceAsStream(name)
Set<String> servletContext.getResourcePaths(name);		//获取指定目录下的所有资源路径,如：

Set<String> paths=servletContext.getResourcePaths("/WEB-INF");//参数一定要以”/”开头，否则会报错
输出：
/WEB-INF/lib,/WEB-INF/classes,/WEB-INF/web.xml
```

### Resource工具类加载

```java
ResourceUtils.getURL("classpath:").getPath()

输出：
/E:/zhdg-iot/zhdg-device/target/classes/
```

### file加载

```java
new File("").getCanonicalPath()
new File("").getAbsolutePath()				//项目运行目录(tomcat中的bin目录，普通Java项目IDEA中的run configuration中可以配置)
new ClassPathResource("").getFile().getAbsolutePath()

输出：
E:\zhdg-iot\zhdg-device
E:\zhdg-iot\zhdg-device
E:\zhdg-iot\zhdg-device\target\classes
```

### 系统

```java
System.getProperty("user.dir")

输出：
E:\zhdg-iot\zhdg-device
```

### 所有路径

```java
System.getProperty("java.class.path")	//获取所有类路径
System.getProperty("java.library.path")	//获取所有库路径
```

### System.getProperty()

> 通过`System.getProperty()`可以获取系统的配置信息，`System.getProperty()`的参数总结:

```java
java.version	Java运行时环境版本
java.vendor	Java运行时环境供应商
java.vendor.url 	Java供应商的 URL
java.home 	Java安装目录
java.vm.specification.version 	Java虚拟机规范版本
java.vm.specification.vendor 	Java虚拟机规范供应商
java.vm.specification.name 	Java虚拟机规范名称
java.vm.version	 Java虚拟机实现版本
java.vm.vendor 	Java虚拟机实现供应商
java.vm.name 	Java虚拟机实现名称
java.specification.version 	Java运行时环境规范版本
java.specification.vendor 	Java运行时环境规范供应商
java.specification.name 	Java运行时环境规范名称
java.class.version 	Java类格式版本号
java.class.path 	Java类路径
java.library.path 	加载库时搜索的路径列表
java.io.tmpdir	默认的临时文件路径
java.compiler	要使用的 JIT 编译器的名称
java.ext.dirs 	一个或多个扩展目录的路径
os.name 	操作系统的名称
os.arch 	操作系统的架构
os.version 	操作系统的版本
file.separator 	文件分隔符（在 UNIX 系统中是“/”）
path.separator	路径分隔符（在 UNIX 系统中是“:”）
line.separator 	行分隔符（在 UNIX 系统中是“/n”）
user.name 	用户的账户名称
user.home 	用户的主目录
user.dir 	用户的当前工作目录
 




java.version : 1.8.0_121
java.vendor : Oracle Corporation
java.vendor.url : http://java.oracle.com/
java.home : C:\Program Files (x86)\Java\jdk1.8.0_121\jre
java.vm.specification.version : 1.8
java.vm.specification.vendor : Oracle Corporation
java.vm.specification.name : Java Virtual Machine Specification
java.vm.version : 25.121-b13
java.vm.vendor : Oracle Corporation
java.vm.name : Java HotSpot(TM) Client VM
java.specification.version : 1.8
java.specification.vendor : Oracle Corporation
java.specification.name : Java Platform API Specification
java.class.version : 52.0
java.class.path : E:\workspace\TestCode\build\classes;E:\eclipse\plugins\org.junit_4.11.0.v201303080030\junit.jar;E:\eclipse\plugins\org.hamcrest.core_1.3.0.v201303031735.jar
java.library.path : C:\Program Files (x86)\Java\jdk1.8.0_121\bin;C:\windows\Sun\Java\bin;C:\windows\system32;C:\windows;C:/Program Files (x86)/Java/jre1.8.0_121/bin/client;C:/Program Files (x86)/Java/jre1.8.0_121/bin;C:/Program Files (x86)/Java/jre1.8.0_121/lib/i386;C:\ProgramData\Oracle\Java\javapath;C:\windows\system32;C:\windows;C:\windows\System32\Wbem;C:\windows\System32\WindowsPowerShell\v1.0\;C:\Program Files\Lenovo Fingerprint Reader\;C:\Program Files\Lenovo Fingerprint Reader\x86\;C:\Program Files (x86)\Java\jdk1.8.0_121\bin;C:\Program Files (x86)\Java\jdk1.8.0_121\jre\bin;E:\TortoiseSVN\bin;E:\mysql-5.6.24-winx64\bin;E:\apache-maven-3.3.3\bin;C:\Program Files\*** ***;.;;E:\eclipse;;.;;.
java.io.tmpdir : C:\Users\***~1.***\AppData\Local\Temp\
java.compiler : null
java.ext.dirs : C:\Program Files (x86)\Java\jdk1.8.0_121\jre\lib\ext;C:\windows\Sun\Java\lib\ext
os.name : Windows 7
os.arch : x86
os.version : 6.1
file.separator : \
path.separator : ;
line.separator : 

user.name : ***.***
user.home : C:\Users\***.***
user.dir : E:\workspace\TestCode
```

### jar包加载文件路径

a.txt 和 Application.java 在同一个目录下，b.txt 在类路径下

```java
public class Application {
    public static void main(String[] args) throws IOException {
        System.out.println(Application.class.getResource(""));
        System.out.println(Application.class.getResource("a.txt"));

        System.out.println(Application.class.getResource("/"));
        System.out.println(Application.class.getResource("/b.txt"));
        getFileContent(Application.class.getResource("/b.txt").getPath());

        System.out.println(Application.class.getClassLoader().getResource(""));
        System.out.println(Application.class.getClassLoader().getResource("b.txt"));
        getFileContent(Application.class.getClassLoader().getResource("b.txt").getPath());

        //报错空指针异常
        /*InputStream is = Application.class.getResourceAsStream("a.txt");
        byte[] bytes = new byte[100];
        is.read(bytes);
        System.out.println(new String(bytes));*/
        InputStream is2 = Application.class.getClassLoader().getResourceAsStream("b.txt");
        byte[] bytes2 = new byte[5];
        is2.read(bytes2);
        System.out.println(new String(bytes2));

    }

    private static void getFileContent(String path) throws IOException {
        FileInputStream fi=new FileInputStream(path);
        byte[] bytes = new byte[5];
        fi.read(bytes);
        System.out.println(new String(bytes));
    }
}

//本地输出
file:/E:/IdeaProjects/redis-master/redis-test/target/classes/com/huawei/fusioninsight/test/
null
    
file:/E:/IdeaProjects/redis-master/redis-test/target/classes/
file:/E:/IdeaProjects/redis-master/redis-test/target/classes/b.txt
bbbbb

file:/E:/IdeaProjects/redis-master/redis-test/target/classes/
file:/E:/IdeaProjects/redis-master/redis-test/target/classes/b.txt
bbbbb

bbbbb

//打成jar包输出
PS E:\IdeaProjects\redis-master\redis-test\target\jars> java -jar redis-test.jar

jar:file:/E:/IdeaProjects/redis-master/redis-test/target/jars/redis-test.jar!/com/huawei/fusioninsight/test/
null
    
null
jar:file:/E:/IdeaProjects/redis-master/redis-test/target/jars/redis-test.jar!/b.txt
java.io.FileNotFoundException: file:\E:\IdeaProjects\redis-master\redis-test\target\jars\redis-test.jar!\b.txt (文件名、目录名或卷标语法不正确。)

null
jar:file:/E:/IdeaProjects/redis-master/redis-test/target/jars/redis-test.jar!/b.txt
java.io.FileNotFoundException: file:\E:\IdeaProjects\redis-master\redis-test\target\jars\redis-test.jar!\b.txt (文件名、目录名或卷标语法不正确。)

bbbbb
```

- 编译时只会加载在类路径下的文件
- 打成 jar 包后无法获取类路径目录，但是可以获取类路径下具体文件路径
- 读取文件无法识别 redis-test.jar! 目录，只能使用 getResourceAsStream 方法读取文件

[Java代码打成jar后 classgetClassLoadergetResource("")返回为null](https://blog.csdn.net/wqc19920906/article/details/79263269)

[读取Jar包中的资源问题探究](https://blog.csdn.net/withiter/article/details/11924095)



