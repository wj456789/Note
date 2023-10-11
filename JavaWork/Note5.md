https://github.com/wj456789/Note

## JDK常用工具 

### jstack

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

















