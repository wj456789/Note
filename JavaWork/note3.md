# Note

# Note

## 下载

```java
String path = uploadDocumentPath + System.getProperty("file.separator") + documentName;
File file = UsFileUtils.getFile(path);
if (!file.exists()) {
    throw new BusinessException("failed");
} else {
    if (!UsFileLiteUtils.isSecurityFileName(file.getCanonicalPath())) {
        throw new BusinessException("failed");
    }
    
    response.setContentType("application/force-download");
    // setFileDownloadHeader(request, resp, fileName, fileType);
    
    byte[] buffer = new byte[1024];
    try(FileInputStream fis = FileUtils.openInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);) {
        OutputStream os = response.getOutputStream();
        int i = 0;
        while ((i=bis.read(buffer)) != -1) {
            os.write(buffer, 0, i);
        }
    } catch (IOException e) {
        LOGGER.error("IOException error: ", e);
    }
}



private static final String MSIE = "msie";
private static final String GECKO = "like gecko";
private static final String MOZILLA = "Mozilla";
// 解决中文名称
public static void setFileDownloadHeader(
    HttpServletRequest request, HttpServletResponse response, String fileName, String fileType) {
    try {
        String encodedfileName = null;
        String agent = NormalizerUtil.normalizeForString(request.getHeader("user-agent").toLowerCase(Locale.US));
        if (agent.contains(MOZILLA)) {
            encodedfileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        } else {
            encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        }
        response.reset();
        // 设置响应的头文件，会自动识别文件内容
        response.setContentType("application/x-msdownload");
        // 设置Content-Disposition
        String contentDisposition=String.format(Locale.ENGLISH, "attachment; filename=\"%s\"", (encodedfileName + fileType).replaceAll("\t|\r|\n", ""));
        response.setHeader("Content-disposition", contentDisposition);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store");
    } catch (UnsupportedEncodingException e) {
        logger.error("Throw exception message here: " + e);
    }
}
```



## JS

### 动态添加

#### 案例一

```html
<div id="OuterDiv_C" style="position:absolute;top:9%;left:47%;width:15%;height:15%;">
    <div id="InterDiv_C">
        <p id="FontStyle1"><span id="sp0_C" style="float:left;margin-top:8px;margin-left:15px;line-height: 11px;color:#41beff;"></span></p>
        <img id="img1" src="images/iopImages/showMore.png" class="img" style="float:right;margin-top:12px;margin-right:8px;" />
    </div>
    <div id="ContentDiv_C" style="width:150px;margin-left: 0px;margin-top: 2px; display: none;">
    </div>
</div>
```

```javascript
$scope.initContentData = function ($Model, type) {
    $Fire({
        'service' : "queryIndexService/getContentInfo",
        'target' : '$Model.contentInfo'
    }, this).onafter(function(){
        $("#sp0_C").text($Model.contentInfo.defaultContent);
        if($Model.contentInfo.timeContents){
            for(var i=0;i<$Model.contentInfo.timeContents.length;i++){
                var timeContent = $Model.contentInfo.timeContents[i];
                if(timeContent == "累计12个月"){
                    $("#ContentDiv_C").append('<div id="Content2Div_C">\n' +
                                              '<img src="images/iopImages/top-partingLine.png" style="width:150px"/>\n' +
                                              '<p id="FontStyle1" style="margin-top:7px;margin-left:15px;color:#666666;font-size:16px;">\n' +
                                              '<span id="sp2_C">累计12个月</span>\n' +
                                              '</p>\n' +
                                              '</div>');
                }else {
                    $("#ContentDiv_C").append('<div id="ContentDiv'+timeContent+'">\n' +
                                              '<img src="images/iopImages/top-partingLine.png" style="width:150px"/>\n' +
                                              '<p id="FontStyle1" style="margin-top:8px;margin-left:15px;color:#666666;font-size:16px;"><span id="sp'+timeContent+'" >'+timeContent+'</span>\n' +
                                              '</p>\n' +
                                              '</div>');
                }
            }
            $('#ContentDiv_C').css("height",$Model.contentInfo.timeContents.length*36+10+'px');
            $('#ContentDiv_C').on('mouseover','div',function(){
                $(this).css("font-weight","bold");
            });
            $('#ContentDiv_C').on('mouseout','div',function(){
                $(this).css("font-weight","normal");
            });
            $('#ContentDiv_C').on('click','div',function(){
                $scope.queryByCondition($Model,$(this).children("p").children("span").text())
            });
            $scope.queryByCondition($Model,$Model.contentInfo.defaultContent)
            $('#ContentDiv_C').css('display','none');
        }
    });
}
```

#### 案例二

```html
<div id="bottombox">
    <div style="position:absolute;top:74%;right:15%;width:50%;height:2%;z-index:999" class="font14">
        <div id="click1" class="click1 clickable">
            <img id="clickCricle1" alt="" src="images/iopImages/circle_selected.png" class="circlePosition">
            <span id="clickspan1">title1</span>
            <uee:fire event="click" script="clickActCricle($Model,'click1')"></uee:fire>
        </div>
    </div>
</div>
```

```javascript
$scope.clickByCondition = function($Model,type,content){

    $('#sp0_C').text(content);

    $('#ContentDiv_C').slideToggle(300);
    $('#clickCricle1').remove();

    $("#clickspan1").before('<img id="clickCricle1" alt="" src="images/iopImages/circle_selected.png" class="circlePosition">');
}
```

参考：

[jQuery-为动态添加的元素绑定事件](https://blog.csdn.net/xiaozhi_2016/article/details/52184328)

[js为动态添加的元素增加事件（事件委托）](https://blog.csdn.net/badmoonc/article/details/78259219)

### img

`div`只包含一个`img`元素，但是页面展示上`div`却比`img`高出一点 

```html
<!-- html部分 -->
<div class="header-pic">
    <img class="" :src='headPic' />
    <div class="linear-bg"></div>
</div>
```

```javascript
//less代码
.header-pic {
    position: relative;
    border-radius: 10px 10px 0px 0px;
    width: 100%;

    img {
        width: 100%;
        background-size: cover;
    }
    .linear-bg {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        height: 100%;
        background: rgba(51,51,51,0.10);
    }
}
```

**问题原因**

> 后来查了一下资料，发现`img`元素后面会跟一个空白符，会使它的高度多出3px
> 至于为什么img元素后面会出现3px的空白呢～？因为`img`元素是行内元素，行内元素默认会有3px的间距。因此当我们的块级元素`div`包含这个行内元素的时候底部就出现了3px的间距。

**解决方案**

> 设置`img`为`display:block`,空白就消失，因为将img元素设置为块级元素，就不会存在默认间距了。

### JQuery

**获取当前标签下的子标签**

1、查找子元素方式1：>
例如：var aNods = $(“ul > a”);查找ul下的所有a标签

2、查找子元素方式2：children()

3、查找子元素方式3：find()

通过下标获取第n个子标签的ID值
1.var num1=$(“ul > a:eq(0)”).attr(“ID”);

2.var num2=$(“ul”).children(“a:eq(0)”).attr(“ID”);

3.var num3=$(“ul”).find(“a:eq(0)”).attr(“ID”);

## SSO

### 流程说明

#### 登录

合法用户首次进入系统：

用户	浏览器			业务子系统				   SSO Server

用户 -> 浏览器(TGC) -> SSO Client(Session)和业务系统 -> SSO Server

1. 用户向浏览器发送请求
2. 浏览器向SSO Client发送请求，SSO_CLIENT过滤出未认证的用户，也就是无法通过Session识别用户，则SSO Client重定向（携带SSO_Server的登录url和用户访问业务的URL）浏览器请求到SSO Server；
   - 其中Session存在如下几种情况：1）用户请求中无SessionID； 2）用户请求中有SessionID，但无SSO_Client的数据（Assertion）；
   - （**已经登录的用户再次进入业务系统**）-> 如果SSO_Client可以从session的得到Assertion数据，认为用户已经登录了，直接将业务请求提交到业务系统第九步。
3. SSO_Server发现该用户没有携带TGC，向用户响应登录页面；
   - （**已经登录的用户进入新的业务系统**）-> 如果浏览器发起的认证请求中携带了TGC，SSO_Server根据TGC关联到用户，根据TGC找到对应的TGT（ST和业务URL纪录在TGT中），判断TGT没有超期，认为用户合法，重定向浏览器到SSO Client并返回一个ST（serive ticket）给浏览器，直接到第六步。
4. 用户在浏览器上输入登录信息，提交到SSO Server。
5. SSO_Server完成认证，根据用户数据生成本次登录的TGT，向浏览器响应，返回该业务的ST和TGC（TGC的值就是TGT的ID），并重定向浏览器请求到SSO Client
6. 浏览器以**cookies的方式记录TGC**，携带ST重定向到SSO Client
7. SSO_Client发现用户请求中包含ticket信息，向SSO Server发起ST票据验证请求。 
8. SSO_Server验证票据是否为认证时为该业务分配的票据，并返回数据给客户端（将TGT中的保存的Assertion返回给client）。
   - 票据验证成功正确，返回用户数据
   - 票据非法，则本次业务认证失败
9. **SSO_Client将用户数据写入Session**中，供业务使用，并将请求提交到业务中，提交方式根据配置有两种：
   - 直接将本次请求提交给业务。
   - 通过rediect方式提交：该方式可以保证sessionid在进入业务之前写入浏览器



**SSO_Server实现单点登录的原理**

根据浏览器携带的TGC判断用户是否登录，如果登录直接放行。否则，呈现登录界面。

浏览器没有TGC，浏览器填写用户到Server认证生成TGC和ST返回给浏览器，有TGC直接返回ST给浏览器

之后始终浏览器获取ticket发送到client再到server认证

**SSO_Client实现单点登录的原理**

根据session判断用户是否登录，如果登录放行，否则，跳转到sso_server。

#### 登出

1. 用户点击业务界面的登出按钮
2. 浏览器向SSO_Server发出登录请求：浏览器会自动携带TGC信息，发起登出请求
3. SSO_Server处理登出请求
   1. 根据TGC找到对应的TGT，从TGT中得到改用户已经登录的所有业务的信息。
   2. 向业务的URL（实际上是SSO Client）发起请求，携带登出logout参数，该参数的中包含登录时分配的ST信息
4. SSO_Client根据消息中携带的logout标识，判断是一个登出请求，注销用户的session，并返回。
   1. SSO_Client判断请求中携带了“logoutRequest”（协议参数）。则认为是一个登出处理。
   2. 根据消息中携带的ST的值，查找出对应的session，将session注销掉。
5. SSO_Server向用户呈现登出界面，并清除TGC，具体的登出界面，在SSO_Server侧根据配置实现。



**TGC:**和一个具有一定有效期的身份证号码相似。

- 是SSO_Server和浏览器之间使用的一个cookies的值，该只在server侧可以唯一标识一个用户。在格式上是一个散列码，能有效防止伪造和攻击。其值的形式如下：TGT-1-w7uJd4E71MeiycjYZCS0KJgvyOwyNm6A0q05Fk1eUmbMjn0OMY-cas
- 根据cookies的协议，TGC只能在SSO_Server和浏览器之间使用。正规的浏览器不能将该TGC传递给sso_client
- TGC对应的数据(TGT)只在Server侧有效，sso_client无法解析TGC的含义

**TGT(Ticket Granting Ticket)**：再服务器侧管理的用户相关的数据，和公安局中管理的用户的户口及档案信息相似。

- TGT的ID为TGC:可以保证由TGC能快速的检索到对应的TGT
- TGT中保存有：
  - 需要返回给业务侧的用户数据：assertion
  - 用户已经等了的业务URL及ST
  - Cookies有效期的有关数据。
- TGT不会给浏览器呈现，可以保证数据安全
- TGT的有效期到期之后，系统会主动注销TGT，保证安全性
- TGT注销之前，会给sso_client发送登出请求



## http

```java
private String getRealIpAttr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (StringUtils.isNotBlank(ip) && ip.contains(",")) {
        ip = ip.split(",")[0];
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
    }
    return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
}
```

[httpServletRequest获取客户端真实ip](https://blog.csdn.net/u014410695/article/details/50162315)

## Gauss

```sql
> su - gtsgsdba -s /bin/bash
> cd app/bin
> zsql / as sysdba -q 
-- > zsql metadb/ABC_abc1@7.220.29.236:15432 -q 使用其他用户登录
> EXP USERS=METADB FILE="/opt/backup/gdbservice/data/metadb_back_20221024.dmp";
> EXP USERS=SERVICEDB FILETYPE=BIN FILE="/opt/backup/gdbservice/data/servicedb_back_20221024.dmp";
-- 其中，斜体部分可根据实际情况配置。
-- /bin/bash和app/bin以现场实际路径为准。
```

- FILETYPE=BIN时，会导出三类文件：元数据文件（用户指定的文件）、数据文件（.D文件）和LOB文件（.L文件）。 

  逻辑导出数据时，会在指定的导出文件路径下生成一个元数据文件和一个名为data的子目录，如果未指定导出文件路径，则默认在当前路径下生成一个元数据文件和一个名为data的子目录。FILETYPE=BIN时，生成的子文件（数据文件、LOB文件）会放在二级目录data下，如果指定的元数据文件和生成的子文件已经存在，则会报错。

- 导出当前用户的tab1和tab2中的数据。`EXP TABLES=tab1,tab2  FILE="file1.dmp";`

```sql
> IMP USERS=SERVICEDB FILETYPE=BIN FILE="/opt/backup/gdbservice/data/servicedb_back_20221024.dmp";
```

### other

#### 删除用户时，用户已登录，删除失败

```sql
SQL> DROP USER IF EXISTS BILLDB CASCADE;
GS-00807, The user BILLDB has logged in, can not be dropped now
```

解决方案： 删除已连接的session，再删除该用户。 

```sql
# 查看已连接的 SESSION
SQL> SELECT SID, SERIAL#, CLIENT_IP, PROGRAM FROM DV_SESSIONS WHERE USERNAME='BILLDB';
SID          SERIAL#      CLIENT_IP
------------ ------------ ----------------------------------------------------------------
280          13495        10.189.105.41
353          9141         10.189.105.41
538          6701         10.189.105.41
3 rows fetched.
 
# 删除 SESSION
SQL> ALTER SYSTEM KILL SESSION '280,13495';
Succeed.
 
SQL> ALTER SYSTEM KILL SESSION '353,9141';
Succeed.
 
SQL> ALTER SYSTEM KILL SESSION '538,6701';
Succeed.
 
# 再次查看已连接的 SESSION
SQL> SELECT SID, SERIAL#, CLIENT_IP FROM DV_SESSIONS WHERE USERNAME='USER_TEST';
SID          SERIAL#      CLIENT_IP
------------ ------------ ----------------------------------------------------------------
0 rows fetched.
 
# 删除用户
SQL> DROP USER IF EXISTS BILLDB CASCADE;
Succeed.
```



## Maven

[Maven把项目依赖的所有jar包都打到同一个jar中](https://blog.51cto.com/u_12796481/2796873) 

[maven打jar包并引入依赖包(使用assembly和dependency插件)](https://blog.csdn.net/Luck_ZZ/article/details/108648753)

[maven中把依赖的JAR包一起打包](https://blog.csdn.net/xiaokanfuchen86/article/details/113919498)

```sh
mvn -T 1C -Dmaven.test.skip=true clean package
# -T 1C 指定多线程编译，表示每个CPU核心跑一个工程；
# -Dmaven.test.skip=true 不编译测试用例，也不执行测试用例;
```

```sh
# 多module项目升级版本号，统一修改pom的版本号，及子模块依赖的版本号，用的是versions-maven-plugin
mvn versions:set -DnewVersion=xxx
mvn versions:commit

# 回退
mvn versions:revert
```

```sh
# maven 下载 工程依赖的所有jar包到本地
$ mvn dependency:copy-dependencies
```



## SpringBoot

### 国际化MessageSource

#### MessageSource接口

Spring中定义了一个MessageSource接口，以用于支持信息的国际化和包含参数的信息的替换。MessageSource接口的定义如下，对应的方法说明已经在方法上注释了。

```java
public interface MessageSource {

    /**
	 * 解析code对应的信息进行返回，如果对应的code不能被解析则返回默认信息defaultMessage。
	 * @param 需要进行解析的code，对应资源文件中的一个属性名
	 * @param 需要用来替换code对应的信息中包含参数的内容，如：{0},{1,date},{2,time}
	 * @param defaultMessage 当对应code对应的信息不存在时需要返回的默认值
	 * @param locale 对应的Locale
	 * @return
	 */
    String getMessage(String code, Object[] args, String defaultMessage, Locale locale);

    /**
	 * 解析code对应的信息进行返回，如果对应的code不能被解析则抛出异常NoSuchMessageException
	 * @param code 需要进行解析的code，对应资源文件中的一个属性名
	 * @param args 需要用来替换code对应的信息中包含参数的内容，如：{0},{1,date},{2,time}
	 * @param locale 对应的Locale
	 * @return 
	 * @throws NoSuchMessageException 如果对应的code不能被解析则抛出该异常
	 */
    String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException;

    /**
	 * 通过传递的MessageSourceResolvable对应来解析对应的信息
	 * @param resolvable 
	 * @param locale 对应的Locale
	 * @return 
	 * @throws NoSuchMessageException 如不能解析则抛出该异常
	 */
    String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;

}
```

#### SpringBoot+MessageSource

springboot默认支持国际化，需要在类路径下创建国际化配置文件，注意名称必须以messages开始。 比如在resoure下新建i18n目录，在此目录下新建message.properties，messages.properties （默认的语言配置文件，当找不到其他语言的配置的时候，使用该文件进行展示）。yml文件中指定了messageSource的basename为message，即根资源文件应为类路径下的messages.properties，其它的都是需要带Locale后缀的，如中国大陆是messages_zh_CN.properties。 

```yaml
# application.yml
spring:
  messages:
    basename: classpath:i18n/messages
    encoding: UTF-8
    cache-duration: 3600ms
```

```properties
# messages.properties
LineAgeShow_100007=传入参数为空
LineAgeShow_100008=模型血缘关系异常
LineAgeShow_100009=模型血缘关系id为空

# messages_en_US.properties
LineAgeShow_100007=The incoming parameter is empty
LineAgeShow_100008=Model LineAge relationship exception 
LineAgeShow_100009=Model lineAge id is empty

# messages_zh_CN.properties
LineAgeShow_100007=传入参数为空
LineAgeShow_100008=模型血缘关系异常
LineAgeShow_100009=模型血缘关系id为空
```

```java
import org.springframework.context.MessageSource;
/**
 * 国际化语言获取工具类
 *
 */
@Component
public class LocaleMessageSourceUtil {
    @Resource
    private MessageSource messageSource;

    private Boolean isEN(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            Object obj = session.getAttribute(AppConstant.LANGUAGESETTING);
            if (obj instanceof String) {
                String lang = NormalizerUtil.normalizeForString((String) obj);
                return lang.toLowerCase(Locale.ENGLISH).contains("en");
            }
        }
        return false;
    }

    public Boolean isEN() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return isEN(request);
        }
        return false;
    }

    public String getMessage(String code, Locale locale) {
        /**
         * code：信息的键，properties中的key
         * args：系统运行时参数，可为空
         * defaultMessage：默认信息，可为空
         * locale：区域信息，我们可以通过java.util.Locale类下面的静态常量找到对应的值。如：简体中文就是zh_CN；英文就是en_US，详见java.util.Locale中的常量值，Locale.US是en_US或Locale.SIMPLIFIED_CHINESE是zh_CN
         */
        return messageSource.getMessage(code, null, null, locale);
    }

    public String getMessage(String code, HttpServletRequest request) {
        return messageSource.getMessage(code, null, null, Locale.SIMPLIFIED_CHINESE);
    }

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.SIMPLIFIED_CHINESE);
    }
}
```

```java
@Autowired
private LocaleMessageSourceUtil messageSourceUtil;

messageSourceUtil.getMessage("LineAgeShow_100007", Global.getHttpServletRequest())
```

```java
public class Global {
    public static HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = null;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return request;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }
}
```

[国际化MessageSource](https://elim168.github.io/spring/bean/21.%E5%9B%BD%E9%99%85%E5%8C%96MessageSource.html)

## IDEA

修改IDE配置文件

Help -- Edit Custom VM Options

监控IDEA进程占用CPU

Help -- Diagnostic Tools -- Activity Monitor

插件启动时间分析器 

Help -- Diagnostic Tools -- Analyze Plugin Startup Performance

## Gson

使用Gson解析json成对象时默认的是将json里对应字段的值解析到java对象里对应字段的属性里面。 可以使用@SerializedName注解来将对象里的属性跟json里字段对应值匹配起来。 

```java
json数据如下：
{
    "id":"1"
    "n":"kyoya"
    "p":"123456"
    "s":"0"
}

public class User{
    private String id;
 
    @SerializedName("n")
    private String userName;
 
    @SerializedName("p")
    private String password;
 
    @SerializedName("s")
    private String sex;
}
// 使用Gson解析json字符串或使用Gson生成json字符串都可以相互匹配
```



## sql

### sequence

在Oracle中sequence就是序号，每次提取完都会自动增加，步幅固定，它与表没有直接关系 

序列(SEQUENCE)其实是序列号生成器，可以为表中的行自动生成序列号，产生一组等间隔的数值(类型为数字)。其主要的用途是生成表的主键值，可以在插入语句中引用，也可以通过查询检查当前值，或使序列增至下一个值。

创建序列需要CREATE SEQUENCE系统权限。

```sql
-- 创建sequence语句：
CREATE SEQUENCE seq_name-- seq_name为计数器的名字，自定；
INCREMENT BY 1 -- 每次加幅度:1,2,3,....；
START WITH 1 -- 起始序号，以实际生产情况而定；
MINVALUE 0 -- 最小值
NOMAXvalue -- 不设置最大值，或设定最大值： maxvalue 9999;
NOCYCLE -- 一直累加，不循环; 或循环使用 cycle ;
CACHE 10; --设置缓存序列个数，如果系统down掉了或者其它情况将会导致序列不连续，也可以设置为--NOCACHE



-- 修改sequence：
-- Alter sequence 可以修改sequence（除起始值）步幅、最大/最小值、是否循环、缓存个数 这些参数；
例：
Alter Sequence seq_name  
Increment  By  2 
Maxvalue 9999
Cycle
Cache 5;
-- 需要修改sequence的起始值，则需要删除原有sequence，重新创建；



-- 应用sequence：
-- sequence创建完成后，就可以使用sequence的两个参数 currval、nextval；
-- currval查询sequence的当前值：
select seq_name.currval from dual;
-- nextval增加sequence的值，然后返回sequence值：
select seq_name.nextval from dual;
-- 例：对某一张表使用：
insert into tb_name(id,name) values(seq_name.nextval,'下一个计数');



-- 删除sequence ：
drop sequence seq_name;
```

**注：**

- currval是取当前值，所以一个新的计数器sequence必须先使用nextval后才可以使用currval否则会报错;
- nextval是取下一个值，但第一次使用时取的是初始值，之后正常取下一个，且如果一个语句(不同的子句)里面有多个nextval，它们的取值可能是不同的；
- 如果指定CACHE值，ORACLE就可以预先在内存里面放置一些sequence，cache里面的取完后，oracle自动再取一组到cache。 
  - 优点：存取的快些，尤其是并发访问时。
  - 缺点：使用cache或许会跳号， 比如数据库突然不正常down掉（shutdown abort),cache中的sequence就会丢失. 所以可以在创建的时候用nocache防止这种情况。

**什么时候使用sequence?**

- 不包含子查询、snapshot/view的select的语句
- insert语句的子查询中
- insert语句的values中
- update的set中

如：update 表名 列值=序列名.nextval where 条件;

 

**在sqlserver和mysql中都可以在定义表的时候，直接给指定自增长。**

sqlserver中设置自增长

create table 表名(id int primary key identity(1,1),name varchar(32));

mysql中设置自增长

create table 表名(id int primary key auto_incrment,name varchar(32));

[数据库Sequence创建与使用](https://www.cnblogs.com/klb561/p/11333643.html)

## Python

[脚本样例](https://blog.csdn.net/m0_48978908/article/details/118881731)

### with open

[python文件读取with open替代try finally](https://www.jianshu.com/p/6f479f14eeac)

### zip

[`zipfile`](https://docs.python.org/zh-cn/3/library/zipfile.html#module-zipfile) --- 使用ZIP存档

[使用python自带的zipfile模块做压缩文件夹/解压缩zip文件功能](https://gist.github.com/hfeeki/ce7b6920b7d01012576b) 

### except

[Python中获取异常（Exception）信息](https://www.cnblogs.com/klchang/p/4635040.html)

### 文件

[python逐行读取文件内容的三种方法](https://blog.csdn.net/zhengxiangwen/article/details/55148287)

[python 实现将txt文件多行合并为一行并将中间的空格去掉方法](https://www.jb51.net/article/153111.htm)

[Python判断文件是否存在的三种方法](https://www.cnblogs.com/jhao/p/7243043.html)

[使用python删除一个文件或文件夹](https://www.cnblogs.com/aaronthon/p/9509538.html)

### 循环

[python跳出多层循环的几种方法](https://www.cnblogs.com/sjx1996/articles/10219812.html)

### 时间戳

[python - 获取时间戳（10位和13位）](https://blog.csdn.net/xuezhangjun0121/article/details/78083717)

### 字符串

[循环拼接字符串](https://blog.csdn.net/Rookie_Max/article/details/104043740)

[Python字符串拼接的十种方式](https://cloud.tencent.com/developer/article/1750006)

