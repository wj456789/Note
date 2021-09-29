# Redis

## Redis简介(了解)

### 关于NoSQL

NoSQL的全称是Not only SQL，在过去的几年中，NoSQL数据库一度成为高并发、海量数据存储解决方案的代名词。

- **BerkeleyDB**是一种极为流行的开源嵌入式数据库，在更多情况下可用于存储引擎，比如BerkeleyDB在被Oracle收购之前曾作为MySQL的存储引擎，由此可以预见，该产品拥有极好的并发伸缩性，支持事务及嵌套事务，海量数据存储等重要特征，在用于存储实时数据方面具有极高的可用价值。然而需要指出的是，该产品的Licence为GPL，这就意味着它并不是在所有情况下都是免费使用的。
- 对**MongoDB**的定义为Oriented­Document数据库服务器，和BerkeleyDB不同的是该数据库可以像其他关系型数据库服务器那样独立的运行并提供相关的数据服务。从该产品的官方文档中我们可以获悉，MongoDB主要适用于高并发的论坛或博客网站，这些网站具有的主要特征是并发访问量高、多读少写、数据量大、逻辑关系简单，以及文档数据作为主要数据源等。和BerkeleyDB一样，该产品的License同为GPL。
- **Redis**，典型的NoSQL数据库服务器，和BerkeleyDB相比，它可以作为服务程序独立运行于自己的服务器主机。在很多时候，人们只是将Redis视为Key/Value数据库服务器，然而事实并非如此，在目前的版本中，Redis除了Key/Value之外还支持List、Set、Hash和Ordered Set等数据结构，因此它的用途也更为宽泛。和以上两种产品不同的是，Redis的License是Apache License，就目前而言，它是完全免费。
- **memcached**，数据缓存服务器。它们之间的最大区别，memcached只是提供了数据缓存服务，一旦服务器宕机，之前在内存中缓存的数据也将全部消失，因此可以看出memcached没有提供任何形式的数据持久化功能，而Redis则提供了这样的功能。再有就是Redis提供了更为丰富的数据存储结构，如Hash和Set。至于它们的相同点，主要有两个，一是完全免费，再有就是它们的提供的命令形式极为接近。  

### Redis的优势

- 和其他NoSQL产品相比，Redis的易用性极高，因此对于那些有类似产品使用经验的开发者来说，一两天，甚至是几个小时之后就可以利用Redis来搭建自己的平台了。
- 在解决了很多通用性问题的同时，也为一些个性化问题提供了相关的解决方案，如索引引擎、统计排名、消息队列服务等。  

### 和关系型数据库的比较

在目前版本的Redis中，提供了对五种不同数据类型的支持，其中只有String类型可以被视为Key-­Value结构，而其他的数据类型均有适用于各自特征的应用场景。

相比于关系型数据库，由于其存储结构相对简单，因此Redis并不能对复杂的逻辑关系提供很好的支持，然而在适用于Redis的场景中，我们却可以由此而获得效率上的显著提升。即便如此，Redis还是为我们提供了一些数据库应该具有的基础概念，如：在同一连接中可以选择打开不同的数据库，然而不同的是，Redis中的数据库是通过数字来进行命名的，缺省情况下打开的数据库为0。如果程序在运行过程中打算切换数据库，可以使用Redis的select命令来打开其他数据库，如select 1，如果此后还想再切换回缺省数据库，只需执行select 0即可。  

在数据存储方面，Redis遵循了现有NoSQL数据库的主流思想，即Key作为数据检索的唯一标识，我们可以将其简单的理解为关系型数据库中索引的键，而Value则作为数据存储的主要对象，其中每一个Value都有一个Key与之关联，这就好比索引中物理数据在数据表中存储的位置。在Redis中，Value将被视为二进制字节流用于存储任何格式的数据，如Json、XML和序列化对象的字节流等，因此我们也可以将其想象为关系型数据库中的BLOB类型字段。由此可见，在进行数据查询时，我们只能基于Key作为我们查询的条件，当然我们也可以应用Redis中提供的一些技巧将Value作为其他数据的Key。  

### 如何持久化内存数据  

缺省情况下，Redis会参照当前数据库中数据被修改的数量，在达到一定的阈值后会将数据库的快照存储到磁盘上，这一点我们可以通过配置文件来设定该阈值。通常情况下，我们也可以将Redis设定为定时保存。如当有1000个以上的键数据被修改时，Redis将每隔60秒进行一次数据持久化操作。缺省设置为，如果有9个或9个以下数据修改时，Redis将每15分钟持久化一次。

从上面提到的方案中可以看出，如果采用该方式，Redis的运行效率将会是非常高效的，每当有新的数据修改发生时，仅仅是内存中的缓存数据发生改变，而这样的改变并不会被立即持久化到磁盘上，从而在绝大多数的修改操作中避免了磁盘IO的发生。然而事情往往是存在其两面性的，在该方法中我们确实得到了效率上的提升，但是却失去了数据可靠性。如果在内存快照被持久化到磁盘之前，Redis所在的服务器出现宕机，那么这些未写入到磁盘的已修改数据都将丢失。为了保证数据的高可靠性，Redis还提供了另外一种数据持久化机制­­Append模式。如果Redis服务器被配置为该方式，那么每当有数据修改发生时，都会被立即持久化到磁盘。  

## 安装Redis  

### 安装  

**步骤：**

1. 解压redis­3.2.8.tar.gz  

   ```sh
   cd ~/software
   tar -zxf redis-3.2.8.tar.gz
   ```

2. 编译  

   ```sh
   cd redis-3.2.8
   make
   ```

3. 安装  

   ```sh
   mkdir ~/software/redis-bin
   make install PREFIX=~/software/redis-bin/ #PREFIX选项用来指定安装的位置
   ```

4. 启动redis  

   ```sh
   cd ~/software/redis-bin/bin/
   ./redis-server #使用默认配置文件启动，默认配置文件所在目录redis-3.2.8/redis.conf
   或 
   cp ~/software/redis-3.2.8/redis.conf myredis.conf #复制默认配置文件到当前目录，并改名
   ./redis-server myredis.conf #使用指定的配置文件启动
   ```

   补充：可以将~/software/redis­bin/bin/添加到PATH变量中，便于执行命令  

   ```sh
   vi ~/.bashrc
   	export PATH=$PATH:/Users/wangbo/software/redis-bin/bin
   	
   source ~/.bashrc
   ```

5. 连接redis  

   ```sh
   ./redis-cli #默认连接本机的6379端口(redis默认使用的端口号)
   或 
   ./redis-cli -h IP地址 -p 端口号 #连接指定主机、指定端口的redis，如./redis-cli -h localhost -p 6379
   ```

### 关闭  

两种方式

- 方式1：在服务器窗口中按 Ctrl+C
- 方式2：在客户端连接后输入 shutdown 或 直接输入 redis-­cli shutdown

查看redis进程  

```sh
ps aux | grep redis #查看redis的进程信息
或 
lsof -i:6379 #查看6379端口的进程信息
```

### 配置

编辑配置文件：  

```sh
$ vi myredis.conf
    daemonize yes #配置为守护进程，后台启动
    
    port 6379 #修改监听端口
    
    #让redis支持远程访问，默认只允许本地访问
    #bind 127.0.0.1 #注释掉该行，允许所有主机访问redis
    
    protected-mode no #关闭保护模式
    
    requirepass itany #配置redis密码，使用时需要输入:auth itany进行认证，认证后才能操作redis
```

## Redis数据类型

### 简介

Redis数据就是以key­-value形式来存储的，key只能是字符串类型，value可以是以下五种类型：String、List、Set、Sorted­-Sets、Hash。

### String类型  

#### 简介

字符串类型是Redis中最为基础的数据存储类型，它在Redis中是二进制安全的，这便意味着该类型可以接受任何格式的数据，如JPEG图像数据或Json对象描述信息等。在Redis中字符串类型的Value最多可以容纳的数据长度是512M。  

#### 操作  

- `set/get/append/strlen`  

  ```sh
  $ redis-cli
  
  127.0.0.1:6379> select 0 #切换到第1个数据库，默认共有16个数据库，索引从0开始
  OK
  
  127.0.0.1:6379> keys * #显示所有的键key
  (empty list or set)
  
  127.0.0.1:6379> set name tom #设置键
  OK
  
  127.0.0.1:6379> get name #获取键对应的值
  "tom"
  
  127.0.0.1:6379> exists mykey #判断该键是否存在，存在返回1，不存在返回0
  (integer) 0
  
  127.0.0.1:6379> append mykey "hello" #如果该键不存在，则创建，返回当前value的长度
  (integer) 5
  
  127.0.0.1:6379> append mykey " world" #如果该键已经存在，则追加，返回追加后value的长度
  (integer) 11
  
  127.0.0.1:6379> get mykey #获取mykey的值
  "hello world"
  
  127.0.0.1:6379> strlen mykey #获取mykey的长度
  (integer) 11
  
  #EX和PX表示失效时间，单位为秒和毫秒，两者不能同时使用；NX表示数据库中不存在时才能设置,XX表示存在时才能设置
  127.0.0.1:6379> set mykey "this is test" EX 5 NX
  OK
  
  127.0.0.1:6379> get mykey
  "this is test"
  ```

  注：命令不区分大小写，但key和value区分大小写  

- `incr/decr/incrby/decrby  `

  ```sh
  127.0.0.1:6379> flushdb #清空数据库
  OK
  
  127.0.0.1:6379> set mykey 20
  OK
  
  127.0.0.1:6379> incr mykey #递增1
  (integer) 21
  
  127.0.0.1:6379> decr mykey #递减1
  (integer) 20
  
  127.0.0.1:6379> del mykey #删除该键
  (integer) 1
  
  127.0.0.1:6379> decr mykey
  (integer) -1
  
  127.0.0.1:6379> del mykey
  (integer) 1
  
  127.0.0.1:6379> INCR mykey
  (integer) 1
  
  127.0.0.1:6379> set mykey 'hello' #将该键的Value设置为不能转换为整型的普通字符串
  OK
  
  127.0.0.1:6379> incr mykey #在该键上再次执行递增操作时，Redis将报告错误信息
  (error) ERR value is not an integer or out of range
  
  127.0.0.1:6379> set mykey 10
  OK
  
  127.0.0.1:6379> incrby mykey 5 #递增5，即步长
  (integer) 15
  
  127.0.0.1:6379> decrby mykey 10 #递减10
  (integer) 5
  ```

- `getset/setex/setnx  `

  ```sh
  # getset 获取的同时并设置新的值
  127.0.0.1:6379> incr mycount #将计数器的值原子性的递增1
  (integer) 1
  
  127.0.0.1:6379> getset mycount 666 #在获取计数器原有值的同时，并将其设置为新值
  "1"
  
  127.0.0.1:6379> get mycount
  "666"
  
  # setex 设置过期时间
  127.0.0.1:6379> setex mykey 10 "hello" #设置指定Key的过期时间为10秒，等同于set mykey hello ex 10
  OK
  
  127.0.0.1:6379> ttl mykey #查看指定Key的过期时间(秒数)
  (integer) 8
  
  # setnx 当key不存在时才能设置
  127.0.0.1:6379> del mykey
  (integer) 0
  
  127.0.0.1:6379> setnx mykey "aaa" #key不存在，可以设置，等同于set mykey aaa nx
  (integer) 1
  
  127.0.0.1:6379> setnx mykey "bbb" #key存在，不能设置
  (integer) 0
  
  127.0.0.1:6379> get mykey
  "aaa"
  ```

- `setrange/getrange` 设置/获取指定索引位置的字符

  ```sh
  127.0.0.1:6379> set mykey "hello world"
  OK
  
  127.0.0.1:6379> get mykey
  "hello world"
  
  127.0.0.1:6379> setrange mykey 6 dd #从索引为6的位置开始替换(索引从0开始)
  (integer) 11
  
  127.0.0.1:6379> get mykey
  "hello ddrld"
  
  127.0.0.1:6379> setrange mykey 20 dd #超过的长度使用0代替
  (integer) 22
  
  127.0.0.1:6379> get mykey
  "hello ddrld\x00\x00\x00\x00\x00\x00\x00\x00\x00dd"
  
  127.0.0.1:6379> getrange mykey 3 12 #获取索引为[3,12]之间的内容
  "lo ddrld\x00\x00"
  ```

- `setbit/getbit` 设置/获取指定位的BIT值，应用场景：考勤打卡

  ```sh
  127.0.0.1:6379> del mykey
  (integer) 1
  
  127.0.0.1:6379> setbit mykey 7 1 #设置从0开始计算的第七位BIT值为1，返回原有BIT值0
  (integer) 0
  
  127.0.0.1:6379> get mykey #获取设置的结果，二进制的0000 0001的十六进制值为0x01
  "\x01"
  
  127.0.0.1:6379> setbit mykey 6 1 #设置从0开始计算的第六位BIT值为1，返回原有BIT值0
  (integer) 0
  
  127.0.0.1:6379> get mykey #获取设置的结果，二进制的0000 0011的十六进制值为0x03
  "\x03"
  
  127.0.0.1:6379> getbit mykey 6 #返回了指定Offset的BIT值
  (integer) 1
  
  127.0.0.1:6379> getbit mykey 10 #如果offset已经超出了value的长度，则返回0
  (integer) 0
  ```

- `mset/mget/msetnx`

  ```sh
  127.0.0.1:6379> mset key1 "hello" key2 "world" #批量设置了key1和key2两个键。
  OK
  
  127.0.0.1:6379> mget key1 key2 #批量获取了key1和key2两个键的值。
  1) "hello"
  2) "world"
  
  #批量设置了key3和key4两个键，因为之前他们并不存在，所以该命令执行成功并返回1
  127.0.0.1:6379> msetnx key3 "itany" key4 "liu"
  (integer) 1
  
  127.0.0.1:6379> mget key3 key4
  1) "itany"
  2) "liu"
  
  #批量设置了key3和key5两个键，但是key3已经存在，所以该命令执行失败并返回0
  127.0.0.1:6379> msetnx key3 "hello" key5 "world"
  (integer) 0
  
  #批量获取key3和key5，由于key5没有设置成功，所以返回nil
  127.0.0.1:6379> mget key3 key5
  1) "itany"
  2) (nil)
  ```

### List类型

#### 概述

在Redis中，List类型是按照插入顺序排序的字符串链表。和数据结构中的普通链表一样，我们可以在其头部(left)和尾部(right)添加新的元素。在插入时，如果该键并不存在，Redis将为该键创建一个新的链表。与此相反，如果链表中所有的元素均被移除，那么该键也将会被从数据库中删除。List中可以包含的最大元素数量是
4294967295。

从元素插入和删除的效率视角来看，如果我们是在链表的两头插入或删除元素，这将会是非常高效的操作，即使链表中已经存储了百万条记录，该操作也可以在常量时间内完成。然而需要说明的是，如果元素插入或删除操作是作用于链表中间，那将会是非常低效的。  

#### 操作

- `lpush/lpushx/lrange`  

  ```sh
  127.0.0.1:6379> flushdb
  OK
  
  #创建键mykey及与其关联的List，然后将参数中的values从左到右依次插入，头部插入
  127.0.0.1:6379> lpush mykey a b c d
  (integer) 4
  
  #获取从位置0开始到位置2结束的3个元素
  127.0.0.1:6379> lrange mykey 0 2
  1) "d"
  2) "c"
  3) "b"
  
  #获取链表中的全部元素，其中0表示第一个元素，-1表示最后一个元素
  127.0.0.1:6379> lrange mykey 0 -1
  1) "d"
  2) "c"
  3) "b"
  4) "a"
  
  #获取从倒数第3个到倒数第2个的元素
  127.0.0.1:6379> lrange mykey -3 -2
  1) "c"
  2) "b"
  
  #lpushx表示键存在时才能插入，mykey2键此时并不存在，因此该命令将不会进行任何操作，其返回值为0
  127.0.0.1:6379> lpushx mykey2 e
  (integer) 0
  
  #可以看到mykey2没有关联任何List Value
  127.0.0.1:6379> lrange mykey2 0 -1
  (empty list or set)
  
  #mykey键此时已经存在，所以该命令插入成功，并返回链表中当前元素的数量
  127.0.0.1:6379> lpushx mykey e
  (integer) 5
  
  #获取该键的List中的第一个元素
  127.0.0.1:6379> lrange mykey 0 0
  1) "e"
  ```

- `lpop/llen`  

  ```sh
  127.0.0.1:6379> flushdb
  OK
  
  127.0.0.1:6379> lpush mykey a b c d
  (integer) 4
  
  #取出链表头部的元素，该元素在链表中就已经不存在了
  127.0.0.1:6379> lpop mykey
  "d"
  
  127.0.0.1:6379> lpop mykey
  "c"
  
  #在执行lpop命令两次后，链表头部的两个元素已经被弹出，此时链表中元素的数量是2
  127.0.0.1:6379> llen mykey
  (integer) 2
  ```

- `lrem/lindex/lset/ltrim`  

  ```sh
  127.0.0.1:6379> flushdb
  OK
  
  #准备测试数据
  127.0.0.1:6379> lpush mykey a b c d a c
  (integer) 6
  
  #从头部(left)向尾部(right)操作链表，删除2个值等于a的元素，返回值为实际删除的数量
  127.0.0.1:6379> lrem mykey 2 a
  (integer) 2
  
  #查看删除后链表中的全部元素
  127.0.0.1:6379> lrange mykey 0 -1
  1) "c"
  2) "d"
  3) "c"
  4) "b"
  
  #获取索引值为1(头部的第二个元素)的元素值
  127.0.0.1:6379> lindex mykey 1
  "d"
  
  #将索引值为1(头部的第二个元素)的元素值设置为新值e
  127.0.0.1:6379> lset mykey 1 e
  OK
  
  #查看是否设置成功
  127.0.0.1:6379> lindex mykey 1
  "e"
  
  #索引值6超过了链表中元素的数量，该命令返回nil
  127.0.0.1:6379> lindex mykey 6
  (nil)
  
  #设置的索引值6超过了链表中元素的数量，设置失败，该命令返回错误信息。
  127.0.0.1:6379> lset mykey 6 h
  (error) ERR index out of range
  
  #仅保留索引值0到2之间的3个元素，注意第0个和第2个元素均被保留。
  127.0.0.1:6379> ltrim mykey 0 2
  OK
  
  #查看trim后的结果
  127.0.0.1:6379> lrange mykey 0 -1
  1) "c"
  2) "e"
  3) "c"
  ```

  

















































