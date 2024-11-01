# Kafka实战





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



## 客户端API

AdminClient API：允许管理和检测Topic、broker以及其他Kafka对象

Producer API：发布消息到一个或多个topic

Consumer API：订阅一个或多喝topic，并处理产生的消息

Streams API：高效的将输入流转换到输出流

Connector API：从一些源系统或应用程序中拉取数据到Kafka

### 依赖

```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>4.1.1</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>2.4.0</version>
</dependency>
<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-streams</artifactId>
    <version>2.4.0</version>
</dependency>
```

### AdminClient 

```java
// 链接
Properties properties = new Properties();
properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.220.128:9092");
AdminClient adminClient = AdminClient.create(properties);

// 创建主题
CreateTopicsResult topics = adminClient.createTopics(Arrays.asList(newTopic));

// 获取Topic列表
ListTopicsResult listTopicsResult = adminClient.listTopics(options);

// 删除Topic
DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Arrays.asList(TOPIC_NAME));

// 描述Topic
DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Arrays.asList(TOPIC_NAME));

// 查看配置信息
DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));

// 修改Config信息
AlterConfigsResult alterConfigsResult = adminClient.incrementalAlterConfigs(configMaps);

// 增加partition数量
CreatePartitionsResult createPartitionsResult = adminClient.createPartitions(partitionsMap);
```





展示kafka日志，每个分区一个文件

![2](img_Kafka%E5%AE%9E%E6%88%98/2.png)







### Producer

KafkaProduce初始化会创建守护线程，send方法发送数据会计算批次，不断向批次中追加消息，守护线程定时扫描，把达到阈值的消息数据批量发送

```java
// 链接
Properties properties = new Properties();
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.220.128:9092");
properties.put(ProducerConfig.ACKS_CONFIG,"all");
properties.put(ProducerConfig.RETRIES_CONFIG,"0");
properties.put(ProducerConfig.BATCH_SIZE_CONFIG,"16384");
properties.put(ProducerConfig.LINGER_MS_CONFIG,"1");
properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"33554432");

properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
Producer<String,String> producer = new KafkaProducer<>(properties); // Producer的主对象
...
producer.close();

// 异步发送
ProducerRecord<String,String> record = new ProducerRecord<>(TOPIC_NAME,"key-"+i,"value-"+i);
producer.send(record);

// 异步发送带回调函数
ProducerRecord<String,String> record = new ProducerRecord<>(TOPIC_NAME,"key-"+i,"value-"+i);
producer.send(record, new Callback() {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.println("partition : "+recordMetadata.partition()+" , offset : "+recordMetadata.offset());
    }
});

// 异步发送带回调函数和Partition负载均衡
properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,"com.imooc.jiangzh.kafka.producer.SamplePartition");

// 异步阻塞发送
ProducerRecord<String,String> record = new ProducerRecord<>(TOPIC_NAME,key,"value-"+i);
Future<RecordMetadata> send = producer.send(record);
RecordMetadata recordMetadata = send.get();
```

```java
public class SamplePartition implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // key值 key-1 key-2 key-3
        String keyStr = String.valueOf(key);
        String keyInt = keyStr.substring(4);
        System.out.println("keyStr : "+keyStr + "keyInt : "+keyInt);
        int i = Integer.parseInt(keyInt);
        return i%2;
    }

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}
```



## 实例

### 生产者批量发送

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
 
public class KafkaBatchProducer {
    public static void main(String[] args) {
        // 配置生产者属性
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批量发送的大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10); // 批处理的延迟时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 记录的内存缓冲区大小
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
 
        // 创建生产者实例
        Producer<String, String> producer = new KafkaProducer<>(props);
 
        // 发送数据
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<>("your-topic", "Key", "Message " + i));
        }
 
        // 关闭生产者实例
        producer.close();
    }
}
```

在这个示例中，我们配置了几个关键的批量发送属性：

- `BATCH_SIZE_CONFIG`: 控制批量发送的大小，达到该值后消息会被发送出去。
- `LINGER_MS_CONFIG`: 控制批处理的延迟时间，当消息累积到足够的数量或者等待了设定的时间后，消息会被发送出去。
- `BUFFER_MEMORY_CONFIG`: 控制生产者可以使用的内存缓冲区大小，该值越大，批量处理的消息越多。

这些属性可以帮助生产者更高效地批量发送消息，从而提高Kafka生产者的性能。

### 消费者+线程池

```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class KafkaConsumerExample {
 
    private final KafkaConsumer<String, String> consumer;
    private final ExecutorService executor;
    private final int threadNumber;
 
    public KafkaConsumerExample(String bootstrapServers, String groupId, String topic, int threadNumber) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
 
        this.consumer = new KafkaConsumer<>(props);
        this.consumer.subscribe(Arrays.asList(topic));
 
        this.threadNumber = threadNumber;
        this.executor = Executors.newFixedThreadPool(threadNumber);
    }
 
    public void pollMessages() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (int i = 0; i < records.count(); i++) {
                    ConsumerRecord<String, String> record = records.iterator().next();
                    executor.submit(new MessageHandler(record));
                }
            }
        } finally {
            consumer.close();
        }
    }
 
    public static void main(String[] args) {
        KafkaConsumerExample example = new KafkaConsumerExample("localhost:9092", "test-group", "test-topic", 5);
        example.pollMessages();
    }
}
 
class MessageHandler implements Runnable {
    private final ConsumerRecord<String, String> record;
 
    public MessageHandler(ConsumerRecord<String, String> record) {
        this.record = record;
    }
 
    @Override
    public void run() {
        // 处理消息的逻辑
        System.out.println("Received message: " + record.value());
    }
}
```







