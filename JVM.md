# JVM

## 内存结构

JVM按照其存储数据的内容将所需内存分配为堆区与非堆区两个部分：所谓堆区即为通过new的方式创建的对象(类实例)所占用的内存空间;非堆区即为代码、常量、外部访问(如文件访问流所占资源)等。然而虽然java的垃圾回收机制主要是对堆区的内存进行回收的。

![截图](img_JVM/%E6%88%AA%E5%9B%BE.png)



一个Java8进程最大占用的物理内存为：

Max Memory = eden + survivor + old + String Constant Pool + Code cache + compressed class space + Metaspace + Thread stack(*thread num) + Direct + Mapped + JVM + Native Memory，可以使用JDK自带工具查看。

### 堆区

Heap Space堆区分为新生代和老年代，新生代分为Eden和Survivor区。

**eden（伊甸区）**

大多数情况下，new出来的对象首先分配在eden区。当eden区没有足够空间进行分配时，虚拟机将发起一次MinorGC。GC回收一次后对象若还活着，则移到s0。

**s0、s1区(Survivor区)**

同一时刻，所有对象只会存在s0或s1区域。如果对象在eden出生并经过第一次Minor GC后仍然存活，并且能被s0容纳的话，将被移动到s0空间，并且对象年龄设为1。根据GC回收的复制算法，再进行一次回收后，s0区域会出现分片的内存空间，不利于后续对象的存储，浪费内存空间，此时会将s0区域的对象按内存空间顺序整理后拷贝到s1空间，然后s0空间重置为空，此时的s1就变回原来的from s0区域，而s0变回原来的to s1区域，对象在s0和s1区中每经过一次Minor GC，年龄就增加1岁，当它的年龄增加到一定程度（默认15岁），就会被晋升到老年代。

**老年代(tenured区)**

当老年代区域的内存满了，则会触发full GC，full GC会使程序暂停，所以要避免老年代区域的内存达到所配置的大小。

### 非堆区

非堆区包括永久代、栈内存等。

**永久代(permanent generation区)**

1. java7之前

   **类数据、方法区和常量池**位于永久代(PermGen)，永久代和堆相互隔离，永久代的大小在启动JVM时可以设置一个固定值，不可变。

   Class在被 Load的时候被放入永久代，如果项目会LOAD很多CLASS的话,就很可能出现PermGen space错误，这种错误常见在web服务器对JSP进行pre compile的时候。JVM运行时会用到多少持久代的空间取决于应用程序用到了多少类，如果JVM发现有的类已经不再需要了，它会去回收（卸载）这些类，将它们的空间释放出来给其它类使用。

   永久代的对象在full GC时会进行垃圾收集。

2. java7中，static变量从永久代移到堆中；

3. java8中，取消永久代，把类的元数据放到本地化的堆内存(native heap)中，这一块区域就叫Metaspace，中文名叫元空间。元空间可以垃圾回收。元空间与永久代之间最大的区别在于：元空间并不在虚拟机中，而是使用本地内存。因此，默认情况下，元空间的大小仅受本地内存限制。

参考：[Metaspace 之一：Metaspace整体介绍（永久代被替换原因、元空间特点、元空间内存查看分析方法）](https://www.cnblogs.com/duanxz/p/3520829.html)



**类指针压缩空间（Compressed Class Pointer Space）**

64位平台上默认打开。压缩指针，指的是在 64 位的机器上，使用 32 位的指针来访问数据（堆中的对象或 Metaspace 中的元数据）的一种方式。如果开启了指针压缩，则CompressedClassSpace分配在MaxMetaspaceSize里头，即MaxMetaspaceSize=Compressed Class Space Size + Metaspace area (excluding the Compressed Class Space) Size

参考：[Java 内存分区之什么是 CCS区 Compressed Class Space 类压缩空间](https://www.pianshen.com/article/90131581146/)

**GC**

参考：[Java程序配置内存大小，gc参数配置-以tomcat为例](https://blog.csdn.net/loophome/article/details/87911711)

#### 虚拟机栈(Stack)

- 虚拟机栈线程私有，每个线程都有自己独立的虚拟机栈，创建一个线程的同时会创建一个栈
- 虚拟机栈是用于描述java方法执行的内存模型。 每个java方法在执行时，会创建一个“栈帧”。
- 通常说的“栈内存”，确切的说，指的是虚拟机栈的栈帧中的局部变量表，因为这里存放了一个方法的所有局部变量。 

##### 栈帧

栈帧的大小在程序代码编译时确定。

栈帧(Stack Frame)是用于支持虚拟机进行方法调用和方法执行的数据结构。栈帧存储了方法的局部变量表、操作数栈、动态链接和方法返回地址等信息。每一个方法从调用至执行完成的过程，都对应着一个栈帧在虚拟机栈里从入栈到出栈的过程。

一个线程中方法的调用链可能会很长，很多方法都同时处于执行状态。对于JVM执行引擎来说，在在活动线程中，只有位于JVM虚拟机栈栈顶的元素才是有效的，即称为当前栈帧，与这个栈帧相关连的方法称为当前方法，定义这个方法的类叫做当前类。

#### 直接内存

NIO的Buffer提供了一个可以不经过JVM内存直接访问**系统物理内存**的类——DirectBuffer。 

DirectBuffer类继承自ByteBuffer，但和普通的ByteBuffer不同，普通的ByteBuffer仍在JVM堆上分配内存，其最大内存受到最大堆内存的限制；而DirectBuffer直接分配在物理内存中，并不占用堆空间，其可申请的最大内存受操作系统限制。

直接内存的单次分配和读写操作比普通Buffer快，但它的创建、销毁比普通Buffer慢。因此直接内存使用于需要大内存空间且频繁访问的场合，不适用于频繁申请释放内存的场合。

比如 Java8 使用元空间：元空间主要存储加载的类信息，这些数据只会在程序启动时直接分配足够的直接内存，可以减少程序的启动时间，运行期一般不会频繁加载新的类，故运行期不需要频繁分配内存。堆内存主要存放的是运行时对象，需要频繁的创建与销毁。 

直接内存可以使用 -XX:MaxDirectMemorySize 配置 

## 内存分配

```sh
#nohup java -jar -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1024m -Xmx1024m -Xmn256m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC /jar包路径 &
```

对java虚拟机运行时的内存分配的不得当会导致内存溢出，比如说OutOfMemoryError等，按照需求配置java虚拟机运行时的所需的内存——通过参数配置的形式实现参数分配自定义化。

配置JVM内存的参数：      

### 堆区

- **-Xms** ：表示java虚拟机堆区内存初始内存分配的大小,通常为操作系统可用内存的1/64大小即可,但仍需按照实际情况进行分配。
- **-Xmx**：表示java虚拟机堆区内存可被分配的最大上限,通常为操作系统可用内存的1/4大小。但是开发过程中,通常会将 -Xms 与 -Xmx	两个参数的配置相同的值,其目的是为了能够在java垃圾回收机制清理完堆区后不需要重新分隔计算堆区的大小而浪费资源。
- **-XX:newSize**：表示新生代初始内存的大小,应该小于 -Xms的值;
- **-XX:MaxnewSize**：表示新生代可被分配的内存的最大上限;当然这个值应该小于 -Xmx的值;
- **-Xmn**：至于这个参数则是对 -XX:newSize、-XX:MaxnewSize两个参数的同时配置,也就是说如果通过-Xmn来配置新生代的内存大小,那么-XX:newSize = -XX:MaxnewSize = -Xmn,虽然会很方便,但需要注意的是这个参数是在JDK1.4版本以后才使用的。Sun官方推荐配置为整个堆的3/8
- **-XX:NewRatio=4**：设置新生代和老年代的内存比例为 1:4；
- **-XX:SurvivorRatio**：设置新生代中Eden和Survivor的比例(默认值为8，即Eden:FromSpace:ToSpace，默认比例8:1:1，假如值为4表示：Eden:S0:S1 = 4:3:3)
- **-XX:MaxTenuringThreshold**：设置对象晋升老年代的年龄阈值，默认15

### 非堆区

- **-XX:PermSize**：表示非堆区初始内存分配大小,其缩写为permanent size(持久化内存)，默认是物理内存的1/64;

- **-XX:MaxPermSize**：表示对永久代分配的内存的最大上限,默认是物理内存的1/4。

- **-XX:MetaspaceSize**：元空间默认大小

- **-XX:MaxMetaspaceSize**：元空间最大大小

- **-Xss**：每个线程栈内存最大深度大小，JDK5.0以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。更具应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。

  参考：[JVM常用基础参数-栈内存Xss讲解](https://blog.csdn.net/longgeqiaojie304/article/details/93972700)

- **-XX:+UseConcMarkSweepGC**：指定使用的垃圾收集器，这里使用CMS收集器
- **-XX:+PrintGCDetails**：打印详细的GC日志
- **-XX:ParallelGCThreads**：Gc线程数



- **-XX:+UseCompressedOops** 允许对象指针压缩。 

- **-XX:+UseCompressedClassPointers** 允许类指针压缩。 

  它们默认都是开启的，可以手动关闭它们。 如果不允许类指针压缩，那么将没有 compressed class space 这个空间，并且-XX:CompressedClassSpaceSize 这个参数无效。 -XX:-UseCompressedClassPointers 需要搭配 -XX:+UseCompressedOops，但是反过来不是，也就是说我们可以只压缩对象指针，不压缩类指针。在对象指针压缩基础上进行类指针压缩。



## 垃圾回收算法

- 标记-清除算法：标记无用对象，然后进行清除回收。缺点：效率不高，无法清除垃圾碎片。
- 复制算法：按照容量划分二个大小相等的内存区域，当一块用完的时候将活着的对象复制到另一块上，然后再把已使用的内存空间一次清理掉。缺点：内存使用率不高，只有原来的一半。
- 标记-整理算法：标记无用对象，让所有存活的对象都向一端移动，然后直接清除掉端边界以外的内存。
- 分代算法：根据对象存活周期的不同将内存划分为几块，一般是新生代和老年代，**新生代基本采用复制算法，老年代采用标记整理算法**。

## 垃圾回收器

垃圾收集算法是内存回收的方法论，那么垃圾收集器就是内存回收的具体实现。有7种作用于不同分代的收集器，其中用于回收新生代的收集器包括Serial、PraNew、Parallel Scavenge，回收老年代的收集器包括Serial Old、Parallel Old、CMS，还有用于回收整个Java堆的G1收集器。




## 内存监控

### JDK自带的工具

这些工具都在JAVA_HOME/bin目录下，执行命令的jdk版本和所监控的jvm的jdk版本需要一致，否则会报错。

jps：用来显示本地的java进程，以及进程号，进程启动的路径等。

```sh
# jps
9033 Bootstrap(启动的 Tomcat)
4284 Jps
12318 jar
```

#### jmap

观察运行中的JVM 物理内存的占用情况，包括Heap size , Perm size

```sh
# jmap -heap 9033
Attaching to process ID 9033, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.112-b15

using thread-local object allocation.
Parallel GC with 2 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 134217728 (128.0MB)
   NewSize                  = 44564480 (42.5MB)
   MaxNewSize               = 44564480 (42.5MB)
   OldSize                  = 89653248 (85.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 134217728 (128.0MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 134217728 (128.0MB)
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 40370176 (38.5MB)
   used     = 1921656 (1.8326339721679688MB)
   free     = 38448520 (36.66736602783203MB)
   4.760088239397321% used
From Space:
   capacity = 2097152 (2.0MB)
   used     = 360448 (0.34375MB)
   free     = 1736704 (1.65625MB)
   17.1875% used
To Space:
   capacity = 2097152 (2.0MB)
   used     = 0 (0.0MB)
   free     = 2097152 (2.0MB)
   0.0% used
PS Old Generation
   capacity = 89653248 (85.5MB)
   used     = 49846600 (47.53742218017578MB)
   free     = 39806648 (37.96257781982422MB)
   55.599324187340095% used

24752 interned Strings occupying 2413552 bytes.
```

#### jcmd

打印java进程的基本类、线程、VM信息

```sh
/*		
    需要在启动Java程序时开启NMT(Native Memory Tracker ，是一个本地内存跟踪工具)
    off 默认配置
    summary 只收集汇总信息
    detail 收集每次调用的信息
    注意，根据Java官方文档，开启NMT会有5%－10%的性能损耗；
*/
-XX:NativeMemoryTracking=[off | summary | detail]
 
//如果想JVM退出时打印退出时的内存使用情况，可以通过如下配置项:
-XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics
 
#java -Xmx8g -Xms8g - -XX:+UseG1GC -XX:NativeMemoryTracking=detail -jar /home/pgcp/pgcp-0.0.1-SNAPSHOT.jar
```

```sh
# jcmd 9033  VM.native_memory
9033:

Native Memory Tracking:

Total: reserved=1284481KB, committed=236913KB
			#堆内存
-                 Java Heap (reserved=131072KB, committed=131072KB)
                        (mmap: reserved=131072KB, committed=131072KB)
    		#类加载信息
-                     Class (reserved=1100460KB, committed=58156KB)
                        (classes #9943)
                        (malloc=4780KB #13763)
                        (mmap: reserved=1095680KB, committed=53376KB)
    		#线程栈
-                    Thread (reserved=10726KB, committed=10726KB)
                        (thread #24)
                        (stack: reserved=10560KB, committed=10560KB)
                        (malloc=75KB #119)
                        (arena=91KB #46)
    		#代码缓存
-                      Code (reserved=10639KB, committed=5375KB)
                        (malloc=239KB #851)
                        (mmap: reserved=10400KB, committed=5136KB)
    		#垃圾回收
-                        GC (reserved=8265KB, committed=8265KB)
                        (malloc=3469KB #243)
                        (mmap: reserved=4796KB, committed=4796KB)
    		#编译器
-                  Compiler (reserved=152KB, committed=152KB)
                        (malloc=22KB #159)
                        (arena=130KB #2)
    		#内部
-                  Internal (reserved=7595KB, committed=7595KB)
                        (malloc=7563KB #13970)
                        (mmap: reserved=32KB, committed=32KB)
    		#符号
-                    Symbol (reserved=13018KB, committed=13018KB)
                        (malloc=11124KB #111613)
                        (arena=1894KB #1)
    		#nmt
-    Native Memory Tracking (reserved=2367KB, committed=2367KB)
                        (malloc=134KB #2113)
                        (tracking overhead=2234KB)

-               Arena Chunk (reserved=186KB, committed=186KB)
                        (malloc=186KB)

reserved
reserved memory是指JVM通过mmaped PROT_NONE申请的虚拟地址空间，在页表中已经存在了记录(entries)，保证了其他进程不会被占用。

committed
committed memory是操作系统实际分配的内存（malloc/mmap）,mmaped PROT_READ | PROT_WRITE，相当于程序实际申请的可用内存。
committed申请的内存并不是说直接占用了物理内存，由于操作系统的内存管理是惰性的，对于已申请的内存虽然会分配地址空间，
但并不会直接占用物理内存，真正使用的时候才会映射到实际的物理内存。所以committed > res也是很可能的。
 
used
表示当前使用的内存量(以字节为单位)
```

jhat 分析jmap等方法生成的dump堆文件，解析Java堆转储文件,并启动一个 web server，可以直接访问。

jinfo 查看jvm系统参数，可以动态设置参数

jstat 可以查看gc和类加载情况

jstack 查看线程堆栈情况

jconsole 可视化工具，可以连接远程linux服务器对内存线程等监视管理。

jvisualVM 傻瓜式工具，功能更强大，可以在线dump内存堆栈,也可以提供后处理工具。

Heap Analyzer工具

Heap Jmeter工具

在故障定位(尤其是out of memory)和性能分析的时候，会用到dump文件来帮助我们排除代码问题，常用的有heap dump和thread dump，heap dump记录内存信息的，thread dump是记录CPU信息的，可以使用jmap和jstack命令获取，使用jhat命令分析。

参考：

[java命令--jhat命令使用](https://www.cnblogs.com/baihuitestsoftware/articles/6406271.html)

[JConsole连接远程linux服务器配置](https://www.cnblogs.com/zluckiy/p/10309495.html)

## 引用类型

- 强引用：发生 gc 的时候不会被回收。
- 软引用：有用但不是必须的对象，在发生内存溢出之前会被回收。
- 弱引用：有用但不是必须的对象，在下一次GC时会被回收。
- 虚引用（幽灵引用/幻影引用）：无法通过虚引用获得对象，用 PhantomReference 实现虚引用，虚引用的用途是在 gc 时返回一个通知。

## springboot内存优化

application.properties

```properties
#没有连接超时时间的配置
server.port=8081			#端口号
server.tomcat.max-threads=200	#最大线程数
server.tomcat.min-spare-threads=10	#最大空闲连接数
server.tomcat.max-connections=500	#最大连接数
server.tomcat.uri-encoding=UTF-8	#编码方式
server.tomcat.max-http-post-size=0		#post提交数据最大大小，设置为0不限制
server.tomcat.max-http-header-size=0 	#请求响应头最大大小
```

或者配置类

```java
@Component
public class TomcatConfig extends TomcatEmbeddedServletContainerFactory{
    public EmbeddedServletContainer getEmbeddedServletContainer(ServletContextInitializer... initializers){
        //设置端口
        this.setPort(8081);
        return super.getEmbeddedServletContainer(initializers);
    }

    protected void customizeConnector(Connector connector){
        super.customizeConnector(connector);
        Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();
        //设置最大连接数
        protocol.setMaxConnections(200);
        //设置最大线程数
        protocol.setMaxThreads(300);
        //连接超时时间
        protocol.setConnectionTimeout(10000);
    }
}
```



## 开发工具

### IDEA

设置Java程序运行内存edit Configuation-->VM options

### Eclipse

eclipse.ini文件，参数中-vmargs的意思是设置JVM参数，如-vmargs -Xms1024M -Xmx1024M -XX:PermSize=1024M -XX:MaxPermSize=1024M



## 类加载

### 类加载过程

**加载 --> 链接 --> 初始化**

**链接：验证 --> 准备 --> 解析**

#### 加载

由类加载器（ClassLoader）执行的。将字节码从不同数据源读到 jvm 中，数据源包括 zip 压缩包，网络，运行时计算生成，其他文件生成，数据库等。

#### 链接

验证：验证字节码信息是否符合 jvm 规范；

准备：分配内存，并为静态变量赋初始值；

解析：将常量池中的符号引用转换为直接引用。也可以在初始化之后再开始，来支持 java 的运行时绑定。

#### 初始化

执行静态初始化块 (static{}) 和类变量赋值，先初始化父类，后初始化子类；

不要在 static 块中抛出异常，否则会导致类初始化失败，抛 ExceptionInInitializerError 异常，进而导致其他异常。

#### 懒加载

所有的类都是在对其第一次使用时，动态加载到 JVM 中的。当程序创建第一个对类的静态成员的引用时，就会加载这个类。使用 new 创建类对象的时候也会被当作对类的静态成员的引用。因此 java 程序在它开始运行之前并非被完全加载，其各个类都是在必需时才加载的。

### classloader的层次结构

1. 启动类加载器 (BootstrapClassLoader)

   查找 jre 核心库，加载路径下的 Class 文件

2. 扩展类加载器 (ExtClassLoader)

   查找 jre/lib/ext 扩展包

3. 应用程序类加载器 (AppClassLoader)

   查找环境变量 CLASSPATH 目录

4. 自定义类加载器 (UserDefineClassLoader)

   查找用户定义的目录



双亲委派：先由父类加载器加载，加载不到后再由子类加载器加载；

如果Class文件不在父类的加载路径中，则由子类加载，如果仍然找不到，抛ClassNotFound异常。

先加载 JDK 中的类，再加载用户的类。

### 类的初始化过程

#### 初始化时机（类加载时机）

- JVM 启动时，先初始化用户指定的主类
- 初始化一个类的子类（会首先初始化子类的父类）
- 访问类的静态变量或静态方法
- 创建类实例
- 反射调用类

访问final修饰的静态变量时，不会触发类加载，因为在编译期已经将此常量放在常量池了。

特点：JVM 会加锁来保证类初始化只进行一次，可以用来实现单例模式



[静态方法什么时候执行](https://blog.csdn.net/weixin_39983051/article/details/111361114)

### 对象初始化顺序

普通类：（静态变量和静态代码块只和出现顺序有关，普通变量和普通代码块也之和出现顺序有关）

1. 静态变量
2. 静态代码块
3. 普通变量
4. 普通代码块
5. 构造函数

继承的子类：（静态——父类——子类）

1. 父类静态变量
2. 父类静态代码块
3. 子类静态变量
4. 子类静态代码块
5. 父类普通变量
6. 父类普通代码块
7. 父类构造函数
8. 子类普通变量
9. 子类普通代码块
10. 子类构造函数

类初始化顺序：父类静态，子类静态，父类代码块、父类构造，子类代码块，子类构造。

在 JVM 中表示两个 Class 对象是否为同一个类存在两个必要条件：

- 类的完整类名必须一致
- 加载这个类的ClassLoader（指ClassLoader实例对象）必须相同

换句话说，在 JVM 中，即时这两个类对象（class对象）来源于同一个 Class 文件，被同一个虚拟机所加载，但只要加载他们的 ClassLoader 实例对象不同，那么这两个类对象也是不相等的。



**题目**

![img](img_JVM/c92c151000d66d7e9e5657d4dc8d4222.png)



```java
结果：
count1=1
count2=0
```

分析：

1. SingleTon.getInstance()，调用静态方法，触发SingleTon类加载。
2. SingleTon类加载初始化，按顺序初始化静态变量。
3. 先执行private static SingleTon singleTon = new SingleTon(); ，调用构造器后，count1，count2均为1；
4. 按顺序执行 public static int count1; 没有赋值，所以count1依旧为1；
5. 按顺序执行 public static int count2 = 0;所以count2变为0.
   