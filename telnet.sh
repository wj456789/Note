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