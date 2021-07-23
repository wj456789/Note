# Swagger

## Swagger2

### 依赖

```java
//Springfox3.0默认用swagger v3来返回信息,使用3.0需要在配置文件添加springfox.documentation.swagger.use-model-v3=false

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.7.0</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 配置类

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.XXX.web.controller"))//扫描包范围
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * API 说明，包含作者、简介、版本、host、服务URL
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("XXXAPI文档")
                .description("XXXAPI文档")
                //.contact(new Contact("API文档", "http://www.XXX.com/", "xxx@qq.com"))//作者信息
                //.version("1.0")//定义api 版本号
                .build();
    }
}

```

```java
//如果有登录验证等拦截器，如下资源需要放行
registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
```



### 访问地址

```java
ip:port/swagger-ui.html
```

### swagger-bootstrap-ui

#### 依赖

```java
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.7.0</version>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>
```

#### 配置类

```java
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
    ...
}
```

```java
registry.addResourceHandler("doc.html")
        .addResourceLocations("classpath:/META-INF/resources/");
registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
```

#### 访问地址

```java
ip:port/doc.html
```

## Swagger3

### 依赖

```java
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

### 配置类

```java
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    Boolean swaggerEnabled=true;
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.example.play"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/");
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot-Swagger3集成和使用-demo示例")
                .description("springboot | swagger")
                // 作者信息
                .contact(new Contact("name", "个人主页url", "email"))
                .version("1.0.0")
                .build();
    }
}
```

```java
//可选
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }
}
```



### 访问地址

```java
ip:port/swagger-ui/index.html
```

### swagger-bootstrap-ui

#### 依赖

```java
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>swagger-bootstrap-ui</artifactId>
    <version>1.9.6</version>
</dependency>
```

#### 访问地址

```java
ip:port/doc.html
```

## 配置访问密码

```java
swagger.production=false
swagger.basic.enable=true
swagger.basic.username=admin
swagger.basic.password=123456
```

## 常用注解

| 注解               | 方法作用                                                     | 属性                                                         |
| :----------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| @Api               | 用在请求的类上，表示对类的说明                               | tags="说明该类的作用，可以在UI界面上看到的注解"；value="该参数没什么意义，在UI界面上也看到，所以不需要配置"； |
| @ApiOperation      | 用在请求的方法上，说明方法的用途、作用                       | value="说明方法的用途、作用"；notes="方法的备注说明"         |
| @ApiImplicitParams | 用在请求的方法上，表示一组参数说明                           | name：参数名；<br>value：参数的汉字说明、解释；<br/>required：参数是否必须传；<br/>paramType：参数放在哪个地方；取值：{header -->@RequestHeader；query --> @RequestParam；path（用于restful接口）--> @PathVariable；body-->@RequestBody；form（不常用）；}<br/>dataType：参数类型，默认String，其它值dataType="Integer";<br/>defaultValue：参数的默认值； |
| @ApiImplicitParam  | 用在@ApiImplicitParams注解中，指定一个请求参数的各个方面     |                                                              |
| @ApiResponses      | 用在请求的方法上，表示一组响应                               |                                                              |
| @ApiResponse       | 用在@ApiResponses中，一般用于表达一个错误的响应信息          | code：数字，例如400；message：信息，例如"请求参数没填好"；response：抛出异常的类； |
| @ApiModel          | 用于响应类上，表示返回响应数据的信息                         |                                                              |
| @ApiModelProperty  | 用在属性上，描述响应类的属性                                 |                                                              |
| @ApiParam          | 和@RequestParam一起使用，相当于@ApiImplictParams+@ApiImplictParam，也可以代替@ApiModel+@ApiModelProperty |                                                              |

###  注解使用

```java
@RestController
// - 用于实体类上，value=实体类名称，description用于描述实体类 用在模型类上，对模型类做注释；
// - 用于描述控制器Controller，即xx管理
@Api(tags = "测试")
public class TestController extends BaseController {
    @ApiOperation(value="说明方法的用途、作用",notes = "测试接口描述方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "参数的汉字说明、解释",required=true,defaultValue="1", dataType = "string",paramType = "header"),
            @ApiImplicitParam(name = "id", value = "参数的汉字说明、解释",required=true,defaultValue="1", dataType = "string",paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code=400,message="请求参数没填好"),
            @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
    @GetMapping("/test")
    public String test(String id,String name)  {
      return name;
    }

    @GetMapping("/test2")
    public String test2(
            @RequestParam @ApiParam(required = false, name = "name", value = "姓名") String name,
            @RequestParam @ApiParam(required = true, name = "subjectId", value = "姓名") String subjectId
    ) {
        return name+":"+subjectId;
    }
    
    @GetMapping("/test3/{name}")
    public String test3(
            @PathVariable @ApiParam(required = false, name = "name", value = "姓名") String name
    ) {
        return name;
    }
}
```

### 将返回对象用swagger注释

将返回对象进行泛型声明，声明后swagger会反射生成对象字段描述

```java
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class ResultView<T> {
    @ApiModelProperty(value = "返回描述",,required = true)
    private String msg;
    @ApiModelProperty(value = "返回的状态码: 200是操作成功；其他值是错误", required = true)
    private int code;
    @ApiModelProperty(value = "返回的结果集")
    private T data;

    public ResultView(CodeEnum code) {
        this.code = code.getValue();
        this.msg = code.getName();
    }

    public ResultView(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultView(CodeEnum code, String msg) {
        this.code = code.getValue();
        this.msg = msg;
    }

    public ResultView(CodeEnum code, T data) {
        this.code = code.getValue();
        this.msg = code.getName();
        this.data = data;
    }

    public ResultView(CodeEnum code, String msg, T data) {
        this.code = code.getValue();
        this.msg = msg;
        this.data = data;
    }

    public void setCode(CodeEnum code) {
        this.code = code.getValue();
    }
}
```

```java
public enum CodeEnum {
    SUCCESS("操作成功", 200),
    SUCCESS_204("成功请求，没有获取到内容", 204),
    SUCCESS_304("成功请求，不符合业务逻辑，操作失败", 304),
    ERROR_400("请求校验失败", 400),
    ERROR_401("没有权限", 401),
    ERROR_403("权限校验失败", 403),
    ERROR_404("资源不存在", 404),
    ERROR_405("访问方式不合法", 405),
    ERROR_406("参数校验不合法", 406),
    ERROR_500("系统内部错误", 500);

    private String name;
    private int value;

    private CodeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static String getName(int value) {
        CodeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CodeEnum c = var1[var3];
            if (c.getValue() == value) {
                return c.name;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
```

#### 使用

```java
@ApiModel
public class User{
    @ApiModelProperites("账户")
    private String name;
    @ApiModelProperites("密码")
    private String password;
}

@ApiOperation
//泛型为List<User>
public ResultView<List<User>> getUsers() throws Exception{
    List<Other> list=new ArrayList<>();
    //实际返回值可以是任何类型，与上述泛型声明List<User>无关
	ResultView result=new ResultView(CodeEnum.SUCCESS,list);
    return result;
}
```

```java
//泛型为List<User>时swagger返回值模型如下，泛型声明可以改为User，Map<String,User>等,同时返回值模型也会相应变化
{
  "code": 0,
  "data": [
      {
        "name": "string",
        "password": "string"
      }
  ],
  "msg": "string"
}




ResultView<List<User>>{
code*	integer($int32)
		返回的状态码: 200是操作成功；其他值是错误

data	[
            返回的结果集
            User{
                name		String
                    		账户
                    
                password    String
                    		密码
            }
        ]

msg*	string
		返回描述

}
```









