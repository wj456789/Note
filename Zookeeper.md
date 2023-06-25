# Zookeeper 

**ZooKeeper是一个分布式数据管理框架，注册中心，客户端注册监听它关心的目录节点，当目录节点发生变化时，ZooKeeper会通知客户端 。**

Apache ZooKeeper是一个开源的分布式服务框架(发布/订阅模式的分布式数据管理与协调框架)，为分布式应用提供协调服务，用来解决分布式应用中的数据管理问题，如：配置管理、域名服务、分布式同步、集群管理等 

**分布式** 

将一个大型应用的不同业务部署在不同的服务器上，解决高并发的问题 。

**集群**

将同一个业务部署在多台服务器上，提高系统的高可用性 。

## 组成

ZooKeeper主要包括两部分：文件系统、通知机制。

### 文件系统

ZooKeeper维护一个类似Linux文件系统的数据结构(多层级的节点命名空间)，用于存储数据。与linux文件系统不同的是，这些节点都可以设置关联的数据，而linux文件系统中只有文件节点可以存放数据而目录节点不行。

- 数据模型结构是一种树形结构，由许多节点构成，每个节点叫做ZNode（ZooKeeper Node） 
- 每个节点对应一个唯一路径，通过该路径来标识节点，如 /app1/p_2，每个节点只能存储大约1M的数据

![image-20210913133941239](img_Zookeeper/image-20210913133941239.png)

节点类型有四种： 

- 持久化目录节点 persistent 

  客户端与服务器断开连接，该节点仍然存在 

- 持久化顺序编号目录节点 persistent_sequential

  基本特性同持久节点，只是增加了顺序属性，节点名后边会追加一个由父节点维护的自增整型数字，如：000001、000002..... 

- 临时目录节点 ephemeral 

  临时节点的生命周期与客户端会话绑定，一旦客户端会话失效（客户端与zookeeper 连接断开不一定会话失效），那么这个客户端创建的所有临时节点都会被移除。

- 临时顺序编号目录节点 ephemeral_sequential 

  基本特性同临时节点，增加了顺序属性，节点名后边会追加一个由父节点维护的自增整型数字，如：000001、000002..... 



顺序编号节点： 

- 顺序编号会紧跟在节点名称后面，节点最终名称为：节点名+序号，如/test0000000005 
- 顺序编号是一个递增的计数器 
- 顺序编号是由父节点维护，从已有的子节点个数开始（包括临时节点和被删除的节点） 
- 如果子节点为空，则从0000000000开始，依次递增1 
- 在分布式系统中，顺序编号可以被用于为所有的事件进行全局排序，这样客户端就可以根据序号推断事件的顺序 

### 通知机制

ZooKeeper是一个基于**观察者模式**设计的分布式服务管理框架

1. ZooKeeper负责管理和维护项目的公共数据，并授受观察者的注册(订阅)；

2. 一旦这些数据发生变化，ZooKeeper就会通知已注册的观察者；

3. 此时观察者就可以做出相应的反应；

简单来说，客户端注册监听它关心的目录节点，当目录节点发生变化时，ZooKeeper会通知客户端 。

ZooKeeper是一个订阅中心(注册中心)。




## 应用场景

### 配置管理

场景：集群环境、服务器的许多配置都是相同的，如：数据库连接信息，当需要修改这些配置时必须同时修改每台服务器，很麻烦

解决：把这些配置全部放到ZooKeeper上，保存在ZooKeeper的某个目录节点中，然后所有的应用程序(客户端)对这个目录节点进行监视Watch，一旦配置信息发生变化，ZooKeeper会通知每个客户端，然后从ZooKeeper获取新的配置信息，并应用到系统中。 

![image-20210913135204629](img_Zookeeper/image-20210913135204629.png)

### 集群管理

场景：集群环境下，如何知道有多少台机器在工作？是否有机器退出或加入？需要选举一个总管master，让总管来管理集群 

解决：在父目录GroupMembers下为所有机器创建临时目录节点，然后监听父目录节点的子节点变化，一旦有机器挂掉，该机器与ZooKeeper的连接断开，其所创建的临时目录节点被删除，所有其他机器都会收到通知。当有新机器加入时也是同样的道理。 

选举master：为所有机器创建临时顺序编号目录节点，给每台机器编号，然后每次选取编号最小的机器作为master

![image-20210913135605138](img_Zookeeper/image-20210913135605138.png)

### 负载均衡

ZooKeeper本身是不提供负载均衡策略的，需要自己实现，所以准确的说，是在负载均衡中使用ZooKeeper来做集群的协调（也称为软负载均衡） 

实现思路： 

1. 将ZooKeeper作为服务的注册中心，所有服务器在启动时向注册中心登陆自己能够提供的服务 

2. 服务的调用者到注册中心获取能够提供所需要服务的服务器列表，然后自己根据负载均衡算法，从中选取一台服务器进行连接 

3. 当服务器列表发生变化时，如：某台服务器宕机下线，或新机器加入，ZooKeeper会自动通知调用者重新获取服务列表 

实际上利用了ZooKeeper的特性，将ZooKeeper用为服务的注册和变更通知中心

![image-20210913162250122](img_Zookeeper/image-20210913162250122.png)

### 常用功能

- 集群管理：监控节点存活状态、运行请求等；

- 主节点选举：主节点挂掉了之后可以从备用的节点开始新一轮选主，主节点选举说的就是这个选举的过程，使用 Zookeeper 可以协助完成这个过程；

- 分布式锁：Zookeeper 提供两种锁：独占锁、共享锁。独占锁即一次只能有一个线程使用资源，共享锁是读锁共享，读写互斥，即可以有多线线程同时读同一个资源，如果要使用写锁也只能有一个线程使用。Zookeeper 可以对分布式锁进行控制。

- 命名服务：在分布式系统中，通过使用命名服务，客户端应用能够根据指定名字来获取资源或服务的地址，提供者等信息。

#### 分布式锁

有了 zookeeper 的一致性文件系统，锁的问题变得容易。锁服务可以分为两类，一个是保持独占，另一个是控制时序。

对于第一类，我们将 zookeeper 上的一个 znode 看作是一把锁，通过 createznode的方式来实现。所有客户端都去创建 /distribute_lock 节点，最终成功创建的那个客户端也即拥有了这把锁。用完删除掉自己创建的 distribute_lock 节点就释放出锁。

对于第二类， /distribute_lock 已经预先存在，所有客户端在它下面创建临时顺序编号目录节点，和选 master 一样，编号最小的获得锁，用完删除，依次方便。

## 基础操作

### 安装

ZooKeeper一般都运行在Linux平台

#### 步骤

1. 解压zookeeper­3.4.13.tar.gz

   ```sh
   cd ~/software 
   tar -zxf zookeeper-3.4.13.tar.gz
   ```

2. 配置

   ```sh
   # 创建存放数据文件的目录 
   cd zookeeper-3.4.13/ 
   mkdir data 
   # 创建配置文件 
   cd conf 
   cp zoo_sample.cfg zoo.cfg # 默认使用的是zoo.cfg，名称固定 
   # 修改配置文件 
   vi zoo.cfg 
   	dataDir=../data # 指定数据存放目录
   ```

3. 启动zookeeper 

   ```sh
   cd bin 
   ./zkServer.sh start | stop | status | restart # 启动|停止|查看状态|重启
   ```

4. 客户端连接zookeeper 

   Zookeeper 的 java 客户端：自带的 zkclient

   ```sh
   ./zkCli.sh # 启动客户端，默认连接本机的2181端口 
   或
   ./zkCli.sh -server 服务器地址:端口 # 连接指定主机、指定端口的zookeeper 
   quit # 退出客户端
   ```

#### 配置文件

| 配置项                      | 含义                             | 说明                                                         |
| --------------------------- | -------------------------------- | ------------------------------------------------------------ |
| tickTime=2000               | 心跳时间                         | 维持心跳的时间间隔，单位是毫秒；在zookeeper中所有的时间都是以这个时间为基础单元，进行整数倍配置 |
| initLimit=10                | 初始通信时限                     | 用于zookeeper集群，此时有多台zookeeper服务器，其中一个为Leader，其他都为Follower |
| syncLimit=5                 | 同步通信时限                     | 在运行时Leader通过心跳检测与Follower进行通信，如果超过syncLimit*tickTime时间还未收到响应，则认为该Follower已经宕机 |
| dataDir=../data             | 存储数据的目录                   | 数据文件也称为snapshot快照文件                               |
| clientPort=2181             | 端口号                           | 默认为2181                                                   |
| maxClientCnxns=60           | 单个客户端的最大连接数限制       | 默认为60，可以设置为0，表示没有限制                          |
| autopurge.snapRetainCount=3 | 保留文件的数量                   | 默认3个                                                      |
| autopurge.purgeInterval=1   | 自动清理快照文件和事务日志的频率 | 默认为0，表示不开启自动清理，单位是小时                      |
| dataLogDir=                 | 存储日志的目录                   | 未指定时日志文件也存放在dataDir中，为了性能最大化，一般建议把dataDir和dataLogDir分别放到不同的磁盘上 |

### 客户端操作

#### 常用命令

| 命令                     | 作用                   | 说明                                       |
| ------------------------ | ---------------------- | ------------------------------------------ |
| help                     | 查看帮助               | 查看所有操作命令                           |
| ls 节点路径              | 查看指定节点下的内容   |                                            |
| ls2 节点路径             | 查看指定节点的详细信息 | 查看所有子节点和当前节点的状态             |
| get 节点路径             | 获取节点中的值         |                                            |
| create 节点路径 内容     | 创建普通节点           | 如果内容中有空格，则需要使用对双引号引起来 |
| create ­-e 节点路径 内容 | 创建临时节点           | 当连接断开后，节点会被自动删除             |
| create ­-s 节点路径 内容 | 创建顺序编号节点       | 即带序号的节点                             |
| delete 节点路径          | 删除节点               | 只能删除空节点，即不能有子节点             |
| rmr 节点路径             | 递归删除节点           | remove recursion                           |
| stat 节点路径            | 查看节点状态           |                                            |
| set 节点路径 新值        | 修改节点内容           |                                            |

#### 详解

查看指定节点的详细信息：` ls2 / `

```sh
# 子节点名称数组 
[zookeeper]

# -------------节点的状态信息，也称为stat结构体------------------

# 创建该znode的事务的zxid(ZooKeeper Transaction ID)
# 事务ID是ZooKeeper为每次更新操作/事务操作分配一个全局唯一的id，表示zxid，值越小，表示越先执行
cZxid = 0x0 # 0x0表示十六进制数0

# 创建时间
ctime = Thu Jan 01 08:00:00 CST 1970

# 最后一次更新的zxid
mZxid = 0x0

# 最后一次更新的时间
mtime = Thu Jan 01 08:00:00 CST 1970

# 最后更新的子节点的zxid
pZxid = 0x0

# 子节点的变化号，表示子节点被修改的次数，-1表示从未被修改过
cversion = -1

# 当前节点的变化号，0表示从未被修改过
dataVersion = 0

# 访问控制列表的变化号 access control list
aclVersion = 0

# 如果临时节点，表示当前节点的拥有者的sessionId
# 如果不是临时节点，则值为0
ephemeralOwner = 0x0

# 数据长度
dataLength = 0

# 子节点数据
numChildren = 1
```



## ACL 权限控制机制

ACL全称为Access Control List 即访问控制列表，用于控制资源的访问权限。zookeeper利用ACL策略控制节点的访问权限，如节点数据读写、节点创建、节点删除、读取子节点列表、设置节点权限等。

在Zookeeper中，znode的ACL是没有继承关系的，每个znode的权限都是独立控制的，只有客户端满足znode设置的权限要求时，才能完成相应的操作。

Zookeeper的ACL，分为三个维度：scheme、id、permission，通常表示为：`scheme:id:permission`，schema代表授权策略，id代表用户，permission代表权限。

### scheme

scheme对应于采用哪种方案来进行权限管理，zookeeper的scheme的分类如下：

- **world**: 它下面只有一个id, 叫anyone, `world:anyone`代表任何人，zookeeper中对所有人有权限的结点就是属于`world:anyone`的
- **digest**:是最常用的权限控制模式，也更符合我们对权限控制的认识，其类似于`username:password`形式的权限标识进行权限配置。它对应的id为`username:BASE64(SHA1(password))`，即对密码先做SHA1加密然后再进行BASE64摘要。
- **ip**: 它对应的id为客户机的IP地址，设置的时候可以设置一个ip段，比如`ip:192.168.1.0/16`, 表示匹配前16个bit的IP段，也可以设置为某一个具体的ip
- **auth**: 它不需要id, 只要是通过authentication的user都有权限（zookeeper支持通过kerberos来进行authencation, 也支持`username/password`形式的authentication)
- **super**: 在这种scheme情况下，对应的id拥有超级权限，可以做任何事情(cdrwa)
  其实这几种scheme中最常用的也就是world，digest和ip，其他的都很少使用，了解一下就行了。

### id

id是验证模式，不同的scheme，id的值也不一样。scheme为ip时，id的值为客户端的ip地址。scheme为world时，id的值为anyone。scheme为digest时，id的值为：username:BASE64(SHA1(password))。

### permission

zookeeper目前支持下面一些权限：

- **CREATE(c)**: 创建权限，可以在在当前node下创建child node，即对子节点Create操作
- **DELETE(d)**: 删除权限，可以删除当前的node，即对子节点Delete操作
- **READ(r)**: 读权限，可以获取当前node的数据，可以list当前node所有的child nodes，即对本节点GetChildren和GetData操作
- **WRITE(w)**: 写权限，可以向当前node写数据，即对本节点SetData操作
- **ADMIN(a)**: 管理权限，可以设置当前node的permission，即对本节点setAcl操作

### 通过命令行操作ACL

#### 获取ACL

```sh
#创建节点没有设置ACL的时候，默认使用的是world这种scheme，任何客户端都可以访问。
[zk: localhost:2181(CONNECTED) 2] create /test aaa
Created /test
[zk: localhost:2181(CONNECTED) 3] getAcl /test
'world,'anyone
: cdrwa
[zk: localhost:2181(CONNECTED) 4] 
```

#### 设置digest

Zookeeper的java api中有一个类可以先对密码做SHA1加密然后再做Base64加密操作：

```java
String str = DigestAuthenticationProvider.generateDigest("wkp:135791"); 
```

这个方法会对冒号后面的密码做加密操作，通过运行得到返回值为`wkp:NrLAZ6FuRnaPGI93r1uPKD67MLw=`。我们可以看下这个方法的源码：

![img](img_Zookeeper/20181031225305841.png)

然后使用此加密密码进行设置操作

```sh
#用户名:加密密码:权限
setAcl /node digest:[username]:[Encrypted password]:[perms]

#digest的认证方式，注意这里的密码为明文
addauth digest [username]:[password plain]
```

```sh
[zk: localhost:2181(CONNECTED) 44] setAcl /test digest:wkp:NrLAZ6FuRnaPGI93r1uPKD67MLw=:cdrwa
cZxid = 0x620000001a
ctime = Wed Oct 31 23:03:51 CST 2018
mZxid = 0x620000001a
mtime = Wed Oct 31 23:03:51 CST 2018
pZxid = 0x620000001a
cversion = 0
dataVersion = 0
aclVersion = 1
ephemeralOwner = 0x0
dataLength = 3
numChildren = 0
[zk: localhost:2181(CONNECTED) 45] getAcl /test
'digest,'wkp:NrLAZ6FuRnaPGI93r1uPKD67MLw=
: cdrwa
[zk: localhost:2181(CONNECTED) 46] create /test/aaa 111
Authentication is not valid : /test/aaa
[zk: localhost:2181(CONNECTED) 50] addauth digest wkp:135791
[zk: localhost:2181(CONNECTED) 51] create /test/aaa 111     
Created /test/aaa
[zk: localhost:2181(CONNECTED) 52] 
```

#### 设置ip认证

我们创建一个/ipTest节点并且设置scheme为ip形式，限制连接ip为127.0.0.1，然后我们本机是可以访问的

```sh
[zk: localhost:2181(CONNECTED) 1] create /ipTest 111
Created /ipTest
[zk: localhost:2181(CONNECTED) 2] setAcl /ipTest ip:127.0.0.1:crwda
cZxid = 0x620000002a
ctime = Wed Oct 31 23:40:20 CST 2018
mZxid = 0x620000002a
mtime = Wed Oct 31 23:40:20 CST 2018
pZxid = 0x620000002a
cversion = 0
dataVersion = 0
aclVersion = 1
ephemeralOwner = 0x0
dataLength = 3
numChildren = 0
[zk: localhost:2181(CONNECTED) 3] getAcl /ipTest
'ip,'127.0.0.1
: cdrwa
[zk: localhost:2181(CONNECTED) 4] ls /ipTest
[]
```

下面我们切换到集群的另一台机器上进行操作，发现认证不通过

```sh
[zk: localhost:2181(CONNECTED) 0] ls /
[testRoot, dubbo, bhz, test, zookeeper, activemq2, switch, ipTest]
[zk: localhost:2181(CONNECTED) 1] ls /ipTest
Authentication is not valid : /ipTest
[zk: localhost:2181(CONNECTED) 2] 
```

### 通过java api操作ACL



参考：

[zookeeper的ACL权限控制机制介绍](https://blog.csdn.net/u012988901/article/details/83388419)



## 集群

### 配置集群

**步骤：**

1. 准备多台ZooKeeper服务器 

2. 配置ZooKeeper服务器 

   在每台服务器的conf/zoo.cfg文件中添加如下内容：

```cfg
server.20=192.168.4.20:2888:3888 
server.21=192.168.4.21:2888:3888 
server.22=192.168.4.22:2888:3888
```

​		格式： `server.A=B:C:D`

- A表示这台服务器的编号ID，是一个数字 

- B表示服务器的IP地址或域名 

- C表示这台服务器与集群中的Leader交换信息时使用的端口 

- D表示执行选举Leader服务器时互相通信的端口

3. 创建myid配置文件 

   在集群环境下，需要在 dataDir 目录中创建一个名为 myid 的文件，文件内容是当前服务器的编号ID，即上面配置的A 

```sh
cd data 
echo A的值 > myid
```

​		ZooKeeper启动时会读取这个文件，将里面的数字与zoo.cfg中配置的server.A进行比较，从而判断这台服务器是哪个 

4. 测试集群环境 

   启动所有ZooKeeper服务器，查看状态。此时在某台服务器上执行更新操作时，其他服务器也会同步 。

#### 部署模式

- 单机部署：一台集群上运行；

- 集群部署：多台集群运行；

- 伪集群部署：一台集群启动多个 Zookeeper 实例运行；

#### 动态扩容

其实就是水平扩容了，Zookeeper 在这方面不太好。两种方式：

- 全部重启：关闭所有 Zookeeper 服务，修改配置之后启动。不影响之前客户端的会话。

- 逐个重启：在过半存活即可用的原则下，一台机器重启不影响整个集群对外提供服务。这是比较常用的方式。

3.5 版本开始支持动态扩容。

### 集群特性

- 一个ZooKeeper集群中，有一个领导者Leader和多个跟随者Follower 
  - Leader负责进行投票的发起和决议；事务请求的唯一调度和处理者，保证集群事务处理的顺序性；集群内部各服务的调度者；
  - Follower用于接收客户端的请求并向客户端返回结果，其中会处理客户端的非事务请求，转发事务请求给 Leader 服务器，参与事务请求Proposal(提议)的投票；参与 Leader 选举投票；
- Observer  3.0 版本以后引入的一个服务器角色，在不影响集群事务处理能力的基础上提升集群的非事务处理能力；处理客户端的非事务请求，转发事务请求给 Leader 服务器；不参与任何形式的投票；
  
- 半数机制：集群中只要有半数以上节点存活，集群就能够正常工作，所以一般集群中的服务器个数都为奇数

- 全局数据一致：集群中每台服务器保存一份相同的数据副本，不论客户端连接到哪个服务器，数据都是一致的 

- 数据更新请求
  - 顺序执行：来自同一个客户端的更新请求，按其发送顺序依次执行 
  - 原子性：一次数据更新，要么成功，要么失败 
  - 实时性：在一定的时间范围内，客户端能读取到最新数据

客户端的读请求可以被集群中的任意一台机器处理。写请求，这些请求会同时发给其他 zookeeper 机器并且达成一致后，请求才会返回成功。因此，随着 zookeeper 的集群机器增多，读请求的吞吐会提高但是写请求的吞吐会下降。

#### 分布式事务

分布式事务就是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。简单的说，就是一次大的操作由不同的小操作组成，这些小的操作分布在不同的服务器上，且属于不同的应用，分布式事务需要保证这些小操作要么全部成功，要么全部失败。本质上来说，分布式事务就是为了保证不同数据库的数据一致性。

集群处理请求分两种：事务和非事务，对于非事务，请求处理和单机类似，节点本地就可以完成数据的请求；事务请求需要提交给Leader处理，Leader以投票的形式，等待半数的Follower的投票，完成同步后才将操作结果返回。

zookeeper采用了全局递增的事务Id来标识，**从而保证事务的顺序一致性**，所有的事务请求proposal(提议)都在被提出的时候加上了zxid，zxid 实际上是一个 64 位的数字，高32位是 epoch（ 时期; 纪元; 世; 新时代）用来标识 leader 周期，如果有新的 leader 产生出来，epoch会自增，低 32 位用来递增计数。当新产生 proposal 的时候，会依据数据库的两阶段过程，首先会向其他的 server 发出事务执行请求，如果超过半数的机器都能执行并且能够成功，那么就会开始执行。


### 选举机制

ZooKeeper在提供服务时会自动选举一个节点服务器作为Leader，其他都是Follower 

![image-20210915145822034](img_Zookeeper/image-20210915145822034.png)

选举流程： 

1. Server1启动，给自己投票，然后发送投票信息，由于其它服务器都还没启动，所以它发现的消息收不到任何反馈，此时Server1为`Looking`状态 

2. Server2启动，给自己投票，同时与Server1通信交换选举结果，由于Server2的id值较大，所以Server2胜出，但由于投票数没有过半，此时Server1和Server2都为`Looking`状态 

3. Server3启动，给自己投票，同时与Server1和Server2通信交换选举结果，由于Server3的id值较大，所以Server3胜出，此时票数已经过半，所以Server3为Leader，Server1和Server2为Follower 

4. Server4启动，给自己投票，同时与Server1、Server2、Server3通信交换选举结果，尽管Server4的id较大，但由于集群中已经存在Leader，所以Server4只能为Follower 

5. Server5启动，同Server4类似，只能为Follower 

总结：

- 每个服务器在启动时都会选择自己，然后将投票信息发送出去 
- 服务器编号ID越大，在选择算法中的权重越大 
- 投票数必须过半，才能选出Leader 
- 谁是Leader：启动顺序的前集群数/2+1 个服务器中，id值最大的会成为Leader 

#### 节点宕机

- 如果是一个 Follower 宕机，还有 2 台服务器提供访问，因为 Zookeeper 上的数据是有多个副本的，数据并不会丢失；
- 如果是一个 Leader 宕机，Zookeeper 会选举出新的 Leader。


ZK 集群的机制是只要超过半数的节点正常，集群就能正常提供服务。只有在 ZK节点挂得太多，只剩一半或不到一半节点能工作，集群才失效。

如：

- 3 个节点的 cluster 可以挂掉 1 个节点(leader 可以得到 2 票>1.5)
- 2 个节点的 cluster 就不能挂掉任何 1 个节点了(leader 可以得到 1 票<=1)

### 监听机制

#### 监听节点值的变化

```sh
# 在集群的A服务器，监听某个节点值的变化 
get /yyy watch 

# 在集群的B服务器，修改对应节点的值 
set /yyy myyyy 

# 此时A服务器会收到事件NodeDataChanged 
WATCHER:: 
WatchedEvent state:SyncConnected type:NodeDataChanged path:/yyy
```

监听Watch事件是一个一次性的触发器，当数据改变时只会触发一次，如果以后这个数据再发生改变，则不会再次触发 

#### 监听节点的子节点变化

```sh
# 在集群的A服务器，监听某个节点的子节点的变化 
ls /yyy watch 

# 在集群的B服务器，创建/修改/删除对应节点的子节点 
create /yyy/hello hello 

# 此时A服务器会收到事件NodeChildrenChanged 
WATCHER:: 
WatchedEvent state:SyncConnected type:NodeChildrenChanged path:/yyy
```

#### 工作机制

<img src="img_Zookeeper/1496926-20200208143818212-1084501821.jpg" alt="img" style="zoom:200%;" />

Zookeeper允许客户端向服务端的某个Znode注册一个 Watcher 监听，当服务端的一些指定事件触发了这个 Watcher，服务端会向指定客户端发送一个事件通知来实现分布式的通知功能，然后客户端根据 Watcher 通知状态和事件类型做出业务上的改变。

1. 客户端注册 watcher

2. 服务端处理 watcher

3. 客户端回调 watcher

##### Watcher 特性

- 一次性

  无论是服务端还是客户端，一旦一个 Watcher 被 触 发 ，Zookeeper 都会将其从相应的存储中移除。原因是如果服务端变动频繁，而监听的客户端很多情况下，每次变动都要通知到所有的客户端，给网络和服务器造成很大压力

- 轻量
  - Watcher 通知非常简单，只会告诉客户端发生了事件，而不会说明事件的具体内容。
  - 客户端向服务端注册 Watcher 的时候，并不会把客户端真实的 Watcher 对象实体传递到服务端，仅仅是在客户端请求中使用 boolean 类型属性进行了标记。

- watcher event 异步发送

  watcher 的通知事件从 server 发送到 client 是异步的，这就存在一个问题，不同的客户端和服务器之间通过 socket 进行通信，由于网络延迟或其他因素导致客户端在不通的时刻监听到事件，由于 Zookeeper 本身提供了 ordering guarantee，即客户端监听事件后，才会感知它所监视 znode发生了变化。所以我们使用 Zookeeper 不能期望能够监控到节点每次的变化。Zookeeper 只能保证最终的一致性，而无法保证强一致性。

- 客户端串行执行

  客户端 Watcher 回调的过程是一个串行同步的过程。

- 当一个客户端连接到一个新的服务器上时，watch 将会被以任意会话事件触发。当与一个服务器失去连接的时候，是无法接收到 watch 的。而当 client 重新连接时，如果需要的话，所有先前注册过的 watch，都会被重新注册。通常这是完全透明的。只有在一个特殊情况下，watch 可能会丢失：对于一个未创建的 znode的 exist watch，如果在客户端断开连接期间被创建了，并且随后在客户端连接上之前又删除了，这种情况下，这个 watch 事件可能会被丢失。
  

##### 客户端注册 Watcher

1. 调用 getData()/getChildren()/exist()三个 API，传入 Watcher 对象

2. 标记请求 request，封装 Watcher 到 WatchRegistration

3. 封装成 Packet 对象，向服务端发送 request

4. 收到服务端响应后，将 Watcher 注册到 ZKWatcherManager 中进行管理

5. 请求返回，完成注册。

##### 服务端处理 Watcher

1. 服务端接收 Watcher 并存储

   接收到客户端请求，处理请求判断是否需要注册 Watcher，需要的话将数据节点的节点路径和 ServerCnxn（ServerCnxn 代表一个客户端和服务端的连接，实现了 Watcher 的 process 接口，此时可以看成一个 Watcher 对象）存储在WatcherManager 的 WatchTable 和 watch2Paths 中去。

2. Watcher 触发

   以服务端接收到 setData() 事务请求触发 NodeDataChanged 事件为例：

   2.1 封装 WatchedEvent

   将通知状态（SyncConnected）、事件类型（NodeDataChanged）以及节点路径封装成一个 WatchedEvent 对象

   2.2 查询 Watcher

   从 WatchTable 中根据节点路径查找 Watcher

   2.3 没找到；说明没有客户端在该数据节点上注册过 Watcher

   2.4 找到；提取并从 WatchTable 和 Watch2Paths 中删除对应 Watcher（从这里可以看出 Watcher 在服务端是一次性的，触发一次就失效了）

3. 调用 process 方法来触发 Watcher

   这里 process 主要就是通过 ServerCnxn 对应的 TCP 连接发送 Watcher 事件通知。

##### 客户端回调 Watcher

客户端 SendThread 线程接收事件通知，交由 EventThread 线程回调 Watcher。

客户端的 Watcher 机制同样是一次性的，一旦被触发后，该 Watcher 就失效了。



参考：[深入理解 ZooKeeper客户端与服务端的watcher回调](https://www.cnblogs.com/ZhuChangwu/p/11593642.html)

























### Server工作状态

服务器具有四种状态，分别是 LOOKING、FOLLOWING、LEADING、OBSERVING。

- LOOKING：寻找Leader状态。当服务器处于该状态时，它会认为当前集群中没有Leader，因此需要进入Leader选举状态。

- FOLLOWING：跟随者状态。表明当前服务器角色是 Follower。

- LEADING：领导者状态。表明当前服务器角色是 Leader。

- OBSERVING：观察者状态。表明当前服务器角色是 Observer。

### 状态同步

Zookeeper保证了主从节点的状态同步。Zookeeper 的核心是原子广播机制，这个机制保证了各个server之间的同步。实现这个机制的协议叫做Zab协议。Zab协议有两种模式，它们分别是恢复模式和广播模式。

- 恢复模式：和leader同步
  当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，且大多数 server 完成了和 leader 的状态同步以后，恢复模式就结束了。状态同步保证了 leader 和 server 具有相同的系统状态。

- 广播模式：消息广播同步
  一旦 leader 已经和多数的 follower 进行了状态同步后，它就可以开始广播消息了，即进入广播状态。这时候当一个 server 加入 ZooKeeper 服务中，它会在恢复模式下启动，发现 leader，并和 leader 进行状态同步。待到同步结束，它也参与消息广播。ZooKeeper 服务一直维持在 Broadcast 状态，直到 leader 崩溃了或者 leader 失去了大部分的 followers 支持。

### 数据同步

整个集群完成Leader选举之后，Learner(Follower和Observer 的统称)会向Leader服务器进行注册。当 Learner服务器向Leader服务器完成注册后，进入数据同步环节。

**Zookeeper 的数据同步通常分为四类：**

- 直接差异化同步（DIFF 同步）

- 先回滚再差异化同步（TRUNC+DIFF 同步）

- 仅回滚同步（TRUNC 同步）

- 全量同步（SNAP 同步）

**在进行数据同步前，Leader 服务器会完成数据同步初始化：**

- peerLastZxid
- minCommittedLog

### ZAB 和 Paxos 算法

**相同点：**

- 两者都存在一个类似于 Leader 进程的角色，由其负责协调多个 Follower 进程的运行

- Leader 进程都会等待超过半数的 Follower 做出正确的反馈后，才会将一个提案进行提交

- ZAB 协议中，每个 Proposal 中都包含一个 epoch 值来代表当前的 Leader周期，Paxos 中名字为 Ballot

**不同点：**

ZAB 用来构建高可用的分布式数据主备系统（Zookeeper），Paxos 是用来构建分布式一致性状态机系统。


## Java访问ZooKeeper

### 依赖

```xml
<!--ZooKeeper客户端--> 
<dependency> 
    <groupId>org.apache.zookeeper</groupId> 
    <artifactId>zookeeper</artifactId> 
    <version>3.4.13</version> 
</dependency>
```

### 操作

```java
public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
    //获取ZooKeeper的连接，即创建ZooKeeper的客户端
    String connectString = "127.0.0.1:2181"; 	//服务器地址 
    int sessionTimeout = 3000; 					//超时时间，单位为毫秒
    Watcher watcher = new MyWatcher(); 
    ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, watcher);

    //Thread.sleep(2000); 
    //System.out.println(zkClient.getState());
    /** 
    * 操作ZooKeeper 
    */   
    // 查看指定节点下的内容 
    // List<String> children = zkClient.getChildren("/", true);//第二个参数表示是 否监视该节点    
    // System.out.println(children);
        
    // 创建节点，OPEN_ACL_UNSAFE表示acl权限列表为完全开放，PERSISTENT表示节点类型为持久化节点
	// zkClient.create("/world", "世界".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

	// 获取节点的数据（节点的值和节点状态Stat） 
    // byte[] data = zkClient.getData("/world", true, null); 
   	// System.out.println(new String(data));

    // Stat stat = new Stat(); 
    // byte[] data = zkClient.getData("/hello", true, stat); 
    // byte[] data = zkClient.getData("/hello", new DataWatcher(), stat); 
    // System.out.println(new String(data)); 
    // System.out.println(stat); 
    // System.out.println(stat.getCtime()); 
    // System.out.println(stat.getVersion()); 
    // System.out.println(stat.getDataLength());
        
    //修改节点的数据 
    // zkClient.setData("/hello","aaa".getBytes(),stat.getVersion()); //第三个参数表示当前节点的数据版本，一般先获取数据stat，然后指定数据版本 
    // zkClient.setData("/hello", "bbb".getBytes(), -1); //也可以设置为-1，表示不检测版本

	//删除节点 
    // zkClient.delete("/hello", -1);

	//判断节点是否存在 
    System.out.println(zkClient.exists("/hello",false)); //存在时返回节点状态，不 存在则返回null 
        
    //休眠 
    // Thread.sleep(1000000); 
        
    //关闭连接 
    zkClient.close();

}
```



参考：

[ZooKeeper面试题（2020最新版）](https://thinkwon.blog.csdn.net/article/details/104397719)











