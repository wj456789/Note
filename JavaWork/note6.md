

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

