# Kafka

**Kafka是一个分布式的消息系统，消息队列MQ，保存消息的队列，是消息在传输过程中的容器。提供统一的、高吞吐量、低延迟的平台来处理实时数据流。**

## 消息系统

### 消息队列

- 消息Message：网络中的两台计算机或者两个通讯设备之间传递的数据，如：文本、音频、视频等。
- 队列Queue：一种特殊的线性表，特殊之处在于它只允许在首部删除元素（队首），在尾部添加元素（队尾）。
- 消息队列MQ：保存消息的队列，是消息在传输过程中的容器。主要提供生产和消费接口供外部调用，进行数据的存储和获取

### MQ分类

主要分为两类：点对点（Peer­to­Peer）、发布/订阅（Publish/Subscribe）

**共同点**：消息生产者(Producer)生产消息发送到队列中，然后消息消费者(Consumer)从队列中读取并消费消息

**不同点**：

- 点对点
  组成：消息队列(Queue)、发送者(Sender)、接收者(Receiver)
  一个生产者生产的消息只能有一个消费者，消息一旦被消费，消息就不在消息队列中了，如：打电话
  即发送到消息队列的消息能且只能被一个接收者接收
- 发布/订阅
  组成：消息队列(Queue)、发布者(Publisher)、订阅者(Subscriber)、主题(Topic)
  每个消息可以有多个消费者，彼此互不影响，如：我发布一个微博，关注我的人都能看到
  即发布到消息队列的消息能被多个接收者（订阅者）接收

### 常见消息系统

- ActiveMQ：历史悠久，实现了JMS（Java Message Service）规范，支持性较好，性能相对不高
- RabbitMQ：可靠性高、安全
- Kafka：分布式、高性能、跨语言
- RocketMQ：阿里开源的消息中间件，纯Java实现

## 概念

Kafka是一个分布式的发布/订阅消息系统，主要用于处理活跃的数据，如登录、浏览、点击、分享、喜欢等用户行为产生的数据。

特点：

- 高吞吐量
  可以满足每秒百万级别消息的生产和消费。
- 持久性
  有一套完善的消息存储机制，确保数据的高效安全的持久化。
- 分布式
  基于分布式的扩展和容错机制；
  Kafka的数据会复制到多台服务器上，当某一台发生故障失效时，生产者和消费者转而使用其它的机器。

### 初识

![image-20210921081911706](img_Kafka/image-20210921081911706.png)



- Broker：kafka集群中包含多个kafka服务节点，每一个kafka服务节点就称为一个broker，Broker 负责接收和处理客户端发送过来的请求，以及对消息进行持久化，虽然多个 Broker 进程能够运行在同一台机器上，但更常见的做法是将 不同的 Broker 分散运行在不同的机器上。

- Topic：主题，发布订阅的对象，可以为每 个业务、每个应用甚至是每类数据都创建专属的主题

- Partition：分区，每个主题 Topic 划分成多个分区 Partition，每个分区是一组有序的消息日志，生产者生产的每条消息只会被发送到主题的一个分区中，生产者向分区写入消息，**每条消息在分区中的位置信息叫位移**

- Replication：副本，每个分区可以有多个副本，分布在不同的Broker上；定义了两类副本：领导者副本和追随者副本，每个分区只能有 1 个领 导者副本和 N-1 个追随者副本；

  会选出一个副本作为Leader，Leader对外提供服务，与客户端程序进行交互，所有的读写请求都会通过Leader完成；所有Follower只负责备份数据，不能与外界进行交互，Follower会自动的从Leader中复制数据，当Leader宕机后， 会从Follower中选出一个新的Leader继续提供服务，实现故障自动转移

- Message：消息，是通信的基本单位，每个消息都属于一个Partition

- Producer：消息的生产者，向主题发布消息的客户端应用程序，生产者程序通常持续不断地向一个或多个主题发送消息

- Consumer：消息的消费者，订阅主题消息的客户端应用程序，消费者也能够同时订阅多个主题的消息。

- Consumer Group：多个消费者实例共同组成一个组，组内的所有消费者协调在一起来消费订阅主题的所有分区。

- Coordinator：协调者，为 Consumer Group 服务，负责为 Group 执行 Rebalance 以及提供位移管理和组成员管理等。

- Zookeeper：协调kafka的正常运行，Kafka将元数据信息保存在Zookeeper中，但发送给Topic本身的消息数据并不存储在ZK中，而在存储在磁盘文件中



### 扩展

#### 位移

- 消费者位移：Consumer Offset，消费者消费进度，每个消费者都有自己的消费者位移。

- 重平衡：Rebalance，消费者组内某个消费者实例挂掉后，其他消费者实例自动重新分配订阅主题分区的过程，Rebalance 是 Kafka 消费者端实现高可用的重要手段。

- AR（Assigned Replicas）：分区中的所有副本统称为AR。

  所有消息会先发送到 leader 副本，然后 follower 副本才能从 leader 中拉取消息进行同步。但是在同步期间，follower 对于 leader 而言会有一定程度的滞后，这个时候 follower 和 leader 并非完全同步状态

- OSR（Out Sync Replicas）：follower 副本与 leader 副本没有完全同步或滞后的副本集合

- ISR（In Sync Replicas）：AR 中的一个子集，ISR 中的副本都是与 leader 保持完全同步的副本，如果某个在 ISR 中的 follower 副本落后于 leader 副本太多，则会被从 ISR 中移除，否则如果完全同步，会从 OSR 中移至 ISR 集合。

  在默认情况下，当leader副本发生故障时，只有在ISR集合中的follower副本才有资格被选举为新leader，而OSR中的副本没有机会（可以通过`unclean.leader.election.enable`进行配置）

- HW（High Watermark）：高水位，它标识了一个特定的消息偏移量（offset），消费者只能拉取到这个水位 offset 之前的消息

  下图表示一个日志文件，这个日志文件中只有9条消息，第一条消息的offset（LogStartOffset）为0，最有一条消息的offset为8，offset为9的消息使用虚线表示的，代表下一条待写入的消息。

  日志文件的 HW 为6，表示消费者只能拉取offset在 0 到 5 之间的消息，offset为6的消息对消费者而言是不可见的。

  <img src="img_Kafka/weixin-kafkahxzszj-f27928a8-9a91-4e39-a68d-c74d8a3291f1.jpg" alt="img" style="zoom: 80%;" />

- LEO（Log End Offset）：标识当前日志文件中下一条待写入的消息的offset

  上图中offset为9的位置即为当前日志文件的 LEO，LEO 的大小相当于当前日志分区中最后一条消息的offset值加1

  分区 ISR 集合中的每个副本都会维护自身的 LEO ，而 ISR 集合中最小的 LEO 即为分区的 HW，对消费者而言只能消费 HW 之前的消息

同步副本最小的 LEO 即为高水位

## 生产者

### 生产者分区

Kafka的消息组织方式实际上是三级结构：主题 - 分区 - 消息。

不同的分区能够被放置到不同节点的机器上，而数据的读写操作也都是针对分区这个粒度而进行的，这样每个节点的机器都能独立地执行各自分区的读写请求处理，并且，我们还可以通过添加新的节点机器来增加整体系统的吞吐量。其实分区的作用就是提供负载均衡的能力，或者说对数据进行分区的主要原因，就是为了实现系统的高伸缩性（Scalability）。

### 分区策略

**分区策略是决定生产者将消息发送到哪个分区的算法。**

Kafka为我们提供了默认的分区策略，同时它也支持你自定义分区策略。

#### 自定义分区策略

编写一个具体的类实现`org.apache.kafka.clients.producer.Partitioner`接口。

这个接口定义了两个方法：partition()和close()，通常只需要实现最重要的partition方法。

```java
int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster);
```

topic、key、keyBytes、value和valueBytes都属于消息数据，cluster则是集群信息（比如当前Kafka集群共有多少主题、多少Broker等）。利用这些信息对消息进行分区，计算出它要被发送到哪个分区中。

还需要显式地配置生产者端的参数 partitioner.class 为你自己实现类的Full Qualified Name，那么生产者程序就会按照你的代码逻辑对消息进行分区。

#### 轮询策略

也称Round-robin策略，即顺序分配。轮询策略是Kafka Java生产者API默认提供的分区策略。

比如一个主题下有3个分区，那么第一条消息被发送到分区0，第二条被发送到分区1，第三条被发送到分区2，以此类推。当生产第4条消息时又会重新开始，即将其分配到分区0

**「轮询策略有非常优秀的负载均衡表现，它总是能保证消息最大限度地被平均分配到所有分区上，故默认情况下它是最合理的分区策略，也是我们最常用的分区策略之一。」**

#### 随机策略

也称Randomness策略。所谓随机就是我们随意地将消息放置到任意一个分区上。

```java
// 实现随机策略的partition方法，先计算出该主题总的分区数，然后随机地返回一个小于它的正整数。
List partitions = cluster.partitionsForTopic(topic);
return ThreadLocalRandom.current().nextInt(partitions.size());
```

#### 按消息键保序策略

Kafka为每条消息定义消息键，简称为Key，它可以是一个有着明确业务含义的字符串，比如客户代码、部门编号或是业务ID等；也可以用来表征消息元数据。

一旦消息被定义了Key，那么你就可以保证同一个Key的所有消息都进入到相同的分区里面，由于每个分区下的消息处理都是有顺序的，故这个策略被称为按消息键保序策略

```java
List partitions = cluster.partitionsForTopic(topic);
return Math.abs(key.hashCode()) % partitions.size();
```

前面提到的Kafka默认分区策略实际上同时实现了两种策略：如果指定了Key，那么默认实现按消息键保序策略；如果没有指定Key，则使用轮询策略。

#### 其他分区策略

其实还有一种比较常见的，即所谓的基于地理位置的分区策略。

当然这种策略一般只针对那些大规模的Kafka集群，特别是跨城市、跨国家甚至是跨大洲的集群。

```java
// 根据 Broker 所在的 IP 地址实现定制化的分区策略，我们可以从所有分区中找出那些 Leader 副本在南方的所有分区，然后随机挑选一个进行消息发送
List partitions = cluster.partitionsForTopic(topic);
return partitions.stream().filter(p -> isSouth(p.leader().host())).map(PartitionInfo::partition).findAny().get();
```

### 压缩算法

#### 消息

Kafka的消息层次都分为两层：消息集合以及消息。Kafka通常不会直接操作具体的一条条消息，它总是在消息集合这个层面上进行写入操作。

一个消息集合中包含若干条日志项，而日志项才是真正封装消息的地方。Kafka底层的消息日志由一系列消息集合日志项组成。

#### V2版本

目前Kafka共有两大类消息格式，社区分别称之为V1版本和V2版本。V2版本是Kafka 0.11.0.0中正式引入的。V2版本主要是针对V1版本的一些弊端做了修正，比如：

- 把消息的公共部分抽取出来放到外层**消息集合**里面，这样就不用每条消息都保存这些信息了。

  原来在V1版本中，每条消息都需要执行CRC校验，但有些情况下消息的CRC值是会发生变化的。比如在Broker端可能会对消息时间戳字段进行更新，那么重新计算之后的CRC值也会相应更新；再比如Broker端在执行消息格式转换时（主要是为了兼容老版本客户端程序），也会带来CRC值的变化。鉴于这些情况，再对每条消息都执行CRC校验就有点没必要了，不仅浪费空间还耽误CPU时间，因此在V2版本中，消息的CRC校验工作就被移到了消息集合这一层。

- 保存压缩消息的方法

  V1版本中保存压缩消息的方法是把多条消息进行压缩然后保存到外层消息的消息体字段中；而V2版本的做法是对整个消息集合进行压缩，显然后者应该比前者有更好的压缩效果。

#### 压缩

在Kafka中，压缩可能发生在两个地方：生产者端和Broker端。

**何时启用压缩是比较合适的时机呢？**启用压缩的一个条件就是Producer程序运行机器上的CPU资源要很充足。除了CPU资源充足这一条件，如果你的环境中带宽资源有限，那么建议你开启压缩。

##### 生产者端

```java
// 如何构建一个开启GZIP的Producer对象
Properties props = new Properties(); 
props.put("bootstrap.servers", "localhost:9092"); 
props.put("acks", "all"); 
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 开启GZIP压缩 
props.put("compression.type", "gzip"); 
Producer producer = new KafkaProducer<>(props);
// props.put(“compression.type”, “gzip”) 表明该Producer的压缩算法使用的是GZIP。生产者程序中配置compression.type参数即表示启用指定类型的压缩算法。
```

这样Producer启动后生产的每个消息集合都是经GZIP压缩过的，故而能很好地节省网络传输带宽以及Kafka Broker端的磁盘占用。

##### Broker端

有两种例外情况就可能让Broker重新压缩消息：

- 情况一：Broker端指定了和Producer端不同的压缩算法。

  一旦你在Broker端设置了不同的`compression.type`值，就一定要小心了，因为可能会发生预料之外的压缩/解压缩操作，通常表现为Broker端CPU使用率飙升。

- 情况二：Broker端发生了消息格式转换。

  所谓的消息格式转换主要是为了兼容老版本的消费者程序。

  在一个生产环境中，Kafka集群中同时保存多种版本的消息格式非常常见。为了兼容老版本的格式，Broker端会对新版本消息执行向老版本格式的转换。这个过程中会涉及消息的解压缩和重新压缩。一般情况下这种消息格式转换对性能是有很大影响的，除了这里的压缩之外，它还让Kafka丧失了Zero Copy特性。

#### 解压缩

解压缩发生在消费者程序中，也就是说Producer发送压缩消息到Broker后，Broker照单全收并原样保存起来。当Consumer程序请求这部分消息时，Broker依然原样发送出去，当消息到达Consumer端后，由Consumer自行解压缩还原成之前的消息。

**基本过程：Producer端压缩、Broker端保持、Consumer端解压缩。**

注意：除了在Consumer端解压缩，Broker端也会进行解压缩。每个压缩过的消息集合在Broker端写入时都要发生解压缩操作，目的就是为了对消息执行各种验证。我们必须承认这种解压缩对Broker端性能是有一定影响的，特别是对CPU的使用率而言。

#### 压缩算法

对于Kafka而言，在吞吐量方面：LZ4 > Snappy > zstd和GZIP；而在压缩比方面，zstd > LZ4 > GZIP > Snappy。

具体到物理资源，使用Snappy算法占用的网络带宽最多，zstd最少；

在CPU使用率方面，各个算法表现得差不多，只是在压缩时Snappy算法使用的CPU较多一些，而在解压缩时GZIP算法则可能使用更多的CPU。

## 消费者组

### 概念

- 消费者组内有多个消费者或消费者实例，它们共享一个公共的ID，这个ID被称为Group ID。组内的所有消费者协调在一起来消费订阅主题的所有分区。

- 主题中的每个分区只能由同一个消费者组内的一个Consumer实例来消费，组内其他消费者实例不能消费该分区

  一个主题可以配置几个分区，生产者发送的消息分发到不同的分区中，消费者接收数据的时候是按照消费者组来接收的，Kafka确保每个分区的消息只能被同一个消费者组中的一个消费者消费，如果想要重复消费，那么需要其他的消费者组来消费，同时一个消费者可以消费多个分区。

- Kafka使用Consumer Group机制，同时实现了传统消息引擎系统的两大模型：
  - 如果所有实例都属于同一个Group，那么它实现的就是消息队列模型；
  - 如果所有实例分别属于不同的Group，那么它实现的就是发布/订阅模型。

**Consumer Group是Kafka提供的可扩展且具有容错性的消费者机制**。

### 特性

- Consumer Group下可以有一个或多个Consumer实例，这里的实例可以是一个单独的进程，也可以是同一进程下的线程。
- Group ID是一个字符串，在一个Kafka集群中，它标识唯一的一个Consumer Group。
- Consumer Group下所有实例订阅的主题的单个分区，只能分配给组内的某个Consumer实例消费，这个分区当然也可以被其他的Group消费。

当Consumer Group订阅了多个主题后，组内的每个实例不要求一定要订阅主题的所有分区，它只会消费部分分区中的消息。

Consumer Group之间彼此独立，互不影响，它们能够订阅相同的一组主题而互不干涉。

### 消费者实例

**实例个数**

一个Group下该有多少个Consumer实例呢？

**理想情况下，Consumer实例的数量应该等于该Group订阅主题的分区总数。**

假设一个Consumer Group订阅了3个主题，分别是A、B、C，它们的分区数依次是1、2、3，那么通常情况下，为该Group设置6个Consumer实例是比较理想的情形，因为它能最大限度地实现高伸缩性。

**位移Offset**

老版本的Consumer Group把位移保存在ZooKeeper中。Apache ZooKeeper是一个分布式的协调服务框架，Kafka重度依赖它实现各种各样的协调管理。将位移保存在ZooKeeper外部系统的做法，最显而易见的好处就是减少了Kafka Broker端的状态保存开销。不过，慢慢地发现了一个问题，即ZooKeeper这类元框架其实并不适合进行频繁的写更新，而Consumer Group的位移更新却是一个非常频繁的操作。这种大吞吐量的写操作会极大地拖慢ZooKeeper集群的性能。

于是，在新版本的Consumer Group中，Kafka社区重新设计了Consumer Group的位移管理方式，采用了将位移保存在Kafka内部主题的方法。这个内部主题就是 __consumer_offsets 。

### 消费者策略

**Round**

默认，也叫轮循，说的是对于同一组消费者来说，使用轮训分配的方式，决定消费者消费的分区

<img src="img_Kafka/weixin-kafkahxzszj-1262b5a7-198e-47b7-ad81-a4f84e755901.jpg" alt="img" style="zoom: 50%;" />

**Range**

决定消费方式是以分区总数除以消费者总数来决定，一般如果不能整除，往往是从头开始将剩余的分区分配开

<img src="img_Kafka/weixin-kafkahxzszj-d6a295aa-85f7-439e-9984-e081f3d95238.jpg" alt="img" style="zoom: 50%;" />



**Sticky**

前面两个当同组内有新的消费者加入或者旧的消费者退出的时候，会从新开始决定消费者消费方式

Sticky，在同组中有新的新的消费者加入或者旧的消费者退出时，不会直接开始新的Range分配，而是保留现有消费者原来的消费策略，将退出的消费者所消费的分区平均分配给现有消费者，新增消费者同理，同其他现存消费者的消费策略中分离

### 重平衡

**（重平衡）Rebalance本质上是一种协议，规定了一个Consumer Group下的所有Consumer如何来分配订阅Topic的每个分区**。

比如某个Group下有20个Consumer实例，它订阅了一个具有100个分区的Topic。正常情况下，Kafka平均会为每个Consumer分配5个分区。这个分配的过程就叫Rebalance。

**Rebalance的触发条件**

- 组成员数发生变更。比如有新的 Consumer 实例加入组或者离开组，或是有 Consumer 实例崩溃被踢出组。
- 订阅主题数发生变更。Consumer Group 可以使用正则表达式的方式订阅主题，比如`consumer.subscribe(Pattern.compile(“t.*c”))`就表明该 Group 订阅所有以字母 t 开头、字母 c 结尾的主题，在 Consumer Group 的运行过程中，你新创建了一个满足这样条件的主题，那么该 Group 就会发生 Rebalance。
- 订阅主题的分区数发生变更。Kafka 当前只能允许增加主题的分区数，当分区数增加时，就会触发订阅该主题的所有 Group 开启 Rebalance。

Rebalance 发生时，Group 下所有的 Consumer 实例都会协调在一起共同参与。

**分配策略**

当前Kafka默认提供了3种分配策略，保证提供最公平的分配策略，即每个Consumer实例都能够得到较为平均的分区数。

比如一个Group内有10个Consumer实例，要消费100个分区，理想的分配策略自然是每个实例平均得到10个分区。这就叫**公平的分配策略**。

举个简单的例子来说明一下Consumer Group发生Rebalance的过程。假设目前某个Consumer Group下有两个Consumer，比如A和B，当第三个成员C加入时，Kafka会触发Rebalance，并根据默认的分配策略重新为A、B和C分配分区，Rebalance之后的分配依然是公平的，即每个Consumer实例都获得了2个分区的消费权。

在Rebalance过程中，所有Consumer实例都会停止消费，等待Rebalance完成，这是Rebalance为人诟病的一个方面。

目前Rebalance的设计是所有Consumer实例共同参与，全部重新分配所有分区。

**Coordinator会在什么情况下认为某个Consumer实例已挂从而要退组呢？**

当 Consumer Group 完成 Rebalance 之后，每个 Consumer 实例都会定期地向 Coordinator 发送**心跳请求**，表明它还存活着。如果某个 Consumer 实例不能及时地发送这些心跳请求，Coordinator 就会认为该 Consumer 已经死了，从而将其从 Group 中移除，然后开启新一轮 Rebalance。

- Consumer 端有个参数，`session.timeout.ms`。该参数的默认值是10秒，即如果 Coordinator 在10秒之内没有收到 Group 下某 Consumer 实例的心跳，它就会认为这个 Consumer 实例已经挂了。

- Consumer 还提供了控制发送心跳请求频率的参数，就是`heartbeat.interval.ms`。这个值设置得越小，Consumer 实例发送心跳请求的频率就越高。频繁地发送心跳请求会额外消耗带宽资源，但好处是能够更加快速地知晓当前是否开启 Rebalance，因为，目前 Coordinator 通知各个 Consumer 实例开启Rebalance 的方法，就是将`REBALANCE_NEEDED`标志封装进心跳请求的响应体中。

- 除了以上两个参数，Consumer 端还有一个参数，用于控制 Consumer 实际消费能力对 Rebalance 的影响，即`max.poll.interval.ms`参数。它限定了 Consumer 端应用程序两次调用 poll 方法的最大时间间隔。它的默认值是5分钟，表示你的 Consumer 程序如果在5分钟之内无法消费完 poll 方法返回的消息，那么 Consumer 会主动发起离开组的请求，Coordinator 也会开启新一轮 Rebalance。

**可避免Rebalance的配置**

- 第一类Rebalance是因为未能及时发送心跳，导致 Consumer 被踢出 Group 而引发的，因此可以设置 session.timeout.ms 和 heartbeat.interval.ms 的值。
  - 设置`session.timeout.ms` = 6s。
  - 设置`heartbeat.interval.ms` = 2s。

  要保证Consumer实例在被判定为dead之前，能够发送至少3轮的心跳请求，即`session.timeout.ms >= 3 * heartbeat.interval.ms`。

  将`session.timeout.ms`设置成6s主要是为了让Coordinator能够更快地定位已经挂掉的Consumer。

- 第二类Rebalance是Consumer消费时间过长导致的

  你要为你的业务处理逻辑留下充足的时间，这样Consumer就不会因为处理这些消息的时间太长而引发Rebalance了。

### ConsumerOffsets

`__consumer_offsets`的主要作用是**保存Kafka消费者的位移信息**。Kafka 将 Consumer 的位移数据作为一条条普通的 Kafka 消息，提交到`__consumer_offsets`中。它要求这个提交过程不仅要实现高持久性，还要支持高频的写操作。`__consumer_offsets`主题是**普通的Kafka主题**。它的消息格式是Kafka自己定义的，用户不能修改，也就是说你不能随意地向这个主题写消息，因为一旦你写入的消息不满足Kafka规定的格式，那么Kafka内部无法成功解析，就会造成Broker的崩溃。

**消息类型**

`__consumer_offsets`有3种消息格式：

- 用于保存 Consumer Group 信息的消息。
- 用于删除 Group 过期位移甚至是删除 Group 的消息。
- 保存了位移值。

第2种格式它有个专属的名字：tombstone 消息，即墓碑消息，也称 delete mark，它的主要特点是它的消息体是 null，即空消息体。

一旦某个 Consumer Group 下的所有 Consumer 实例都停止了，而且它们的位移数据都已被删除时，Kafka 会向`__consumer_offsets`主题的对应分区写入tombstone 消息，表明要彻底删除这个 Group 的信息。

**提交位移**

当 Kafka 集群中的**第一个 Consumer 程序启动时，Kafka 会自动创建位移主题**。默认该主题的分区数是50，副本数是3。

目前 Kafka Consumer 提交位移的方式有两种：自动提交位移和手动提交位移，详情见下个目录。

如果你选择的是自动提交位移，那么就可能存在一个问题：只要 Consumer 一直启动着，它就会无限期地向位移主题写入消息。假设Consumer当前消费到了某个主题的最新一条消息，位移是100，之后该主题没有任何新消息产生，故Consumer无消息可消费了，所以位移永远保持在100。由于是自动提交位移，位移主题中会不停地写入位移=100的消息。显然Kafka只需要保留这类消息中的最新一条就可以了，之前的消息都是可以删除的。

这就要求Kafka必须要有针对位移主题消息特点的消息删除策略，否则这种消息会越来越多，最终撑爆整个磁盘。

**Compact策略**

Kafka使用 Compact 策略来删除`__consumer_offsets`主题中的过期消息，避免该主题无限期膨胀。Compact的过程就是扫描日志的所有消息，剔除那些过期的消息，然后把剩下的消息整理在一起。

比如对于同一个Key的两条消息M1和M2，如果M1的发送时间早于M2，那么M1就是过期消息。

Compact过程如下图，图中位移为0、2和3的消息的Key都是K1，Compact之后，分区只需要保存位移为3的消息，因为它是最新发送的。

![img](img_Kafka/weixin-kafkahxzszj-14efc721-5848-4931-80f3-b8a52c9a7816.jpg)



**Kafka提供了专门的后台线程定期地巡检待Compact的主题，看看是否存在满足条件的可删除数据**。

这个后台线程叫Log Cleaner。很多实际生产环境中都出现过位移主题无限膨胀占用过多磁盘空间的问题，如果你的环境中也有这个问题，建议你去检查一下Log Cleaner线程的状态，通常都是这个线程挂掉了导致的。

### 位移提交

假设一个分区中有10条消息，位移分别是0到9。某个Consumer应用已消费了5条消息，这就说明该Consumer消费了位移为0到4的5条消息，此时Consumer的位移是5，指向了下一条消息的位移。

因为Consumer能够同时消费多个分区的数据，所以位移的提交实际上是在分区粒度上进行的，即**Consumer需要为分配给它的每个分区提交各自的位移数据**。

**位移提交分为自动提交和手动提交；从Consumer端的角度来说，位移提交分为同步提交和异步提交**。

#### 自动提交

Consumer端参数`enable.auto.commit`，设置为 true 可以开启自动提交位移，默认值 true，即 Java Consumer 默认就是自动提交位移的。

如果启用了自动提交，Consumer 端参数：`auto.commit.interval.ms`，它的默认值是5秒，表明 Kafka 每5秒会为你自动提交一次位移。

```java
Properties props = new Properties();
props.put("bootstrap.servers", "localhost:9092");
props.put("group.id", "test");
props.put("enable.auto.commit", "true");
props.put("auto.commit.interval.ms", "2000");
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
KafkaConsumer consumer = new KafkaConsumer<>(props);
consumer.subscribe(Arrays.asList("foo", "bar"));
while (true) {
    ConsumerRecords records = consumer.poll(100);
    for (ConsumerRecord record : records)
        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
}
```

自动提交 Kafka 会保证在开始调用 poll 方法时，提交上次 poll 返回的所有消息。从顺序上来说，poll 方法的逻辑是先提交上一批消息的位移，再处理下一批消息，因此它能保证不出现消费丢失的情况。

但自动提交位移的一个问题在于，**「它可能会出现重复消费」**。

#### 手动提交

开启手动提交位移的方法就是设置`enable.auto.commit为false`。还需要调用相应的API手动提交位移。

**同步操作**

**KafkaConsumer#commitSync()**

该方法会提交`KafkaConsumer#poll()`返回的最新位移。该方法会一直等待，直到位移被成功提交才会返回，如果提交过程中出现异常，该方法会将异常信息抛出。调用commitSync()时，Consumer程序会处于阻塞状态，直到远端的Broker返回提交结果，这个状态才会结束。

```java
while (true) {
    ConsumerRecords records = consumer.poll(Duration.ofSeconds(1));
    process(records); // 处理消息
    try {
        consumer.commitSync();
    } catch (CommitFailedException e) {
        handle(e); // 处理提交失败异常
    }
}
```

**异步操作**

**KafkaConsumer#commitAsync()**。

调用commitAsync()之后，它会立即返回，不会阻塞，Kafka还提供了回调函数（callback），供你实现提交之后的逻辑，比如记录日志或处理异常等。

```java
while (true) {
    ConsumerRecords records = consumer.poll(Duration.ofSeconds(1));
    process(records); // 处理消息
    consumer.commitAsync((offsets, exception) -> {
        if (exception != null)
            handle(exception);
    });
}
```

commitAsync的问题在于，出现问题时它不会自动重试。

**组合使用**

显然，如果是手动提交，我们需要将commitSync和commitAsync组合使用才能到达最理想的效果，原因有两个：

1. 我们可以利用commitSync的自动重试来规避那些瞬时错误，比如网络的瞬时抖动，Broker端GC等，因为这些问题都是短暂的，自动重试通常都会成功。
2. 我们不希望程序总处于阻塞状态，影响TPS。

```java
// 将两个API方法结合使用进行手动提交
try {
    while(true) {
        ConsumerRecords records = consumer.poll(Duration.ofSeconds(1));
        process(records); // 处理消息
        commitAysnc(); // 使用异步提交规避阻塞
    }
} catch(Exception e) {
    handle(e); // 处理异常
} finally {
    try {
        consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
    } finally {
        consumer.close();
    }
}
```

#### 阶段提交

这样一个场景：你的poll方法返回的不是500条消息，而是5000条。那么，你肯定不想把这5000条消息都处理完之后再提交位移，因为一旦中间出现差错，之前处理的全部都要重来一遍。比如前面这个5000条消息的例子，你可能希望**每处理完100条消息就提交一次位移**，这样能够避免大批量的消息重新消费。

Kafka Consumer API为手动提交提供了这样的方法：commitSync(Map)和commitAsync(Map)。它们的参数是一个Map对象，键就是TopicPartition，即消费的分区，而值是一个OffsetAndMetadata对象，保存的主要是位移数据。

```java
// 以commitAsync为例，commitSync的调用方法和它是一样的
private Map offsets = new HashMap<>();
int count = 0;
……
while (true) {
    ConsumerRecords records = consumer.poll(Duration.ofSeconds(1));
    for (ConsumerRecord record: records) {
        process(record);  // 处理消息
        offsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
        if(count % 100 == 0) {
            consumer.commitAsync(offsets, null); // 回调处理逻辑是null
        }
        count++;
	}
}
```

先创建一个Map对象，用于保存Consumer消费处理过程中要提交的分区位移，之后开始逐条处理消息，并构造要提交的位移值。最后是做位移的提交。设置了一个计数器，每累计100条消息就统一提交一次位移。

与调用无参的commitAsync不同，这里调用了带Map对象参数的commitAsync进行细粒度的位移提交。这样，这段代码就能够实现每处理100条消息就提交一次位移，不用再受poll方法返回的消息总数的限制了。

### Coordinator

> 协调器是用于协调多个消费者之间能够正确地工作的一个角色, 比如计算**消费的分区分配策略**，又或者消费者的加入组与离开组的处理逻辑, 有一点类似 Kafka的控制器的角色。

协调器分为 **消费组协调器** 和 **消费者协调器**两种

- 消费组协调器

  组协调器(GroupCoordinator)可以理解为各个消费者协调器的一个中央处理器, 每个消费者的所有交互都是和组协调器(GroupCoordinator)进行的。

  - 选举Leader消费者客户端
  - 处理申请加入组的客户端
  - 再平衡后同步新的分配方案
  - 维护与客户端的心跳检测
  - 管理消费者已消费偏移量,并存储至__consumer_offset中

- 消费者协调器

  每个客户端都会有一个消费者协调器, 他的主要作用就是向组协调器发起请求做交互, 以及处理回调逻辑

  - 向组协调器发起入组请求
  - 向组协调器发起同步组请求(如果是Leader客户端,则还会计算分配策略数据放到入参传入)
  - 发起离组请求
  - 保持跟组协调器的心跳线程
  - 向组协调器发送提交已消费偏移量的请求

所有Broker在启动时，都会创建和开启相应的Coordinator组件，**所有Broker都有各自的Coordinator组件**

Consumer 端应用程序在提交位移时，其实是向 Coordinator 所在的 Broker 提交位移，同样地，当 Consumer 应用启动时，也是向 Coordinator 所在的 Broker 发送各种请求，然后由 Coordinator 负责执行消费者组的注册、成员管理记录等元数据管理操作。

Consumer Group 如何确定为它服务的 Coordinator 在哪台 Broker 上呢？通过Kafka内部主题 `__consumer_offsets` 。默认情况下, `__consumer_offset`有50个分区，每个消费组都会对应其中的一个分区。目前，Kafka 为某个 Consumer Group 确定 Coordinator 所在的 Broker 的算法有2个步骤。

1. 第1步：确定由 __consumer_offsets 主题的哪个分区来保存该 Group 内实例的位移数据：`partitionId=Math.abs(groupId.hashCode() % offsetsTopicPartitionCount)`。
2. 第2步：找出该分区 Leader 副本所在的 Broker ，该 Broker 即为对应的 Coordinator 。

> 首先，Kafka会计算该Group的 group.id 参数的哈希值。比如你有个Group的 group.id 设置成了 test-group ，那么它的 hashCode 值就应该是627841412。
>
> 其次，Kafka会计算 `__consumer_offsets` 的分区数，通常是50个分区，之后将刚才那个哈希值对分区数进行取模加求绝对值计算，即`abs(627841412 % 50) = 12`。此时，我们就知道了 `__consumer_offsets` 主题的分区12负责保存这个 Group 的数据。
>
> 有了分区号，我们只需要找出 __consumer_offsets 主题分区12的 Leader 副本在哪个 Broker上 就可以了，这个 Broker ，就是我们要找的 Coordinator。

反过来说，一个消费者组内所有消费者的位移数据都保存到 __consumer_offsets 主题的一个分区中，可以通过这个分区的领导者副本确认 Coordinator 所在的 Broker 。

##  副本机制

根据Kafka副本机制的定义，同一个分区的所有副本保存有相同的消息序列，这些副本分散保存在不同的Broker上，从而能够对抗部分Broker宕机带来的数据不可用。

下面展示的是一个有3台Broker的Kafka集群上的副本分布情况。

从这张图中，我们可以看到，主题1分区0的3个副本分散在3台Broker上，其他主题分区的副本也都散落在不同的Broker上，从而实现数据冗余。

<img src="img_Kafka/weixin-kafkahxzszj-b2a22704-3d2e-4e1d-8f43-0dfa869ff8a4.jpg" alt="img" style="zoom: 67%;" />

### 副本角色

<img src="img_Kafka/weixin-kafkahxzszj-804bdef2-6281-4dd6-bee7-ec3361cff781.jpg" alt="img" style="zoom: 50%;" />

在Kafka中，副本分成两类：领导者副本（Leader Replica）和追随者副本（Follower Replica）。每个分区在创建时都要选举一个副本，称为领导者副本，其余的副本自动称为追随者副本。kafka使用多副本机制提高可靠性，但是只有leader副本对外提供读写服务，follow副本只是做消息同步。

- 所有的请求都必须由**领导者副本**来处理，或者说，所有的读写请求都必须发往领导者副本所在的Broker，由该Broker负责处理。

- 在Kafka中，**追随者副本**是不对外提供服务的。这就是说，任何一个追随者副本都不能响应消费者和生产者的读写请求。追随者副本不处理客户端请求，它唯一的任务就是从领导者副本**异步拉取**消息，并写入到自己的提交日志中，从而实现与领导者副本的同步。

- 当领导者副本挂掉了，或者说领导者副本所在的Broker宕机时，Kafka依托于ZooKeeper提供的监控功能能够实时感知到，并立即开启新一轮的领导者选举，从追随者副本中选一个作为新的领导者。老Leader副本重启回来后，只能作为追随者副本加入到集群中。

> 对于客户端用户而言，Kafka的追随者副本没有任何作用，Kafka为什么要这样设计呢？
>
> 这种副本机制有两个方面的好处。
>
> - 方便实现Read-your-writes
>
>   所谓Read-your-writes，顾名思义就是，当你使用生产者API向Kafka成功写入消息后，马上使用消费者API去读取刚才生产的消息。
>
> - 方便实现单调读（Monotonic Reads）
>
>   假设当前有2个追随者副本F1和F2，它们异步地拉取领导者副本数据。倘若F1拉取了Leader的最新消息而F2还未及时拉取，那么，此时如果有一个消费者先从F1读取消息之后又从F2拉取消息，它可能会看到这样的现象：第一次消费时看到的最新消息在第二次消费时不见了，这就不是单调读一致性。
>
>   但是，如果所有的读请求都是由Leader来处理，那么Kafka就很容易实现单调读一致性。

### ISR机制

In-sync Replicas，也就是所谓的ISR副本集合。

- ISR中的副本都是与Leader同步的副本，相反，不在ISR中的追随者副本就被认为是与Leader不同步的。

  **ISR不只是追随者副本集合，它必然包括Leader副本。甚至在某些情况下，ISR只有Leader这一个副本**。

- 能够进入到ISR的追随者副本要满足一定的条件。**通过Broker端参数replica.lag.time.max.ms参数值**。这个参数的含义是Follower副本能够落后Leader副本的最长时间间隔，当前默认值是10秒。

  这就是说，只要一个Follower副本落后Leader副本的时间不连续超过10秒，那么Kafka就认为该Follower副本与Leader是同步的，即使此时Follower副本中保存的消息明显少于Leader副本中的消息。

- ISR是一个动态调整的集合，而非静态不变的。Follower副本唯一的工作就是不断地从Leader副本拉取消息，然后写入到自己的提交日志中。倘若该副本后面慢慢地追上了Leader的进度，那么它是能够重新被加回ISR的。

### Unclean领导者选举

**「Kafka把所有不在ISR中的存活副本都称为非同步副本」**。

- 在Kafka中，非同步副本参与选举的过程称为Unclean领导者选举。**Broker端参数unclean.leader.election.enable控制是否允许Unclean领导者选举**

- 非同步副本中保存的消息远远落后于Leader中的消息。因此，如果选择这些副本作为新Leader，就可能出现数据的丢失。

  开启Unclean领导者选举可能会造成数据丢失，但好处是，它使得分区Leader副本一直存在，不至于停止对外提供服务，因此提升了高可用性。反之，禁止Unclean领导者选举的好处在于维护了数据的一致性，避免了消息丢失，但牺牲了高可用性。

### 副本选举

如果一个分区的leader副本不可用，就意味着整个分区不可用，此时需要从follower副本中选举出新的leader副本提供服务。

- **kafka集群中会尽量分配每一个partition的副本leader在不同的broker中**，这样会避免多个leader在同一个broker，导致集群中的broker负载不平衡。对于任意的topic的分区以及副本leader的设定，都需要考虑到集群整体的负载能力的平衡性。

- 在创建主题的时候，该主题的分区和副本会尽可能的均匀发布到kafka的各个broker上。

  比如我们在包含3个broker节点的kafka集群上创建一个分区数为3，副本因子为3的主题`topic-partitions`时，leader副本会均匀的分布在3台broker节点上。

<img src="img_Kafka/weixin-kafkahxzszj-1dfdbca2-372c-4bcf-9f9c-d58a94f32477.jpg" alt="img" style="zoom: 67%;" />

- **针对同一个分区，一个broker节点上不可能出现它的多个副本**。

#### 优先副本选举

我们可以把leader副本所在的节点叫作分区的leader节点，把follower副本所在的节点叫作follower节点。在上面的例子中，分区0的leader节点是broker1，分区1的leader节点是broker2，分区2的leader节点是broker0。

当分区leader节点发生故障时，其中的一个follower节点就会选举为新的leader节点。当原来leader的节点恢复之后，它只能成为一个follower节点，此时就导致了集群负载不均衡。

比如分区1的leader节点broker2崩溃了，此时选举了在broker1上的分区1follower节点作为新的leader节点。当broker2重新恢复时，此时的kafka集群状态如下：

<img src="img_Kafka/weixin-kafkahxzszj-6630b85d-2c0d-44ea-add9-aa1ea1d66ac8.jpg" alt="img" style="zoom: 67%;" />

可以看到，此时broker1上负载更大，而broker2上没有负载。

- 为了解决上述负载不均衡的情况，kafka支持了优先副本选举，**优先副本指的是一个分区的AR（分区中的所有副本）集合的第一个副本**。理想情况下，优先副本应该就是leader副本。

  比如上面的分区1，它的AR集合是`[2,0,1]`，表示分区1的优先副本就是在broker2上。

- **优先副本选举就是对分区leader副本进行选举的时候，尽可能让优先副本成为leader副本**，针对上述的情况，只要再触发一次优先副本选举就能保证分区负载均衡。

kafka支持自动优先副本选举功能，默认每5分钟触发一次优先副本选举操作



## 网络通信模型

<img src="img_Kafka/weixin-kafkahxzszj-8b040ee5-4ccc-4e81-b653-611c758c9899.jpg" alt="img" style="zoom:50%;" />

监听线程 -> 网络线程池 -> IO线程池 -> 网络线程池

1. Broker 中`Acceptor(mainReactor)`监听新连接的到来，与新连接**建连**之后轮询选择一个`Processor(subReactor)`管理这个连接。

   每个`listener`只有一个`Acceptor线程`，因为它只是作为新连接建连再分发，没有过多的逻辑，很轻量。

2. 而`Processor`会**监听其管理的连接**，当事件到达之后，读取封装成`Request`，并将`Request`放入共享请求队列中。

   `Processor` 在Kafka中称之为网络线程，默认网络线程池有3个线程，对应的参数是`num.network.threads`，并且可以根据实际的业务动态增减。

3. 然后IO线程池不断的从该队列中取出请求，执行真正的**处理**。

    IO 线程池，即`KafkaRequestHandlerPool`，执行真正的处理，对应的参数是`num.io.threads`，默认值是 8。

4. 处理完之后将响应`Response`发送到对应的`Processor`的响应队列中，然后由`Processor`将`Response`返还给客户端。

可以看到网络线程和IO线程之间利用的经典的生产者 - 消费者模式，不论是用于处理Request的共享请求队列，还是IO处理完返回的Response。

## 幂等性

**幂等性Producer**

- 是0.11.0.0版本引入的新功能，在此之前，Kafka向分区发送数据时，可能会出现同一条消息被发送了多次，导致消息重复的情况。

- 在Kafka中，Producer默认不是幂等性的，但我们可以创建幂等性Producer。需要设置一个参数：`enable.idempotence`，被设置成true后，Producer自动升级成幂等性Producer，其他所有的代码逻辑都不需要改变。Kafka自动帮你做消息的重复去重。

  ```java
  props.put(“enable.idempotence”, true);
  // 或
  props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG， true);
  ```

- 底层具体的原理很简单，就是经典的用空间去换时间的优化思路，即在Broker端多保存一些字段。当Producer发送了具有相同字段值的消息后，Broker能够自动知晓这些消息已经重复了，于是可以在后台默默地把它们丢弃掉。

**幂等性Producer的作用范围**

首先，它只能保证**单分区**上的幂等性，即一个幂等性Producer能够保证某个主题的一个分区上不出现重复消息，它无法实现多个分区的幂等性。

其次，它只能实现**单会话**上的幂等性，不能实现跨会话的幂等性。这里的会话，你可以理解为Producer进程的一次运行，当你重启了Producer进程之后，这种幂等性保证就丧失了。

## 事务

Kafka自0.11版本开始也提供了对事务的支持，默认为 read committed 隔离级别。

它能保证多条消息原子性地写入到目标分区，同时也能保证 Consumer 只能看到事务成功提交的消息。

**事务型Producer**

事务型 Producer 能够保证将消息原子性地写入到多个分区中。这批消息要么全部写入成功，要么全部失败，另外，事务型 Producer 也不惧进程的重启。Producer 重启回来后，Kafka 依然保证它们发送消息的精确一次处理。

设置事务型 Producer 的方法也很简单，满足两个要求即可：

- 和幂等性 Producer 一样，开启`enable.idempotence = true`
- 设置 Producer 端参数`transactional. id`

```java
// Producer代码
producer.initTransactions();
try {
    producer.beginTransaction();
    producer.send(record1);
    producer.send(record2);
    producer.commitTransaction();
} catch (KafkaException e) {
	producer.abortTransaction();
}
```

事务型 Producer 调用了一些事务API，initTransaction、beginTransaction、commitTransaction和abortTransaction，它们分别对应事务的初始化、事务开始、事务提交以及事务终止。

这段代码能够保证Record1和Record2被当作一个事务统一提交到Kafka，要么它们全部提交成功，要么全部写入失败。

实际上即使写入失败，Kafka也会把它们写入到底层的日志中，也就是说Consumer还是会看到这些消息。

**事务级别**

`isolation.level`参数，这个参数有两个取值：

- `read_uncommitted`：这是默认值，表明 Consumer 能够读取到 Kafka 写入的任何消息，不论事务型 Producer 提交事务还是终止事务，其写入的消息都可以读取，如果你用了事务型 Producer，那么对应的 Consumer 就不要使用这个值。

- `read_committed`：表明 Consumer 只会读取事务型 Producer 成功提交事务写入的消息，它也能看到非事务型 Producer 写入的所有消息。

## 拦截器

**Kafka拦截器分为生产者拦截器和消费者拦截器**

- 生产者拦截器允许你在发送消息前以及消息提交成功后植入你的拦截器逻辑，而消费者拦截器支持在消费消息前以及提交位移后编写特定逻辑。

- 可以将一组拦截器串连成一个大的拦截器，Kafka会按照添加顺序依次执行拦截器逻辑。

**实现：**

当前Kafka拦截器的设置方法是通过参数配置完成的，生产者和消费者两端有一个相同的参数`interceptor.classes`，它指定的是一组类的列表，每个类就是特定逻辑的拦截器实现类。

```java
Properties props = new Properties(); 
List interceptors = new ArrayList<>(); 
interceptors.add("com.yourcompany.kafkaproject.interceptors.AddTimestampInterceptor"); // 拦截器1 
interceptors.add("com.yourcompany.kafkaproject.interceptors.UpdateCounterInterceptor"); // 拦截器2 
props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors); 
…… 
```

Producer端拦截器实现类都要继承`org.apache.kafka.clients.producer.ProducerInterceptor`接口，里面有两个核心的方法。

1. onSend：该方法会在消息发送之前被调用。
2. onAcknowledgement：该方法会在消息成功提交或发送失败之后被调用。onAcknowledgement的调用要早于callback的调用。值得注意的是，这个方法和onSend不是在同一个线程中被调用的，因此如果你在这两个方法中调用了某个共享可变对象，一定要保证线程安全。

消费者拦截器实现类要实现`org.apache.kafka.clients.consumer.ConsumerInterceptor`接口，这里面也有两个核心方法。

1. onConsume：该方法在消息返回给 Consumer 程序之前调用。
2. onCommit：Consumer 在提交位移之后调用该方法。通常你可以在该方法中做一些记账类的动作，比如打日志等。

一定要注意的是，**「指定拦截器类时要指定它们的全限定名」**。通俗点说就是要把完整包名也加上，不要只有一个类名在那里，并且还要保证你的Producer程序能够正确加载你的拦截器类



## 控制器

控制器组件（Controller），它的主要作用是在Apache ZooKeeper的帮助下管理和协调整个Kafka集群（**管理Kafka集群**）。

集群中任意一台Broker都能充当控制器的角色，但是，在运行过程中，只能有一个Broker成为控制器，行使其管理和协调的职责。

Kafka控制器大量使用ZooKeeper的Watch功能实现对集群的协调管理。

**控制器选举：**

实际上，Broker在启动时，会尝试去ZooKeeper中创建`/controller`节点。Kafka当前选举控制器的规则是：**「第一个成功创建/controller节点的Broker会被指定为控制器」**。

**控制器职责：**

大致可以分为5种：

- 主题管理（创建、删除、增加分区）

  控制器帮助我们完成对Kafka主题的创建、删除以及分区增加的操作。

- 分区重分配

- Preferred领导者选举

  Preferred领导者选举主要是Kafka为了避免部分Broker负载过重而提供的一种换Leader的方案。

- 集群成员管理（新增Broker、Broker主动关闭、Broker宕机）

  包括自动检测新增Broker、Broker主动关闭及被动宕机。这种自动检测是依赖于Watch功能和ZooKeeper临时节点组合实现的。

  比如，控制器组件会利用**「Watch机制」**检查ZooKeeper的`/brokers/ids`节点下的子节点数量变更。目前，当有新Broker启动后，它会在`/brokers`下创建专属的znode节点。一旦创建完毕，ZooKeeper会通过Watch机制将消息通知推送给控制器，这样，控制器就能自动地感知到这个变化，进而开启后续的新增Broker作业。

  侦测Broker存活性则是依赖于刚刚提到的另一个机制：**「临时节点」**。每个Broker启动后，会在`/brokers/ids`下创建一个临时znode。当Broker宕机或主动关闭后，该Broker与ZooKeeper的会话结束，这个znode会被自动删除。同理，ZooKeeper的Watch机制将这一变更推送给控制器，这样控制器就能知道有Broker关闭或宕机了，从而进行善后。

- 数据服务

  控制器上保存了最全的集群元数据信息，其他所有Broker会定期接收控制器发来的元数据更新请求，从而更新其内存中的缓存数据。

  **控制器故障转移（Failover）：**

  **故障转移指的是，当运行中的控制器突然宕机或意外终止时，Kafka能够快速地感知到，并立即启用备用控制器来代替之前失败的控制器**。这个过程就被称为Failover，该过程是自动完成的，无需你手动干预。

  <img src="img_Kafka/weixin-kafkahxzszj-d9a4e4d3-0045-4755-87a4-4da396c7ad9f.jpg" alt="img" style="zoom: 67%;" />

  最开始时，Broker 0是控制器。当Broker 0宕机后，ZooKeeper通过Watch机制感知到并删除了`/controller`临时节点。之后，所有存活的Broker开始竞选新的控制器身份。Broker 3最终赢得了选举，成功地在ZooKeeper上重建了`/controller`节点。之后，Broker 3会从ZooKeeper中读取集群元数据信息，并初始化到自己的缓存中。至此，控制器的Failover完成，可以行使正常的工作职责了。

## 日志存储

Kafka中的消息是以主题为基本单位进行归类的，每个主题在逻辑上相互独立。每个主题又可以分为一个或多个分区，在不考虑副本的情况下，一个分区会对应一个日志。

但设计者考虑到随着时间推移，日志文件会不断扩大，因此为了防止Log过大，设计者引入了日志分段（LogSegment）的概念，将Log切分为多个LogSegment，便于后续的消息维护和清理工作。

下图描绘了主题、分区、副本、Log、LogSegment五者之间的关系。

<img src="img_Kafka/weixin-kafkahxzszj-da2e2224-c452-40d2-b1e6-9719e5520954.jpg" alt="img" style="zoom:67%;" />

**LogSegment：**

在Kafka中，每个Log对象又可以划分为多个LogSegment文件，每个LogSegment文件包括一个日志数据文件和两个索引文件（偏移量索引文件和消息时间戳索引文件）。

其中，每个LogSegment中的日志数据文件大小均相等（该日志数据文件的大小可以通过在Kafka Broker的`config/server.properties`配置文件的中的**log.segment.bytes**进行设置，默认为1G大小（1073741824字节），在顺序写入消息时如果超出该设定的阈值，将会创建一组新的日志数据和索引文件）。

![img](img_Kafka/weixin-kafkahxzszj-57d8c857-af51-4d68-9dcb-86c96ba3cb8e.jpg)































## 安装

**步骤：**

1. 解压kafka_2.12­2.3.0.tgz

   ```sh
   cd ~/software
   tar -zxf kafka_2.12-2.3.0.tgz
   ```

2. 配置

   ```sh
   # 创建存放数据的文件夹
   cd kafka_2.12-2.3.0
   mkdir data
   # 修改kafka配置文件
   cd config
   vi server.properties
   	#listeners=PLAINTEXT://:9092 # kafka默认监听的端口号为9092
   	log.dirs=../data # 指定数据的存放目录
   	zookeeper.connect=localhost:2181 # zookeeper的连接信息
   ```

3. 启动zookeeper

   - 使用kafka内置zk

     ```sh
     cd ~/software/kafka_2.12-2.3.0/bin/
     ./zookeeper-server-start.sh ../config/zookeeper.properties
     ```

   - 使用外部zk(推荐)

     ```sh
     cd ~/software/zookeeper-3.4.13/bin/
     ./zkServer.sh start
     ```

4. 启动kafka

   ```sh
   ./kafka-server-start.sh ../config/server.properties & # &表示后台运行，也可使用-daemon选项
   jps # 查看进程，jps是jdk提供的用来查看所有java进程的命令
   ```

5. 创建Topic（主题）

   ```sh
   ./kafka-topics.sh \
   	--create \
   	--zookeeper localhost:2181 \
   	--replication-factor 1 \
   	--partitions 3 \
   	--topic hello
   ```

   ```sh
   # 查看Topic列表
   ./kafka-topics.sh --list --zookeeper localhost:2181 # __consumer_offsets是kafka的内部Topic
   # 查看某一个具体的Topic
   ./kafka-topics.sh --describe --zookeeper localhost:2181 --topic hello
   # 修改Topic：只能增加partition个数，不能减少，且不能修改replication-factor
   ./kafka-topics.sh --alter --zookeeper localhost:2181 --topic hello --partitions 5
   # 删除Topic (需要启用topic删除功能)
   ./kafka-topics.sh --delete --zookeeper localhost:2181 --topic hello
   ```

6. 启动kafka的Producer（生产者 ）

   ```sh
   ./kafka-console-producer.sh --broker-list localhost:9092 --topic hello
   ```

7. 启动kafka的Consumer（消费者）

   ```sh
   ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic hello --from-beginning
   ```

8. 验证

   查看data数据存放目录：Topic的每个Partition对应一个目录，数据存储在目录下的00000000000000000000.log文件中

   查看zookeeper中的内容：get /brokers/topics/hello/partitions/0/state

### 配置文件

```properties
############################# Server Basics #############################
# broker的id，值为整数，且必须唯一，在一个集群中不能重复
broker.id=0

############################# Socket Server Settings #############################
# kafka默认监听的端口为9092
#listeners=PLAINTEXT://:9092
# 处理网络请求的线程数量，默认为3个
num.network.threads=3
# 执行磁盘IO操作的线程数量，默认为8个
num.io.threads=8
# socket服务发送数据的缓冲区大小，默认100KB
socket.send.buffer.bytes=102400
# socket服务接受数据的缓冲区大小，默认100KB
socket.receive.buffer.bytes=102400
# socket服务所能接受的一个请求的最大大小，默认为100M
socket.request.max.bytes=104857600

############################# Log Basics #############################
# kafka存储消息数据的目录
log.dirs=../data
# 每个topic默认的partition数量
num.partitions=1
# 在启动时恢复数据和关闭时刷新数据时每个数据目录的线程数量
num.recovery.threads.per.data.dir=1

############################# Log Flush Policy #############################
# 消息刷新到磁盘中的消息条数阈值
#log.flush.interval.messages=10000
# 消息刷新到磁盘中的最大时间间隔
#log.flush.interval.ms=1000

############################# Log Retention Policy #############################
# 日志保留小时数，超时会自动删除，默认为7天
log.retention.hours=168
# 日志保留大小，超出大小会自动删除，默认为1G
#log.retention.bytes=1073741824
# 日志分片策略，单个日志文件的大小最大为1G，超出后则创建一个新的日志文件
log.segment.bytes=1073741824
# 每隔多长时间检测数据是否达到删除条件
log.retention.check.interval.ms=300000

############################# Zookeeper #############################
# Zookeeper连接信息，如果是zookeeper集群，则以逗号隔开
zookeeper.connect=localhost:2181
# 连接zookeeper的超时时间
zookeeper.connection.timeout.ms=6000
# 是否可以删除topic，默认为false
delete.topic.enable=true
```

## 集群搭建

### 搭建zk集群

可以在一台主机上启动多个zk服务，配置使用不同的端口即可

**步骤：**

1. 拷贝多个zk目录
    zookeeper1、zookeeper2、zookeeper3

2. 分别配置每个zk

   ```sh
   vi zookeeper1/conf/zoo.cfg
       clientPort=2181
       server.1=192.168.2.153:6661:7771
       server.2=192.168.2.153:6662:7772
       server.3=192.168.2.153:6663:7773
   echo 1 > zookeeper1/data/myid
   
   vi zookeeper2/conf/zoo.cfg
       clientPort=2182
       server.1=192.168.2.153:6661:7771
       server.2=192.168.2.153:6662:7772
       server.3=192.168.2.153:6663:7773
   echo 2 > zookeeper2/data/myid
   
   vi zookeeper3/conf/zoo.cfg
       clientPort=2183
       server.1=192.168.2.153:6661:7771
       server.2=192.168.2.153:6662:7772
       server.3=192.168.2.153:6663:7773
   echo 3 > zookeeper3/data/myid
   ```

3. 启动zk集群

### 搭建Kafka集群

**步骤：**

1. 拷贝多个kafka目录
    kafka1、kafka2、kafka3

2. 分别配置每个kafka

   ```sh
   vi kafka1/config/server.properties
       broker.id=1
       listeners=PLAINTEXT://192.168.2.153:9091
       zookeeper.connect=192.168.2.153:2181,192.168.2.153:2182,192.168.2.153:2183
   
   vi kafka2/config/server.properties
       broker.id=2
       listeners=PLAINTEXT://192.168.2.153:9092
       zookeeper.connect=192.168.2.153:2181,192.168.2.153:2182,192.168.2.153:2183
   
   vi kafka3/config/server.properties
       broker.id=3
       listeners=PLAINTEXT://192.168.2.153:9093
       zookeeper.connect=192.168.2.153:2181,192.168.2.153:2182,192.168.2.153:2183
   ```

3. 启动kafka集群

4. 创建Topic

   ```sh
   ./kafka-topics.sh \
       --create \
       --zookeeper 192.168.7.40:2181,192.168.7.40:2182,192.168.7.40:2183 \
       --replication-factor 3 \
       --partitions 5 \
       --topic aaa
   ```

5. 生成数据/发布消息

   ```sh
   ./kafka-console-producer.sh --broker-list
   192.168.7.40:9091,192.168.7.40:9092,192.168.7.40:9093 --topic aaa
   ```

6. 消费数据/订阅消息

   ```sh
   ./kafka-console-consumer.sh --bootstrap-server
   192.168.7.40:9091,192.168.7.40:9092,192.168.7.40:9093 --topic aaa --from-beginning
   ```

## SpringBoot集成Kafka

SpringBoot提供了一个名为 spring­kafka 的starter，用于在Spring项目里快速集成kafka

**步骤：**

1. 创建SpringBoot项目
    勾选Spring Web Starter和Spring for Apache Kafka

2. 配置kafka，编辑application.yml文件

   ```yml
   spring:
     kafka:
       # kafka服务器地址(可以多个)
       bootstrap-servers: 192.168.7.40:9091,192.168.7.40:9092,192.168.7.40:9093
       producer:
   	  # 每次批量发送消息的数量
         batch-size: 65536
         buffer-memory: 524288
         # key/value的序列化
         key-serializer: org.apache.kafka.common.serialization.StringSerializer
         value-serializer: org.apache.kafka.common.serialization.StringSerializer
       consumer:
         # 指定一个默认的组名
         group-id: test
         # key/value的反序列化
         key-deserializer:
           org.apache.kafka.common.serialization.StringDeserializer
         value-deserializer:
           org.apache.kafka.common.serialization.StringDeserializer
   ```

3. 创建生产者

   ```java
   @RestController
   public class KafkaProducer {
       
       @Autowired
       private KafkaTemplate template;
           
       /**
       * 发送消息到Kafka
       * @param topic 主题
       * @param message 消息
       */
       @RequestMapping("/sendMsg")
       public String sendMsg(String topic, String message) {
           template.send(topic, message);
           return "success";
       }
   }
   ```

4. 创建消费者

   ```java
   @Component
   public class KafkaConsumer{
       /**
       * 订阅指定主题的消息
       * @param record 消息记录
       */
       @KafkaListener(topics = {"hello","world"})
       
       public void listen(ConsumerRecord record) {
           // System.out.println(record);
           System.out.println(record.topic()+","+record.value());
       }
   }
   ```

5. 测试
访问http://localhost:8080/sendMsg?topic=hello&message=aaaa









## 临时笔记

实时数据流

分区是消息日志

每条消息在分区中的位置信息叫位移

多个消费者实例可以属于同一个 `Group`，也可以属于不 同的 `Group`，主题中的每个分区都只会被group内的一个消费者实例消费

消费者Group确定Coordinator所在的Broker：groupId哈希确认主题的哪个分区来保存该Group数据，找出该分区Leader副本所在的Broker





















