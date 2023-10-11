https://github.com/wj456789/Note

## JDK常用工具 

### 常用命令

1、jps：查看本机java进程信息

2、jstack：打印线程的栈信息，制作 线程dump文件

3、jmap：打印内存映射信息，制作 堆dump文件

4、jstat：性能监控工具

5、jhat：内存分析工具，用于解析堆dump文件并以适合人阅读的方式展示出来

6、jconsole：简易的JVM可视化工具

7、jvisualvm：功能更强大的JVM可视化工具

8、javap：查看字节码

### JAVA Dump 

JAVA Dump就是虚拟机运行时的快照，将虚拟机运行时的状态和信息保存到文件中，包括：

线程dump：包含所有线程的运行状态，纯文本格式

堆dump：包含所有堆对象的状态，二进制格式



## Jmap和Jstack

### Jmap

主要用于打印指定java进程的共享对象内存映射或堆内存细节。 

jmap PID

打印的信息分别为：共享对象的起始地址、映射大小、共享对象路径的全程。

jmap -histo PID：查看堆中对象数量和大小

打印的信息分别是：序列号、对象的数量、这些对象的内存占用大小、这些对象所属的类的全限定名

如果是内部类，类名的开头会加上*，如果加上live子参数的话，如jmap –histo:live PID，这个命名会触发一次FUll GC，只统计存活对象

查看对象数最多的对象

jmap -histo PID|sort -k 2 -g -r|less

查看占用内存最多的最象

jmap -histo PID|sort -k 3 -g -r|less

jmap -heap pid:查看堆使用情况 





jmap -dump:format=b,file=heapdump PID

将内存使用的详细情况输出到文件

jmap -dump:live,format=b,file= heapdump PID

将存活对象输出到文件



Dump文档可以使用VisualVM工具查看大对象

可以在tomcat启动参数中添加下面的参数在outofmemory时生成dump

JAVA_OPTS=“$JAVA_OPTS ”-XX:+HeapDumpOnOutOfMemoryError





内存溢出是指应用系统中存在无法回收的内存或使用的内存过多，最终使得程序运行要用到的内存大于虚拟机能提供的最大内存。 

引起内存溢出的原因有很多种，常见的有以下几种：

1.内存中加载的数据量过于庞大，如一次从数据库取出过多数据；

2.集合类中有对对象的引用，使用完后未清空，使得JVM不能回收；

3.代码中存在死循环或循环产生过多重复的对象实体；

4.使用的第三方软件中的BUG；

5.启动参数内存值设定的过小；





```
常见日志报错：
堆内存溢出：Exception in thread “main” java.lang.OutOfMemoryError: Java heap space
永久区内存溢出：
Exception in thread “main” java.lang.OutOfMemoryError: PermGen space
本地内存溢出：
Exception in thread “main” java.lang.OutOfMemoryError: request <size> bytes for <reason>. Out of swap space?
Exception in thread "main" java.lang.OutOfMemoryError: <reason>
<stack trace>(Native method)
本地线程资源不足
Exception in thread <thread>/Caused by java.lang.OutOfMemoryError: unable to create new native thread 
```

### jstack

性能压测过程中cpu出现波动、过高、过低等情况，需要截取jstack文件看下cpu在做什么。

jstack是java虚拟机自带的一种堆栈跟踪工具。jstack用于生成java虚拟机当前时刻的线程快照。线程快照是当前java虚拟机内每一条线程正在执行的方法堆栈的集合。

生成线程快照的主要目的是定位线程出现长时间停顿的原因，如线程间死锁、死循环、请求外部资源导致的长时间等待等。







日常程序中常见的耗CPU的操作：

1、频繁GC，访问量高时，有可能造成频繁的GC、甚至FGC。当调用量大时，内存分配过快，就会造成GC线程不停的执行，导致CPU飙高

2、序列化与反序列化，比如程序执行xml解析的时，调用量增大的情况下，导致了CPU被打满

3、加密解密

4、正则表达式校验，频繁正则校验将CPU打满。大概原因是：Java 正则表达式使用的引擎实现是 NFA 自动机，这种引擎在进行字符匹配会发生回溯（backtracking）

5、线程上下文切换、当启动了很多线程，而这些线程都处于不断的阻塞状态（锁等待、IO等待等）和执行状态的变化过程中。当锁竞争激烈时，很容易出现这种情况

6、某些线程在做无阻塞的运算，简单的例子while(true)中不停的做运算，没有任何阻塞。写程序时，如果需要做很久的计算，可以适当将程序sleep下

7、Excel 导出事件







服务器CPU问题分类：

1、cpu飙高，load高，响应很慢

一个请求过程中多次dump

对比多次dump文件的runnable线程，如果执行的方法有比较大变化，说明比较正常。如果在执行同一个方法，就有一些问题了。

2、cpu使用率不高但是响应很慢

进行dump，查看是否有很多thread struck在了i/o、数据库等地方，定位瓶颈原因。 

3、请求无法响应

多次dump，对比是否所有的runnable线程都一直在执行相同的方法，如果是的，恭喜你，锁住了！

Cpu高分析可从以下三方面着手：

a)线程引起，一般查看runnable状态线程

b)内存问题引起，内存满导致频繁GC

c)IO或数据库瓶颈



\1. top命令查看系统使用信息，找出消耗最大的进程，如进程id为24152

\2. 查找该进程内最耗费CPU的线程,可以使用如下命令：

​     $ top -Hp 24152  

​    如最耗费性能的线程pid为24937

\3. 获取线程id 24937的十六进制值

​    $ printf "%x\n" 24937

​    6169

\4. 查看堆栈信息

4.1 使用jstack来输出线程id 24937的堆栈信息，根据线程id的十六进制值grep

​    $ jstack 24152 | grep –A50 6169

4.2导出进程快照

​    $ jstack -l 24152 > ./24152.stack

​    $ cat 24152.stack | grep '6169' -C 10









耗时长的线程：一个线程连续一段时间内都是同一堆栈

消耗cpu高的堆栈：运行比例比较大的堆栈

阻塞线程：线程状态为Waiting on monitor的线程，产生了线程锁，阻塞了其他线程运行

堆栈采集，5秒一次，根据进程内容自行调整

```sh
#!/bin/bash
while(true)
do
{
        aa=`date +%s%N`
        echo $aa
        /home/uap/portal/jdk1.8.0_272/bin/jstack 32199 | grep -A500 90b9 > $aa
        sleep 5
}
done
```



堆栈采集，执行./jstack.sh PID 线程数 间隔时间(单位秒)>> jstack.log 

```sh
#!/bin/bash

# input check
if [ $# -eq 0 ];then
    echo "Usage:jstack.sh jvm_pid thread_count internal_time"
    exit -1
fi

# jvm pid
pid=$1

# find jstack command
jstack_cmd=""
if [[ $JAVA_HOME != "" ]]; then
    jstack_cmd="$JAVA_HOME/bin/jstack"
else
    r=`which jstack 2>/dev/null`
    if [[ $r != "" ]]; then
        jstack_cmd=$r
    else
        echo "can not find jstack"
        exit -2
    fi
fi

# top thread count
thread_count=2
if [ $# -gt 1 ];then
	let "thread_count=$2+1"
fi

# internal time
internal_time=0
if [ $# -gt 2 ];then
	let "internal_time=$3"
fi

# display title function
function disp_title()
{

	echo "PID:$pid, Timestamp: `date` " 

	i=80

	while [ $i -gt 0 ]
	do
		printf "%c" "-"
		let "i=$i-1"
	done

	printf "\n"
}

# get jvm top threads
function top_threads()
{
	tops=`top -H -b -n 1 -p $1 |sed '1,/^$/d' |grep -v $1 |head -$2`

	echo "$tops"

	threads="$(echo "$tops"|sed "1d" |awk '{if ($9>0) {print $1}}')"
	echo "-------threads:"$threads
	echo "#####Usage:threads detail#####"
}

# dump thread stack functon
function dump_thread()
{
	tid_0x=$(printf "%0x" $2)
	echo "---------------tid_0x:"$tid_0x
	$jstack_cmd $1 | grep $tid_0x -A200 | sed -n '1,/^$/p'
}

# dump top thread stack function
function dump_top_thread()
{1
	disp_title

	threads=""
	top_threads $pid $thread_count

	for tid in $threads
	do
		echo "----------tid:"$tid
		dump_thread $pid $tid
	done
}

# call dump top thread stack function
dump_top_thread

# loop
while [ $internal_time -gt 0 ]
do
	sleep $internal_time
	dump_top_thread
done
```





死锁，Deadlock（重点关注）

执行中，Runnable（重点关注）

等待资源，Waiting on condition（重点关注）

等待监控器检查资源，Waiting on monitor

暂停，Suspended

对象等待中，Object.wait()

阻塞，Blocked（重点关注）

停止，Parked

Deadlock：死锁线程，一般指多个线程调用间，进入相互资源占用，导致一直等待无法释放的情况。

Runnable：一般指该线程正在执行状态中，该线程占用了资源，正在处理某个请求，有可能正在传递SQL到数据库执行，有可能在对某个文件操作，有可能进行数据类型等转换。

Waiting on condition：等待资源，如果堆栈信息明确是应用代码，则证明该线程正在等待资源，一般是大量读取某资源，且该资源采用了资源锁的情况下，线程进入等待状态，等待资源的读取。又或者，正在等待其他线程的执行等。

Blocked：线程阻塞，是指当前线程执行过程中，所需要的资源长时间等待却一直未能获取到，被容器的线程管理器标识为阻塞状态，可以理解为等待资源超时的线程。







## 可视化监控工具 

在 JDK 安装目录的 bin 文件夹下，除了提供有命令行监控工具外，还提供了几种可视化的监控工具，以方便用户直观地了解虚拟机的运行状态。这里说明两种可视化监控工具JConsole、JVisualVM 

### JConsole 

简介

JConsole（Java Monitoring and Management Console）是一款基于 JMX（Java Manage-ment Extensions）的可视化监视工具。它的主要功能是通过 JMX 的 MBean（Managed Bean）对系统信息进行收集和动态调整系统参数。JMX（Java Management Extensions）是一个为应用程序、设备、系统等植入管理功能的框架，通常用于监控系统的运行状态或管理系统的部分功能。



选中需要监控的进程后，点击连接，即可进入监控界面。监控界面包含了 概览、内存、线程、类、VM 概要、MBean 六个选项卡。其中概览界面显示的是 内存、线程、类 等三个选项卡界面的概览信息，如图所示： 





内存界面主要用于显示堆和非堆上各个区域的使用量： 



线程界面内主要显示各个线程的堆栈信息，最下角有一个 检测死锁 按钮，点击后如果检测到死锁存在，则在下部的线程选项卡旁边会出现死锁选项卡： 



类 选项卡主要用于显示当前已加载和已卸载的类的数量。而 VM 概要 选项卡则主要用于显示虚拟机的相关参数 



### VisualVM

简介

VisualVM（All-in-One Java Troubleshooting Tool）是 Oracle 提供的功能最强大的运行监视和故障处理程序之一， 它除了支持常规的运行监视、故障处理等功能外，还能用于性能分析（Profiling）。同时因为 VisualVM 是基于 NetBeans 平台的开发工具，所以它还支持通过插件来进行功能的拓展。VisualVM 的主要功能如下：

1）显示虚拟机进程及其配置信息、环境信息（与 jps、jinfo 功能类似）；

2）监视应用程序的处理器、垃圾收集、堆、方法区以及线程的信息（与 jstat、jstack 功能类似）；

3）dump以及分析堆转储快照（与 jmap、jhat 功能类似）；

4）方法级的程序运行性能分析，找出被调用最多、运行时间最长的方法；

5）离线程序快照：可以收集程序的运行时配置、线程 dump、内存 dump 等信息来建立快照。



点击需要监控的进程后，右侧即会显示相关的监控信息 





在线程界面可以查看所有线程的状态，如果出现死锁，该界面还会进行提示： 





在 Profiler 界面，可以进行 CPU 和 内存的性能分析。要开始性能分析，需要先选择 CPU 或 内存 按钮中的一个，VisualVM 将会开始记录应用程序执行过的所有方法：如果是进行的是 CPU 执行时间分析，将会统计每个方法的执行次数、执行耗时；如果是内存分析，则会统计每个方法关联的对象数以及这些对象所占的空间。想要结束性能分析，点击停止按钮即可： 













监控远程主机上的进程，需要进行 JMX 的相关配置，根据连接时是否需要用户名和密码，可以分为以下两种配置方式：

不使用安全凭证

-Dcom.sun.management.jmxremote.authenticate=false 

-Dcom.sun.management.jmxremote.ssl=false 

-Djava.rmi.server.hostname=IP地址

-Dcom.sun.management.jmxremote.port=PORT端口 

使用安全凭证

-Dcom.sun.management.jmxremote.ssl=false 

-Dcom.sun.management.jmxremote.authenticate=true 

-Djava.rmi.server.hostname=IP地址

-Dcom.sun.management.jmxremote.port=PORT端口 

-Dcom.sun.management.jmxremote.access.file=/usr/local/jmxremote.access 

-Dcom.sun.management.jmxremote.password.file=/usr/local/jmxremote.password 

其中 jmxremote.access  的内容为admin readwrite ，其中 admin 为用户名，readwrite 表示可读可写，也可以设置为 readonly（只读）。jmxremote.password 的内容为admin 123456，其中 admin 为用户名，123456 为密码

两个文件创建好后，还需要赋予其执行和用户权限









在tomcat/bin/catalina.sh文件中添加declare -x CATALINA_OPTS=“-Dcom.sun.management.jmxremote.port=PORT端口 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=IP地址" 



## RUNTIME　AREA：

运行数据区是整个JVM的重点。我们所有写的程序都被加载到这里，之后才开始运行。















```
top - 09:53:13 up 34 days, 16:37, 12 users,  load average: 0.24, 0.30, 0.38
显示开机运行时间，当前时间，在线用户数，平均负载
09:53:13  当前时间
up 34 days, 16:37  系统开始运行时间，格式为时:分
12 users  当前登录用户数
load average: 0.24, 0.30, 0.38  系统负载，即任务队列的平均长度。
三个数值分别为1分钟、5分钟、15分钟前到现在的平均值。
Tasks: 193 total,   1 running, 192 sleeping,   0 stopped,   0 zombie
		任务数量和状态
193 total 进程的总数
1 running 正在运行的进程数
192 sleeping 睡眠的进程数
0 stopped 停止的进程数
0 zombie 僵尸进程数
Cpu(s):  0.7%us, 0.2%sy, 0.0%ni, 98.8%id, 0.1%wa, 0.0%hi, 0.2%si, 0.0%st
		CPU的当前状态
0.7%us  用户空间占用CPU百分比
0.2%sy	内核空间占用CPU百分比
0.0%ni  	用户进程空间内改变过优先级的进程占用CPU百分比
98.8%id	空闲CPU百分比
0.1%wa  等待输入输出的CPU时间百分比
0.0%hi
0.2%si
0.0%st
Mem:  24672844k total, 21493856k used,  3178988k free,   744316k buffers
	内存占用情况
	24672844k total  物理内存总量
21493856k used  使用的物理内存总量
3178988k free  空闲内存总量
744316k buffers  用作内核缓存的内存量
Swap: 16779852k total,   340780k used, 16439072k free, 12905060k cached
	交换分区信息
16779852k total  交换分区总量
340780k used  使用的交换区总量
16439072k free  空闲交换区总量
12905060k cached   缓冲的交换区总量。
		            
  PID  USER  PR  NI  VIRT  RES  SHR S  %CPU  %MEM  TIME+  COMMAND
12726  csp   16   0  5014m  3.0g  169m S  5      12.8    243:59.39  java
进程信息
序号列名含义
PID    进程id
PPID    父进程id
RUSER    Realusername
UID    进程所有者的用户id
USER    进程所有者的用户名
GROUP    进程所有者的组名
TTY    启动进程的终端名。不是从终端启动的进程则显示为?
PR    优先级
NI nice 值。负值表示高优先级，正值表示低优先级
P    最后使用的CPU，仅在多CPU环境下有意义
%CPU    上次更新到现在的CPU时间占用百分比
TIME    进程使用的CPU时间总计，单位秒
TIME+    进程使用的CPU时间总计，单位1/100秒
%MEM    进程使用的物理内存百分比
VIRT    进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
SWAP    进程使用的虚拟内存中，被换出的大小，单位kb。
RES    进程使用的、未被换出的物理内存大小，单位kb RES=CODE+DATA
CODE    可执行代码占用的物理内存大小，单位kb
DATA    可执行代码以外的部分(数据段+栈)占用的物理内存大小，单位kb
SHR    共享内存大小，单位kb
nFLT    页面错误次数
nDRT    最后一次写入到现在，被修改过的页面数。
S    进程状态。
D=    不可中断的睡眠状态
R=    运行
S=    睡眠
T=    跟踪/停止
Z=    僵尸进程
COMMAND    命令名/命令行
WCHAN    若该进程在睡眠，则显示睡眠中的系统函数名
Flags    任务标志，参考sched.h

```

```
以下为AI生成内容，仅供参考，不允许未经评估直接将生成内容植入工作输出件中。

ParNewGC和ParallelGC是Java虚拟机（JVM）中的两种垃圾收集器。它们在垃圾收集的方式和性能方面有一些区别。

1. 垃圾收集方式：
   - ParNewGC：ParNewGC是年轻代的并行垃圾收集器，主要用于新生代的垃圾回收。它使用多个线程并行地进行垃圾收集，可以充分利用多核处理器的优势。
   - ParallelGC：ParallelGC是整堆并行垃圾收集器，用于整个堆的垃圾回收。它也使用多个线程并行地进行垃圾收集，但相比ParNewGC，它更适用于多核处理器和大内存的情况。

2. 垃圾收集性能：
   - ParNewGC：ParNewGC的主要目标是减少垃圾收集的停顿时间，因此它通常与CMS（Concurrent Mark Sweep）收集器配合使用，以实现更短的垃圾收集停顿时间。
   - ParallelGC：ParallelGC的主要目标是提高垃圾收集的吞吐量，即在给定时间内完成尽可能多的工作。它通常与Serial Old收集器配合使用，以实现更高的吞吐量。

3. 适用场景：
   - ParNewGC：ParNewGC适用于多核处理器和较小的堆内存的情况。它在减少垃圾收集停顿时间方面表现较好，适合对响应时间要求较高的应用。
   - ParallelGC：ParallelGC适用于多核处理器和较大的堆内存的情况。它在提高垃圾收集吞吐量方面表现较好，适合对吞吐量要求较高的应用。

总的来说，ParNewGC和ParallelGC都是并行垃圾收集器，但在垃圾收集方式和性能方面有一些区别。选择哪个收集器取决于应用的需求，如响应时间要求、吞吐量要求以及系统的硬件配置等。
不再接收
```

```
以下为AI生成内容，仅供参考，不允许未经评估直接将生成内容植入工作输出件中。

Serial、PraNew、Parallel Scavenge是Java虚拟机中的三种垃圾收集器，它们的区别如下：

1. Serial收集器：Serial收集器是一种单线程的垃圾收集器，它只使用一个线程进行垃圾收集，因此它的收集效率较低。Serial收集器适用于小型应用程序和客户端应用程序，因为它的暂停时间较短。

2. ParNew收集器：ParNew收集器是Serial收集器的多线程版本，它可以使用多个线程进行垃圾收集，因此它的收集效率比Serial收集器高。ParNew收集器适用于多核服务器应用程序，因为它可以利用多个CPU核心进行垃圾收集。

3. Parallel Scavenge收集器：Parallel Scavenge收集器是一种多线程的垃圾收集器，它可以使用多个线程进行垃圾收集，并且可以动态调整垃圾收集线程的数量。Parallel Scavenge收集器适用于大型应用程序和服务器应用程序，因为它可以在多个CPU核心上并行执行垃圾收集，从而提高垃圾收集效率。

总之，Serial收集器适用于小型应用程序和客户端应用程序，ParNew收集器适用于多核服务器应用程序，Parallel Scavenge收集器适用于大型应用程序和服务器应用程序。
不再接收
```







```
以下为AI生成内容，仅供参考，不允许未经评估直接将生成内容植入工作输出件中。

Serial Old、Parallel Old和CMS是Java虚拟机（JVM）中用于垃圾回收的不同算法。它们的主要区别在于垃圾回收的方式和效率。

1. Serial Old（串行老年代回收）：Serial Old是一种单线程的垃圾回收算法，它会暂停应用程序的所有线程来进行垃圾回收。它适用于小型应用程序和单核处理器，因为它的效率较低。在进行垃圾回收时，它会停止应用程序的执行，直到垃圾回收完成。

2. Parallel Old（并行老年代回收）：Parallel Old是一种多线程的垃圾回收算法，它会使用多个线程来进行垃圾回收，从而提高回收效率。它适用于大型应用程序和多核处理器，因为它可以充分利用多核处理器的并行能力。在进行垃圾回收时，它会暂停应用程序的执行，但相比于Serial Old，它的回收速度更快。

3. CMS（Concurrent Mark Sweep，并发标记清除）：CMS是一种并发的垃圾回收算法，它可以在应用程序运行的同时进行垃圾回收，减少了应用程序的停顿时间。它适用于对停顿时间要求较高的应用程序。CMS算法通过并发标记和并发清除两个阶段来进行垃圾回收，其中标记阶段会暂停应用程序的执行，但清除阶段是与应用程序并发执行的。

总结来说，Serial Old适用于小型应用程序和单核处理器，Parallel Old适用于大型应用程序和多核处理器，而CMS适用于对停顿时间要求较高的应用程序。选择哪种算法取决于应用程序的特点和性能需求。
不再接收
```

```
以下为AI生成内容，仅供参考，不允许未经评估直接将生成内容植入工作输出件中。

Parallel Old和CMS都是Java虚拟机中的垃圾回收器，但它们有一些区别：

1. 并行度：Parallel Old是一个并行垃圾回收器，它使用多个线程来同时回收垃圾。而CMS是一个并发垃圾回收器，它在应用程序运行的同时回收垃圾。

2. 暂停时间：Parallel Old在进行垃圾回收时需要暂停应用程序，暂停时间较长。而CMS在进行垃圾回收时只需要短暂的暂停，对应用程序的影响较小。

3. 内存占用：Parallel Old在进行垃圾回收时需要占用较多的内存，因为它需要将整个堆分成多个区域进行回收。而CMS只需要占用一小部分内存，因为它只回收部分堆区域。

4. 适用场景：Parallel Old适用于需要大量内存的应用程序，因为它可以高效地回收大量垃圾。而CMS适用于需要快速响应的应用程序，因为它的暂停时间较短，对应用程序的影响较小。
不再接收
```
	编译器优化相关：
-server 启用能够执行优化的编译器, 显著提高服务器的性能，但使用能够执行优化的编译器时，服务器的预备时间将会较长。生产环境的服务器强烈推荐设置此参数。
	垃圾收集（GC）相关：
-XX:+UseParNewGC 可用来设置年轻代为并发收集【多CPU】，如果你的服务器有多个CPU，你可以开启此参数；开启此参数，多个CPU 可并发进行垃圾回收，可提高垃圾回收的速度。此参数和+UseParallelGC，-XX:ParallelGCThreads搭配使用。
+UseParallelGC 选择垃圾收集器为并行收集器。此配置仅对年轻代有效。即上述配置下，年轻代使用并发收集，而年老代仍旧使用串行收集。可提高系统的吞吐量。
-XX:ParallelGCThreads 年轻代并行垃圾收集的前提下（对并发也有效果）的线程数，增加并行度，即：同时多少个线程一起进行垃圾回收。此值最好配置与处理器数目相等。永久存储区相关参数：参数名参数说明
-Xnoclassgc 每次永久存储区满了后一般GC 算法在做扩展分配内存前都会触发一次FULL GC，除非设置了-Xnoclassgc（不进行GC）.

