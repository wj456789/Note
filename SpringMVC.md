# SpringMVC

## MVC

Web应用架构，思想是将所有客户端请求全部交由控制器，由控制器将其分发处理并将结果响应给客户端。

## SpringMVC原理

![img](F:%5Cfile_youdao%5Cwang1256116295@163.com%5C9bdaf9dd53b140259999c43e43cb6cdf%5Cspringmvc.jpg)



- DispatcherServlet

  SpringMVC核心控制器：前端控制器，主要作用是用来分发

- HandlerMapping

  映射处理器：根据请求url映射到具体的处理Handler

- HandlerAdapter

  适配处理器：用来适配不同的处理器Handler

  处理器有两种实现方式：实现接口、基于注解，所以执行之前需要先适配

- Handler

  处理器：执行处理具体业务，并产生数据模型Model和视图名View

  Handler就是Controller层的实现类，也称为Action或Controller

  Handler会将数据模型Model和视图名View封装成ModelAndView对象并返回

- ViewResolver

  视图解析器：根据视图名解析为具体的视图，一般多为jsp页面，然后封装为View对象

- View

  视图：使用具体的视图技术进行渲染，将Model模型数据渲染到页面上

  视图有很多种形式：jsp、freemarker、velocity、excel、pdf等

## SpringMVC程序

```xml
	//依赖整合包
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
</dependency>
```

### web.xml

```xml
<!-- 1.配置DispatherServlet，核心控制器 -->
<servlet>
    <servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springmvc.xml</param-value>
    </init-param>
</servlet>
<servlet-mapping>
    <servlet-name>springMVC</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

### springmvc.xml

核心配置文件

位置命名：

1. 使用默认位置，默认在WEB-­INF/目录下，名称为： 核心Servlet名称­-servlet.xml(如上述servlet-name为springMVC，默认为springMVC-servlet.xml)
2. 自定义位置，名称自定义springmvc.xml

配置方式：

根据Controller实现方式的不同，有两种方式定义Controller:

1. 实现接口
2. 基于注解

#### 实现接口

```java
//实现接口的Controller
//访问路径http://ip:port/hello
public class HelloController implements Controller {
    @Override
    public ModelAndView handleRequest(HttpServletRequest req,HttpServletResponse resp) throws Exception {
        String name = req.getParameter("name");
        ModelAndView mav=new ModelAndView();
        mav.addObject("msg","Hello "+name);
        mav.setViewName("hello");
        return mav;
    }
}
```

```xml
<!-- 2.配置HandlerMapping -->
<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
<!-- 3.配置HandlerAdapter -->
<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>
<!-- 4.配置Handler -->
<bean name="/hello" class="controller.HelloController"/>
<!-- 5.配置ViewResolver -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/view/"/>
    <property name="suffix" value=".jsp"/>
    <!-- 6.配置View，使用jsp视图技术渲染页面 -->
    <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
</bean>
```

#### 基于注解

```java
//基于注解的Controller
@Controller
public class HelloAnnotationController {
    @RequestMapping("/hello")
    public ModelAndView sayHello(String name){
        ModelAndView mav=new ModelAndView();
        mav.addObject("msg","您好 "+name);
        mav.setViewName("hello");
        return mav;
    }
}
```

```xml
<!-- 2.配置HandlerMapping -->
<!-- <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/> -->
<!-- 3.配置HandlerAdapter -->
<!-- <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/> -->

<!-- mvc的注解驱动，用来简化配置 -->
<mvc:annotation-driven/>

<!-- 4.配置Handler -->
<context:component-scan base-package="controller"/>
<!-- 5.配置ViewResolver -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/view/"/>
    <property name="suffix" value=".jsp"/>
    <!-- 6.配置View,如果是jsp视图技术，可以省略不写 -->
    <!-- <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" /> -->
</bean>
```

#### 其他常用配置

```xml
//springMVC.xml
<!-- 静态资源处理:当配置DispatcherServlet为 <url-pattern>/</url-pattern> 时，会拦截所有请求（包括静态资源），导致所有静态资源都无法访问 -->
    <!--使用tomcat提供的默认Servlet来处理静态资源
        1.如果使用的不是tomcat，则可能不生效，与tomcat耦合
        2.只能读取webapp下的资源，无法读取/WEB-INF/下的资源 -->
    <mvc:default-servlet-handler/>
    
    <!-- 使用SpringMVC提供的处理方式-->
    <mvc:resources mapping="/image/**" location="/WEB-INF/img/"/>
    <mvc:resources mapping="/css/**" location="/WEB-INF/css/"/>
    <mvc:resources mapping="/js/**" location="/WEB-INF/js/"/>



<!-- 直接访问jsp页面:默认不能直接访问WEB-INF目录下的jsp页面，一般都是在Controller中做转发映射，也可以直接配置 -->
<mvc:view-controller path="/userLogin" view-name="login"/>
```

## Controller详解

1. 方法返回值
2. 方法请求URL
3. 方法请求参数

### 方法返回值

有四种类型：

- ModelAndView 表示返回的为数据模型和视图

- String 表示返回的是视图

  三种写法(形式)：

  - 普通字符串——>表示视图名称
  - "forward:"+url——>转发
  - "redirect:"+url——>重定向

- void 将请求的url作为视图名称，很少使用

- Object 表示返回的是数据模型（一般返回的是json数据）

### 方法请求URL

#### URL写法

请求映射路径有三种写法：

- Ant风格（较少使用）
  - `/*`表示单层目录，匹配任意字符，可以没有字符，但正斜杠必须有
  - `**` 表示多层或单层目录，可以没有字符，正斜杠也可以没有
  - `?` 表示单个字符，必须有一个字符

- Rest风格
  - `{变量}`表示URL中的占位符，可以结合@PathVariable获取值
  - `{变量:正则}`表示使用正则表达式来限定值的格式

- 固定写法

  value和path互为别名，值为数组，可以指定多个值

```JAVA
//http://localhost:8080/test/s/aa/qwe/bvc/d
@RequestMapping("test/?/aa/*/b?c/**/d")
public String test(){
    return "hello";
}

@RequestMapping("test/{id:\\d+}/{name}")
public String test(@PathVariable String id,@PathVariable("name") String username){
    return "hello";
}

@RequestMapping(value={"/test","t"})
public String test(){
    return "hello";
}
```

#### @RequestMapping

将 HTTP 请求映射到控制器（controller类）的处理方法上，如果请求URL有后缀，如请求/rest.do、/rest.action、/rest先精确匹配，如果找不到，那么@RequestMapping("/rest")就会生效

##### 配置请求映射路径URL(value)

配置URL时以/开头和不以/开头的请求没什么区别，在转发中有区别：

- 添加时表示从项目根路径开始查找
- 不添加时表示从当前方法所在层级开始查找

```java
@Controller
@RequestMapping("/path")
public class Path1Controller{
    @RequestMapping("/showLogin")
    public String showLogin(){
        return "login";
    }
    
    @RequestMapping("/forwardLogin")
    public String showLogin(){
        //转发相对于/path下的Path1Controller的showlogin
        return "forward:showLogin";
        //转发Path2Controller的showlogin
        return "forward:/showLogin";
        //转发Path1Controller的showlogin
        return "forward:/path/showLogin";
    }
}

@Controller
public class Path2Controller{
    @RequestMapping("/showLogin")
    public String showLogin(){
        return "login";
    }
}
```

##### 根据请求方式访问(method)

限定请求方式：GET、POST、PUT、DELETE等

```java
//@RequestMapping(path = "/user/{id}",method = RequestMethod.GET)
@GetMapping("/user/{id}")
public String deleteUser(@PathVariable Integer id){
    return "hello";
} 

//@RequestMapping(path = "/user/{id}",method = RequestMethod.POST)
@PostMapping("/user/{id}")
public String selectUser(@PathVariable Integer id){
    return "hello";
}
```

##### 限定请求参数(params)

```java
//请求参数中必须包括id，且username必须等于admin,password不等于123
@RequestMapping(path="/test",params={"id","username=admin","password!=123"})
```

##### 限定请求的头部(headers)

```java
@RequestMapping(path="/test",headers={"Cookie","Accept-Language=zh-CN,zh;q=0.8"})
```

##### 指定请求的提交内容类型(consumes)

指定Content-Type的值

```java
@PostMapping(path="/test",consumes="application/json")
```

##### 指定返回的内容类型(produces)

 仅当request请求头中的(Accept)类型中包含该指定类型才返回

```java
@PostMapping(path="/test",produces="application/json")
```

###### 浏览器请求头Accept

其中Accept中 ：`application/xml;q=0.9, */*;q=0.8`

`*/*`该项表明可以接收任何MIME类型的;`application/json` 几种主流浏览器都可以自动解析。`q`是相对品质因子，范围 `0 =< q <= 1`，权重系数 q 值越大，请求越倾向于获得其`;`之前的类型表示的内容，若没有指定 q 值，则默认为1，若被赋值为0，则用于提醒服务器哪些是浏览器不接受的内容类型。

![img](img_SpringMVC/clipboard.png)

### 方法请求参数

#### JavaEE组件

- HttpServletRequest
- HttpServletResponse
- HttpSession

#### IO流

- InputStream/OutputStream
- Reader/Writer

#### 向界面传递数据

Model、Map、ModelMap

可以通过request.setAttribute(name, value)方法将数据存储到Request作用域中返回

```java
@RequestMapping("/test")
public String test(Model model,Map map,ModelMap modelMap){
    model.addAttribute("name","tom");
    map.put("age",20);
    modelMap.put("address","nanjing");
    modelMap.addAttribute("sex","male");
    return "result";
}
```

#### String和基本类型

- @RequestParam 表示参数来源于请求参数，默认所有参数都添加该注解，参数值来源于同名的请求参数
- @PathVariable 表示参数来源于URL
- @RequestHeader 表示参数来源于请求头
- @CookieValue 表示参数来源于Cookie
- @RequestBody 表示参数来源于请求体（只有post请求才会有）

```java
//形参前无配置默认省略@RequestParam,此时可以不传值，但是添加了@RequestParam则必须传值
//或者@RequestParam配置属性required=false可以不传，这时可以同时配置defaultVlaue当不传值的时候为默认值
@RequestMapping("/test")
public String test(@Requestparam(name="name",required=false,defaultValue="admin") String username,int age){
    return "result";
}

//默认情况下如果url结尾有后缀，会被自动截取掉,比如{filename}为传值/123.jpg,参数只接收123
@RequestMapping("/test/{name}/{age}/{filename}")
public String test(@PathVariable("name") String username,int age,String filename){
    return "result";
}

//cookie值为cookie:username=admin;JSESSIONID=dhbaisbdihasbashbda454
@RequestMapping("/test")
public String test(@RequestHeader("User-Agent") String userAgent,@RequestHeader("Cookie") String cookie){
    return "result";
}

@RequestMapping("/test")
public String test(@CookieValue String username,@CookieValue("JSESSIONID") String sessionId){
    return "result";
}

//post请求，rebo值为username=lisi&password=123
@RequestMapping("/test")
public String test(@RequestBody String rebo){
    return "result";
}
```

#### 自定义类型

@ModelAttribute 将请求数据转换为对象

条件：对象的属性名必须与请求中元素的名称相同

@ModelAttribute注解有两种用法：

- 在方法参数的前面添加该注解，可省略		

  作用：将请求参数转换为对象

- 在方法的上面添加该注解

  作用：在调用所有目标方法前都会调用添加@ModelAttribute注解的方法，并向模型中添加数据

```java
//userVo值为userVo{username='aaa',password='123',phone='12345678912',email='aaa@163.com',age=21}
@RequestMapping("/regist")
public String regist(@ModelAttribute UserVo userVo){
    return "success";
}


//执行此controller中其他任何一个方法都会先调用@ModelAttribute注解的方法，并以注解中的字段作为key，将方法返回值作为value添加到request作用域中
@Controller
@RequestMapping("/user")
public class UserController{
    @RequestMapping("/regist")
    public String regist(@ModelAttribute UserVo userVo){
        return "success";
    }
    
    @ModelAttribute("types")
    public List<String> getTypes(){
        List<String> list=Arrays.asList("服装","数码","商品");
        return list;
    }
}
```

#### 错误参数

Errors、BindingResult

用来接受错误信息，实现服务端的数据校验

实际开发中，既要做客户端表单校验，又要做服务端数据校验

```java
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/regist")
    public String regist(ModelAttribute UserVo userVo,Errors errors){
        //手动进行服务端数据校验
        if(userVo.getAge()<0|| userVo.getAge()>120){
            errors.reject("年龄只能在0-120之间"); //手动添加错误消息
        }

        //判断是否有错误
        if(errors.hasErrors()){
            System.out.println(errors);//服务端校验的错误消息一般只在后台处理
            return "regist";
        }
        System.out.println("UserController.regist,userVo:"+userVo);
        return "success";
    }

    @ModelAttribute("types")
    public List<String> getTypes(){
        System.out.println("UserController.getTypes");
        List<String> list = Arrays.asList("服装", "数码", "食品");
        return list;
    }
}
```

#### @SessionAttributes

作用：将模型中指定名称的数据存储到session中,并且该注解只能放在类的上面，而不能修饰方法。

`@SessionAttributes(value={"xxx"}, types={xxxx.class})`

- value：是通过键来指定放入HttpSession 的域中的值；
- types：是通过类型指定放入HttpSession 的域中的值；这个注解会将类中所有放入Request域中的对象同时放进HttpSession的域空间中

```java
@Controller
@RequestMapping("/session")
//分步页面将数据依次存储到session域的userVo对象中
@SessionAttributes("userVo")
public class SessionController {
    @RequestMapping("/step1")
    public String step1(){
        return "step1";
    }
    @RequestMapping("/step2")
    public String step2(UserVo userVo){
        System.out.println("SessionController.step2,"+userVo);
        return "step2";
    }
    @RequestMapping("/step3")
    public String step3(UserVo userVo){
        System.out.println("SessionController.step3,"+userVo);
        return "step3";
    }
    @RequestMapping("/regist")
    public String regist(UserVo userVo,HttpSession session){
        System.out.println(session.getAttribute("userVo"));
        System.out.println("SessionController.regist,"+userVo);
        return "success";
    }
}
```

```jsp
//step1.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h2>用户注册</h2>
    <form action="${pageContext.request.contextPath}/session/step2" method="post">
        用户名：<input type="text" name="username"> <br>
        密码：<input type="password" name="password"> <br>
        <input type="submit" value="下一步">
    </form>
</body>
</html>

//step2.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h2>用户注册</h2>
    <form action="${pageContext.request.contextPath}/session/step3" method="post">
        手机号：<input type="text" name="phone"> <br>
        邮箱：<input type="text" name="email"> <br>
        <input type="submit" value="下一步">
    </form>
</body>
</html>


//step3.jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h2>用户注册</h2>
    <form action="${pageContext.request.contextPath}/session/regist" method="post">
        年龄：<input type="text" name="age"> <br>
        地址：<input type="text" name="address"> <br>
        <input type="submit" value="注册">
    </form>
</body>
</html>
```

## 服务端数据校验

### JSR303校验

JSR303是一个数据验证的标准规范，用于对Java Bean中的属性进行校验，称为Bean Validation，提供了常用的校验注解

### Hibernate Validator

是JSR303的一个参考实现，并提供了扩展注解

### 依赖包

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator-annotation-processor</artifactId>
</dependency>
 
//SpringBoot依赖 
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 验证信息的注解

当不符合要求时就会在方法中返回message 的错误提示信息

```java
public class UserVo {
    @NotEmpty(message = "用户名不能为空")
    @Pattern(regexp = "\\w{6,10}",message = "用户名只能包含数字、字母、下划线，且长度为6-10位")
    private String username;
    
    @Length(min = 4,max = 10,message = "密码必须为4-10位")
    private String password;

    @Pattern(regexp = "(139|133|131)\\d{8}",message = "手机号码格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Range(min = 1,max = 120,message = "年龄必须在1-120之间")
    private Integer age;

    private Address address;
}
```

**空检查**

- @Null	限制只能为null
- @NotNull	限制必须不为null
- @NotEmpty	验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
- @NotBlank	验证注解的元素值不为空(不为null、去除首位空格后长度为0),不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格

**Boolean检查**

- @AssertFalse	限制Boolean 对象必须为false
- @AssertTrue	限制Boolean 对象必须为true

**数值检查**

- @DecimalMax(value)	限制必须为一个不大于指定值的数字， 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度 
- @DecimalMin(value)	限制必须为一个不小于指定值的数字， 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度 

- @Max(value)，标注数字，限制必须为一个不大于指定值的数字
- @Min(value)，标注数字，限制必须为一个不小于指定值的数字

**日期检查**

- @Future 限制必须是一个将来的日期
- @Past	限制必须是一个过去的日期

**长度检查**

- @Length(min=, max=)，标注属性（String），检查字符串长度是否符合范围
- @Size(max,min)	限制字符长度必须在min到max之间

**其他**

- @Pattern(regex="regexp", flag=)	限制必须符合指定的正则表达式
- @Email	验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式

1. 使用ValidatorFactory 进行手动参数校验，具体参考[Java @Valid 注解详解 校验实体属性（Java Bean Validation）](https://blog.csdn.net/weixin_43740223/article/details/100889250)
2. 使用@Valid+errors/bindingResult，无需手动配置，需要处理报错信息
3. 使用@Valid+@ControllerAdvice，对报错信息进行统一处理























