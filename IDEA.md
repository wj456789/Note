# IDEA

## 配置

- 自动删除无用的包引用			Editor -> General -> Auto Import -> 勾选Add unambiguous imports on the fly以及Optimize imports on the fly

  - Add unambiguous imports on the fly：快速添加明确的导入。
  - Optimize imports on the fly：快速优化导入，优化的意思即自动帮助删除无用的导入。

- 短代码不设为一行		"code style"-->Java-->Code Generation-->Line comment at first column

- 软换行			"General"-->Soft Wraps

- 去掉never used提示		"inspections"-->"Unused declaration"

- 去掉Could not autowired提示	"inspections"-->"Autowiring for Bean Class"-->Severity的级别从Error改成Warning

- 打开多个文件显示在多行tab上	"Editor tabs"-->Show tabs in one row

- 修改显示tabs数上限值	"Editor tabs"-->Tab Closing Policy

- 自动生成serialVersionUID	"Inspections"-->"serialVersionUID"-->Serializable class without serialVersionUID

- 调整Searcheverywhere对话框	Ctrl+Shift+A输入registry-->找到ide.suppress.double.click.handler打上勾-->设置快捷键

- 复制一行向下		"Duplicate Line or Selection"

- 删除一行			"DeleteLine"

- 取消代码一行显示		"Code Folding"-->"One-line methods"

- terminal设置shell		"terminal"-->"Application settings"-->"Shell path"-->"D:\Git\bin\bash.exe"

- svn忽略文件		"File Types"-->在下方 Ignore files and folders 追加如下 表达式
  				`*.iml;*.idea;*.gitignore;*.sh;*.classpath;*.project;*.settings;target;`
  
- IDEA自动导包        Editor-->general-->auto import-->勾选add unambiguous ...和Optimize imports on ...

- 创建代码模板         Editor->Live Templates

  ```java
  /**
   * $END$
   *
   * @author w30021900
   * @since $date$
   */
  ```

  - 预定义变量

    `$END$`预定义变量，表示模板结束后的光标位置 ；

    自定义`$date$`变量；

    使用Edit variables编辑模板变量，会和系统内置函数关联

    | Name | Expression         | Default value | Skip if defined |
    | ---- | ------------------ | ------------- | --------------- |
    | date | date("yyyy/MM/dd") |               | 选中            |

    





## 快捷键

- Alt+Enter		错误提示
- ctrl+2		快捷引用
- Alt+Shift+上/下	move line down/up
- ctrl+Y		复制一行向下
- ctrl+D		删除一行
- 点选类+ctrl+H	查看当前类继承关系
- Ctrl+Shift+Enter	换行
- Ctrl+F12		查找类中所有方法\
- Ctrl+N		按名字搜索类
- Ctrl+Alt+L	对代码进行格式化
- pom中提取版本		Refactor-->Extract-->property
- 包名格式			点击左上角设置图标，Compact Middle package
- 定位文件在左侧文件夹的位置	打开当前页面，在左上方的雷达位置上点一下，就能精确定位到文件的具体位置
- ctrl+shift+t 切换测试方法
- ctrl+alt+m 提取方法
- ctrl+shift+alt+t 重构
- shift+f6 整体修改参数名称
- ctrl+alt+o 删除无用的包引用
- F2/Shift+F2 快速定位错误和警告

## 文件

idea会安装 Language level 指定的jdk版本来对我们的代码进行编译，以及错误检查。project Structure中有两个地方可以设置，真正生效的地方实在module中

```xml
<!-- IDEA的配置文件 project.iml ,这些参数可以在project structure中配置 -->
<component name="NewModuleRootManager" LANGUAGE_LEVEL="JDK_1_8">
    <output url="file://$MODULE_DIR$/target/classes" />
    <output-test url="file://$MODULE_DIR$/target/test-classes" />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      <!-- 添加java-resource资源文件所在目录 -->
      <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />
      <sourceFolder url="file://$MODULE_DIR$/src/test/java" isTestSource="true" />
      <excludeFolder url="file://$MODULE_DIR$/target" />
    </content>	
    ...
```



## 操作

### git

在仓库新建分支
IDEA-->页头git-->pull-->ctrl+F5更新分支-->页尾git-->选中remote新分支右键-->new branch from selected-->输入名称创建本地分支-->右键本地分支checkout切换分支

切换分支时
Smart Checkout会把当前分支改动过的内容与要到达的分支合并（到达分支也会添加上当前分支的内容，减少的内容将会提示合并选项）
Force Chexkout 会把当前分支的变动取消然后切换到想要到达的分支
如果切换分支前把改动提交就不会有提示切换选项





































