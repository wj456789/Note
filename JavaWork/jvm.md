### Java破坏双亲委派实现自定义加载器加载不同版本类

```java
/**
 *
 * 为避免jar冲突，比如hbase可能有多个版本的读写依赖jar包
 * 就需要脱离当前classLoader去加载这些jar包，执行完成后，又退回到原来classLoader上继续执行接下来的代码
 */
public final class ClassLoaderSwapper {
    private ClassLoader storeClassLoader = null;

    private ClassLoaderSwapper() {
    }

    public static ClassLoaderSwapper newCurrentThreadClassLoaderSwapper() {
        return new ClassLoaderSwapper();
    }

    /**
     * 保存当前classLoader，并将当前线程的classLoader设置为所给classLoader
     *
     * @param
     * @return
     */
    public ClassLoader setCurrentThreadClassLoader(ClassLoader classLoader) {
        this.storeClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        return this.storeClassLoader;
    }

    /**
     * 将当前线程的类加载器设置为保存的类加载
     * @return
     */
    public ClassLoader restoreCurrentThreadClassLoader() {
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.storeClassLoader);
        return classLoader;
    }
}

```

```java
/**
 * 提供Jar隔离的加载机制，会把传入的路径、及其子路径、以及路径中的jar文件加入到class path。
 * 破坏双亲委派机制，改为逆向
 * */
public class JarLoader extends URLClassLoader {
    private static ThreadLocal<URL[]> threadLocal = new ThreadLocal<>();
    private URL[] allUrl;
    public JarLoader(String[] paths) {
        this(paths, JarLoader.class.getClassLoader());
    }

    public JarLoader(String[] paths, ClassLoader parent) {
        super(getURLs(paths), parent);
        //暂时先这样
        allUrl = threadLocal.get();
    }

    private static URL[] getURLs(String[] paths) {
        if (null == paths || 0 == paths.length) {
            throw new RuntimeException("jar包路径不能为空.");
        }

        List<String> dirs = new ArrayList<String>();
        for (String path : paths) {
            dirs.add(path);
            JarLoader.collectDirs(path, dirs);
        }

        List<URL> urls = new ArrayList<URL>();
        for (String path : dirs) {
            urls.addAll(doGetURLs(path));
        }
        URL[] urls1 = urls.toArray(new URL[0]);
        threadLocal.set(urls1);
        return urls1;
    }

    private static void collectDirs(String path, List<String> collector) {
        if (null == path || "".equalsIgnoreCase(path)) {
            return;
        }

        File current = new File(path);
        if (!current.exists() || !current.isDirectory()) {
            return;
        }

        for (File child : current.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }

            collector.add(child.getAbsolutePath());
            collectDirs(child.getAbsolutePath(), collector);
        }
    }

    private static List<URL> doGetURLs(final String path) {
        if (null == path || "".equalsIgnoreCase(path)) {
            throw new RuntimeException("jar包路径不能为空.");
        }
        File jarPath = new File(path);

        if (!jarPath.exists() || !jarPath.isDirectory()) {
            throw new RuntimeException("jar包路径必须存在且为目录.");
        }

		/* set filter */
        FileFilter jarFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        };

		/* iterate all jar */
        File[] allJars = new File(path).listFiles(jarFilter);
        List<URL> jarURLs = new ArrayList<URL>(allJars.length);

        for (int i = 0; i < allJars.length; i++) {
            try {
                jarURLs.add(allJars[i].toURI().toURL());
            } catch (Exception e) {
                throw new RuntimeException("系统加载jar包出错", e);
            }
        }
        return jarURLs;
    }
    //破坏双亲委派模型,采用逆向双亲委派
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (allUrl != null) {
            String classPath = name.replace(".", "/");
            classPath = classPath.concat(".class");
            for (URL url : allUrl) {

                byte[] data = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                InputStream is = null;
                try {
                    File file = new File(url.toURI());
                    if (file != null && file.exists()) {
                        JarFile jarFile = new JarFile(file);
                        if (jarFile != null) {
                            JarEntry jarEntry = jarFile.getJarEntry(classPath);
                            if (jarEntry != null) {
                                is = jarFile.getInputStream(jarEntry);
                                int c = 0;
                                while (-1 != (c = is.read())) {
                                    baos.write(c);
                                }
                                data = baos.toByteArray();
                                return this.defineClass(name, data, 0, data.length);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return super.loadClass(name);
    }


}

```

```java
@RunWith(JUnit4.class)
public class JarClassloaderTests {
    String jar1_3 = "C:\\Users\\Administrator\\.gradle\\caches\\modules-2\\files-2.1\\com.iscas\\base\\1.3-RELEASE\\f068847d9148a666f6c4c74cd4c8cec6ee41fda5";
    String jar1_4 = "C:\\Users\\Administrator\\.gradle\\caches\\modules-2\\files-2.1\\com.iscas\\base\\1.4-RELEASE\\9204c31cae18a2b4aef592e2bd7805d133da71a6";
    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        System.out.println("=========================开始第一次测试，读取指定jar包1.3版本:=========================");
        JarLoader jarLoader = new JarLoader(new String[]{jar1_3});
        ClassLoaderSwapper classLoaderSwapper = ClassLoaderSwapper.newCurrentThreadClassLoaderSwapper();
        classLoaderSwapper.setCurrentThreadClassLoader(jarLoader);
        Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("com.iscas.test.base.StringUtils");
        classLoaderSwapper.restoreCurrentThreadClassLoader();
        Object o = aClass.newInstance();
        Method isEmptyMethod = aClass.getDeclaredMethod("isEmpty", String.class);
        Object invoke = isEmptyMethod.invoke(o, "ewe");


        System.out.println("=========================开始第二次测试，读取指定jar包1.4版本:=========================");
        JarLoader jarLoader2 = new JarLoader(new String[]{jar1_4});
        classLoaderSwapper.setCurrentThreadClassLoader(jarLoader2);
        Class<?> aClass2 = Thread.currentThread().getContextClassLoader().loadClass("com.iscas.test.base.StringUtils");
        classLoaderSwapper.restoreCurrentThreadClassLoader();
        Object o2 = aClass2.newInstance();
        Method isEmptyMethod2 = aClass2.getDeclaredMethod("isEmpty", String.class);
        Object invoke2 = isEmptyMethod2.invoke(o2, "ewe");
    }
}

```

