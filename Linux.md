# Linux

## 关机重启

#shutdown -c now：取消已经执行的 shutdown 命令；
#shutdown -h now：现在关机；
#shutdown -r now：现在重启；

## vi编辑器

命令模式：vi启动后默认进入的是命令模式，从这个模式使用命令可以切换到另外两种模式，同时无论在任何模式下只要按一下[Esc]键都可以返回命令模式。在命令模式中输入字幕“i”就可以进入vi的输入模式编辑文件。

输入模式：在这个模式中我们可以编辑、修改、输入等编辑工作，在编辑器最后一行显示一个“--INSERT--”标志着vi进入了输入模式。当我们完成修改输入等操作的时候我们需要保存文件，这时我们需要先返回命令模式，在进入末行模式保存。

末行模式：在命令模式输入“：”即可进入该模式。

命令模式(光标移到哪行就是对哪行进行操作)
yy		复制
5yy		复制当前行在内的以下5行
p		粘贴
dd		剪切
5dd		复制当前行在内的以下5行
u		撤销
ctrl+r		撤销后的恢复

末行模式
/单词		按n或N向下、向上继续查找
q!		强制不保存退出
wq!		强制保存退出

## 创建文件

使用vi/vim保存退出，使用touch命令当文件不存在时创建文件
#cat >> file	输入结束后换行按Ctrl+d保存退出
#mkdir -p direction	创建多级目录
#echo "/usr/local/bin/redis-server /etc/redis.conf" >>/etc/(rc.d/)rc.local	添加开机启动项

## 查看文件

#tail -n 100 filename	显示文件尾部100行内容
#tail -100f filename	查看文件尾部100行内容并实时监控 
#cat -n filename	查看所有文件内容并编号

## 查看文件和文件夹大小

-  df 可以查看一级文件夹大小、使用比例、档案系统及其挂入点，但无法查看文件
-  du 查询文件或文件夹的磁盘使用空间。

```sh
查看根目录的磁盘使用情况，从而查看磁盘的分区和已经使用量
参数-h表示使用「Human-readable」的输出，也就是在档案系统大小使用 GB、MB 等易读的格式。
#df -h

文件系统        	容量  已用  可用 已用% 挂载点
devtmpfs         32G     0   32G    0% /dev
tmpfs            32G  4.0K   32G    1% /dev/shm
/dev/sda2      1016M  166M  850M   17% /boot
/dev/sda1       381M  9.8M  372M    3% /boot/efi
/dev/sdb2       3.6T   89M  3.4T    1% /home/dbown
tmpfs           6.3G   12K  6.3G    1% /run/user/42
tmpfs           6.3G     0  6.3G    0% /run/user/0
```

`/dev/sda2、/dev/sda1、/dev/sdb2`是磁盘分区，`tmps`等是系统运行时，在内存划分的一部分区域。这部分区域被临时挂载到文件树下。

```sh
fdisk+磁盘设备名称进入磁盘操作命令
#fdisk /dev/sda
```

```sh
列出/home/work/目录下面一级子目录大小，--max-depth 用于指定深入目录的层数
#du -h --max-depth=1 /home/work/    
列出/home/work/目录下面一级子目录和文件大小
#du -h --max-depth=1 /home/work/*   
```

参考：
[Linux磁盘分区的详细步骤](https://blog.csdn.net/dufufd/article/details/80253561)

## 复制文件

```sh
此选项通常在复制目录时使用，它保留链接、文件属性，并复制目录下的所有内容
#cp -a dir
覆盖已经存在的目标文件而不给出提示
#cp -f 
```

```sh
复制一个文件的前100000行到另一个文件中
#head -n 100000 data.txt > sample.txt
复制一个文件的后100000行到另一个文件中
#tail -n 100000 data.txt > sample.txt
```

## 删除文件rm(remove)

- -f		强制删除文件或direction
- -r		将指定目录下的所有文件和子目录一并删除

```
#rm -f *		删除当前目录下的所有类型的文件
#rm -rf direction	将会删除目录以及其下所有文件、文件夹
```

### 筛选文件并删除

找到根目录下所有的以test开头的文件并把查找结果当做参数传给rm -rf命令进行删除： 

```sh
使用xargs参数
#find . -type f  -name 'test*' | xargs rm -rf
使用-delete参数
#find . -type f  -name 'test*' -delete
使用-exec参数
#find . -type f  -name 'test*' -exec rm -rf {} \；
```

## 查找文件

```sh
#find / -name "april*"	查找根目录及其子目录下所有以april开头的目录和文件
#find -name   "[A-Z]*"	查找当前目录及其子目录下所有以大写字母开头的目录和文件
#find -name   "[A-Z]*" -type d/f	查找当前目录及其子目录下所有以大写字母开头的目录/文件
#whereis 文件名	定位可执行文件、源代码文件、帮助文件在文件系统中的位置
#locate test	查找和test相关的所有文件
#locate /etc/sh	搜索etc目录下所有以sh开头的文件,可以用#updatedb 命令用来创建或更新 slocate/locate 命令所必需的数据库文件
```

参考：
Linux find 用法示例
https://www.cnblogs.com/wanqieddy/archive/2011/06/09/2076785.html

## 显示目录内容列表

#ls		
#ll		为"ls -l"，列出当前目录可见文件详细信息
#ll -h	可查看当前目录文件大小
#ll -a		显示当前目录所有文件详细信息，包括隐藏目录，解析如下

如：
#ll
drwxr-xr-x. 1 root root 4096 Sep 18 03:30 Documents

以Documents目录的权限来解说，drwxr-xr-x是它的权限，d代表目录，r-读，w-写，x-执行，1代表的是节点数，紧跟着的root是代表这个文件所属的用户是哪个，下一个root代表的是所属的用户群组是哪个，4096是大小，Sep 18 03:30代表创建时间，最后就是创建的文件或目录的名字了。

drwxr-xr-x详解：
d：第一位表示文件类型，d是目录文件、l是链接文件、-是普通文件、p是管道；
rwx：第2-4位表示这个文件的属主(拥有者)拥有的权限。r是读、w是写、x是执行；
r-x：第5-7位表示和这个文件属主所在同一个组的用户(同组用户)所具有的权限；
r-x：第8-10位表示其他用户(其他用户)所具有的权限。

	权限用三位八进制数字表示，r是4，w是2，x是1，三位相加即为数字权限，r，w，x直接谁与谁相加都不会有重复值，也就是说读写执行操作随意组合得出的1，2，3，4，5，6，7不会重复，一个数字代表一种权限。
	赋予权限使用chmod 读写权限 文件名，如Documents这个目录，它的所属用户的权限是rwx（7），所属用户组权限是r-x（5），其它用户对这个目录的权限是r-x（5），它的权限就是755。如用chmod指令来执行操作“chmod 775 Documents” ，这样Documents的权限便被修改了，变为drwxrwxr-x。

#chmod -x filename		收回filename执行权限
#chmod +x filename		给filename执行权限

## Set uid, gid,sticky bit的三个权限的详细说明

setuid(SUID): 设置使文件在执行阶段具有文件所有者的权限，即允许普通用户以root身份暂时执行该程序，并在执行结束后再恢复身份

setgid(SGID): 该权限一般只对目录有效. 目录被设置该位后, 任何用户在此目录下创建的文件都具有和该目录所属的组相同的组；文件也是可以被设置为SGID的，比如一个可执行文件为赋予SGID，它就具有所有组的特权，任意存取所有组所能使用的系统资源，复制到拥有；在复制时需要加上-p参数，才能保留原来的组群设置

sticky bit: 该位可以理解为防删除位.设置该位后, 目录和目录下的文件的其他用户即使有写权限也无法删除.移动等，只能被文件所有者删除，移动等

	SUID占用属主x（执行）位，SGID占用组x位，sticky-bit占用其他x位，如果该位有x权限，就用小写s，没有就用大写S


​	
​	
使用标志操作权限
​	u：User，即文件或目录的拥有者。
　　g：Group，即文件或目录的所属群组。
　　o：Other，除了文件或目录拥有者或所属群组之外，其他用户皆属于这个范围。
　　a：All，即全部的用户，包含拥有者，所属群组以及其他用户。
　　r：读取权限，数字代号为"4"。
　　w：写入权限，数字代号为"2"。
　　x：执行或切换权限，数字代号为"1"。
​	s
​	t
#chmod u+s temp — 为temp文件加上setuid标志. (setuid 只对文件有效)
#chmod g+s tempdir — 为tempdir目录加上setgid标志 
#chmod o+t tempdir — 为tempdir目录加上sticky标志 (sticky只对目录有效)



也可以用数值设定特殊权限
	需要4位8进制数，第一个表示特殊权限，后三位表示基本权限,只说第一位8进制代表权限
	0: 不设置特殊权限 
	1: 只设置sticky 
	2 : 只设置SGID 
	4 : 只设置SUID 
#chmod 777 temp - rwxrwxrwx
#chmod 4777 temp - rwSrwxrwx
#chmod 2777 temp - rwxrwSrwx
#chmod 1777 temp - rwSrwxrwt


参考：
chmod g+s 、chmod o+t 、chmod u+s
https://blog.csdn.net/taiyang1987912/article/details/41121131

## 版本信息

--系统版本
#lsb_release -a
#cat /etc/issue

--内核版本
#cat /proc/version

--cpu信息
#cat /proc/cpuinfo

T:【查看centos或redhat版本】
#cat /etc/centos-release	#cat /etc/redhat-release
CentOS Linux release 7.7.1908 (Core)



## 用户

--添加用户
#useradd
-g	指定用户所属的群组。
-m	自动建立用户的登入目录，在/home目录下创建同名文件夹，可以用此用户账号直接登录
-r	建立系统帐号，系统帐号就是系统用的帐号，区别于个人帐号，一般系统帐号编号都是 <500，是专门用来跑对外提供服务程序的帐号，使用系统账号可以隔离个人账号中的个人信息数据

--修改用户
#usermod
-g	修改用户的初始组，或修改 /etc/passwd 文件目标用户信息的第 4 个字段（GID）

--删除用户
#userdel
-r	表示在删除用户的同时删除用户的家目录。

--查看用户

```java
#cat /etc/passwd	查看所有用户信息
root:x:0:0:root:/root:/bin/bash
```

username password UserID GroupID comment home directory shell(用冒号分开)
用户名、密码、用户id、用户所在组id、备注、用户家目录、shell命令所在目录

username password UserID GroupID comment home directory shell(用冒号分开)
用户名、密码、用户id、用户所在组id、备注、用户家目录、shell命令所在目录

--查看所有组信息

```java
#cat /etc/group
root:x:0:
用户组、用户组口令、OID、用户组包含用户
```

用户组、用户组口令、OID、用户组包含用户

--查看用户组
#groups
当前登录用户所属组

#groups 用户	
用户：用户所属组

## 命令重导向

<  ：由 < 的右边读入参数档案；

>  ：将原本由屏幕输出的数据输出到 > 右边的 file ( 文件名称 ) 或 device ( 装置，如 printer )去；
>  > ：将原本由屏幕输出的数据输出到 >> 右边，与 > 不同的是，该档案将不会被覆盖，而新的数据将以『增加的方式』增加到该档案的最后面；
>  > 1> ：将原本应该由屏幕输出的正确数据输出到 1> 的右边去。
>  > 2> ：将原本应该由屏幕输出的错误数据输出到 2> 的右边去。
>  > /dev/null ：数据丢弃

如：

ls -al 1> list.txt 2> /dev/null		将显示的数据，正确的输出到 list.txt，错误的数据则予以丢弃

## linux中环境变量配置

执行顺序为：/etc/profile -> (~/.bash_profile | ~/.bash_login | ~/.profile) -> ~/.bashrc -> /etc/bashrc -> ~/.bash_logout

注:
～在Linux中代表用户主目录（家目录）
对一般用户，～表示/home/（用户名）
对于root用户，～表示/root

参考:
Linux 中/etc/profile、~/.bash_profile 环境变量配置及执行过程
https://blog.csdn.net/hnoysz/article/details/78666272

## /etc/ld.so.conf文件

此文件记录了编译时使用的动态库的路径，也就是加载so库的路径。
参考：/etc/ld.so.conf详解	https://www.cnblogs.com/chris-cp/p/3591306.html

## chown或chgrp

//超级用户和属于组的文件所有者，可以使用此命令设置文件所有者和文件关联组，"-R"处理指定目录以及子目录下的所有文件，设置了新的文件拥有者：(或".")新的使用者组，使用者组可以省略
#chown -R root:root /usr/local/mysql/或
#chown -R root.root /usr/local/mysql/	

//普通用户可以变更文件或目录的所属群组，只要该用户是该组的一员，也就是说文件所有者将，文件组改成文件所有者所在用户组
#chgrp bin log2012.log

## 解压缩命令

**tar解压缩命令**

- -z	用于gzip压缩方式	文件名.tar.gz
- -x	解压缩、提取打包的内容
- -c	建立一个压缩，打包文档
- -v	显示压缩或者打包的内容
- -f	f后面要接压缩后的文件的名字，只要用到tar命令，-f选项是必须要用的，-f参数在使用的时候一定排在其他参数的后面，在最右边

```sh
tar -zxvf test.tar.gz		#解压缩tar.gz文件
tar -cvf test.tar test/		#压缩为test.tar
```

**zip压缩命令**

- -r	递归压缩，将自定目录下的所有子文件以及文件一起处理

```sh
zip -r test.zip test/
zip test.zip test1.txt test2.txt...
```

**unzip解压命令**

- -d	指定文件解压后存储的目录

```sh
unzip test.war -d test/ 
```

参考：
[Linux压缩打包命令——tar、zip、unzip](https://blog.csdn.net/weixin_44901564/article/details/99682926)


## 查看端口

//t:tcp u:udp
#netstat -ntupl|grep 5000
tcp        0      0 172.16.36.51:5000       58.240.115.99:29926     ESTABLISHED 13375/java
tcp	        服务端ip:port	         客户端ip:port	            tcp状态	   进程

#lsof -i:端口号

## 查看内存信息

#cat /proc/meminfo
MemTotal:       65685108 kB
MemFree:        42266248 kB
MemAvailable:   53242164 kB
Buffers:            2004 kB
Cached:         14030488 kB
SwapCached:         3496 kB
Active:         14670264 kB
Inactive:        6991176 kB
Active(anon):    9388648 kB
Inactive(anon):  1626856 kB

可用内存=MemFree + Buffers + Cached

## rpm

 如果使用RPM安装了一些包，一般来说，RPM默认安装路径如下：

| Directory      | **Contents of Directory**                 |
| -------------- | ----------------------------------------- |
| /etc           | 一些配置文件的目录，例如/etc/init.d/mysql |
| /usr/bin       | 一些可执行文件                            |
| /usr/lib       | 一些程序使用的动态函数库                  |
| /usr/share/doc | 一些基本的软件使用手册与帮助文档          |
| /usr/share/man | 一些man page文件                          |

```java
# rpm -qa 				//查询所有套件名
# rpm -qa|grep unzip	//查询unzip安装包名称
unzip-6.0-22.el7_9.x86_64
    
# rpm -ql unzip-6.0-22.el7_9.x86_64		//查询unzip安装目录
/usr/bin/funzip
/usr/bin/unzip
/usr/bin/unzipsfx
/usr/bin/zipgrep
/usr/bin/zipinfo
/usr/share/doc/unzip-6.0
/usr/share/doc/unzip-6.0/BUGS
/usr/share/doc/unzip-6.0/LICENSE
/usr/share/doc/unzip-6.0/README
/usr/share/man/man1/funzip.1.gz
/usr/share/man/man1/unzip.1.gz
/usr/share/man/man1/unzipsfx.1.gz
/usr/share/man/man1/zipgrep.1.gz
/usr/share/man/man1/zipinfo.1.gz 
 
# rpm -qc mariadb-server-5.5.68-1.el7.x86_64	//查询mariadb数据库的相关配置文件
/etc/logrotate.d/mariadb
/etc/my.cnf.d/server.cnf
/var/log/mariadb/mariadb.log    
    
# rpm -ivh --prefix=/java  jdk-7u79-linux-x64.rpm		//将jdk-7u79-linux-x64.rpm安装到/java目录下
    -i 　显示套件的相关信息
    -v 　显示指令执行过程
    -h 　套件安装时列出标记
```

参考：[Linux如何查看YUM的安装目录](https://www.cnblogs.com/kerrycode/p/6924153.html)

## yum

YUM（全称为 Yellow dog Updater, Modified）是一个在Fedora和RedHat以及CentOS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，避免了手动安装的麻烦(寻找资源、下载；放到指定目录安装；处理依赖关系并下载依赖关系的包进行安装)。所以用yum安装，实质上是用RPM安装，所以RPM查询信息的指令都可用。

### 语法

```java
#yum [options] [command] [package ...]
```

- **options：**选项

  -h											帮助

  -y											当安装过程提示选择全部为 "yes"

  -q											不显示安装的过程

  -c  /etc/yum.conf         		表示指定yum配置文件地址

  --installroot=/usr/local    	表示指定自定义的安装目录

- **command：**要进行的操作。

  list											列出所有可安裝的软件清单命令

  update									更新所有软件命令

  update <package_name>	仅更新指定的软件命令

  install <package_name>	  仅安装指定的软件命令

  remove <package_name>   删除软件包命令

- **package：**安装的包名。

  

### 常见bug

1. centos运行yum报错：There was a problem importing one of the Python modules

   参考：https://blog.csdn.net/u013255206/article/details/78143408

2. Linux 中 yum /var/run/yum.pid 已被锁定

   rm -f /var/run/yum.pid

## Expect

Expect 是用来进行自动化控制和测试的工具，用来解决shelI脚本中不可交互的问题。比如，远程登录服务器，登录的过程是一个交互的过程，可能会需要输入yes/no，password等信息。为了模拟这种输入，可以使用Expect脚本。

**一般流程：spawn 启动追踪 —> expect 匹配捕捉关键字 ——> 捕捉到将触发send 代替人为输入指令—> interact /expect eof**

### 基本命令

- **spawn：**启动进程，并跟踪后续交互信息

- **expect：** expect的一个内部命令，判断上次输出结果里是否包含指定的字符串，如果有则立即返回，否则就等待超时时间后返回，只能捕捉由spawn启动的进程的输出expect

- **send：**向进程发送字符串，用于模拟用户的输入， 该命令不能自动回车换行，一般要加\r或\n（回车）

- **interact：**执行完成后保存交互状态，把控制权交给控制台

- **expect eof：** 是在等待结束标志。由spawn启动的命令在结束时会产生一个eof标记，expect eof 即在等待这个标记。Expect脚本必须以interact或expect eof 结束，执行自动化任务通常expect eof就够了
- **set timeout 30：**设置超时时间为30秒(默认的超时时间是 10 秒，通过set命令可以设置会话超时时间, 若不限制超时时间则应设置为-1)

- **exp_continue：** 允许expect继续向下执行指令meout：指定超时时间，过期则继续执行后续指令

- **send_user：** 回显命令，相当于echo

- **$argv参数数组：**Expect脚本可以接受从bash传递的参数，可以使用 [lindex $argv n] 获得，n从0开始，分别表示第一个$1，第二个$2，第三个$3……参数 ($argvn没有空格则表示脚本名称 ； $argv n有空格则代表下标)

### 语法

```java
expect "password" {send "mypassword\r"}
```

```java
//只要匹配了aaa或bbb或ccc中的任何一个，执行了了相应的send语句后将会退出该expect语句
expect {
    "aaa" {send "AAA\r"}
    "bbb" {send "BBB\r"}
    "ccc" {send "CCC\r"}
}
```

```java
//exp_continue表示继续后面的匹配，如果匹配了aaa，执行完send语句后还要继续向下匹配bbb
expect {
    "aaa" {send "AAA\r";exp_continue}
    "bbb" {send "BBB\r";exp_continue}
    "ccc" {send "CCC\r"}
}
```

### 执行

ssh远程免交互登录

**直接执行:**

```java
[root@localhost ~]#vi login.sh
#!/usr/bin/expect                           //expect文件路径
set timeout 60
set hostname [lindex $argv 0]
set password [lindex $argv 1]
spawn ssh root@$hostname
expect {
    "*yes/no"  {send "yes\r";exp_continue}
    "*password"  {send "$password\r"}
}
interact
[root@localhost ~]#chmod +x a.sh
[root@localhost ~]#./login.sh 192.168.100.101 123abc
```

**嵌入执行:**

```java
[root@localhost ~]#vi login.sh
#!/bin/bash
hostname=$1
password=$2
set timeout 60
//加载expect文件路径，其中-EOF表示expect结束标志EOF的前面可以有tab制表符或空格，没有-表示不能有    
/usr/bin/expect<<-EOF                           
spawn ssh root@${hostname}
expect {
    "(yes/no)" {send "yes\r";exp_continue}
    "*password" {send "$password\r"}
}
expect "*]#"             //匹配进入后的页面
send "exit\r"            //触发退出
expect eof               //等待结束
	EOF                  //expect结束标志，EOF前后可以有tab制表符或空格
[root@localhost ~]#chmod +x a.sh
[root@localhost ~]#./login.sh 192.168.100.101 123abc
```


参考：[Linux中EOF和Expect命令详解](https://blog.csdn.net/lixinkuan328/article/details/111991344)



## sed

通过sed命令可以处理、编辑文本文件

### 语法

```java
sed [-hnVi][-e<script>][-f<script文件>][文本文件]
```

- **-e<script>** 以选项中指定的script来处理输入的文本，可省略
- **-f<script文件>** 以选项中指定的script文件来处理输入的文本
- **-n** 仅显示script处理后的结果。
- **-i** 可以直接修改原始文件的内容，如`sed -i '$a # This is a test' test.txt`，在`test.txt`文件最后一行添加`# This is a test`，其中`$`表示最后一行



### 文本动作

- **a ：**新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行)
- **i ：**插入， i 的后面可以接字串，而这些字串会在新的一行出现(目前的上一行)
- **s ：**取代字符串，可以搭配正规表达式，例如 1,20s/old/new/g 
- **c ：**取代行， c 的后面可以接字串，这些字串可以取代 n1,n2 之间的行
- **p ：**打印，亦即将某个选择的数据印出。通常 p 会与参数 sed -n 一起运行
- **d ：**删除， d 后面通常不接任何



### 实例

#### 新增

```java
//在testfile文件的第四行后添加一行，并将结果输出
#sed -e 4a\newLine testfile
#sed -e '4a\newLine' testfile
#sed -e '4a newLine' testfile
#sed -e '4anewLine' testfile
#sed '4a newLine' testfile
    
HELLO LINUX!
Linux is a free unix-type opterating system.  
This is a linux testfile!  
Linux test  
newline
```

#### 插入

```java
//在第二行前插入
# nl /etc/passwd | sed '2i drink tea' 
1  root:x:0:0:root:/root:/bin/bash
drink tea
2  bin:x:1:1:bin:/bin:/sbin/nologin
3  daemon:x:2:2:daemon:/sbin:/sbin/nologin 
```

#### 取代字符串

```java
sed 's/要被取代的字串/新的字串/g'
```

```java
//原始信息，利用 /sbin/ifconfig 查询 IP
# /sbin/ifconfig eth0
eth0 Link encap:Ethernet HWaddr 00:90:CC:A6:34:84
inet addr:192.168.1.100 Bcast:192.168.1.255 Mask:255.255.255.0
inet6 addr: fe80::290:ccff:fea6:3484/64 Scope:Link
UP BROADCAST RUNNING MULTICAST MTU:1500 Metric:1

//将 IP 前面的部分予以删除
# /sbin/ifconfig eth0 | grep 'inet addr' | sed 's/^.*addr://g'
192.168.1.100 Bcast:192.168.1.255 Mask:255.255.255.0

//将 IP 后面的部分予以删除
# /sbin/ifconfig eth0 | grep 'inet addr' | sed 's/^.*addr://g' | sed 's/Bcast.*$//g'
192.168.1.100
```

```java
//删除/etc/passwd第三行到末尾的数据，并把bash替换为blueshell
# nl /etc/passwd | sed -e '3,$d' -e 's/bash/blueshell/'
1  root:x:0:0:root:/root:/bin/blueshell
2  daemon:x:1:1:daemon:/usr/sbin:/bin/sh
```



#### 取代行

```java
//将第2-5行的内容取代成为"No 2-5 number"
# nl /etc/passwd | sed '2,5c No 2-5 number'    
1 root:x:0:0:root:/root:/bin/bash
No 2-5 number
6 sync:x:5:0:sync:/sbin:/bin/sync
```

#### 打印

```java
//仅列出 /etc/passwd 文件内的第 5-7 行
# nl /etc/passwd | sed -n '5,7p'
5 lp:x:4:7:lp:/var/spool/lpd:/sbin/nologin
6 sync:x:5:0:sync:/sbin:/bin/sync
7 shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
    
    
    
//搜索 /etc/passwd有root关键字的行,如果root找到，除了输出所有行，还会输出匹配行。
# nl /etc/passwd | sed '/root/p'
1  root:x:0:0:root:/root:/bin/bash
1  root:x:0:0:root:/root:/bin/bash
2  daemon:x:1:1:daemon:/usr/sbin:/bin/sh
3  bin:x:2:2:bin:/bin:/bin/sh
4  sys:x:3:3:sys:/dev:/bin/sh
5  sync:x:4:65534:sync:/bin:/bin/sync

//使用-n的时候将只打印包含模板的行。
#nl /etc/passwd | sed -n '/root/p'  
1  root:x:0:0:root:/root:/bin/bash
    
//搜索/etc/passwd,找到root对应的行，执行后面花括号中的一组命令，每个命令之间用分号分隔，这里把bash替换为blueshell，再输出这行,最后的q是退出。
# nl /etc/passwd | sed -n '/root/{s/bash/blueshell/;p;q}'    
1  root:x:0:0:root:/root:/bin/blueshell   
```



#### 删除

```java
//将/etc/passwd的内容列出并且列印行号，同时，请将第 2~5 行删除
#nl /etc/passwd | sed '2,5d'    
1 root:x:0:0:root:/root:/bin/bash
6 sync:x:5:0:sync:/sbin:/bin/sync
7 shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
   
//删除第 2 行
#nl /etc/passwd | sed '2d'   

//删除第 3 到最后一行
#nl /etc/passwd | sed '3,$d'   
    
//删除/etc/passwd所有包含root的行，其他行输出
#nl /etc/passwd | sed  '/root/d'    
2  daemon:x:1:1:daemon:/usr/sbin:/bin/sh
3  bin:x:2:2:bin:/bin:/bin/sh    
```



## top

```bash
#top
#top -p 进程号
top - 09:43:54 up 195 days, 17:53,  1 user,  load average: 0.09, 0.10, 0.12
Tasks: 461 total,   1 running, 460 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.1 us,  0.2 sy,  0.0 ni, 99.7 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem : 65685108 total, 42266972 free,  8329524 used, 15088612 buff/cache
KiB Swap:  7812092 total,  7759268 free,    52824 used. 53242476 avail Mem

  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND	
12318 root      20   0 26.392g 800092  11712 S   0.3  1.2  60:20.37 java
17653 mysql     20   0 4632064 375564  13316 S   0.3  0.6  14:53.54 mysqld
19785 root      20   0  114468   2788   1344 S   0.3  0.0   3:12.64 bash
```

这时可以使用以下命令操作屏幕
f或者F：从当前显示中添加或者删除列。

### 常用信息

- KiB Mem计算可用内存=free + buff/cache
- KiB Swap中used数值变化表示内存不够用了
- RES表明进程常驻内存
- 计算某个进程所占的物理内存大小公式：RES – SHR

### cpu状态信息

0.1%us【user space】— 用户空间占用CPU的百分比。
0.2%sy【sysctl】— 内核空间占用CPU的百分比。
0.0%ni【】— 改变过优先级的进程占用CPU的百分比
99.7%id【idolt】— 空闲CPU百分比
0.0%wa【wait】— IO等待占用CPU的百分比
0.0%hi【Hardware IRQ】— 硬中断占用CPU的百分比
0.0%si【Software Interrupts】— 软中断占用CPU的百分比

### 内存状态

```bash
KiB Mem : 65685108 total, 42266972 free,  8329524 used, 15088612 buff/cache(缓存的内存量)
```

### swap交换分区信息

```bash
KiB Swap:  7812092 total,  7759268 free,    52824 used. 53242476 avail Mem
```

Linux会将文件缓存提高读写效率，但是程序运行结束后，Cache Memory也不会自动释放，导致可用物理内存变少，当系统的物理内存不够用的时候，就需要将物理内存中的一部分空间释放出来，以供当前运行的程序使用。这些被释放的空间被临时保存到Swap空间中，等到那些被释放的空间中的程序要运行时，再从Swap分区中恢复保存的数据到内存中。也就是说将暂时不用的物理内存释放，里面的内容备份在Swap空间中，等用的时候再从swap空间中恢复

### 各进程（任务）的状态监控

PID — 进程id
USER — 进程所有者
PR — 进程优先级
NI — nice值。负值表示高优先级，正值表示低优先级
VIRT — 进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
RES — 进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA
SHR — 共享内存大小，单位kb
S —进程状态。D=不可中断的睡眠状态 R=运行 S=睡眠 T=跟踪/停止 Z=僵尸进程
%CPU — 上次更新到现在的CPU时间占用百分比
%MEM — 进程使用的物理内存百分比
TIME+ — 进程使用的CPU时间总计，单位1/100秒
COMMAND — 进程名称（命令名/命令行）

VIRT虚拟内存
RES常驻内存：进程当前使用的内存大小，包含其他进程的共享
SHR共享内存：不同进程之间共享的内存通常为同一段物理内存。所有的进程都可以访问共享内存中的地址。如果某个进程向共享内存写入数据，所做的改动将立即影响到可以访问同一段共享内存的任何其他进程。



参考:
Linux top命令详解
https://www.cnblogs.com/niuben/p/12017242.html



## 服务管理

Linux 服务管理两种方式service和systemctl

service命令会在/etc/init.d/(/etc/rc.d/init.d/)目录下寻找指定的服务脚本，然后执行对应的脚本来完成命令。如果使用管理MySQL的service命令，那么只需要将mysql.server脚本放到/etc/init.d目录下即可，如：

#移动mysql.server脚本到/etc/init.d
cp /root/data/mysql-5.7.24/support-files/mysql.server  /etc/init.d/mysql
#使用service命令启动mysql
service mysql start


systemctl命令兼容了service，即systemctl也会去/etc/init.d目录下，查看，执行相关程序，如：

systemctl mysql start
systemctl mysql stop

## 开机自启动
systemctl enable mysql

此命令的服务(unit)脚本放在目录/usr/lib/systemd/system(Centos)或/etc/systemd/system(Ubuntu)
start：立刻启动后面接的 unit。
stop：立刻关闭后面接的 unit。
restart：立刻关闭后启动后面接的 unit，亦即执行 stop 再 start 的意思。
reload：不关闭 unit 的情况下，重新载入配置文件，让设置生效。
enable：设置下次开机时，后面接的 unit 会被启动。
disable：设置下次开机时，后面接的 unit 不会被启动。
status：目前后面接的这个 unit 的状态，会列出有没有正在执行、开机时是否启动等信息。
show：列出 unit 的配置。

systemctl list-units 列举已经启动的unit

参考：
linux中systemctl详细理解及常用命令
https://blog.csdn.net/skh2015java/article/details/94012643

https://blog.csdn.net/dufufd/article/details/80253561)

## 软链接

#ln -s /root/data/mysql-5.7.24/support-files/mysql.server /usr/bin

## 常用软件

### jdk

```sh
#java -version
java version "1.8.0_112"
Java(TM) SE Runtime Environment (build 1.8.0_112-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.112-b15, mixed mode)
```

#alternatives --config java
共有 2 个提供“java”的程序。

  选项    命令

   1           /usr/java/jdk1.7.0_79/bin/java
*+ 2           /home/zhsq/jdk1.8.0_112/bin/java
按 Enter 保留当前选项[+]，或者键入选项编号：2
参考:
Linux下切换使用两个版本的JDK
https://blog.csdn.net/meiLin_Ya/article/details/80650945?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param

### nginx

netstat -ntpl |grep 8763	//查看端口
ps -ef|grep nginx		//查看进程信息

nginx			//启动
nginx -s stop		//停止
nginx -s reload		//重启
nginx -c /etc/nginx.conf	//使用指定的配置文件启动
nginx -t			//测试配置文件是否有错误
nginx -v			//查看版本信息

### redis

lsof -i :6379		//查看端口进程信息

./redis-server myredis.conf	//使用指定的配置文件启动
./redis­cli ­h IP地址 ­p 端口号 -a 密码	//连接指定主机、指定端口的redis

### mysql

service mysql start | stop | status | restart		//启动|停止|查看状态|重启 
/usr/local/mysql/support-files/mysql.server start	//启动服务
/usr/local/mysql/bin/mysql -u root -p password	//登录mysql
/usr/local/mysql/bin/mysqldump -u用户名 -p密码 数据库名 > 数据库名.sql		//导出数据和表结构
/usr/local/mysql/bin/mysqldump -u用户名 -p密码 -d 数据库名 > 数据库名.sql	//只导出表结构
/usr/local/mysql/bin/mysql -u用户名 -p密码 数据库名 < 数据库名.sql		//导入数据库

### zookeeper

./zkServer.sh start | stop | status | restart		//启动|停止|查看状态|重启 
./zkCli.sh ­server 服务器地址：端口		//连接指定主机、指定端口的zookeeper

### kafka

./kafka­server­start.sh ../config/server.properties &	//启动
jps					//查看进程

