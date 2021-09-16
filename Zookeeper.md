# Zookeeper 

Apache ZooKeeper是一个开源的分布式服务框架，为分布式应用提供协调服务，用来解决分布式应用中的数据管理问题，如：配置管理、域名服务、分布式同步、集群管理等 

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

ZooKeeper是一个订阅中心（注册中心） 。




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

## 安装

ZooKeeper一般都运行在Linux平台

### 步骤

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

```sh
./zkCli.sh # 启动客户端，默认连接本机的2181端口 
或
./zkCli.sh -server 服务器地址:端口 # 连接指定主机、指定端口的zookeeper 
quit # 退出客户端
```

### 配置文件

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

## 客户端操作

### 常用命令

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

### 详解

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

### 集群特性

- 一个ZooKeeper集群中，有一个领导者Leader和多个跟随者Follower 
  - Leader负责进行投票的发起和决议，更新系统状态 
  - Follower用于接收客户端的请求并向客户端返回结果，在选举Leader过程中参与投票

- 半数机制：集群中只要有半数以上节点存活，集群就能够正常工作，所以一般集群中的服务器个数都为奇数

- 全局数据一致：集群中每台服务器保存一份相同的数据副本，不论客户端连接到哪个服务器，数据都是一致的 

- 数据更新请求
  - 顺序执行：来自同一个客户端的更新请求，按其发送顺序依次执行 
  - 原子性：一次数据更新，要么成功，要么失败 
  - 实时性：在一定的时间范围内，客户端能读取到最新数据



客户端的读请求可以被集群中的任意一台机器处理。写请求，这些请求会同时发给其他 zookeeper 机器并且达成一致后，请求才会返回成功。因此，随着 zookeeper 的集群机器增多，读请求的吞吐会提高但是写请求的吞吐会下降。

有序性(顺序执行)，所有的更新都是全局有序的，每个更新都有一个唯一的时间戳，这个时间戳称为 zxid（Zookeeper Transaction Id）。读请求的返回结果中会带有这个zookeeper 最新的 zxid。参考ls2命令。



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

### 状态同步

Zookeeper保证了主从节点的状态同步。Zookeeper 的核心是原子广播机制，这个机制保证了各个server之间的同步。实现这个机制的协议叫做Zab协议。Zab协议有两种模式，它们分别是恢复模式和广播模式。

- 恢复模式：和leader同步
  当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，且大多数 server 完成了和 leader 的状态同步以后，恢复模式就结束了。状态同步保证了 leader 和 server 具有相同的系统状态。

- 广播模式：消息广播同步
  一旦 leader 已经和多数的 follower 进行了状态同步后，它就可以开始广播消息了，即进入广播状态。这时候当一个 server 加入 ZooKeeper 服务中，它会在恢复模式下启动，发现 leader，并和 leader 进行状态同步。待到同步结束，它也参与消息广播。ZooKeeper 服务一直维持在 Broadcast 状态，直到 leader 崩溃了或者 leader 失去了大部分的 followers 支持。



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















