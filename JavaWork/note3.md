# Note

## 多线程

### 线程池

```java
// ThreadPoolExecutor
public class CSMSThreadFactory implements ThreadFactory {
    private static final DebugLog LOGGER = LogFactory.getDebugLog(CSMSThreadFactory.class);

    private final String namePrefix;

    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * Instantiates a new Csms thread factory.
     *
     * @param whatFeatureOfGroup the what feature of group
     */
    public CSMSThreadFactory(String whatFeatureOfGroup) {
        namePrefix = "From CSMSThreadFactory's " + whatFeatureOfGroup + "-CSMS customization-";
    }

    /**
     * New thread thread.
     *
     * @param task the task
     * @return the thread
     */
    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        Thread thread = new Thread(null, task, name, 0);
        thread.setUncaughtExceptionHandler((tr, ex) -> LOGGER.error(tr.getName() + " : " + ex.getMessage(), ex));
        thread.setPriority(Thread.MAX_PRIORITY);
        return thread;
    }
}
```



### CompletableFuture

#### runAsync 和 supplyAsync方法

CompletableFuture提供了四个静态方法来创建一个异步操作。 

```java
public static CompletableFuture<Void> runAsync(Runnable runnable)
public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)
```

没有指定 Executor 的方法会使用 ForkJoinPool.commonPool() 作为它的线程池执行异步代码。如果指定线程池，则使用指定的线程池运行。以下所有的方法都类同。

- runAsync 方法不支持返回值。
- supplyAsync 可以支持返回值。

```java
//无返回值
public static void runAsync() throws Exception {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        System.out.println("run end ...");
    });
    future.get();
}

//有返回值
public static void supplyAsync() throws Exception {         
    CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
        return System.currentTimeMillis();
    });
    long time = future.get();
}
```

#### whenComplete 和 whenCompleteAsync 

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("error");
});
future.whenComplete((result, throwable) -> {
    if (throwable != null) {
        System.out.println("exception: " + throwable.getMessage());
    } else {
        System.out.println("result: " + result);
    }
});
```

#### thenApply 和 thenAccept

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 1);
CompletableFuture<String> future2 = future.thenApply(i -> i + 1)
    .thenApply(i -> "result: " + i);
System.out.println(future2.get()); // 输出 "result: 2"
```

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 1);
CompletableFuture<Void> future2 = future.thenAccept(i -> 
               System.out.println("result: " + i));
future2.get(); // 等待异步任务完成
```

thenApply和thenAccept都是用来处理异步任务完成后的结果的，它们的区别在于thenApply会返回一个新的CompletableFuture对象，而thenAccept不会返回任何结果。 

```java
// supplyAsync异步执行，thenAccept会等待获取supplyAsync返回值，thenAccept 和 exceptionally 只会执行一个
CompletableFuture<Void> supplyAsync = CompletableFuture.supplyAsync(() -> {
    // ...
    return "first";
}).thenAccept(result -> {
    // ...
}).exceptionally(throwable -> {
    // 上面的两个方法里面的异常都会捕获到
    return null;
});

// 获取 supplyAsync 返回值
CompletableFuture<String> supplyAsync = CompletableFuture.supplyAsync((Supplier<String>) () -> {
    // ...
    return "true";
}).exceptionally(throwable -> {
    return "false";
});
String result = supplyAsync.get();
```



[CompletableFuture 使用详解](https://blog.csdn.net/jmysql/article/details/123689473)

## String

```
JSONArray jsonArray = new JSONArray();
jsonArray.fluentRemove(i);

public Object remove(int index);
public JSONArray fluentRemove(int index);

`remove`方法和`fluentRemove`方法的主要区别在于，`remove`方法只能删除一个元素，并返回被删除的元素，而`fluentRemove`方法可以链式调用，以删除多个元素，并返回当前JSONArray对象。

链式调用:可以在一个语句中多次调用`fluentRemove`方法，以删除多个元素
```

```
StringUtils.wrap是一个Java中的字符串工具类方法，它的作用是将一个字符串用指定的字符包装起来。具体来说，它会在字符串的前后分别添加指定的字符，使得字符串被包裹在这些字符之间。
例如，如果我们调用StringUtils.wrap("hello", '"')，则会返回一个新的字符串，内容为'"hello"'，即将原来的字符串"hello"用双引号包裹起来。
```



## IO

### File

```java
File file = new File("D:\\test\\1.txt");
boolean isSuccess = file.createNewFile();

没有1.txt文件，则创建该文件
没有test目录，直接抛出异常
如果1.txt已存在，则文件创建失败
```

```java
File.createTempFile 方法创建的临时文件名称示例通常是类似于"prefix1234567890suffix"的格式，其中：

- "prefix"是指定的前缀字符串
- "1234567890"是一个随机生成的数字序列，用于确保文件名的唯一性
- "suffix"是指定的后缀字符串

例如，如果使用以下代码创建一个临时文件：
File cPTemplate = File.createTempFile("cPTemplate", ".txt");
// 则可能会创建一个名为"cPTemplate1234567890.txt"的临时文件，默认的保存路径为C:\Documents and Settings\Administrator\Local Settings\Temp。
File cPTemplate = File.createTempFile("cPTemplate", "test.temp", new File("D:\\test"));
// 则可能会创建一个名为"cPTemplate1234567890test.temp"的临时文件。
```

### available()

```java
int count = inputStream.available();
byte[] b = new byte[count];
inputStream.read(b);
```

一次读取多个字节时，经常用到InputStream.available()方法，这个方法可以在读写操作前先得知数据流里有多少个字节可以读取。需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦。可以调用这个函数下载文件或者对本地文件进行其他处理时获取文件的总大小。

如果网络阻塞了，inputstream已经打开，但是数据却还没有传输过来，会发生什么？inputstream.available()方法返回的值是该inputstream在不被阻塞的情况下一次可以读取到的数据长度。如果数据还没有传输过来，那么这个inputstream势必会被阻塞，从而导致inputstream.available返回0。

## Java8

```java
Date -> LocalDate
Date date = new Date();
Instant instant = date.toInstant();
ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
LocalDate localDate1 = zonedDateTime.toLocalDate();

String -> LocalDate
DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
LocalDate occupyStartDate = LocalDate.parse("2023-07-12", dtf);
occupyStartDate.isBefore(LocalDate.now());
```





## Java基础

```java
// AccessController.doPrivileged
// https://blog.csdn.net/pml18710973036/article/details/69190796
// https://blog.csdn.net/jiangtianjiao/article/details/87909065

T t = null;
try {
    t = entity.newInstance();
    Field[] fields = entity.getDeclaredFields();
    for (Field field : fields) {
        ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
        if (annotation != null) {
            String value = valueMap.get(headMap.get(header));
            if (StringUtils.isNotBlank(value)) {
                boolean flag = field.isAccessible();
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    field.setAccessible(true);
                    return null;
                });
                field.set(t, value.trim());
                AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                    field.setAccessible(flag);
                    return null;
                });
            }
        }
    }
    return t;
} catch (InstantiationException e) {
    LOGGER.error("parse excel error", e);
    throw new BrokerException("", "excel error");
} catch (IllegalAccessException e) {
    LOGGER.error("parse excel error", e);
    throw new BrokerException("", "excel error");
}
```



```java
// 校验小时:分钟
public boolean checkTime(String time) {
    if (checkHHMM(time)) {
        String[] temp = time.split(":");
        if (temp[0].length() == 2 && temp[1].length() == 2) {
            int h, m;
            try {
                h = Integer.parseInt(temp[0]);
                m = Integer.parseInt(temp[1]);
            } catch (NumberFormatException e) {
                return false;
            }
            if (h >= 0 && h <= 24 && m <= 60 && m >= 0) {
                return true;
            }
        }
    }
    return false;
}

/**
     * 校验时间格式（仅格式）
     */
public boolean checkHHMM(String time) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
    try {
        Date t = dateFormat.parse(time);
    } catch (Exception ex) {
        return false;
    }
    return true;
}
```



```java
// 解析properties文件
Resource resource = new ClassPathResource(resourceFile);
try {
    Properties props = new Properties();
    InputStream inputStream = resource.getInputStream();
    Reader reader = new InputStreamReader(inputStream, "UTF-8");
    props.load(reader);
    for (Object key : props.keySet()) {
        String keyStr = String.valueOf(key);
        String value = props.getProperty(keyStr);
    }
} catch (IOException e) {
    LOGGER.error("parse properties error", e);
}
```

```java
// headMap中文表头：列数	valueMap列数：值
// 解析excel到实体类
public <T> T rowToEntity(Map<String, Integer> headMap, Map<Integer, String> valueMap, Class<T> entity) {
    T t = null;
    try {
        t = entity.newInstance();
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                String header = annotation.value();
                if (Objects.isNull(headMap.get(header))) {
                    LOGGER.error(header + "is not exist");
                    throw new BrokerException("", "excel error");
                }
                String value = valueMap.get(headMap.get(header));
                if (StringUtils.isNotBlank(value)) {
                    boolean flag = field.isAccessible();
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        field.setAccessible(true);
                        return null;
                    });
                    field.set(t, value.trim());
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        field.setAccessible(flag);
                        return null;
                    });
                }
            }
        }
        return t;
    } catch (InstantiationException e) {
        LOGGER.error("parse excel error", e);
        throw new BrokerException("", "excel error");
    } catch (IllegalAccessException e) {
        LOGGER.error("parse excel error", e);
        throw new BrokerException("", "excel error");
    }
}
```



## SQL

mysql\Gaussdb

```
ON DUPLICATE key
当执行insert操作时，有已经存在的记录，执行update操作。

ON DUPLICATE KEY UPDATE首先会检查插入的数据主键是否冲突，如果冲突则执行更新操作，如果ON DUPLICATE KEY UPDATE的子句中要更新的值与原来的值都一样，则不更新。如果有一个值与原值不一样，则更新

如果一次插入多条数据，怎么动态获取主键冲突所要更新的值呢？
ON DUPLICATE KEY UPDATE age = VALUES(age)
```

### 分区操作

```sql
alter table c_dnd_monitor_log  drop partition <%:before3MonthPartition%>

ALTER TABLE c_dnd_monitor_log ADD PARTITION <%:partition%> VALUES LESS THAN(to_date(<%:startDay%>,'YYYYMMdd'))

SELECT count(1) FROM c_dnd_monitor_log PARTITION(<%:partition%>)


create table c_dnd_monitor_log(
	ID NVARCHAR(32),
	POLICY_ID VARCHAR(32),
	EXEC_TIME TIMESTAMP,
	IN_COUNT BINARY_BIGINT,
	BATCH_USER_CONTACT BINARY_BIGINT,
	BATCH_SPECIAL_LIST BINARY_BIGINT,
	BATCH_CHANNEL_FLOW BINARY_BIGINT,
	BATCH_USER_RULE_CONTACT BINARY_BIGINT,
	BATCH_RULE_FLOW BINARY_BIGINT,
	CUST_SEGMENT_FILTER BINARY_BIGINT,
	SELECTED_OFFER_FILTER BINARY_BIGINT,
	BATCH_RULE_DAILY_FLOW BINARY_BIGINT,
	CHANNEL_FAILED_FILTER BINARY_BIGINT,
	OUT_COUNT BINARY_BIGINT,
	FILE_NAME NVARCHAR(128)	
)
PARTITION BY RANGE (EXEC_TIME)
(
	PARTITION p202205 VALUES less than (to_date('20220601','YYYYMMdd')),
	PARTITION p202206 VALUES less than (to_date('20220701','YYYYMMdd')),
	PARTITION p202207 VALUES less than (to_date('20220801','YYYYMMdd')),
	PARTITION p202208 VALUES less than (to_date('20220901','YYYYMMdd'))
);
```



## Spring

```java
//假设这是一个service类的片段
try{ 
    //出现异常
} catch (Exception e) {
    e.printStackTrace();
    //设置手动回滚
    TransactionAspectSupport.currentTransactionStatus()
        .setRollbackOnly();
}
//此时return语句能够执行
return  xxx;
```

> 如上：
>
> 　　**当我们需要在事务控制的service层类中使用try catch 去捕获异常后，就会使事务控制失效，因为该类的异常并没有抛出，就不是触发事务管理机制。怎样才能即使用try catch去捕获异常，而又让出现异常后spring回滚呢，这里就要用到**
>
> ```
> TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
> ```
>
> 完美解决问题。并且能够使该方法执行完。
>
> 在aop配置事务控制或注解式控制事务中，try...catch...会使事务失效，可在catch中抛出运行时异常throw new RuntimeException(e)或者手动回滚TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();使得事务生效，异常回滚。

### 事务

[Java-spring事务学习分享](https://blog.csdn.net/qq_41056410/article/details/123800638)

## JSON

### Jackson

#### @JsonAutoDetect

Jackson库中的一个注解，用于**指定类的属性的可见性**，也就是指定Java类中哪些属性和方法可以被序列化和反序列化。其中，creatorVisibility、fieldVisibility、getterVisibility、setterVisibility、isGetterVisibility是@JsonAutoDetect注解中的属性，具体含义如下：

- creatorVisibility：指定构造函数的可见性，即哪些构造函数可以被序列化和反序列化。默认值为Visibility.DEFAULT，表示使用默认可见性规则。
- fieldVisibility：指定字段的可见性，即哪些字段可以被序列化和反序列化。默认值为Visibility.DEFAULT，表示使用默认可见性规则。
- getterVisibility：指定getter方法的可见性，即哪些getter方法可以被序列化和反序列化。默认值为Visibility.DEFAULT，表示使用默认可见性规则。
- setterVisibility：指定setter方法的可见性，即哪些setter方法可以被序列化和反序列化。默认值为Visibility.DEFAULT，表示使用默认可见性规则。
- isGetterVisibility：指定isGetter方法的可见性，即哪些isGetter方法可以被序列化和反序列化。默认值为Visibility.DEFAULT，表示使用默认可见性规则。

这些属性可以设置为以下可见性规则：

- Visibility.ANY：表示任何可见性的属性和方法都可以被序列化和反序列化。
- Visibility.NONE：表示没有可见性的属性和方法都不能被序列化和反序列化。
- Visibility.NON_PRIVATE：表示除了private修饰的属性和方法，其他的都可以被序列化和反序列化。
- Visibility.PROTECTED_AND_PUBLIC：表示除了private修饰的属性和方法，protected和public修饰的属性和方法都可以被序列化和反序列化。
- Visibility.DEFAULT：表示使用默认可见性规则，即只有public修饰的属性和方法可以被序列化和反序列化。

#### @JsonProperty

注解用于指定Java类中的属性在序列化和反序列化时所使用的名称。它可以用于将Java类中的属性名称映射到JSON中的属性名称。如果没有使用@JsonProperty注解，则默认情况下，属性名称将与JSON中的属性名称相同。

@JsonAutoDetect注解和@JsonProperty注解可以一起使用，以指定Java类中哪些属性可以被序列化和反序列化，并指定这些属性在序列化和反序列化时所使用的名称。

```java
// 这里设置都为NONE，只有属性parentId、name可以序列化，其余desc属性和方法都不会序列化
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class OfferGroupDir extends Resource {

    @JsonProperty("parentId")
    private String parentId;

    @JsonProperty("name")
    private String name;

    private String desc;

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
```



[Jackson /常用注解/ annotation](https://blog.csdn.net/u010457406/article/details/50921632?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-50921632-blog-41213051.235%5Ev38%5Epc_relevant_default_base&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-50921632-blog-41213051.235%5Ev38%5Epc_relevant_default_base&utm_relevant_index=4)

## Git

### rebase

构造两个分支master和feature，其中feature是在提交点B处从master上拉出的分支

master上有一个新提交M，feature上有两个新提交C和D

```sh
# 这两条命令等价于git rebase master feature
git checkout feature
git rebase master

# 从主分支拉代码到feature并且变基
git pull origin master:feature --rebase
```

> 当在feature分支上执行git rebase master时，git会从master和featuer的共同祖先B开始提取feature分支上的修改，也就是C和D两个提交，先提取到。然后将feature分支指向master分支的最新提交上，也就是M。最后把提取的C和D接到M后面，**注意这里的接法，官方没说清楚，实际是会依次拿M和C、D内容分别比较，处理冲突后生成新的C’和D’**。一定注意，这里新C’、D’和之前的C、D已经不一样了，是我们处理冲突后的新内容，feature指针自然最后也是指向D’ 

ABM是master分支线，ABCD是feature分支线。 变基完成以后 ABMC’D’ ，C’的内容就是C和M两个节点的内容合并的结果，D’的内容就是D和M两个节点的内容合并的结果。

```sh
# 变基可能导致冲突，先处理完C，会继续报D的冲突，所以下面命令一共会执行两次
git add file
git rebase --continue
```

[git rebase详解](https://blog.csdn.net/weixin_42310154/article/details/119004977)

```sh
# 取消合并
$ git reset --merge
```

## Linux

[文件格式](https://blog.csdn.net/aaaaaxss/article/details/125105101)

## Zookeeper

```java
CuratorFramework client = CuratorFrameworkFactory
        .builder()
        .aclProvider(aclProvider)
        .authorization(ZKSCHEME, auth)
        .sessionTimeoutMs(SESSION_TIMEOUTMS)
        .connectionTimeoutMs(
                CONNECT_TIMEOUTMS) // 连接时间过长易导致应用启动阻塞，connectionTimeout一般小于sessionTimeout，此处设为10000
        .connectString(connectStr)
        .retryPolicy(new ExponentialBackoffRetry(1000, 2,
                3000)) // 连接失败时间间隔过大容易阻塞启动，计算公式：baseSleepTimeMs * Math.max(1, random.nextInt(1 << (retryCount
        .build();

client.start();
```











