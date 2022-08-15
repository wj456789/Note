# Shell

自定义的脚本建议放到/usr/local/sbin/目录下

Shell脚本通常都是以.sh 为后缀名的
第一行一定是 “#! /bin/bash” 它代表的意思是，该文件使用的是bash语法。#表示注释，Shell脚本的执行很简单，直接”sh filename “ 即可

```sh
# %Y表示年，%m表示月，%d表示日期，%H表示小时，%M表示分钟，%S表示秒，%w表示星期
$ date "+%Y/%m/%d %H:%M:%S %w"
2021/02/19 10:50:45 5

$ date "+%y/%m/%d %H:%M:%S"
21/02/19 10:50:45
```



## 变量

### 变量操作

```sh
# 用反引号，将shell命令引起来，可以将命令的输出值赋给变量，$()也可以输出命令的结果值
d = `date +%y/%m/%d %H:%M:%S`
echo "date is $d"

输出：
date is 21/02/19 10:50:45
```

```sh
# 数学计算要用[ ]括起来并且外头要带一个$，也可以sum=$[a+b]
a=1
b=2
sum=$[$a+$b]		
echo "sum is $sum"

输出：
sum is 3
```

```sh
# read x表示x变量的值需要用户通过键盘输入得到。
echo "please input a number"
read x

# read -p相当于echo+read
read -p "please input another number" y
echo "number is $x and another is $y"

输出：
number is 7 and another is 56
```

```sh
echo "############## start test ##############"
sum =$[$1+$2]
echo $sum

# sh -x filename加上-x选项可以查看整个执行过程
$ sh -x test.sh 11 23
- echo '############## start test ##############'
###### ######## start test
- sum=34
- echo 34
  34
```

### 特殊变量

- `$$`  Shell本身的PID（ProcessID）  
- `$!`  Shell最后运行的后台Process的PID  
- `$?`  最后运行的命令的结束代码（返回值）  
- `$-`  使用Set命令设定的Flag一览  
- `$*`  所有参数列表。如"$*"用「"」括起来的情况、以"$1 $2 … $n"的形式输出所有参数。 
- `$@`  所有参数列表。如"$@"用「"」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。  
- `$#`  添加到Shell的参数个数  
- `$0`  Shell本身的文件名  
- `$1～$n`  添加到Shell的各参数值。$1是第1参数、$2是第2参数…。  

```sh
1 #!/bin/bash
2 #
3 printf "The complete list is %s\n" "$$"
4 printf "The complete list is %s\n" "$!"
5 printf "The complete list is %s\n" "$?"
6 printf "The complete list is %s\n" "$*"
7 printf "The complete list is %s\n" "$@"
8 printf "The complete list is %s\n" "$#"
9 printf "The complete list is %s\n" "$0"
10 printf "The complete list is %s\n" "$1"
11 printf "The complete list is %s\n" "$2
结果：
$ bash params.sh 123456 QQ
The complete list is 24249
The complete list is
The complete list is 0
The complete list is 123456 QQ
The complete list is 123456
The complete list is QQ
The complete list is 2
The complete list is params.sh
The complete list is 123456
The complete list is QQ
```

#### $?

程序执行正确或者执行结果为空，exit 0,$?为0
程序执行错误或执行结果不为空，exit 1,$?为1

```sh
grep "/openportal" /etc/rc.d/rc.local > /dev/null
if [ $? -eq 0 ]; then
    echo "Found!Already add startup service，rc.local文件中存在/openportal"
else
    echo "Not Found!rc.local文件中不存在/openportal"
    echo "source /etc/profile" >>/etc/rc.d/rc.local
    echo "/usr/openportal/bin/startup.sh" >>/etc/rc.d/rc.local
    echo '##############Add startup service is OK##############'
fi
```

#### cd dirname $0

返回这个脚本文件放置的目录，并可以根据这个目录来定位所要运行程序的相对位置（绝对位置除外） 

> 脚本文件/test/build.sh

```sh
#!/bin/bash
echo $0
echo $(dirname $0)
echo $(cd $(dirname $0) && pwd)
```

```sh
$ sh build.sh
build.sh
.
/test
```

## 

## 逻辑判断

### if

#### 模板

```sh
1)
if 判断语句; then
	command
fi

2)
if 判断语句 ; then
	command
else
	command
fi

3)
if 判断语句一 ; then
	command
elif 判断语句二; then
	command
else
	command
fi
```

#### 示例

```sh
!/bin/bash
echo '############## start test ##############'
read -p "please input a number:" n
if((n<60));then
    echo "$n<60"
elif((n>=60))&&((n<100));then
# 或 elif((n>=60&&n<100));then
    echo "60=<$n<100"
else 
    echo "other"
fi

执行结果：
$ sh test.sh
###### ######## start test
please input a number:78
60=<78<100
```

判断数值大小除了可以用”(( ))”的形式外，还可以使用”[ ]”。但是使用[ ]必须使用 -lt （小于），-gt （大于），-le （小于等于），-ge （大于等于），-eq （等于），-ne （不等于）符号。

```sh
!/bin/bash
echo '############## start test ##############'
read -p "please input a number:" n
if [ $n -lt 60 ];then
    echo "$n<60"
# 注意if后面空格，括号前后空格，$符号，不能写成这种 [ $n -ge 60 && $n -lt 100 ]
elif [ $n -ge 60 ] && [ $n -lt 100 ];then
    echo "60=<$n<100"
else 
    echo "other"
fi
```

使用[ ]也可以使用如下符号进行判断。

- `if [ -n str1 ]`	当串的长度大于0时为真(串非空)  
- `if [ -z str1 ]`   当串的长度为0时为真(空串)  

- -e ：判断文件或目录是否存在
- -d ：判断是不是目录，并是否存在
- -f ：判断是否是普通文件，并存在
- -r ：判断文档是否有读权限
- -w ：判断是否有写权限
- -x ：判断是否可执行

```sh
if [ -d /home ];then
    echo "ok"
fi
```

```sh
ARGS=$*
if [ -n "$ARGS"  ]
then
	print "with argument"
fi
	print " without argument"
```

### case

#### 模板

```sh
case 变量 in
value1)
	command
	;;
value2)
	command
	;;
value3)
	command
	;;
*)
	command
	;;
esac
```

#### 示例

```sh
!/bin/bash
echo '############## start test ##############'
read -p "input a number:" n
case $n in
1)
  echo "n=1"
  ;;
5)
  echo "n=5"
  ;;
*)
  echo "other"
  ;;
esac
```



## 逻辑循环

### for

```sh
for 变量名 in 循环的条件； do
	command
done
```

--脚本

```sh
for i in `seq 1 5`;do
    echo "$i"
done

执行结果
$ sh test.sh
1
2
3
4
5
```

### while

```sh
while 条件; do
	command
done
```

```sh
a=10
while [ $a -ge 1 ];do
    echo "$a"
    a=$[$a-1]
done

执行结果：
10
9
8
7
6
5
4
3
2
1
```



## 函数

--脚本
```sh
function summer(){
  # $1 $2对应下方summer传入的值
  sum=$[$1+$2]
  echo "$sum"
}
# $1 $2对应命令输入的值，函数调用要写在函数之后
summer $1 $2

执行：
$ sh test.sh 45 56
101
```

参考：
[linux 的基本操作（编写shell 脚本）](https://www.cnblogs.com/zhang-jun-jie/p/9266858.html)




## 定时脚本

--定时服务
linux、ubuntu一般默认安装了crontab，是开机自启动的。 cron服务会每分钟检查一次/etc/crontab、/etc/cron.d/、/var/spool/cron文件下的变更。如果发现变化，就会下载到存储器中。因此，即使crontab文件改变了，程序也不需要重新启动

service crond reload //重新载入配置
service crond status //查看定时服务状态

systemctl status crond.service	//查看定时服务状态

--需要定时的任务脚本

cat mysqlbk.sh

```sh
!/bin/bash
number=7
backup_dir=/usr/local/mysql/backup
time=`date "+%Y-%m-%d %H:%M:%S"`
echo "#############################"
echo "start mysql backup file in $time"
touch $backup_dir/iot_zhdg$(date +%Y-%m-%d).sql
mysqldump -uroot -pZhdgbiud@#2020 iot_zhdg >/usr/local/mysql/backup/iot_zhdg$(date +%Y-%m-%d).sql
# sql文件计数
count=ls -l -crt $backup_dir/*.sql | awk '{print $9}' |wc -l
echo "count is $count"
if test $count -gt $number
then
  delcount=$[$count-$number]
  # 找出需要删除的文件
  delfile=ls -l -crt $backup_dir/*.sql |awk '{print $9}' |head -$delcount
  rm -f $delfile
  echo "delete $delfile"
fi
echo 'mysql backup over'
```





--新增调度任务
1)、在命令行输入: crontab -e 然后添加相应的任务，wq!存盘退出。 
2)、直接编辑/etc/crontab 文件，即vi /etc/crontab，添加相应的任务。 (系统任务用crontab无法查看，只能查看日志)
编辑/etc/crontab是针对系统的任务。而crontab -e配置是针对某个用户的 ，存盘退出后每个用户定义的crontab存储在目录/var/spool/cron下，文件会保存在成如下文件/var/spool/cron/username，文件名会根据用户名而不同
3)、系统通过调用 run-parts 命令，定时运行四个目录下的所有脚本。
/etc/cron.hourly，目录下的脚本会每个小时让执行一次，在每小时的17分钟时运行；
/etc/cron.daily，目录下的脚本会每天让执行一次，在每天的6点25分时运行；
/etc/cron.weekly，目录下的脚本会每周让执行一次，在每周第七天的6点47分时运行；
/etc/cron.mouthly，目录下的脚本会每月让执行一次，在每月1号的6点52分时运行；

--查看调度任务 
crontab -l 	//列出当前用户的所有调度任务
crontab -l -u root 	//列出用户root的所有调度任务

--删除任务调度工作 
crontab -r 	//删除所有任务调度工作 

--任务命令写法
如：
用户任务0 0 * * * /bin/bash /usr/local/sbin/mysqlbk.sh >> /usr/local/sbin/mysqlbk.log 2>>/usr/local/sbin/mysqlbk.log

系统任务0 0 * * * root sh /usr/local/sbin/mysqlbk.sh >> /usr/local/sbin/mysqlbk.log 2>>/usr/local/sbin/mysqlbk.log
 minute hour day month dayofweek  command

minute - 从0到59的整数
hour - 从0到23的整数
day - 从1到31的整数 (必须是指定月份的有效日期)
month - 从1到12的整数 (或如Jan或Feb简写的月份)
dayofweek - 从0到7的整数，0或7用来描述周日 (或用Sun或Mon简写来表示)

*	表示所有可用的值。例如*在指代month时表示每月执行(需要符合其他限制条件)该命令。
		表示整数列，例如1-4意思是整数1,2,3,4
		指定数值由逗号分开。如：3,4,6,8表示这四个指定整数。
		指定步进设置。“/<interger>”表示步进值。如0-59/2定义每两分钟执行一次；*/3用来定义每三个月份运行一次。



--command解析 - 需要执行的命令
root表示以root用户身份来运行。

root后面可加run-parts表示后面跟着的是一个文件夹，要执行的是该文件夹下的所有脚本

参考：
[linux定时执行shell脚本](https://blog.csdn.net/qq_39131177/article/details/79051711)

[Linux安装配置mysql，并实现数据定时备份](https://blog.csdn.net/ToYouMake/article/details/106411335)


## Other

### command

[shell之用command在终端判断是否存在这个命令](https://blog.csdn.net/u011068702/article/details/80787824)





## telnet.sh

```sh
#!bin/bash
dir1=/etc/xinetd.d/telnet
dir2=/etc/securetty
echo "telnet start----------------------------------" 
yum install xinetd telnet-server -y
if [ -f "$dir1" ];then
  echo "$dir1----------------------------------" 
  cat $dir1
  sed -i 's/disable = no/disable = yes/g' $dir1
else
  echo "$dir1 not exist" 
fi
grep "pts\/0" $dir2
if [ $? -eq 0 ]; then
    echo "find pts/0"
else
    sed -i '$a pts\/0' $dir2
fi
grep "pts\/1" $dir2
if [ $? -eq 0 ]; then
    echo "find pts/1"
else
    sed -i '$a pts\/1' $dir2
fi
service telnet.socket restart
service xinetd restart
netstat -lntp|grep 23
echo "telnet end----------------------------------" 
```

