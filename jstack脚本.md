# jstack脚本

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

















