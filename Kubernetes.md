# Kubernetes

[官网](https://kubernetes.io/zh/)

## 概念

### 概述

- Kubernetes 是一个可移植的，可扩展的开源平台，用于管理容器化的工作负载和服务，方便了声明式配置和自动化。 
- Kubernetes 集群由代表控制平面的组件和一组称为节点的机器组成。 
- Kubernetes 控制平面的核心是 API 服务器和它暴露的 HTTP API。 用户、集群的不同部分以及外部组件都通过 API 服务器相互通信。 Kubernetes API 使你可以查询和操纵 Kubernetes 中对象的状态。 
- Kubernetes 对象是 Kubernetes 系统中的持久性实体。Kubernetes 使用这些实体表示您的集群状态。 

#### 定义

容器是打包和运行应用程序的好方式。在生产环境中，你需要管理运行应用程序的容器，并确保不会停机。Kubernetes 可以解决这些问题。

k8s 这个缩写是因为 k 和 s 之间有八个字符的关系。  

Kubernetes 提供了一个可弹性运行分布式系统的框架。 

Kubernetes 提供：

- **服务发现和负载均衡**

  Kubernetes 可以使用 DNS 名称或自己的 IP 地址公开容器，如果进入容器的流量很大， Kubernetes 可以负载均衡并分配网络流量，从而使部署稳定。

- **存储编排**

  Kubernetes 允许你自动挂载你选择的存储系统，例如本地存储、公共云提供商等。

- **自动部署和回滚**

  你可以使用 Kubernetes 描述已部署容器的所需状态，它可以以受控的速率将实际状态 更改为期望状态。例如，你可以自动化 Kubernetes 来为你的部署创建新容器， 删除现有容器并将它们的所有资源用于新容器。

- **自动完成装箱计算**

  Kubernetes 允许你指定每个容器所需 CPU 和内存（RAM）。 当容器指定了资源请求时，Kubernetes 可以做出更好的决策来管理容器的资源。

- **自我修复**

  Kubernetes 重新启动失败的容器、替换容器、杀死不响应用户定义的 运行状况检查的容器，并且在准备好服务之前不将其通告给客户端。

- **密钥与配置管理**

  Kubernetes 允许你存储和管理敏感信息，例如密码、OAuth 令牌和 ssh 密钥。 你可以在不重建容器镜像的情况下部署和更新密钥和应用程序配置，也无需在堆栈配置中暴露密钥

#### 组件

- 部署完 Kubernetes, 即拥有了一个完整的集群。
- 一个 Kubernetes 集群由一组被称作节点的机器组成。这些节点上运行 Kubernetes 所管理的容器化应用。集群具有至少一个工作节点。
- 工作节点托管作为应用负载的组件的 Pod 。控制平面管理集群中的工作节点和 Pod 。 为集群提供故障转移和高可用性，这些控制平面一般跨多主机运行，集群跨多个节点运行。

##### 控制平面组件

- 控制平面组件（Control Plane Components）对集群做出全局决策(比如调度)，以及检测和响应集群事件。
- 控制平面组件可以在集群中的任何节点上运行。 然而，为了简单起见，设置脚本通常会在同一个计算机上启动所有控制平面组件， 并且不会在此计算机上运行用户容器。 

###### kube-controller-manager

运行控制器进程的控制平面组件。从逻辑上讲，每个控制器都是一个单独的进程， 但是为了降低复杂性，它们都被编译到同一个可执行文件，并在一个进程中运行。 控制器通过 apiserver 监控集群的公共状态，并致力于将当前状态转换为期望状态。

这些控制器包括:

- 节点控制器（Node Controller）: 负责在节点出现故障时进行通知和响应
- 任务控制器（Job controller）: 监测代表一次性任务的 Job 对象，然后创建 Pods 来运行这些任务直至完成
- 端点控制器（Endpoints Controller）: 填充端点(Endpoints)对象(即加入 Service 与 Pod)
- 服务帐户和令牌控制器（Service Account & Token Controllers）: 为新的命名空间创建默认帐户和 API 访问令牌

###### kube-scheduler

调度决策 

控制平面组件，负责监视新创建的、未指定运行节点（node）的 Pods，选择节点让 Pod 在上面运行。

**节点指的是Kubernetes中的工作机器，Pods指的是集群上一组正在运行的容器。**

###### kube-apiserver

API 服务器是 Kubernetes 控制面的组件， 该组件公开了 Kubernetes API。 API 服务器是 Kubernetes 控制面的前端。Kubernetes API 服务器的主要实现是 kube-apiserver。

###### etcd

etcd 是兼具一致性和高可用性的键值数据库，可以作为保存 Kubernetes 所有集群数据的后台数据库。 

##### Node 组件

节点组件在每个节点上运行，维护运行的 Pod 并提供 Kubernetes 运行环境 

###### kubelet

一个在集群中每个节点（node）上运行的代理。 它保证容器（containers）都 运行在 Pod 中。

###### kube-proxy

kube-proxy 是集群中每个节点上运行的网络代理， 实现 Kubernetes 服务（Service）概念的一部分。

###### Container runtime

云控制器管理器是指嵌入特定云的控制逻辑的控制平面组件。 云控制器管理器使得你可以将你的集群连接到云提供商的 API 之上， 并将与该云平台交互的组件同与你的集群交互的组件分离开来。 

##### 插件

###### DNS

集群 DNS 是一个 DNS 服务器，和环境中的其他 DNS 服务器一起工作，它为 Kubernetes 服务提供 DNS 记录。 

###### Web 界面

###### 容器资源监控

###### 集群层面日志

#### Kubernetes API

可以通过 kubectl 命令行接口或 类似 kubeadm 这类命令行工具来执行， 这些工具在背后也是调用 API。不过，你也可以使用 REST 调用来访问这些 API。 

##### OpenAPI 规范

Kubernetes API 服务器通过 `/openapi/v2` 末端提供 OpenAPI 规范。 你可以按照下表所给的请求头部，指定响应的格式：

| 头部              | 可选值                                                       | 说明                     |
| ----------------- | ------------------------------------------------------------ | ------------------------ |
| `Accept-Encoding` | `gzip`                                                       | *不指定此头部也是可以的* |
| `Accept`          | `application/com.github.proto-openapi.spec.v2@v1.0+protobuf` | *主要用于集群内部*       |
|                   | `application/json`                                           | *默认值*                 |
|                   | `*`                                                          | *提供*`application/json` |

#### Kubernetes 对象

##### 定义

在 Kubernetes 系统中，*Kubernetes 对象* 是持久化的实体。 Kubernetes 使用这些实体去表示整个集群的状态。它们描述了如下信息：

- 哪些容器化应用在运行（以及在哪些节点上）
- 可以被应用使用的资源
- 关于应用运行时表现的策略，比如重启策略、升级策略，以及容错策略

一旦创建对象，Kubernetes 系统将持续工作以确保对象存在。 通过创建对象，本质上是在告知 Kubernetes 系统，所需要的集群工作负载看起来是什么样子的， 这就是 Kubernetes 集群的 **期望状态（Desired State）**。 

需要使用 Kubernetes API 操作 Kubernetes 对象 。

###### 对象规约（Spec）与状态（Status）

`spec`  描述了对象的 *期望状态（Desired State）*  ，对于具有 `spec` 的对象，你必须在创建对象时设置其内容 

`status` 描述了对象的 *当前状态（Current State）* ，是由 Kubernetes 系统和组件设置并更新的 

如 Kubernetes 中的 Deployment 对象能够表示运行在集群中的应用。 当创建 Deployment 时，可能需要设置 Deployment 的 `spec`，以指定该应用需要有 3 个副本运行。 Kubernetes 系统读取 Deployment 规约，并启动我们所期望的应用的 3 个实例 

###### 描述 Kubernetes 对象

创建 Kubernetes 对象时，必须提供对象的规约， 可以在 API 请求的请求体中使用 JSON 格式的信息，在 .yaml 文件中为 kubectl 提供这些信息， `kubectl` 在发起 API 请求时，将这些信息转换成 JSON 格式。

```yml
Kubernetes Deployment 的必需字段和对象规约
application/deployment.yaml

apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 # tells deployment to run 2 pods matching the template
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
```

```sh
#使用上述文件创建 Deployment
$kubectl apply -f https://k8s.io/examples/application/deployment.yaml --record
deployment.apps/nginx-deployment created
```

##### 对象管理

`kubectl` 命令行工具支持多种不同的方式来创建和管理 Kubernetes 对象。 

| 管理技术       | 作用于   | 建议的环境 | 支持的写者 | 学习难度 |
| -------------- | -------- | ---------- | ---------- | -------- |
| 指令式命令     | 活跃对象 | 开发项目   | 1+         | 最低     |
| 指令式对象配置 | 单个文件 | 生产项目   | 1          | 中等     |
| 声明式对象配置 | 文件目录 | 生产项目   | 1+         | 最高     |

##### 对象名称和 IDs

##### Namespaces

Kubernetes 支持多个虚拟集群，它们底层依赖于同一个物理集群。 这些虚拟集群被称为命名空间。 每个 Kubernetes 资源只能在一个命名空间中。 

```sh
#列出集群中现存的命名空间
$ kubectl get namespace

#设置命名空间
$ kubectl run nginx --image=nginx --namespace=<命名空间名称>
$ kubectl get pods --namespace=<命名空间名称>

#永久保存命名空间
$ kubectl config set-context --current --namespace=<名字空间名称>
# 验证
$ kubectl config view | grep namespace:
```















