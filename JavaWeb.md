# JavaWeb

## Servlet

### 概念

- Servlet是运行在服务端的Java程序，由servlet容器(或者称为web容器)进行管理，由容器来实例化和调用 Servlet的方法，常见的容器有tomcat等。
- Servlet可以创建并返回客户请求的动态HTML页面，也可以与其他服务器进行通信等。
- Servlet中四个常用对象：ServletConfig、ServletContext、ServletRequest、ServletResponse。
- Servlet容器在收到我们浏览器的url请求后，通过web.xml配置文件中Servlet映射配置，默认自动实例化相应的Servlet接口的实现类。

### Servlet生命周期

1. 实例化：构造方法，只会被执行一次，意味着Servlet是单例的，在整个Servlet容器中每个具体的Servlet实现类都只有一个实例！

2. init：初始化，只执行一次，init(ServletConfig arg0)

3. service：被多次调用，每次请求都会调用一次service方法。

4. destroy：只被调用一次，web应用被卸载前执行，用于释放Servlet占用的计算机资源。

web.xml中Servlet映射配置中`<load-on-startup>整数</load-on-startup>`，这个配置的整数若为0或正整数时表示web应用启动的时候，自动实例化对应的Servlet，整数越小，越优先执行。若为负数，则会在第一次请求时实例化对象的Servlet。

### ServletConfig

(使用`<init-param>`标签在web.xml中`<servlet>`标签中配置)：在web容器创建servlet实例对象时实例化初始化，并在调用servlet的init方法时， 将ServletConfig对象传递给servlet。

ServletConfig接口的定义的方法：

- `String getServletName()` -- 获取当前Servlet在web.xml中配置的名字
- `String getInitParameter(String name)` -- 获取当前Servlet指定名称的初始化参数的值
- `Enumeration getInitParameterNames()` -- 获取当前Servlet所有初始化参数的名字组成的枚举
- `ServletContext getServletContext()` -- 获取代表当前web应用的ServletContext对象

### ServletContext

(使用`<context-param>`标签在web.xml中配置)：web容器启动时，为每个web应用程序都创建一个ServletContext对象,而一个web应用中的所有Servlet共享同一个ServletContext对象。

servletContext中常用的方法有：

- 同ServletConfig
- `void setAttribute(String,Object)`
- `Object getAttribute(String)`
- `void removeAttribute(String)`

如：

```java
// 一次性获取Context里所有的初始化参数
Enumeration enumeration = context.getInitParameterNames();
while (enumeration.hasMoreElements()) {
    String name = (String) enumeration.nextElement();
    String value = context.getInitParameter(name);
    System.out.println(name + ";" + value);
}

context.getRealPath("/");//获取web应用的全部路径根目录。如：D:\tomcat\webapps\code
context.getContextPath();//获取web应用的部分路径根目录。如：\code
```

### ServletRequest

封装了http请求信息

servletRequest中常用的方法有：

- void setAttribute(String,Object);
- Object getAttribute(String);		获取命名属性值
- void removeAttribute(String);

- String getParameter(String name) 		获取请求参数值
- Map getParameterMap()				返回键值对
- Enumeration getParameterNames()
- String[] getParameterValues(String name)	根据key获取value,比如表单提交多选框

```java
httpreq.getRequestURI()		ip:port/项目名/servlet映射名(请求全路径)
httpreq.getContextPath()	/项目名
httpreq.getServletPath()	/servlet映射名
httpreq.getMethod()			GET/POST
httpreq.getQueryString()	username=aaa&password=aaa
```

### ServletResponse

封装了http响应信息

### Servlet接口继承关系

**Servlet-->GenericServlet-->HttpServlet(doGet(),doPost())**

## JSP

### 概念

- 全名为Java Server Pages，中文名叫java服务器页面，其根本是一个简化的Servlet设计。它是在HTML文件中插入Java程序段和JSP标记从而形成JSP文件。
- 容器会自动将index.jsp翻译为index_jsp.java文件，这个类继承了HttpServlet。

### JSP隐含对象

`request，response，pageContext，session，application，config，out，page，exception`由容器自动在转化jsp页面为Servlet的时候帮我们声明定义了，所以我们在jsp页面中使用这些变量不需要在声明，直接使用。

### JSP语法

- <%="变量或常量"%>：向浏览器输出内容
- <%Java代码,以分号结束%>：java代码会出现在自动生成的servlet类的service方法中
- <%! 声明实例变量或普通方法%>：定义成员变量和除了service以外的其他方法
- <%-- JSP特有的注释 --%>

### JSP四大域对象(可供共享的对象)

- PageContext：只能应用在当前页面的一次请求中,不能在servlet中使用，只能在jsp页面中使用
- request：只要在同一个请求中，不论该请求经过N个动态资源，只能是转发
- session：只要在一次新会话中（浏览器不关），不论该请求经过N个动态资源，不论转发还是重定向
- application(ServletContext)：只要在当前web应用中，不论该请求经过N个动态资源，不论转发还是重定向，不论多个新会话

域对象属性相关方法：

- setAttribute(name, o)：设置属性
- getAttribute(name)：根据属性名字，获取属性的值
- getAttributeNames()：获取所有属性名字，以枚举类型返回
- removeAttribute(name)：移除指定名字的属性

## 转发和重定向

```java
//index.html页面---->Servlet1------>Servlet2
request.getRequestDispatcher("/servlet2").forward(request, response);	//转发
response.sendRedirect("servlet2");										//重定向
```

- 转发只能转发到Web应用内部的资源，重定向可以到任何资源。
- 转发中地址用“/”表示web应用的根目录(webapp下的目录，即`http://locahost:8080/项目名/`)，重定向中地址“/”是站点的根目录(即`http://locahost:8080/`)。

## JavaWeb相对目录

- 在浏览器看来，根目录指的是服务器的根目录` http://localhost/`
- 在 web 应用看来，根目录指的是自己的根目录` http://localhost/project/`

适用服务器根目录: `http://localhost/`

- jsp: 

  - 超链接:`<a href=resquest.getContextPath()+"/a.jsp">超链接</a>`

  - form表单:`<form action=resquest.getContextPath()+"/a.jsp">`

- Servlet：重定向response.sendRedirect(resquest.getContextPath()+"/a.jsp");

适用web应用根目录:`http://localhost/project/`

- Servlet：转发 request.getRequestDispatcher("/a.jsp")
- web.xml：servlet-mapping中的url映射

## 中文乱码

### JSP页面

```java
//contentType属性指定JSP页面的MIME（浏览器打开JSP文件格式）和编码格式
//pageEncoding属性用来指定JSP文件的编码格式(JSP文件保存时选择的编码格式)
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
```

### servlet代码

POST请求：`request.setCharacterEncoding("UTF-8");`

GET请求：tomcat-->conf--->server.xml--->:

`<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" useBodyEncodingForURI="true"/>`

## MVC

Model(模型，包括业务处理层service和数据访问层dao)-View(视图)-Controller(控制)

## cookie

在WEB应用中,我们把同一个浏览器与WEB服务器的一次一系列的各种交互活动称之为"会话"。

​    ![0](data:image/jpeg;base64,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)

其实本质上cookies就是http的一个扩展。有两个http头部是专门负责设置以及发送cookie的,它们分别是Set-Cookie以及Cookie。其中如果包含Set-Cookie这个头部时，意思就是指示客户端建立一个cookie，并且在后续的http请求中自动发送这个cookie到服务器端，直到这个cookie过期。

```java
Cookie[] cookies=request.getCookies();
if(cookies!=null&&cookies.length>0){
    for(Cookie coo:cookies){
    	System.out.println("cookie的名字："+coo.getName()+",cookie的值："+coo.getValue());
    }
}else{
    Cookie cookie=new Cookie("uuid","1234");
    //int maxAge：该Cookie失效的时间，单位秒。
    //如果为正数，则该Cookie在>maxAge秒之后失效。如果为负数，该Cookie为临时Cookie，关闭浏览器即失效，浏览器也不会以任何形式保存该Cookie。如果为0，表示删除该Cookie。默认为–1。
    cookie.setMaxAge(7*24*60*60);
    response.addCookie(cookie);
}
```

## session

Session在用户第一次访问服务器的时候自动创建。需要注意只有访问JSP、Servlet等程序时才会创建Session，只访问html、image等静态资源并不会创建Session。如果尚未生成Session，也可以使用request.getSession(true)强制生成Session。

在创建了Session的同时，服务器会为该Session生成唯一的Session id，Session会保存在服务器中，发到客户端的只有Sessionid(放在Cookie文件里)；当客户端再次发送请求的时候，cookie会将这个Sessionid带上，服务器接受到请求之后就会依据Sessionid找到相应的Session，从而再次使用之。

Tomcat中Session的默认超时时间为20分钟。通过setMaxInactiveInterval(int seconds)修改超时时间。也可以修改web.xml改变Session的默认超时时间。例如修改为60分钟：

```java
<session-config>
    <session-timeout>60</session-timeout>  <!-- 单位：分钟 -->
</session-config>
```

## URL地址重写

URL地址重写的原理是将该用户Session的id信息重写到URL地址中，而不是将SessionId放在cookie中。

HttpServletResponse类提供了encodeURL(Stringurl)实现URL地址重写，该方法会自动判断客户端是否支持Cookie。如果客户端支持Cookie，会将URL原封不动地输出来。如果客户端不支持Cookie，则会将用户Session的id重写到URL中。
例如：

```java
//超链接重写
<a href="<%=response.encodeURL("index.jsp?c=1&d=78") %>">
重写后：
<a href="<%=response.encodeURL("index.jsp;jsessionid=XXX?c=1&d=78") %>">

//重定向重写
response.sendRedirect(response.encodeRedirectURL("index.jsp"));
```

### 禁用cookie中的sessonid

打开项目根目录下的META-INF文件夹（跟WEB-INF文件夹同级，如果没有则创建），打开context.xml（如果没有则创建），编辑内容如下：

```java
//修改/META-INF/context.xml
<? xml version='1.0' encoding='UTF-8'?>
<Context path="/sessionWeb" cookie="false"/>
```

或者修改tomcat全局

```java
//修改conf/context.xml
<Context cookie="false"/>              
```

配置后Cookie中无JSESSIONID，但是cookie依然可以用，只是禁用了cookie中的sessonid

## Filter

### 实现

对用户请求进行预处理，也可以对HttpServletResponse进行后处理。

1. 编写java类实现Filter接口，并实现其doFilter方法。

2. 在web.xml文件中对编写的filter类进行注册，并设置它所能拦截的资源。

```java
//使用@WebFilter(urlPatterns = {"/user/*","/account/*"}, filterName = "DemoFilter")注解不需要在web.xml中配置
public class DemoFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DemoFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("come into DemoFilter init...");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("come into DemoFilter and do processes...");
        // 实际业务处理，这里就是下面图中的before doFilter逻辑
        HttpServletRequest request = (HttpServletRequest) request;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loginUser")) {
                    logger.info("find loginUser: " + cookie.getValue());
                    break;
                }
            }
        }
        // 当前过滤器处理完了交给下一个过滤器处理
        chain.doFilter(request, response);
        logger.info("DemoFilter process has completed!");
    }
    @Override
    public void destroy() {
        logger.info("come into DemoFilter destroy...");
    }
}
```

```java
<filter>
    <filter-name>DemoFilter</filter-name>
    <filter-class>cn.shzx.filter.DemoFilter</filter-class>
    <init-param>
    	<param-name>username</param-name>
     	<param-value>wj</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>DemoFilter</filter-name>
    <url-pattern>/*</url-pattern>
    //<dispatcher>指定过滤器所拦截的目标资源被访问调用的方式，可以是REQUEST,INCLUDE,FORWARD和ERROR之一，默认REQUEST。
    <dispatcher>REQUEST</dispatcher>
</filter-mapping>
```

### Filter的生命周期

Filter的创建和销毁由web容器控制

1. 容器启动的时候，web容器实例化并调用init()方法初始化Filter实例。filter对象只会创建一次，init方法也只会执行一次。

2. 拦截到请求时，执行doFilter方法。可以执行多次。

3. 容器关闭时，web容器销毁Filter的实例对象。

### FilterConfig

- String getFilterName()：得到filter的名称。
- String getInitParameter(String name)：返回在部署描述中指定名称的初始化参数的值。如果不存在返回null.
- Enumeration getInitParameterNames()：返回过滤器的所有初始化参数的名字的枚举集合。
- public ServletContext getServletContext()：返回Servlet上下文对象的引用。

### FilterChain

将拦截下的请求转发`chain.doFilter(req,resp)`

一个web应用中有很多个过滤器对某些web资源进行拦截，那么这组过滤器就称为过滤器链。过滤器的执行顺序和`<filter-mapping>`有关（谁在前先执行谁）和过滤器在web.xml文件中的`<filter>`配置顺序无关。

### Filter禁用浏览器缓存

```java
// 在response的头部设置Cache-Control、Pragma和Expires即可取消缓存
HttpServletResponse resp = (HttpServletResponse)response;
resp.setHeader("Cache-Control", "no-cache");
resp.setHeader("Pragma", "no-cache");
resp.setDateHeader("Expires", -1);
```

## Listener

1. 通过实现监听接口（可实现多个监听器接口）
2. 配置实现类成为监听器，或者将注册监听器,有两种配置方式：

- 通过web.xml方式配置，代码如下：

```java
<listener>
	<listener-class>com.zrgk.listener.MyListener</lisener-class>
</listener>
```

- 直接用@WebListener注解修饰实现类



常用的Web事件的监听接口如下：

- ServletContextListener：用于监听Web的启动及关闭
- ServletContextAttributeListener：用于监听ServletContext范围内属性的改变
- ServletRequestListener：用于监听用户请求
- ServletRequestAttributeListener：用于监听ServletRequest范围属性的改变
- HttpSessionListener：用于监听用户session的开始及结束
- HttpSessionAttributeListener：用于监听HttpSession范围内的属性改变

```java
@WebListener
public class MyServetContextListener implements ServletContextListener{
    //web应用关闭时调用该方法
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext application = event.getServletContext();
        String userName = application.getInitParameter("userName");
        System.out.println("关闭web应用的用户名字为："+userName);
    }
    //web应用启动时调用该方法
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext application = event.getServletContext();
        String userName = application.getInitParameter("userName");
        System.out.println("启动web应用的用户名字为："+userName);
    }
}
```

## 配置文件的路径问题

### URL拦截路径

两种配置文件url拦截路径都是相对于`http://ip:port/contextpath`目录

#### web.xml的url拦截路径

- `<url-pattern>/<url-pattern>`会匹配到/login这样的路径url和静态资源(`*.js`和`*.html`等)，但不会匹配到`*.jsp`。
- `<url-pattern>/*<url-pattern>`会匹配到所有url(包括静态资源`*.js`和`*.html`等)，也会匹配到`*.jsp`。
- `<url-pattern>/login/*<url-pattern>`会匹配到所有以login/为前缀的路径
- `<url-pattern>*.do<url-pattern>`会匹配到所有以.do结尾的路径

#### 普通配置文件的url拦截路径

/*表示拦截请求根目录下的所有资源的路径，只是单层目录，不包括子目录。
/**表示拦截请求的所有路径。

### 配置文件的资源路径

web.xml和SpringMVC中的资源都是相对于web应用的根目录，比如：

```java
 <mvc:resources mapping="/image/**" location="/WEB-INF/img/"/>
```

```java
<error-page>         
    <error-code>404</error-code>     
    <location>/error.html</location>
</error-page>
```

也可以用classpath和file为前缀分别表示类路径下和系统绝对路径

## Http

请求包括请求头、请求头、请求体；响应包括状态行、响应头、响应体；

### HTTP Request Header 请求头

| Accept              | 指定客户端能够接收的内容类型，服务器可以返回的数据类型       | Accept: text/plain, text/html                           |
| ------------------- | ------------------------------------------------------------ | ------------------------------------------------------- |
| Accept-Charset      | 浏览器可以接受的字符编码集。                                 | Accept-Charset: iso-8859-5                              |
| Accept-Encoding     | 指定浏览器可以支持的web服务器返回内容压缩编码类型。          | Accept-Encoding: compress, gzip                         |
| Accept-Language     | 浏览器可接受的语言                                           | Accept-Language: en,zh                                  |
| Accept-Ranges       | 可以请求网页实体的一个或者多个子范围字段                     | Accept-Ranges: bytes                                    |
| Authorization       | HTTP授权的授权证书                                           | Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==       |
| Cache-Control       | 指定请求和响应遵循的缓存机制                                 | Cache-Control: no-cache                                 |
| Connection          | 表示是否需要持久连接。（HTTP 1.1默认进行持久连接）           | Connection: close                                       |
| Cookie              | HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器。 | Cookie: $Version=1; Skin=new;                           |
| Content-Length      | 请求的内容长度                                               | Content-Length: 348                                     |
| Content-Type        | 请求的与实体对应的MIME信息，服务器可以接受的数据类型         | Content-Type: application/x-www-form-urlencoded         |
| Date                | 请求发送的日期和时间                                         | Date: Tue, 15 Nov 2010 08:12:31 GMT                     |
| Expect              | 请求的特定的服务器行为                                       | Expect: 100-continue                                    |
| From                | 发出请求的用户的Email                                        | From: user@email.com                                    |
| Host                | 指定请求的服务器的域名和端口号                               | Host: www.zcmhi.com                                     |
| If-Match            | 只有请求内容与实体相匹配才有效                               | If-Match: “737060cd8c284d8af7ad3082f209582d”            |
| If-Modified-Since   | 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码 | If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT        |
| If-None-Match       | 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变 | If-None-Match: “737060cd8c284d8af7ad3082f209582d”       |
| If-Range            | 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag | If-Range: “737060cd8c284d8af7ad3082f209582d”            |
| If-Unmodified-Since | 只在实体在指定时间之后未被修改才请求成功                     | If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT      |
| Max-Forwards        | 限制信息通过代理和网关传送的时间                             | Max-Forwards: 10                                        |
| Pragma              | 用来包含实现特定的指令                                       | Pragma: no-cache                                        |
| Proxy-Authorization | 连接到代理的授权证书                                         | Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ== |
| Range               | 只请求实体的一部分，指定范围                                 | Range: bytes=500-999                                    |
| Referer             | 先前网页的地址，当前请求网页紧随其后,即来路                  | Referer: http://www.zcmhi.com/archives/71.html          |
| TE                  | 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息     | TE: trailers,deflate;q=0.5                              |
| Upgrade             | 向服务器指定某种传输协议以便服务器进行转换（如果支持）       | Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11          |
| User-Agent          | User-Agent的内容包含发出请求的用户信息                       | User-Agent: Mozilla/5.0 (Linux; X11)                    |
| Via                 | 通知中间网关或代理服务器地址，通信协议                       | Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)             |
| Warning             | 关于消息实体的警告信息                                       | Warn: 199 Miscellaneous warning                         |











































