# **SpringBoot**

## 概念

SpringBoot是一个用来简化Spring应用的初始化创建和开发的框架

- 快速创建独立运行的Spring应用并与主流框架集成
- 内置Servlet容器，应用无需打包war包
- 使用starter（启动器）管理依赖并进行版本控制
- 大量的自动配置，简化开发
- 提供了准生产环境的运行时监控，如指标、 健康检查、外部配置等
- 无需配置XML，没有生成冗余代码，开箱即用

## **IDEA新建SpringBoot工程**

![](img_SpringBoot/1.png)

![1 (2)](img_SpringBoot/1%20(2).png)

![1 (3)](img_SpringBoot/1%20(3).png)

项目名和所在路径

![1 (4)](img_SpringBoot/1%20(4).png)

## SpringBoot应用

### pom.xml解析

```java
/* 
    父项目spring-boot-starter-parent.pom的父项目是spring-boot-dependencies.pom，用来管理SpringBoot应用中依赖的版本
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath>../../spring-boot-dependencies</relativePath>
    </parent>
    
    spring-boot-dependencies.pom中配置了多个依赖版本
    <properties>
      <activemq.version>5.16.2</activemq.version>
      <antlr2.version>2.7.7</antlr2.version>
      <appengine-sdk.version>1.9.89</appengine-sdk.version>
      <artemis.version>2.17.0</artemis.version>
      <aspectj.version>1.9.6</aspectj.version>
      <assertj.version>3.19.0</assertj.version>
      <atomikos.version>4.0.6</atomikos.version>
      ...
    </properties>
*/
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>新版本</version>
</parent>

<dependencies>

    /* 通过启动器starter添加依赖，这里是web应用场景下的依赖包*/
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### **主程序类**

```java
//标注在类上，表示这个类是一个SpringBoot应用
@SpringBootApplication

//SpringbootDemoApplication.class参数表示Spring扫描组件范围是主程序类所在的包及其子包
//也可以使用@ComponentScan标注在类上，指定要扫描的包
@ComponentScan
public class SpringbootDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemoApplication.class, args);
    }
}
```

### **@SpringBootApplication源码**

```java
//@SpringBootConfiguration标注在类上，表示这个SpringBoot的配置类，相当于xml配置文件
@SpringBootConfiguration
//开启自动配置功能，SpringBoot会自动完成许多配置，简化了以前的繁琐的配置
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
      @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {...}
```

#### @SpringBootConfiguration源码

```java
//@Configuration标注在类上，表示这个类是Spring的配置类
@Configuration
public @interface SpringBootConfiguration {...}

@Component
public @interface Configuration {...}
```

#### @EnableAutoConfiguration自动配置源码

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {...}

public class AutoConfigurationImportSelector implements DeferredImportSelector, 
                                        BeanClassLoaderAware,ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
       ...
       AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
       ...
    }
    protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
       ...
       List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
       ...
    }
    protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
       List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
             getBeanClassLoader());
       ...
    }
}

public final class SpringFactoriesLoader {
    public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
        ...
        return (List)loadSpringFactories(classLoaderToUse).getOrDefault(factoryTypeName, Collections.emptyList());
    }
    private static Map<String, List<String>> loadSpringFactories(ClassLoader classLoader) {
        ...
        //扫描类路径下所有Jar包，加载spring-boot-autoconfigure.jar包中META-INF/spring.factories文件，获取EnableAutoConfiguration对应的值
        Enumeration urls = classLoader.getResources("META-INF/spring.factories");
        ...
    }
}



//spring.factories
//将这些自动配置类(xxxAutoConfiguration)添加到容器中
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration,\
org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration,\
...
```

#### HttpEncodingAutoConfiguration源码(2.5.1版本)

```java
@Configuration(proxyBeanMethods = false)
//启用ServerProperties类的ConfigurationProperties功能，并将ServerProperties添加到容器中
@EnableConfigurationProperties(ServerProperties.class)
//根据当前不同条件判断(@Conditional)，决定自动配置类是否生效
//如果当前应用是Web应用，则该配置类生效，否则不生效
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//如果当前应用中有CharacterEncodingFilter类，则该配置类生效，否则不生效
@ConditionalOnClass(CharacterEncodingFilter.class)
//如果配置文件中有server.servlet.encoding.enabled选项，则该配置项生效，否则不生效，默认设置为True，所以默认生效
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
public class HttpEncodingAutoConfiguration {
    ...
}

//从全局配置文件中获取指定的属性值，然后绑定到当前类的属性中
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
public class ServerProperties {
    private Integer port;
    private InetAddress address;
    @NestedConfigurationProperty
    private final ErrorProperties error = new ErrorProperties();
    @NestedConfigurationProperty
    private Ssl ssl;
    @NestedConfigurationProperty
    private final Servlet servlet = new Servlet();
    private final Tomcat tomcat = new Tomcat();
    private final Netty netty = new Netty();
    public static class Servlet {
       @NestedConfigurationProperty
       private final Encoding encoding = new Encoding();
       @NestedConfigurationProperty
       private final Session session = new Session();
       ...
    }
    ...
}

public class Encoding {
    public static final Charset DEFAULT_CHARSET;
    private Charset charset;
    private Boolean force;
    private Boolean forceRequest;
    private Boolean forceResponse;
    private Map<Locale, Charset> mapping;
    ...
}
    
//自动配置类xxxAutoConfiguration的属性是从对应的xxxProperties类中获取，这些属性是通过全局配置文件注入绑定的，可以在配置文件中指定属性值
//application.properties
server.address=
server.port=
server.error.include-exception=false
server.ssl.ciphers=
server.servlet.encoding.charset=GBK
server.servlet.encoding.force=true
...
 
 
总结：SpringBoot启动时会加载大量的自动配置类，通过这些自动配置类向容器中添加组件，来实现自动配置功能 
```

### Web开发的自动配置类：WebMvcAutoConfiguration源码              

```java
@Configuration
@ConditionalOnWebApplication(
    type = Type.SERVLET
)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
@AutoConfigureOrder(-2147483638)
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
public class WebMvcAutoConfiguration {
    ...
    public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ResourceLoaderAware {
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            ...
            this.customizeResourceHandlerRegistration(registry
                .addResourceHandler(new String[]{staticPathPattern})
                //加载静态资源
                .addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations()))
                .setCachePeriod(this.getSeconds(cachePeriod))
                .setCacheControl(cacheControl));
            ...
        }    
    }
    
    //内置的内部类WebMvcAutoConfigurationAdapter
    @Configuration(proxyBeanMethods = false)
    @Import(EnableWebMvcConfiguration.class)
    @EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
    @Order(0)
    public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {
    	 //自动添加格式转换器
        @Override
        public void addFormatters(FormatterRegistry registry) {
           ApplicationConversionService.addBeans(registry, this.beanFactory);
        }
    }
    ...
}
```

#### 加载静态资源源码

```java
@ConfigurationProperties(prefix = "spring.resources",ignoreUnknownFields = false)
public class ResourceProperties {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]
    		//静态资源默认加载位置
    		{"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};
    private String[] staticLocations;

    public ResourceProperties() {
        this.staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
    }

    public String[] getStaticLocations() {
        return this.staticLocations;
    }
    ...
} 
 
# 当访问静态资源时会到所有静态资源文件夹中查找
# 配置文件指定静态资源的位置,修改后，除配置的目录以外其他目录就不可以再访问静态资源了
spring.resources.static-locations=classpath:/static,classpath:/public 
```

#### 自动添加格式转换器源码

```java
//SpringBoot初始化时，会将自定义的convert，GenericConvert, Formatter接口实现类自动注册到ConversionService对象中
public class ApplicationConversionService extends FormattingConversionService {
    ...
    public static void addBeans(FormatterRegistry registry, ListableBeanFactory beanFactory) {
       Set<Object> beans = new LinkedHashSet<>();
       beans.addAll(beanFactory.getBeansOfType(GenericConverter.class).values());
       beans.addAll(beanFactory.getBeansOfType(Converter.class).values());
       beans.addAll(beanFactory.getBeansOfType(Printer.class).values());
       beans.addAll(beanFactory.getBeansOfType(Parser.class).values());
       //注册各类转换器
       for (Object bean : beans) {
          //将IOC容器中GenericConverter类型的Bean注册到服务类中
          if (bean instanceof GenericConverter) {
             registry.addConverter((GenericConverter) bean);
          }
          //将IOC容器中Converter类型的Bean注册到服务类中
          else if (bean instanceof Converter) {
             registry.addConverter((Converter<?, ?>) bean);
          }
          //将IOC容器中Formatter类型的Bean注册到服务类中
          else if (bean instanceof Formatter) {
             registry.addFormatter((Formatter<?>) bean);
          }
          else if (bean instanceof Printer) {
             registry.addPrinter((Printer<?>) bean);
          }
          else if (bean instanceof Parser) {
             registry.addParser((Parser<?>) bean);
          }
       }
    }
}
```

## 配置文件

### 源码

```java
public class ConfigFileApplicationListener implements EnvironmentPostProcessor, SmartApplicationListener, Ordered {

	private static final String DEFAULT_PROPERTIES = "defaultProperties";
	private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/*/,file:./config/";
	private static final String DEFAULT_NAMES = "application";
    
	public static final String ACTIVE_PROFILES_PROPERTY = "spring.profiles.active";
	public static final String INCLUDE_PROFILES_PROPERTY = "spring.profiles.include";
	public static final String CONFIG_NAME_PROPERTY = "spring.config.name";
	public static final String CONFIG_LOCATION_PROPERTY = "spring.config.location";
	public static final String CONFIG_ADDITIONAL_LOCATION_PROPERTY = "spring.config.additional-location";
    ...
        
    private Set<String> getSearchNames() {
        if (this.environment.containsProperty(CONFIG_NAME_PROPERTY)) {
            String property = this.environment.getProperty(CONFIG_NAME_PROPERTY);
            Set<String> names = asResolvedSet(property, null);
            names.forEach(this::assertValidConfigName);
            return names;
        }
        return asResolvedSet(ConfigFileApplicationListener.this.names, DEFAULT_NAMES);
    }
    
    private Set<String> getSearchLocations() {
        Set<String> locations = getSearchLocations(CONFIG_ADDITIONAL_LOCATION_PROPERTY);
        if (this.environment.containsProperty(CONFIG_LOCATION_PROPERTY)) {
            locations.addAll(getSearchLocations(CONFIG_LOCATION_PROPERTY));
        }else {
            locations.addAll(asResolvedSet(ConfigFileApplicationListener.this.searchLocations, DEFAULT_SEARCH_LOCATIONS));
        }
        return locations;
    }
    ...
}
```

### 默认的配置文件加载顺序

springboot 读取配置文件的优先级：

1. `file:./config/`：读取jar包的同一目录下的config文件夹下的配置文件
2. `file:./config/*/`：读取jar包的同一目录下的config文件夹子目录下的配置文件
3. `file:./`：读取jar包同级目录下的配置文件
4. `classpath:config/`：读取classpath下config文件夹中的配置文件
5. `classpath:`：读取classpath下的配置文件

我们通常在`src/main/resources` 文件夹下创建的`application.properties` 文件的优先级是最低的，相同的配置项优先级高的会覆盖优先级低的，不同的配置项所有配置文件时累加互补的。

### 默认的配置文件加载名称

springboot 读取配置文件默认名称：

- application.properties
- application.yml

### 其他配置文件

application-{profile}.properties或者application-{profile}.yml

```properties
#启用，优先级最高
spring.profiles.active=profile
#引入多个
spring.profiles.include=profile1,profile2,...


spring.config.name
spring.config.location
spring.config.additional-location
```

在jar的外面，新建一个配置文件，然后运行jar命令后面跟上 --spring.config.location=x:xxxx/application.properties

```java
# java -jar xx.jar --spring.config.location=./application.properties
```

### properties文件

```java
//使用引用变量
user.username=张三
user.map.k=${user.username}

//当user.username2没定义时可以用:后面加上默认值
user.map.k2=${user.username2:李四} 
 
//使用随机值 
user.userid=${random.uuid} 
user.username=张三${random.value} 
user.password=qwp${random.int}
user.age=${random.int(10)}
```

### **YAML文件用法**

YAML是专门用来写配置文件的语言，文件的后缀是.yml或.yaml。

#### **语法**

- 大小写敏感
- 使用缩进表示层级关系

缩进时不允许使用Tag键，只允许使用空格

缩进的空格数目不重要，只要相同层级的元素左侧对齐即可

- \# 表示注释

```java
server:
 port: 8882 # 写法key: value，冒号后面必须有空格
 servlet:
  context-path: /springboot_demo
```

#### **数据结构**

- 字面量：单个值
- 对象：键值对
- 数组或集合：一组数据的集合

```java
#字面量：普通的值，字符串、数字、布尔等
number: 25
str: 'hello world'
flag: true

#对象或Map，也称为Map映射，包含属性和值
# 写法1：换行写，使用缩进
user:
  name: tom
  age: 21
# 写法2：行内写法
user: {name: tom,age: 21}

#数组或集合，如List、Set等
# 写法1：换行写，使用短横线
names:
 -tom
 -jack
 -alice
# 写法2：行内写法
names: [tom,jack,alice]
```

### **多环境配置**

可以为不同环境提供不同中的配置信息，如开发环境、测试环境、生产环境

两种方式：

- 创建多个properties文件
- 使用yml配置文件

#### **创建多个properties文件**

步骤：

1. 创建不同环境的properties文件 ，文件命名要必须application-­xxx.properties

   `application.properties`

   `application-dev.properties`

   `application-test.properties`

   `application-pro.properties`

2. 在application.properties文件中指定要激活的配置

```java
# 指定激活的配置
spring.profiles.active=pro
```

#### **使用yml配置文件**

- 单个yml中编写多个配置
- 编写多个yml文件，分别代表不同的配置

```java
#单个yml配置文件
spring:
  profiles:
    active: dev 
---

# 注意：文档块以---分隔，这个是第一个文档块
server:
  port: 8085
  
spring:
  profiles: test # 指定test，代表测试环境

---
# 这个代表第二个文档块
server:
  port: 8086

spring:
  profiles: dev # 指定dev，代表开发环境
  
---
# 这个代表第三个文档块
server:
  port: 8087

spring:
  profiles: pro # 指定pro，代表生产环境
```

```java
//多个yml配置文件
//application.yml
spring:
  profiles:
    active: dev
    
//application-test.yml
server:
  port: 8085
  
//application-dev.yml
server:
  port: 8086
  
//application-prod.yml
server:
  port: 8087
```

### 从配置文件获取值

**@Value和@ConfigurationProperties**

和配置文件绑定，可以从配置文件中为属性注入值

- @Value只能一个个为属性注入值，而@ConfigurationProperties可以整体为属性注入值
- @Value不支持复杂类型封装，而@ConfigurationProperties支持
- @Value支持Sqel表达式写法，如：@Value("#{12+56}")
- @Value不支持JSR303数据校验，而@ConfigurationProperties支持

#### 全局配置文件

application.properties和application.yml会默认加载

```java
//application.yml配置文件
user:
 username: admin
 age: 18
 status: true
 birthday: 2018/2/14
 address:
   province: 江苏省
   city: 南京市
 lists:
   -list1
   -list2
   -list3
 maps: {k1: v1,k2: v2}
 
//application.properties配置文件
user.username=tom
user.age=21
user.status=false
user.birthday=2017/7/12
user.address.province=山东省
user.address.city=威海市
user.lists=list1,list2,list2
user.maps.k1=v1
user.maps.k2=v2
```

```java
// 将当前Bean添加到容器中
@Component
// 默认读取全局配置文件获取值，将当前类中的属性与配置文件中的user前缀进行绑定
@ConfigurationProperties(prefix = "user")
public class User implements Serializable {
    private String username;
    private Integer age;
    private Boolean status;
    ...
}
 
// 使用@Value从配置文件中获取值
// 将当前Bean添加到容器中
@Component
public class User implements Serializable {
    @Value("${user.username}")
    private String username;
    @Value("${user.age}")
    private Integer age;
    @Value("${user.status}")
    private Boolean status;
    @Value("${user.birthday}")
    private Date birthday;
    //@Value不支持复杂类型封装
    private Address address;
    @Value("${user.lists}")
    private List<String> lists; 
}
```

#### **自定义配置文件**

```java
// 将当前Bean添加到容器中
@Component
// 加载外部的属性文件，两个注解都要有
@ConfigurationProperties(prefix = "user")
@PropertySource({"classpath:user.properties"})
public class User implements Serializable {...}
 
// 标注在程序入口主类上且只能加载外部的spring配置文件
@ImportResource({"classpath:spring.xml"})
@SpringBootApplication
public class SpringbootConfigApplication {...}
```

```java
//user.properties放在项目根目录下或者项目打成jar包后放在jar包同级目录下
user.username=zhangsan
user.password=123456

@Component
@PropertySource({"file:user.properties"})
public class User implements Serializable {
    @Value("${user.username}")
    private String username;
}
```

### 使用注解方式添加配置类

```java
// 标注在类上，表示这是一个配置类，相当于以前的spring配置文件
@Configuration
public class SpringConfig {
    //标注在方法上，向容器中添加组件，方法的返回值就是放入到SpringIOC容器中的对象，将方法名作为组件id
    @Bean
    public Address address(){
        Address address = new Address();
        address.setProvince("江苏");
        address.setCity("苏州");
        return address;
    }
}
```

## **热部署**

```java
//使用SpringBoot提供的devtools实现热部署
//原理：实现监控classpath下文件的变化，如果发生变化则自动重启
/* 只需要添加devtools依赖*/
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    /* 该依赖不传递*/
    <optional>true</optional>
</dependency>
```

## **SpringBoot启动初始化**

```java
@Component
@Order(1)//注解基于spring容器，数字越小优先级越高
public class RunnerLoadOne implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        ClassDo classDo = SpringContextUtil.getBean(ClassDo.class);
        classDo.setClassName("Java");
        System.out.println("------------容器初始化bean之后,加载资源结束-----------");
    }
}
```

## **SpringBoot获取jar包所在目录路径**

```java
ApplicationHome h = new ApplicationHome(getClass());
File jarF = h.getSource();
System.out.println(jarF.getParentFile().toString());
```

## SpringBoot Test

```java
/* 单元测试依赖 */
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>2.0.3.RELEASE</version>
    <scope>test</scope>
</dependency>
```

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import cn.yunpay.account.WebimtApplicationRunner;

//@RunWith是Junit4提供的注解，将Spring和Junit链接了起来。
@RunWith(SpringJUnit4ClassRunner.class)

/*
	@SpringBootTest替代了spring-test中的@ContextConfiguration注解，目的是加载ApplicationContext，启动spring容器。
    
	可以不指定classes启动程序类或locations配置文件的值，因为@SpringBootTest注解会自动检索程序，检索顺序是从当前包开始，逐级向上查找被@SpringBootApplication或@SpringBootConfiguration注解的类。
    
	使用webEnvironment参数指定了web服务环境，也可以不指定,该参数的值一共有四个可选值：
        MOCK：此值为默认值，该类型提供一个mock环境，可以和@AutoConfigureMockMvc或@AutoConfigureWebTestClient搭配使用，开启Mock相关的功能。注意此时内嵌的服务（servlet容器）并没有真正启动，也不会监听web服务端口。
        RANDOM_PORT：启动一个真实的web服务，监听一个随机端口。
        DEFINED_PORT：启动一个真实的web服务，监听一个定义好的端口（从application.properties读取）。
        NONE：启动一个非web的ApplicationContext，既不提供mock环境，也不提供真实的web服务。
*/
    
@SpringBootTest(classes=WebimtApplicationRunner.class,locations={"classpath:application.yml"},webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class AccountBasicServiceImplTest {
 
	@Autowired
	AccountBasicServiceImpl accountBasicServiceImpl;
	
    //本Test单元测试同类中的其他测试方法都不能添加带返回值的方法，方法类型也必须是void
	@Test
	public void testAddBaseAccount() {
		accountBasicServiceImpl.addBaseAccount("", "");
	}
}
```

参考：[SpringBoot Test及注解详解](https://www.cnblogs.com/myitnews/p/12330297.html)

### 打包跳过Test

可以在pom.xml 里面增加插件跳过测试的插件

```java
<plugin>
	<artifactId>maven-surefire-plugin</artifactId>
	<configuration>
		<skipTests>true</skipTests>
	</configuration>
</plugin>
```

还可以在idea界面点击右上角的⚡️，如下图，test生命周期就被跳过去了，灰色。

![image-20210715161355898](img_SpringBoot/image-20210715161355898.png)





















