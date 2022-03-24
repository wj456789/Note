## Ant

> ant 是一个将软件编译、测试、部署等步骤联系在一起加以自动化的一个工具，大多用于Java环境中的软件开发。

- Ant是由一个内置任务和可选任务组成的，用ant任务就像是在dos中写命令行一样。
- Ant 运行时需要一个 build.xml 文件(构建文件)。 Ant通过调用 target 树，就可以执行各种 task。

### 运行

**安装JDK，下载解压Ant程序压缩包之后，在环境变量中配置ANT_HOME ，在cmd中运行ant程序 ，任务执行**

```sh
#在当前目录下的build-test.xml运行Ant，执行一个叫做clean的target
> ant -lib helloworld.jar -buildfile build-test.xml clean
```

- `-lib` 指定依赖jar包
- `-buildfile filename ` 指定Ant 需要处理的构建文件。默认的构建文件为build.xml
- `-find filename`  指定Ant 应当处理的构建文件。与-buildfile 选项不同，如果所指定文件在当前目录中未找到，-find 就要求Ant 在其父目录中再进行搜索。这种搜索会继续在其祖先目录中进行，直至达到文件系统的根为止，在此如果文件还未找到，则构建失败。 

### build.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- name是当前工程的名称，default是默认执行的任务，basedir是工作目录（.代表当前根目录） -->
<project name="HelloWorld" default="run" basedir=".">
    <!-- property类似于程序中定义简单的变量 -->
    <property name="src" value="src"/>
    <property name="dest" value="classes"/>
    <property name="hello_jar" value="helloWorld.jar"/>
    <!-- 
    target是一个事件、事情、任务， name是当前事情的名称，depends是依赖的上一件或是多件事情
    如果所依赖的事情没有执行，ant会先运行依赖事情，然后再运行当前事情
    -->
    
    <!-- 初始化 -->
    <target name="init">
        <!-- 建立classes目录 -->
        <mkdir dir="${dest}"/>
        <mkdir dir="temp"/>
        <mkdir dir="temp2"/>
    </target>
    
    <!-- 编译 -->
    <target name="compile" depends="init">
        <javac srcdir="${src}" destdir="${dest}"/>
        <!-- 设置jvm内存
        <javac srcdir="src" fork="true"/> 
        <javac srcdir="src" fork="true" executable="d:\sdk141\bin\javac" 
        memoryMaximumSize="128m"/> 
        -->
    </target>
    
    <!-- 建立jar包 -->
    <target name="build" depends="compile">
        <!-- 
        <jar jarfile="${hello_jar}" basedir="${dest}"/>
        创建一个名称是package.jar文件
        <jar destfile="package.jar" basedir="classes"/> 
        -->
        <jar destfile="${hello_jar}" basedir="classes"> 
            <!-- 向jar包中的main文件中添加内容 -->
            <manifest> 
                <attribute name="Built-By" value="${user.name}"/> 
                <attribute name="Main-class" value="package.Main"/> 
            </manifest> 
        </jar> 
        <!-- 复制jar文件  todir="复制到目录"-->
        <copy file="${hello_jar}" tofile="${dest}\temp.jar"/> 
        <copy todir="temp"> 
            <!-- 不按照默认方式 defaultexcludes="" -->
              <fileset dir="src"> 
                <include name="**/*.java"/>
              </fileset> 
        </copy> 
        
        <copy todir="temp2"> 
            <fileset dir="src">
                <and>
                    <contains text="main"/> 
                    <size value="1" when="more"/> 
                </and>
            </fileset>
        </copy> 
        
        <!-- 移动jar文件 -->
        <move file="${dest}\temp.jar" tofile="temp\move-temp.jar"/> 
        <!-- 创建zip -->
        <zip basedir="${basedir}\classes" zipfile="temp\output.zip"/> 
        <!-- 创建tgz -->
        <gzip src="classes\**\*.class" zipfile="output.class.gz"/>
        <!-- 解压zip -->
        <unzip src="output.class.gz" dest="extractDir"/> 
        <!--替换input.txt内容中的old为new
        <replace file="input.txt" token="old" value="new"/>
        --> 
    </target>
    
    <!-- 运行 -->
    <target name="run" depends="build">
        <java classname="com.hoo.test.HelloWorld" classpath="${hello_jar}"/>
    </target>
    
    <!-- 清除 -->
    <target name="clean">
        <!-- 删除生成的文件 -->
        <delete dir="${dest}"/>
        <delete file="${hello_jar}"/>
    </target>
    
    <tstamp> 
       <format property="OFFSET_TIME" 
               pattern="HH:mm:ss" 
               offset="10" unit="minute"/> 
    </tstamp>
    
    <!-- 重新运行 -->
    <target name="rerun" depends="clean,run">
        <echo message="###${TSTAMP}#${TODAY}#${DSTAMP}###"/>
        <aunt target="clean"/>
        <aunt target="run"/>
    </target>
</project>
```

#### project 节点元素

> project 元素是 Ant 构件文件的根元素， Ant 构件文件至少应该包含一个 project 元素，否则会发生错误。在每个 project 元素下，可包含多个 target 元素。

- name 属性：用于指定 project 元素的名称。 
- default 属性：用于指定 project 默认执行时所执行的 target 的名称。 
- basedir 属性：用于指定基路径的位置。该属性没有指定时，使用 Ant 的构件文件的附目录作为基准目录。

```xml
<?xml version="1.0" ?>
<project name="ant-project" default="print-dir" basedir=".">
    <target name="print-dir">
        <echo message="The base dir is: ${basedir}" />
    </target>
</project>
```

#### target节点元素

> target为ant的基本执行单元或是任务，它可以包含一个或多个具体的单元/任务。多个target 可以存在相互依赖关系。它有如下属性： 

- name 属性：指定 target 元素的名称，这个属性在一个 project 元素中是唯一的。我们可以通过指定 target 元素的名称来指定某个 target 。 
- depends 属性：用于描述 target 之间的依赖关系，若与多个 target 存在依赖关系时，需要以“,”间隔。 Ant 会依照 depends 属性中 target 出现的顺序依次执行每个 target ，被依赖的target 会先执行。
- if 属性：用于验证指定的属性是存在，若不存在，所在 target 将不会被执行。
- unless 属性：该属性的功能与 if 属性的功能正好相反，它也用于验证指定的属性是否存在，若不存在，所在 target 将会被执行。
- description 属性：该属性是关于 target 功能的简短描述和说明。  

```xml
<?xml version="1.0" ?>
<project name="ant-target" default="print">
    <target name="version" if="ant.java.version">
        <echo message="Java Version: ${ant.java.version}" />
    </target>
    <target name="print" depends="version" unless="docs">
        <description>
            a depend example!
        </description>
        <echo message="The base dir is: ${basedir}" />
    </target>
</project>

输出信息：
"[echo] Java Version: 1.6 "
"[echo] The base dir is:D:\Workspace\AntExample\build"。
```

#### property属性节点元素

> property元素可看作参量或者参数的定义，project 的属性可以通过 property 元素来设定，也可在 Ant 之外设定。 

若要在外部引入某文件，例如 build.properties 文件，可以通过如下内容将其引：`  <property file="build.properties"/>`  

property 元素可用作 task 的属性值。在 task 中是通过将属性名放在${属性名}之间，并放在 task 属性值的位置来实现的。 

 Ant 提供了一些内置的属性，它能得到的系统属性的列表与 Java 文档中 System.getProperties() 方法得到的属性一致 

- `basedir`： project 基目录的绝对路径；   
- `ant.file`： buildfile的绝对路径，上例中ant.file值为`D:\Workspace\AntExample\build`；  
- `ant.version`： Ant的版本信息，本文为1.8.1 ；  
- `ant.project.name`： 当前指定的project的名字，即前文说到的project的name属性值；  
- `ant.java.version`： Ant 检测到的JDK版本，本文为 1.6 。 

```xml
<project name="ant-project" default="example">
    <property name="name" value="jojo" />
    <property name="age" value="25" />
    <target name="example">
        <echo message="name: ${name}, age: ${age}" />
    </target>
</project>
```

#### arg 数据参数元素

> 由Ant构建文件调用的程序，可以通过`<arg>`元素向其传递命令行参数，如apply、exec、java任务均可接受嵌套`<arg>`元素，可以为各自的过程调用指定参数。 

- values 是一个命令参数。如果参数中有空格，但又想将它作为单独一个值，则使用此属性。
- file 表示一个参数的文件名。在构建文件中，此文件名相对于当前的工作目录
- line 表示用空格分隔的多个参数列表。
- path 表示路径
- pathref 引用的path（使用path元素节点定义path）的id  
- prefix 前缀
- suffix 后缀 

```xml
<!-- 是一个含有空格的单个的命令行变量 -->
<arg value="-l -a"/> 

<!-- 是两个用空格分隔的命令行变量 -->
<arg line="-l -a"/>
```

#### fileset 文件类型

> fileset 数据类型定义了一组文件，并通常表示为`<fileset>`元素。不过，许多ant任务构建成了隐式的fileset，这说明他们支持所有的fileset属性和嵌套元素。以下为fileset 的属性列表。  

- dir表示fileset 的基目录。
- casesensitive的值如果为false，那么匹配文件名时，fileset不是区分大小写的，其默认值为true。
- defaultexcludes 用来确定是否使用默认的排除模式，默认为true。
- excludes 是用逗号分隔的需要派出的文件模式列表。
- excludesfile 表示每行包含一个排除模式的文件的文件名。
- includes 是用逗号分隔的，需要包含的文件模式列表。
- includesfile 表示每行包括一个包含模式的文件名。 

```xml
<fileset id="lib.runtime" dir="${lib.path}/runtime">
    <include name="**/*.jar"/>
    <include name="**/*.so"/>
    <include name="**/*.dll"/>
</fileset>
```

参考：

[Apache Ant的使用——基础使用教程](  https://blog.csdn.net/qq_33360240/article/details/83028728)

### maven比较

- Ant是一种基于Java的build工具，Maven除了以程序构建能力之外，还提供高级项目管理工具。

- Ant通过build.xml构建任务，实现build过程，它提供了非常多能够重用的task，比如 copy, move, delete以及junit单元测试，但是Ant所提供的可重用的task粒度太小，开发一个新的项目很难复用原先的build.xml。

- Maven提供一个标准的开发构架用来对多个项目进行管理，通过写plugin的方式实现build过程的复用，如compile, test, install ，也可以实现繁琐的路径配置信息，以及复杂的第三方库下载设置。

参考：

[MAVEN和ANT的差别](https://www.cnblogs.com/liguangsunls/p/6984243.html)



