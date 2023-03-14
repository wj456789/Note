## Linux

### zip命令

基本用法：

`zip [参数][压缩包名][压缩的目录或者文件的路径]`

常用参数：
zip命令的常用参数
-m	将文件压缩后，删除原文件
-o	将压缩文件内的所有文件的最新变动时间设为压缩的时间
-q	安静模式，在压缩的时候不显示指令执行的过程
-r	递归压缩，将自定目录下的所有子文件以及文件一起处理
-x	”文件列表“，压缩时排除文件列表中的文件

### unzip命令

`unzip [参数][压缩文件]  （-d [目录]）`  //如果不是用括号里面的内容，则解压文件在当前工作目录 

unzip命令的常用参数
-c	将解压缩的结果显示到屏幕上（显示每一个目录下的每一个文件的内容），同时对字符做适当的转换，但是并没有解压压缩包
-l	显示压缩文件内所包含的文件
-t	检查压缩文件是否正确
-v	执行时显示压缩文件的详细信息
-q	安静模式，执行时不显示任何信息
-d	指定文件解压后存储的目录
-x	指定不要处理压缩文件中的那些文件



nl 可以将输出的文件内容自动的加上行号！其默认的结果与 cat -n 有点不太一样， nl 可以将行号做比较多的显示设计，包括位数与是否自动补齐 0 等等的功能。   

```sh
# 返回file中，与str匹配的行数（一共有几行）
$ grep -c str file
$ nl file |grep str |wc -l
```

### 删除文件排除

```sh
$ rm -rf `ls WEB-INF/lib/* | grep -v WEB-INF/lib/c_pcm_smsUtils.jar`
```



### 删除文件后没有释放空间

1. [/dev/sda2占用100%、磁盘占用100%的怎么解决？](/dev/sda2占用100%、磁盘占用100%的怎么解决？ ) 

   ```sh
   $ df -h
   $ du -sh /* | sort -nr # 命令查找 / 目录下所有文件和目录的大小的排序结果
   ```

2. 在Linux或者Unix系统中，通过rm或者文件管理器删除文件将会从文件系统的目录结构上解除链接(unlink).然而如果文件是被 打开的（有一个进程正在使用），那么进程将仍然可以读取该文件，磁盘空间也一直被占用。 

   ```sh
   # 获得一个已经被删除但是仍然被应用程序占用的文件列表
   $ lsof |grep deleted
   pulseaudi 16848                  gdm    6u      REG                0,4  67108864      96183 /memfd:pulseaudio (deleted)
   null-sink 16848 17030            gdm    6u      REG                0,4  67108864      96183 /memfd:pulseaudio (deleted)
   
   # 可以看出两个文件分别占用67108864 B空间，一共大概134M
   
   $ ps -ef|grep 16848
   gdm      16848     1  0 Mar09 ?        00:00:00 /usr/bin/pulseaudio --start --log-target=syslog
   
   # 停掉使用这个文件的应用或者kill掉相应的进程，让os自动回收磁盘空间
   ```

   [linux删除文件后没有释放空间](https://blog.csdn.net/wyzxg/article/details/4971843)

3. ```sh
   # 查看inode使用率，inode不够用也会导致此问题
   $ df -i
   文件系统					Inodes	已用(I)	可用(I) 已用(I)%% 挂载点
   Filesystem                    Inodes  IUsed    IFree IUse% Mounted on
   /dev/vda3                     917504 133559   783945   15% /
   devtmpfs                     4047378    443  4046935    1% /dev
   tmpfs                        4050638      1  4050637    1% /dev/shm
   tmpfs                        4050638   1710  4048928    1% /run
   tmpfs                        4050638     17  4050621    1% /sys/fs/cgroup
   /dev/vda1                      65536    341    65195    1% /boot
   /dev/mapper/vg1-lv2          3276800     10  3276790    1% /home/test
   /dev/mapper/euleros-var       524288   1932   522356    1% /var
   /dev/mapper/euleros-var_tmp   262144     35   262109    1% /tmp
   /dev/mapper/euleros-var_log   262144    194   261950    1% /var/log
   /dev/mapper/vg1-lv1         13107200  78027 13029173    1% /home/dataasset
   tmpfs                        4050638      9  4050629    1% /run/user/42
   tmpfs                        4050638      1  4050637    1% /run/user/1007
   tmpfs                        4050638      1  4050637    1% /run/user/0
   ```

   [Linux磁盘空间满，但实际占用却没有那么大](https://blog.csdn.net/lqy461929569/article/details/77895704)

4. ```sh
   # 查看隐藏文件
   $ ls -lha
   ```

5. 新磁盘还没挂载就开始写数据了 ，挂载后这部分数据看不到

   [Linux 离奇磁盘爆满，如何解决？](https://cloud.tencent.com/developer/news/590360)

### grep

```sh
# grep 命令用于查找文件里符合条件的字符串。

# 查找指定目录/etc/acpi 及其子目录下所有文件中包含字符串"update"的文件，打印出该字符串所在行的内容，并标示出该行的行数编号
$ grep -nr update /etc/acpi

# 在当前目录及其子目录下所有文件中查找"update"并输出
$ grep -nr update
```

### 脚本执行

像 ls 、cd 、pwd 这样的命令，它们的程序是位于 /bin 目录下，但是我们却可以在系统的任意位置都可以执行这些程序。 

那是因为这些命令对应的程序所在路径被加到了 Path 环境变量里。所以我们如果把自己的脚本路径加到了这个变量里，我们也可以实现在任意地方执行自己脚本的效果。 



```sh
# 脚本路径为/home/alvin/scripts/hello.sh

Bash 在运行起来之后，会先加载 .bashrc 文件。所以，我们可以把脚本路径添加到 .bashrc 文件，然后就能达到目标。
export PATH="/home/alvin/scripts:$PATH" 加到 .bashrc 最后

.profile 文件是一个环境变量配置文件，用户在登录系统的时候加载此文件来配置环境变量。
export PATH="$PATH:$HOME/scripts" 添加到 .profile 文件末尾

直接改环境变量文件，方法2一样，需要将用户登出再重新登录即可
PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin"  -> PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/home/alvin/scripts" 
修改/etc/environment 
```

 /etc/profile 是所有用户的环境变量 /etc/enviroment是系统的环境变量 

如果同一个变量在用户环境(/etc/profile)和系统环境(/etc/environment)有不同的值那应该是以用户环境为准了。 



执行顺序为：/etc/profile -> (~/.bash_profile | ~/.bash_login | ~/.profile) -> ~/.bashrc -> /etc/bashrc -> ~/.bash_logout 



/etc/profile： 此文件为系统的每个用户设置环境信息，每一个用户第一次登录时,该文件被执行 

/etc/bashrc: 每一个运行bash shell的用户执行此文件.当bash shell被打开时,该文件被读取。 

~/.bash_profile 是交互式、login 方式进入 bash 运行的，~/.bashrc 是交互式 non-login 方式进入 bash 运行的 

### nohup

```sh
$ nohup /root/runoob.sh > runoob.log 2>&1 &
2>&1 解释：
将标准错误 2 重定向到标准输出 &1 ，标准输出 &1 再被重定向输入到 runoob.log 文件中。
0 – stdin (standard input，标准输入)
1 – stdout (standard output，标准输出)
2 – stderr (standard error，标准错误输出)
```

### grep 

查找文件里符合条件的字符串 

```sh
grep [-abcEFGhHilLnqrsvVwxy][-A<显示行数>][-B<显示列数>][-C<显示列数>][-d<进行动作>][-e<范本样式>][-f<范本文件>][--help][范本样式][文件或目录...]
```

```sh
# 查找前缀有“test”的文件包含“test”字符串的文件，-n 标示出该行的列数编号。
$ grep -n test test*

# 以递归的方式查找符合条件的文件。例如，查找指定目录/etc/acpi 及其子目录（如果存在子目录的话）下所有文件中包含字符串"update"的文件，并打印出该字符串所在行的内容
$ grep -r update /etc/acpi

# 反向查找，通过"-v"参数可以打印出不符合条件行的内容
# 查找文件名中包含 test 的文件中不包含test 的行
$ grep -v test *test*
```

### 删除

```sh
# 删除当前test文件夹下 所有目录和文件名中包含keep字符串 之外的所有文件目录
$ find  ./test/  |  grep  -v  keep  |  xargs  rm  -rf
```



### top

用top命令查看cpu 显示的所有的cpu加起来的使用率，说明你的CPU是多核，你运行top后按大键盘1看看，可以显示每个cpu的使用率，top里显示的是把所有使用率加起来

按下1后可以看到我的机器的CPU是双核的。%Cpu0，%Cpu1

### 查看文件系统的挂载时间 

#### ext文件系统

```sh
# 如果文件系统类型为ext3、ext4，那么可以使用命令tune2fs查看文件系统最后一次挂载时间。
$ tune2fs -l /dev/sda1
...
Last mount time:          Tue Feb 12 15:36:22 2019
...
```

[Linux查看文件系统的挂载时间浅析](https://blog.51cto.com/u_15338523/3586937) 

### df

```sh
$ df -h /tmp	# 查看目录/tmp空间信息
```



### jar

> spring boot替换jar包中的jar包（Unable to open nested entry 'BOOT-INF/lib/**.jar'. It has been compressed）

```sh
# 通过jar命令先将jar包解压
$ jar -xvf test.jar
# 手动替换内部jar包再压缩
$ jar -cfM0 test.jar ./
```



### hostname

```sh
# 查看主机名，-f 显示完整的内容
$ hostname -f

# 设置主机名
$ hostname 主机名
```



### zip

```sh
# zip把文件直接增加/更新到某个压缩包指定目录
$ zip -u 压缩包路径  要存进的目录/文件名

# /home/user/test.zip		/var/mail/123.txt
$ zip -u /home/user/test.zip /var/mail/123.txt	# 123.txt在压缩包中的路径仍为 var/mail/123.txt

# 假设test.zip压缩包中有个文件夹名为Hi，我想把123.txt放在Hi文件夹下，需要在当前目录创建目录Hi，将123.txt放在Hi目录下
$ cd 当前目录
$ zip -u /home/user/test.zip Hi/123.txt
```

### unzip

在linux下解压在windows下压缩的zip文件时，如果压缩包中的文件名或目录名包含中文，就会出现乱码 

```sh
# 可以使用以下两种方法解决
$ unzip -O CP936 xxx.zip
$ unzip -O GBK xxx.zip
```



### 变量作用域

Shell脚本中在函数外和函数内定义的变量是都是global（全局）的。

- 函数外定义的变量其作用域从被定义的地方开始，到shell结束或被显示删除的地方为止。
- 函数内定义的变量其作用域是从函数被调用时变量定义的地方开始，到shell结束或被显示删除的地方为止。注意，不是从定义函数的地方开始，而是从调用函数的地方开始。 

 

脚本接收的参数是global（全局）的。

函数接收的参数是local（局部）的，其作用域在函数内。函数内部可以用local定义局部变量。e.g:  local a1=200

当局部变量和全局变量重名的时候，局部变量会屏蔽全局变量

[Linux shell脚本 变量作用域](https://www.cnblogs.com/pass-ion/p/14372742.html)



### traceroute

traceroute 一台主机时，会看到有一些行是以星号表示的。出现这样的情况，可能是防火墙封掉了ICMP的返回信息，所以我们得不到什么相关的数据包返回数据。 

[traceroute和tracert用法详解](https://blog.csdn.net/zhouwei1221q/article/details/45370039)

### hosts文件

hosts文件是Linux系统上一个负责ip地址与域名快速解析的文件，以ascii格式保存在/etc/目录下。hosts文件包含了ip地址与主机名之间的映射，还包括主机的别名。在没有域名解析服务器的情况下，系统上的所有网络程序都通过查询该文件来解析对应于某个主机名的ip地址，否则就需要使用dns服务程序来解决。通过可以将常用的域名和ip地址映射加入到hosts文件中，实现快速方便的访问。

**hosts文件格式**

` ip地址 主机名/域名 (主机别名)`

**主机名和域名的区别在于**：

- 主机名通常在局域网内使用，通过hosts文件，主机名就被解析到对应的ip。
- 域名通常在internet上使用，但是优先级低于hosts文件中内容，因此如果你不想使用internet上的域名解析，可以更改自己的hosts文件，加入自己的域名解析。

修改`www.baidu.com`的ip为127.0.0.1： 

```sh
$ cat /etc/hosts
127.0.0.1 localhost
127.0.0.1 www.baidu.com

$ ping www.baidu.com
64 bytes from localhost(127.0.0.1): icmp_seq=1 ttl=56 time=21.5 ms
```

### expect

expect是一个**自动化交互**套件，主要应用于执行命令和程序时，系统以交互形式要求输入指定字符串，实现交互通信。

expect自动交互流程：

spawn启动指定进程---expect获取指定关键字---send向指定程序发送指定字符---执行完成退出。

```sh
# 注意该脚本能够执行的前提是安装了expect
$ yum install -y expect
```

```
expect常用命令总结:
spawn               交互程序开始后面跟命令或者指定程序
expect              获取匹配信息匹配成功则执行expect后面的程序动作
send exp_send       用于发送指定的字符串信息
exp_continue        在expect中多次匹配就需要用到
send_user           用来打印输出 相当于shell中的echo
exit                退出expect脚本
eof                 expect执行结束 退出
set                 定义变量
puts                输出变量
set timeout         设置超时时间
$argv			   expect脚本可以接受从bash传递过来的参数.可以使用[lindex $argv n]获得，n从0开始，分别表示第一个,第二个,第三个....参数
interact		   允许用户交互；执行完成后保持交互状态，把控制权交给控制台，这个时候就可以手工操作了。如果没有这一句登录完成后会退出，而不是留在远程终端上。
```

#### except脚本

```sh
# 这一行告诉操作系统脚本里的代码使用那一个shell来执行，需要在脚本的第一行
#!/usr/bin/expect -f
# “set 自定义变量名”：设置超时时间，计时单位是：秒。timeout -1 为永不超时
set timeout 10
# spawn是进入expect环境后才可以执行的expect内部命令，主要的功能是给运行进程加个壳，用来传递交互指令。可以理解为启动一个新进程
spawn sudo su - root
# 从进程接收字符串，这里的expect是expect的一个内部命令。这个命令的意思是判断上次输出结果里是否包含“*password*”的字符串，如果有则立即返回，否则就等待一段时间后返回，这里等待时长就是前面设置的10秒； 
expect "*password*"
# send接收一个字符串参数"123456"，并将该参数发送到进程。这里就是执行交互动作，与手工输入密码的动作等效。 命令字符串结尾加上“\r”，表示“回车键”。 
send "123456\r"
expect "#*"	#进程返回#*时
send "ls\r"	#向进程输入ls\r
expect "#*"
send "df -Th\r"
# 单一分支模式
expect "hi" {send "You said hi"}
# 多分支模式，匹配到hi,hello,bye任意一个字符串时，执行相应的输出
expect {
    "hi" { send "You said hi\n"}
    "hello" { send "Hello yourself\n"}
    "bye" { send "That was unexpected\n"}
}
send "exit\r"
expect eof
```

#### shell调用expect 

```sh
#!/bin/bash
# 密码过期需要批量修改密码
for i in `cat /root/soft/ip.txt`
do
    /usr/bin/expect << EOF
    spawn /usr/bin/ssh root@$i
    expect {
    	"UNIX password" { send "Huawei@123\r" }
    }
    expect {
    	"New password:" { send "xxHuzzawexxi@1234#\r" }
    }
    expect {
    	"Retype new password:" { send "xxHuzzawexxi@1234#\r" }
    }
    expect "*]#"
    send "echo Huawei@123|passwd --stdin root\r"
    expect "*]#"
    send "exit\r"
    expect eof
EOF
done
```

[Linux expect 介绍和用法一](https://www.cnblogs.com/saneri/p/10819348.html)

[linux expect spawn的用法](https://www.cnblogs.com/bulh/articles/12779173.html)

### 用户

切换用户只执行一条命令的可以用: `su - oracle -c command`

切换用户执行一个shell文件可以用:`su - oracle -s /bin/bash shell.sh`

### awk

AWK 是一种处理文本文件的语言

```sh
# -F 指定输入文件折分隔符

$ cat log.txt
2 this, is a test
10 There are orange,apple,mongo

# 每行按空格或TAB分割，输出文本中的1、4项
$ awk '{print $1,$4}' log.txt
2 a
10 orange,apple,mongo

# 使用","分割
$ awk -F, '{print $1,$2}' log.txt
2 this  is a test
10 There are orange apple
```

**awk、sed、grep更适合的方向：**

- grep 更适合单纯的查找或匹配文本
- sed 更适合编辑匹配到的文本
- awk 更适合格式化文本，对文本进行较复杂格式处理

### su

su命令和su -命令最大的本质区别就是：

su是switch user 或set user id的一个缩写。这个命令让你开启一个子进程，成为新的用户 ID 和赋予你存取与这个用户ID 关联所有文件的存取权限；也就是说只是切换了用户身份，但Shell环境仍然是之前用户的Shell，工作目录仍然是之前用户的工作目录。su username是获得username的文件存取执行权限，不能获得环境变量。

su -是用户和Shell环境一起切换成新用户身份，切换了Shell环境不会出现PATH环境变量错误，可以用echo $PATH命令查看环境变量，工作目录变成新用户的工作目录了。su - username是获得username的文件存取执行权限和环境变量 。

su 后面不加用户是默认切到 root

## Shell

### 对文件名中包含空格的处理方法

Linux命令行工具和Shell带来了困扰，因为大多数命令中，都是默认以空格做为值与值之间的分隔符，而不是做为文件名的一部分。即环境变量IFS（the Internal Field Separator）为值为 空格、Tab、回车。

 修改Linux命令行工具和Shell的默认分隔符来解决

```sh
MY_SAVESIFS=$IFS
IFS=$'\n'

# 脚本内容

IFS=$MY_SAVESIFS
```



### 单括号和双括号

#### [  ] 单双括号

Ø  [ ] 两个符号左右都要有空格分隔

Ø  内部操作符与操作变量之间要有空格：如  [  “a”  =  “b”  ]

Ø  字符串比较中，> < 需要写成\> \< 进行转义

Ø  [ ] 中字符串或者${}变量尽量使用"" 双引号扩住，避免值未定义引用而出错的好办法

Ø  [ ] 中可以使用布尔运算符 –a –o 进行逻辑运算

Ø  [ ] 是bash 内置命令：[ is a shell builtinbash  [[  ]] 双方括号

#### [[  ]] 双方括号

Ø  [[ ]] 两个符号左右都要有空格分隔

Ø  内部操作符与操作变量之间要有空格：如  [[  “a” =  “b”  ]]

Ø  字符串比较中，可以直接使用 > < 无需转义

Ø  [[ ]] 中字符串或者   ${}变量未使用"" 双引号扩住    ，会进行模式和元字符匹配

```sh
$ [[ "ab"=a* ]] && echo "ok"
ok
```

Ø  [[] ] 内部可以使用逻辑运算符 &&  || 进行逻辑运算

Ø  [[ ]] 是bash  keyword：[[ is a shell keyword

[[ ]] 其他用法都和[ ] 一样

```sh
[  exp1  -a exp2  ] = [[  exp1 && exp2 ]] = [  exp1  ]&& [  exp2  ] = [[ exp1  ]] && [[  exp2 ]]

[  exp1  -o exp2  ] = [[  exp1 || exp2 ]] = [  exp1  ]|| [  exp2  ] = [[ exp1  ]] || [[  exp2 ]]
```

[BASH 中单括号和双括号](https://blog.csdn.net/hittata/article/details/8049665)



### 数组

```sh
for var in ${array[@]};do
    USERNAME="$var"
    if [[ ${USERNAME} != "pc" && ${USERNAME} != "pcm" && ${USERNAME} != "ckm" && ${USERNAME} != "portal" ]]; then
        continue
    fi
    if id -u ${USERNAME} >/dev/null 2>&1 ; then
        if [ -e /home/Software_temp/Software/${USERNAME}/ ]; then
            currentarray[$index]=${USERNAME};
            index=$(($index+1));
        fi
    fi
done
log_info "execute user: ${currentarray[@]}"
```

[Shell数组的增删改查](https://www.cnblogs.com/tangshengwei/p/5446315.html)

### 运算符

#### 算数运算符

```sh
==	相等。用于比较两个数字，相同则返回 true。
!=	不相等。用于比较两个数字，不相同则返回 true。
```

```sh
val=`expr 2 + 2`
val=`expr $b / $a`
1.表达式和运算符之间要有空格，例如 2+2 是不对的，必须写成 2 + 2
2.完整的表达式要被``包含，注意使用的是反引号 `` 而不是单引号 ''
3.expr 是一款表达式计算工具
```

```sh
1.使用let命令 
等号右边以及运算符和括号的两边都不能有空格
let "sum=3+5"
let "sum=3*5"
let "sum=(-6-9)*5"

2.使用expr命令  
乘号(*), 左括号( , 右括号)必须使用反斜杠(\)转义。expr右边以及运算符和括号的两边必须有空格; 
val=`expr $a + $b`
val=`expr $a - $b`
val=`expr $a \* $b` # 乘号*前边必须加反斜杠\才能实现乘法运算；
val=`expr \( $b - $a \) \* $c`


3.使用(( ... )) 的形式  
无需对运算符和括号做转义处理，也可以采用松散或紧凑的格式
sum=$((3+5))
sum=$(( 3 * 5 ))
```



#### 关系运算符

关系运算符只支持数字，不支持字符串，除非字符串的值是数字。 

```sh
-eq -ne -gt -lt -ge -le
```

#### 布尔运算符

```sh
!	非运算，表达式为 true 则返回 false，否则返回 true。	[ ! false ] 返回 true。
-o	或运算，有一个表达式为 true 则返回 true。	[ $a -lt 20 -o $b -gt 100 ] 返回 true。
-a	与运算，两个表达式都为 true 才返回 true。	[ $a -lt 20 -a $b -gt 100 ] 返回 false。
```

#### 逻辑运算符

**&&**：

```sh
command1  && command2
1.命令之间使用 && 连接，实现逻辑与的功能。
2.只有在 && 左边的命令返回真（命令返回值 $? == 0），&& 右边的命令才会被执行；只要有一个命令返回假（命令返回值 $? == 1），后面的命令就不会被执行。
```

**||**：

```sh
command1 || command2
1.命令之间使用 || 连接，实现逻辑或的功能。
2.只有在 || 左边的命令返回假（命令返回值 $? == 1），|| 右边的命令才会被执行；只要有一个命令返回真（命令返回值 $? == 0），后面的命令就不会被执行。
```

```sh
[[ $a -lt 100 && $b -gt 100 ]] 返回 false
[[ $a -lt 100 || $b -gt 100 ]] 返回 true
```

#### 字符串运算符

```sh
=	检测两个字符串是否相等，相等返回 true。	[ $a = $b ] 返回 false。
!=	检测两个字符串是否不相等，不相等返回 true。	[ $a != $b ] 返回 true。
-z	检测字符串长度是否为0，为0返回 true。	[ -z $a ] 返回 false。
-n	检测字符串长度是否不为 0，不为 0 返回 true。	[ -n "$a" ] 返回 true。
$	检测字符串是否不为空，不为空返回 true。	[ $a ] 返回 true。
```

#### 文件运算符

```sh
-b file	检测文件是否是块设备文件，如果是，则返回 true。	[ -b $file ] 返回 false。
-c file	检测文件是否是字符设备文件，如果是，则返回 true。	[ -c $file ] 返回 false。
-d file	检测文件是否是目录，如果是，则返回 true。	[ -d $file ] 返回 false。
-f file	检测文件是否是普通文件（既不是目录，也不是设备文件），如果是，则返回 true。	[ -f $file ] 返回 true。
-g file	检测文件是否设置了 SGID 位，如果是，则返回 true。	[ -g $file ] 返回 false。
-k file	检测文件是否设置了粘着位(Sticky Bit)，如果是，则返回 true。	[ -k $file ] 返回 false。
-p file	检测文件是否是有名管道，如果是，则返回 true。	[ -p $file ] 返回 false。
-u file	检测文件是否设置了 SUID 位，如果是，则返回 true。	[ -u $file ] 返回 false。
-r file	检测文件是否可读，如果是，则返回 true。	[ -r $file ] 返回 true。
-w file	检测文件是否可写，如果是，则返回 true。	[ -w $file ] 返回 true。
-x file	检测文件是否可执行，如果是，则返回 true。	[ -x $file ] 返回 true。
-s file	检测文件是否为空（文件大小是否大于0），不为空返回 true。	[ -s $file ] 返回 true。
-e file	检测文件（包括目录）是否存在，如果是，则返回 true。	[ -e $file ] 返回 true。
```



**|**：

```sh
command 1 | command 2 
管道符号，把第一个命令command 1执行的结果作为command2的输入传给command 2
```

**()**：

```sh
(command1;command2;command3....)               多个命令之间用;分隔
1.把几个命令合在一起执行，命令之间使用命令分隔符（;）分隔。
2.使用()括起来的命令在执行前面都不会切换当前工作目录，也就是说命令组合都是在当前工作目录下被执行的，尽管命令中有切换目录的命令。
```

[shell 中| && || () {} 用法以及shell的逻辑与或非](https://www.cnblogs.com/aaronLinux/p/8340281.html)

### 重定向

在shell脚本中，默认情况下，总是有三个文件处于打开状态，标准输入(键盘输入)、标准输出（输出到屏幕）、标准错误（也是输出到屏幕），它们分别对应的文件描述符是0，1，2 。输出重定向到相应的文件中，而不再屏幕显示。

```
> 默认为标准输出重定向，与 1> 相同
2>&1  意思是把 标准错误输出 重定向到 标准输出.

&>file  意思是把标准输出 和 标准错误输出 都重定向到文件file中
```

```sh
$ nohup java -jar app.jar >log 2>&1 &
本来1----->屏幕 （1指向屏幕）
执行>log后， 1----->log (1指向log)
执行2>&1后， 2----->1 (2指向1，而1指向log,因此2也指向了log
所以>log 2>&1顺序不能错误
```

### 当前时间

```sh
time=$(date "+%Y-%m-%d %H:%M:%S")
echo "${time}"
```



### 跳出循环 

continue、break、continue、return、exit

**break**：跳出当前一层循环	**break n**：跳出n层循环

**continue**：跳出一层本次循环	**continue n**：跳出n层本次循环，其效果相当于内层循环和外层循环同时执行了不带 n 的 continue 

**exit**：

exit 是一个 Shell 内置命令，用来退出当前 Shell 进程，并返回一个退出状态；使用$?可以接收这个退出状态；

exit 命令可以接受一个整数值作为参数，代表退出状态。如果不指定，默认状态值是 0。

一般情况下，退出状态为 0 表示成功，退出状态为非 0 表示执行失败（出错）了。

exit 退出状态只能是一个介于 0~255 之间的整数，其中只有 0 表示成功，其它值都表示失败。

**return**：表示函数返回值，只能用在函数里，使得shell函数退出并返回数值，返回值为你指定的参数n的值，如果没有指定 n 的值，则默认为函数最后一条命令执行的返回状态。 

[跳出循环](https://blog.csdn.net/focus_lyh/article/details/112319193)

```sh
# 判断一个用户是否存在
USERNAME=tsit
if id -u ${USERNAME} >/dev/null 2>&1 ; then
    echo "User ${USERNAME} exists."
else
    echo "User ${USERNAME} does not exist!"
fi
```

### temp

```sh
# 等号左右两边不能有空格，否则把变量作为命令匹配
time=`date "+%Y%m%d%H%M%S"`
pid=`ps -ef | grep C_CMTJ_UnifiedManage.jar | grep java | awk '{print $2}'`

if [ `grep -c admin_username BOOT-INF/classes/properties/iop.properties` -eq 0 ]; then
    sed -i '$a #查询用户ID接口账号密码' BOOT-INF/classes/properties/iop.properties
    sed -i '$a admin_username = sysadmin' BOOT-INF/classes/properties/iop.properties
    sed -i '$a admin_password = 4d3ea2cf' BOOT-INF/classes/properties/iop.properties
fi


if [ -n ${PAAS_TYPE} -a "${PAAS_TYPE}" = "FusionStage" ];then
	...
fi

SCRIPT_DIR=`dirname $SCRIPT`
SCRIPT_DIR=`cd $SCRIPT_DIR && pwd`

typeset PARSE_JSON="sh $SCRIPT_DIR/tools/JSON.sh -b"
typeset global_config="${SCRIPT_DIR}/globalConfigurations.json"

key_size=`${PARSE_JSON} < ${global_config} | grep "key" | uniq | wc -l`
for((k=0;k<${key_size};k++))
do
	...
done



su - pc -c "cd 7897899"
if [ $? -ne 0 ]; then
    ...
    exit 1
fi


if [ $# -ne 4 ] && [ $# -ne 2 ]; then
    ...
    exit 1
fi

if [ $? -ne 0 ] || [ "X" = "X${json_string}" ];then
    ...
    exit 1
else
    ...
fi


if [ "X${base_version:0:8}" = "XV300R001" -o "X${base_version:0:8}" = "XV300R002" -o "X${base_version:0:8}" = "XV300R003" -o "X${base_version:0:11}" = "XV300R005C01" ];then
	...
fi


if [ -f "${SCRIPT_DIR}/install_itpaas_info.ini" ];then
	...
elif [ -f "..." ];then
	...
else
	...
fi



while [ -h "$SCRIPT" ] ; do
	...
done





if [ -e /home/uap -a -e /home/paas ]; then
    echo "123"
else
    echo "fail"
fi
if [[ -e /home/uap && -e /home/paas ]]; then
    echo "456"
fi







if [[ -e /home/uap/dsp_backup && -n "$(cd /home/uap/dsp_backup;ls -l |tac |grep backup_|awk '{print $9}'|head -1)" ]];then
        echo "1"
else
        echo "2"
fi

if [[ -e /home/uap/dsp_backup && $(cd /home/uap/dsp_backup;echo $?) -eq 0 ]];then
        echo "3"
else
        echo "4"
fi

USERNAME=pc
if [[ -e /home/uap/dsp_backup ]];then
        cd /home/uap/dsp_backup
        backup_path=$(ls -l |tac |grep backup_|awk '{print $9}'|head -1)
        echo $backup_path
        if [[ -n $backup_path && -e /home/uap/dsp_backup/$backup_path/$USERNAME ]];then
                currentarray[$index]=${USERNAME};
                index=$(($index+1));
        fi
echo ${currentarray[@]}
fi


function log_temp(){
	echo "$(date "+%Y-%m-%d %H:%M:%S") "$1 >>/opt/varlib/kubelet/pods/uninstall.log 2>&1
}








SCRIPT_DIR=$(dirname $SCRIPT)
SCRIPT_DIR=$(cd $SCRIPT_DIR && pwd)

# 遍历备份文件列表中的所有行，文件格式必须是unix
while read filePath
do
# 将每一行文件路径拼接在空格之后存放在一个变量中
    backup_content="${backup_content} ${filePath}"
done < ${SCRIPT_DIR}/backuplist.properties
```

