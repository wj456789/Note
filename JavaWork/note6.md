

## Redis



[Redis数据一致](https://www.zhihu.com/question/582570001)



## zookeeper

~~~java
以下为AI生成内容，仅供参考，不允许未经评估直接将生成内容植入工作输出件中。

以下是使用ZooKeeper实现分布式锁的Java代码示例：

```java
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DistributedLock implements Watcher {

    private ZooKeeper zooKeeper;
    private String lockPath;
    private String currentLock;
    private String waitLock;
    private CountDownLatch countDownLatch;

    public DistributedLock(String connectString, int sessionTimeout, String lockPath) {
        try {
            this.zooKeeper = new ZooKeeper(connectString, sessionTimeout, this);
            this.lockPath = lockPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        try {
            if (tryLock()) {
                System.out.println(Thread.currentThread().getName() + " 获得锁成功");
            } else {
                waitForLock(waitLock);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean tryLock() throws KeeperException, InterruptedException {
        // 创建临时有序节点
        currentLock = zooKeeper.create(lockPath + "/lock_", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() + " 创建节点 " + currentLock);
        // 获取所有子节点
        List<String> children = zooKeeper.getChildren(lockPath, false);
        // 对子节点进行排序
        Collections.sort(children);
        // 判断当前节点是否为最小节点
        if (currentLock.equals(lockPath + "/" + children.get(0))) {
            return true;
        }
        // 获取当前节点的前一个节点
        int currentIndex = children.indexOf(currentLock.substring(lockPath.length() + 1));
        waitLock = lockPath + "/" + children.get(currentIndex - 1);
        return false;
    }

    private void waitForLock(String prev) throws KeeperException, InterruptedException {
        // 监听前一个节点
        Stat stat = zooKeeper.exists(prev, true);
        if (stat != null) {
            System.out.println(Thread.currentThread().getName() + " 等待前一个节点 " + prev);
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            System.out.println(Thread.currentThread().getName() + " 等待结束");
        }
    }

    public void unlock() {
        try {
            zooKeeper.delete(currentLock, -1);
            zooKeeper.close();
            System.out.println(Thread.currentThread().getName() + " 释放锁成功");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(waitLock)) {
            countDownLatch.countDown();
        }
    }
}
```

使用示例：

```java
public class Main {

    public static void main(String[] args) {
        String connectString = "localhost:2181";
        int sessionTimeout = 5000;
        String lockPath = "/distributed-lock";

        DistributedLock lock = new DistributedLock(connectString, sessionTimeout, lockPath);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                lock.lock();
                // 执行业务逻辑
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }).start();
        }
    }
}
```

在上述示例中，我们创建了一个`DistributedLock`类，其中`lock()`方法用于获取锁，`unlock()`方法用于释放锁。在`tryLock()`方法中，我们创建了一个临时有序节点，并获取所有子节点并排序，判断当前节点是否为最小节点。如果不是最小节点，则获取前一个节点并等待其删除。在`waitForLock()`方法中，我们使用`CountDownLatch`进行等待，直到前一个节点被删除。在`process()`方法中，我们监听前一个节点的删除事件，当前一个节点被删除时，释放`CountDownLatch`。

在`Main`类中，我们创建了10个线程，每个线程都会尝试获取锁并执行业务逻辑，然后释放锁。

请注意，上述代码仅为示例，实际使用时需要根据具体需求进行适当修改和优化。
不再接收
~~~

## 代理

JDK代理和cglib代理都是Java中常用的代理方式，它们的主要**区别**在于： 

- JDK代理只能代理实现了接口的类，而cglib代理可以代理没有实现接口的类。 
- JDK代理是通过反射来实现的，而cglib代理是通过生成字节码来实现的。 
- JDK代理在生成代理对象时需要传入一个接口，而cglib代理则不需要。 
- JDK代理的性能比cglib代理略低，因为JDK代理需要通过反射来调用方法，而cglib代理直接调用生成的字节码。 

因此，如果需要代理的类实现了接口，建议使用JDK代理；如果需要代理的类没有实现接口，或者需要更高的性能，建议使用cglib代理。 



**CGLIB**（Code Generation Library）是一个基于ASM（Java字节码操作框架）的代码生成库，它可以在运行时动态地生成字节码，从而实现代理类的生成。 

CGLIB代理主要是通过继承目标类来实现的。当我们使用CGLIB代理时，CGLIB会在运行时动态地生成一个新的类，该类继承了目标类，并重写了目标类中的方法。在重写的方法中，CGLIB会调用代理类中的方法，从而实现对目标类方法的增强。 

具体来说，CGLIB代理的实现过程如下： 

1. 创建Enhancer对象，该对象用于生成代理类的字节码。 
2. 设置Enhancer对象的父类和回调函数。父类即为目标类，回调函数即为代理类中的方法。 
3. 调用Enhancer对象的create方法，生成代理类的字节码。 
4. 使用反射机制创建代理类的实例。 
5. 调用代理类的方法，实现对目标类方法的增强。 

总的来说，CGLIB代理通过动态生成字节码来实现对目标类方法的增强，从而实现代理的功能。 

## 分页

```java
public class PageUtils {
    /*public static <T> PageInfo<T> getPageInfo(PageCondition pageCondition, boolean needCount, Supplier<List<T>> func){

    }*/
    /*public static <T> PageInfo<T> getPageInfo(int pageNo, int pageSize, boolean needCount, Supplier<List<T>> func){
        int tmpPageNo = pageNo;
        int tmpPageSize = pageSize;
        if (tmpPageNo <= 0) {
            tmpPageNo = 0;
        }
        if (tmpPageSize > 100) {
            tmpPageSize = 100;
        }
        PageHelper.startPage(tmpPageNo, tmpPageSize, needCount);
        return new PageInfo<>(func.get());
    }*/

    public static <T> Pagination<T> queryList(int pageNo, int pageSize, Function<PageCondition, List<T>> func) {
        PageCondition pageCondition = new PageCondition();
        pageCondition.setCurPage(pageNo);
        pageCondition.setRecordperpage(pageSize);
        List<T> apply = func.apply(pageCondition);
        Pagination<T> pagination = new Pagination<>();
        pagination.setResult(apply);
        pagination.setTotal(pageCondition.getTotalRecord());
        pagination.setLimit(pageSize);
        pagination.setOffset((pageNo - 1)*pageSize);
        return pagination;
    }

    public static void main(String[] args) {
        /*PageInfo<UserInfo> userInfoPageInfo = PageUtils.getPageInfo(1, 10, true, new Supplier<List<UserInfo>>() {
            @Override
            public List<UserInfo> get() {
                return null;
            }
        });*/

        Pagination<UserInfo> tPagination = PageUtils.queryList(1, 10, pageCondition -> null);
    }
}
```

## AOP

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CalculateFeeProcessor {
    String value() default "";
}
```

```java
public abstract class CommonBusinessAspect {

    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = null;
        try {
            // 前置处理
            CIntResponse response = preProcess(joinPoint);
            if (!handleResponse(response, joinPoint)) {
                return response;
            }
            obj = joinPoint.proceed();
            // 方法处理
            if (!handleResponse(obj, joinPoint)) {
                return obj;
            }
            // 后置处理
            response = postProcess(joinPoint);
            if (!handleResponse(response, joinPoint)) {
                return response;
            }
            return response;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }

    private CIntResponse preProcess(ProceedingJoinPoint joinPoint) {
        // getProcessor方法调用实现类UnitCalculateFeeAspect的方法，感觉没必要
        IOrderProcessorService processor = getProcessor(joinPoint);
        // 使用回调函数Function调用IOrderProcessorService具体实现类中的preProcess方法
        CIntResponse ret = process(joinPoint, processor::preProcess);
        ret.PrintInfo();
        return ret;
    }

    private CIntResponse postProcess(ProceedingJoinPoint joinPoint) {
        IOrderProcessorService processor = getProcessor(joinPoint);
        CIntResponse response = process(joinPoint, processor::postProcess);
        response.PrintInfo();
        return response;
    }

    private CIntResponse process(ProceedingJoinPoint joinPoint, Function<CPObject[], CIntResponse> func) {
        Object[] args = joinPoint.getArgs();
        CPObject[] coArgs = Arrays.asList(args).toArray(new CPObject[0]);
        CIntResponse response = func.apply(coArgs);
        return response;
    }

    protected abstract IOrderProcessorService getProcessor(ProceedingJoinPoint joinPoint);

    /**
     * 处理响应信息
     */
    private boolean handleResponse(Object object, ProceedingJoinPoint pointcut) {
        ...
        handleInterrupt(pointcut);
        ...
    }

    /**
     * 执行中断后的接续接点
     */
    private CIntResponse handleInterrupt(ProceedingJoinPoint point) {
        String processorName = getInterruptOrderProcessor();
        IOrderProcessorService processorService = SpringUtil.getBean(processorName);
		...
        // 执行处理器的后置，此处处理器应为大流程的最后一步
        return process(point, processorService::postProcess);
    }

    /**
     * 获取业务中断后，接点执行的处理器
     *
     * @return
     */
    protected abstract String getInterruptOrderProcessor();
}
```

```java
@Aspect
@Component
@EnableAspectJAutoProxy
public class UnitCalculateFeeAspect extends CommonBusinessAspect {

    @Pointcut("@annotation(com.huawei.jcrm.om.ordertransact.annotation.CalculateFeeProcessor)")
    public void unitCalculateFee() {
    }

    @Around("unitCalculateFee()")
    public Object aroundUnitCalculateFee(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.around(joinPoint);
    }

    // 根据注解属性动态获取具体实现类
    /**
     * 获取处理信息
     */
    @Override
    protected IOrderProcessorService getProcessor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CalculateFeeProcessor processor = method.getAnnotation(CalculateFeeProcessor.class);
        return SpringUtil.getBean(processor.value());
    }

    /**
     * 获取中断之后的接续处理器名
     */
    @Override
    protected String getInterruptOrderProcessor() {
        return "unitCalculateFeeProcessor";
    }
}
```









```java
public interface IOrderProcessorService {

    /**
     * 前置处理
     */
    CIntResponse preProcess(CPObject... inParamArr);

    /**
     * 后置处理
     */
    CIntResponse postProcess(CPObject... inParamArr);
}
```

```java
public abstract class CommonOrderProcessor implements IOrderProcessorService {

    /**
     * 前置处理
     */
    @Override
    public CIntResponse preProcess(CPObject... inParamArr) {
        return invokeChain(getPreProcessCommandIds(), getCatalogId(), inParamArr);
    }

    /**
     * 后置处理
     */
    @Override
    public CIntResponse postProcess(CPObject... inParamArr) {
        return invokeChain(getPostProcessCommandIds(), getCatalogId(), inParamArr);
    }

    // 使用模板模式供处理器实现
    /**
     * 获取前置流程commandId
     */
    protected abstract String[] getPreProcessCommandIds();

    /**
     * 获取后置流程commandId
     */
    protected abstract String[] getPostProcessCommandIds();

    /**
     * 获取catalogId
     */
    protected abstract String getCatalogId();
}
```

```java
public abstract class MessageStructureProcessor extends CommonOrderProcessor {
    protected static final String MESSAGE_STRUCTURE_PRE_PROCESS_COMMANDID = "messageStructurePre";

    protected static final String MESSAGE_STRUCTURE_POST_PROCESS_COMMANDID = "messageStructurePost";

    protected static final String MESSAGE_STRUCTURE_CATALOGID = "calcFeeOuterMessageStructure";
}

@Service
public class GroupMessageStructureProcessor extends MessageStructureProcessor {
    private static final String GROUP_MESSAGE_STRUCTURE_PRE_PROCESS_COMMANDID = "groupMessageStructurePre";

    private static final String GROUP_MESSAGE_STRUCTURE_POST_PROCESS_COMMANDID = "groupMessageStructurePost";

    @Override
    protected String[] getPreProcessCommandIds() {
        return new String[] {MESSAGE_STRUCTURE_PRE_PROCESS_COMMANDID, GROUP_MESSAGE_STRUCTURE_PRE_PROCESS_COMMANDID};
    }

    @Override
    protected String[] getPostProcessCommandIds() {
        return new String[] {MESSAGE_STRUCTURE_POST_PROCESS_COMMANDID, GROUP_MESSAGE_STRUCTURE_POST_PROCESS_COMMANDID};
    }

    @Override
    protected String getCatalogId() {
        return MESSAGE_STRUCTURE_CATALOGID;
    }
}
```





```java
@CalculateFeeProcessor("groupMessageStructureRecProcessor")
public CIntResponse buildCalcFeeParamForOrderRec()
    ...
}
```

## 责任链

```java
@Slf4j
public abstract class AbstractSenderFilter<T> {
    protected AbstractSenderFilter nextFilter;
    protected NorthChannel northChannel;

    public AbstractSenderFilter(NorthChannel northChannel) {
        this.northChannel = northChannel;
    }

    public void setNextFilter(AbstractSenderFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public AbstractSenderFilter getNextFilter() {
        return nextFilter;
    }

    /**
     * 判断可以传递给下一个
     *
     * @param t 参数
     * @return true可以传递给下个链，false不需要传递了
     */
    public boolean canDoNext(final T t) {
        if (!doFilterJudge(t)) {
            return false;
        }
        if (nextFilter != null) {
            return nextFilter.canDoNext(t);
        }
        return true;
    }

    /**
     * 做过滤判断
     *
     * @param t 参数
     * @return 返回值 true就是数据可以发送  false数据是被过滤掉，不发送
     */
    protected abstract boolean doFilterJudge(final T t);
}
```

```java
@Slf4j
public class FlowReceiveFilter extends AbstractSenderFilter<byte[]> {
    public FlowReceiveFilter(NorthChannel northChannel) {
        super(northChannel);
    }

    @Override
    protected boolean doFilterJudge(byte[] bytes) {
        if (bytes == null) {
            return false;
        }
        ...
        return true;
    }
}
```







```java
public class NorthChannel {
    private AbstractSenderFilter senderFilter;
    
    public void initSendFilter(String filterKeys) {
        ...
        List<AbstractSenderFilter> filterList = castPathToInstance(filterKey);
        this.senderFilter = getFilterChain(filterList);
    }
    
    private List<AbstractSenderFilter> castPathToInstance(String[] filterKey) {
        List<AbstractSenderFilter> filterList = new ArrayList<>();
        for (int i = 0; i < filterKey.length; i++) {
            String filterPath = SendFilterContainer.getFilterContainer().get(filterKey[i]);
            AbstractSenderFilter filterInstance = getFilterInstance(filterPath);
            if (filterInstance != null) {
                filterList.add(filterInstance);
            }
        }
        return filterList;
    }
    
    private AbstractSenderFilter getFilterInstance(String path) {
        try {
            Class<?> filterClass = Class.forName(path);
            Constructor constructor = filterClass.getDeclaredConstructor(NorthChannel.class);
            return (AbstractSenderFilter) constructor.newInstance(this);
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException | IllegalAccessException e) {
        } catch (Exception e) {
        }
        return null;
    }
    
    private AbstractSenderFilter getFilterChain(List<AbstractSenderFilter> filterList) {
        for (int i = filterList.size() - 1; i >= 1; i--) {
            filterList.get(i - 1).setNextFilter(filterList.get(i));
        }
        return filterList.get(0);
    }
}
```

```java
public class NorthChannelFactory {
    private static List<NorthChannel> channels = new CopyOnWriteArrayList<>();
    
    private static void doInitSocketAndChannels(...)
        throws Exception {
        for (int i = 0; i < ipPorts.length; i++) {
            NorthChannel northChannel = new NorthChannel(i);
            ...
            northChannel.initSendFilter(...);
            ...
            channels.add(northChannel);
        }
    }
}
```

```java
public final class SendFilterContainer {
    private static Map<String, String> filterContainer = new HashMap<>();

    /**
     * 这里的key对应着clientConfig.properties里面的sendExtends
     */
    static {
        filterContainer.put("userPlaneHttpCodeFilter", "com.huawei.bdpp.sdtp.multiclient.extend.fiter.ScaSenderFilter");
        filterContainer.put("flowReceiveFilter", "com.huawei.bdpp.sdtp.multiclient.extend.fiter.FlowReceiveFilter");
    }

    public static Map<String, String> getFilterContainer() {
        return filterContainer;
    }
}
```







```java
private static void dealFailData(BaseMsg baseMsg) {
    // 获取北路对象
    List<NorthChannel> channels = NorthChannelFactory.getChannels();
    // 遍历北路对象,获取北路对象的过滤器,如果没有过滤器,数据直接放写文件队列
    // 如果有过滤器,执行北路对象的过滤器的canDoNext方法进行数据过滤,并将过滤后的数据写入北路对象的写文件队列中
    for (NorthChannel channel : channels) {
        AbstractSenderFilter senderFilter = channel.getSenderFilter();
        if (Objects.isNull(senderFilter) || senderFilter.canDoNext(baseMsg.getReport())) {
            ...
        }
    }
}
```





### JSONObject

```
fluentRemove
```

## spring

```
TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
https://www.cnblogs.com/liuzhenlei/p/6777644.html
```

## JVM

```
jvm之AccessController.doPrivileged
https://blog.csdn.net/jiangtianjiao/article/details/87909065
```







## sql

```mysql
分组连接
select score,group_concat(distinct student separator '%') from exam group by score;


|score |group_concat(student)	|
---------------------------------
|70	   |小王%小秦%小赵%小高		|
|80	   |小孙%小李%小秦%小红%小马	|
|90	   |小张%小朱%小钱%小马		|
```



## 分布式

```
分布式锁
https://mp.weixin.qq.com/s?__biz=MzU0OTE4MzYzMw==&mid=2247534770&idx=3&sn=397788f05d0a664495e23959701ab703&chksm=fbb1c14cccc6485aeed1311213f7c06ddb4d15073e8d0c638ea423ea68954fd4c703afb8023e&scene=27
```



## GAUSSDB

### 自增序列

```
CREATE SEQUENCE c_pcm_segment_info_sequence START WITH 1 INCREMENT BY 1;
CREATE TABLE SERVICEDB.C_PCM_SEGMENT_INFO (
	RECORD_ID NUMBER DEFAULT c_pcm_segment_info_sequence.nextval PRIMARY KEY,
	CUSTOMER_GROUP_ID VARCHAR(200) NOT NULL,
	CUSTOMER_GROUP_NAME VARCHAR(1000) NOT NULL,
	CUSTOMER_COUNT NUMBER NOT NULL,
	CREATE_USER VARCHAR(32) NOT NULL,
	CREATE_TIME VARCHAR(1000) NOT NULL,
	AREA_CODE VARCHAR(20) NOT NULL,
	PUSH_TYPE VARCHAR(2) NOT NULL
);
```

```
CREATE TABLE SERVICEDB.C_PCM_SEGMENT_RECORD (
	RECORD_ID SERIAL PRIMARY KEY,
	CUSTOMER_GROUP_ID VARCHAR(200) NOT NULL,
	CUSTOMER_GROUP_NAME VARCHAR(1000) NOT NULL,
	CUSTOMER_COUNT NUMBER NOT NULL,
	CREATE_USER VARCHAR(32) NOT NULL,
	CREATE_TIME VARCHAR(1000) NOT NULL,
	AREA_CODE VARCHAR(20) NOT NULL,
	PUSH_TYPE VARCHAR(2) NOT NULL
);
COMMENT ON TABLE C_PCM_SEGMENT_RECORD IS '客户群信息记录表';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.CUSTOMER_GROUP_ID IS '客群ID';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.CUSTOMER_GROUP_NAME IS '客群名称';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.CUSTOMER_COUNT IS '客户群数量';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.CREATE_USER IS '创建人';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.AREA_CODE IS '地市';
COMMENT ON COLUMN C_PCM_SEGMENT_RECORD.PUSH_TYPE IS '推送类型';

在GaussDB中，SERIAL是一种数据类型，用于定义自增的整数列。SERIAL类型的列会自动递增，每次插入新行时都会自动增加。SERIAL类型的列可以用于定义主键或唯一键，确保每个新行都有唯一的标识符。
```

### 查询表信息

```
SELECT * FROM MY_OBJECTS;
SELECT * FROM MY_TABLES WHERE TABLE_NAME = 'C_PCM_SEGMENT_INFO';
SELECT * FROM MY_TAB_COLUMNS T WHERE T.TABLE_NAME ='C_PCM_SEGMENT_INFO';
SELECT * FROM MY_INDEXES WHERE TABLE_NAME = 'C_PCM_SEGMENT_INFO';
```



## Linux防火墙

```
设置SUSE12防火墙的IP黑名单
1. 打开终端并以root用户身份登录。
2. 停止SuSEfirewall2服务：
   systemctl stop SuSEfirewall2.service
3. 编辑SuSEfirewall2配置文件：
   vi /etc/sysconfig/SuSEfirewall2
4. 找到以下行：
   FW_CUSTOMRULES=""
   在其下方添加以下行：
   FW_CUSTOMRULES="/etc/sysconfig/scripts/firewall-custom-rules.sh"

5. 创建自定义规则脚本文件：
   vi /etc/sysconfig/scripts/firewall-custom-rules.sh

6. 添加以下内容：

   #!/bin/bash
   # Add IP addresses to blacklist
   iptables -I INPUT -s <IP_ADDRESS> -j DROP
   将`<IP_ADDRESS>`替换为要加入黑名单的IP地址。
7. 保存并关闭文件。
8. 使脚本文件可执行：
   chmod +x /etc/sysconfig/scripts/firewall-custom-rules.sh
9. 启动SuSEfirewall2服务：
   systemctl start SuSEfirewall2.service

现在，已将指定的IP地址添加到防火墙的黑名单中。如果要添加更多IP地址，只需编辑`/etc/sysconfig/scripts/firewall-custom-rules.sh`文件并重复步骤6-8即可。
```

```
iptables -I INPUT -s 192.168.0.100 -j DROP和  iptables -A INPUT -s 192.168.0.100 -j DROP区别

这两个命令的区别在于 `-I` 和 `-A` 参数的不同。
`-I` 参数表示在规则链的开头插入一条规则，而 `-A` 参数表示在规则链的末尾添加一条规则。

因此，`iptables -I INPUT -s 192.168.0.100 -j DROP` 会将针对源IP地址为192.168.0.100的所有数据包的动作设置为“DROP”，并将这条规则插入到 INPUT 规则链的开头。
而 `iptables -A INPUT -s 192.168.0.100 -j DROP` 会将相同的规则添加到 INPUT 规则链的末尾，这意味着它会在其他规则之后执行。如果有其他规则允许数据包通过，则这条规则将被忽略。
因此，使用 `-I` 参数可以确保规则被放置在规则链的开头，而使用 `-A` 参数可以将规则添加到规则链的末尾。具体使用哪个参数取决于您的需求和规则链中其他规则的设置。
```

## FI

```sh
$ klist -ket user_l2_unity01.keytab

$ source bigdata_env
$ kinit -kt user_l2_unity01.keytab user_l2_unity01
$ hadoop fs -ls /hbase/data

$ hbase shell
$ list
$ create 'c_strategy_monitor','strategyinfo','promresult','contresult';   策略表
$ create 'c_push_detail','info';									推送明细表
$ create 'c_touch_detail','info'									接触表
$ scan 'c_touch_detail'

$ zkCli.sh -server kwephispre13714:24002
$ ls /hbase/meta-region-server
$ getAcl /hbase/meta-region-server
'sasl,'hbase@HADOOP.COM
: cdrwa
'sasl,'hbase/zkclient.hadoop.com@HADOOP.COM
: cdrwa
'world,'anyone
: r
```

## 第三方接口安全

```
DDoS攻击是指分布式拒绝服务攻击，它是一种通过向目标服务器发送大量的请求来使其无法正常工作的攻击方式。

重放攻击(Replay Attacks)又称为重放攻击、回放攻击或新鲜性攻击(Freshness Attacks),是指攻击者发送一个目的主机已接收过的包，来达到欺骗系统的目的，主要用于身份认证过程，破坏认证的正确性。攻击者通过截获网络通信中的数据包，然后将其重新发送到目标系统上，以此欺骗系统认为这是合法的请求，从而达到攻击的目的。


为了接口安全，可以采取以下措施：

1. 使用时间戳：在通信数据包中加入时间戳，以确保每个请求的时间戳都是唯一的。
timestamp 要求每次请求时重新生成，与服务器时间上下超过5分钟，请求将被拒绝

2. 使用随机数：在通信数据包中加入随机数，以确保每个请求的随机数都是唯一的。
nonce 32位由数字+大写字母组成的随机字符串，每次请求必须重新生成

3. 使用加密技术：对通信数据包进行加密，以确保只有合法的用户才能解密数据包。

4. 使用数字签名：对通信数据包进行数字签名，以确保数据包的完整性和真实性。
sign 数字签名

5. 使用防火墙和入侵检测系统：使用防火墙和入侵检测系统来监控网络流量，及时发现并阻止重放攻击。
```

```
nonce是一个随机字符串，用于确保每个请求都是唯一的。这样做是为了增加请求的安全性，防止恶意攻击者伪造请求。如果每次请求都使用相同的nonce，那么攻击者可以使用重放攻击来伪造请求，从而绕过安全措施。因此，每次请求都必须重新生成一个唯一的nonce。
```

```
数字签名的加密内容介绍
将公共参数中除sign外的参数，按照字母顺序排序后用&符号拼接，再拼上业务参数，组成待签名串，例如：
appId=502004&busiSerial=2bcb6d92fb8f4284bb08517beb1f51fe&nonce=TWEFRLU19IYAP8HKVN6S0O4BJXG3CQZ5&route_type=0&route_value=200&timestamp=20201118115427725{"date_type":"1","date_time":"20201103","area_type":"2"}
公共参数：appId，busiSerial，nonce，timestamp
业务参数为: {"date_type":"1","date_time":"20201103","area_type":"2"}

数字签名的加密算法
数字签名使用SHA256withRSA算法和开发者自己拥有的私钥生成加密串，再对加密串进行base64编码，最后生成签名串，正式的调用环境中，开发者（第三方商户）自身维护对应的私钥，保障私钥的安全、防止泄露。
```

```
为什么签名使用私钥签名，公钥验证，而加密使用公钥加密，私钥解密

这是因为签名和加密的目的不同。
签名的目的是为了验证数据的真实性和完整性，确保数据没有被篡改或伪造。在签名过程中，使用私钥对数据进行签名，公钥用于验证签名的有效性。因为私钥是保密的，只有拥有私钥的人才能对数据进行签名，同时，公钥是公开的，任何人都可以使用公钥来验证签名的有效性。
而加密的目的是为了保护数据的机密性，确保数据只能被授权的人访问。在加密过程中，使用公钥对数据进行加密，私钥用于解密数据。因为公钥是公开的，任何人都可以使用公钥对数据进行加密，但只有拥有私钥的人才能解密数据。
```









## 内存





```
-server -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:NewRatio=4 -XX:MaxMetaspaceFreeRatio=80 -XX:MinMetaspaceFreeRatio=50 -Xloggc:/home/uap/pcm/tomcat/logs/gc_%p.log -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/uap/pcm/tomcat/logs/OutOfMemoryError.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+PrintGCDateStamps -Djava.security.egd=file:/dev/random -Dsun.rmi.dgc.server.gcInterval=0x7FFFFFFFFFFFFFE -Dsun.rmi.dgc.client.gcInterval=0x7FFFFFFFFFFFFFE
```

```
jmap

jmap -dump:live,format=b,file=<filename> <java process id>

MemoryAnalyzer-1.15.0.20231206-linux.gtk.x86_64.zip (88.71MB)

mat/ParseHeapDump.sh   …/dump/dataNode17600.hprof   org.eclipse.mat.api:suspects  org.eclipse.mat.api:overview   org.eclipse.mat.api:top_components
```

```
linux环境用MAT解析dump文件、打dump步骤
1.首先下载对应linux环境版本MAT，例如选择x86_64物理机环境，下载MemoryAnalyzer-1.10.0.20200225-linux.gtk.x86_64
下载链接：https://eclipse.dev/mat/downloads.php

2.将MAT压缩包和dump文件传到物理机上，并解压MAT文件夹，修改MemoryAnalyzer.ini中的Xmx大于dump的大小。

3.执行
./ParseHeapDump.sh ../dump.hprof org.eclipse.mat.api:suspects org.eclipse.mat.api:overview org.eclipse.mat.api:top_components


打dump步骤：
chown omm:wheel -R /opt/rsj
su - omm
cd /opt/huawei/Bigdata/common/runtime/jdk1.8.0_402/bin
./jmap -dump:format=b,file=/opt/rsj/rsj_dump.hprof 3615811
    Dumping heap to /opt/rsj/rsj_dump.hprof ...
    Heap dump file created
参考链接:https://zhuanlan.zhihu.com/p/397236489
```

**MAT分析hprof文件**

1. 本地安装MemoryAnalyzer软件
2. 打开MemoryAnalyzer软件，导入.hprof文件
3. 查看MAT分析的结果
   用MAT打开标准的hpof文件，Open File-->选择文件-->Leak Suspects Report-->Finish。这时MAT就会生成报告，这个报告分为两个标签页，一个是Overview，一个是Leak Suspects（内存泄漏猜想）

（1）Overview视图
打开Overview标签页
首先看到的是一个饼状图，它主要用来显示内存的消耗，其中涉及的类有多少，对象有多少，类加载器，如果有**没有回收的对象**，会有一个连接，可以直接参看(**Unreachable Objects Histogram**)。饼状图的彩色区域代表被分配的内存，灰色区域的则是空闲内存，点击每个彩色区域可以看到这块区域的详细信息

Actions一栏的下面列出了MAT提供的四种Action:
Histogram视图: 列举内存中对象存在的个数和大小
Dominator Tree视图：该视图会以占用总内存的百分比来列举所有实例对象，注意这个地方是对象而不是类了，这个视图是用来发现大内存对象的
Top Consumers: 该视图会显示可能的内存泄漏点
Duplicate Classes: 该视图显示重复的类等信息

其中分析内存泄漏最常用的就是Histogram和Dominator Tree。Histogram视图是最重要的，其他三个视图也只是提供一些可能会造成内存泄漏的信息，点击Actions中给出的链接或者在MAT工具栏中就可以打开Dorminator Tree和Histogram



```
1、目前从dump和gc日志看,是内存配的太大了.然后导致部分垃圾数据在内存里待的太久.无法通过young gc清理.累积后会导致full gc
2、确实有存在占用了30多G内存，gc不下来的。这个目前还在找数据的引用源。


./jcmd 47834 GC.heap_info
./jcmd 47834 Perffcounter.print

-XX:NativeMemoryTracking:detail

jcmd工具
https://www.cnblogs.com/caicz/p/16542942.html

jinfo -flags 20399



-XX:ParallelGCThread=8
-XX：MaxGCPauseMillis=400

-XX：InitiatingHeapOccupancyPercent=20

举个例子:一个Web服务器,Java进程最大堆内存为4G,每分钟响应1500个请求,每45
秒钟会新分配大约2G的内存。G1会每45秒钟进行一次年轻代回收,每31个小时整个堆的
使用率会达到45%,会开始老年代并发标记过程,标记完成后开始四到五次的混合回收


JVM调优实战：to-space exhausted & Evacuation Failure
https://blog.csdn.net/yzh_1346983557/article/details/128718401

jcmd pid VM.native_memory
jcmd pid VM.flags



$ profiler start --event allocation --interval 1000000000 --file <文件路径及文件名>.jfr
$ profiler stop

JProfiler
```







