# path

//接收一个路径参数，返回一个URL对象或者文件流，表示的是name指向的资源(文件或文件夹)或者资源流；
URL this.getClass().getResource(name)					类加载
URL this.getClass().getClassLoader().getResource(name)	类加载器加载

InputStream this.getClass().getResourceAsStream(name)
InputStream this.getClass().getClassLoader().getResourceAsStream(name)

//类加载的绝对路径和类加载器加载的相对路径保持一致，都是文件编译之后的根目录classes/，也就是类路径；类加载底层都是类加载器加载，class.getResource最终调用是ClassLoader.getResource，只是在这之前对参数进行了调整，如果类加载路径以"/"开头，则去除"/"；若无"/"，则把当前类的包名加在参数的前面



【类加载】
路径以"/"开头，表示绝对路径，指的是文件编译之后的根目录
路径不以"/"开头，表示相对路径，指的是文件所在的底层目录
this.getClass().getResource("/")
this.getClass().getResource("/").getPath()
this.getClass().getResource("").getPath()

输出:
file:/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/com/iot/application/device/basics/remotepro/service/


【类加载器加载】
路径不能以"/"开头，相对路径，指的是文件编译之后的根目录
this.getClass().getClassLoader().getResource("").getPath()
Thread.currentThread().getContextClassLoader().getResource("").getPath()

输出：
/E:/zhdg-iot/zhdg-device/target/classes/
/E:/zhdg-iot/zhdg-device/target/classes/

【servlet加载】
//servlet默认从web应用根目录下取资源，Tomcat下path是否以"/"开头无所谓
URL servletContext.getResource(name)
InputStream servletContext.getResourceAsStream(name)
Set<String> servletContext.getResourcePaths(name);		//获取指定目录下的所有资源路径,如：
		 Set<String> paths=servletContext.getResourcePaths("/WEB-INF");//参数一定要以”/”开头，否则会报错
		 输出：/WEB-INF/lib,/WEB-INF/classes,/WEB-INF/web.xml


【Resource工具类加载】
ResourceUtils.getURL("classpath:").getPath()

输出：
/E:/zhdg-iot/zhdg-device/target/classes/


【file加载】
new File("").getCanonicalPath()
new File("").getAbsolutePath()				//项目运行目录(tomcat中的bin目录，普通Java项目IDEA中的run configuration中可以配置)
new ClassPathResource("").getFile().getAbsolutePath()

输出：
E:\zhdg-iot\zhdg-device
E:\zhdg-iot\zhdg-device
E:\zhdg-iot\zhdg-device\target\classes

【系统】
System.getProperty("user.dir")

输出：
E:\zhdg-iot\zhdg-device

【所有路径】
System.getProperty("java.class.path")	//获取所有类路径
System.getProperty("java.library.path")	//获取所有库路径