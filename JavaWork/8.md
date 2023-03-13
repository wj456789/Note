# Note

## Spring

spring 中 bean 默认是多线程单例模式，所以 controller 的请求是线程不安全。

## 多线程

### ExecutorService

```java
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogWebservice extends BaseDAO {
    private static final int MAX_QUEUE_SIZE = 200000;

    private static final BlockingQueue<InterfaceLogBO> interfaceLogQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);

    private static final ExecutorService SCH_THREAD_POOL_EXECUTOR_NEW =
        new ThreadPoolExecutor(
        5,
        10,
        60,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(MAX_QUEUE_SIZE),
        new UserThreadFactory("CbInterfaceLog"));

    static {
        LOGGER.info("Start the execution of the log obtaining queue....");
        RunLogWebserviceThread runLogWebserviceThread = new RunLogWebserviceThread();
        SCH_THREAD_POOL_EXECUTOR_NEW.execute(runLogWebserviceThread);
    }

    public boolean interfaceLogWrite(InterfaceLogBO interfaceLogBO) {
        return interfaceLogBO != null && interfaceLogQueue.offer(interfaceLogBO);
    }

    public static InterfaceLogBO pollInterfaceLogBO(long timeout, TimeUnit unit) throws InterruptedException {
        return interfaceLogQueue.poll(timeout, unit);
    }

    private static class RunLogWebserviceThread implements Runnable {
        @Override
        public void run() {
            List<InterfaceLogBO> interfaceLogLists = new ArrayList<>();
            while (true) {
                InterfaceLogBO interfaceLogBo = pollInterfaceLogBO(20L, TimeUnit.SECONDS);
                Runnable insertTask = new LogWebserviceThread(interfaceLogLists);
                SCH_THREAD_POOL_EXECUTOR_NEW.execute(insertTask);
            }
        }
    }
}
```

```java
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class UserThreadFactory implements ThreadFactory {
    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public UserThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "From UserThreadFactory's " + whatFeatureOfGroup + "-hebei customization-";
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        return new Thread(null, task, name, 0);
    }
}
```

## Hbase

### cache 和 batch

缓存是服务端缓存 result 个数

批量处理是处理每行列数，默认是全列，当数据比每行列数小，则分批次处理，比每行列数大还是一行。比如默认一行一个 result ，一行数据有23列，批量处理为10，需要分三个批次读取这行数据，会放在3个 result 中，第三个 result 只有3列。

每次 rpc 请求可以获取缓存中的所有 result ，每次调用 next 都会触发一次rpc请求

RowFilter用于过滤row key

| Operator         | Description |
| ---------------- | ----------- |
| LESS             | 小于        |
| LESS_OR_EQUAL    | 小于等于    |
| [EQUAL           | 等于        |
| NOT_EQUAL        | 不等于      |
| GREATER_OR_EQUAL | 大于等于    |
| GREATER          | 大于        |
| NO_OP            | 排除所有    |

[Hbase](https://www.cnblogs.com/dengrenning/articles/9520924.html)

[Hadoop、HDFS、Hive、Hbase之间的关系](https://www.cnblogs.com/liyuanhong/articles/14518037.html)

[HBase高级特性之过滤器](http://3ms.huawei.com/km/groups/388/blogs/details/10461377) 

[HBase - Filter - 过滤器的介绍以及使用](https://blog.csdn.net/qq_36864672/article/details/78624856)

[HBase的二级索引](https://www.cnblogs.com/tesla-turing/p/11515351.html)

[hbase的cache与batch的理解](https://blog.csdn.net/luxiangzhou/article/details/83615993)

[HBase基础知识(8):扫描操作之缓存与批量处理](http://pangjiuzala.github.io/2015/08/20/HBase%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86-8-%E6%89%AB%E6%8F%8F%E6%93%8D%E4%BD%9C%E4%B9%8B%E7%BC%93%E5%AD%98%E4%B8%8E%E6%89%B9%E9%87%8F%E5%A4%84%E7%90%86/)

[hbase 程序优化 参数调整方法](https://blog.51cto.com/u_15127532/4045809) 

[hbase零碎小记](https://blog.csdn.net/bryce123phy/article/details/52943434)

