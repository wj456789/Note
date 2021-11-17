# IDEA

短代码不设为一行		"code style"-->Java-->Code Generation-->Line comment at first column
软换行			"General"-->Soft Wraps
去掉never used提示		"inspections"-->"Unused declaration"
去掉Could not autowired提示	"inspections"-->"Autowiring for Bean Class"
打开多个文件显示在多行tab上	"Editor tabs"-->Show tabs in one row
修改显示tabs数上限值	"Editor tabs"-->Tab Closing Policy
自动生成serialVersionUID	"Inspections"-->"serialVersionUID"-->Serializable class without serialVersionUID
调整Searcheverywhere对话框	Ctrl+Shift+A输入registry-->找到ide.suppress.double.click.handler打上勾-->设置快捷键
复制一行向下		"Duplicate Line or Selection"
删除一行			"DeleteLine"
取消代码一行显示		"Code Folding"-->"One-line methods"
terminal设置shell		"terminal"-->"Application settings"-->"Shell path"-->"D:\Git\bin\bash.exe"
svn忽略文件		"File Types"-->在下方 Ignore files and folders 追加如下 表达式
				*.iml;*.idea;*.gitignore;*.sh;*.classpath;*.project;*.settings;target;

Alt+Enter		错误提示
ctrl+2		快捷引用
Alt+Shift+上/下	move line down/up
ctrl+Y		复制一行向下
ctrl+D		删除一行
点选类+ctrl+H	查看当前类继承关系
Ctrl+Shift+Enter	换行
Ctrl+F12		查找类中所有方法
Ctrl+N		按名字搜索类
Ctrl+Alt+L	对代码进行格式化

pom中提取版本		Refactor-->Extract-->property
包名格式			点击左上角设置图标，Compact Middle package
定位文件在左侧文件夹的位置	打开当前页面，在左上方的雷达位置上点一下，就能精确定位到文件的具体位置

idea会安装Language level指定的jdk版本来对我们的代码进行编译，以及错误检查。project Structure中有两个地方可以设置，真正生效的地方实在module中

IDEA的配置文件project.iml,这些参数可以在project structure中配置
<component name="NewModuleRootManager" LANGUAGE_LEVEL="JDK_1_8">
    <output url="file://$MODULE_DIR$/target/classes" />
    <output-test url="file://$MODULE_DIR$/target/test-classes" />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      //添加java-resource资源文件所在目录
      <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />
      <sourceFolder url="file://$MODULE_DIR$/src/test/java" isTestSource="true" />
      <excludeFolder url="file://$MODULE_DIR$/target" />
    </content>	