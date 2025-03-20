# Mybatis1

## 普通查询、流式查询和游标查询

### JDBC

#### 普通查询

- 原理：通过JDBC一次性将整个结果集加载到JVM内存中，逐行处理。
- 特点
  - 优势：代码简单，适合小数据量（如1000条以内）。
  - 劣势：大数据量易导致OutOfMemoryError（OOM）。无法实时处理数据，需等待全量加载完成。

```java
Connection conn = DriverManager.getConnection(url, user, password);
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM table");
while (rs.next()) {
    // 处理数据
}
```

#### 流式查询

- 原理：通过设置 ResultSet.TYPE_FORWARD_ONLY 和 fetchSize=Integer.MIN_VALUE，逐行从数据库读取数据，避免一次性加载到内存。
- 特点
  - 优势：内存效率高，适合处理百万级数据。实时处理数据，减少等待时间。
  - 劣势：需独占数据库连接，可能引发连接耗尽。无法随机访问结果集（仅支持顺序读取）。

```java
Connection conn = DriverManager.getConnection(url, user, password);
Statement stmt = conn.createStatement(
    ResultSet.TYPE_FORWARD_ONLY, 
    ResultSet.CONCUR_READ_ONLY
);
stmt.setFetchSize(Integer.MIN_VALUE);
ResultSet rs = stmt.executeQuery("SELECT * FROM table");
while (rs.next()) {
    // 处理数据
}
```

#### 游标查询

- 原理：通过useCursorFetch=true参数和fetchSize设置，分批次从数据库获取数据（如每次1000条），结合游标逐行处理。
- 特点
  - 优势：内存占用低于普通查询，性能优于流式查询。支持批量处理，减少网络开销。
  - 劣势：需手动管理连接生命周期，复杂度较高。可能引发MySQL临时空间占用飙升。

```java
String url = "jdbc:mysql://localhost:3306/test?useCursorFetch=true";
Connection conn = DriverManager.getConnection(url, user, password);
Statement stmt = conn.createStatement(
    ResultSet.TYPE_FORWARD_ONLY, 
    ResultSet.CONCUR_READ_ONLY
);
stmt.setFetchSize(1000); // 每次获取1000条
ResultSet rs = stmt.executeQuery("SELECT * FROM table");
while (rs.next()) {
    // 处理数据
}
```

#### 对比与选择建议

| 特性     | 普通查询         | 流式查询           | 游标查询               |
| -------- | ---------------- | ------------------ | ---------------------- |
| 内存占用 | 高（全量加载）   | 低（逐行加载）     | 中（分批次加载）       |
| 实时性   | 低（需全量加载） | 高（实时处理）     | 中（分批次处理）       |
| 适用场景 | 小数据量         | 大数据量（如导出） | 大数据量（需批量处理） |
| 连接占用 | 共享连接         | 独占连接           | 共享连接（需管理）     |

- 选择建议
  - 小数据量：优先使用普通查询。
  - 大数据量导出：推荐流式查询（如MyBatis的@Options注解）。
  - 实时处理或批量操作：选择游标查询，注意控制fetchSize。

- 注意事项
  - 流式/游标查询需显式关闭 ResultSet 和 Connection，避免资源泄漏。
  - 游标查询可能引发 MySQL 临时空间占用飙升，需监控磁盘I/O。
  - 流式查询在事务中可能影响性能，建议结合 COMMIT 优化。
  - 通过合理选择查询模式，可在性能与资源消耗间取得平衡。



### MyBatis

#### 流式查询（Cursor）

MyBatis 提供了一个叫 `org.apache.ibatis.cursor.Cursor` 的接口类用于流式查询，这个接口继承了 `java.io.Closeable` 和 `java.lang.Iterable` 接口，由此可知：Cursor 是可关闭的；Cursor 是可遍历的。

Cursor 还提供了三个方法：

- `isOpen()`：用于在取数据之前判断 Cursor 对象是否是打开状态。只有当打开时 Cursor 才能取数据；
- `isConsumed()`：用于判断查询结果是否全部取完。
- `getCurrentIndex()`：返回已经获取了多少条数据

```
设置连接属性 useCursorFetch=true
jdbc:mysql://127.0.0.1:3306/test?useCursorFetch=true
```

```java
// 通过@Options注解配置resultSetType=FORWARD_ONLY和fetchSize=Integer.MIN_VALUE，返回Cursor对象逐行处理数据
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    Cursor<User> streamUsers();
}

// 或者流式查询使用配置文件
<select id="streamingQuery" fetchSize="-2147483648" resultMap="BaseResultMap">
  select id, name from user limit 10
</select>

    
    
// 调用时遍历Cursor：
try (Cursor<User> cursor = userMapper.streamUsers()) {
    cursor.forEach(user -> {
        // 处理单条数据
    });
}
```

在取数据的过程中需要保持数据库连接，而 Mapper 方法通常在执行完后连接就关闭了，因此 Cusor 也一并关闭了。

```java
// 1. 用 SqlSessionFactory 来手工打开数据库连接
@Autowired
private SqlSessionFactory sqlSessionFactory;

@GetMapping("foo/scan/1/{limit}")
public void scanFoo1(@PathVariable("limit") int limit) throws Exception {
    try (
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Cursor<Foo> cursor =
              sqlSession.getMapper(FooMapper.class).scan(limit)
    ) {
        cursor.forEach(foo -> { });
    }
}

// 2. 用 TransactionTemplate 来执行一个数据库事务，这个过程中数据库连接同样是打开的。
@GetMapping("foo/scan/2/{limit}")
public void scanFoo2(@PathVariable("limit") int limit) throws Exception {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

    transactionTemplate.execute(status -> {               
        try (Cursor<Foo> cursor = fooMapper.scan(limit)) {
            cursor.forEach(foo -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    });
}

// 3. @Transactional 注解，只在外部调用时生效
@GetMapping("foo/scan/3/{limit}")
@Transactional
public void scanFoo3(@PathVariable("limit") int limit) throws Exception {
    try (Cursor<Foo> cursor = fooMapper.scan(limit)) {
        cursor.forEach(foo -> { });
    }
}
```



#### 游标查询

```
设置连接属性 useCursorFetch=true
jdbc:mysql://127.0.0.1:3306/test?useCursorFetch=true
```

```java
// 通过fetchSize分批次获取数据
@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders")
    @Options(fetchSize = 1000)
    Cursor<Order> batchOrders();
}

// 或者游标查询使用配置文件
<select id="cursorQuery" fetchSize="1000" resultMap="BaseResultMap">
  select id, name from user limit 10000
</select>

    

// 调用时结合事务管理：
@Transactional
public void processOrders() {
    try (Cursor<Order> cursor = orderMapper.batchOrders()) {
        cursor.forEach(order -> {
            // 批量处理数据
        });
    }
}
```



### MyBatis-Plus

#### 普通查询

```java
// 直接使用BaseMapper的通用方法（如selectList、selectPage），无需特殊配置
List<User> users = userMapper.selectList(Wrappers.emptyWrapper());
```

#### 流式查询

```
设置连接属性 useCursorFetch=true
jdbc:mysql://127.0.0.1:3306/test?useCursorFetch=true
```

```java
// 通过ResultHandler接口逐行处理数据，需在Mapper方法上添加@Options注解配置流式参数
@Select("SELECT * FROM table")
@Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
void streamQuery(@Param(Constants.WRAPPER) Wrapper wrapper, ResultHandler<User> handler);

// 调用时传入ResultHandler：
userMapper.streamQuery(Wrappers.emptyWrapper(), new ResultHandler<User>() {
    @Override
    public void handleResult(ResultContext<? extends User> resultContext) {
        User user = resultContext.getResultObject();
        // 处理单条数据
    }
});

// 结合分页，按批次从数据库拉取数据出来跑批，例如从数据库获取10万记录，做数据处理
Page<H2User> page = new Page<>(1, 100000);
baseMapper.selectList(page, Wrappers.emptyWrapper(), new ResultHandler<H2User>() {
    int count = 0;
    @Override
    public void handleResult(ResultContext<? extends H2User> resultContext) {
        H2User h2User = resultContext.getResultObject();
        System.out.println("当前处理第" + (++count) + "条记录: " + h2User);
        // 在这里进行你的业务处理，比如分发任务
    }
});

// 从数据库获取表所有记录，做数据处理
baseMapper.selectList(Wrappers.emptyWrapper(), new ResultHandler<H2User>() {
    int count = 0;
    @Override
    public void handleResult(ResultContext<? extends H2User> resultContext) {
        H2User h2User = resultContext.getResultObject();
        System.out.println("当前处理第" + (++count) + "条记录: " + h2User);
        // 在这里进行你的业务处理，比如分发任务
    }
});
```

[流式查询](https://baomidou.com/guides/stream-query/)

#### 游标查询

```
设置连接属性 useCursorFetch=true
jdbc:mysql://127.0.0.1:3306/test?useCursorFetch=true
```

```application.yml
mybatis-plus:
  configuration:
    settings:
      useCursorFetch: true
```

```java
public interface YourMapper {
    Cursor<YourEntity> selectByCursor(Page<YourEntity> page);
}


@Service
public class YourService {
    @Resource
    private YourMapper yourMapper;

    public void processLargeData() {
        int pageSize = 1000; // 指定每页数据量
        int currentPage = 1;

        Page<YourEntity> page = new Page<>(currentPage, pageSize);
        Cursor<YourEntity> cursor = yourMapper.selectByCursor(page);

        while (cursor.isOpen() && cursor.hasNext()) {
            YourEntity entity = cursor.next();
            // 处理数据
        }
        cursor.close();
    }
}
```



```java
// 通过Cursor接口分批次获取数据，设置fetchSize
@Select("SELECT * FROM table")
@Options(fetchSize = 1000)
Cursor<User> cursorQuery();

// 调用时遍历Cursor：
try (Cursor<User> cursor = userMapper.cursorQuery()) {
    cursor.forEach(user -> {
        // 处理单条数据
    });
}
```































