

> 作者：千锋-索尔
>
> 版本：QF1.0
>
> 版权：千锋Java教研院



# 一、Spring Security简介

> Spring Security是一个功能强大且高度可定制的身份验证和访问控制框架。Spring Security致力于为Java应用程序提供身份验证和授权的能力。像所有Spring项目一样，Spring Security的真正强大之处在于它可以轻松扩展以满足定制需求的能力。

![角色和权限时许](pic/角色和权限时许.png)

> Spring Security两大重要核心功能：**用户认证（Authentication）**和**用户授权（Authorization）**。
>
> - 用户认证：验证某个用户是否为系统中的合法主体，也就是说用户能否访问该系统。用户认证一般要求用户提供用户名和密码。系统通过校验用户名和密码来完成认证过程。
> - 用户授权：验证某个用户是否有权限执行某个操作。在一个系统中，不同用户所有的权限是不同的。比如对一个文件来说，有的用户只能进行读取，有的用户既能读取，又能修改。一般来说，系统会为不同的用户分配不同的角色，而每个角色则对应一系列的权限。



# 二、快速开始

> 使用Springboot工程搭建Spring Security项目。

## 1.引入依赖

> 在pom中新增了Spring Security的依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
```



## 2.创建测试访问接口

> 用于访问接口时触发Spring Security登陆页面

```java
package com.qf.my.ss.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * web controller
 * @author Thor
 * @公众号 Java架构栈
 */
@RestController
public class SecurityController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello security";
    }
}

```



## 3.访问接口，自动跳转至Security登陆页面

> 访问add接口，讲自动跳转至Security的登陆页面

![image-20210305105043157](pic/login.png)



> 默认账号是: user
>
> 默认密码是：启动项目的控制台中输出的密码

![快速开始](pic/log.png)



# 三、Spring Security基础概念

> 在上一节中访问add接口，发现被Spring Security的登陆页面拦截，可以猜到这是触发了Security框架的过滤器。Spring Security本质上就是一个过滤器链。下面讲介绍Security框架的过滤器链。



##  1.过滤器链

> - WebAsyncManagerIntegrationFilter：将SecurityContext集成到Spring MVC中用于管理异步请求处理的WebAsyncManager中。
> - SecurityContextPersistenceFilter：在当前会话中填充SecurityContext，SecurityContext即Security的上下文对象，里面包含了当前用户的认证及权限信息等。
> - HeaderWriterFilter：向请求的Header中添加信息
> - CsrfFilter：用于防止CSRF（跨域请求伪造）攻击。Spring Security会对所有post请求验证是否包含系统生成的CSRF的信息，如果不包含则报错。
> - LogoutFilter：匹配URL为“/logout”的请求，清除认证信息，实现用户注销功能。
> - UsernamePasswordAuthenticationFilter：认证操作的过滤器，用于匹配URL为“/login”的POST请求做拦截，校验表单中的用户名和密码。
> - DefaultLoginPageGeneratingFilter：如果没有配置登陆页面，则生成默认的认证页面
> - DefaultLogoutPageGeneratingFilter：用于生成默认的退出页面
> - BasicAuthenticationFilter：用于Http基本认证，自动解析Http请求头中名为Authentication的内容，并获得内容中“basic”开头之后的信息。
> - RequestCacheAwareFilter：用于缓存HttpServletRequest
> - SecurityContextHolderAwareRequestFilter：用于封装ServletRequest，让ServletRequest具备更多功能。
> - AnonymousAuthenticationFilter：对于未登录情况下的处理，当SecurityContextHolder中认证信息为空，则会创建一个匿名用户存入到SecurityContextHolder中
> - SessionManagementFilter：限制同一用户开启多个会话
> - ExceptionTranslationFilter：异常过滤器，用来处理在认证授权过程中抛出异常。
> - FilterSecurityInterceptor：获取授权信息，根据SecurityContextHolder中存储的用户信息判断用户是否有权限访问

 

## 2.过滤器加载过程源码

> Springboot在整合Spring Security项目时会自动配置**DelegatingFilterProxy**过滤器，若非Springboot工程，则需要手动配置该过滤器。

​    

![springsecurity过滤器链](pic/springsecurity过滤器链.png)



> 过滤器如何进行加载的？
>
> 结合源码，Security在**DelegatingFilterProxy**过滤器的doFilter()调用了getBean()方法获取到了FilterChainProxy对象，容器已经把所有过滤器封装到了此对象中，FilterChainProxy的doFilter()获取过滤链中的所有过滤器并依次执行。

```java
public interface Filter {
    default void init(FilterConfig filterConfig) throws ServletException {}

    void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException;

    default void destroy() {}
}

public interface FilterChain {
    void doFilter(ServletRequest var1, ServletResponse var2) throws IOException, ServletException;
}

public interface SecurityFilterChain {
	boolean matches(HttpServletRequest request);
	List<Filter> getFilters();
}
```



```java
public abstract class GenericFilterBean implements Filter, BeanNameAware, EnvironmentAware, EnvironmentCapable, ServletContextAware, InitializingBean, DisposableBean {
    ...
}


// 1. 在DelegatingFilterProxy拿到springSecurity的过滤器链，并调用它的doFilter()方法；同时把原来的过滤器链传递下去
public class DelegatingFilterProxy extends GenericFilterBean {
    @Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        Filter delegateToUse = initDelegate(wac);
        invokeDelegate(delegateToUse, request, response, filterChain);
        ...
    }
    
    protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
        // targetBeanName 是 springSecurityFilterChain，Filter 是 FilterChainProxy 类对象，此时过滤器链2中已经存放了上述十几种过滤器
        Filter delegate = wac.getBean(targetBeanName, Filter.class);
        ...
        return delegate;
    }
    
    protected void invokeDelegate(
			Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		delegate.doFilter(request, response, filterChain);
	}
    ...
}



public class FilterChainProxy extends GenericFilterBean {
    private List<SecurityFilterChain> filterChains;
    
    @Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		doFilterInternal(request, response, chain);
        ...
	}
    
    private void doFilterInternal(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
        List<Filter> filters = getFilters(fwRequest);
		VirtualFilterChain vfc = new VirtualFilterChain(fwRequest, chain, filters);
		vfc.doFilter(fwRequest, fwResponse);
        ...
	}
    
    private List<Filter> getFilters(HttpServletRequest request) {
		for (SecurityFilterChain chain : filterChains) {
			if (chain.matches(request)) {
				return chain.getFilters();
			}
		}
		return null;
	}
    
    
    // 2. springSecurity的过滤器链在list中封装了所有过滤器，依次调用所有过滤器；其中将过滤器链传入到过滤器中，使用过滤器回调的方式实现了责任链模式
    private static class VirtualFilterChain implements FilterChain {
		private final FilterChain originalChain;
		private final List<Filter> additionalFilters;
		private final int size;
		private int currentPosition = 0;
        ...

		private VirtualFilterChain(FirewalledRequest firewalledRequest,
				FilterChain chain, List<Filter> additionalFilters) {
			this.originalChain = chain;
			this.additionalFilters = additionalFilters;
			this.size = additionalFilters.size();
			this.firewalledRequest = firewalledRequest;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			if (currentPosition == size) {
                // 走回到原来的过滤器链
				originalChain.doFilter(request, response);
			}
			else {
				currentPosition++;
				Filter nextFilter = additionalFilters.get(currentPosition - 1);
                // 把springSecurity过滤器链传递给每个过滤器
				nextFilter.doFilter(request, response, this);
			}
            ...
		}
	}
}



public final class WebAsyncManagerIntegrationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		...
        // springSecurity过滤器链回调doFilter方法
		filterChain.doFilter(request, response);
	}
}
```







# 四、Spring Security的认证方式-基本认证

## 1.认证概念

> 所谓的认证，就是用来判断系统中是否存在某用户，并判断该用户的身份是否合法的过程，解决的其实是用户登录的问题。认证的存在，是为了保护系统中的隐私数据与资源，只有合法的用户才可以访问系统中的资源。
>

## 2.认证方式

> 在Spring Security中，常见的认证方式可以分为HTTP层面和表单层面，常见的认证方式如下:
>
> - HTTP基本认证
>
> - Form表单认证
>
> - HTTP摘要认证
>

## 3.基本认证

> HTTP基本认证是在RFC2616标准中定义的一种认证模式，它以一种很简单的方式与用户进行交互。HTTP基本认证可以分为如下4个步骤：
>
> - 客户端首先发起一个未携带认证信息的请求；
>
> - 然后服务器端返回一个401 Unauthorized的响应信息，并在WWW-Authentication头部中说明认证形式：当进行HTTP基本认证时，WWW-Authentication会被设置为Basic realm=“被保护的页面”；
>
> - 接下来客户端会收到这个401 Unauthorized响应信息，并弹出一个对话框，询问用户名和密码。当用户输入后，客户端会将用户名和密码使用冒号进行拼接并用Base64编码，然后将其放入到请求的Authorization头部并发送给服务器；
>
> - 最后服务器端对客户端发来的信息进行解码得到用户名和密码，并对该信息进行校验判断是否正确，最终给客户端返回响应内容。
>
> HTTP基本认证是一种无状态的认证方式，与表单认证相比，HTTP基本认证是一种基于HTTP层面的认证方式，无法携带Session信息，也就无法实现Remember-Me功能。另外，用户名和密码在传递时仅做了一次简单的Base64编码，几乎等同于以明文传输，极易被进行密码窃听和重放攻击。所以在实际开发中，很少会使用这种认证方式来进行安全校验。
>
> 基本认证的代码实现：
>

- **创建SecurityConfig配置类**

```java
package com.qf.my.ss.demo.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1.配置基本认证方式
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                //开启basic认证
                .httpBasic();
    }
}
```

- **Basic认证详解**

> 在未登录状态下访问目标资源时，查看响应头，可以看到WWW-Authenticate认证信息:WWW-Authenticate：Basic realm="Realm"，其中WWW-Authenticate: 表示服务器告知浏览器进行代理认证工作。Basic: 表示认证类型为Basic认证。realm="Realm": 表示认证域名为Realm域。
>
> 根据401和以上响应头信息，浏览器会弹出一个对话框，要求输入 用户名/密码，Basic认证会将其拼接成 “用户名:密码” 格式，中间是一个冒号，并利用Base64编码成加密字符串xxx；然后在请求头中附加 Authorization: Basic xxx 信息，发送给后台认证；后台需要利用Base64来进行解码xxx，得到用户名和密码，再校验 用户名:密码 信息。如果认证错误，浏览器会保持弹框；如果认证成功，浏览器会缓存有效的Base64编码，在之后的请求中，浏览器都会在请求头中添加该有效编码。
>

## 4.Http摘要认证

- 概念

> HTTP摘要认证和HTTP基本认证一样，也是在RFC2616中定义的一种认证方式，它的出现是为了弥补HTTP基本认证存在的安全隐患，但该认证方式也并不是很安全。**HTTP摘要认证会使用对通信双方来说都可知的口令进行校验，且最终以密文的形式来传输数据，所以相对于基本认证来说，稍微安全了一些**。
>
> **HTTP摘要认证与基本认证类似，基于简单的“挑战-回应”模型。**当我们发起一个未经认证的请求时，服务器会返回一个401回应，并给客户端返回与验证相关的参数，期待客户端依据这些参数继续做出回应，从而完成整个验证过程。

- 摘要认证核心参数

> 服务端给客户端返回的验证相关参数如下：

```
username: 用户名。

password: 用户密码。

realm: 认证域，由服务器返回。

opaque: 透传字符串，客户端应原样返回。

method: 请求的方法。

nonce: 由服务器生成的随机字符串，包含过期时间(默认过期时间300s)和密钥。

nc: 即nonce-count,指请求的次数，用于计数，防止重放攻击。qop被指定时，nc也必须被指定。

cnonce: 客户端发给服务器的随机字符串，qop被指定时，cnonce也必须被指定。

qop: 保护级别，客户端根据此参数指定摘要算法。若取值为 auth,则只进行身份验证；若取值为auth-int，则还需要校验内容完整性，默认的qop为auth。

uri: 请求的uri。

response: 客户端根据算法算出的摘要值，这个算法取决于qop。

algorithm: 摘要算法，目前仅支持MD5。

entity-body: 页面实体，非消息实体，仅在auth-int中支持。
```

> 通常服务器端返回的数据包括realm、opaque、nonce、qop等字段，如果客户端需要做出验证回应，就必须按照一定的算法得到一些新的数据并一起返回。**在以上各种参数中，对服务器而言，最重要的字段是nonce；对客户端而言，最重要的字段是response。**

- 摘要认证的实现

```java
package com.qf.my.spring.security.demo.config;

import com.qf.my.spring.security.demo.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

/**
 * 摘要认证的配置
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class DigestConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DigestAuthenticationEntryPoint digestAuthenticationEntryPoint;

    @Autowired
    private MyUserDetailService userDetailService;

    //配置认证入口端点，主要是设置认证参数信息
    @Bean
    public DigestAuthenticationEntryPoint digestAuthenticationEntryPoint(){
        DigestAuthenticationEntryPoint point = new DigestAuthenticationEntryPoint();
        point.setKey("security demo");
        point.setRealmName("thor");
        point.setNonceValiditySeconds(500);
        return point;
    }

    public DigestAuthenticationFilter digestAuthenticationFilter(){
        DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
        filter.setAuthenticationEntryPoint(digestAuthenticationEntryPoint);
        filter.setUserDetailsService(userDetailService);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/hello").hasAuthority("role")
                .anyRequest().authenticated()
                .and().csrf().disable()
                //当未认证时访问某些资源,则由该认证入口类来处理.
                .exceptionHandling()
                .authenticationEntryPoint(digestAuthenticationEntryPoint)
                .and()
                //添加自定义过滤器到过滤器链中
                .addFilter(digestAuthenticationFilter());

    }
}

```



# 五、Form表单认证

在SpringBoot开发环境中，只要我们添加了Spring Security的依赖包，就会自动实现表单认证。可以通过WebSecurityConfigurerAdapter提供的configure方法看到默认的认证方式就是表单认证

```java
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");

		http
			.authorizeRequests()
				.anyRequest().authenticated()
				.and()
			.formLogin().and()
			.httpBasic();
	}
```

## 1.表单认证中的预置url和页面

> 默认的formLogin配置中，自动配置了一些url和页面:
>
> - **/login(get)**: get请求时会跳转到这个页面，只要我们访问任意一个需要认证的请求时，都会跳转到这个登录界面。
> - **/login(post)**: post请求时会触发这个接口，在登录页面点击登录时，默认的登录页面表单中的action就是关联这个login接口。
> - **/login?error**: 当用户名或密码错误时，会跳转到该页面。
> - **/:** 登录成功后，默认跳转到该页面，如果配置了index.html页面，则 ”/“ 会重定向到index.html页面，当然这个页面要由我们自己实现。
> - **/logout:** 注销页面。
> - **/login?logout:** 注销成功后跳转到的页面。
>
> 由此可见，SpringSecurity默认有两个login，即登录页面和登录接口的地址都是 /login:
>
> - GET http://localhost:8080/login
> - POST http://localhost:8080/login
>
> 如果是 GET 请求，表示你想访问登录页面；如果是 POST 请求，表示你想提交登录数据。
> 对于这几个URL接口，我们简单了解即可。

## 2.自定义认证页面

- 自定义登陆页面

```html

<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap 101 Template</title>

    <!-- Bootstrap -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- HTML5 shim 和 Respond.js 是为了让 IE8 支持 HTML5 元素和媒体查询（media queries）功能 -->
    <!-- 警告：通过 file:// 协议（就是直接将 html 页面拖拽到浏览器中）访问页面时 Respond.js 不起作用 -->
    <!--[if lt IE 9]>
    <script src="https://cdn.jsdelivr.cn/npm/html5shiv@3.7.3/dist/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.cn/npm/respond.js@1.4.2/dest/respond.min.js"></script>
    <![endif]-->
    <style>
        .login-top{
            width: 600px;
            height: 300px;
            border: 1px solid #DCDFE6;
            margin: 150px auto;
            padding: 20px 50px 20px 30px;
            border-radius: 20px;
            box-shadow: 0px 0px 20px #DCDFE6;
        }
    </style>
</head>
<body>
<div class="login-top">
    <div>
        <h3>欢迎登陆</h3>
    </div>

    <form action="/login" method="post">

        <div class="form-group" style="padding-bottom: 20px">
            <label for="inputUsername" class="col-sm-2 control-label">用户名</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="inputUsername" name="username" placeholder="用户名">
            </div>
        </div>

        <div class="form-group">
            <label for="inputPassword" class="col-sm-2 control-label">密码</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="inputPassword" name="password" placeholder="密码">
            </div>
        </div>


        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <div class="checkbox">
                    <label>
                        <input type="checkbox"> 记住我
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">登陆</button>
            </div>
        </div>


    </form>

</div>
</body>
<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.1/jquery.js"></script><!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/5.2.2/css/bootstrap-utilities.min.css" rel="stylesheet"></body>
</html>

```

- 自定义首页

- 自定义错误页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <h3>用户名或密码错误</h3>
</body>
</html>
```

## 3.自定义配置项

```java
package com.qf.my.ss.demo.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/**","/css/**","/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //1.配置基本认证方式
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .permitAll()
//指登录成功后，是否始终跳转到登录成功url。它默认为false
                .defaultSuccessUrl("/index.html",true)
//post登录接口，登录验证由系统实现
                .loginProcessingUrl("/login")
//用户密码错误跳转接口
                .failureUrl("/error.html")
                //要认证的用户参数名，默认username
                .usernameParameter("username")
//要认证的密码参数名，默认password
                .passwordParameter("password")
                .and()
//配置注销
                .logout()
//注销接口
                .logoutUrl("/logout")
//注销成功后跳转到的接口
                .logoutSuccessUrl("/login.html")
                .permitAll()
//删除自定义的cookie
                .deleteCookies("myCookie")
                .and()
//注意:需禁用crsf防护功能,否则登录不成功
                .csrf()
                .disable();
    }
}

```



## 4.WebSecurity和HttpSecurity

> Spring Security内部是如何加载我们自定义的登录页面的？需要了解这两个类：WebSecurity和HttpSecurity。
>
> - WebSecurity
>
> 在这个类里定义了一个securityFilterChainBuilders集合，可以同时管理多个SecurityFilterChain过滤器链，
>
> 当WebSecurity在执行时，会构建出一个名为 **”springSecurityFilterChain“** 的 **Spring BeanFilterChainProxy代理类**，它的作用是来 **定义哪些请求可以忽略安全控制，哪些请求必须接受安全控制**；以及在合适的时候 **清除SecurityContext** 以避免内存泄漏，同时也可以用来 **定义请求防火墙和请求拒绝处理器**，也可以在这里 **开启Spring Security 的Debug模式**。
>
> - HttpSecurity
>
> HttpSecurity用来构建包含一系列的过滤器链SecurityFilterChain，平常我们的配置就是围绕着这个SecurityFilterChain进行。



# 六、自定义用户名和密码

> Spring Security提供了多种方式自定义用户名和密码。

## 1.使用application.properties

```properties
# 配置用户名
spring.security.user.name=qfadmin
# 配置密码
spring.security.user.password=123456
```

> 还需要向IOC容器里注入一个PasswordEncoder，用于生成密码的base64编码的字符串，和解析base64编码的字符串为实际密码内容。

```java
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
```

## 2.通过创建配置类实现设置

> 将用户名和密码写在配置类里，虽然配置类中可以自己编写用户名和密码的代码，但因为它是配置类的缘故，不适合将从数据库中获取用户名和密码的业务代码写入到配置类中。

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //用于密码的密文处理
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //生成密文
        String password = passwordEncoder.encode("123456");
        //设置用户名和密码
        auth.inMemoryAuthentication().withUser("qfAdmin").password(password).roles("admin");
    }
  
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
```



## 3.编写自定义实现类（常用）

- 设计数据库表

```sql
-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(60) NOT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `headImgUrl` varchar(255) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `telephone` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `sex` tinyint(1) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  `createTime` datetime NOT NULL,
  `updateTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

```

- 使用mybatis-generator生成映射文件
- 引入Mybatis和连接池的依赖

```xml
        <!--        mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--        druid连接-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!--        mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
```

- 编写application.properties配置文件

```properties
# 指明mapper映射文件的位置
mybatis.mapper-locations=classpath:mapper/*.xml
# 配置连接池Druid
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/db_security?serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=qf123456
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
```

- 启动类上打上注解

```java
@SpringBootApplication
@MapperScan("com.qf.my.ss.demo.mapper")
public class MySsDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySsDemoApplication.class, args);
    }
}
```

- 编写UserDetailService实现类

  > 编写从数据库中获取用户名和密码的业务

```java
package com.qf.my.ss.demo.service;

import com.mysql.cj.util.StringUtils;
import com.qf.my.ss.demo.entity.SysUser;
import com.qf.my.ss.demo.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //设置角色，角色的概念在之后章节介绍
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
        //可以从数据库获取用户名和密码
        if(StringUtils.isNullOrEmpty(username)){
            return null;
        }
        SysUser sysUser = userMapper.selectByUsername(username);
        User user = null;
        if(Objects.nonNull(sysUser)){
            user = new User(username,sysUser.getPassword(),auths);
        }
        return user;
    }
}
```

- 编写SecurityConfig配置类，指明对UserDetailsService实现类认证

```java
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
```



# 七、角色和权限

![Snipaste_2024-11-13_08-33-02](img_SpringSecurity/Snipaste_2024-11-13_08-33-02.png)

```
sys_user
sys_role
sys_permission
sys_role_permission
sys_role_user
```



## 1.角色和权限的概念

> 所谓权限，就是用户是否有访问当前页面，或者是执行某个操作的权利。
>
> 所谓角色，是对权限的汇总，比如“管理员”角色，可以对数据进行增删改查，增删改查是数据的四个权限，拥有“管理员”角色的用户拥有这四个权限。“普通用户”角色，只具备数据的增和查两种权限，那么拥有“普通用户”角色的用户只拥有这两个权限。
>
> Spring Security提供了四个方法用于角色和权限的访问控制。通过这些方法，对用户是否具有某个或某些权限，进行过滤访问。对用户是否具备某个或某些角色，进行过滤访问。
>
> - hasAuthority
> - hasAnyAuthority
> - hasRole
> - hasAnyRole

## 2.hasAuthority方法

> 判断当前主体是否有指定的权限，有返回true，否则返回false
>
> 该方法适用于只拥有一个权限的用户。

- 在配置类中设置当前主体具有怎样的权限才能访问。

```java
package com.qf.my.ss.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class PermissionConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/nopermission.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/login") //登陆时访问的路径
                .failureUrl("/error.html")//登陆失败的页面
                .defaultSuccessUrl("/index.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/login").permitAll() //设置可以直接访问的路径，取消拦截
                //1.hasAuthority方法：当前登陆用户，只有具有admin权限才可以访问这个路径
                .antMatchers("/index.html").hasAuthority("26")
                .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf防护
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

```

- 从数据库查询权限的Service

```java
package com.qf.my.ss.demo.service.impl;

import com.mysql.cj.util.StringUtils;
import com.qf.my.ss.demo.entity.SysUser;
import com.qf.my.ss.demo.mapper.SysRolePermissionMapper;
import com.qf.my.ss.demo.mapper.SysRoleUserMapper;
import com.qf.my.ss.demo.mapper.SysUserMapper;
import com.qf.my.ss.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysRoleUserMapper roleUserMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private SysUserMapper userMapper;


    @Override
    public List<Integer> getPermissonsByName(String username) {

        if(StringUtils.isNullOrEmpty(username)){
            return null;
        }
        SysUser sysUser = userMapper.selectByUsername(username);
        List<Integer> permissionIds = new ArrayList<>();
        if(Objects.nonNull(sysUser)){
            Integer id = sysUser.getId();
            List<Integer> roleIds = roleUserMapper.selectByUserId(id);
            if(!CollectionUtils.isEmpty(roleIds)){
                //查询全选
                roleIds.forEach(rid -> {
                    List<Integer> pIds = rolePermissionMapper.selectByRoleId(rid);
                    permissionIds.addAll(pIds);
                });
                //去重
                Set<Integer> pSet = new HashSet<>(permissionIds);
                permissionIds.clear();
                permissionIds.addAll(pSet);

            }
        }
        return permissionIds;
    }
}

```

- 在userdetailsService，为返回的User对象设置权限

```java
package com.qf.my.ss.demo.service;

import com.mysql.cj.util.StringUtils;
import com.qf.my.ss.demo.entity.SysUser;
import com.qf.my.ss.demo.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(StringUtils.isNullOrEmpty(username)){
            return null;
        }
        //从数据库获得该用户相关的权限
        List<Integer> permissons = permissionService.getPermissonsByName(username);
        //设置权限
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(
                permissons.stream().map(String::valueOf).collect(Collectors.joining(",")));
        SysUser sysUser = userMapper.selectByUsername(username);
        User user = null;
        if(Objects.nonNull(sysUser)){
            user = new User(username,sysUser.getPassword(),auths);
        }
        return user;
    }
}
```





## 3.hasAnyAuthority方法

> 适用于一个主体有多个权限的情况，多个权限用逗号隔开。

```java
package com.qf.my.ss.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class PermissionConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/nopermission.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/login") //登陆时访问的路径
                .failureUrl("/error.html")//登陆失败的页面
                .defaultSuccessUrl("/index.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/login").permitAll() //设置可以直接访问的路径，取消拦截
                //1.hasAuthority方法：当前登陆用户，只有具有admin权限才可以访问这个路径
                .antMatchers("/index.html").hasAnyAuthority("26,9")
                .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf防护
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}


```



## 4.hasRole方法

> 如果用户具备给定角色就允许访问，否则报403错误。

- 修改配置类

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/nopermission.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/login") //登陆时访问的路径
                .failureUrl("/error.html")//登陆失败的页面
                .defaultSuccessUrl("/index.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/login").permitAll() //设置可以直接访问的路径，取消拦截
                .antMatchers("/index.html").hasRole("1")
                .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf防护
    }
```

- 在PermissionServiceImpl添加获得角色的功能

```java
    @Override
    public List<Integer> getRoleByName(SysUser sysUser) {
        return roleUserMapper.selectByUserId(sysUser.getId());
    }
```



- 修改UserDetailsService

```java
//权限设置
@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户输入的用户名去数据库查询具体的用户对象
        if(StringUtils.isNullOrEmpty(username)){
            return null;
        }
        //数据库查询
        SysUser sysUser = userMapper.selectByUsername(username);
        User user = null;
        if(Objects.nonNull(sysUser)){
            //从数据库获得该用户相关的权限
        		List<Integer> permissons = permissionService.getPermissonsByName(username);
        		String perString = permissons.stream().map(String::valueOf).collect(Collectors.joining(","));

          	//从数据库获得该用户的角色
        		SysUser sysUser = userMapper.selectByUsername(username);
        		List<Integer> roles = permissionService.getRoleByName(sysUser);
        		String roleString = roles.stream().map(num -> "ROLE_" + num).collect(Collectors.joining(","));

        		//设置权限
        		List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(perString+","+roleString);

            user = new User(username,sysUser.getPassword(),auths);
        }
        return user;

    }
```

> 其中角色student需要在设置时加上“ROLE_”前缀，因为通过源码hasRole方法给自定义的角色名前加上了“ROLE_”前缀

```java
private static String hasRole(String role) {
        Assert.notNull(role, "role cannot be null");
        Assert.isTrue(!role.startsWith("ROLE_"), () -> {
            return "role should not start with 'ROLE_' since it is automatically inserted. Got '" + role + "'";
        });
        return "hasRole('ROLE_" + role + "')";
    }
```



## 5.hasAnyRole方法

> 设置多个角色，多个角色之间使用逗号隔开，只要用户具有某一个角色，就能访问。

```java
@Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/nopermission.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/login") //登陆时访问的路径
                .failureUrl("/error.html")//登陆失败的页面
                .defaultSuccessUrl("/index.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/login").permitAll() //设置可以直接访问的路径，取消拦截
                .antMatchers("/index.html").hasAnyRole("1","2")
                .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf防护
    }
```



# 八、自动登陆

JdbcTokenRepositoryImpl 会把token票据持久化到数据库 persistent_logins 表中





## 1. 准备数据库表

创建persistent_logins表，用于持久化自动登陆的信息。

```sql
create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)
```

## 2.实现自动登陆

- 修改SecurityConfig配置类

```java
package com.qf.my.ss.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@EnableWebSecurity
public class PermissionConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //配置数据源
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        

        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/nopermission.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/login") //登陆时访问的路径
                .failureUrl("/error.html")//登陆失败的页面
                .defaultSuccessUrl("/index.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                .antMatchers("/","/login").permitAll() 
                .antMatchers("/index.html").hasRole("1")
                .anyRequest().authenticated()
                //开启记住我功能
                .and().rememberMe().userDetailsService(userDetailsService)
                //持久化令牌方案
                .tokenRepository(tokenRepository)
                //设置令牌有效期，为7天有效期
                .tokenValiditySeconds(60*60*24*7)
                .and().csrf().disable(); //关闭csrf防护
    }

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

```

- 前端页面添加自动登陆表单项

```html
<div class="checkbox">
    <label>
        <input type="checkbox" name="remember-me"> 记住我
    </label>
</div>
```

## 3.自动登陆底层实现逻辑

> - 首先从前端传来的 cookie 中解析出 series 和 token；
>- 根据 series 从数据库中查询出一个 PersistentRememberMeToken 实例；
> - 如果查出来的 token 和前端传来的 token 不相同，说明账号可能被人盗用(别人用你的令牌登录之后，token 会变)。此时根据用户名移除相关的 token，相当于必须要重新输入用户名密码登录才能获取新的自动登录权限。
>- 接下来校验 token 是否过期;
> - 构造新的 PersistentRememberMeToken 对象，并且更新数据库中的 token(这就是我们文章开头说的，新的会话都会对应一个新的 token)；
>- 将新的令牌重新添加到 cookie 中返回；
> - 根据用户名查询用户信息，再走一波登录流程。
>

对比票据 token ，之后更新数据库中票据，将新的令牌添加到 cookie 中返回

# 九、用户注销

![](pic/用户注销流程.png)

## 1.在配置类添加注销的配置

```java
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        //注销的配置
        http.logout().logoutUrl("/logout") //注销时访问的路径
                .logoutSuccessUrl("/logoutSuccess").permitAll(); //注销成功后访问的路径

        //配置没有权限的跳转页面
        http.exceptionHandling().accessDeniedPage("/error.html");
        http.formLogin()
                .loginPage("/login.html") //设置自定义登陆页面
                .loginProcessingUrl("/usr/login") //登陆时访问的路径
//                .defaultSuccessUrl("/index").permitAll() //登陆成功后跳转的路径
                .defaultSuccessUrl("/success.html").permitAll() //登陆成功后跳转的路径
                .and().authorizeRequests()
                    .antMatchers("/","/add","/user/login").permitAll() //设置可以直接访问的路径，取消拦截
                    //1.hasAuthority方法：当前登陆用户，只有具有admin权限才可以访问这个路径
                    //.antMatchers("/index").hasAuthority("admin")
                    //2.hasAnyAuthority方法：当前登陆用户，具有admin或manager权限可以访问这个路径
                    //.antMatchers("/index").hasAnyAuthority("admin,manager")
                    //3.hasRole方法：当前主体具有指定角色，则允许访问
                    //.antMatchers("/index").hasRole("student")
                    //4.hasAnyRole方法：当前主体只要具备其中某一个角色就能访问
                    .antMatchers("/index").hasAnyRole("student1,teacher")
                .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf防护
    }
```





## 2.设置注销链接

添加success.html页面作为登陆成功后的跳转页面

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    登陆成功 <a href="/logout">退出</a>
</body>
</html>
```

登陆后访问退出按钮，实现注销功能。

# 十、JWT(Json Web Token)

## 1.基于Token的认证方式

> 使用基于Token的身份验证方法，在服务端不需要存储用户的登陆信息。流程如下：
>
> - 客户端使用用户名和密码请求登陆。
> - 服务端收到请求，去验证用户名和密码。
> - 验证成功后，服务端会签发一个Token，再把这个Token发送给客户端。
> - 客户端收到Token以后可以把它存储在Cookie本地。
> - 客户端每次向服务端请求资源时需要携带Cookie中该Token。
> - 服务端收到请求后，验证客户端携带的Token，如果验证成功则返回数据。

<img src="pic/jwt认证.png" alt="jwt认证" style="zoom:70%;" />



JavaWeb的session-cookie方案，用户登录认证把用户信息存放在 服务器端的session中，并把sessionid返回客户端存放cookie，客户端再次访问比对sessionid实现认证，但是不适用于分布式，因为session只能存在一个服务端中



## 2.什么是JWT

> JSON Web Token （JWT）是一个开放的行业标准（RFC 7519），它定义了一种简洁的、自包含的协议格式，用于在通信双方传递json对象，传递的信息经过数字签名可以被验证和信任。JWT可以使用HMAC算法或使用RSA的公钥/私钥对进行签名，防止被篡改。
>
> JWT官网： https://jwt.io
>
> JWT令牌的优点：
>
> - JWT基于json，非常方便解析。
> - 可以在令牌中自定义丰富的内容，易扩展。
> - 通过非对称加密算法及数字签名技术，JWT防止篡改，安全性高。
> - 资源服务使用JWT可不依赖认证服务即完成授权。
>
> JWT令牌的缺点：
>
> - JWT令牌较长，占存储空间比较大。



## 3.JWT组成

> 一个JWT实际上就一个字符串，它由三部分组成，头部、负载与签名。



### 1）头部（Header）

> 头部用于描述关于该JWT的最基本信息，例如其类型（即JWT）以及签名所用的算法（如HMAC SHA256 或 RSA）等。这也可以被表示成一个JSON对象。

```json
{
  "alg":"HS256",
  "typ":"JWT"
}
```

> - alg：签名算法
> - typ：类型
>
> 我们对头部的json字符串进行BASE64编码，编码后的字符串如下：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

> Base64是一种基于64个可打印字符串来表示二进制数据的表示方式。JDK提供了非常方便的Base64Encoder和Base64Decoder，用它们可以非常方便的完成基于Base64的编码和解码。



### 2）负载（Payload）

> 负载，是存放有效信息的地方，比如用户的基本信息可以存在该部分中。负载包含三个部分：
>
> - 标准中注册的声明（建议但不强制使用）
>   - iss：jwt签发者
>   - sub：jwt所面向的用户
>   - aud：接收jwt的一方
>   - exp：jwt的过期时间，过期时间必须大于签发时间
>   - nbf：定义在什么时间之前，该jwt都是不可用的
>   - iat：jwt的签发时间
>   - jti：jwt的唯一身份标识，主要用来作为一次性token，从而回避重放攻击。
>
> - 公共的声明
>
> 公共的声明可以添加任何信息，一般添加用户的相关信息或其他业务需要的必要信息，但不建议添加敏感信息，因为该部分在客户端可解密。
>
> - 私有的声明
>
> 私有声明是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该部分信息可以归类为明文信息。
>
> 私有声明也就是自定义claim，用于存放自定义键值对。

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}
```

> 其中sub是标准的声明，name是自定义的私有声明，编码后如下：

```
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
```



### 3）签证、签名（Signature）

> jwt的第三部分是一个签证信息，由三部分组成：
>
> - Header（Base64编码后）
> - Payload（Base64编码后）
> - Secret（盐，必须保密）
>
> **第一段是Base64编码后的header，第二段是base64编码后的payload，第三段是Base64编码后的header、base64编码后的payload、盐Secret使用.连接，然后通过header重声明的加密方式进行加密的字符串**——使用“qfjava”作为盐：

```
eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

> 从官方工具中可以看到，三个部分组合出的完整字符串：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.eZqdTo1mRMB-o7co1oAiTvNvumfCkt-1H-CdfNm78Cw
```

![image-20241216205410223](img_SpringSecurity/image-20241216205410223.png)



> 注意：secret是保存在服务器端的，jwt在签发生成也是在服务器端的，secret就是用来进行jwt的签发和验证，所以，它就是服务器端的私钥，在任何场景都不应该泄漏。一旦客户端得知这个secret，那就意味着客户端是可以自我签发jwt了。

## 4.使用JJWT

JJWT是一个提供端到端的JWT创建和验证的开源Java库。也就是说使用JJWT能快速完成JWT的功能开发。

- 引入依赖

> 创建Springboot工程并引入jjwt依赖，pom.xml如下：

```xml
        <!--jjwt-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>RELEASE</version>
        </dependency>
```

- 创建Token

```java
    @Test
    public void testCrtToken(){

        //创建JWT对象
        JwtBuilder builder = Jwts.builder().setId("1001")//设置负载内容
                .setSubject("小明")
                .setIssuedAt(new Date())//设置签发时间
                .signWith(SignatureAlgorithm.HS256, "qfjava");//设置签名秘钥
        //构建token
        String token = builder.compact();
        System.out.println(token);

    }
```



> JWT将用户信息转换成Token字符串，生成结果如下：

```
eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjE1MzY2MDEyfQ.2LNcw1v64TNQ96eCpWKvtAccBUA-cEVMDyJNMef-zu0
```



- 解析Token

> 通过JWT解析Token，获取Token中存放的用户信息，即生成Claims对象。

```java
    @Test
    public void testParseToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAxIiwic3ViIjoi5bCP5piOIiwiaWF0IjoxNjE1MzY2MDEyfQ.2LNcw1v64TNQ96eCpWKvtAccBUA-cEVMDyJNMef-zu0";
        //解析Token，生成Claims对象，Token中存放的用户信息解析到了claims对象中
        Claims claims = Jwts.parser().setSigningKey("qfjava").parseClaimsJws(token).getBody();
        System.out.println("id:" + claims.getId());
        System.out.println("subject:" + claims.getSubject());
        System.out.println("IssuedAt:" + claims.getIssuedAt());
    }
```

> 解析结果如下：

```
id:1001
subject:小明
IssuedAt:Wed Mar 10 16:46:52 CST 2021
```



- Token过期检验

> 在有效期内Token可以正常读取，超过有效期则Token失效

```java
    @Test
    public void testExpToken(){
        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
                .setExpiration(new Date(exp));//设置超时
    }
```



- 自定义claims

> 除了使用官方api设置属性值，也可以添加自定义键值对。

```java
    @Test
    public void testCustomClaims(){
        long now = System.currentTimeMillis();  //当前时间
        long exp = now + 1000 * 60; //过期时间为1分钟
        JwtBuilder builder = Jwts.builder().setId("1001")
                .setSubject("小明")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "qfjava")
                .setExpiration(new Date(exp))
                .claim("role", "admin");//设置自定义键值对
    }
```

> 使用下面语句获取属性值：

```java
claims.get("role")
```



# 十一、微服务项目-使用Security+JWT实现权限管理

## 1.前后端分离的权限管理

![Spring Security + jwt 前后端分离的权限系统的时序图](pic/Spring Security + jwt 前后端分离的权限系统的时序图.png)

第一次是登录成功返回token和权限信息

第二次是携带token访问接口，从redis获取权限信息，校验url是否有权限，有则返回数据

## 2.引入依赖

```xml
        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!--spring security-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--druid连接-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>

        <!--jjwt-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>RELEASE</version>
        </dependency>

        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

```

## 3.登陆过滤器的实现

```java
package com.qf.my.security.admin.demo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.my.security.admin.demo.common.ResponseUtil;
import com.qf.my.security.admin.demo.common.ResultModel;
import com.qf.my.security.admin.demo.entity.SecurityUser;
import com.qf.my.security.admin.demo.entity.User;
import com.qf.my.security.admin.demo.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    private AuthenticationManager authenticationManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager,TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.authenticationManager = authenticationManager;
        //不是只允许post请求，经过这个filter
        this.setPostOnly(false);
        //设置登陆的路径和请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/user/login","POST"));
    }

    /**
     * 执行认证的方法
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //获取表单提供的数据
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);
            //校验==认证的过程
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()
                            , new ArrayList<>())
            );
            return authenticate;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("认证失败");
        }

    }

    /**
     * 认证成功以后调用的方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //得到用户名
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
        String username = securityUser.getUsername();
        //生成token
        String token = tokenManager.crtToken(username);
        //存入到redis  username: 权限
        redisTemplate.opsForValue().set(username,securityUser.getPermissionValueList());
        //返回token
        ResponseUtil.out(response, ResultModel.success(token));

    }

    /**
     * 认证失败调用的方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response,ResultModel.error(401,failed.getMessage()));
    }
}

```

## 4.权限过滤器的实现

```java
package com.qf.my.security.admin.demo.filter;

import com.mysql.cj.util.StringUtils;
import com.qf.my.security.admin.demo.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.CollectionUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager,
                           TokenManager tokenManager,RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 权限相关的操作
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获得token
        String token = request.getHeader("token");
        if(!StringUtils.isNullOrEmpty(token)){
            //使用jwt解析token获得username
            String username = tokenManager.getUsernameFromToken(token);
            //从redis中获得该用户名对应的权限
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            //将取出的权限存入到权限上下文中，表示当前token对应的用户具备哪些权限
            Collection<GrantedAuthority> authorityCollection = new ArrayList<>();
            if(!CollectionUtils.isEmpty(permissionValueList)){
                for (String permissionValue : permissionValueList) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
                    authorityCollection.add(authority);
                }
            }
            //生成权限信息对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,token,authorityCollection);
            //把权限信息对象存入到权限上下文中
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //放行
        chain.doFilter(request,response);
    }
}

```

## 5.注销处理器的实现

```java
package com.qf.my.security.admin.demo.security;

import com.mysql.cj.util.StringUtils;
import com.qf.my.security.admin.demo.common.ResponseUtil;
import com.qf.my.security.admin.demo.common.ResultModel;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 注销时具体要执行的业务
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //1.从请求头中获得前端携带的token
        String token = request.getHeader("token");
        if(!StringUtils.isNullOrEmpty(token)){
            //2.使用jwt解析token
            String username = tokenManager.getUsernameFromToken(token);
            //3.删除redis中的数据
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, ResultModel.success("注销成功"));
    }
}

```

## 6.用户名密码验证逻辑

```java
package com.qf.my.security.admin.demo.service.impl;

import com.qf.my.security.admin.demo.entity.SecurityUser;
import com.qf.my.security.admin.demo.entity.User;
import com.qf.my.security.admin.demo.service.PermissionService;
import com.qf.my.security.admin.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Thor
 * @公众号 Java架构栈
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名从数据库查询到该用户的信息
        User user = userService.selectByUsername(username);
        if(Objects.isNull(user)) {
            throw new UsernameNotFoundException("当前用户不存在");
        }
        //根据用户名从数据库查询到该用户的权限信息
        List<String> permissionValues = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(user);
        securityUser.setPermissionValueList(permissionValues);
        return securityUser;
    }
}

```

## 登录过滤器源码

```java
// 登录过滤器中认证流程
Authentication authenticate = authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()
                                            , new ArrayList<>())
);
```

```java
public class ProviderManager implements AuthenticationManager, MessageSourceAware,InitializingBean {
    ...
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ...
		for (AuthenticationProvider provider : getProviders()) {
			...
            result = provider.authenticate(authentication);
            ...
		}
        ...
	}
}
```

```java
// 上述用户名密码校验、角色权限校验走的都是 AbstractUserDetailsAuthenticationProvider， 可以自定义其他 Provider 实现
public abstract class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ...
        UserDetails user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);    
        ...
        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        ...
		return createSuccessAuthentication(principalToReturn, authentication, user);
	}
    
    protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails user) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
				principal, authentication.getCredentials(),
				authoritiesMapper.mapAuthorities(user.getAuthorities()));
		result.setDetails(authentication.getDetails());
		return result;
	}
}
```

```java
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    ...
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        ...
		UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
		return loadedUser;
	}
    
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
        ...
		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
	}
}
```

