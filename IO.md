# IO

### 流分类

![5cf198bced5b9237097aacb1c96a62de.png](img_IO/5cf198bced5b9237097aacb1c96a62de.png)



DataInputStream 类，支持按照基本数据类型（int、boolean、long 等）来读取数据。

### 装饰器模式应用

```java
public class BufferedInputStream extends InputStream {
    protected volatile InputStream in;
    protected BufferedInputStream(InputStream in) {
        this.in = in;
    }
    // f()函数不需要增强，只是重新调用一下InputStream in对象的f()
    public void f() {
        in.f();
    }
}
```

BufferedInputStream、DataInputStream继承自 FilterInputStream，BufferedInputStream类本质是功能增强，需要将最终读取数据的任务，委托给传递进来的 InputStream 对象来完成。

如上所示，InputStream 是一个抽象类而非接口，而且它的大部分函数（比如 read()、available()）都有默认实现，但是为了实现委托，每次对不需要增加缓存功能的函数也要重新调用。

为了避免代码重复，Java IO 抽象出了一个装饰器父类 FilterInputStream，InputStream 的所有的装饰器类（BufferedInputStream、DataInputStream）都继承自这个装饰器父类。这样，装饰器类只需要实现它需要增强的方法就可以了，其他方法继承装饰器父类的默认实现。







### ByteArrayOutputStream

字节数组输出流，低级流，避开了序列化和反序列化，不用跟硬盘交互，直接内存操作，节省了资源和缩短了响应时间，ByteArrayOutputStream 中的数据被写入一个 byte 数组。缓冲区会随着数据的不断写入而自动增长。

```java
ByteArrayOutputStream baos = new ByteArrayOutputStream();
// 依次写入“A”、“B”、“C”三个字母。0x41对应A，0x42对应B，0x43对应C。
baos.write(0x41);
baos.write(0x42);
baos.write(0x43);

// 将ArrayLetters数组中从下标“3”开始的后5个字节写入到baos中。
byte[] ArrayLetters;
baos.write(ArrayLetters, 3, 5);

// 转换成byte[]数组
byte[] buf = baos.toByteArray();
String str = new String(buf);

// 将baos写入到另一个输出流中
ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
baos.writeTo((OutputStream)baos2);
```























