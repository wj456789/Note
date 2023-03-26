# Note

## 日志

[slf4j log4j log4j2的jar包配合使用](https://blog.csdn.net/catoop/article/details/121858136)



## Java基础

### File

[Java –将文件转换为字符串](https://blog.csdn.net/cyan20115/article/details/106548417)



## 软件工程

[Java中的委托和继承](https://blog.csdn.net/Seriousplus/article/details/80462722)

委托有依赖、关联、聚合、组合，委派和继承都是为了代码复用，只是方式不同。委托可以被看作是对象级别的重用机制，而继承是类级别的重用机制。

依赖称为“use-a”关系 ，关联属于“has-a”关系，组合属于“a part of”关系，聚合属于“has-a”关系，聚合直接传入对象，组合是在内部初始化。

继承属于“is-a”关系



## Redis

```sh
$ ./redis-cli -h 172.18.0.3 -cipherdir /opt/redis/cipher -a sysmgrrdb@admin@hJ.uya88nr@I?f84 -p 16379
```





## 多线程

[ForkJoinPool](https://segmentfault.com/a/1190000039267451)





## Normalizer.normalize()

normalize方法对外部输入字符串做归一化/标准化处理，如：

```java
//将全角形式的字符转化为半角形式的字符，"＜root＞"-->"<root>"
Normalizer.normalize(str,Normalizer.Form.NFKC);
```

[关于Normalizer.normalize()方法的用途](https://blog.csdn.net/u010512607/article/details/79921353)




## 正则表达式

```java
System.out.println("as/asdas/as".replaceAll("(?<=/).*?(?=/)","*"));		// as/*/as
System.out.println("as/asdas/as".replaceAll("(?=/).*?(?<=/)","*"));		// as*asdas*as
```

## Java8

```
String nowTime= DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
```

[字符串和时间相互转换](https://blog.csdn.net/WLFIGHTER/article/details/113752001)

## 远程调试

[IDEA 进行远程 Debug ](https://www.cnblogs.com/javastack/p/15484248.html)

[idea远程Remote使用](https://blog.csdn.net/weixin_42582494/article/details/115312211)

## 流

[BIO、NIO、AIO](https://www.cnblogs.com/williamjie/p/11194561.html)



## Springboot


### Bug

**springboot升级版本2.6.7报错**

```java
1、Failed to instantiate [org.springframework.boot.autoconfigure.quartz.QuartzDataSourceScriptDatabaseInitializer]: Factory method 'quartzDataSourceScriptDatabaseInitializer' threw exception; nested exception is java.lang.IllegalStateException: Unable to detect database type


// 解决方式
Platform used for Quartz, Session, Integration, and Batch schema initialization cannot be configured 
// 配置文件添加
spring.quartz.jdbc.initialize-schema=never

2、循环依赖
// 解决方式:配置文件添加
spring.main.allow-circular-references=true
```

## Java基础

[Java Math 数学方法](https://www.cainiaojc.com/java/java-library-math.html)

## IDEA

**jar包存在但是报错找不到：**

1. 找到对应编译不了的类。右键 - Build Module ‘xxxx’，recompile ‘xxxxx’
2. 运行maven命令 mvn idea:idea 或 mvn -U idea:idea

生成idea项目工程所有文件: mvn idea:idea

清除idea项目工程文件:mvn idea:clean

常见问题:
如果项目环境有错误,怀疑是这些idea本地文件有错误可以先使用 mvn idea:clean清空所有本地文件,然后使用mvn idea:idea重新生成所有idea本地文件

[误删.idea目录和.iml文件](https://www.cnblogs.com/zhangxl1016/articles/14990497.html)







项目或者jar包标注library root，说明这个项目被其他项目引入，作为外部依赖库的存在 



## WebService

> WebService就是一种跨编程语言和跨操作系统平台的远程调用技术。

XML,SOAP和WSDL就是构成WebService平台的三大技术 。

- WebService采用Http协议来在客户端和服务端之间传输数据。 
- WebService通过HTTP协议发送请求和接收结果时，发送的请求内容和结果内容都采用XML格式封装，并增加了一些特定的HTTP消息头，以说明HTTP消息的内容格式，这些特定的HTTP消息头和XML内容格式就是SOAP协议规定的。
- WebService服务器端首先要通过一个WSDL文件来说明自己有什么服务可以对外调用。WSDL用于描述WebService及其方法、参数和返回值。 WSDL文件保存在Web服务器上，通过一个url地址就可以访问到它。 

### WSDL

WSDL(Web Services Description Language), web服务描述语言，说明webservice服务端接口、方法、参数和返回值，WSDL是随服务发布成功，自动生成，无需编写。

- Service：提供的服务
- Binding：服务的具体协议和数据格式规范，通过Service指向Binding
- portType: 服务端点，描述操作方法名称和参数，通过binding指向portType
- message: 定义一个操作（方法）的数据参数名称，通过portType指向message
- types: 定义 web service 使用的全部的具体数据参数，通过message指向types

```xml
<?xml version="1.0" encoding="UTF-8"?>
<wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:ns1="http://org.apache.axis2/xsd" xmlns:ns="http://service.prp.campaign.huawei.com" xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:ax21="http://domain.prp.campaign.huawei.com/xsd" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap/" targetNamespace="http://service.prp.campaign.huawei.com">
    <!-- 5 -->
    <wsdl:types>
        <xs:schema attributeFormDefault="qualified" elementFormDefault="qualified" targetNamespace="http://domain.prp.campaign.huawei.com/xsd">
            <!-- 5.2查找对应name -->
            <xs:complexType name="RequestHeader">
                <xs:sequence>
                    <xs:element minOccurs="0" name="accessChannel" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="beId" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="language" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="operator" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="password" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="transactionId" nillable="true" type="xs:string"/>
                </xs:sequence>
            </xs:complexType>
            <xs:complexType name="EventBody">
                <xs:sequence>
                    <xs:element maxOccurs="unbounded" minOccurs="0" name="eventAttrMap" nillable="true" type="ax21:AttrMap"/>
                    <xs:element minOccurs="0" name="eventCode" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="msisdn" nillable="true" type="xs:string"/>
					<xs:element minOccurs="0" name="subsId" nillable="true" type="xs:string"/>
                </xs:sequence>
            </xs:complexType>
            <xs:complexType name="AttrMap">
                <xs:sequence>
                    <xs:element minOccurs="0" name="key" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="value" nillable="true" type="xs:string"/>
                </xs:sequence>
            </xs:complexType>
            <xs:complexType name="RecommednationResult">
                <xs:sequence>
                    <xs:element maxOccurs="unbounded" minOccurs="0" name="offerList" nillable="true" type="ax21:Offer"/>
                    <xs:element minOccurs="0" name="resultCode" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="resultMessage" nillable="true" type="xs:string"/>
                </xs:sequence>
            </xs:complexType>
            <xs:complexType name="Offer">
                <xs:sequence>
                    <xs:element minOccurs="0" name="clickUrl" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="urlType" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="desc" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="displayMode" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="businessPositionCode" nillable="true" type="xs:string"/>
                    <xs:element maxOccurs="unbounded" minOccurs="0" name="offerAttrMap" nillable="true" type="ax21:AttrMap"/>
                    <xs:element minOccurs="0" name="offerId" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="offerName" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="offerType" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="priority" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="url" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="bpOfferContentType" nillable="true" type="xs:string"/>
                    <xs:element minOccurs="0" name="bpOfferContent" nillable="true" type="xs:string"/>
                </xs:sequence>
            </xs:complexType>
        </xs:schema>
        <!-- 5.1查看type:RequestHeader和EventBody、RecommednationResult -->
        <xs:schema xmlns:ax22="http://domain.prp.campaign.huawei.com/xsd" attributeFormDefault="qualified" elementFormDefault="qualified" targetNamespace="http://service.prp.campaign.huawei.com">
            <xs:import namespace="http://domain.prp.campaign.huawei.com/xsd"/>
            <xs:element name="getRecommendedOffer">
                <xs:complexType>
                    <xs:sequence>
                        <xs:element minOccurs="0" name="requestHeader" nillable="true" type="ax22:RequestHeader"/>
                        <xs:element minOccurs="0" name="eventBody" nillable="true" type="ax22:EventBody"/>
                    </xs:sequence>
                </xs:complexType>
            </xs:element>
            <xs:element name="getRecommendedOfferResponse">
                <xs:complexType>
                    <xs:sequence>
                        <xs:element minOccurs="0" name="return" nillable="true" type="ax22:RecommednationResult"/>
                    </xs:sequence>
                </xs:complexType>
            </xs:element>
        </xs:schema>
    </wsdl:types>
    <!-- 4.查看element，参数分别为：getRecommendedOffer和getRecommendedOfferResponse -->
    <wsdl:message name="getRecommendedOfferRequest">
        <wsdl:part name="parameters" element="ns:getRecommendedOffer"/>
    </wsdl:message>
    <wsdl:message name="getRecommendedOfferResponse">
        <wsdl:part name="parameters" element="ns:getRecommendedOfferResponse"/>
    </wsdl:message>
    <!-- 3.查看message分别为:getRecommendedOfferRequest和getRecommendedOfferResponse -->
    <wsdl:portType name="EventAccessServicePortType">
        <wsdl:operation name="getRecommendedOffer">
            <wsdl:input message="ns:getRecommendedOfferRequest" wsaw:Action="urn:getRecommendedOffer"/>
            <wsdl:output message="ns:getRecommendedOfferResponse" wsaw:Action="urn:getRecommendedOfferResponse"/>
        </wsdl:operation>
    </wsdl:portType>
    <!-- 2.查看type:EventAccessServicePortType -->
    <wsdl:binding name="EventAccessServiceSoap11Binding" type="ns:EventAccessServicePortType">
        <soap:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
        <wsdl:operation name="getRecommendedOffer">
            <soap:operation soapAction="urn:getRecommendedOffer" style="document"/>
            <wsdl:input>
                <soap:body use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap:body use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:binding name="EventAccessServiceSoap12Binding" type="ns:EventAccessServicePortType">
        <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" style="document"/>
        <wsdl:operation name="getRecommendedOffer">
            <soap12:operation soapAction="urn:getRecommendedOffer" style="document"/>
            <wsdl:input>
                <soap12:body use="literal"/>
            </wsdl:input>
            <wsdl:output>
                <soap12:body use="literal"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <wsdl:binding name="EventAccessServiceHttpBinding" type="ns:EventAccessServicePortType">
        <http:binding verb="POST"/>
        <wsdl:operation name="getRecommendedOffer">
            <http:operation location="EventAccessService/getRecommendedOffer"/>
            <wsdl:input>
                <mime:content type="text/xml" part="getRecommendedOffer"/>
            </wsdl:input>
            <wsdl:output>
                <mime:content type="text/xml" part="getRecommendedOffer"/>
            </wsdl:output>
        </wsdl:operation>
    </wsdl:binding>
    <!-- 1.查看binding指向 -->
    <wsdl:service name="EventAccessService">
        <wsdl:port name="EventAccessServiceHttpSoap11Endpoint" binding="ns:EventAccessServiceSoap11Binding">
            <soap:address location="http://localhost:8080/axis2/services/EventAccessService"/>
        </wsdl:port>
        <wsdl:port name="EventAccessServiceHttpSoap12Endpoint" binding="ns:EventAccessServiceSoap12Binding">
            <soap12:address location="http://localhost:8080/axis2/services/EventAccessService"/>
        </wsdl:port>
        <wsdl:port name="EventAccessServiceHttpEndpoint" binding="ns:EventAccessServiceHttpBinding">
            <http:address location="http://localhost:8080/axis2/services/EventAccessService"/>
        </wsdl:port>
    </wsdl:service>
</wsdl:definitions>
```

```java
public RecommendationOutput getRecommendedOffer(RecommendationInput in) {
    ...
}

public class RecommendationInput {
    private String msisdn;
    private String subsId;
    private String eventCode;
    private EventAttrMap[] eventAttrInfoList;
    private RequestHeader accessSessionRequest;
}

public class EventAttrMap {
    private String key;
    private String value;
}

public class RequestHeader {
    private String accessChannel;
    private String version;
    private String beId;
    private String operator;
    private String language;
    private String password;
    private String transactionId;
}

// 返回值
public class RecommendationOutput {
    private String resultCode;
    private String resultMessage;
    private List<Offer> offerList;
}

public class Offer {
    private String clickUrl;
    private String desc;
    private String displayMode;
    private List<EventAttrMap> offerAttrMap;
    private String offerId;
    private String offerName;
    private String offerType;
    private String priority;
    private String offerUrl;
    private String url;
    private String bpOfferContentType;
    private String bpOfferContent;
    private String urlType;
    private String businessPositionCode;
}
```







## 数据结构

二叉排序树，又称为二叉查找树。二叉排序树或者是一棵空树，或者是具有以下性质的二叉树：若其左子树不为空，则左子树上的所有节点的值均小于它的根结点的值；若其右子树不为空，则右子树上的所有节点的值均大于它的根结点的值；左右子树又分别是二叉排序树。 

平衡二叉树又称AVL树。它是具有以下性质的二叉排序树：它的左子树和右子树的高度之差(平衡因子)的绝对值不超过1且它的左子树和右子树都是一颗平衡二叉树。 

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

### gaussDB

```mysql
# 修改 where 查询条件字段值
UPDATE DAMSDB.DATA_ASSETS_INFO_CONFIG SET ENTITY_TYPE=13 WHERE DATA_CONFIG_ID IN 
(SELECT DISTINCT DATA_CONFIG_ID FROM DAMSDB.DATA_ASSETS_INFO_CONFIG WHERE ENTITY_TYPE=10);

# 批量插入
INSERT INTO DAMSDB.DATA_ASSETS_INFO_CONFIG 
(DATA_CONFIG_ID,ENTITY_COLUMN,ENTITY_COLUMN_NAME,ENTITY_COLUMN_NAME_EN,ENTITY_COLUMN_ORDER,IS_HIDE,IS_TEMPLATE,ENTITY_TYPE,
TENEMENT,IS_FIELD,TEMPLATE_ORDER,FIELD_LENGTH) 
VALUES 
('1000','DATAPRODUCT_CODE','数据产品编码','DATA_DATAPRODUCT_INFO.DATAPRODUCT_CODE',1000,0,0,10,'GX',1,1000,'100'),
('1001','DATAPRODUCT_NAME_EN','数据产品英文名称','DATA_DATAPRODUCT_INFO.DATAPRODUCT_NAME_EN',1001,0,0,10,'GX',1,1001,'200'),
('1002','DATAPRODUCT_NAME_CN','数据产品中文名称','DATA_DATAPRODUCT_INFO.DATAPRODUCT_NAME_CN',1002,0,0,10,'GX',1,1002,'200');

# 单个添加表字段
ALTER TABLE DATA_API_INFO ADD COLUMN SHARING_SYSTEM_INFORMATION VARCHAR(2000) COMMENT '服务API共享系统信息';

# 批量添加表字段(表中旧字段已有记录，新加字段非空，默认空格，空包括空字符串)
ALTER TABLE DATA_DATAPRODUCT_INFO ADD  (
	DATAPRODUCT_UPDATE_TIME VARCHAR(20) COMMENT '数据产品更新时间',
	DATAPRODUCT_BUSINESS_DEPARTMENT VARCHAR(100) NOT NULL DEFAULT ' ' COMMENT '数据产品局方业务部门',
	DATAPRODUCT_BUSINESS_PIC VARCHAR(100) NOT NULL DEFAULT ' ' COMMENT '数据产品局方业务责任人'
);

# 修改表字段注释
COMMENT ON COLUMN DAMSDB.DATA_DATAPRODUCT_INFO.DATAPRODUCT_RELEASE_TYPE IS '发布类型';

# 修改表字段名称
ALTER TABLE DAMSDB.DATA_DATAPRODUCT_INFO RENAME COLUMN DATAPRODUCT_RELEASE_TYPE TO DATAPRODUCT_RELEASE_MODE;

# 删除表字段
ALTER TABLE DAMSDB.DATA_DATAPRODUCT_INFO DROP COLUMN DATAPRODUCT_BUSINESS_DEPARTMENT;
```





#### Bug

普通用户使用exp 导出时加上parallel 或者filetype类型时遇到报错： 

```sql
> exp tables=test file='test2.dmp' filetype=bin;
> exp tables=test file='test1.dmp' parallel=8;

GS-01001, Permissions were insufficient
```

使用并行或者filetype不为text时，需要select 系统表的权限。 

【问题解决】

步骤 一.    查询数据库参数

```sql
SQL> show parameter ENABLE_ACCESS_DC;
NAME                 DATATYPE             VALUE                RUNTIME_VALUE   EFFECTIVE
-------------------- -------------------- -------------------- --------------- ----------
ENABLE_ACCESS_DC     GS_TYPE_BOOLEAN      TRUE                 TRUE            immediatel
```

如果该参数为FALSE，则修改该参数的值：

```sql
SQL> alter system set ENABLE_ACCESS_DC=true;
```

步骤 二.    给用户授予查询所有表的权限

```sql
SQL> grant select any table to testexp;
```

步骤 三.    再次执行导出。

【备注】

权限和参数回滚

步骤 一.    修改参数值

```sql
SQL> alter system set ENABLE_ACCESS_DC=false;
```

步骤 二.    回收用户权限

```sql
SQL> revoke select any table from testexp;
```



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



## IDEA

修改IDE配置文件

Help -- Edit Custom VM Options

监控IDEA进程占用CPU

Help -- Diagnostic Tools -- Activity Monitor

插件启动时间分析器 

Help -- Diagnostic Tools -- Analyze Plugin Startup Performance





[java 将下划线方式命名的字符串转换为驼峰式](https://blog.csdn.net/qq_34626094/article/details/122578870)

[java将驼峰式命名的字符串转换为下划线大写方式](https://blog.csdn.net/sdgames/article/details/106471352)

 

IntelliJ IDEA中可以在主菜单中选择Navigate | Call Hierarchy命令查看一个Java方法调用树 

IntelliJ IDEA中可以在主菜单中选择Analyze | Data flow from/to Here两个命令查看表达式、变量和方法参数的传递关系树。 

IntelliJ IDEA的"Find Usage"可以查看一个Java类、方法或变量的直接使用情况。 

类关系图 在包或类上右键点击Diagram或者用快捷键Ctrl+Alt+U。



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



