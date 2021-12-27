# Docker

## 虚拟机(VM)和容器

- 虚拟机允许多应用能够稳定、安全地同时运行在一个服务器中的技术，但是最大的缺点就是依赖其专用的操作系统(OS)，OS会占用额外的CPU、RAM和存储，而且安装在OS上的应用，进行迁移难度和成本都很高。

- 容器模型其实跟虚拟机模型相似，其主要的区别在于，运行在相同宿主机上的容器是共享一个操作系统的。

## 概念

Docker是容器模型中的一种具体的实现产品，是一个运行在Windows或Linux上的软件。

### 镜像

镜像是一堆静态文件。比如虚拟机本质是许多文件，可以把这些文件看成虚拟机镜像，整体移动文件，即是移动虚拟机。

- 虚拟机镜像安装的是操作系统，操作系统上可以安装各种应用。

- Docker镜像是应用和支撑应用所必须的环境，比如web镜像是一个web应用和支撑web应用的环境如tomcat、jdk、mysql等。

### 容器

可以把容器看装镜像的盒子，容器彼此之间相互隔离、互不可见，可以运行里边的镜像里的应用。镜像的静态文件在容器中动态运行。

> 镜像：是静态的，里边有应用，是没有运行起来的应用。好比Java的类。
>
> 容器：是动态的，是镜像跑起来，运行起来后的概念，镜像在怎么跑起来，靠Docker引擎。好比类实例化后的对象。

### 仓库

镜像仓库服务器存放着多个仓库。每个仓库存放某一类镜像，通过不同的标签( tag)来进行区分。

根据所存储的镜像公开分享与否，Docker 仓库可以分为公开仓库(Public) 和私有仓库(Private) 两种形式。

## Docker引擎

> *通过Docker引擎下载镜像，启动镜像(基于镜像生成一个容器实例)*
>
> 宿主机：安装Docker引擎的计算机主机，虚拟机里面安装Docker则虚拟机即是宿主机。
>
> 虚拟机：在虚拟机引擎里安装的操作系统。
>
> 物理机：在物理计算机上安装的操作系统。

### 概念

- Docker引擎是用于运行和编排容器的基础设施工具(之后安装的服务，其他产品运行在服务上)。Docker引擎是是容器最核心的东西，其他产品基于Docker引擎的核心功能进行集成。
- 2017 年第一季度以前，Docker 版本号遵循**大版本号.小版本号的格式**。采用新格式前的最后一个版本是Docker 1.13。从2017年第一季度开始，Docker 版本号遵循YY.MM-xx格式，例如，2018 年6月第一次发布的社区版本为18.06.0-CE。

### 安装

- Windows安装

  - 下载地址：`https://hub.docker.com/editions/community/docker-ce-desktop-windows/`
  - 需要运行在一个安装了64位Windows 10操作系统的计算机上，通过启动一个独立的引擎来提供Docker环境。
  - 安装Docker的计算机要求在BIOS开启硬件虚拟化支持，Windows系统需要开启Hyper-v服务
  
- Linux安装

  ```sh
  #安装Docker CE
  yum -y install docker-ce docker-ce-cli containerd.io
  #启动Docker
  systemctl start docker
  systemctl status docker
  #测试安装好没的
  docker version
  ```

  Docker服务启动时实际上是调用了dockerd命令来启动Docker服务，dockerd是管理后台进程，默认的配置文件为/etc/docker/daemon.json

  ```sh
  #-D：开启Debug模式
  #-H：设置监听端口
  $ dockerd -D -H tcp://127.0.0.1:2376
  
  #也可以修改Docker配置文件/etc/docker/daemon.json，随着dockerd启动而生效：
  {
  "debug": true,
  "hosts": {"tcp://127.0.0.1:2376"}
  }
  
  #查看Docker服务的日志信息
  在RedHat系统上日志文件为/var/log/messages,
  在Ubuntu或CentOS系统上可以执行命令
  $ journalctl -u docker.service
  
  #查看Docker信息
  $ docker info 
  ```

## Docker镜像

### 下载获取

默认从官方(docker.hub.com)的仓库Docker Hub上下载镜像，可以把Docker的默认配置改为从国内的阿里云镜像仓库里获取镜像

**镜像加速器配置**

可以加速下载

> 1. 访问阿里云地址：dev.aliyun.com
>
> 2. 注册阿里云账号
>
> 3. 找到镜像加速器，拿到专属自己的镜像加速地址   [https://ycq9id5i.mirror.aliyuncs.com](https://ycq9id5i.mirror.aliyuncs.com)    
>
>    将说明中的配置，配置到Docker引擎的配置文件/etc/docker/daemon.json
>
>    {
>
>    ​	 "registry-mirrors": ["https://ycq9id5i.mirror.aliyuncs.com"]
>
>    }
>
>    //可选网易：http://hub-mirror.c.163.com
>
> 4. 让上面的配置文件生效，重新加载一下：systemctl daemon-reload
>
> 5. 重启docker：systemctl restart docker

**下载放置地址**

Linux Docker 主机本地镜像仓库通常位于`/var/lib/docker/`

Windows Docker主机则是`C:\ProgramData\docker\windowsfilter`

**下载的镜像文件结构**

是以层的结构组成的：

![img](img_Docker/clipboard.png)

> 可以共享，不同的镜像，如果有相同的部分层文件，他们不同的镜像之间会共享相同的层，只需要下载和保存不同的层就可以了，这样就节省了大量的存储空间。

### 镜像命令

#### 下载

```sh
$ docker [image] pull [可选的选项参数] 镜像仓库名称:tag标记

#tag标记不写，默认latest，latest标签意味着该镜像的内容会跟踪最新版本的变更而变化，内容是不稳定的
#镜像的仓库名称中还应该添加仓库地址，默认使用的是官方Docker Hub 服务，该前缀可以忽略
#可选的选项参数
-a/--all-tags=true | false: 是否获取仓库中的所有镜像，默认为否
--disable-content-trust: 取消镜像的内容校验，默认为真


$ docker pull redis:6.0.5
$ docker image pull redis:6.0.5
$ docker pull registry.hub.docker.com/redis:6.0.5
$ docker pull hub.c.163.com/public/redis:2.8.4 #网易云的镜像源

#pull后镜像就从远程仓库中，下载到本地了，下载后就可以用来构建容器，运行起来
$ docker run -it redis:6.0.5
```



#### 查看

```sh
#列出本地主机上已有镜像的基本信息
$ docker images
$ docker image ls
REPOSITORY    TAG     IMAGE ID           CREATED       SIZE
ubuntu        18.04   452a96d81c30    	2 weeks ago    79.6MB

#-a或--all=“true“列出所有(包括临时文件)镜像文件，默认为false
$ docker images -a

#只显示镜像ID
$ docker images -q

#-f或--filter可以用来过滤
#只显示没有被使用的镜像
$ docker images -f  "dangling=true"

#根据repository名称和tag模糊过滤
$ docker images --filter reference='busy*:*libc'

#默认是false会截断过长的信息，比如IMAGE ID
$ docker images --no-trunc=true
```

```sh
#添加镜像标签(类似快捷方式)
$ docker tag ubuntu:latest myubuntu:latest 
```

```sh
#查看详细信息
$ docker image inspect redis:6.0.5 

#-f用来指定获取其中一项内容
$ docker image inspect -f {{".RepoDigests"}}  redis:6.0.5 	#获取镜像的RepoDigests
```

```sh
# -f或--filter搜索Docker Hub官方仓库中的镜像
#搜索官方提供的带redis关键字的镜像
$ docker search --filter=is-official=true redis

#搜收藏数量大于等于90的redis的镜像
$ docker search --filter=stars=90 redis
```

#### 删除清理

```sh
#通过标签删除镜像，当一个镜像有多个标签时，只会删除当前标签
$ docker image  rm redis:latest
$ docker rmi redis:latest

#通过镜像ID删除镜像，当一个镜像有多个标签时删除失败
$ docker rmi 452a96d81c30
#-f，-force强制删除，才能删除成功
$ docker rmi 452a96d81c30 -f
```

```sh
#清理镜像，使用Docker一段时间后，系统中可能会遗留一些临时的镜像文件，以及一些没有被使用的镜像需要清理
#自动清理临时的遗留镜像文件层，最后会提示释放的存储空间
$ docker image prune -f
Total reclaimed space: 1.4 GB

-a, -all:删除所有无用镜像，不光是临时镜像;
-filter filter: 只清理符合给定过滤器的镜像;
-f，-force:强制删除镜像，而不进行提示确认。
```

#### 创建

```sh
#在现有容器的基础上创建新镜像
docker [container] commit [options]  containerid  [repository[:tag]]

#-a，--author="":作者信息;
#-C，--change=[] :提交的时候执行Dockerfile指令,包括CMD | ENTRYPOINT |ENV | EXPOSE |LABEL | ONBUILD |USER | VOLUME WORKDIR 等;
#-m，--message="":提交消息;
#-p，--pause=true:提交时暂停容器运行。

$ docker container commit -a "zhonghao" -m "创建新镜像测试" 98da88460690 contest:1
sha256:cd53befffef838231ed906fc0053a5c15dc97e2abece5fa926edb14d1255a0bf  #返回新创建的镜像的ID:
```

#### 导出载入

```sh
# -o、-output导出本地的hello-world:latest镜像为文件hhh.tar
$ docker [image] save -o hhh.tar hello-world:latest

#-i、-input从文件hhh.tar导人镜像到本地镜像库
$ docker load -i hhh.tar
$ docker load < hhh.tar
```

#### 上传

上传到公共的或者私有的镜像仓库

**使用阿里云**

构建自己的网络上的镜像仓库，进入到阿里云的官网：dev.aliyun.com查看操作指南

```sh
#将本地的镜像添加新的标签，标签路径可在阿里云账号上获取
$ docker tag hello_world:lastest registry.cn-hangzhou.aliyuncs.com/mypro/repodemo:hello001
#上传镜像到阿里云仓库
$ docker push registry.cn-hangzhou.aliyuncs.com/mypro/repodemo:hello001
```

**创建本地私有仓库**

```sh
#下载安装本地镜像   
$ docker run -d -p 5000:5000 --restart=always --name registry2 registry:2

#5000:5000宿主机端口:容器端口
#--restart=alwarys 标识当docker daemon启动的时候一起启动私库容器
#--name registry2容器名称
#registry:2指的是私库镜像标签：tag

#将镜像上传到docker私服
$ docker tag centos:7 localhost:5000/centos:7
$ docker push localhost:5000/centos

#获取私有仓库镜像
$ docker pull 127.0.0.1:5000/centos:7
$ docker pull localhost:5000/centos:7
$ docker pull 192.168.17.128:5000/centos:7
```

> 用ip地址，https和http不匹配报错的话尝试：
>
> 在/etc/docker下的daemon.json
>
> {
>
> "insecure-registries":["192.168.17.128:5000"]
>
> }
>
> 
>
> 修改了配置文件后重启docker
>
> systemctl daemon-reload
>
> systemctl restart docker

```sh
#查看私服镜像所有仓库
$ curl http://localhost:5000/v2/_catalog

#查看仓库中镜像的所有标签列表
$ curl http://localhost:5000/v2/centos/tags/list
```

## Docker容器

















