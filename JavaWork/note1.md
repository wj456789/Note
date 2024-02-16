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



## Java8

```
String nowTime= DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
```

[字符串和时间相互转换](https://blog.csdn.net/WLFIGHTER/article/details/113752001)

[FlatMap的使用](https://blog.csdn.net/Xumuyang_/article/details/120951979)

[Collectors: partitioningBy](https://blog.csdn.net/qq_31635851/article/details/116055676)

[list转map方法](https://blog.csdn.net/Alecor/article/details/124388252)

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





# Note

## SQL

### 日期类型

```sql
-- 日期类型默认输出格式
> SELECT sysdate, systimestamp [from dual];
SYSTIMESTAMP			SYSDATE
2023-05-09 20:18:12		2023-05-09 20:18:12

-- 使用格式控制符将一个字符串转化为日期类型：
> SELECT to_date('07-JAN-2018', 'DD-MON-YYYY') [FROM dual];
TO_DATE('07-JAN-2018', 'DD-MON-YYYY')
2018-01-07 00:00:00

-- 通过格式控制符的描述，可以用to_char函数指定时期类型的输出格式
> SELECT to_char(sysdate, 'MON-YY-DD') [FROM dual];
TO_CHAR(SYSDATE, 'MON-YY-DD')
MAY-23-09

> SELECT to_char(sysdate, 'MON-YY-DD HH:MI:SS AM') [FROM dual];
TO_CHAR(SYSDATE, 'MON-YY-DD HH:MI:SS AM')
MAY-23-09 08:22:27 PM
```

Gauss100 目前支持的日期/时间类型包括DATE、带时区和不带时区的时间戳以及时间间隔。

#### DATETIME/DATE

**语法**：

```
DATETIME
```

**功能**：存储不带时区的日期类型的数据。

保存年、月、日、时、分、秒。

**取值范围**：公元0001年01月01日 00:00:00至公元9999年12月31日 23:59:59。

**占用空间**：8字节。

**对应关键字**：

- DATE
- DATETIME

#### TIMESTAMP

**语法**：

```
TIMESTAMP[(n)]
```

**功能**：存储不带时区的时间戳类型的数据。

- 保存年、月、日、时、分、秒，微妙。
- n取值范围[0,6]，表示秒后面的精度。TIMESTAMP[(n)]也可以不带参数，即写为TIMESTAMP，这时默认为6。

**取值范围**：公元0001年01月01日 00:00:00.000000至公元9999年12月31日 23:59:59.999999。

**占用空间**：8字节。

**对应关键字**：TIMESTAMP

#### TIMESTAMP(n) WITH TIME ZONE

**语法**：

```
TIMESTAMP(n) WITH TIME ZONE
```

**功能**：存储带时区的时间戳类型的数据。

- 保存年、月、日、时、分、秒，微妙。
- n取值范围[0,6]，表示秒后面的精度。TIMESTAMP[(n)]也可以不带参数，即写为TIMESTAMP，这时默认为6。

**取值范围**：公元0001年01月01日 00:00:00.000000至公元9999年12月31日 23:59:59.999999。

**占用空间**：12字节。

**对应关键字**：TIMESTAMP(n) WITH TIME ZONE

#### TIMESTAMP(n) WITH LOCAL TIME ZONE

**语法**：

```
TIMESTAMP(n) WITHLOCAL TIME ZONE
```

**功能**：带时区的时间戳类型的数据。不存储时区，存储时转换为数据库时区的TIMESTAMP，用户查看时转换为当前会话的时区的TIMESTAMP。

**占用空间**：8字节。

**对应关键字**：TIMESTAMP(n) WITHLOCAL TIME ZONE

#### 日期类型的格式控制符

| 符号                                                | 说明                                                         | 转换是否可逆 | 示例                                                         |
| --------------------------------------------------- | ------------------------------------------------------------ | ------------ | ------------------------------------------------------------ |
| " "(空格)、"-"(减号)、"\"、"/" 、":"、","、"."、";" | 分隔符                                                       | 是           | -                                                            |
| "text"                                              | 文本类型                                                     | 是           | 文本类型，作为输出参数时，输出引号中包含的内容；作为输入参数时，跳过引号中的内容，忽略空格。`select to_char(sysdate, '"Hello world!"') from dual;` |
| AM、PM                                              | 上午和下午指示符                                             | 否           | `select to_char(systimestamp, 'HH12:MI:SS AM') from dual;`   |
| CC                                                  | 世纪                                                         | 否           | `select to_char(systimestamp, 'CC') from dual;`              |
| DAY                                                 | 星期天全称                                                   | 否           | `select to_char(systimestamp, 'DAY') from dual;`             |
| DY                                                  | 星期天简称                                                   | 否           | `select to_char(systimestamp, 'DY') from dual;`              |
| DDD                                                 | 一年中的第几天                                               | 否           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'DDD') from dual;` |
| DD                                                  | 当前月中的第几天                                             | 是           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'DD') from dual;` |
| D                                                   | 当前周中的第几天                                             | 否           | `select to_char(to_date('2018-01-07', 'YYYY-MM-DD'), 'D') from dual;` |
| FF3、FF6、FF（默认FF6）                             | 秒的小数部分                                                 | 是           | `select to_char(systimestamp, 'FF3') from dual;`             |
| HH12、HH24 、HH（默认HH12）                         | 12小时制/24小时制                                            | 是           | `select to_char(systimestamp, 'HH,HH12,HH24') from dual;`    |
| MI                                                  | 时间的分钟数(0 ~ 59)                                         | 是           | -                                                            |
| MM                                                  | 日期的月份(1 ~ 12)                                           | 是           | -                                                            |
| MONTH                                               | 日期中月份全称                                               | 是           | `select to_char(systimestamp, 'MONTH, MON') from dual;`      |
| MON                                                 | 日期中月份简称                                               | 是           | -                                                            |
| Q                                                   | 当前日期的季度(1 ~ 4)                                        | 否           | -                                                            |
| SSSSS                                               | 一天中已经逝去的秒数(0 ~ 86400 - 1)                          | 否           | -                                                            |
| SS                                                  | 时间中的秒数(0 ~ 59)                                         | 是           | -                                                            |
| WW                                                  | 当前日期为该年份的week数，即当年的第几周，第一周从当年第一天计算起，每周7天 | 否           | -                                                            |
| W                                                   | 当前日期为该月份的week数，即当月的第几周，第一周从当月第一天计算起，每周7天 | 否           | -                                                            |
| YYYY                                                | 四位年份                                                     | 是           | -                                                            |
| YYY                                                 | 三位年份，如2018年可以写作018                                | 否           | -                                                            |
| YY                                                  | 两位年份，如2018年可以写作18                                 | 否           | -                                                            |
| Y                                                   | 一位年份，如2018年可以写作8                                  | 否           | `select to_char(systimestamp, 'Y') from dual;`               |



| 日期类型                       | 默认输出格式                     |
| ------------------------------ | -------------------------------- |
| DATETIME                       | YYYY-MM-DD HH24:MI:SS            |
| TIMESTAMP                      | YYYY-MM-DD HH24:MI:SS.FF         |
| TIMESTAMP WITH TIME ZONE       | YYYY-MM-DD HH24:MI:SS.FF TZH:TZM |
| TIMESTAMP WITH LOCAL TIME ZONE | YYYY-MM-DD HH24:MI:SS.FF         |

## 多线程

获取线程安全的List我们可以通过Vector、Collections.synchronizedList()方法和CopyOnWriteArrayList三种方式

- 读多写少的情况下，推荐使用CopyOnWriteArrayList方式
- 读少写多的情况下，推荐使用Collections.synchronizedList()的方式

[三种线程安全的List](https://blog.csdn.net/weixin_45668482/article/details/117396603)

## 分区表

把一张几千级的表TEST1改为分区表一般有以下几种方法：

**方法一**   **利用原表重建分区表**

根据原表TEST1表结构定义来新建分区表TEST1_TMP；

把TEST1的数据insert进表TEST1_TMP； --也可以用**CTAS**的方法      

rename表名，检查数据一致性。

优点：简单

缺点：考虑数据一致性，建议闲时 或 停机操作

 

**方法二** **使用数据泵导出导入**

根据原表TEST1表结构定义来新建分区表TEST1_TMP；

expdp并行导出TEST1，impdp并行导入TEST1_TMP；

rename表名，检查数据一致性。

优点：简单；若表在上百G，效率也算蛮高

缺点：考虑数据一致性，建议闲时 或 停机操作

表越大，越建议用这种方式，停机做

 

**方法三** **使用分区交换**

优点：只是对数据字典中的分区和表定义进行修改，没有数据复制，效率最高

缺点：全部表的数据都在一个分区内

这个缺点太致命，所以效率最高的方法，反而不用

```sql
--1、创建一个测试表
CREATE TABLE p1 AS SELECT t.OBJECT_NAME,t.CREATED FROM User_Objects t;
CREATE INDEX idx_p1_created ON p1(created);
--2、创建分区表，结构要与原表一致
CREATE TABLE p2(object_name VARCHAR2(32),created DATE) PARTITION BY RANGE(created) 
(
       PARTITION p_202204 VALUES LESS THAN (DATE'2022-05-01'),
       PARTITION p_202205 VALUES LESS THAN (DATE'2022-06-01')
);
--3、创建局部分区索引
CREATE INDEX idx_p2_created ON p2(created) LOCAL;
--4、交换分区与表
ALTER TABLE p2 EXCHANGE PARTITION p_202204 WITH TABLE p1 INCLUDING INDEXES WITHOUT validation;
--5、查看数据
SELECT * FROM p1;
SELECT * FROM p2 PARTITION (p_202204);
--6、查看索引状态
SELECT * FROM User_Indexes t WHERE t.table_name='P2';
SELECT * FROM User_Ind_Partitions t WHERE t.index_name=UPPER('idx_p2_created');

	总结：采用交换分区的办法，其原理仅仅是修改ORACLE系统中表定义的数据字典。不需要额外的表空间，速度非常快速，原表上的索引也不需要重建。当然，此种方法是将原表所有原来的数据放入了一个分区之中，新增数据才会进入新的分区。这样对于历史数据的查询使用不到分区排除带来的效率提高，有一定的局限性。
```

**方法四** **使用在线重定义**

11g的在线重定义其实已经非常好用，也支持rows的方式来重定义，不必有主键。

优点：可以在线做，不用担心数据一致性

缺点：效率一般

这个优点太突出，建议几十G内的表都可以选用这种方式做

## Mysql

```
SHOW GLOBAL VARIABLES LIKE 'innodb_lock_wait_timeout';
SET GLOBAL innodb_lock_wait_timeout=500;
SHOW GLOBAL VARIABLES LIKE 'innodb_lock_wait_timeout';
```



## oracle

```
merge into 目标表 t
using (源表) ti
on (原表与目标表的关联条件) -- 一般用主键或者能确认唯一的组合条件
when matched then
update set t.xxxx = ti.xxx
when not matched then
insert
(xxxx, xxx, xx, xxx, xxxxxxxx, xxx) -- 目标表字段
values
(ti.xxx, ....) -- 源表字段


merge into会将源表的每一条记录和目标表按关联字段匹配，目标表被匹配到的记录会更新数据，匹配不到的记录话就会把源表这些数据插入目标表，匹配的前提是关联字段要是目标表与源表的主键或唯一匹配条件。


直接根据 on的条件去匹配，在数据库层次就实现了 新增or更新操作。不需要将大量的数据查出来进行操作，同时也不会造成锁表问题，最终实现了500w数据3分钟同步完成（因为这张表上百个字段比较大）
```



## 分布式锁

[分布式锁的实现方式](https://blog.csdn.net/fuzh19920202/article/details/127999788)

[从spring管理的datasource中获取connection](https://blog.csdn.net/qq_18671415/article/details/119112362)

## Java基础

[Arrays.toList() 和Collections.singletonList()的区别](https://blog.csdn.net/wz1159/article/details/86704752?ydreferer=aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS5oay8%3D?ydreferer=aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS5oay8%3D)

## Java Web

在web.xml中定义 contextConfigLocation参数，spring会使用这个参数加载所有逗号分割的xml，如果没有这个参数，spring默认加载WEB-INF/applicationContext.xml文件。

[spring如何使用多个xml配置文件 【转】](https://www.cnblogs.com/secret1998/archive/2010/05/24/1742555.html)

实现Servlet的Filter接口 + web.xml配置过滤器

实现Servlet的Filter接口 + @WebFilter注解 + 服务主类上增加@ServletComponentScan注解，basePackages需要包含过滤器类所在的包，过滤器的执行顺序按照过滤器的类名升序排序 

实现Servlet的Filter接口 + @Configuration实现WebMvcConfigurerAdapter接口配置类，过滤器的执行顺序由setOrder的参数决定，值越小，优先级越高，如果两个过滤器的order值相同，则执行顺序按过滤器类名排序



在Spring容器中，servlet过滤器的的创建早于spring bean的初始化，所以在过滤器中用@Autowired、@Inject等注解注入bean，以及用@Value注解注入环境参数，都只能取得null。我们必须手动在过滤器的init方法中进行bean的初始化：

![img](note2.assets/59dc31552657a.jpg) 

[@WebFilter两种使用方法和失效解决方案](https://blog.csdn.net/z69183787/article/details/127808802)

[Java Web之过滤器Filter（@WebFilter）](https://blog.csdn.net/weixin_44989630/article/details/121357652)

[Servlet3.0模块化支持](https://www.iteye.com/blog/elim-2017099)



## IDEA

**查看变量调用链**

选中变量，右键选择 Analyze ->  Analyze Data Flow to Here/Analyze Data Flow from Here 

## Hbase

### cache 和 batch

缓存是服务端缓存 result 个数

批量处理是处理每行列数，默认是全列，当数据比每行列数小，则分批次处理，比每行列数大还是一行。比如默认一行一个 result ，一行数据有23列，批量处理为10，需要分三个批次读取这行数据，会放在3个 result 中，第三个 result 只有3列。

每次 rpc 请求可以获取缓存中的所有 result ，每次调用 next 都会触发一次rpc请求

RowFilter用于过滤row key

| Operator         | Description |
| ---------------- | ----------- |
| LESS             | 小于        |
| LESS_OR_EQUAL    | 小于等于    |
| [EQUAL           | 等于        |
| NOT_EQUAL        | 不等于      |
| GREATER_OR_EQUAL | 大于等于    |
| GREATER          | 大于        |
| NO_OP            | 排除所有    |

[Hbase](https://www.cnblogs.com/dengrenning/articles/9520924.html)

[Hadoop、HDFS、Hive、Hbase之间的关系](https://www.cnblogs.com/liyuanhong/articles/14518037.html)

[HBase高级特性之过滤器](http://3ms.huawei.com/km/groups/388/blogs/details/10461377) 

[HBase - Filter - 过滤器的介绍以及使用](https://blog.csdn.net/qq_36864672/article/details/78624856)

[HBase的二级索引](https://www.cnblogs.com/tesla-turing/p/11515351.html)

[hbase的cache与batch的理解](https://blog.csdn.net/luxiangzhou/article/details/83615993)

[HBase基础知识(8):扫描操作之缓存与批量处理](http://pangjiuzala.github.io/2015/08/20/HBase%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86-8-%E6%89%AB%E6%8F%8F%E6%93%8D%E4%BD%9C%E4%B9%8B%E7%BC%93%E5%AD%98%E4%B8%8E%E6%89%B9%E9%87%8F%E5%A4%84%E7%90%86/)

[hbase 程序优化 参数调整方法](https://blog.51cto.com/u_15127532/4045809) 

[hbase零碎小记](https://blog.csdn.net/bryce123phy/article/details/52943434)

## CURL

查看所有curl命令： man curl或者curl -h 

请求头：H,A,e 

响应头：I,i,D 

cookie：b,c,j 

传输：F(POST),G(GET),T(PUT),X 

输出：o,O,w 

断点续传：r 

调试：v,--trace,--trace-ascii,--trace-time 

[CURL详解](https://www.cnblogs.com/lei-z/p/16543184.html#_label0)

```sh
# -v 显示更详细的信息，调试时使用；
# -k (SSL)设置此选项将允许使用无证书的不安全SSL进行连接和传输。
$ curl -vk https://7.220.28.99:9011/sso/isAlive.jsp

* About to connect() to 7.220.28.99 port 9011 (#0)
*   Trying 7.220.28.99...
* Connected to 7.220.28.99 (7.220.28.99) port 9011 (#0)
* Initializing NSS with certpath: sql:/etc/pki/nssdb
* skipping SSL peer certificate verification
* SSL connection using TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
* Server certificate:
*       subject: CN=DigitalServicePlatform,O=Huawei,C=CN
*       start date: Mar 07 06:14:52 2019 GMT
*       expire date: Mar 04 06:14:52 2029 GMT
*       common name: DigitalServicePlatform
*       issuer: CN=HUAWEI Software Product CA,O=Huawei,C=CN
> GET /sso/isAlive.jsp HTTP/1.1
> User-Agent: curl/7.29.0
> Host: 7.220.28.99:9011
> Accept: */*
>
< HTTP/1.1 200
< Date: Fri, 24 Mar 2023 06:25:09 GMT
< Content-Type: text/html;charset=ISO-8859-1
< Content-Length: 11
< Connection: keep-alive
< Keep-Alive: timeout=30
< Set-Cookie: JSESSIONID=9139F6861719ECEE9E4890DB2D9B715C94CE21414FDE19A0; Path=/sso; Secure; HttpOnly; SameSite=None
< Set-Cookie: route=44432b10ef109634d5027b1b70a2ac3a; Path=/sso
< Server: lb
< Expires: 0
< Referrer-Policy: origin
<
* Connection #0 to host 7.220.28.99 left intact
ssoserverok
```

## tomcat

springboot使用外置tomcat

```java
@SpringBootApplication(scanBasePackages = {"com.huawei.iop.*"})
@MapperScan({"com.huawei.iop.mapper"})
@ServletComponentScan
public class IopApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(IopApplication.class, args);
    }

    @Override // 为了打包springboot项目
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IopApplication.class);
    }
}
```

```xml
<packaging>war</packaging>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <!-- 去掉自身tomcat避免冲突 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <!--打包的时候可以不用包进去，别的设施会提供。事实上该依赖理论上可以参与编译，测试，运行等周期。
                相当于compile，但是打包阶段做了exclude操作-->
    <scope>provided</scope>
</dependency>
```

```xml
${project.basedir}理解：${basedir}项目的根目录(包含pom.xml文件的目录)
<build>
    <!-- 应与application.properties(或application.yml)中context-path保持一致 -->
    <finalName>unifiedManage</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <configuration>
                <failOnMissingWebXml>false</failOnMissingWebXml>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <mainClass>com.huawei.iop.IopApplication</mainClass>
                <includeSystemScope>true</includeSystemScope>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <source>8</source>
                <target>8</target>
            </configuration>
        </plugin>
    </plugins>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <excludes>
                <exclude>**/*.java</exclude>
            </excludes>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.*</include>
            </includes>
        </resource>
        <resource>
            <directory>src/lib</directory>
            <targetPath>${basedir}/target/unifiedManage/WEB-INF/lib</targetPath>
            <includes>
                <include>EncryptUtilsGCM.jar</include>
            </includes>
        </resource>
    </resources>
</build>





```



## maven

```xml
IDEA 在 maven 项目打 war 包时将外部第三方引入的 jar 包

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
    	<!-- 导入系统之外的 Jar 包 -->
        <includeSystemScope>true</includeSystemScope>
    </configuration>
</plugin>
```

~~~xml
maven-jar-plugin是Maven的一个插件，用于将项目打包成jar包。

常用的配置选项及含义如下：

- `<archive>`：指定打包时要包含的文件和目录。
- `<manifest>`：指定manifest文件的信息。
- `<excludes>`：指定不包含在打包文件中的文件和目录。
- `<includes>`：指定需要包含在打包文件中的文件和目录。
- `<classifier>`：为打包文件指定附加分类器。
- `<finalName>`：指定打包文件的最终名称。

其中，archive 元素指定了生成的 Jar 包需要包含哪些文件以及如何打包这些文件。它具体包含以下配置项：
- manifest：用于指定 Manifest 文件(MANIFEST.MF)的位置和内容。
- manifestEntries：用于指定 Manifest 文件中的条目。
- addMavenDescriptor：是否将 Maven 项目描述文件 pom.xml 添加到生成的 Jar 包中。
- index：是否创建一个包含索引信息的 JAR 文件。
- compress：是否压缩生成的 Jar 包。
- forced：是否强制覆盖已存在的 Jar 包。
    
举个例子：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.0.2</version>
            <configuration>
                <archive> 
                    <manifest>
                        <addClasspath>true</addClasspath>
                        <mainClass>com.example.MainClass</mainClass>
                    </manifest>
                    <manifestEntries>
                        <Built-By>${user.name}</Built-By>
                        <Build-Jdk>${java.version}</Build-Jdk>
                    </manifestEntries>
                </archive>
                <excludes>
                    <exclude>**/*.txt</exclude>
                    <exclude>**/*.properties</exclude>
                </excludes>
                <includes>
                    <include>**/*.java</include>
                </includes>
                <classifier>exec</classifier>
                <finalName>my-app</finalName>
            </configuration>
        </plugin>
    </plugins>
</build>
```

在上述例子中，配置了打包的manifest信息，排除了txt和properties文件，添加了java文件，增加了分类器为exec，最终生成的文件名为my-app。
~~~



## path

```java
// 获取jar包内filePath下的文件名称集合
public static List<String> getJarPathFile(String filePath) {
    URL url = StringUtil.class.getClassLoader().getResource("/");
    String jarPath = url.toString().substring(0, url.toString().indexOf("!/") + 2);
    URL jarURL = null;
    List<String> fileNameLists = new ArrayList<>();
    try {
        jarURL = new URL(jarPath);
        JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
        JarFile jarFile = jarCon.getJarFile();
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        while (jarEntrys.hasMoreElements()) {
            JarEntry entry = jarEntrys.nextElement();
            String name = entry.getName();
            if (name.contains(filePath)){
                name = name.substring(name.lastIndexOf("/") + 1);
                if (StringUtils.isNotBlank(name)){
                    fileNameLists.add(name);
                }
            }
        }
    } catch (MalformedURLException e) {
        LOGGER.error("read path fail:" + e.getMessage());
    } catch (IOException e) {
        LOGGER.error("read path fail:" + e.getMessage());
    }
    return fileNameLists;
}
```

## Linux

### sed

```
s：替换，替换指定字符
d：删除，删除指定的行
a：增加，在当前行的下一行增加一行指定内容
i：插入，在指定行上面插入一行指定内容
s：替换，将选定行替换成指定内容
y：字符转换，转换前后的字符长度必须相同
p：打印，如果同时指定行，表示打印指定行；如果不指定行，则表示打印所有内容；如果有非打印字符，则以ASCII码输出。通常与-n一起使用
=：打印行数
l：打印数据流中的文本和不可打印的ASCII字符
H:复制到剪切板
G:粘贴

sed命令格式
sed -e '具体操作' 文件名1 文件名2 ...
sed -n -e '具体操作' 文件名1 文件名2 ...
sed -f 脚本文件 文件名1 文件名2 ...
sed -i -e '具体操作' 文件名1 文件名2 ...

查看文件指定行
sed -n '1p' /etc/passwd
5、查看一到三行
sed -n '1,3p' /etc/passwd
6、查看奇数行（n的作用是跳过当前行的下一行）
sed -n 'p;n' num
7、查看偶数行
sed -n 'n;p' num
8、sed 的查找与替换（g标识符表示全局查找替换）
sed 's/要被取代的字串/新的字串/g'
9、匹配包含相关字符的行
sed -n '/cao/p' /etc/passwd
10、匹配以指定字符为首的行
sed -n '/^root/p' /etc/passwd
11、匹配以指定字符结尾的行
sed -n '/bash$/p' /etc/passwd
12、使用-r选项支持扩展正则表达式
sed -n -r '/ro+t/p' /etc/passwd
13、删除指定行
sed -n '1d;p' num
14、删除范围内的行
sed -n '1,3d;p' num
15、取反删除，表示删除匹配项以外的行
sed -n '1,3!d;p' num
16、在每行行首加#号
sed -n 's/^/#/;p' num
17、删除行首的#号
sed -n 's/#//;p' num
18、在第一行下方插入123
sed '1a 123' num
19、在第一行行前插入123
sed '1i 123' num
20、剪切
sed '1{H;d};$G' num
21、复制粘贴
sed '1H；$G' num

$ cat example.txt
123

456
789

输出第一行直到空行为止
$ sed -n '1,/^$/p' example.txt
123
(空格也会输出)

# 删除第一行直到空行，输出剩余
$ sed '1,/^$/d' example.txt
456
789
```

```
-C<显示行数> 或 --context=<显示行数>或-<显示行数> : 除了显示符合样式的那一行之外，并显示该行之前后的内容。
-A<显示行数> 或 --after-context=<显示行数> : 除了显示符合范本样式的那一列之外，并显示该行之后的内容。
cat example.txt | grep "strpattern" -C 10
cat example.txt | grep "strpattern" -A 10
```

### curl

在使用curl命令发送HTTP请求时，可以使用 `-H` 参数来添加请求头。请求头的格式是`HeaderName:HeaderValue`，其中HeaderName表示请求头的名称，HeaderValue表示请求头的值。例如，要添加一个名为`Authorization`，值为`Bearer abcdefg`的请求头，可以这样使用curl命令：

```
curl -H "Authorization: Bearer abcdefg" https://example.com/api
复制代码
```

其中`https://example.com/api`是要访问的API的URL。

**如果要添加多个请求头，可以使用多个 `-H` 参数**，例如：

```
curl -H "Authorization: Bearer abcdefg" -H "Content-Type: application/json" https://example.com/api
```

这样会添加两个请求头，一个是`Authorization: Bearer abcdefg`，另一个是`Content-Type: application/json`。

### vim

:set fileencoding查看文件当前展示的编码格式 

## JVM

### 类加载

[java同时引用不同版本同一个jar包](https://blog.csdn.net/white_grimreaper/article/details/120921270)

[Java破坏双亲委派实现自定义加载器加载不同版本类](https://blog.csdn.net/u011943534/article/details/89204709)

[Java 自定义 ClassLoader 实现隔离运行不同版本jar包的方式](https://blog.csdn.net/t894690230/article/details/73252331?spm=1001.2101.3001.6650.7&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-7-73252331-blog-120921270.235%5Ev27%5Epc_relevant_multi_platform_whitelistv3&utm_relevant_index=13)

[Java插件开发之类加载隔离](https://www.cnblogs.com/xmzpc/p/15187495.html)

[java的类加载器以及如何自定义类加载器](https://blog.csdn.net/blueheartstone/article/details/127784519)

<https://cloud.tencent.com/developer/article/1832222>

<https://cloud.tencent.com/developer/article/1915650>

<https://www.zhihu.com/question/466696410>

<https://cloud.tencent.com/developer/article/1890187>

<https://juejin.cn/post/6865572557329072141>

























































```
出现上传到Hadoop HDFS文件乱码的问题，通常是因为编码格式不一致导致的。在Java上传Hadoop HDFS文件时，需要讲本地文件和HDFS文件都设置为相同的编码格式。常见的编码格式有UTF-8和GBK两种。下面给出一个示例代码，在上传文件时将编码格式设置为UTF-8。

​```
Configuration conf = new Configuration();
conf.set("fs.defaultFS", "hdfs://localhost:9000");
FileSystem fs = FileSystem.get(conf);
InputStream in = new FileInputStream(new File("localFilePath"));
OutputStream out = fs.create(new Path("hdfsFilePath"));
IOUtils.copyBytes(in, out, 4096, true);
​``` 

如果上传的文件的编码格式为GBK，那么需要在代码中加上如下两行代码：

​```
conf.set("fs.defaultFS", "hdfs://localhost:9000");
conf.set("dfs.client.use.datanode.hostname", "true");
conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
conf.set("hadoop.job.ugi", "hadoop");
FileSystem fs = FileSystem.get(conf);
FSDataOutputStream outputStream = fs.create(new Path("hdfsFilePath"));
InputStreamReader isr = new InputStreamReader(new FileInputStream(new File("localFilePath")), "GBK");
BufferedReader br = new BufferedReader(isr);
String line = null;
while ((line = br.readLine()) != null) {
    outputStream.write(line.getBytes("GBK"));
    outputStream.write("
".getBytes());
    outputStream.flush();
}
outputStream.close();
​``` 

在以上代码中，需要注意以下几点：

1. 首先需要将文件的编码方式设置为GBK

2. 需要在代码中设置hadoop.job.ugi参数，设置为hadoop，表示Hadoop用户。

3. 将FileSystem设置为DistributedFileSystem的实现，添加如下代码：

​```
conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
​``` 

4. 如果是上传一个多行的文本文件，则需要对每一行的数据进行编码，并加上换行符。

希望这些内容对你有所帮助。
不再接收
```

```
Hadoop HDFS使用两种编码格式：UTF-8和UTF-16。UTF-8是一种变长编码，用于表示Unicode字符集中的所有字符。而UTF-16是一种固定长度的编码格式，也用于表示Unicode字符集中的字符。

在Hadoop HDFS中，文件名、目录名、路径、文件内容等都是以UTF-8编码格式存储的。另外，Hadoop HDFS在存储数据块时也使用UTF-8编码格式进行编码。但是，Hadoop HDFS在存储元数据时使用UTF-16编码格式进行编码，因为UTF-16编码格式在处理字符时更加高效。

需要注意的是，Hadoop HDFS使用的UTF-8编码格式和普通的UTF-8编码格式并不完全一样。因为Hadoop HDFS需要处理大量文件名和路径信息，而这些信息通常包含大量的无效字符和控制字符，因此Hadoop HDFS进行了一些扩展和修改，以兼容各种不同的文件系统和操作系统。
不再接收
```



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











