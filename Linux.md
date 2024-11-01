# Linux

## 关机重启

```sh
$shutdown -c now：取消已经执行的 shutdown 命令；
$shutdown -h now：现在关机；
$shutdown -r now：现在重启；
```

## 修改密码

```sh
$passwd root
```

## Linux时间

在Linux下，默认情况下，系统时间和硬件时间并不会自动同步。在Linux运行过程中，系统时间和硬件时间以异步的方式运行，互不干扰。硬件时间的运行，是靠BIOS电池来维持，而系统时间，是用CPU Tick来维持的。在系统开机的时候，会自动从BIOS中取得硬件时间，设置为系统时间。 

### 设置系统时间

在Linux中设置系统时间，可以用date命令：

```sh
#查看时间
$ date
Tue Feb 25 20:15:18 CST 2014

#修改时间
$ date -s "20140225 20:16:00"  #yyyymmdd hh:mm:ss
Tue Feb 25 20:16:00 CST 2014

#date 有多种时间格式可接受，查看date --help
```

### 设置硬件时间

硬件时间的设置，可以用hwclock或者clock命令。两者基本相同，只用一个就行，只不过clock命令除了支持x86硬件体系外，还支持Alpha硬件体系。

```sh
#查看硬件时间可以是用hwclock ，hwclock --show 或者 hwclock -r
$ hwclock --show
Tue 25 Feb 2014 08:21:14 PM CST -0.327068 seconds

#设置硬件时间
$ hwclock --set --date "20140225 20:23:00"

$ hwclock
Tue 25 Feb 2014 08:23:04 PM CST -0.750440 seconds
```

### 系统时间和硬件时间的同步

同步系统时间和硬件时间，可以使用hwclock命令。

```sh
#以系统时间为基准，修改硬件时间
$ hwclock --systohc <== sys（系统时间）to（写到）hc（Hard Clock）
#或者
$ hwclock -w



#以硬件时间为基准，修改系统时间
$ hwclock --hctosys
#或者
$ hwclock -s
```

### 不同机器之间的时间同步

参考：[Linux系统时间同步方法小结](https://www.cnblogs.com/williamjie/p/10768657.html)

## 文件管理

### vi编辑器

- **命令模式**：vi启动后默认进入的是命令模式，从这个模式使用命令可以切换到另外两种模式，同时无论在任何模式下只要按一下[Esc]键都可以返回命令模式。在命令模式中输入字幕“i”就可以进入vi的输入模式编辑文件。

- **输入模式**：在这个模式中我们可以编辑、修改、输入等编辑工作，在编辑器最后一行显示一个“--INSERT--”标志着vi进入了输入模式。当我们完成修改输入等操作的时候我们需要保存文件，这时我们需要先返回命令模式，在进入末行模式保存。

- **末行模式**：在命令模式输入“：”即可进入该模式。

```
命令模式(光标移到哪行就是对哪行进行操作)
yy		复制
5yy		复制当前行在内的以下5行
p		粘贴
dd		剪切
5dd		复制当前行在内的以下5行
u		撤销
ctrl+r		撤销后的恢复
```

```
末行模式
/单词		按n或N向下、向上继续查找
q!		强制不保存退出
wq!		强制保存退出
```



### 创建文件

使用vi/vim保存退出，使用touch命令当文件不存在时创建文件

```sh
$cat >> file	#输入结束后换行按Ctrl+d保存退出
$mkdir -p direction	#创建多级目录
```



### 查看文件

```sh
$tail -n 100 filename	#显示文件尾部100行内容
$tail -100f filename	#查看文件尾部100行内容并实时监控 
$cat -n filename	#查看所有文件内容并编号
```



### 查看文件和文件夹大小

-  df 可以查看一级文件夹大小、使用比例、档案系统及其挂入点，但无法查看文件
-  du 查询文件或文件夹的磁盘使用空间。

```sh
#查看根目录的磁盘使用情况，从而查看磁盘的分区和已经使用量
#参数-h表示使用「Human-readable」的输出，也就是在档案系统大小使用 GB、MB 等易读的格式。
$df -h

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
#fdisk+磁盘设备名称进入磁盘操作命令
$fdisk /dev/sda
```

```sh
#列出/home/work/目录下面一级子目录大小，--max-depth 用于指定深入目录的层数
$du -h --max-depth=1 /home/work/    
#列出/home/work/*目录下面一级子目录和文件大小
$du -h --max-depth=1 /home/work/*  
#列出/home/work/目录下面一级子目录和文件大小
$du -h --max-depth=0 /home/work/*  

$du -h --max-depth=0 /usr/local/file_ssh/
180M    /usr/local/file_ssh/
$du -h --max-depth=1 /usr/local/file_ssh/
130M    /usr/local/file_ssh/openssl-1.1.1k
40M     /usr/local/file_ssh/openssh-8.0p1
180M    /usr/local/file_ssh/

$du -h --max-depth=0 /usr/local/file_ssh/*
40M     /usr/local/file_ssh/openssh-8.0p1
1.6M    /usr/local/file_ssh/openssh-8.0p1.tar.gz
130M    /usr/local/file_ssh/openssl-1.1.1k
9.4M    /usr/local/file_ssh/openssl-1.1.1k.tar.gz
4.0K    /usr/local/file_ssh/sshd.service
$du -h --max-depth=1 /usr/local/file_ssh/*
232K    /usr/local/file_ssh/openssh-8.0p1/contrib
2.6M    /usr/local/file_ssh/openssh-8.0p1/openbsd-compat
1.4M    /usr/local/file_ssh/openssh-8.0p1/regress
40M     /usr/local/file_ssh/openssh-8.0p1
1.6M    /usr/local/file_ssh/openssh-8.0p1.tar.gz
368K    /usr/local/file_ssh/openssl-1.1.1k/Configurations
44K     /usr/local/file_ssh/openssl-1.1.1k/VMS
4.2M    /usr/local/file_ssh/openssl-1.1.1k/apps
26M     /usr/local/file_ssh/openssl-1.1.1k/crypto
284K    /usr/local/file_ssh/openssl-1.1.1k/demos
3.5M    /usr/local/file_ssh/openssl-1.1.1k/doc
504K    /usr/local/file_ssh/openssl-1.1.1k/engines
208K    /usr/local/file_ssh/openssl-1.1.1k/external
548K    /usr/local/file_ssh/openssl-1.1.1k/fuzz
1.9M    /usr/local/file_ssh/openssl-1.1.1k/include
36K     /usr/local/file_ssh/openssl-1.1.1k/ms
4.0K    /usr/local/file_ssh/openssl-1.1.1k/os-dep
3.2M    /usr/local/file_ssh/openssl-1.1.1k/ssl
77M     /usr/local/file_ssh/openssl-1.1.1k/test
20K     /usr/local/file_ssh/openssl-1.1.1k/tools
788K    /usr/local/file_ssh/openssl-1.1.1k/util
130M    /usr/local/file_ssh/openssl-1.1.1k
9.4M    /usr/local/file_ssh/openssl-1.1.1k.tar.gz
4.0K    /usr/local/file_ssh/sshd.service

```

参考：
[Linux磁盘分区的详细步骤](https://blog.csdn.net/dufufd/article/details/80253561)

### 复制文件

```sh
#此选项通常在复制目录时使用，它保留链接、文件属性，并复制目录下的所有内容
$cp -a dir
#覆盖已经存在的目标文件而不给出提示
$cp -f 
```

```sh
#复制一个文件的前100000行到另一个文件中
$head -n 100000 data.txt > sample.txt
#复制一个文件的后100000行到另一个文件中
$tail -n 100000 data.txt > sample.txt
```

### 删除文件rm(remove)

- -f		强制删除文件或direction
- -r		将指定目录下的所有文件和子目录一并删除

```sh
$rm -f *		#删除当前目录下的所有类型的文件(强烈不推荐使用)
$rm -rf direction	#将会删除目录以及其下所有文件、文件夹
```

### 筛选文件并删除

找到根目录下所有的以test开头的文件并把查找结果当做参数传给rm -rf命令进行删除： 

```sh
#使用xargs参数
$find . -type f  -name 'test*' | xargs rm -rf
#使用-delete参数
$find . -type f  -name 'test*' -delete
#使用-exec参数
$find . -type f  -name 'test*' -exec rm -rf {} \
```

### 查找文件

```sh
$find / -name "april*"	#查找根目录及其子目录下所有以april开头的目录和文件
$find -name   "[A-Z]*"	#查找当前目录及其子目录下所有以大写字母开头的目录和文件
$find -name   "[A-Z]*" -type d/f	#查找当前目录及其子目录下所有以大写字母开头的目录/文件
$whereis 文件名	#定位可执行文件、源代码文件、帮助文件在文件系统中的位置
$locate test	#查找和test相关的所有文件
$locate /etc/sh	#搜索etc目录下所有以sh开头的文件,可以用#updatedb 命令用来创建或更新 slocate/locate 命令所必需的数据库文件
```

参考：
[Linux find 用法示例](https://www.cnblogs.com/wanqieddy/archive/2011/06/09/2076785.html)

## 显示目录内容列表

```sh
$ls		
$ll		#为"ls -l"，列出当前目录可见文件详细信息
$ll -h	#可查看当前目录文件大小
$ll -a		#显示当前目录所有文件详细信息，包括隐藏目录，解析如下
```

如：

```sh
$ll
drwxr-xr-x. 1 root root 4096 Sep 18 03:30 Documents
```

以Documents目录的权限来解说，drwxr-xr-x是它的权限，d代表目录，r-读，w-写，x-执行，1代表的是节点数，紧跟着的root是代表这个文件所属的用户是哪个，下一个root代表的是所属的用户群组是哪个，4096是大小，Sep 18 03:30代表创建时间，最后就是创建的文件或目录的名字了。

### 权限详解

**drwxr-xr-x：**

1. d：第1位表示文件类型，d是目录文件、l是链接文件、-是普通文件、p是管道；

2. rwx：第2-4位表示这个文件的属主(拥有者)拥有的权限；标志为u: User，即文件或目录的拥有者。

3. r-x：第5-7位表示和这个文件属主所在同一个组的用户(同组用户)所具有的权限；标志为g：Group，即文件或目录的所属群组。

4. r-x：第8-10位表示其他用户(其他用户)所具有的权限；标志为o：Other，除了文件或目录拥有者或所属群组之外，其他用户皆属于这个范围。

> 权限用三位八进制数字表示：
>
> ​		r：读取权限，数字代号为"4"。
> 　　w：写入权限，数字代号为"2"。
> 　　x：执行或切换权限，数字代号为"1"
>
> 三位相加即为数字权限，r，w，x直接谁与谁相加都不会有重复值，也就是说读写执行操作随意组合得出的1，2，3，4，5，6，7不会重复，一个数字代表一种权限。
>
> 赋予权限使用chmod 读写权限 文件名，如Documents这个目录，它的所属用户的权限是rwx（7），所属用户组权限是r-x（5），其它用户对这个目录的权限是r-x（5），它的权限就是755。如用chmod指令来执行操作“chmod 775 Documents” ，这样Documents的权限便被修改了，变为drwxrwxr-x。

```sh
#使用数字操作权限
$chmod 755 -R dirname
#使用标志操作权限
$chmod -x filename		#收回filename执行权限
$chmod +x filename		#给filename执行权限
```

### Set uid, gid,sticky bit三个特殊权限

- `setuid(SUID)`: 设置文件在执行阶段暂时具有文件所有者的权限，即允许普通用户以root身份暂时执行该程序，并在执行结束后再恢复身份

- `setgid(SGID)`: 该权限一般只对目录有效. 目录被设置该位后, 任何用户在此目录下创建的文件都具有和该目录所属的组相同的组；文件也是可以被设置为SGID的，比如一个可执行文件为赋予SGID，它就具有所有组的特权，任意存取或复制所有组所能使用的系统资源，；在复制时需要加上-p参数，才能保留原来的组群设置

- `sticky bit`: 该位可以理解为防删除位.设置该位后, 目录和目录下的文件的其他用户即使有写权限也无法删除.移动等，只能被文件所有者删除，移动等

> SUID占用属主x（执行）位，SGID占用组x位，如果该位有x权限，就用小写s标识，没有就用大写S，比如rwS/rws
>
> sticky-bit占用其他x位，如果该位有x权限，就用小写t标识，没有就用大写T，比如rwT/rwt

```sh
#使用标志操作权限
$chmod u+s temp #为temp文件加上setuid标志. (setuid 只对文件有效)
$chmod g+s tempdir #为tempdir目录加上setgid标志 
$chmod o+t tempdir #为tempdir目录加上sticky标志 (sticky只对目录有效)
```

> 也可以用数值设定特殊权限，需要4位8进制数，第一个表示特殊权限，后三位表示基本权限，只说第一位8进制代表权限
> 	0: 不设置特殊权限 
> 	1: 只设置sticky 
> 	2 : 只设置SGID 
> 	4 : 只设置SUID 

```sh
$chmod 7000 temp #--S--S--T

$chmod 777 temp #rwxrwxrwx
$chmod 4777 temp #rwsrwxrwx
$chmod 2777 temp #rwxrwsrwx
$chmod 1777 temp #rwxrwxrwt
```

参考：
[chmod g+s 、chmod o+t 、chmod u+s](https://blog.csdn.net/taiyang1987912/article/details/41121131)

## 开机自启

```sh
$echo "/usr/local/bin/redis-server /etc/redis.conf" >>/etc/(rc.d/)rc.local	#添加开机启动项
```



## 系统信息

```sh
# 系统版本
$ lsb_release -a
$ cat /etc/issue

# 内核版本
$ cat /proc/version

# cpu信息
$ cat /proc/cpuinfo

# 查看centos或redhat版本
$ cat /etc/centos-release	
$ cat /etc/redhat-release
$ cat /etc/*release
CentOS Linux release 7.7.1908 (Core)
```

```sh
$ cat /proc/cpuinfo| grep "cpu cores"
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4
cpu cores       : 4

# 查询CPU内核个数  
$ cat /proc/cpuinfo| grep "cpu cores"| uniq
cpu cores       : 4



$ cat /proc/cpuinfo| grep "physical id"
physical id     : 0
physical id     : 0
physical id     : 0
physical id     : 0
physical id     : 0
physical id     : 0
physical id     : 0
physical id     : 0

# 查询物理CPU个数  
$ cat /proc/cpuinfo| grep "physical id"| sort| uniq| wc -l
1
```



## 用户

```sh
#添加用户
$ useradd

-g	指定用户所属的群组。
-m	自动建立用户的登入目录，在/home目录下创建同名文件夹，可以用此用户账号直接登录
-r	建立系统帐号，系统帐号就是系统用的帐号，区别于个人帐号，一般系统帐号编号都是 <500，是专门用来跑对外提供服务程序的帐号，使用系统账号可以隔离个人账号中的个人信息数据
```

```sh
#修改用户
$ usermod

-g	修改用户的初始组，或修改 /etc/passwd 文件目标用户信息的第 4 个字段（GID）
```

```sh
#删除用户
$ userdel

-r	表示在删除用户的同时删除用户的家目录。
```

```sh
#查看用户
$ cat /etc/passwd	查看所有用户信息
root:x:0:0:root:/root:/bin/bash

username password UserID GroupID comment home directory shell(用冒号分开)
用户名、密码、用户id、用户所在组id、备注、用户家目录、shell命令所在目录
```



```sh
#查看所有组信息
$ cat /etc/group
root:x:0:

用户组、用户组口令、OID、用户组包含用户
```

```sh
#查看用户组
$ groups

当前登录用户所属组

$ groups 用户	
用户：用户所属组
```



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

tar -zxvf test.tar.gz -C test/		#解压缩tar.gz文件到当前test目录
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



**不解压删除 jar包内容**

```sh
deleteJar/datacatalog.war/WEB-INF/lib/elasticsearch-7.3.1.jar

# -q 不显示指令执行过程。-d 从压缩文件内删除指定的文件。
$ cd deleteJar/
$ zip -q -d datacatalog.war WEB-INF/lib/elasticsearch-7.3.1.jar
```

## jar

```sh
# 将当前目录下的所有文件打成jar包，jar包名为main.jar
$ jar -cvf ./main.jar ./

# 使用jar命令替换jar中的一个或多个文件
https://blog.csdn.net/ab7253957/article/details/77850111

# 选项:
 -c  创建新档案
 -t  列出档案目录
 -x  从档案中提取指定的 (或所有) 文件
 -u  更新现有档案
 -v  在标准输出中生成详细输出
 -f  指定档案文件名
 -m  包含指定清单文件中的清单信息
 -n  创建新档案后执行 Pack200 规范化
 -e  为捆绑到可执行 jar 文件的独立应用程序指定应用程序入口点
 -0  仅存储; 不使用任何 ZIP 压缩
 -P  保留文件名中的前导 '/' (绝对路径) 和 ".." (父目录) 组件
 -M  不创建条目的清单文件
 -i  为指定的 jar 文件生成索引信息
 -C  更改为指定的目录并包含以下文件
# 如果任何文件为目录, 则对其进行递归处理。
# 清单文件名, 档案文件名和入口点名称的指定顺序 与 'm', 'f' 和 'e' 标记的指定顺序相同。

示例 1: 将两个类文件归档到一个名为 classes.jar 的档案中: 
    jar cvf classes.jar Foo.class Bar.class 
示例 2: 使用现有的清单文件 'mymanifest' 并将 foo/ 目录中的所有文件归档到 'classes.jar' 中: 
    jar cvfm classes.jar mymanifest -C foo/ .
```

```sh
# 手动生成jar包
项目名是 java-test, 其所在的目录是 /data/project/java-test; main方法所属的类的全限定名是 com.healchow.test.Main:

# 进入项目所在的根目录:
$ cd /data/project
# 运行打包命令: 
$ jar  cvfe  test.jar  java-test.com.healchow.test.Main  java-test 
```



## java

```sh
# 运行jar包，会自动到 jar 包中查询mainfest中定义的启动类并运行
$ java -jar  *.jar param1 param2

# 将lib下的所有jar文件以及etc下的所有配置文件添加到 classpath 中，并在classpath 中寻找 com.Start类（main方法类）并运行
$ java -cp lib/*;etc/ com.Start
# jar 文件引入classpath，通配符不能写成*.jar， 只能使用*或123*，配置文件引入classpath，只能写到目录/， 不能添加*
```



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

## 网络配置

### 网卡配置

```sh
#编辑网卡
$vi /etc/sysconfig/network-scripts/ifcfg-enp7s0f0

#修改前
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=dhcp
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp7s0f0
UUID=e5b58cfe-3963-4f89-bcc4-4995f8896603
DEVICE=enp7s0f0
ONBOOT=no

#修改后
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
#静态连接
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp7s0f0
UUID=2e7867d0-1229-4584-880a-412023b6275a
DEVICE=enp7s0f0
#网络设备开机启动
ONBOOT=yes
IPV6_PRIVACY=no
#IP地址
IPADDR=172.16.16.6
#子网掩码
NETMASK=255.255.240.0
#网关IP
GATEWAY=172.16.16.3
DNS1=8.8.8.8
ZONE=public

#重启网卡
$service network restart
#或者
$systemctl restart network
```

```sh
#如果有启动失败的情况，则删除网卡生成规则文件，然后重启服务器
$rm -f /etc/udev/rules.d/70-persistent-ipoib.rules
$reboot
```

```sh
#查看网络配置
$ip addr

ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host
       valid_lft forever preferred_lft forever
2: enp7s0f0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc mq state UP group default qlen 1000
    link/ether 00:26:2d:0d:5e:05 brd ff:ff:ff:ff:ff:ff
    inet 172.16.16.6/20 brd 172.16.31.255 scope global noprefixroute enp7s0f0
       valid_lft forever preferred_lft forever
    inet6 fe80::62a1:40a6:c851:4dc5/64 scope link noprefixroute
       valid_lft forever preferred_lft forever
3: enp9s0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc pfifo_fast state DOWN group default qlen 1000
    link/ether 00:26:2d:0d:5e:04 brd ff:ff:ff:ff:ff:ff
4: enp7s0f1: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc mq state DOWN group default qlen 1000
    link/ether 00:26:2d:0d:5e:06 brd ff:ff:ff:ff:ff:ff
```

### DNS文件配置

```sh
$vi /etc/resolv.conf 

nameserver 8.8.8.8              #google域名服务器
nameserver 114.114.114.114      #国内域名服务器(中国电信)
nameserver 202.103.24.68		#私有DNS
```

参考：

[Linux配置网卡](https://www.cnblogs.com/aknife/p/11181805.html)

[centos7 ping: www.baidu.com: Name or service not known](https://blog.csdn.net/abcd5711664321/article/details/80436457)

### 路由配置

#### route

```sh
#显示现在所有路由
#结果是自上而下， 就是说， 哪条在前面， 哪条就有优先， 前面都没有， 就用最后一条default
$route -n
Kernel IP routing table
Destination     Gateway         Genmask         Flags Metric Ref    Use Iface
0.0.0.0         172.16.16.3     0.0.0.0         UG    103    0        0 enp7s0f0
172.16.16.0     0.0.0.0         255.255.240.0   U     103    0        0 enp7s0f0
192.168.80.0    172.16.16.3     255.255.240.0   UG    103    0        0 enp7s0f0

#添加一条路由(发往192.168.80这个网段的全部要经过网关172.16.16.3)
$route add -net 192.168.80.0 netmask 255.255.240.0 gw 172.16.16.3

#删除一条路由　删除的时候不用写网关
$route del -net 192.168.80.0 netmask 255.255.240.0
```



| 输出项      | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| Destination | 目标网段或者主机                                             |
| Gateway     | 网关地址，”*” 表示目标是本主机所属的网络，不需要路由         |
| Genmask     | 网络掩码                                                     |
| Flags       | 标记。一些可能的标记如下：                                   |
|             | U — 该路由可以使用                                           |
|             | H — 该路由是到一个主机                                       |
|             | G — 该路由是到一个网关，如果没有该标志，说明目的地的直连的，它区分了间接路由和直接路由 |
|             | N — 该路由是到一个网络                                       |
|             | R — 恢复动态路由产生的表项                                   |
|             | D — 由路由的后台程序动态地安装，由重定向报文创建             |
|             | M — 由路由的后台程序修改，被重定向报文修改                   |
|             | ! — 拒绝路由                                                 |
| Metric      | 路由距离，到达指定网络所需的中转数（linux 内核中没有使用）   |
| Ref         | 路由项引用次数（linux 内核中没有使用）                       |
| Use         | 此路由项被路由软件查找的次数                                 |
| Iface       | 该路由表项对应的输出接口                                     |

##### 语法

```sh
$route  [add|del] [-net|-host] target [netmask Nm] [gw Gw] [[dev] If]
```

- add : 添加一条路由规则
- del : 删除一条路由规则
- -net : 目的地址是一个网络
- -host : 目的地址是一个主机
- target : 目的网络或主机
- netmask : 目的地址的网络掩码
- gw : 路由数据包通过的网关
- dev : 为路由指定的网络接口

```sh
#添加到主机的路由
$route add –host 192.168.168.110 dev eth0
$route add –host 192.168.168.119 gw 192.168.168.1

#添加到网络的路由
$route add –net IP netmask MASK eth0
$route add –net IP netmask MASK gw IP
$route add –net IP/24 eth1

#添加默认网关
$route add default gw 192.168.1.1

#删除路由
$route del –host 192.168.168.110 dev eth0
```

参考：

[linux 路由表设置 之 route 指令详解](https://blog.csdn.net/chenlycly/article/details/52141854)

[传统的网络配置命令ifconfig、ip](https://blog.csdn.net/qq_34595352/article/details/82218515)

#### 设置永久路由

- 在`/etc/rc.local`启动项中里添加，但是network服务重启后失效

  ```sh
  route add -net 192.168.80.0 netmask 255.255.240.0 gw 172.16.16.3
  ```

- CentOS7添加永久静态路由

  先在`/etc/sysconfig/network-scripts/`下，新建文件名为`route-*`的文件，（`*`代表网卡名）

  ```sh
  #ADDRESS后面是数字0表示第一条,有多条路由就是0，1，2，3依次
  $vim /etc/sysconfig/network-scripts/route-enp7s0f0
  ADDRESS0=192.168.80.0
  NETMASK0=255.255.240.0
  GATEWAY0=172.16.16.3
  ```

#### 路由跟踪

- Internet，即国际互联网，是目前世界上最大的计算机网络。它由遍布全球的几万局域网和数百万台计算机组成，并通过用于异构网络的TCP/IP协议进行网间通信。
- 互联网中，信息的传送是通过网中许多段的传输介质和设备（路由器，交换机，服务器，网关等等）从一端到达另一端。每一个连接在Internet上的设备，如主机、路由器、接入服务器等一般情况下都会有一个独立的IP地址。
- 通过Traceroute我们可以知道信息从你的计算机到互联网另一端的主机是走的什么路径。
- Traceroute通过发送小的数据包到目的设备直到其返回，来测量其需要多长时间。一条路径上的每个设备Traceroute要测3次。输出结果中包括每次测试的时间(ms)和设备的名称（如有的话）及其IP地址。
- Windows中为Tracert

```sh
#linux
$traceroute www.baidu.com
#windows
$tracert www.baidu.com
```

参考：

[traceroute和tracert用法详解](https://blog.csdn.net/zhouwei1221q/article/details/45370039)

### 防火墙(centos7)

#### firewalld

> 启动： systemctl start firewalld
>
> 关闭： systemctl stop firewalld
>
> 查看状态： systemctl status firewalld 
>
> 开机禁用 ： systemctl disable firewalld
>
> 开机启用 ： systemctl enable firewalld

#### systemctl

> 启动一个服务：systemctl start firewalld.service
> 关闭一个服务：systemctl stop firewalld.service
> 重启一个服务：systemctl restart firewalld.service
> 显示一个服务的状态：systemctl status firewalld.service
> 在开机时启用一个服务：systemctl enable firewalld.service
> 在开机时禁用一个服务：systemctl disable firewalld.service
> 查看服务是否开机启动：systemctl is-enabled firewalld.service
> 查看已启动的服务列表：systemctl list-unit-files|grep enabled
> 查看启动失败的服务列表：systemctl --failed

#### firewalld-cmd

> 查看版本： firewall-cmd --version
>
> 查看帮助： firewall-cmd --help
>
> 显示状态： firewall-cmd --state
>
> 查看所有打开的端口： firewall-cmd --zone=public --list-ports
>
> 更新防火墙规则： firewall-cmd --reload
>
> 查看区域信息:  firewall-cmd --get-active-zones
>
> 查看指定接口所属区域： firewall-cmd --get-zone-of-interface=eth0
>
> 拒绝所有包：firewall-cmd --panic-on
>
> 取消拒绝状态： firewall-cmd --panic-off
>
> 查看是否拒绝： firewall-cmd --query-panic



> 那怎么开启一个端口呢
>
> 添加
>
> firewall-cmd --zone=public --add-port=80/tcp --permanent   （--permanent永久生效，没有此参数重启后失效）
>
> 重新载入
>
> firewall-cmd --reload
>
> 查看
>
> firewall-cmd --zone=public --query-port=80/tcp
>
> 删除
>
> firewall-cmd --zone=public --remove-port=80/tcp --permanent



参考：

[Linux下防火墙iptables用法规则详及其防火墙配置](https://www.cnblogs.com/yi-meng/p/3213925.html)

[Linux防火墙状态查看与修改命令（含centOS7操作命令）](https://www.cnblogs.com/wei-lu/articles/11104066.html)

[Linux之 linux7防火墙基本使用及详解](https://blog.csdn.net/zhang123456456/article/details/78149206)

## 服务管理

### systemctl

> Systemctl是一个systemd工具，主要负责控制systemd系统和服务管理器。systemd即为system daemon,是linux下的一种init软件。
>
> systemctl命令是系统服务管理器指令，它实际上将 service 和 chkconfig 这两个命令组合到一起。使用它可以永久性或只在当前会话中启用/禁用服务。

```sh
#列出所有可用单元
$systemctl list-unit-files
$systemctl list-units
#列出所有服务
$systemctl list-unit-files --type=service
#列出所有失败单元
$systemctl --failed		

#检查是否配置开机启动
$systemctl is-enabled httpd.service 	
#开机启动
$systemctl enable httpd.service 
#开机不启动
$systemctl disable httpd

#启动，重启，停止服务，重新加载，查看服务运行状态
$systemctl start httpd.service
$systemctl restart httpd.service
$systemctl stop httpd.service
$systemctl reload httpd.service		#不关闭服务的情况下，重新载入配置文件，让设置生效。
$systemctl status httpd.service
```

参考：
[linux中systemctl详细理解及常用命令](https://blog.csdn.net/skh2015java/article/details/94012643)



### service

> service命令可以启动、停止、重新启动和关闭系统服务，还可以显示所有系统服务的当前状态。
>
> service命令的作用是去/etc/init.d(/etc/rc.d/init.d/)目录下寻找相应的服务，进行开启和关闭等操作。

```sh
#启动、停止、查看服务状态
$service httpd start
Redirecting to /bin/systemctl start  httpd.service
$service httpd stop
$service httpd status
```

> 如果使用管理MySQL的service命令，那么只需要将mysql.server脚本放到/etc/init.d目录下即可，如：
>

```sh
#移动mysql.server脚本到/etc/init.d
$cp /root/data/mysql-5.7.24/support-files/mysql.server  /etc/init.d/mysql
#使用service命令启动mysql
$service mysql start

#systemctl命令兼容了service，即systemctl也会去/etc/init.d目录下，查看，执行相关程序，如：
$systemctl start mysql
```



### chkconfig

> chkconfig是管理系统服务(service)的命令行工具。所谓系统服务(service)，就是随系统启动而启动，随系统关闭而关闭的程序。
>
> chkconfig可以更新(启动或停止)和查询系统服务(service)运行级信息。更简单一点，chkconfig是一个用于维护/etc/rc[0-6].d目录的命令行工具。

**设置服务是否开机启动**

```sh
##在Redhat7上，运行chkconfig命令，都会被转到systemctl命令上
$chkconfig httpd on
注意：正在将请求转发到“systemctl enable httpd.service

$chkconfig httpd off/reset
```

- on、off、reset用于改变service的启动信息。
- on表示开启，off表示关闭，reset表示重置。
- 默认情况下，on和off开关只对运行级2，3，4，5有效，reset可以对所有运行级有效。

**设置服务运行级别**

```sh
$ chkconfig --level 5 httpd on
注意：正在将请求转发到“systemctl enable httpd.service”
```

该命令可以用来指定服务的运行级别，即指定运行级别2,3,4,5等。

- 等级0表示：表示关机
- 等级1表示：单用户模式
- 等级2表示：无网络连接的多用户命令行模式
- 等级3表示：有网络连接的多用户命令行模式
- 等级4表示：不可用
- 等级5表示：带图形界面的多用户模式
- 等级6表示：重新启动

**列出服务启动信息**

```sh
$ chkconfig --list [name]
```

```sh
#如果不指定name，会列出所有services的信息
$chkconfig --list
netconsole      0:关    1:关    2:关    3:关    4:关    5:关    6:关
network         0:关    1:关    2:开    3:开    4:开    5:开    6:关
rhnsd           0:关    1:关    2:开    3:开    4:开    5:开    6:关
```

每个service每个运行级别都会有一个启动和停止脚本；当切换运行级别时，init不会重启已经启动的service，也不会重新停止已经停止的service。

参考：

[Linux下systemctl命令和service、chkconfig命令的区别](https://blog.csdn.net/qq_38265137/article/details/83081881)



#### 系统运行级别

> 运行级别就是操作系统当前正在运行的功能级别。 它让一些程序在一个级别启动，而另外一个级别的时候不启动。

| 级别 | 说明                                                        |
| ---- | ----------------------------------------------------------- |
| 0    | 系统停机状态，系统默认运行级别不能设为0，否则不能正常启动   |
| 1    | 单用户工作状态，root权限，用于系统维护，禁止远程登陆        |
| 2    | 多用户状态(没有NFS)                                         |
| 3    | 完全的多用户状态(有NFS)，登陆后进入控制台命令行模式         |
| 4    | 系统未使用，保留                                            |
| 5    | X11控制台，登陆后进入图形GUI模式                            |
| 6    | 系统正常关闭并重启，默认运行级别不能设为6，否则不能正常启动 |

标准的Linux运行级为3或者5，如果是3的话，系统就在多用户状态。如果是5的话，则是运行着X Window 系统。如果目前正在3或5，而你把运行级降低到2的话，init就会执行K45named脚本。



**运行级别原理**

1. 运行级别定义在/etc/inittab文件中。这个文件是init程序寻找的主要文件。init是Linux系统里的根进程，是系统所有进程的祖先。它的主要作用是根据记录在/etc/inittab里的一个脚本（script）程序产生进程。 这个文件通常用于控制用户的登录模式。
2. 在/etc/rc.d下有7个名为rcN.d的目录，对应系统的7个运行级别。rcN.d目录下都是一些符号链接文件，这些链接文件都指向init.d目录下的service脚本文件，命名规则为K+nn+服务名或S+nn+服务名，其中nn为两位数字。
3. 系统会根据指定的运行级别进入对应的rcN.d目录，并按照文件名顺序检索目录下的链接文件：对于以K开头的文件，系统将终止对应的服务；对于以S开头的文件，系统将启动对应的服务。
4. 在目录/etc/rc.d/init.d下有许多服务器脚本程序，一般称为服务(service)

```sh
#查看运行级别
$runlevel
N 5

#进入其它运行级别
$init N

#init 0为关机，init 6为重启系统
```



参考：

[Linux系统的运行级别](https://www.cnblogs.com/l75790/p/9301775.html)

[linux操作系统的7种运行级别的详细说明](https://blog.csdn.net/ymeng9527/article/details/102678485)

[Linux的运行级别和chkconfig用法](https://www.cnblogs.com/itfat/p/7268122.html)



## 软链接

#ln -s /root/data/mysql-5.7.24/support-files/mysql.server /usr/bin



## 常用命令

### rpm

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

### yum

YUM（全称为 Yellow dog Updater, Modified）是一个在Fedora和RedHat以及CentOS中的Shell前端软件包管理器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包，避免了手动安装的麻烦(寻找资源、下载；放到指定目录安装；处理依赖关系并下载依赖关系的包进行安装)。所以用yum安装，实质上是用RPM安装，所以RPM查询信息的指令都可用。

#### 语法

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

```sh
# 安装下载上传工具
$ yum install -y lrzsz
# 显示文件夹
$ rz
```



#### 常见bug

1. centos运行yum报错：There was a problem importing one of the Python modules

   参考：https://blog.csdn.net/u013255206/article/details/78143408

2. Linux 中 yum /var/run/yum.pid 已被锁定

   rm -f /var/run/yum.pid

### Expect

Expect 是用来进行自动化控制和测试的工具，用来解决shelI脚本中不可交互的问题。比如，远程登录服务器，登录的过程是一个交互的过程，可能会需要输入yes/no，password等信息。为了模拟这种输入，可以使用Expect脚本。

**一般流程：spawn 启动追踪 —> expect 匹配捕捉关键字 ——> 捕捉到将触发send 代替人为输入指令—> interact /expect eof**

#### 基本命令

- **spawn：**启动进程，并跟踪后续交互信息

- **expect：** expect的一个内部命令，判断上次输出结果里是否包含指定的字符串，如果有则立即返回，否则就等待超时时间后返回，只能捕捉由spawn启动的进程的输出expect

- **send：**向进程发送字符串，用于模拟用户的输入， 该命令不能自动回车换行，一般要加\r或\n（回车）

- **interact：**执行完成后保存交互状态，把控制权交给控制台

- **expect eof：** 是在等待结束标志。由spawn启动的命令在结束时会产生一个eof标记，expect eof 即在等待这个标记。Expect脚本必须以interact或expect eof 结束，执行自动化任务通常expect eof就够了
- **set timeout 30：**设置超时时间为30秒(默认的超时时间是 10 秒，通过set命令可以设置会话超时时间, 若不限制超时时间则应设置为-1)

- **exp_continue：** 允许expect继续向下执行指令meout：指定超时时间，过期则继续执行后续指令

- **send_user：** 回显命令，相当于echo

- **$argv参数数组：**Expect脚本可以接受从bash传递的参数，可以使用 [lindex $argv n] 获得，n从0开始，分别表示第一个$1，第二个$2，第三个$3……参数 ($argvn没有空格则表示脚本名称 ； $argv n有空格则代表下标)

#### 语法

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

#### 执行

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



### sed

通过sed命令可以处理、编辑文本文件

#### 语法

```java
sed [-hnVi][-e<script>][-f<script文件>][文本文件]
```

- **-e<script>** 以选项中指定的script来处理输入的文本，可省略
- **-f<script文件>** 以选项中指定的script文件来处理输入的文本
- **-n** 仅显示script处理后的结果。
- **-i** 可以直接修改原始文件的内容，如`sed -i '$a # This is a test' test.txt`，在`test.txt`文件最后一行添加`# This is a test`，其中`$`表示最后一行



#### 文本动作

- **a ：**新增， a 的后面可以接字串，而这些字串会在新的一行出现(目前的下一行)
- **i ：**插入， i 的后面可以接字串，而这些字串会在新的一行出现(目前的上一行)
- **s ：**取代字符串，可以搭配正规表达式，例如 1,20s/old/new/g 
- **c ：**取代行， c 的后面可以接字串，这些字串可以取代 n1,n2 之间的行
- **p ：**打印，亦即将某个选择的数据印出。通常 p 会与参数 sed -n 一起运行
- **d ：**删除， d 后面通常不接任何



#### 实例

##### 新增

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

##### 插入

```java
//在第二行前插入
# nl /etc/passwd | sed '2i drink tea' 
1  root:x:0:0:root:/root:/bin/bash
drink tea
2  bin:x:1:1:bin:/bin:/sbin/nologin
3  daemon:x:2:2:daemon:/sbin:/sbin/nologin 
```

##### 取代字符串

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



##### 取代行

```java
//将第2-5行的内容取代成为"No 2-5 number"
# nl /etc/passwd | sed '2,5c No 2-5 number'    
1 root:x:0:0:root:/root:/bin/bash
No 2-5 number
6 sync:x:5:0:sync:/sbin:/bin/sync
```

##### 打印

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



##### 删除

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



### top

```sh
$ top -p 进程号
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

**常用信息**

- KiB Mem计算可用内存=free + buff/cache
- KiB Swap中used数值变化表示内存不够用了
- RES表明进程常驻内存
- 计算某个进程所占的物理内存大小公式：RES – SHR

```sh
top - 09:53:13 up 34 days, 16:37, 12 users,  load average: 0.24, 0.30, 0.38
显示开机运行时间，当前时间，在线用户数，平均负载

09:53:13  当前时间
up 34 days, 16:37  系统开始运行时间，格式为时:分
12 users  当前登录用户数
load average: 0.24, 0.30, 0.38  系统负载，即任务队列的平均长度，三个数值分别为1分钟、5分钟、15分钟前到现在的平均值。
```

```sh
Tasks: 193 total,   1 running, 192 sleeping,   0 stopped,   0 zombie
任务数量和状态

193 total 进程的总数
1 running 正在运行的进程数
192 sleeping 睡眠的进程数
0 stopped 停止的进程数
0 zombie 僵尸进程数
```

```sh
Cpu(s):  0.7%us, 0.2%sy, 0.0%ni, 98.8%id, 0.1%wa, 0.0%hi, 0.2%si, 0.0%st
CPU状态信息

0.7%us  用户空间占用CPU百分比【user space】
0.2%sy	内核空间占用CPU百分比【sysctl】
0.0%ni  用户进程空间内改变过优先级的进程占用CPU百分比
98.8%id	空闲CPU百分比【idolt】
0.1%wa  IO等待CPU时间的百分比【wait】
0.0%hi  硬中断占用CPU的百分比【Hardware IRQ】
0.2%si  软中断占用CPU的百分比【Software Interrupts】
0.0%st
```

```sh
Mem:  24672844k total, 21493856k used,  3178988k free,   744316k buffers
内存占用情况

24672844k total  物理内存总量
21493856k used  使用的物理内存总量
3178988k free  空闲内存总量
744316k buffers  用作内核缓存的内存量
```

```sh
Swap: 16779852k total,   340780k used, 16439072k free, 12905060k cached
交换分区信息
	
16779852k total  交换分区总量
340780k used  使用的交换区总量
16439072k free  空闲交换区总量
12905060k cached   缓冲的交换区总量。
```

Linux会将文件缓存提高读写效率，但是程序运行结束后，Cache Memory也不会自动释放，导致可用物理内存变少，当系统的物理内存不够用的时候，就需要将物理内存中的一部分空间释放出来，以供当前运行的程序使用。这些被释放的空间被临时保存到Swap空间中，等到那些被释放的空间中的程序要运行时，再从Swap分区中恢复保存的数据到内存中。也就是说将暂时不用的物理内存释放，里面的内容备份在Swap空间中，等用的时候再从swap空间中恢复

```sh
  PID  USER  PR  NI  VIRT  RES  SHR    S  %CPU  %MEM  TIME+  COMMAND
12726  csp   16   0  5014m  3.0g  169m S  5      12.8    243:59.39  java
进程信息

PID    进程id
PPID    父进程id
RUSER    Realusername
UID    进程所有者的用户id
USER    进程所有者的用户名
GROUP    进程所有者的组名
TTY    启动进程的终端名。不是从终端启动的进程则显示为?
PR    进程优先级
NI 		nice值。负值表示高优先级，正值表示低优先级
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
S    进程状态。D=不可中断的睡眠状态 R=运行 S=睡眠 T=跟踪/停止 Z=僵尸进程
COMMAND    进程名称（命令名/命令行）
WCHAN    若该进程在睡眠，则显示睡眠中的系统函数名
Flags    任务标志，参考sched.h
```

VIRT虚拟内存
RES常驻内存：进程当前使用的内存大小，包含其他进程的共享
SHR共享内存：不同进程之间共享的内存通常为同一段物理内存。所有的进程都可以访问共享内存中的地址。如果某个进程向共享内存写入数据，所做的改动将立即影响到可以访问同一段共享内存的任何其他进程。

参考:
[Linux top命令详解](https://www.cnblogs.com/niuben/p/12017242.html)

### dirname

获取给定路径的目录部分 

```sh
$ dirname /usr/sbin/cron
/usr/sbin
$ dirname /usr/sbin/
/usr
$ dirname /usr/sbin
/usr
```

### set

```sh
# 命令行下不带任何参数，直接运行set，会显示所有的环境变量和 Shell 函数。
$ set
```

```sh
# 在shell脚本中，set命令用来修改 Shell 环境的运行参数，也就是可以定制环境，如：
#!/bin/bash
set -ex

# 遇到不存在的变量就会报错，并停止执行
set -u

# 用来在运行结果之前，先输出执行的那一行命令。
set -x

# 脚本只要发生错误，就终止执行。
set -e

#set -e有一个例外情况，就是不适用于管道命令。
#所谓管道命令，就是多个子命令通过管道运算符（|）组合成为一个大的命令。Bash 会把最后一个子命令的返回值，作为整个命令的返回值。也就是说，只要最后一个子命令不失败，管道命令总是会执行成功，因此它后面命令依然会执行，set -e就失效了。set -o pipefail用来解决这种情况，只要一个子命令失败，整个管道命令就失败，脚本就会终止执行。
set -o pipefail

# 整合写法，建议放在所有 Bash 脚本的头部。
# 写法一
set -euxo pipefail

# 写法二
set -eux
set -o pipefail

#另一种办法是在执行 Bash 脚本的时候，从命令行传入这些参数。
$ bash -euxo pipefail script.sh
```



[Bash 脚本 set 命令教程](http://www.ruanyifeng.com/blog/2017/11/bash-set.html)

[set命令详解](https://www.cnblogs.com/insane-Mr-Li/p/9096859.html)



### xargs

将参数列表转换成小块分段传递给其他命令，以避免参数列表过长的问题。

xargs 可以从管道、stdin 或文件中读取数据，并把这些数据转换成命令行参数。它通常用作给其他命令传递参数的过滤器，也是组合多个命令的工具。xargs 默认的命令是 echo，这意味着通过管道传递给 xargs 的输入将会包含换行和空白，不过通过 xargs 的处理，换行和空白将被空格取代。

xargs的常用选项

- -n 选项表示每次构建命令行的时候取几个参数
- -d 选项则用于指定分割符，默认情况下 xargs 会将标准输入中的内容按照空白字符（空格、换行）分割。
- -p 选项，使用该选项之后 xargs 并不会马上执行其后面的命令，而是输出即将要执行的完整的命令（包括命令以及传递给命令的命令行参数），询问是否执行，输入 y 才继续执行，否则不执行。
- -t 在执行每个命令之前先打印出该命令
- `-I{}` 是一个选项，为 `xargs` 替换传递给命令的参数。如：`cat files.txt | xargs -I{} find /path/to/search -name {}`，`-I{}` 告诉 `xargs` 使用 `{}` 作为每个参数的占位符。然后，`find` 命令将使用这个占位符来查找文件。

```sh
# 方式一：逐个拷贝
cp redis-6.2.4/redis.conf dir_7001
cp redis-6.2.4/redis.conf dir_7002
cp redis-6.2.4/redis.conf dir_7003

# 方式二：管道组合命令，一键拷贝
echo dir_7001 dir_7002 dir_7003 | xargs -t -n 1 cp redis-6.2.4/redis.conf
```

```sh
# 逐一执行
sed -i '1a replica-announce-ip 192.168.150.101' dir_7001/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' dir_7002/redis.conf
sed -i '1a replica-announce-ip 192.168.150.101' dir_7003/redis.conf

# 或者一键修改
printf '%s\n' dir_7001 dir_7002 dir_7003 | xargs -I{} -t sed -i '1a replica-announce-ip 192.168.150.101' {}/redis.conf
```

### printf

用于格式化输出

```
printf format-options string
```

 其中，`format-options` 是你希望输出的格式，而 `string` 是包含这些格式选项的字符串。

以下是一些常用的 `printf` 格式选项：

- `%d`：输出十进制整数。
- `%s`：输出字符串。
- `%f`：输出浮点数。
- `%c`：输出字符。
- `%x`：输出十六进制数。
- `%%`：输出百分号。

```sh
# 输出一个简单的字符串
printf "Hello, World!\n"

# 输出一个整数
printf "The value of pi is approximately %d\n" 3.141592653589793
```



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

## 文件过大无法复制到U盘

U盘使用的FAT32文件系统，不能存储单个文件大于4G的文件，需要转为NTFS文件系统

**cmd**

```sh
$convert f:/fs:ntfs
```

