# Spring

轻量级的Java开发框架

## IOC控制反转

将对象的创建和管理交给Spring容器处理

```java
1、创建spring的ioc容器对象，实例化所有配置的bean(springioc容器有ApplicationContext和BeanFactory,BeanFactory不常用)
//默认查找classpath路径下的文件
ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
//默认为项目的根目录
ApplicationContext ctx=new FileSystemXmlApplicationContext("src/main/resources/applicationContext.xml");
//也可以读取classpath下的文件
ApplicationContext ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
//前缀file表示绝对路径
ApplicationContext ctx=new FileSystemXmlApplicationContext("file:D:/applicationContext.xml");

2、从ioc容器中获取对象
//使用id获取
User user=(User)ctx.getBean("user");
//容器中只有一个User对象实例
User user=ctx.getBean(User.class);
```

spring配置文件applicationContext.xml

```java
<bean id="user" class="cn.ydzy.springdemo.model.User">
    <property name="username" value="admin"></property>
    <property name="password" value="123456"></property>
</bean>   
```

## 依赖注入DI

在运行期，由外部容器动态的创建依赖对象实例并传递到另一个对象中。

注入方式有三种:

- 属性注入
- 构造器注入

### 属性注入(setter注入)

```java
<bean id="account" class="cn.ydzy.springdemo.model.Account">
    <property name="user" ref="user"></property>
    <property name="friend" value="zhangsan"></property>
</bean>    

public class Account{
    //只存在引用
    private User user;
    private String friend;
    public void setUser(User user){
        this.user=user;
   }
   public void setFriend(String friend){
       this.friend=friend;
   }
}
```

### 构造函数注入

```java
<bean id="account" class="cn.ydzy.springdemo.model.Account">
    <constructor-arg ref="user"/>
</bean>    
<bean id="account" class="cn.ydzy.springdemo.model.Account">
    <constructor-arg type="java.lang.String" value="Jack"/>
    <constructor-arg type="int" value="25"/>
</bean>    
<bean id="account" class="cn.ydzy.springdemo.model.Account">
    <constructor-arg index="0" value="Jack"/>
   <constructor-arg index="1" value="25"/>
</bean>    

public class Account{
    private User user;
    private String friend;
    private int age;
    public Account(User user){
        this.user=user;
   }
   public Account(String friend,int age){
       this.friend=friend;
       this.age=age;
   }
}
```

1、xml文件中定义一个bean新建实例就是控制反转；bean中使用property等就是依赖注入的数据；最后在实例里面声明变量，使用set或构造方法等依赖注入；

2、bean;自动装配；set或构造

3、@Component等；@Autowired等

## Bean的生命周期

BeanFactoryPostProcessor——>代码块——>**实例化**——>**数据装配**——>初始化之前——> **初始化方法**——>初始化之后——>就绪——>使用——>销毁方法——>从容器销毁

## Bean实例化

默认在容器启动的时候实例化bean，也可以在bean标签中设置`lazy-­init="true"`,即懒实例化，即在第一次使用bean时实例化。



**可以使用xml配置文件或annotation注解方式两种配置bean实例**

### 使用xml配置文件配置bean实例

利用 xml配置文件方式中， 还包括如下三小类

- 反射模式（前面的所有配置都是这种模式）
- 工厂方法模式
- Factory Bean模式

#### 工厂方法模式

##### 静态工厂

```xml
<!-- 通过静态工厂实例化bean -->
<!-- 无参：class中虽然是SpringBeanFactory，但这个bean是SpringBean的实例 -->
<bean id="springBean" class="ioc10.SpringBeanFactory" factory-method="getSpringBean">
    <property name="name" value="jack"/>
</bean>
<!-- 带参：factory-method静态工厂方法的名字，constructor-arg给静态工厂方法传递参数 -->
<bean id="springBean2" class="ioc10.SpringBeanFactory" factory-method="getSpringBean">
    <constructor-arg name="name" value="lucy"/>
</bean>
```

```java
public class SpringBeanFactory {
    //无参
    public static SpringBean getSpringBean(){
        return new SpringBean();
    }
    //带参
    public static SpringBean getSpringBean(String name){
        SpringBean springBean = new SpringBean();
        springBean.setName(name);
        return springBean;
    }
}

public static void main(String[] args){
    ApplicationContext ac=new ClassPathXmlApplicationContext("ioc10/spring.xml");
    SpringBean springBean = (SpringBean) ac.getBean("springBean");
}
```

##### 实例工厂

```xml
<!-- 实例工厂：先实例工厂bean -->
<bean id="springBeanFactory" class="ioc12.SpringBeanFactory"/>
<!-- 无参 -->
<bean id="springBean" factory-bean="springBeanFactory"  factory-method="getSpringBean">
    <property name="name" value="tom"/>
</bean>
<!-- 带参 -->
<bean id="springBean2" factory-bean="springBeanFactory" factory-method="getSpringBean">
    <constructor-arg name="name" value="alice"/>
</bean>
```

```java
public class SpringBeanFactory {
    //无参
    public SpringBean getSpringBean(){
        return new SpringBean();
    }
    //带参
    public SpringBean getSpringBean(String name){
        SpringBean springBean = new SpringBean();
        springBean.setName(name);
        return springBean;
    }
}
```

#### FactoryBean

```java
//FactoryBean(工厂bean)实例化Bean
//自定义的FactoryBean需要实现FactoryBean接口
public class CarFactoryBean implements FactoryBean<Car>{
    private String name;
    public void setName(String name){
        this.name=name;
    }
    //返回bean对象
    @Override
    public Car getObject() throws Exception {
    	return new Car(1,"奔驰");
    }
    //返回bean类型
    @Override
    public Class<?> getObjectType() {
    	return Car.class;
    }
    //是否单例
    @Override
    public boolean isSingleton() {
    	return true;
    }
}
```

```xml
<!-- class:指向FactoryBean的全类名	property:配置Factory的属性
	实际返回的实例是FactoryBean的getObject()方法返回的实例 -->
<bean id="car" class="cn.ybzy.spring.CarFactoryBean">
    <property name="name" value="baoma"></property>
</bean>
```









































