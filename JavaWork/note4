# Note



```java
public interface TemplateRegisterService {
    void registerTemplate(ConfigRegisterDomain config);
}


public class TemplateRegisterServiceImpl implements TemplateRegisterService {
    @Override
    public void registerTemplate(ConfigRegisterDomain config) {
        ...
    }

    void handleRegisterResult(String batchNo, String templatePath) {
        ...
    }
}
```

```java
public interface DataProductTemplateService extends TemplateRegisterService {
    
}

public class DataProductTemplateServiceImpl extends TemplateRegisterServiceImpl implements DataProductTemplateService {
    @Override
    public void registerTemplate(ConfigRegisterDomain config) {
		...
        super.handleRegisterResult(config.getBatchNo(), config.getTemplatePath());
    }
}
```

```java
public interface DataModelTemplateService extends TemplateRegisterService {
    
}

public class DataModelTemplateServiceImpl extends TemplateRegisterServiceImpl implements DataModelTemplateService {
    @Override
    public void registerTemplate(ConfigRegisterDomain config) {
		...
         super.handleRegisterResult(batchNo, config.getTemplatePath());
    }
}
```

```java
TemplateRegisterService templateRegisterService = getRegisterService(assetType);
templateRegisterService.registerTemplate(config);

private TemplateRegisterService getRegisterService(String assetType) {
    switch (assetType) {
        case AppConstant.MODEL_ASSETTYPE:
            return SpringUtils.getBean(DataModelTemplateService.class);
        case AppConstant.REPORT_ASSETTYPE:
            return SpringUtils.getBean(ReportTemplateService.class);
        case AppConstant.API_ASSETTYPE:
            return SpringUtils.getBean(APITemplateService.class);
        case AppConstant.DATAPRODUCT_ASSETTYPE:
            return SpringUtils.getBean(DataProductTemplateService.class);
        default:
            return null;
    }
}
```

```java
@Component
public class SpringUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     *
     * @param name name
     * @return Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz clazz
     * @param <T>   clazz
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name  name
     * @param clazz clazz
     * @param <T>   T
     * @return T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
```

```





本系统作为面向运营商市场的NFV-I电信云平台产品，主要面向国内的三大运营商的电信设备云化场景提供云基础设施的解决方案。产品可以独立软件产品形态上市，也可以被集成到云化电信设备系统中作为系统的基座。支撑的产品线包括华为公司的云核，网络，无线等。

华为Fi的能力服务化到亚信Paas

背景:移动集团1+N 场景需要把华为fi服务化到亚信pass平台。
需求:把Fi8.0组件Hadoop(包含HDFS、YARN、Hive、Spark、Flink、HetuEngine)、Hbase、Kafka、Flume、ElasticSearch、Clickhouse分别服务化到亚信pass平台。

租户通过pass平台订购Hadoop服务实例。通过Hadoop服务实例来订购hadoop资源。




华为FusionInsight MRS是一个分布式数据处理系统，对外提供大容量的数据存储、查询和分析能力，可解决各大企业的以下需求：
•快速地整合和管理不同类型的大容量数据
•对原生形式的信息提供高级分析
•可视化所有的可用数据，供特殊分析使用
•为构建新的分析应用程序提供开发环境
•工作负荷的优化和调度


请选择至少三个主机。
请选择至少6个Redis实例。
请选择偶数个数的Redis实例。
同一个主机上选择的Redis实例需要小于或等于所选择的Redis实例总数的一半。
同一个主机上选择的Redis主实例需要小于所选择的Redis主实例总数的一半













数据资产可视化系统能够通过界面对数据资产进行统一管理并分类展示，为公司内部人员共享资产、发现资产、使用资产提供便捷的途径。

各大公司的数据资产越来越多，查找数据资产、共享数据资产变得越来越困难。
数据资产通过页面来展示，更加直观。
提供输入关键字检索数据资产，提升数据资产查询效率，更方便公司内部人员共享资产。


数据资产可视化系统分别从模型、指标、报表、源系统、数据产品、客户标签、数据服务API、接口和业务等视图角度分类展示数据资产。

```

```
依托Universe大数据平台，充分利用互联网技术及资源，深度洞察客户信息，构建实时、高效、精准、易用的数字化营销解决方案。

One Click Decision Workbench
提供一站式营销分析、策划管理能力，支撑运营商快速、灵活、可视化地实现营销流程编排与发布。
Contextual Awareness Engine
基于上下文实时还原情景信息，深度洞察客户位置和互联网行为特征变化，并将识别出来的时机作为营销事件，支撑实时决策系统。
Real-time Decision Engine
当客户触发某业务后，系统根据触发事件和营销规则，自动判断业务信息并及时做出决策响应，提升营销效率。
Customer Insight
从多个维度分析客户信息，深度洞察客户行为和偏好，实现精准营销。
Omni Customer Engagement
提供传统营销渠道和电子营销渠道，支持特殊名单、客户接触规则、渠道流量、营销时间多种接触控制策略，提升用户体验。

portal为平台或解决方案提供统一用户入口，集成产品或解决方案下的各组件，提供用户、权限、资源、单点登录等基础管理能力。

CKM全称Customer Knowledge Management，即客户知识管理，提供客户属性、标签、画像、客群的管理功能。
Corpus知识库：提供知识标注、知识检索的功能。
客户洞察通过提供高性能的数据分析手段，支持业务专家基于现有客户统一视图进行客户标签研究，经过运营实践后，逐步将这些具备业务含义的标签沉淀到客户统一视图，使企业的客户受众更加清晰、全面。

Campaign提供营销资源管理、活动策划、活动调度、活动监控、活动评估的一站式营销平台。
营销组件的目的是提供基于业务语义的持续策略决策能力。通过策略设计器，将事件、客户群、过滤条件、动作（渠道或动作）连接在一起，配置出一个或多个策略，实现策略统一管理。
营销组件在系统中的作用就是活动的管理，管理内容包括活动基础数据（系统参数、函数、对象元数据定义），资源（事件、运营位、销售品、素材、渠道、推荐模型）的管理。

PC全称Policy Center，策略中心；提供根据规则执行引擎，对外支持批量调度、实时同步产品推荐、实时小批量营销、异步kafka事件营销等多种执行方式。
将业务决策从应用程序中分离出来，实现业务代码与技术代码的分离解耦。策略引擎使用预定义的语义模块编写业务决策，接受数据输入并根据业务规则做出业务决策。

EventHub，事件中心，通过图形化托拉拽，配置复杂事件处理逻辑，支持维表过滤、窗口统计、持续监测、合并、窗口限流、过期处理、字段提取、表达式过滤等多种模式匹配功能，并通过接口把任务下发至Flink做复杂事件处理。

TagService，标签中心查询服务，对外提供高性能的基于主键的查询能力，通常可用于查询指定用户的属性、标签、画像、客户群。
```

