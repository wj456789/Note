# Maven

[Maven教程](https://blog.csdn.net/liupeifeng3514/category_7500193.html)

## 生命周期

> 创建项目、编写代码、清理已编译的代码、编译代码、执行单元测试、打包、集成测试、验证、部署、生成站点等，这些环节组成了项目的生命周期，这些过程也叫做项目的构建过程。

maven中定义的3套**生命周期**：

- clean生命周期
- default生命周期
- site生命周期

上面这3套生命周期是相互独立的，没有依赖关系的，而每套生命周期中有多个**阶段：**

- 每套中的多个阶段是有先后顺序的，并且后面的阶段依赖于前面的阶段；
- 当执行某个阶段时，这个阶段前面的阶段也会执行；
- 每个阶段中需要执行什么，可以通过插件的方式让用户自己去配置。
- 用户可以直接使用`mvn 阶段1 [阶段2] [阶段3] ...`命令来调用这些阶段去完成项目生命周期中具体的操作。

### clean生命周期

clean生命周期的目的是清理项目，它包含三个阶段：

| **生命周期阶段** | **描述**                              |
| ---------------- | ------------------------------------- |
| pre-clean        | 执行一些需要在clean之前完成的工作     |
| clean            | 移除所有上一次构建生成的文件          |
| post-clean       | 执行一些需要在clean之后立刻完成的工作 |

```sh
#调用clean生命周期中的pre-clean阶段需要执行的操作
$ mvn pre-clean

#执行上面3个阶段所有的操作
$ mvn post-clean
```

### default生命周期

这个是maven主要的生命周期，主要被用于构建应用，包含了23个阶段。

| **生命周期阶段**        | **描述**                                                     |
| ----------------------- | ------------------------------------------------------------ |
| validate                | 校验：校验项目是否正确并且所有必要的信息可以完成项目的构建过程。 |
| initialize              | 初始化：初始化构建状态，比如设置属性值。                     |
| generate-sources        | 生成源代码：生成包含在编译阶段中的任何源代码。               |
| process-sources         | 处理源代码：处理源代码，比如说，过滤任意值。                 |
| generate-resources      | 生成资源文件：生成将会包含在项目包中的资源文件。             |
| process-resources       | 编译：复制和处理资源到目标目录，为打包阶段最好准备。         |
| compile                 | 处理类文件：编译项目的源代码。                               |
| process-classes         | 处理类文件：处理编译生成的文件，比如说对Java class文件做字节码改善优化。 |
| generate-test-sources   | 生成测试源代码：生成包含在编译阶段中的任何测试源代码。       |
| process-test-sources    | 处理测试源代码：处理测试源代码，比如说，过滤任意值。         |
| generate-test-resources | 生成测试源文件：为测试创建资源文件。                         |
| process-test-resources  | 处理测试源文件：复制和处理测试资源到目标目录。               |
| test-compile            | 编译测试源码：编译测试源代码到测试目标目录.                  |
| process-test-classes    | 处理测试类文件：处理测试源码编译生成的文件。                 |
| test                    | 测试：使用合适的单元测试框架运行测试（Juint是其中之一）。    |
| prepare-package         | 准备打包：在实际打包之前，执行任何的必要的操作为打包做准备。 |
| package                 | 打包：将编译后的代码打包成可分发格式的文件，比如JAR、WAR或者EAR文件。 |
| pre-integration-test    | 集成测试前：在执行集成测试前进行必要的动作。比如说，搭建需要的环境。 |
| integration-test        | 集成测试：处理和部署项目到可以运行集成测试环境中。           |
| post-integration-test   | 集成测试后：在执行集成测试完成后进行必要的动作。比如说，清理集成测试环境。 |
| verify                  | 验证：运行任意的检查来验证项目包有效且达到质量标准。         |
| install                 | 安装：安装项目包到本地仓库，这样项目包可以用作其他本地项目的依赖。 |
| deploy                  | 部署：将最终的项目包复制到远程仓库中与其他开发者和项目共享。 |

### site生命周期

site生命周期的目的是建立和发布项目站点，主要包含以下4个阶段：

| **阶段**    | **描述**                                                   |
| ----------- | ---------------------------------------------------------- |
| pre-site    | 执行一些需要在生成站点文档之前完成的工作                   |
| site        | 生成项目的站点文档                                         |
| post-site   | 执行一些需要在生成站点文档之后完成的工作，并且为部署做准备 |
| site-deploy | 将生成的站点文档部署到特定的服务器上                       |

 

## 插件

**maven插件主要是为maven中生命周期中的阶段服务的**

### 插件目标

- maven只是定义了生命周期中的阶段，而没有定义每个阶段中具体的实现，这些实现是由插件的目标来完成的，生命周期中的每个阶段支持绑定多个插件的多个目标。 
- maven中的插件就相当于一些工具，每个工具包含了多个功能，插件中的每个功能就叫做插件的目标（Plugin Goal）。
- 插件：编译代码的工具，运行测试用例的工具，打包代码的工具，将代码上传到本地仓库的工具，将代码部署到远程仓库的工具等等，目标：编译代码的插件，可以编译源代码、也可以编译测试代码
- 将**插件目标和maven生命周期的阶段进行绑定**，然后通过mvn 阶段的方式执行阶段的时候，会自动执行和这些阶段绑定的插件，插件也可以通过mvn命令的方式调用直接运行

```sh
#列出插件所有目标
mvn 插件groupId:插件artifactId[:插件version]:help
mvn 插件前缀:help
```

#### maven内置插件以及绑定

我们不用做任何配置就可以执行清理代码、编译代码、测试、打包、安装到本地仓库、上传到远程仓库等阶段的操作，是因为maven内部已经默认给这些阶段绑定好了插件目标，所以不需要我们再去配置，就直接可以运行

##### clean生命周期阶段与插件绑定关系

| 生命周期阶段 | 插件:目标                |
| ------------ | ------------------------ |
| pre-clean    |                          |
| clean        | maven-clean-plugin:clean |
| post-clean   |                          |

##### default生命周期阶段与插件绑定关系

default生命周期中有23个阶段，我只列出有默认绑定的，其他的没有列出的没有绑定任何插件，因此没有任何实际的行为。

| 生命周期阶段           | 插件:目标                            | 执行任务                       |
| ---------------------- | ------------------------------------ | ------------------------------ |
| process-resources      | maven-resources-plugin:resources     | 复制主资源文件至主输出目录     |
| compile                | maven-compiler-plugin:compile        | 编译主代码至主输出目录         |
| process-test-resources | maven-resources-plugin:testResources | 复制测试资源文件至测试输出目录 |
| test-compile           | maven-compiler-plugin:testCompile    | 编译测试代码至测试输出目录     |
| test                   | maven-surefile-plugin:test           | 执行测试用例                   |
| package                | maven-jar-plugin:jar                 | 创建项目jar包                  |
| install                | maven-install-plugin:install         | 将输出构件安装到本地仓库       |
| deploy                 | maven-deploy-plugin:deploy           | 将输出的构件部署到远程仓库     |

##### site生命周期阶段与插件绑定关系

| 生命周期阶段 | 插件:目标                |
| ------------ | ------------------------ |
| pre-site     |                          |
| site         | maven-site-plugin:site   |
| post-site    |                          |
| site-deploy  | maven-site-plugin:deploy |

#### 自定义绑定

常见的一个案例是：创建项目的源码jar包，将其安装到仓库中，内置插件绑定关系中没有涉及到这一步的任务，所以需要用户自己配置。插件`maven-source-plugin`的`jar-no-fork`可以帮助我们完成该任务，我们将这个目标绑定在default生命周期的verify阶段上面，这个阶段没有任何默认绑定，verify是在测试完成之后并将构件安装到本地仓库之前执行的阶段，在这个阶段我们生成源码

### maven-compiler-plugin

> 编译Java源码，一般只需设置编译的jdk版本 

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.6.0</version>
    <configuration>
        <encoding>utf-8</encoding>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
<!-- 也可以在在properties设置jdk版本 -->
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
</properties>
```

### maven-jar-plugin

> 打成jar时，设定manifest的参数，比如指定运行的Main class，还有依赖的jar包，加入classpath中 

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>2.4</version>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>/data/lib</classpathPrefix>
                <mainClass>com.zhang.spring.App</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

### tomcat7-maven-plugin

> 用于远程部署Java Web项目 

```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <url>http://59.110.162.178:8080/manager/text</url>
        <username>linjinbin</username>
        <password>linjinbin</password>
    </configuration>
</plugin>
```

### maven-shade-plugin

> 用于把多个jar包，打成1个jar包 一般Java项目都会依赖其他第三方jar包，最终打包时，希望把其他jar包包含在一个jar包里。 

### maven-assembly-plugin

要使用maven-assembly-plugin，需要指定至少一个要使用的assembly descriptor 文件(assembly.xml)。

#### 内置

默认情况下，maven-assembly-plugin内置了几个可以用的assembly descriptor：

- bin ： 类似于默认打包，会将bin目录下的文件打到包中；
- jar-with-dependencies ： 会将所有依赖都解压打包到生成物中；
- src ：只将源码目录下的文件打包；
- project ： 将整个project资源打包。

如：使用 descriptorRefs来引用(官方提供的定制化打包方式)【不建议使用】 

##### 在pom中引入插件

```xml
<!-- pom引入 -->
<plugin>  
    <artifactId>maven-assembly-plugin</artifactId>  
    <configuration>  
        <descriptorRefs>  
            <descriptorRef>jar-with-dependencies</descriptorRef>  
        </descriptorRefs>  
    </configuration>  
</plugin>
```

##### 默认assembly配置文件

> 上述直接配置jar-with-dependencies打包方式。不需要引入额外文件。实际上，上述4中预定义的assembly descriptor有对应的xml。要查看它们的详细定义，可以到maven-assembly-plugin.jar里去看，例如对应 bin 的assembly descriptor 原始文件如下：

```xml
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0 http://maven.apache.org/xsd/assembly-1.1.0.xsd">
    <id>bin</id>
    <formats>
        <format>tar.gz</format>
        <format>tar.bz2</format>
        <format>zip</format>
    </formats>
    <fileSets>
        <fileSet>
            <directory>${project.basedir}</directory>
            <outputDirectory>/</outputDirectory>
            <includes>
                <include>README*</include>
                <include>LICENSE*</include>
                <include>NOTICE*</include>
            </includes>
        </fileSet>
        <fileSet>
            <directory>${project.build.directory}</directory>
            <outputDirectory>/</outputDirectory>
            <includes>
                <include>*.jar</include>
            </includes>
        </fileSet>
        <fileSet>
            <directory>${project.build.directory}/site</directory>
            <outputDirectory>docs</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

#### 自定义

##### 在pom中引入插件

```xml
<projects>
    <build>
        <plugins>
            <plugin>       
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <!-- 子pom也会执行execution -->
                <inherited>true</inherited> 
                <executions>
                    <execution><!-- 配置执行器 -->
                        <id>make-assembly</id>
                        <phase>package</phase><!-- 绑定到package生命周期阶段上 -->
                        <goals>
                            <goal>single</goal><!-- 插件目标为single，只运行一次 -->   
                        </goals>
                        <!-- configuration标签可以和executions同级 -->
                        <configuration>  
                            <finalName>demo</finalName> 
                            <archive>
                                <manifest>
                                    <addClasspath>true</addClasspath>
                                    <!-- 主类名 -->
                                    <mainClass>com.***.startup.BootStrap</mainClass> 
                                </manifest>
                            </archive>
                            <!-- 最后默认在目录output下会生成一个demo-demo.jar 文件，其中前一个demo来自标签finalName，后一个demo来自assembly.xml中的id,设置false就不会跟上AssemblyId -->
                            <appendAssemblyId>false</appendAssemblyId>  
                            <descriptors>
                                <!--配置描述文件路径(相对于pom的路径)--> 
                                <descriptor>src/assembly/assembly.xml</descriptor>  
                            </descriptors>  
                            <!-- outputDirectory定义了压缩包最终的位置，${basedir}为pom所在的位置 -->
                            <outputDirectory>output</outputDirectory>
                        </configuration> 
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</projects>

<!-- 多重的复杂情况存在的时候，也可以考虑使用profile的方式，这样可以有条件判断 -->
<profile>
    <id>example</id>
    <activation>
        <file>
            <!-- 先判断有没有descriptor文件然后再跑，这里也可以添加其他条件 -->
            <exists>src/package.xml</exists>
        </file>
    </activation>
    <build>
        <plugins>
            ...
        </plugins>
    </build>
</profile>
```

##### 指定assembly配置文件

使用 `descriptors`，指定打包文件 `src/assembly/assembly.xml`，即在配置文件内指定打包操作要使用这个自定义assembly descriptor(自定义的xml中配置)，需要如下配置，即要引入描述文件：

```xml
<!-- src/assembly/assembly.xml: -->
<?xml version='1.0' encoding='UTF-8'?>
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.0  
                    http://maven.apache.org/xsd/assembly-1.1.0.xsd">
    <id>demo</id>
    
    <formats>
        <format>jar</format>
    </formats>
    
    <includeBaseDirectory>false</includeBaseDirectory>
    
    <fileSets>
        <fileSet>
            <directory>${project.build.directory}/classes</directory>
            <outputDirectory>/</outputDirectory>
        </fileSet>
    </fileSets>
</assembly>
```

- format：指定打包类型；支持的打包格式有zip、tar、tar.gz (or tgz)、tar.bz2 (or tbz2)、jar、dir、war，可以同时指定多个打包格式 

- includeBaseDirectory：指定是否包含打包层目录（比如finalName是output，当值为true，所有文件被放在output目录下，否则直接放在包的根目录下）；

- fileSets：指定要包含的文件集，可以定义多个fileSet；

  管理一组文件的存放位置，核心元素如下表所示。

  | 元素            | 类型           | 作用                                                         |
  | --------------- | -------------- | ------------------------------------------------------------ |
  | outputDirectory | String         | 指定文件集合的输出目录，该目录是相对于根目录                 |
  | includes        | `List<String>` | 包含文件                                                     |
  | excludes        | `List<String>` | 排除文件                                                     |
  | fileMode        | String         | 指定文件属性，使用八进制表达，分别为(User)(Group)(Other)所属属性，默认为 0644 |

  

- dependencySets

  用来定制工程依赖 jar 包的打包方式，核心元素如下表所示。

  | 元素            | 类型           | 作用                                 |
  | --------------- | -------------- | ------------------------------------ |
  | outputDirectory | String         | 指定包依赖目录，该目录是相对于根目录 |
  | includes        | `List<String>` | 包含依赖                             |
  | excludes        | `List<String>` | 排除依赖                             |

  ```xml
  <dependencySets>
      <dependencySet>
          <outputDirectory>/lib</outputDirectory>
          <excludes>
              <exclude>${project.groupId}:${project.artifactId}</exclude>
          </excludes>
      </dependencySet>
      <dependencySet>
          <outputDirectory>/</outputDirectory>
          <includes>
              <include>${project.groupId}:${project.artifactId}</include>
          </includes>
          <!-- 将scope为runtime的依赖包打包到lib目录下。 -->
          <scope>runtime</scope>
      </dependencySet>
  </dependencySets>
  ```

### docker-maven-plugin

可以自动生成镜像并推送到仓库中 

#### 构建镜像

构建镜像可以使用一下两种方式，第一种是将构建信息指定到 POM 中，第二种是使用已存在的 Dockerfile 构建。 

第一种方式，支持将 `FROM`, `ENTRYPOINT`, `CMD`, `MAINTAINER` 以及 `ADD` 信息配置在 POM 中，不需要使用 Dockerfile 配置。但是如果使用 `VOLUME` 或其他 Dockerfile 中的命令的时候，需要使用第二种方式，创建一个 Dockerfile，并在 POM 中配置 `dockerDirectory` 来指定路径即可。 

##### POM

```xml
<!-- pom.xml -->

<properties>
    <assembly.format>tar.gz</assembly.format>
    <DOCKER_HOST>http://10.244.148.28:2375</DOCKER_HOST>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <!-- 连接 Docker 地址 -->
                <dockerHost>${DOCKER_HOST}</dockerHost>
                <imageName>mavendemo</imageName>
                <baseImage>java</baseImage>
                <maintainer>docker_maven docker_maven@email.com</maintainer>
                <workdir>/ROOT</workdir>
                <cmd>["java", "-version"]</cmd>
                <entryPoint>
                    ["java", "-jar", "${project.build.finalName}.${assembly.format}"]
                </entryPoint>
                <!-- 从"directory"下"include"复制到 docker 容器指定"targetPath"目录 -->
                <resources>
                    <resource>
                        <targetPath>/ROOT</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.${assembly.format}</include>
                    </resource>
                </resources>
                <!-- 安全认证配置 -->
                <serverId>my-docker-registry</serverId>
            </configuration>
            <executions>
                <execution>
                    <id>build-image</id>
                    <phase>package</phase>
                    <goals>
                        <goal>build</goal>
                    </goals>
                </execution>
                <execution>
                    <id>tag-image</id>
                    <phase>package</phase>
                    <goals>
                        <goal>tag</goal>
                    </goals>
                    <configuration>
                        <image>mavendemo:latest</image>
                        <newName>docker.io/wanyang3/mavendemo:${project.version}</newName>
                    </configuration>
                </execution>
                <execution>
                    <id>push-image</id>
                    <phase>deploy</phase>
                    <goals>
                        <goal>push</goal>
                    </goals>
                    <configuration>
                        <imageName>docker.io/wanyang3/mavendemo:${project.version}</imageName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

1) 以上示例， 可以绑定 Docker 命令到 Maven 各个阶段，我们可以把 Docker 分为 build、tag、push，然后分别绑定 Maven 的 package、deploy 阶段，当我们执行mvn package时，执行 build、tag 操作，当执行mvn deploy时，执行build、tag、push 操作。如果我们想跳过 docker 某个过程时，只需要：

- `-DskipDockerBuild` 跳过 build 镜像
- `-DskipDockerTag` 跳过 tag 镜像
- `-DskipDockerPush` 跳过 push 镜像
- `-DskipDocker` 跳过整个阶段

例如：我们想执行 package 时，跳过 tag 过程，那么就需要`mvn package -DskipDockerTag`。

2) 执行命令

`mvn clean package docker:build` 只执行 build 操作

`mvn clean package docker:build -DpushImage` 执行 build 完成后 push 镜像

`mvn clean package docker:build -DpushImageTag` 执行 build 并 push **指定 tag 的镜像**

注意：这里必须指定至少一个 imageTag，它可以配置到 POM 中，也可以在命令行指定。命令行指定如下：`mvn clean package docker:build -DpushImageTags -DdockerImageTags=imageTag_1 -DdockerImageTags=imageTag_2`，POM 文件中指定配置如下： 

```xml
<build>
    <plugins>
        ...
        <plugin>
            ...
            <configuration>
                ...
                <imageTags>
                    <imageTag>imageTag_1</imageTag>
                    <imageTag>imageTag_2</imageTag>
                </imageTags>
            </configuration>
        </plugin>
    </plugins>
</build>
```

3) 当我们 push 镜像到 Docker 仓库中时，有时需要安全认证，登录完成之后才可以进行操作。 可以通过命令行 `docker login -u user_name -p password docker_registry_host` 登录 ，也可以在 docker-maven-plugin 插件中配置

首先在 Maven 的配置文件 setting.xml 中增加相关 server 配置，主要配置 Docker registry用户认证信息。 

```xml
<servers>
    <server>
        <id>my-docker-registry</id>
        <username>wanyang3</username>
        <password>12345678</password>
        <configuration>
            <email>wanyang3@mail.com</email>
        </configuration>
    </server>
</servers>
```

然后只需要在 pom.xml 中使用 server id 即可。 

4) docker-maven-plugin 插件还提供了很多很实用的配置

| 参数                                      | 说明                                                         | 默认值 |
| ----------------------------------------- | ------------------------------------------------------------ | ------ |
| `<forceTags>true</forceTags>`             | build 时强制覆盖 tag，配合 imageTags 使用                    | false  |
| `<noCache>true</noCache>`                 | build 时，指定 –no-cache 不使用缓存                          | false  |
| `<pullOnBuild>true</pullOnBuild>`         | build 时，指定 –pull=true 每次都重新拉取基础镜像             | false  |
| `<pushImage>true</pushImage>`             | build 完成后 push 镜像                                       | false  |
| `<pushImageTag>true</pushImageTag>`       | build 完成后，push 指定 tag 的镜像，配合 imageTags 使用      | false  |
| `<retryPushCount>5</retryPushCount>`      | push 镜像失败，重试次数                                      | 5      |
| `<retryPushTimeout>10</retryPushTimeout>` | push 镜像失败，重试时间                                      | 10s    |
| `<rm>true</rm>`                           | build 时，指定 –rm=true 即 build 完成后删除中间容器          | false  |
| `<useGitCommitId>true</useGitCommitId>`   | build 时，使用最近的 git commit id 前7位作为tag，例如：image:b50b604，前提是不配置 newName | false  |

##### Dockerfile

```xml
<!-- pom.xml -->
<build>
    <plugins>
        <plugin>
            <groupId>com.spotify</groupId>
            <artifactId>docker-maven-plugin</artifactId>
            <version>1.0.0</version>
            <configuration>
                <imageName>mavendemo</imageName>
                <dockerDirectory>${basedir}/docker</dockerDirectory> <!-- 指定 Dockerfile 路径-->
                <!-- 这里是复制 jar 包到 docker 容器指定目录配置，也可以写到 Dockerfile 中 -->
                <resources>
                    <resource>
                        <targetPath>/ROOT</targetPath>
                        <directory>${project.build.directory}</directory>
                        <include>${project.build.finalName}.jar</include>
                    </resource>
                </resources>
            </configuration>
        </plugin>   
    </plugins>
</build>
```

```dockerfile
#${basedir}/docker/Dockerfile
FROM java
MAINTAINER docker_maven docker_maven@email.com
WORKDIR /ROOT
CMD ["java", "-version"]
ENTRYPOINT ["java", "-jar", "${project.build.finalName}.jar"]
```



[Maven 插件之 docker-maven-plugin 的使用](https://www.cnblogs.com/jpfss/p/10945324.html)



### maven-war-plugin

```xml
<!-- 打包成war包 -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <webResources>
            <resource>
                <directory>../data-assest-common/src/lib</directory>
                <targetPath>WEB-INF/lib/</targetPath>
                <includes>
                    <include>**/*.jar</include>
                </includes>
            </resource>
        </webResources>
        <!--如果想在没有web.xml文件的情况下构建WAR，请设置为false。-->
        <failOnMissingWebXml>false</failOnMissingWebXml>
    </configuration>
</plugin>
```

### spring-boot-maven-plugin

```xml
<!-- 
	1.可以打成直接运行的Jar包
	2.一般的maven项目的打包命令，不会把依赖的jar包也打包进去的，只是会放在jar包的同目录下，能够引用就可以了，但是spring-boot-maven-plugin插件，会将依赖的jar包全部打包进jar包内部。
	3.可以指定默认执行类
 -->
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>2.2.4.RELEASE</version>
    <configuration>
        <mainClass>com.huawei.DataAssetApplicationHttp</mainClass>
        <!--<skip>true</skip>-->
        <!--<includeSystemScope>true</includeSystemScope>-->
        <!--<layout>ZIP</layout>-->
        <!--<includes>
            <include>
                <groupId>nothing</groupId>
                <artifactId>nothing</artifactId>
            </include>
        </includes>-->
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**repackage作用：**

1、在原始 Maven 打包形成的 jar 包基础上，进行重新打包，新形成的 jar 包不但包含应用类文件和配置文件，而且还会包含应用所依赖的 jar 包以及 Springboot 启动相关类，以此来满足Springboot独立应用的特性；

2、将原始 Maven 打包的 jar 重命名为 XXX.jar.original 作为原始文件；


[spring-boot-maven-plugin打包war包](https://cloud.tencent.com/developer/article/1768906)

### maven-surefire-plugin

```xml
<!-- 设置打包的时候跳过测试用例,相当于 mvn package -Dmaven.test.skip=true -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <skip>true</skip>
    </configuration>
</plugin>
```





参考：

[maven-compiler-plugin](https://www.cnblogs.com/east7/p/13363069.html)

[maven--插件篇（assembly插件）](https://www.cnblogs.com/sidesky/p/10651266.html)

[Maven 插件之 docker-maven-plugin 的使用](https://www.cnblogs.com/jpfss/p/10945324.html)

```sh
在命令行下,到pom.xml所在目录下,执行 mvn assembly:assembly -Dmaven.test.skip=true
mvn assembly:assembly -Dmaven.test.skip=true
```

[maven](https://blog.csdn.net/sxyandapp/category_5839321.html?spm=1001.2014.3001.5482)

[idea执行maven命令的三种方式](https://www.cnblogs.com/aaabbbcccddd/p/15049355.html)

## pom.xml

### dependencyManagement

- 在父 pom 中使用 dependencyManagement 只是声明依赖，并不实现引入，因此子项目需要显式的声明需要用的依赖。 

- 没有写在 dependencyManagement 中的 dependencies ，不仅会引入到当前的 maven 项目，也会被子 maven 项目继承。 

- 在子 pom 中使用 parent 继承父 pom ，子 pom 只需要写 groupId 和 artifactId 即可，会自动去父 pom 中查找继承 version 和 scope 等。 

## settings.xml

- 用来设置maven参数的配置文件，settings.xml是maven的全局配置文件，而pom.xml文件是所在项目的局部配置。  
- settings.xml文件一般存在于两个位置： 
  - 全局配置: ${M2_HOME}/conf/settings.xml 
  - 用户配置: user.home/.m2/settings.xml

**局部配置优先于全局配置**。 配置优先级从高到低：pom.xml> user settings > global settings 如果这些文件同时存在，在应用配置时，会合并它们的内容，如果有重复的配置，优先级高的配置会覆盖优先级低的。 

```xml
<!-- settings.xml中的顶级元素 -->
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository/>
    <interactiveMode/>
    <usePluginRegistry/>
    <offline/>
    <pluginGroups/>
    <servers/>
    <mirrors/>
    <proxies/>
    <profiles/>
    <activeProfiles/>
</settings>
```

### InteractiveMode

> 表示maven是否需要和用户交互以获得输入，如果maven需要和用户交互以获得输入，则设置成true，反之则应为false。默认为true。 

```xml
<interactiveMode>true</interactiveMode>
```

### UsePluginRegistry

> maven是否需要使用plugin-registry.xml文件来管理插件版本。 如果需要让maven使用文件~/.m2/plugin-registry.xml来管理插件版本，则设为true。默认为false。 

```xml
<usePluginRegistry>false</usePluginRegistry>
```

### Offline

> 表示maven是否需要在离线模式下运行。 如果构建系统需要在离线模式下运行，则为true，默认为false。 当由于网络设置原因或者安全因素，构建服务器不能连接远程仓库的时候，该配置就十分有用。 

```xml
<offline>false</offline>
```

### PluginGroups

> 当插件的组织id（groupId）没有显式提供时，供搜寻插件组织Id（groupId）的列表。 可以在列表中搜寻匹配组织Id。

该元素包含一个pluginGroup元素列表，每个子元素包含了一个组织Id（groupId）。 当我们使用某个插件，并且没有在命令行为其提供组织Id（groupId）的时候，Maven就会使用该列表。默认情况下该列表包含了`org.apache.maven.plugins`和`org.codehaus.mojo`。 

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
    <pluginGroups>
        <!--plugin的组织Id（groupId） -->
        <pluginGroup>org.codehaus.mojo</pluginGroup>
        <pluginGroup>org.apache.maven.plugins</pluginGroup>
    </pluginGroups>
    ...
</settings>
```

### Servers

有些仓库访问是需要安全认证的，用户名、密码等信息配置在`settings.xml`中。  

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
    <!--配置服务端的一些设置。一些设置如安全证书不应该和pom.xml一起分发。这种类型的信息应该存在于构建服务器上的settings.xml文件中。 -->
    <servers>
        <!--服务器元素包含配置服务器时需要的信息 -->
        <server>
            <!--这是server的id（注意不是用户登陆的id），该id与distributionManagement中repository元素的id相匹配。 -->
            <id>server001</id>
            <!--鉴权用户名。鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码。 -->
            <username>my_login</username>
            <!--鉴权密码 。鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码。密码加密功能已被添加到2.1.0 +。详情请访问密码加密页面 -->
            <password>my_password</password>
            <!--鉴权时使用的私钥位置。和前两个元素类似，私钥位置和私钥密码指定了一个私钥的路径（默认是${user.home}/.ssh/id_dsa）以及如果需要的话，一个密语。将来passphrase和password元素可能会被提取到外部，但目前它们必须在settings.xml文件以纯文本的形式声明。 -->
            <privateKey>${usr.home}/.ssh/id_dsa</privateKey>
            <!--鉴权时使用的私钥密码。 -->
            <passphrase>some_passphrase</passphrase>
            <!--文件被创建时的权限。如果在部署的时候会创建一个仓库文件或者目录，这时候就可以使用权限（permission）。这两个元素合法的值是一个三位数字，其对应了unix文件系统的权限，如664，或者775。 -->
            <filePermissions>664</filePermissions>
            <!--目录被创建时的权限。 -->
            <directoryPermissions>775</directoryPermissions>
        </server>
    </servers>
    ...
</settings>

```

### Mirrors

> 为仓库列表配置的下载镜像列表 

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
    <mirrors>
        <!-- 给定仓库的下载镜像。 -->
        <mirror>
            <!-- 该镜像的唯一标识符。id用来区分不同的mirror元素。 -->
            <id>planetmirror.com</id>
            <!-- 镜像名称 -->
            <name>PlanetMirror Australia</name>
            <!-- 该镜像的URL。构建系统会优先考虑使用该URL，而非使用默认的服务器URL。 -->
            <url>http://downloads.planetmirror.com/pub/maven2</url>
            <!-- 被镜像的服务器的id。例如，如果我们要设置了一个Maven中央仓库（http://repo.maven.apache.org/maven2/）的镜像，就需要将该元素设置成central。这必须和中央仓库的id central完全一致。 -->
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
    ...
</settings>

```

### Proxies

> 用来配置不同的代理 

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
    ...
    <proxies>
        <!--代理元素包含配置代理时需要的信息 -->
        <proxy>
            <!--代理的唯一定义符，用来区分不同的代理元素。 -->
            <id>myproxy</id>
            <!--该代理是否是激活的那个。true则激活代理。当我们声明了一组代理，而某个时候只需要激活一个代理的时候，该元素就可以派上用处。 -->
            <active>true</active>
            <!--代理的协议。 协议://主机名:端口，分隔成离散的元素以方便配置。 -->
            <protocol>http</protocol>
            <!--代理的主机名。协议://主机名:端口，分隔成离散的元素以方便配置。 -->
            <host>proxy.somewhere.com</host>
            <!--代理的端口。协议://主机名:端口，分隔成离散的元素以方便配置。 -->
            <port>8080</port>
            <!--代理的用户名，用户名和密码表示代理服务器认证的登录名和密码。 -->
            <username>proxyuser</username>
            <!--代理的密码，用户名和密码表示代理服务器认证的登录名和密码。 -->
            <password>somepassword</password>
            <!--不该被代理的主机名列表。该列表的分隔符由代理服务器指定；例子中使用了竖线分隔符，使用逗号分隔也很常见。 -->
            <nonProxyHosts>*.google.com|ibiblio.org</nonProxyHosts>
        </proxy>
    </proxies>
    ...
</settings>
```

### Profiles

> 根据环境参数来调整构建配置的列表 

### Repositories

> 远程仓库列表，它是maven用来填充构建系统本地仓库所使用的一组远程仓库 

```xml
<repositories>
    <!--包含需要连接到远程仓库的信息 -->
    <repository>
        <!--远程仓库唯一标识 -->
        <id>codehausSnapshots</id>
        <!--远程仓库名称 -->
        <name>Codehaus Snapshots</name>
        <!--如何处理远程仓库里发布版本的下载 -->
        <releases>
            <!--true或者false表示该仓库是否为下载某种类型构件（发布版，快照版）开启。 -->
            <enabled>false</enabled>
            <!--该元素指定更新发生的频率。Maven会比较本地POM和远程POM的时间戳。这里的选项是：always（一直），daily（默认，每日），interval：X（这里X是以分钟为单位的时间间隔），或者never（从不）。 -->
            <updatePolicy>always</updatePolicy>
            <!--当Maven验证构件校验文件失败时该怎么做-ignore（忽略），fail（失败），或者warn（警告）。 -->
            <checksumPolicy>warn</checksumPolicy>
        </releases>
        <!--如何处理远程仓库里快照版本的下载。有了releases和snapshots这两组配置，POM就可以在每个单独的仓库中，为每种类型的构件采取不同的策略。例如，可能有人会决定只为开发目的开启对快照版本下载的支持。参见repositories/repository/releases元素 -->
        <snapshots>
            <enabled />
            <updatePolicy />
            <checksumPolicy />
        </snapshots>
        <!--远程仓库URL，按protocol://hostname/path形式 -->
        <url>http://snapshots.maven.codehaus.org/maven2</url>
        <!--用于定位和排序构件的仓库布局类型-可以是default（默认）或者legacy（遗留）。Maven 2为其仓库提供了一个默认的布局；然而，Maven 1.x有一种不同的布局。我们可以使用该元素指定布局是default（默认）还是legacy（遗留）。 -->
        <layout>default</layout>
    </repository>
</repositories>
```

#### 仓库类型

maven中的仓库分为两种，**snapshot快照仓库**和**release发布仓库**。snapshot快照仓库用于保存开发过程中的不稳定版本，release正式仓库则是用来保存稳定的发行版本。定义一个组件/模块为快照版本，只需要在pom文件中在该模块的版本号后加上-SNAPSHOT即可(注意这里必须是大写)，如下： 

```xml
<groupId>cc.mzone</groupId>  
<artifactId>m1</artifactId>  
<version>0.1-SNAPSHOT</version>
<packaging>jar</packaging> 
```

maven的依赖管理是基于版本管理的，对于发布状态的artifact，如果版本号相同，即使我们内部的镜像服务器上的组件比本地新，maven也不会主动下载的。 

maven会根据模块的版本号(pom文件中的version)中是否带有-SNAPSHOT来判断是快照版本还是正式版本。如果是快照版本，那么在mvn deploy时会自动发布到快照版本库中，而使用快照版本的模块，在不更改版本号的情况下，直接编译打包时，maven会自动从镜像服务器上下载最新的快照版本。如果是正式发布版本，那么在mvn deploy时会自动发布到正式版本库中，而使用正式版本的模块，在不更改版本号的情况下，编译打包时如果本地已经存在该版本的模块则不会主动去镜像服务器上下载。 

所以，我们在开发阶段，可以将公用库的版本设置为快照版本，而被依赖组件则引用快照版本进行开发，在公用库的快照版本更新后，我们也不需要修改pom文件提示版本号来下载新的版本，直接mvn执行相关编译、打包命令即可重新下载最新的快照库了，从而也方便了我们进行开发。 

### distributionManagement 

> 定义了发布仓库的地址，应用snapshot和release库达到不同环境下发布不同的版本的目的 

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">  
    <modelVersion>4.0.0</modelVersion>  
    <groupId>net.aty.mybatis</groupId>  
    <artifactId>mybatis-demo</artifactId>  
    <packaging>jar</packaging>  
    <version>${project.release.version}</version>  
    <name>mybatis-demo</name>  
    <url>http://maven.apache.org</url>  

    <properties>  
        <project.release.version>0.1-SNAPSHOT</project.release.version>  
    </properties>  

    <profiles>  
        <profile>  
            <id>release</id>  
            <properties>  
                <project.release.version>0.1</project.release.version>  
            </properties>  
        </profile>  
    </profiles>  

    <!--定义snapshots库和releases库的nexus地址-->  
    <distributionManagement>  
        <repository>  
            <id>nexus-releases</id>  
            <url>  
                http://172.17.103.59:8081/nexus/content/repositories/releases/  
            </url>  
        </repository>  
        <snapshotRepository>  
            <id>nexus-snapshots</id>  
            <url>  
                http://172.17.103.59:8081/nexus/content/repositories/snapshots/  
            </url>  
        </snapshotRepository>  
    </distributionManagement>  
</project>  
```

首先我们看到pom文件中version的定义是采用占位符的形式，这样的好处是可以根据不同的profile来替换版本信息，比如maven默认是使用0.1-SNAPSHOT作为该模块的版本。

1、如果在发布时使用 mvn deploy -P release 的命令，那么会自动使用0.1作为发布版本，那么根据 maven 处理 snapshot 和 release 的规则，由于版本号后不带 -SNAPSHOT 故当成是正式发布版本，会被发布到 release 仓库；
2、如果发布时使用 mvn deploy 命令，那么就会使用默认的版本号 0.1-SNAPSHOT，此时 maven 会认为是快照版本，会自动发布到快照版本库。

[distributionManagement](https://www.cnblogs.com/liu2-/p/9035181.html)

### pluginRepositories

> 发现插件的远程仓库列表 

和`repository`类似，只是`repository`是管理jar包依赖的仓库，`pluginRepositories`则是管理插件的仓库。 maven插件是一种特殊类型的构件。由于这个原因，插件仓库独立于其它仓库。

```xml
<pluginRepositories>
    <!-- 包含需要连接到远程插件仓库的信息.参见profiles/profile/repositories/repository元素的说明 -->
    <pluginRepository>
        <releases>
            <enabled />
            <updatePolicy />
            <checksumPolicy />
        </releases>
        <snapshots>
            <enabled />
            <updatePolicy />
            <checksumPolicy />
        </snapshots>
        <id />
        <name />
        <url />
        <layout />
    </pluginRepository>
</pluginRepositories>
```

[maven全局配置文件settings.xml详解](https://www.cnblogs.com/jingmoxukong/p/6050172.html)

### 仓库和镜像

```xml
<!--中央仓库就是一个默认的远程仓库-->
<repositories>
    <repository>
        <id>central</id>
        <name>Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
        <layout>default</layout>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>

<pluginRepositories>
    <pluginRepository>
        <id>central</id>
        <name>Central Repository</name>
        <url>https://repo.maven.apache.org/maven2</url>
        <layout>default</layout>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <releases>
            <updatePolicy>never</updatePolicy>
        </releases>
    </pluginRepository>
</pluginRepositories>


<!--镜像仓库，mirrorOf 的值为 central，表示该配置为 id 为 central 仓库的镜像，也就是中央仓库的镜像。任何对中央仓库的请求都会转向到这个镜像，也可以用同样的方式配置其他仓库的镜像。-->
<settings>
    ...
    <mirrors>
        <mirror>
            <id>maven.net.cn</id>
            <name>中央仓库在中国的镜像</name>
            <url>http://maven.net.cn/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
        ...
    </mirrors>
    ...
</settings>
<!--id、name 和 url 表示镜像的唯一标记、名称和地址-->

```

### 示例文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <!--本地仓库。该值表示构建系统本地仓库的路径。其默认值为~/.m2/repository。 -->
    <localRepository>usr/local/maven</localRepository>

    <!--Maven是否需要和用户交互以获得输入。如果Maven需要和用户交互以获得输入，则设置成true，反之则应为false。默认为true。-->
    <interactiveMode>true</interactiveMode>

    <!--Maven是否需要使用plugin-registry.xml文件来管理插件版本。如果需要让Maven使用文件~/.m2/plugin-registry.xml来管理插件版本，则设为true。默认为false。-->
    <usePluginRegistry>false</usePluginRegistry>

    <!--表示Maven是否需要在离线模式下运行。如果构建系统需要在离线模式下运行，则为true，默认为false。当由于网络设置原因或者安全因素，构建服务器不能连接远程仓库的时候，该配置就十分有用。 -->
    <offline>false</offline>

    <!--当插件的组织Id（groupId）没有显式提供时，供搜寻插件组织Id（groupId）的列表。该元素包含一个pluginGroup元素列表，每个子元素包含了一个组织Id（groupId）。当我们使用某个插件，并且没有在命令行为其提供组织Id（groupId）的时候，Maven就会使用该列表。默认情况下该列表包含了org.apache.maven.plugins。 -->
    <pluginGroups>
        <!--plugin的组织Id（groupId） -->
        <pluginGroup>org.codehaus.mojo</pluginGroup>
    </pluginGroups>

    <!--用来配置不同的代理，多代理profiles 可以应对笔记本或移动设备的工作环境：通过简单的设置profile id就可以很容易的更换整个代理配置。 -->
    <proxies>
        <!--代理元素包含配置代理时需要的信息-->
        <proxy>
            <!--代理的唯一定义符，用来区分不同的代理元素。-->
            <id>myproxy</id>
            <!--该代理是否是激活的那个。true则激活代理。当我们声明了一组代理，而某个时候只需要激活一个代理的时候，该元素就可以派上用处。 -->
            <active>true</active>
            <!--代理的协议。 协议://主机名:端口，分隔成离散的元素以方便配置。-->
            <protocol>http</protocol>
            <!--代理的主机名。协议://主机名:端口，分隔成离散的元素以方便配置。  -->
            <host>proxy.somewhere.com</host>
            <!--代理的端口。协议://主机名:端口，分隔成离散的元素以方便配置。 -->
            <port>8080</port>
            <!--代理的用户名，用户名和密码表示代理服务器认证的登录名和密码。 -->
            <username>proxyuser</username>
            <!--代理的密码，用户名和密码表示代理服务器认证的登录名和密码。 -->
            <password>somepassword</password>
            <!--不该被代理的主机名列表。该列表的分隔符由代理服务器指定；例子中使用了竖线分隔符，使用逗号分隔也很常见。-->
            <nonProxyHosts>*.google.com|ibiblio.org</nonProxyHosts>
        </proxy>
    </proxies>

    <!--配置服务端的一些设置。一些设置如安全证书不应该和pom.xml一起分发。这种类型的信息应该存在于构建服务器上的settings.xml文件中。-->
    <servers>
        <!--服务器元素包含配置服务器时需要的信息 -->
        <server>
            <!--这是server的id（注意不是用户登陆的id），该id与distributionManagement中repository元素的id相匹配。-->
            <id>server001</id>
            <!--鉴权用户名。鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码。 -->
            <username>my_login</username>
            <!--鉴权密码 。鉴权用户名和鉴权密码表示服务器认证所需要的登录名和密码。 -->
            <password>my_password</password>
            <!--鉴权时使用的私钥位置。和前两个元素类似，私钥位置和私钥密码指定了一个私钥的路径（默认是/home/hudson/.ssh/id_dsa）以及如果需要的话，一个密语。将来passphrase和password元素可能会被提取到外部，但目前它们必须在settings.xml文件以纯文本的形式声明。 -->
            <privateKey>${usr.home}/.ssh/id_dsa</privateKey>
            <!--鉴权时使用的私钥密码。-->
            <passphrase>some_passphrase</passphrase>
            <!--文件被创建时的权限。如果在部署的时候会创建一个仓库文件或者目录，这时候就可以使用权限（permission）。这两个元素合法的值是一个三位数字，其对应了unix文件系统的权限，如664，或者775。 -->
            <filePermissions>664</filePermissions>
            <!--目录被创建时的权限。 -->
            <directoryPermissions>775</directoryPermissions>
            <!--传输层额外的配置项 -->
            <configuration></configuration>
        </server>
    </servers>

    <!--为仓库列表配置的下载镜像列表。 -->
    <mirrors>
        <!--给定仓库的下载镜像。 -->
        <mirror>
            <!--该镜像的唯一标识符。id用来区分不同的mirror元素。 -->
            <id>planetmirror.com</id>
            <!--镜像名称 -->
            <name>PlanetMirror Australia</name>
            <!--该镜像的URL。构建系统会优先考虑使用该URL，而非使用默认的服务器URL。 -->
            <url>http://downloads.planetmirror.com/pub/maven2</url>
            <!--被镜像的服务器的id。例如，如果我们要设置了一个Maven中央仓库（http://repo1.maven.org/maven2）的镜像，就需要将该元素设置成central。这必须和中央仓库的id central完全一致。-->
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>


    <!--根据环境参数来调整构建配置的列表。settings.xml中的profile元素是pom.xml中profile元素的裁剪版本。它包含了id，activation, repositories, pluginRepositories和 properties元素。这里的profile元素只包含这五个子元素是因为这里只关心构建系统这个整体（这正是settings.xml文件的角色定位），而非单独的项目对象模型设置。如果一个settings中的profile被激活，它的值会覆盖任何其它定义在POM中或者profile.xml中的带有相同id的profile。 -->
    <profiles>

        <!--根据环境参数来调整的构件的配置-->
        <profile>

            <!--该配置的唯一标识符。 -->
            <id>test</id>

            <!--自动触发profile的条件逻辑。Activation是profile的开启钥匙。如POM中的profile一样，profile的力量来自于它能够在某些特定的环境中自动使用某些特定的值；这些环境通过activation元素指定。activation元素并不是激活profile的唯一方式。settings.xml文件中的activeProfile元素可以包含profile的id。profile也可以通过在命令行，使用-P标记和逗号分隔的列表来显式的激活（如，-P test）。-->
            <activation>
                <!--profile默认是否激活的标识-->
                <activeByDefault>false</activeByDefault>
                <!--当匹配的jdk被检测到，profile被激活。例如，1.4激活JDK1.4，1.4.0_2，而!1.4激活所有版本不是以1.4开头的JDK。-->
                <jdk>1.5</jdk>
                <!--当匹配的操作系统属性被检测到，profile被激活。os元素可以定义一些操作系统相关的属性。-->
                <os>
                    <!--激活profile的操作系统的名字 -->
                    <name>Windows XP</name>
                    <!--激活profile的操作系统所属家族(如 'windows')  -->
                    <family>Windows</family>
                    <!--激活profile的操作系统体系结构  -->
                    <arch>x86</arch>
                    <!--激活profile的操作系统版本-->
                    <version>5.1.2600</version>
                </os>
                <!--如果Maven检测到某一个属性（其值可以在POM中通过${名称}引用），其拥有对应的名称和值，Profile就会被激活。如果值字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段-->
                <property>
                    <!--激活profile的属性的名称-->
                    <name>mavenVersion</name>
                    <!--激活profile的属性的值 -->
                    <value>2.0.3</value>
                </property>
                <!--提供一个文件名，通过检测该文件的存在或不存在来激活profile。missing检查文件是否存在，如果不存在则激活profile。另一方面，exists则会检查文件是否存在，如果存在则激活profile。-->
                <file>
                    <!--如果指定的文件存在，则激活profile。 -->
                    <exists>/usr/local/hudson/hudson-home/jobs/maven-guide-zh-to-production/workspace/</exists>
                    <!--如果指定的文件不存在，则激活profile。-->
                    <missing>/usr/local/hudson/hudson-home/jobs/maven-guide-zh-to-production/workspace/</missing>
                </file>
            </activation>

            <!--对应profile的扩展属性列表。Maven属性和Ant中的属性一样，可以用来存放一些值。这些值可以在POM中的任何地方使用标记${X}来使用，这里X是指属性的名称。属性有五种不同的形式，并且都能在settings.xml文件中访问。
            1. env.X: 在一个变量前加上"env."的前缀，会返回一个shell环境变量。例如,"env.PATH"指代了$path环境变量（在Windows上是%PATH%）。
            2. project.x：指代了POM中对应的元素值。
            3. settings.x: 指代了settings.xml中对应元素的值。
            4. Java System Properties: 所有可通过java.lang.System.getProperties()访问的属性都能在POM中使用该形式访问，
             如/usr/lib/jvm/java-1.6.0-openjdk-1.6.0.0/jre。
            5. x: 在<properties/>元素中，或者外部文件中设置，以${someVar}的形式使用。 -->
            <properties>
                <user.install>/ebs1/build-machine/usr/local/hudson/hudson-home/jobs/maven-guide-</user.install>
            </properties>

            <!--远程仓库列表，它是Maven用来填充构建系统本地仓库所使用的一组远程项目。 -->
            <repositories>
                <!--包含需要连接到远程仓库的信息 -->
                <repository>
                    <!--远程仓库唯一标识-->
                    <id>codehausSnapshots</id>
                    <!--远程仓库名称 -->
                    <name>Codehaus Snapshots</name>
                    <!--如何处理远程仓库里发布版本的下载-->
                    <releases>
                        <!--true或者false表示该仓库是否为下载某种类型构件（发布版，快照版）开启。  -->
                        <enabled>false</enabled>
                        <!--该元素指定更新发生的频率。Maven会比较本地POM和远程POM的时间戳。这里的选项是：always（一直），daily（默认，每日），interval：X（这里X是以分钟为单位的时间间隔），或者never（从不）。 -->
                        <updatePolicy>always</updatePolicy>
                        <!--当Maven验证构件校验文件失败时该怎么做-ignore（忽略），fail（失败），或者warn（警告）。-->
                        <checksumPolicy>warn</checksumPolicy>
                    </releases>
                    <!--如何处理远程仓库里快照版本的下载。有了releases和snapshots这两组配置，POM就可以在每个单独的仓库中，为每种类型的构件采取不同的策略。例如，可能有人会决定只为开发目的开启对快照版本下载的支持。参见repositories/repository/releases元素-->
                    <snapshots>
                        <enabled/>
                        <updatePolicy/>
                        <checksumPolicy/>
                    </snapshots>
                    <!--远程仓库URL，按protocol://hostname/path形式 -->
                    <url>http://snapshots.maven.codehaus.org/maven2</url>
                    <!--用于定位和排序构件的仓库布局类型-可以是default（默认）或者legacy（遗留）。Maven 2为其仓库提供了一个默认的布局；然而，Maven 1.x有一种不同的布局。我们可以使用该元素指定布局是default（默认）还是legacy（遗留）。 -->
                    <layout>default</layout>
                </repository>
            </repositories>

            <!--发现插件的远程仓库列表。仓库是两种主要构件的家。第一种构件被用作其它构件的依赖。这是中央仓库中存储的大部分构件类型。另外一种构件类型是插件。Maven插件是一种特殊类型的构件。由于这个原因，插件仓库独立于其它仓库。pluginRepositories元素的结构和repositories元素的结构类似。每个pluginRepository元素指定一个Maven可以用来寻找新插件的远程地址。-->
            <pluginRepositories>
                <!--包含需要连接到远程插件仓库的信息.参见profiles/profile/repositories/repository元素的说明-->
                <pluginRepository>
                    <releases>
                        <enabled/>
                        <updatePolicy/>
                        <checksumPolicy/>
                    </releases>
                    <snapshots>
                        <enabled/>
                        <updatePolicy/>
                        <checksumPolicy/>
                    </snapshots>
                    <id/>
                    <name/>
                    <url/>
                    <layout/>
                </pluginRepository>
            </pluginRepositories>

            <!--手动激活profiles的列表，按照profile被应用的顺序定义activeProfile。 该元素包含了一组activeProfile元素，每个activeProfile都含有一个profile id。任何在activeProfile中定义的profile id，不论环境设置如何，其对应的
                    profile都会被激活。如果没有匹配的profile，则什么都不会发生。例如，env-test是一个activeProfile，则在pom.xml（或者profile.xml）中对应id的profile会被激活。如果运行过程中找不到这样一个profile，Maven则会像往常一样运行。 -->
            <activeProfiles>
                <!-- -->
                <activeProfile>env-test</activeProfile>
            </activeProfiles>

        </profile>
    </profiles>
</settings>
```

## Profiles

> 每个profile对应不同的激活条件和配置信息 ，多个profile可以实现不同环境使用不同配置信息。

### 定义信息

`settings.xml`中的`profile`元素是`pom.xml`中`profile`元素的**裁剪版本**。 

#### 定义在settings.xml中

当profile定义在settings.xml中时意味着该profile是全局的，它会对所有项目或者某一用户的所有项目都产生作用

能够定义在settings.xml中的profile信息有

`<repositories>`

`<pluginRepositories>`

`<properties>`

`activation  `

`id`

定义在`<properties>`里面的键值对可以在pom.xml中使用

#### 定义在pom.xml中

定义在pom.xml中的profile可以定义更多的信息。主要有以下这些：

`<repositories>`

`<pluginRepositories>`

`<dependencies>`

`<plugins>`

`<properties>`

`<dependencyManagement>`

`<distributionManagement>`

还有build元素下面的子元素，主要包括：

`<defaultGoal>`

`<resources>`

`<testResources>`

`<finalName>`

### 激活方式

```sh
#查看处于激活状态的profile
$ mvn help:active-profiles
```

#### -P命令参数

```sh
#激活profileTest
$ mvn package –P profileTest
```

```sh
#当我们使用activeByDefault或settings.xml中定义了处于激活的profile，但是当我们在进行某些操作的时候又不想它处于激活状态，这个时候我们可以这样做： 
$ mvn package –P !profileTest1
#假设profileTest1是在settings.xml中使用activeProfile标记的处于激活状态的profile，那么当我们使用“-P !profile”的时候就表示在当前操作中该profile将不处于激活状态。 
```

#### activation

```xml
<profiles>  
    <profile> 
        <activation>
            <!--profile默认是否激活的标识，当没有指定条件，然后指定activeByDefault为true的时候就表示当没有指定其他profile为激活状态时，该profile就默认会被激活。  -->
            <activeByDefault>false</activeByDefault>
            <!--当匹配的jdk被检测到，profile被激活。例如，1.4激活JDK1.4，1.4.0_2，而!1.4激活所有版本不是以1.4开头的JDK，[1.4,1.7)在jdk为1.4、1.5和1.6的时候激活   -->
            <jdk>1.5</jdk>
            <!--当匹配的操作系统属性被检测到，profile被激活。os元素可以定义一些操作系统相关的属性。 -->
            <os>
                <!--激活profile的操作系统的名字 -->
                <name>Windows XP</name>
                <!--激活profile的操作系统所属家族(如 'windows') -->
                <family>Windows</family>
                <!--激活profile的操作系统体系结构 -->
                <arch>x86</arch>
                <!--激活profile的操作系统版本 -->
                <version>5.1.2600</version>
            </os>
            <!--如果Maven检测到某一个属性（其值可以在POM中通过${name}引用），其拥有对应的name = value，Profile就会被激活。如果value字段是空的，那么存在属性名称字段就会激活profile，否则按区分大小写方式匹配属性值字段。当提供了系统属性mavenVersion，并且其值为2.0.3的时候激活,或者直接使用命令激活"mvn package –DmavenVersion=2.0.3" -->
            <property>
                <!--激活profile的属性的名称 -->
                <name>mavenVersion</name>
                <!--激活profile的属性的值 -->
                <value>2.0.3</value>
            </property>
            <!--提供一个文件名，通过检测该文件的存在或不存在来激活profile。missing检查文件是否存在，如果不存在则激活profile。另一方面，exists则会检查文件是否存在，如果存在则激活profile。 -->
            <file>
                <!--如果指定的文件存在，则激活profile。 -->
                <exists>${basedir}/file2.properties</exists>
                <!--如果指定的文件不存在，则激活profile。 -->
                <missing>${basedir}/file1.properties</missing>
            </file>
        </activation>
    </profile>  
</profiles> 
```

#### activeProfiles

> 可以在settings.xml中使用activeProfiles来指定需要激活的profile，这种方式激活的profile将所有情况下都处于激活状态。 

```xml
<profiles>  
    <profile>  
        <id>profileTest1</id>  
        <properties>  
            <hello>world</hello>  
        </properties>  
    </profile>  

    <profile>  
        <id>profileTest2</id>  
        <properties>  
            <hello>andy</hello>  
        </properties>  
    </profile>  
</profiles>

<activeProfiles>  
     <activeProfile>profileTest1</activeProfile>  
</activeProfiles>  
```

```xml
<!-- 在activeProfiles下同时定义了多个需要激活的profile -->
<activeProfiles>  
    <activeProfile>profileTest1</activeProfile>  
    <activeProfile>profileTest2</activeProfile>  
</activeProfiles>
<!-- 不是根据activeProfile定义的顺序，后面的覆盖前面的。而是根据profile定义的先后顺序来进行覆盖取值的，然后后面定义的会覆盖前面定义的 -->
```

[Maven简介（三）——profile介绍](https://www.iteye.com/blog/elim-1900568)

## Other

> 在使用maven命令的时候 添加忽略SSL证书校验。
>
> maven命令 -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true

```sh
$ mvn help:system -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.valid
```

