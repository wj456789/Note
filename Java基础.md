# Java基础

## 基本数据类型

| 数据类型 | 类型   | 存储空间 | 取值（包装类中MIN_VALUE和MAX_VALUE）              | 默认值    | 精度           |
| -------- | ------ | -------- | ------------------------------------------------- | --------- | -------------- |
| byte     | 整数型 | 1字节    | `-128(-2^7) ~ 127(2^7 - 1)`                       | 0         |                |
| short    | 整数型 | 2字节    | `-32768(-2^15) ~ 32767(2^15 – 1)`                 | 0         |                |
| int      | 整数型 | 4字节    | `-2,147,483,648(-2^31) ~ 2,147,483,647(2^31 – 1)` | 0         |                |
| long     | 整数型 | 8字节    | `(-2^63) ~ (2^63 – 1)`                            | 0L        |                |
| float    | 浮点型 | 4字节    | `-3.4*10^38 ~ 3.4*10^38`                          | 0.0f      | 23bit(6~7位)   |
| double   | 浮点型 | 8字节    | `-1.7*10^308 ~ 1.7*10^308`                        | 0.0d/0.0  | 52bit(15~16位) |
| char     | 字符型 | 2字节    | `0(\u0000) ~ 65535(\uffff)（Unicode）`            | 0(\u0000) |                |
| boolean  | 布尔型 | 1字节    | `true or false`                                   | false     |                |

Java的基本类型占用空间和系统位数无关，只和类型本身有关。引用类型虽然在规范中没有规定空间占用，但在大多数虚拟机里都以指针的方式实现，因此空间占用也和指针一致。`int i(64位系统)、int i(32位系统)、long[] arr(32位系统)`都占用4个字节

#### 类型转换

- 允许自动类型转换的标准是表数范围小的类型赋值给表数范围大的类型
- 强制类型转换可用于所有基础数值类型之间的转换，很多场合特指表数范围大的类型给表数范围小的类型赋值

##### 类型强转

```java
Object obj;
List<Integer> list = castList(obj,Integer.class);
//list强转
private static <T> List<T> castList(Object obj, Class<T> clazz) {
    List<T> result = new ArrayList<T>();
    if (obj instanceof List<?>) {
        for (Object o : (List<?>) obj) {
            result.add(clazz.cast(o));
        }
        return result;
    }
    return null;
}
```

```java
Object obj;
Map<String,Object> map = castHashMap(obj,String.class,Object.class);

/**
 * 防止出现强转警告
 *
 * @param obj 强转对象
 * @param clazz1 HashMap的key
 * @param clazz2 HashMap的value
 * @param <K> Key泛型
 * @param <V> Value泛型
 * @return 强转后对象
 */
private <K, V> HashMap<K, V> castHashMap(Object obj, Class<K> clazz1, Class<V> clazz2) {
    HashMap<K, V> result = new HashMap<K, V>();
    if (obj instanceof HashMap<?, ?>) {
        for (Object o : ((HashMap<?, ?>) obj).keySet()) {
            result.put(clazz1.cast(o), clazz2.cast(((HashMap<?, ?>) obj).get(o)));
        }
        return result;
    }
    return null;
}
```



#### 类型提升

当一个算数表达式包含多个基本类型的值时，整个算术表达式的数据类型将发生自动提升

- byte、char、short自动提升至int
- 整个表达式的数据类型自动提升至表达式中的最高数据类型

```java
//虽然short可以容纳char，但提升的最低类型为int
char c=0;
short s=0;
s=s+c;//编译错误
    
//即便表达式的操作数是同类型，只要有多个操作数就会发生提升
short s=0;
s=s+s;//编译错误

char c=10;
c+=c;//编译成功
```

#### byte

byte类型达到127，再加1会反转变为-128

#### char

```java
char c="t";//编译错误
char c='t';//编译成功
```

#### 浮点型

```java
//浮点数不能保证精度以外的数字大小，所以不能直接==比较，这里精度为小数点后一位
for (float f = 0.1f; f != 10.0f; f += 0.1f) {
    System.out.println(f);
}
8.599997
8.699997
8.799997
8.899998
8.999998
9.099998
9.199999
9.299999
9.4
9.5
9.6
9.700001
9.800001
9.900002
10.000002
10.100003
```

##### 精度误差epslon

Math.abs(x)<1e-6 其实相当于 x == 0

1e-6 (也就是0.000001)叫做 epslon ，用来抵消浮点运算中因为误差造成的相等无法判断的情况。它通常是一个非常小的数字（具体多小要看你的运算误差），只要两数差值小于 epslon 可以视为相等

```java
//反例1
float a = 1.0f - 0.9f;
float b = 0.9f - 0.8f;
System.out.println(a == b);
//反例2
Float x = Float.valueOf(a);
Float y = Float.valueOf(b);
System.out.println(x.equals(y));

//正例
//指定一个误差范围，两个浮点数的插值在此范围之内，则认为是相等的
float diff = 1e-6f;
System.out.println(Math.abs(a - b) < diff);

//使用 Bigdecimal 来定义值，在进行浮点数运算操作
BigDecimal b1 = new BigDecimal("1.0");
BigDecimal b2 = new BigDecimal("0.9");
BigDecimal b3 = new BigDecimal("0.8");
BigDecimal s1 = b1.subtract(b2);
BigDecimal s2 = b2.subtract(b3);
System.out.println(s1.equals(s2));

输出：
false
false
true
true
```























## Decimal

### BigDecimal

#### 类型转换

```java
//字符串转换为BigDecimal
BigDecimal a = new BigDecimal("5.33");
//BigDecimal转换为String
String str=a.toString();



//double转换为BigDecimal
Double b=1.23;

BigDecimal c = BigDecimal.valueOf(b);
System.out.println(c);	//1.23

BigDecimal d= new BigDecimal(b);
System.out.println(d);	//1.229999999999999982236431605997495353221893310546875

//BigDecimal转换为double
double e = c.doubleValue();
```

#### 四则运算

```java
BigDecimal num1 = new BigDecimal("100");  
BigDecimal num2 = new BigDecimal("50");
//加法
BigDecimal result1 = num1.add(num2);        
//减法 
BigDecimal result2 = num1.subtract(num2);
//乘法
BigDecimal result3 = num1.multiply(num2);      
//除法
BigDecimal result4 = num1.divide(num2);
```

[BigDecimal-1](https://blog.csdn.net/u014369799/article/details/50995874)

[BigDecimal-2](https://blog.csdn.net/BADAO_LIUMANG_QIZHI/article/details/84618023)




### DecimalFormat

```java
System.out.println(0.0000000000000001);		//1.0E-16	
System.out.println(11540*0.35);				//4038.9999999999995

BigDecimal value = new BigDecimal("1.10");
BigDecimal value1 = new BigDecimal("15687.12364");

//0对整数不足补0
//0对小数不足补0，多余切掉四舍五入
DecimalFormat fnum = new DecimalFormat("000.000");
System.out.println(fnum.format(value));				//001.100
System.out.println(fnum.format(value1));			//15687.124

//#对小数末尾去0，多余切掉四舍五入
DecimalFormat fnum1 = new DecimalFormat("###.###");
System.out.println(fnum1.format(value));			//1.1
System.out.println(fnum1.format(value1));			//15687.124
```

[DecimalFormat 中0和#的作用](https://blog.csdn.net/u013394527/article/details/78722095)









































## 三大特性

### 多态

**当超类对象引用变量引用子类对象时，被引用对象的类型而不是引用变量的类型决定了调用谁的成员方法，但是这个被调用的方法必须是在超类中定义过的，也就是说被子类覆盖的方法，但是它仍然要根据继承链中方法调用的优先级来确认方法，该优先级为：**

**this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)。**

```java
public class A {
    public String show(D obj) {
        return ("A and D");
    }

    public String show(A obj) {
        return ("A and A");
    } 

}

public class B extends A{
    public String show(B obj){
        return ("B and B");
    }
    
    public String show(A obj){
        return ("B and A");
    } 
}

public class C extends B{

}

public class D extends B{

}

public class Test {
    public static void main(String[] args) {
        A a1 = new A();
        A a2 = new B();
        B b = new B();
        C c = new C();
        D d = new D();
        
        System.out.println("1--" + a1.show(b));
        System.out.println("2--" + a1.show(c));
        System.out.println("3--" + a1.show(d));
        System.out.println("4--" + a2.show(b));
        System.out.println("5--" + a2.show(c));
        System.out.println("6--" + a2.show(d));
        System.out.println("7--" + b.show(b));
        System.out.println("8--" + b.show(c));
        System.out.println("9--" + b.show(d));      
    }
}

输出：
1--A and A
2--A and A
3--A and D
4--B and A
5--B and A
6--A and D
7--B and B
8--B and B
9--A and D
    
    需要先根据优先级确认调用方法，然后判断此方法是否被子类重写
    
    分析4，a2.show(b)，a2是A类型的引用变量，所以this就代表了A，a2.show(b),它在A类中找发现没有找到，于是到A的超类中找(super)，由于A没有超类（Object除外），所以跳到第三级，也就是this.show((super)O)，B的超类是A，所以(super)O为A，this同样是A，这里在A中找到了show(A obj)，同时由于a2是B类的一个引用且B类重写了show(A obj)，因此最终会调用子类B类的show(A obj)方法，结果也就是B and A。
    
    分析5，a2.show(c)，a2是A类型的引用变量，所以this就代表了A，a2.show(c),它在A类中找发现没有找到，于是到A的超类中找(super)，由于A没有超类（Object除外），所以跳到第三级，也就是this.show((super)O)，C的超类有B、A，所以(super)O为B、A，this同样是A，这里在A中找到了show(A obj)，同时由于a2是B类的一个引用且B类重写了show(A obj)，因此最终会调用子类B类的show(A obj)方法，结果也就是B and A。
```



## 运算符

多数运算符具有左结合性，单目运算符、三目运算符、赋值运算符具有右结合性。

| 优先级 | 运算符                                            | 结合性     |
| ------ | ------------------------------------------------- | ---------- |
| 1      | `()、[]、{}`                                      | 从左向右   |
| 2      | `!、+、-、~、++、--`                              | *从右向左* |
| 3      | `*、/、%`                                         | 从左向右   |
| 4      | `+、-`                                            | 从左向右   |
| 5      | `«、»、>>>`                                       | 从左向右   |
| 6      | `<、<=、>、>=、instanceof`                        | 从左向右   |
| 7      | `==、!=`                                          | 从左向右   |
| 8      | `&`                                               | 从左向右   |
| 9      | `^`                                               | 从左向右   |
| 10     | `|`                                               | 从左向右   |
| 11     | `&&`                                              | 从左向右   |
| 12     | `||`                                              | 从左向右   |
| 13     | `?:`                                              | *从右向左* |
| 14     | `=、+=、-=、*=、/=、&=、|=、^=、~=、«=、»=、>>>=` | *从右向左* |

逻辑运算符包括`&&、||、!`三种，部分教材里把`&、|`也归为逻辑运算，事实上是用位运算来代替逻辑运算，容易混淆概念。

## 回车换行符

System.getProperties("line.separator");获取换行符 



> line.separator（windows:"\r\n"；unix:"\n"）； 
>
> file.separator（windows:"\"；unix:"/"）； 
>
> path.separator（windows:";"；unix:":"）； 

Windows系统下，换行符中的"\r\n"：

\r 表示：回车符(ACSII：13 或0x0d)，就是我们常说的硬回车。

\n 表示：换行(ACSII：10 或0x0a)，就是我们常说的软回车。

\r 表示回车，仅表示完成，把光标回到行首

\n 表示换行，位置是当前光标位置的下一行的竖直位置。

\r\n 表示回车和换行，表示完成后，光标移动到下一行的行首。

按键盘上回车键时，会自动产生 \r\n.



Unix系统里，每行结尾只有“<换行>”，即“\n”；Windows系统里面，每行结尾是“<回车><换行>”，即“\r\n”；Mac系统里，每行结尾是“<回车>”。一个直接后果是，Unix/Mac系统下的文件在Windows里打开的话，所有文字会变成一行；而Windows里的文件在Unix/Mac下打开的话，在每行的结尾可能会多出一个^M符号。 





## 数据运算越界检查

> oldAcc + (newVal * scale)

### 先决条件检查

```java
public int safeAdd(int left, int right) {
    if (right > 0 ? left > Integer.MAX_VALUE - right
        : left < Integer.MIN_VALUE - right) {
        throw new ArithmeticException("Integer overflow");
    }
    return left + right;
}

//Java数据类型的合法取值范围是不对称的（最小值的绝对值比最大值大1），所以对最小值取负时，会导致溢出。
public int safeMultiply(int left, int right) {
    if (right > 0 ? left > Integer.MAX_VALUE / right
        || left < Integer.MIN_VALUE / right
        : (right < -1 ? left > Integer.MIN_VALUE / right 
           || left < Integer.MAX_VALUE / right
           : right == -1 && left == Integer.MIN_VALUE)) {
        throw new ArithmeticException("Integer overflow");
    }
    return left * right;
}
```

### 向上类型转换 

该方式对long类型不适用。 

```java
public static long intRangeCheck(long value) {
    // 向上类型转换
    if ((value < Integer.MIN_VALUE) || (value > Integer.MAX_VALUE)) {
        throw new ArithmeticException("Integer overflow");
    }
    return value;
}

public static int multAccum(int oldAcc, int newVal, int scale) {
    final long res = intRangeCheck(((long) oldAcc) +
                                   intRangeCheck((long) newVal * (long) scale));
    return (int) res; // Safe downcast
}
```

### BigInteger 

```java
private static final BigInteger bigMaxInt = BigInteger.valueOf(Integer.MAX_VALUE);
private static final BigInteger bigMinInt = BigInteger.valueOf(Integer.MIN_VALUE);
// BigInteger检查是否存在溢出
public static BigInteger intRangeCheck(BigInteger val) {
    if (val.compareTo(bigMaxInt) == 1 || val.compareTo(bigMinInt) == -1) {
        throw new ArithmeticException("Integer overflow");
    }
    return val;
}

public static int multAccum(int oldAcc, int newVal, int scale) {
    BigInteger product = BigInteger.valueOf(newVal).multiply(BigInteger.valueOf(scale));
    BigInteger res = intRangeCheck(BigInteger.valueOf(oldAcc).add(product));
    return res.intValue(); // Safe conversion
}
```

## 



## static关键字

方便在没有创建对象的情况下来进行调用（方法/变量），只要类被加载了，就可以通过类名去进行访问，不需要依赖于对象来进行访问。

static可以用来修饰类的成员方法、类的成员变量，另外可以编写static代码块来优化程序性能。

### static方法

静态方法没有对象，没有this，在静态方法中不能访问类的非静态成员变量和非静态成员方法，因为非静态成员方法/变量都是必须依赖具体的对象才能够被调用。但是在非静态成员方法中是可以访问静态成员方法/变量的。

子类重写父类的静态方法，只要是父类的引用，无论是否指向子类的对象甚至指向null，调用的依然是父类的静态方法

### static变量

静态变量当且仅当在类初次加载时会被初始化，在内存中只有一个副本，被所有的对象所共享

### static代码块

静态块可以置于类中的任何地方，类中可以有多个static块。在类初次被加载的时候，会按照static块的顺序来执行每个static块，并且只会执行一次。

**PS:**

- 静态成员变量虽然独立于所在的对象，但是不代表不可以通过对象去访问，所有的静态方法和静态变量都可以通过所在类的实例对象访问（只要访问权限足够）。
- static是不允许用来修饰局部变量

```java
//通过具体实例对象访问静态方法看引用变量类型
public class Parent {
    public static void test(){
        System.out.println("parent-test");
    }
}

public class Child extends Parent {
    public static void test(){
        System.out.println("child-test");
    }
}

public static void main(String[] args) {
    Parent parent=new Parent();
    parent.test();

    Parent _parent=new Child();
    _parent.test();

    Child child=new Child();
    child.test();
}

输出：
parent-test
parent-test
child-test    
```



## final关键字

- final 修饰变量
  
  - final 修饰成员变量，则该成员变量不会进行隐式的初始化，必须要初始化。
- final 修饰变量，则该变量的值只能赋一次值。
  
- final 修饰方法，则该方法不允许被覆写。

- final 修饰类，则该类不允许被继承。

  注意：final修饰的类，类中的所有成员方法都被隐式地指定为final方法。

## Java修饰符作用域

| 作用域    | 当前类 | 同包 | 子类 | 其他包 |
| --------- | ------ | ---- | ---- | ------ |
| public    | √      | √    | √    | √      |
| protected | √      | √    | √    | ×      |
| default   | √      | √    | ×    | ×      |
| private   | √      | ×    | ×    | ×      |

## 变量类型

类中定义的变量称为成员变量，成员变量分为静态变量(全局变量)和实例变量，方法中定义的变量称为局部变量。

## 错误

### catch

编写多重catch语句时，需要先小后大，即先子类再父类

### finally

- finally语句块总是会被执行。只有finally块执行完成之后，才会回来执行try或者catch块中的return或者throw语句，如果finally中使用了return或者throw等终止方法的语句，则就不会跳回执行，直接停止。

  ```java
  public String test() {
      int i = 0;
      try {
          i = 1;
          return "" + (++i);
      } catch (Exception e) {
  
      } finally {
          System.out.println(i);
          i = 10;
          return "" + i;
      }
      return "0";
  }
  
  输出2
  return 10
  ```

- try或者catch块中的return或者throw语句会延迟执行，但是finally执行语句不会影响try或者catch块中的return或者throw执行结果，除非是在finally中的return或throw替代。

  ```java
  public String test() {
      int i = 0;
      try {
          i = 1;
          return "" + (++i);
      } catch (Exception e) {
  
      } finally {
          System.out.println(i);
          i = 10;
      }
      return "0";
  }
  
  输出2
  return 2
  ```

  也就是说，**finally执行语句不会影响原有返回值，但是finally中如执行返回（return或抛异常），会代替原有返回逻辑。**

  finally执行时机在try/catch代码块退出前。即，所有代码块后，跳出逻辑前。只有finally块执行完成之后，才会回来执行try或者catch块中的return或者throw语句，如果finally中使用了return或者throw等终止方法的语句，则就不会跳回执行，直接停止




### try-with-resource

- 传统的`try-finally`方式存在复杂易出错和异常抑制(Suppressed)等问题。 使用`try-with-resource`，可以更安全、简洁地申请和关闭资源，同时解决了异常抑制问题。 
- `try-with-resource`是语法糖，其最终仍然会被编译成`try-finally`方式并调用close方法关闭资源。 
- 为了支持`try-with-resource`，资源类必须要实现`AutoClosable接口`，否则无法使用。在使用`try-with-resource`之前，请务必确认下资源类是否已经实现了`AutoClosable接口`。
- `try-with-resource`使用方式
  - `try-with-resource`使用很简单，申请资源的代码写在try后面的()中即可，无需显式调用`close方法`来关闭资源。 
  - 为了使程序更加健壮，在`try-with-resouce`中使用装饰器时，建议显式声明被装饰/包裹对象的引用。 

#### 使用

能够借助`try-with-resource`关闭资源的类必须实现`AutoClosable接口`，重写`close方法`。如果是自定义资源类， 为了支持`try-with-resource`，请务必实现`AutoClosable接口`。 

```java
//申请资源的代码写在try后面的()中即可，资源关闭无需显式close
public void twrClose2NotCatch(String src, String dst) throws IOException {
    try (FileInputStream ins = new FileInputStream(src);
         OutputStream outs = new FileOutputStream(dst)) {
        byte[] buf = new byte[BUF_SIZE];
        int n;
        while ((n = ins.read(buf)) >= 0) {
            outs.write(buf, 0, n);
        }
    }
}
```

`try-with-resource`主要做了两件事：

- 添加调用close方法的代码，关闭资源。
- 使用addSuppressed方法附加异常，消除异常抑制的问题。

##### try-finally举例

###### 异常抑制

异常抑制（Suppressed）也有叫异常覆盖和异常屏蔽

```java
public class ConnectionNormal {
    public void send() throws Exception {
        throw new SendException("send fail.");
    }

    public void close() throws Exception {
        throw new CloseException("close fail");
    }
}

//按照程序逻辑，应该先抛出SendException，再抛出CloseException：
public static void main(String[] args) {
    try {
        test();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


private static void test() throws Exception {
    ConnectionNormal conn = null;
    try {
        conn = new ConnectionNormal();
        conn.send();
    } finally {
        if (conn != null) {
            conn.close();
        }
    }
}

//运行后我们发现：
com.CloseException: close fail
at com.ConnectionNormal.close(ConnectionNormal.java:10)
    at com.TryWithResource.test(TryWithResource.java:20)
    at com.TryWithResource.main(TryWithResource.java:6)
```

SendException明明先被抛出了，但是没有丝毫痕迹，被后抛的CloseException给抑制了，这就是异常抑制，SendException被称为Suppressed Exception。关键的异常信息丢失，这会导致某些bug变得极其隐蔽而难以发现！

##### try-with-resource举例

`try-with-resource`为`Throwable类`新增了`addSuppressed方法`，支持将一个异常附加到另一个之上，从而解决异常抑制。

```java
public class ConnectionAutoClose implements AutoCloseable{
    public void send() throws Exception {
        throw new SendException("send fail.");
    }
    @Override
    public void close() throws Exception {
        throw new CloseException("close fail");
    }
}

private static void test2() {
    try (ConnectionAutoClose conn = new ConnectionAutoClose()) {
        conn.send();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

运行结果：
com.SendException: send fail.
    at com.ConnectionAutoClose.send(ConnectionAutoClose.java:5)
    at com.TryWithResource.test2(TryWithResource.java:27)
    at com.TryWithResource.main(TryWithResource.java:6)
    Suppressed: com.CloseException: close fail
at com.ConnectionAutoClose.close(ConnectionAutoClose.java:10)
        at com.TryWithResource.test2(TryWithResource.java:28)
        ... 1 more
```

#### 原理

```java
//try-with-resource反编译.class
private static void test2() {
    try {
        ConnectionAutoClose conn = new ConnectionAutoClose();
        Throwable var1 = null;
        try {
            conn.send();
        } catch (Throwable var11) {
            var1 = var11;
            throw var11;
        } finally {
            if (conn != null) {
                if (var1 != null) {
                    try {
                        conn.close();
                    } catch (Throwable var10) {
                        var1.addSuppressed(var10);
                    }
                } else {
                    conn.close();
                }
            }
        }
    } catch (Exception var13) {
        var13.printStackTrace();
    }
}
```

#### 显式声明

为了使程序更加健壮，在try-with-resouce中使用装饰器时，建议显式声明被装饰 / 包裹对象的引用。

```java
//在finally中仅调用了out.close()，在GZIPOutputStream.close内部，会关闭被包裹的FileOutputStream，但是这个关闭可能会失败
public void gzipWrapper(File file) throws IOException {
   try (GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(file))) {
      ...
  }
}
```

```java
//fout.close一定会被调用
public void gzipWrapperRobust(File file) throws IOException {
   try (FileOutputStream fout = new FileOutputStream(file); // 显式声明
        GZIPOutputStream out = new GZIPOutputStream(fout)) {
      ...
  }
}
```



## 抽象类和接口

抽象类和接口的作用就是让子类继承，在子类中根据具体情况去实现抽象方法。

- 抽象类并不是只能包含抽象方法，他也可以包含普通的成员方法和成员变量、构造方法、静态方法、静态成员变量。接口中只能有抽象方法和静态成员变量；
  - 抽象类中的抽象方法只能用public或protected修饰以便子类继承重写。接口中的抽象方法只能是 public 类型的，并且默认即为 public abstract 类型。
  - 其中抽象类中的静态成员变量的访问类型可以任意，但接口中定义的变量只是public static final 类型，并且默认即为 public static final 类型。

- 一个类可以实现多个接口，但只能继承一个抽象类；
- 抽象类和接口不能创建对象，即不能实例化；

- 继承抽象类的子类和实现接口的子类必须实现所有抽象方法，否则子类也是抽象类；

- 在jdk1.8以后增加default方法。default修饰的方法是可以在接口的实现类中不实现的。

## 包装类

> 包装类就是把基本数据类型和其辅助方法的封装到类中
>
> 包装类装箱是指把基本类型装入包装类的过程，拆箱是从包装类对象中取出基本类型

```java
Integer a = Integer.valueof(5);	//装箱
int b = a.intValue();			//拆箱

//自动拆装箱
Integer a=5;	//装箱
int b=a;		//拆箱
```

```java
Integer s = 0;
for (int i = 0; i < 1000; i++) {
    s += i;
}
//这个循环，被编译器转换为下面的代码，一共执行了1000次拆箱和装箱，申请/释放了1000次Integer对象。
Integer s = 0;
for (int i = 0; i < 1000; i++) {
    s = Integer.valueof(s.intvalue + i);
}
```

```java
//包装类默认值为null，自动拆箱时会报NullPointerException
Integer i;
if (i > 0) {//NullPointerException
    syso(...);
}
```

- 包装类比较必须使用equals()，`==`比较的是两个引用是否指向一个对象
- 包装类的创建很浪费性能，因此Java对简单数字（ -128 ~ 127 ）对应的包装类进行了缓存，称为常量池。
  通过直接量赋值的包装类如果在此范围内，会直接使用常量池中的引用，因此`==`返回true。

```java
Integer i1 = 100;
Integer i2 = 100;
System.out.println(i1 == i2);//true
Integer i3 = new Integer(100);
System.out.println(i1 == i3);//false
```



### Integer

```java
//常用方法
public static void main(String[] args) {
    System.out.println(Integer.toBinaryString(14));	//1110
    System.out.println(Integer.toHexString(14));	//e
    System.out.println(Integer.toOctalString(14));	//16
    System.out.println(Integer.parseInt("12",8));	//10
    System.out.println(Integer.parseInt("1010",2));	//10
    System.out.println(Integer.parseInt("ff",16));	//255
}
```



- `Integer.parseInt(String s)`将会返回int常量。
- `Integer.valueOf(String s)`将会返回Integer类型，如果存在缓存将会返回缓存中已有的对象。

```java
//Integer.valueOf的源码
public static Integer valueOf(String s) throws NumberFormatException {
    return Integer.valueOf(parseInt(s, 10));
}

//Integer.valueOf()是有缓存的，在IntegerCache中cache数组初始化存入了-128 - 127的值,会缓存-128 ~ 127范围的整型数字
//给Interger 赋予的int数值在-128 - 127的时候，直接从cache中获取，这些cache引用对Integer对象地址是不变的，但是不在这个范围内的数字，则new Integer(i) 这个地址是新的地址。
public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
}
```

```java
public static void main(String[] args){
    Integer a = new Integer(3);
    Integer b = 3;             //自动装箱，将会调用Integer.valueOf(n)
    int c = 3;
    System.out.println(a == b);//false    
    System.out.println(a == c);//true,一个Integer与int比较，先将Integer转换成int类型，再做值比较
}
```



## 内部类

> 每个内部类都能独立地继承类，所以无论外围类是否已经继承了某个类，对于内部类都没有影响。使用内部类最大的优点就在于它能够非常好的解决多重继承的问题。

内部类的其他特性：

- 内部类可以用多个实例，每个实例都有自己的状态信息，并且与其外围对象的信息相互独立。

- 在单个外围类中，可以让多个内部类以不同的方式实现同一个接口，或者继承同一个类。

- 内部类提供了更好的封装，除了该外围类，其他类都不能访问。

### 基本语法

- 内部类会自动创建一个隐式的外部类对象的引用`OuterClassName.this`，可以通过这个引用访问外部对象
- 创建内部类时必须要利用外部类的对象通过.new来创建： `OuterClass.InnerClass innerClass = outerClass.new InnerClass();`

```java
public class OuterClass {
    private String name ;
    private int age;

    /**省略getter和setter方法**/
    
    public void display(){
        System.out.println("OuterClass...");
    }
    
    public class InnerClass{
        public InnerClass(){
            name = "chenssy";
            age = 23;
        }
        
        public void display(){
            System.out.println("name：" + getName() +"   ;age：" + getAge());
        }
        
        public OuterClass getOuterClass(){
            return OuterClass.this;
        }
    }
    
    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
   		//实例化内部类，必须要依赖外部类的对象
        OuterClass.InnerClass innerClass = outerClass.new InnerClass();
        innerClass.display();
        
        innerClass.getOuterClass().display();
    }
}
```

内部类是个编译时的概念，一旦编译成功后，它就与外围类属于两个完全不同的类（当然他们之间还是有联系的）。对于一个名为`OuterClass`的外围类和一个名为`InnerClass`的内部类，在编译成功后，会出现这样两个class文件：`OuterClass.class`和`OuterClass$InnerClass.class`。

### 成员内部类

> 成员内部类也是最普通的内部类，它是外围类的一个成员，所以他是可以无限制的访问外围类的所有成员属性和方法。
>
> 但是外围类要访问内部类的成员属性和方法则需要通过内部类实例来访问。

- 第一：成员内部类中不能存在任何static的变量和方法；
- 第二：成员内部类是依附于外围类的，所以只有先创建了外围类才能够创建内部类。

```java
public class OuterClass {
    private String str;
    
    public void outerDisplay(){
        System.out.println("outerClass...");
    }
    
    public class InnerClass{
        public void innerDisplay(){
            //使用外围类的属性
            str = "chenssy...";
            System.out.println(str);
            //使用外围内的方法
            outerDisplay();
        }
    }
    
    /*推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时 */
    public InnerClass getInnerClass(){
        return new InnerClass();
    }
    
    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        OuterClass.InnerClass inner = outer.getInnerClass();
        inner.innerDisplay();
    }
}
```

### 局部内部类

> 局部内部类嵌套在方法的方法体内。

想创建一个类来辅助我们的解决方案，到那时又不希望这个类是公共可用的，所以就产生了局部内部类，它的作用域只在该方法中。

```java
public class OuterClass {
    public String destionation(String str){
        class InnerClass implements Destionation{
            private String label;
            private InnerClass(String whereTo){
                label = whereTo;
            }
            public String readLabel(){
                return label;
            }
        }
        final InnerClass innerClass = new InnerClass(str);
        return innerClass.readLabel();
    }

    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
        String label = outerClass.destionation("chenssy");
        System.out.println(label);
    }
}

interface Destionation{
    String readLabel();
}
```

### 匿名内部类

```java
public class MainClass {
    public OuterClass getOuterClass(final int num){
        final OuterClass outerClass = new OuterClass() { // new 一个接口，outerClass指向这个匿名内部类
            int number = num + 3;
            public int getNumber() {
                return number;
            }
        };
        return outerClass;
    }

    public static void main(String[] args) {
        MainClass main = new MainClass();
        OuterClass outer = main.getOuterClass(2);
        System.out.println(outer.getNumber());
    }
}

interface OuterClass {
    int getNumber();
}
```

### 静态内部类

> 使用static修饰的内部类我们称之为静态内部类。

非静态内部类在编译完成之后会隐含地保存着一个引用，该引用是指向创建它的外围类，但是静态内部类却没有。

- 它的创建是不需要依赖于外围类的。
- 它不能使用任何外围类的非static成员变量和方法，可以使用外部类中所有静态属性和所有的静态方法，其他使用方法和普通类相同
- 外部类初次加载，会初始化静态变量、静态代码块、静态方法，但不会加载静态内部类。直接调用静态内部类时，外部类不会加载。

```java
public class OuterClass {
    private String sex;
    public static String name = "chenssy";
    
    /**
     *静态内部类
     */
    static class InnerClass1{
        /* 在静态内部类中可以存在静态成员 */
        public static String _name1 = "chenssy_static";
        
        public void display(){
            /* 
             * 静态内部类只能访问外围类的静态成员变量和方法
             * 不能访问外围类的非静态成员变量和方法
             */
            System.out.println("OutClass name :" + name);
        }
    }
    
    /**
     * 非静态内部类
     */
    class InnerClass2{
        /* 非静态内部类中不能存在静态成员 */
        public String _name2 = "chenssy_inner";
        /* 非静态内部类中可以调用外围类的任何成员,不管是静态的还是非静态的 */
        public void display(){
            System.out.println("OuterClass name：" + name);
        }
    }
    
    /**
     * @desc 外围类方法
     * @author chenssy
     * @data 2013-10-25
     * @return void
     */
    public void display(){
        /* 外围类访问静态内部类：内部类. */
        System.out.println(InnerClass1._name1);
        /* 静态内部类 可以直接创建实例不需要依赖于外围类 */
        new InnerClass1().display();
        
        /* 非静态内部的创建需要依赖于外围类 */
        OuterClass.InnerClass2 inner2 = new OuterClass().new InnerClass2();
        /* 访问非静态内部类的成员需要使用非静态内部类的实例 */
        System.out.println(inner2._name2);
        inner2.display();
    }
    
    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        outer.display();
    }
}
```



### 小结

内部类主要为了实现多重继承问题，每个内部类都能独立地继承类 ，可以在单个外围类中，让多个内部类以不同的方式实现同一个接口，或者继承同一个类。

内部类是个编译时的概念，一旦编译成功后，它就与外围类属于两个完全不同的类。

成员内部类、局部内部类和匿名内部类会创建隐式引用，内部可以访问外部类的所有变量方法，外部访问内部必须先创建外部类对象，其中局部内部类作用域在方法内，在方法内部可以直接当成常规类使用。

静态内部类无隐式引用，内部可以访问外部类的所有静态变量方法，不依赖于外部类可以直接当成常规类使用。

[方法中定义内部类](https://blog.csdn.net/weixin_46245201/article/details/111687848)





## 覆写, 重载, 隐藏, 遮蔽, 遮掩

- **重写**方法名、返回值和形参 都不能改变，发生在父子类中；

- **重载**形参必须不同，修饰符和返回值可以不同；

- **隐藏(hide)** 存在于子类和父类间

  在子类中定义一个和父类相同的**属性, 静态方法或内部类**，父类成员则被隐藏，被隐藏后，将阻止其被继承

- **遮蔽 (shadow)** 存在于类内部

  在类中定义和其他外部类名称相同的内部类，内部类中定义和外部类相同的**变量, 方法**，则其他外部类、外部类中的变量方法就被遮蔽了，在内部类中无法调用被遮蔽的变量方法。

- **遮掩(obscure)** 存在于类内部

  一个变量可以遮掩具有相同名字的一个类

[Java 之 覆写, 重载, 隐藏, 遮蔽, 遮掩](https://blog.csdn.net/Beyond_Nothing/article/details/112465710)



































## 集合

Collection 接口的接口 对象的集合（单列集合）

- List 接口：元素按进入先后有序保存，可重复
  - LinkedList 接口实现类， 链表， 插入删除， 没有同步， 线程不安全
  - ArrayList 接口实现类， 数组， 随机访问， 没有同步， 线程不安全
  - Vector 接口实现类 数组， 同步， 线程安全
    - Stack 是Vector类的实现类

- Set 接口： 仅接收一次，不可重复，并做内部排序
  - HashSet 使用hash表（数组）存储元素，无序
    - LinkedHashSet 链表维护元素的插入次序
  - TreeSet 底层实现为二叉树，元素排好序

Map 接口 键值对的集合 （双列集合）

- Hashtable 接口实现类， 同步， 线程安全，不能放null
- HashMap 接口实现类 ，没有同步， 线程不安全，key或value可以为null，key最多只能有一个null
  - LinkedHashMap 双向链表和哈希表实现
  - WeakHashMap
- TreeMap 红黑树对所有的key进行排序
- IdentifyHashMap

Vector与ArrayList一样，也是通过数组实现的，不同的是它支持线程的同步，并且当Vector或ArrayList中的元素超过它的初始大小时，Vector会将容量翻倍，ArrayList只增加50%的大小。

参考：[java集合超详解](https://blog.csdn.net/feiyanaffection/article/details/81394745)

### hash

- 哈希表（hash table）也叫散列表，是一种非常重要的数据结构，哈希表的主干是数组，所以在哈希表中进行添加，删除，查找等操作，性能十分之高，不考虑哈希冲突的情况下，仅需一次定位即可完成。比如我们要新增或查找某个元素，我们通过把当前元素的关键字通过Hash函数映射到数组中的某个位置，通过数组下标一次定位就可完成操作。

- Hash，一般翻译做“散列”，也有直接音译为“哈希”的，就是把任意长度的输入，通过散列算法，变换成固定长度的输出，该输出就是散列值。常见的Hash算法有直接定址法、平方取中法、折叠法、除数取余法、随机数法。

  - 这种转换是一种压缩映射，但是两个不同的输入值，根据同一散列函数计算出的散列值可能相同，这种现象叫做碰撞(哈希冲突)。

  - 衡量一个哈希函数的好坏的重要指标就是发生碰撞的概率以及发生碰撞的解决方案。好的哈希函数会尽可能地保证 计算简单和散列地址分布均匀，但是，我们需要清楚的是，数组是一块连续的固定长度的内存空间，再好的哈希函数也不能保证得到的存储地址绝对不发生冲突。常见的解决碰撞的方法有以下几种：

    - 开放定址法

      开放定址法就是一旦发生了冲突，就去寻找下一个空的散列地址，只要散列表足够大，空的散列地址总能找到，并将记录存入。

    - 链地址法

      将哈希表的每个单元作为链表的头结点，所有哈希地址为i的元素构成一个同义词链表。即发生冲突时就把该关键字链在以该单元为头结点的链表的尾部。HashMap即是采用了链地址法，也就是**数组+链表**的方式

    - 再哈希法

      当哈希地址发生冲突用其他的函数计算另一个哈希函数地址，直到冲突不再产生为止。

    - 建立公共溢出区

      将哈希表分为基本表和溢出表两部分，发生冲突的元素都放入溢出表中。
    

- Hash算法中会用到`hashCode()`方法，`hashCode()`的作用就是获取哈希码，也称为散列码，返回一个`int`整数。这个哈希码的作用就是用来确定该对象在哈希表中的索引位置。`hashCode()`定义在JDK的`Object.java`中，这就意味着`Java`中的任何类都包含有`hashCode()`函数。在`Object`类中，`hashCode`方法是通过`Object`对象的地址计算出来的。

#### HashMap

##### 存储流程

hashMap就是一个哈希表，数组默认初始容量是16

```java
//HashMap的hash算法，计算出map中key的hash值
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

```java
//HashMap的indexFor方法，hashmap是通过数组长度-1&key的hash值来计算出数组下标的
/**
 * 返回数组下标
 */
static int indexFor(int h, int length) {
    return h & (length-1);
}
```

综上可以看出数组下标时通过`(length-1)&(h = key.hashCode()) ^ (h >>> 16)`计算得出的

```java
System.out.println(Integer.toBinaryString(15));
System.out.println(Integer.toBinaryString("测试".hashCode()));


位数不够，高位补0，即
0000 0000 0000 0000 0000 0000 0000 1111	//(length-1)
    &
0000 0000 0000 1101 1100 0111 1110 1010	//(h = key.hashCode())
	^
0000 0000 0000 0000 0000 0000 0000 1101	//(h >>> 16)
```

- `(length - 1) & hash`与运算取到了末四位的值作为数组下标。
- hash算法中通过`key.hashCode() ^ (key.hashCode() >>> 16)` 这个巧妙的扰动算法，key的hash值经过无符号右移16位，再与key原来的hash值进行 ^ 运算，就能很好的保留hash值的高16位和低16位的所有特征，这都是为了让数组下标更分散，避免哈希冲突

**所以最终存储位置的确定流程是这样的：**

![img](img_Java%E5%9F%BA%E7%A1%80/1024555-20161115133556388-1098209938.png)

##### 实现原理

HashMap的主干是一个Entry数组。Entry是HashMap的基本组成单元，每一个Entry包含一个key-value键值对。

```java
//HashMap的主干数组，可以看到就是一个Entry数组，初始值为空数组{}，主干数组的长度一定是2的次幂
transient Entry<K,V>[] table = (Entry<K,V>[]) EMPTY_TABLE;
```

Entry是HashMap中的一个静态内部类。代码如下

```java
static class Entry<K,V> implements Map.Entry<K,V> {
    final K key;
    V value;
    Entry<K,V> next;//存储指向下一个Entry的引用，单链表结构
    int hash;//对key的hashcode值进行hash运算后得到的值，存储在Entry，避免重复计算

    /**
     * Creates new entry.
     */
    Entry(int h, K k, V v, Entry<K,V> n) {
        value = v;
        next = n;
        key = k;
        hash = h;
    } 
}
```

所以，HashMap的整体结构如下

![img](img_Java%E5%9F%BA%E7%A1%80/1024555-20161113235348670-746615111.png)

**简单来说，HashMap由数组+链表组成的，数组是HashMap的主体，链表则是主要为了解决哈希冲突而存在的，如果定位到的数组位置不含链表（当前entry的next指向null）,那么对于查找，添加等操作很快，仅需一次寻址即可；如果定位到的数组包含链表，对于添加操作，其时间复杂度为O(n)，首先遍历链表，存在即覆盖，否则新增；对于查找操作来讲，仍需遍历链表，然后通过key对象的equals方法逐一比对查找。所以，性能考虑，HashMap中的链表出现越少，性能才会越好。**

其他几个重要字段：

```java
//实际存储的key-value键值对的个数
transient int size;

//阈值，当table == {}时，该值为初始容量（初始容量默认为16）；当table被填充了，也就是为table分配内存空间后，threshold一般为 capacity*loadFactory。HashMap在进行扩容时需要参考threshold
int threshold;

//负载因子，代表了table的填充度有多少，默认是0.75
final float loadFactor;

//用于快速失败，由于HashMap非线程安全，在对HashMap进行迭代时，如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），需要抛出异常ConcurrentModificationException
transient int modCount;
```

- HashMap构造器可以传入initialCapacity 和loadFactor这两个参数，initialCapacity默认为16，loadFactory默认为0.75。其中无论用户传入的initialCapacity 是多少，HashMap都可以确保capacity为大于或等于initialCapacity ，且最接近toSize的二次幂，使得数组长度一定为2的次幂。
- 当发生哈希冲突并且size大于阈值的时候，需要进行数组扩容，扩容时，需要新建一个长度为之前数组2倍的新的数组，然后将当前的Entry数组中的元素全部传输过去，扩容后的新数组长度为之前的2倍，所以扩容相对来说是个耗资源的操作。

##### 扩容

```java
void resize(int newCapacity) {
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
    if (oldCapacity == MAXIMUM_CAPACITY) {
        threshold = Integer.MAX_VALUE;
        return;
    }

    Entry[] newTable = new Entry[newCapacity];
    //transfer
    transfer(newTable, initHashSeedAsNeeded(newCapacity));
    table = newTable;
    threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
}
```

如果数组进行扩容，数组长度发生变化，而存储位置` index = h&(length-1)`，index也可能会发生变化，需要重新计算index，我们先来看看transfer这个方法

```java
void transfer(Entry[] newTable, boolean rehash) {
    int newCapacity = newTable.length;
 	//for循环中的代码，逐个遍历链表，重新计算索引位置，将老数组数据复制到新数组中去（数组不存储实际数据，所以仅仅是拷贝引用而已）
    for (Entry<K,V> e : table) {
        while(null != e) {
            Entry<K,V> next = e.next;
            if (rehash) {
                e.hash = null == e.key ? 0 : hash(e.key);
            }
            int i = indexFor(e.hash, newCapacity);
      		//将当前entry的next链指向新的索引位置,newTable[i]有可能为空，有可能也是个entry链，如果是entry链，直接在链表头部插入。
            e.next = newTable[i];
            newTable[i] = e;
            e = next;
        }
    }
}
```

hashMap的数组长度一定保持2的次幂，在计算数组下标`hash&(length-1)`中扩容后`length-1`的二进制只有一位差异，也就是多出了最左位的1，可以尽量保证新的数组索引和老数组索引一致，减少了之前已经散列良好的老数组的数据位置重新调换

##### 重写hashCode方法

重写equals方法需同时重写hashCode方法

hashcode是用于散列数据的快速存取，如利用HashSet/HashMap/Hashtable类来存储数据时，都是根据存储对象的hashcode值来进行判断是否相同的

```java
public class MyTest {
    private static class Person{
        int idCard;
        String name;

        public Person(int idCard, String name) {
            this.idCard = idCard;
            this.name = name;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()){
                return false;
            }
            Person person = (Person) o;
            //两个对象是否等值，通过idCard来确定
            return this.idCard == person.idCard;
        }

    }
    public static void main(String []args){
        HashMap<Person,String> map = new HashMap<Person, String>();
        Person person = new Person(1234,"乔峰");
        //put到hashmap中去
        map.put(person,"天龙八部");
        //get取出，从逻辑上讲应该能输出“天龙八部”
        System.out.println("结果:"+map.get(new Person(1234,"萧峰")));
    }
}

//结果：null
```

只重写了equals方法，并没有重写hashCode方法。尽管我们在进行get和put操作的时候，使用的key从逻辑上讲是等值的（通过equals比较是相等的），但是hashCode没有被重写，所以就调用超类Object的hashCode方法，而这个方法是根据地址来获取hashcode的。而两个person又是不同的对象，它们的地址肯定不同，所以获得的hashcode也不同，所以返回为null。

##### Java7和Java8

...

#### hash算法

...

参考：

[hash算法原理详解](https://blog.csdn.net/tanggao1314/article/details/51457585)

[全网把Map中的hash()分析的最透彻的文章，别无二家。](https://mp.weixin.qq.com/s/qCHkzs4JPOipB-ZzqrfbeQ)











参考：

[HashMap实现原理及源码分析](https://www.cnblogs.com/chengxiao/p/6059914.html)

[真正搞懂hashCode和hash算法](https://blog.csdn.net/qq_33709582/article/details/113337405?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522163065867216780262570503%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fblog.%2522%257D&request_id=163065867216780262570503&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~blog~first_rank_v2~rank_v29-1-113337405.pc_v2_rank_blog_default&utm_term=hash&spm=1018.2226.3001.4450)

[重写equals方法需同时重写hashCode方法](https://blog.csdn.net/weixin_43207025/article/details/114177242)

### Iterator（迭代器）

```java
//获取集合对应的迭代器，用来遍历集合中的元素。
public Iterator iterator()
```

```java
public E next() 			返回迭代的下一个元素。
public boolean hasNext() 	如果仍有元素可以迭代，则返回 true。
```

```java
List<String> list = new ArrayList<String>();     
Iterator<String> it = list.iterator();    //获取迭代类的实现对象，并将指针指向 -1 索引    

while(it.hasNext()){         //判断是否有下一个元素
    String s = it.next();    //获取下一个出的元素，并将指针向后移动一位
    System.out.println(s);
}
```

### 增强 for 循环

专门用来遍历数组和集合的，它的内部原理其实是个Iterator迭代器。

### ConcurrentModificationException

使用迭代器对集合遍历的过程中，不能对集合中的元素进行增删操作，否则会抛出`java.util.ConcurrentModificationException`异常,如：

```java
public class Test {
    public static void main(String[] args)  {
        List<Integer> list = new ArrayList<Integer>();
        list.add(2);
        Iterator<Integer> iterator = list.iterator();
        while(iterator.hasNext()){
            Integer integer = iterator.next();
            if(integer==2)
                list.remove(integer);
        }
    }
}
```

#### 在单线程环境下的解决办法

```java
//1、使用Iterator提供的remove方法，用于删除当前元素
for (Iterator<string> it = myList.iterator(); it.hasNext();) {
    String value = it.next();
    if (value.equals( "3")) {
    	it.remove();
    }
}
System.out.println( "List Value:" + myList.toString());
 
//2、建一个集合，记录需要删除的元素，之后统一删除
List<string> templist = new ArrayList<string>();
    for (String value : myList) {
        if (value.equals("3")) {
            templist.add(value);
        }
    }
}
//可以查看removeAll源码，其中使用Iterator进行遍历
myList.removeAll(templist);
System. out.println( "List Value:" + myList.toString());        
 
//3、使用线程安全CopyOnWriteArrayList进行删除操作
List<string> myList = new CopyOnWriteArrayList<string>();
myList.add( "1");
myList.add( "2");
myList.add( "3");
myList.add( "4");
myList.add( "5");
Iterator<string> it = myList.iterator();
while (it.hasNext()) {
    String value = it.next();
    if (value.equals( "3")) {
        myList.remove( "4");
        myList.add( "6");
        myList.add( "7");
    }
}
System.out.println("List Value:" + myList.toString());
 
//4、不使用Iterator进行遍历，需要注意的是自己保证索引正常
for ( int i = 0; i < myList.size(); i++) {
    String value = myList.get(i);
    System.out.println( "List Value:" + value);
    if (value.equals( "3")) {
        myList.remove(value);
        i--; // 因为位置发生改变，所以必须修改i的位置
    }
}
System.out.println( "List Value:" + myList.toString());
```

#### 在多线程环境下的解决方法

```java
List<string> myList = new CopyOnWriteArrayList<string>();
myList.add( "1");
myList.add( "2");
myList.add( "3");
myList.add( "4");
myList.add( "5");

new Thread(new Runnable() {
     @Override
     public void run() {
          for (String string : myList) {
               System.out.println("遍历集合 value = " + string);
               try {
                    Thread.sleep(100);
               } catch (InterruptedException e) {
                    e.printStackTrace();
               }
          }
     }
}).start();

new Thread(new Runnable() {
    @Override
    public void run() {
        //使用迭代器获取list里的值
        Iterator<String> it = myList.iterator();
        while (it.hasNext()){
            String value=it.next();
            System.out.println("删除元素 value = " + value);
            if (value.equals( "3")) {
                myList.remove(value);
                myList.add( "7");
        		myList.add( "8");
            }
            try {
                Thread.sleep(100);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
        //也可以直接使用for循环
        /*for (int i = 0; i < myList.size(); i++) {
            String value = myList.get(i);
            System.out.println("删除元素 value = " + value);
            if (value.equals( "3")) {
                myList.remove(value);
                i--; // 注意                           
            }
            try {
            	Thread.sleep(100);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }*/
    }
}).start();
```

**总结：**

单线程中`CopyOnWriteArrayList`需要使用`list.remove(value)`删除；`ArrayList`需要使用`it.remove()`删除；或者使用for循环遍历索引配合`CopyOnWriteArrayList`和`ArrayList`都可以使用，此时只能用`list.remove(value)`;

多线程中必须使用`CopyOnWriteArrayList`，使用`list.remove(value)`删除；或者使用for循环配合`CopyOnWriteArrayList`





参考：[Java ConcurrentModificationException异常原因和解决方法](https://www.cnblogs.com/zhuyeshen/p/10956822.html)



## 启动 jar 包

使用命令启动 jar 包

**使用 java -cp 命令指定类路径：**

jvm搜索类，系统会先在当前包下找，然后会在 jre 中的 rt.jar 文件下找，最后会在 classpath 所指向的路径下找； -cp 即 -classpath，可以指定classpath；java -cp 和 -classpath 一样，是指定类运行所依赖其他类的路径，通常是类库，jar包之类，需要全路径到jar包 。

```sh
#windows
#编译
$ javac -cp 绝对路径1.jar;绝对路径2.jar java文件名.java

#运行
$ java -cp .;../lib/*;../common.jar;../list/*.jar;../conf packname.mainclassname(此类有main方法)
```

```sh
#linux
#编译
$ javac -cp 绝对路径1.jar:绝对路径2.jar java文件名.java

#运行
$ java -cp .:../lib/*:../common.jar:../list/*.jar:../conf packname.mainclassname(此类有main方法)
```

**引用 jar 包中的 jar 包：**

 [java命令执行jar包的方式](https://www.cnblogs.com/zpbolgs/p/7267384.html)

**启动 jar 包方式：**

jar 包内目录 META-INF 下的 MANIFEST.MF 文件可以指定入口的方法，

```java
//META-INF\MANIFEST.MF的内容：
Manifest-Version: 1.0 
Main-Class: test.core.Core 
```

```sh
#pom中build指定mainClass && META-INF\MANIFEST.MF文件中增加了Main-Class: test.core.Core
$ java -cp test-jar-with-dependencies.jar  test.core.Core  //执行成功
$ java -jar test-jar-with-dependencies.jar  //执行成功
```

[启动jar包](https://www.cnblogs.com/klb561/p/10850803.html)









## 注解

注解(Annotation)相当于一种标记，标记可以加在包、类，属性、方法，方法的参数以及局部变量上。

自定义注解需要三个类：

- 注解类：定义了注解的类
- 应用类：应用了"注解类"的类
- 反射类：对应用类进行了反射操作，判断有无注解并且创建注解类的实例对象

注解更像是一种特殊的接口，而应用类可以认为是实现了这个特殊的接口，使用了注解就是创建了注解类的一个实例对象。

一般注解有两个作用：打一个标记，对被标记的类或元素在反射类中做后续处理，比如通过注解在应用类中注入对象；因为定义了注解对象，引入了注解类中的属性变量可以定义属性值；

### 注解类

```java
/*
    在注解类上使用另一个注解类，那么被使用的注解类就称为元注解
    
    Retention注解决定MyAnnotation注解的生命周期
    	@Retention(RetentionPolicy.SOURCE)
     		让MyAnnotation注解只在java源文件中存在，编译成.class文件后注解就不存在了
       @Retention(RetentionPolicy.CLASS)
       	这个注解的意思是让MyAnnotation注解在java源文件(.java文件)中存在，编译成.class文件后注解也还存在，
        	但是应用类被类加载器加载到内存中(内存中的字节码)后MyAnnotation注解就不存在了
    	@Retention(RetentionPolicy.RUNTIME)
     		注解的生命周期一直到程序运行时都存在
    Target注解决定MyAnnotation注解可以加在哪些成分上，如加在类身上，或者属性身上，或者方法身上,默认值为任何元素(成分)
        ElementType.TYPE：说明该注解只能被声明在一个类前。
        ElementType.FIELD：说明该注解只能被声明在一个类的字段前。
        ElementType.METHOD：说明该注解只能被声明在一个类的方法前。
        ElementType.PARAMETER：说明该注解只能被声明在一个方法参数前。
        ElementType.CONSTRUCTOR：说明该注解只能声明在一个类的构造方法前。
        ElementType.LOCAL_VARIABLE：说明该注解只能声明在一个局部变量前。
        ElementType.ANNOTATION_TYPE：说明该注解只能声明在一个注解类型前。
        ElementType.PACKAGE：说明该注解只能声明在一个包名前。
       
*/ 
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface MyAnnotation{
    
    //定义基础属性,default为属性指定缺省值(默认值)
    String color() default "blue";
    
    //value属性:如果一个注解中有一个名称为value的属性，且你只想设置value属性(即其他属性都采用默认值或者你只有一个value属性)，那么可以在注解中省略掉“value=”部分。
    String value();
    
    //增加数组类型的属性
    int[] arrayAttr() default {1,2,4};
    
    //增加枚举类型的属性
    EumTrafficLamp lamp() default EumTrafficLamp.RED;
    
    //增加注解类型的属性
    MetaAnnotation annotationAttr() default @MetaAnnotation("xdp");
}
 
public enum EumTrafficLamp {
    RED,//红
    YELLOW,//黄
    GREEN//绿
}

public @interface MetaAnnotation {
    String value();//元注解MetaAnnotation设置有一个唯一的属性value
}
```

### 应用类

```java
@MyAnnotation(color="red",arrayAttr={2,4,5},lamp=EumTrafficLamp.GREEN,annotationAttr=@MetaAnnotation("gacl"))
//@MyAnnotation("xxx")等价于@MyAnnotation(value="xxx")
//如果数组属性只有一个值，这时候属性值部分可以省略大括号，如：@MyAnnotation(arrayAttr=2)
public class AnnotationUse{
    ...
}
```

### 反射类

```java
public class AnnotationReflection{
    public static void main(String[] args) {
        //使用反射检查AnnotationUse类是否有@MyAnnotation注解 
        if (AnnotationUse.class.isAnnotationPresent(MyAnnotation.class)) {  
            //用反射方式获得注解对应的实例对象后，在通过该对象调用属性对应的方法
            MyAnnotation annotation = (MyAnnotation) AnnotationUse.class.getAnnotation(MyAnnotation.class);
            System.out.println(annotation.color());//输出red
            System.out.println(annotation.value());
            System.out.println(annotation.lamp());//这里输出的枚举属性值为：GREEN
            
            MetaAnnotation ma = annotation.annotationAttr();//annotation是MyAnnotation类的一个实例对象
            System.out.println(ma.value());//输出的结果为：gacl
        }
    }
}
```

   

JDK1.5之后内部提供的三个注解

- ​    @Deprecated 意思是“废弃的，过时的”
- ​    @Override 意思是“重写、覆盖”
- ​    @SuppressWarnings 意思是“压缩警告”，作用：用于抑制编译器产生警告信息。

## 枚举类

它是一种特殊的数据类型,它既是一种类(class)类型却又比类类型多了些特殊的约束,类型中可以定义枚举变量，变量值一般是大写的字母，多个值之间以逗号分隔；

```java
//定义周一到周日的变量
public enum Day {
    MONDAY, TUESDAY, WEDNESDAY,THURSDAY, 
    FRIDAY, SATURDAY, SUNDAY
}
```

### 原理

**上述枚举类型Day本质上就是定义多个以枚举变量命名的Day类型**

实际上在使用关键字enum创建枚举类型并编译后，底层代码中编译器会为我们生成一个Day类，这个类继承了Java API中的java.lang.Enum类，类中生成了7个Day类型的实例对象分别对应枚举中定义的7个日期，如上述的MONDAY枚举类型对应public static final Day MONDAY;还为我们生成了两个静态方法，分别是values()和 valueOf()。

### 常用方法

```java
//获取枚举类中的所有变量，并作为数组返回
Day[] days = Day.values();
System.out.println("days:"+Arrays.toString(days));
//days:[MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
 
//根据名称获取枚举变量
Day monday = Day.MONDAY;
Day monday1 = Day.valueOf("MONDAY");
Day monday2 = Day.valueOf(Day.class, "MONDAY");
System.out.println(monday);
//MONDAY
//以上是所有有关Day的方法 
```

​     

```java
//获取的是枚举变量在枚举类中声明的顺序，下标从0开始，如日期中的MONDAY在第一个位置，那么MONDAY的ordinal值就是0
int ordinal = monday.ordinal();
//比较枚举的大小，注意其内部实现是根据每个枚举的ordinal值大小进行比较的。
int i = monday.compareTo(Day.FRIDAY);
//name()方法与toString()几乎是等同的，都是输出变量的字符串形式。
String name = monday.name();
```

​        

### 向enum类添加方法与自定义属性和构造函数

```java
public enum Day2 {
    MONDAY("星期一",1),
    TUESDAY("星期二",2),
    WEDNESDAY("星期三",3),
    THURSDAY("星期四",4),
    FRIDAY("星期五",5),
    SATURDAY("星期六",6),
    SUNDAY("星期日",7);//记住要用分号结束

    private String desc;//文字描述
    private Integer code; //对应的代码

    private Day2(String desc，Integer code){
        this.desc=desc;
 		 this.code=code;
    }

    public String getDesc(){
        return desc;
    }

    public String getCode(){
        return code;
    }
}
 
public static void main(String[] args){
    for (Day2 day:Day2.values()) {
        System.out.println("name:"+day.name()+",desc:"+day.getDesc());
    }
}

/**
 输出结果:
 name:MONDAY,desc:星期一
 name:TUESDAY,desc:星期二
 name:WEDNESDAY,desc:星期三
 name:THURSDAY,desc:星期四
 name:FRIDAY,desc:星期五
 name:SATURDAY,desc:星期六
 name:SUNDAY,desc:星期日
 */
```

## 排序

### Comparator<T>接口

![image-20210902103126846](img_Java%E5%9F%BA%E7%A1%80/image-20210902103126846.png)

实现排序需要重写`int compare(T o1, T o2);`方法

对o1和o2进行排序，o1和o2传入compare方法顺序是o1、o2，根据Comparator.compare(o1, o2)方法的返回值，如果返回的值小于零，则不交换两个o1和o2的位置；如果返回的值大于零，则交换o1和o2的位置；然后根据o1、o2传入值的大小和交换之后的排序，判断是升序或逆序

大小	前-后>0	小大	正序
小大	前-后<0	小大	正序

大小	后-前<0	大小	逆序
小大	后-前>0	大小	逆序

**总之前者减后者是升序，后者减前者是降序**

```java
List<Integer> list = Stream.of(1,2,10).collect(Collectors.toList());
Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                //o1-o2是升序
                //o2-o1是降序
                return o2-o1;
            }
});
System.out.println(list);//[10, 2, 1]
```

### Comparable<T>接口

需要重写`public int compareTo(T o)`方法

**当前值减传入值是升序，传入值减当前值是降序**

#### String默认实现

```java
System.out.println("ASD".compareTo("DAE"));//-3
```

#### 自定义

```java
class myComparable implements Comparable<myComparable>{
        String a;
        String b;
        public String getA() {
            return a;
        }
        public void setA(String a) {
            this.a = a;
        }
        public String getB() {
            return b;
        }
        public void setB(String b) {
            this.b = b;
        }
        myComparable(String a, String b){
            this.a=a;
            this.b=b;
        }
        @Override
        public int compareTo(myComparable o) {
            int result=0;
            result=a.compareTo(o.a);
            if(result==0){
                result=b.compareTo(o.b);
            }
            return result;
        }
        public String toString(){
            return JSONObject.toJSONString(this);
        }
}

public static void main(String[] args) {
        ArrayList<myComparable> list1 = new ArrayList<>();
        list1.add(new myComparable("1","6"));
        list1.add(new myComparable("5","3"));
        list1.add(new myComparable("1","2"));
        Collections.sort(list1);
        System.out.println(list1);
}

//输出：[{"a":"1","b":"2"}, {"a":"1","b":"6"}, {"a":"5","b":"3"}]
```

### Java8排序

```java
//正向排序
list.stream().sorted(Comparator.comparing(Employee::getName)).collect(Collectors.toList());
//反向排序
list.stream().sorted(Comparator.comparingLong(Employee::getId).reversed()).collect(Collectors.toList());
//多个排序
list.stream().sorted(Comparator.comparing((Employee::getName).thenComparing(Employee::getId)).collect(Collectors.toList());
//多个排序时需要指明参数类型Employee否则编译失败
list.stream().sorted(Comparator.comparing((Employee e)->Integer.parseInt(e.getOrderNum()))
                     .thenComparing(Person::getId)).collect(Collectors.toList());                     
```



## 浅拷贝和深拷贝

Java 中的数据类型分为基本数据类型和引用数据类型。对于这两种数据类型，在进行赋值操作、用作方法参数或返回值时，会有值传递和引用（地址）传递的差别。

### 浅拷贝

浅拷贝会创建一个新对象

- 如果属性是基本类型，值传递，拷贝的就是基本类型的值，其中一个对象修改该值，不会影响另外一个。
- 如果属性是内存地址（引用类型），引用传递，拷贝的就是内存地址，因为指向了同一内存空间，所以改变其中一个，会对另外一个也产生影响。

实现对象拷贝的类，需要实现 `Cloneable` 接口，并覆写 `clone()` 方法。

```java
@Data
@AllArgsConstructor
public class Subject {
    
    private String name;

    @Override
    public String toString() {
        return "[Subject: " + this.hashCode() + ",name:" + name + "]";
    }
}
```

```java
@Data
public class Student implements Cloneable {

    private Subject subject;
    private String name;
    private int age;


    /**
     *  重写clone()方法
     * @return
     */
    @Override
    public Object clone() {
        //浅拷贝
        try {
            // 直接调用父类的clone()方法
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[Student: " + this.hashCode() + ",subject:" + subject + ",name:" + name + ",age:" + age + "]";
    }
}
```

```java
public class ShallowCopy {
    public static void main(String[] args) {
        Subject subject = new Subject("yuwen");
        Student studentA = new Student();
        studentA.setSubject(subject);
        studentA.setName("Lynn");
        studentA.setAge(20);
        
        Student studentB = (Student) studentA.clone();
        studentB.setName("Lily");
        studentB.setAge(18);
        Subject subjectB = studentB.getSubject();
        subjectB.setName("lishi");
        
        System.out.println("studentA:" + studentA.toString());
        System.out.println("studentB:" + studentB.toString());
    }
}

输出结果：
studentA:[Student: 460141958,subject:[Subject: 1163157884,name:lishi],name:Lynn,age:20]
studentB:[Student: 1956725890,subject:[Subject: 1163157884,name:lishi],name:Lily,age:18]

浅拷贝创建了新的对象，其中基础数据类型的修改互不影响，而引用类型修改后是会有影响的。
```

#### 对象拷贝

```java
public static void main(String[] args) {
    Subject subject = new Subject("yuwen");
    Student studentA = new Student();
    studentA.setSubject(subject);
    studentA.setName("Lynn");
    studentA.setAge(20);

    Student studentB = studentA;
    studentB.setName("Lily");
    studentB.setAge(18);
    Subject subjectB = studentB.getSubject();
    subjectB.setName("lishi");

    System.out.println("studentA:" + studentA.toString());
    System.out.println("studentB:" + studentB.toString());
}

输出结果：
studentA:[Student: 460141958,subject:[Subject: 1163157884,name:lishi],name:Lily,age:18]
studentB:[Student: 460141958,subject:[Subject: 1163157884,name:lishi],name:Lily,age:18]    

对象拷贝后没有生成新的对象，二者的对象地址完全一样
```

### 深拷贝

深拷贝，在拷贝引用类型成员变量时，为引用类型的数据成员另辟了一个独立的内存空间，实现真正内容上的拷贝。

- 如果属性是基本类型，值传递，拷贝的就是基本类型的值，其中一个对象修改该值，不会影响另外一个。
- 如果属性是引用类型，深拷贝会新建一个对象空间，然后拷贝里面的内容，所以它们指向了不同的内存空间。改变其中一个，不会对另外一个也产生影响。
- 对于有多层对象的，每个对象都需要实现 `Cloneable` 并重写 `clone()` 方法，进而实现了对象的串行层层拷贝。
- 深拷贝相比于浅拷贝速度较慢并且花销较大。

```java
@Data
@AllArgsConstructor
public class Subject implements Cloneable {

    private String name;
    protected Object clone() throws CloneNotSupportedException {
        //Subject 如果也有引用类型的成员属性，也应该和 Student 类一样实现
        return super.clone();
    }
    @Override
    public String toString() {
        return "[Subject: " + this.hashCode() + ",name:" + name + "]";
    }
}
```

```java
@Data
public class Student implements Cloneable {
    private Subject subject;
    private String name;
    private int age;

    public Object clone() {
        //深拷贝
        try {
            // 直接调用父类的clone()方法
            Student student = (Student) super.clone();
            student.subject = (Subject) subject.clone();
            return student;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[Student: " + this.hashCode() + ",subject:" + subject + ",name:" + name + ",age:" + age + "]";
    }
}
```

```java
public class ShallowCopy {
    public static void main(String[] args) {
        Subject subject = new Subject("yuwen");
        Student studentA = new Student();
        studentA.setSubject(subject);
        studentA.setName("Lynn");
        studentA.setAge(20);
        
        Student studentB = (Student) studentA.clone();
        studentB.setName("Lily");
        studentB.setAge(18);
        Subject subjectB = studentB.getSubject();
        subjectB.setName("lishi");
        
        System.out.println("studentA:" + studentA.toString());
        System.out.println("studentB:" + studentB.toString());
    }
}

输出结果：
studentA:[Student: 460141958,subject:[Subject: 1163157884,name:yuwen],name:Lynn,age:20]
studentB:[Student: 1956725890,subject:[Subject: 356573597,name:lishi],name:Lily,age:18]  

深拷贝后，不管是基础数据类型还是引用类型的成员变量，修改其值都不会相互造成影响。
```

参考： [Java浅拷贝和深拷贝](https://www.jianshu.com/p/94dbef2de298)

### CloneUtils

```java
//两个方法
T cloneObject(final T obj)
Object clone(final Object obj)
```

底层也是调用了拷贝对象中的`clone()`方法，所以拷贝对象也需要实现 `Cloneable` 接口，并覆写 `clone()` 方法。

比如拷贝HashMap对象

```java
public static void main(String[] args) throws Exception {
    Subject subject = new Subject("张三");
    Student studentA = new Student();
    studentA.setSubject(subject);
    studentA.setName("Lynn");
    studentA.setAge(20);
    Map<String,Student> mapA=new HashMap<>();
    mapA.put("1",studentA);

    Map<String,Student> mapB = CloneUtils.cloneObject(mapA);
    Student studentB = mapB.get("1");
    studentB.setName("Lily");
    studentB.setAge(18);
    Subject subjectB = studentB.getSubject();
    subjectB.setName("李四");

    System.out.println("studentA:" + studentA.toString());
    System.out.println("studentB:" + studentB.toString());
    /*
    输出结果：
    studentA:[Student: -1223601269,subject:[Subject: 842120,name:李四],name:Lily,age:18]
    studentB:[Student: -1223601269,subject:[Subject: 842120,name:李四],name:Lily,age:18]
    
    mapA和mapB中存放的student是同一个对象
    */

    Student studentC = new Student();
    studentC.setSubject(subject);
    studentC.setName("wangwu");
    studentC.setAge(50);
    mapB.put("2",studentC);
    System.out.println(mapA.get("2"));
    System.out.println(mapB.get("2"));
    /*
    输出结果：
    null
    [Student: -1031784360,subject:[Subject: 842120,name:李四],name:wangwu,age:50]
    
    mapA和mapB是不同的对象
    */
}
```

```java
//HashMap默认只能浅拷贝，否则需要重写HashMap中的clone()方法
//HashMap相关源码
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
    @Override
    public Object clone() {
        HashMap<K,V> result;
        try {
            result = (HashMap<K,V>)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }
}
```



## Java序列化和反序列化

### @JsonDeserialize和@JsonSerialize

使用 @JsonDeserialize 和 @JsonSerialize 注解在对象序列化和反序列化时对参数进行处理

- **@JsonDeserialize**
  - 是在反序列化时，所以就是对参数进行封装，使用的是 setXxxx() 方法，所以需要将注解添加到对应的 set 方法上，若使用了 Lombok 需要自己定义相应的 set 方法。
  - 需要使用 using 属性指定处理参数的类，该类需要继承 JsonDeserializer 类，并重写 deserialize()。

- **@JsonSerialize**
  - 是在序列化时，所以需要获取数据，那么需要使用到 getXxxx() 方法，故需要将注解添加到对应的 get 方法上，若使用了 Lombok 需要自己定义相应的 get 方法。
  - 需要使用 using 属性指定处理参数的类，该类需要继承 JsonSerializer 类，并重写 serialize()。



> 在前端性别显示“男 / 女”，而数据库中存储的是“1 / 0”，对应的 Pojo 也是使用的 Integer 类型，如何实现？

#### Pojo 类

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = 4346092911489022673L;

    private Integer id;
    private String name;
    private Integer age;

    /**
     * 1 男，0 女
     */
    private Integer gender;

    @JsonDeserialize(using = GenderJsonDeserializer.class)
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @JsonSerialize(using = GenderJsonSerializer.class)
    public Integer getGender() {
        return gender;
    }
}
```

#### GenderJsonDeserializer 类

其作用是处理参数，按照规则封装到指定的属性中，通过 p.getText() 获取参数。

```java
@Component
@Slf4j
public class GenderJsonDeserializer extends JsonDeserializer {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
            
        if (ObjectUtils.isEmpty(jsonParser)) {
            return null;
        }

        int gender = 0;

        switch (jsonParser.getText()) {
            case "男":
                gender = 1;
                break;
            case "女":
                break;
            default:
                throw new RuntimeException("传入的性别为非法字符！");
        }

        log.info("【 GenderJsonDeserializer.deserialize() 】  jsonParser.getText() ==> " 
        				+ jsonParser.getText() + "，转换后的结果 ==> " + gender);

        return gender;
    }
}
```

#### GenderJsonSerializer 类

其作用是处理属性，按照规则封装到指定的参数中，通过value 获取属性，通过 gen.writeXxx() 方法写出参数。

```java
@Component
@Slf4j
public class GenderJsonSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) 
    		throws IOException {
    		
        log.info("【 GenderJsonSerializer.serialize() 】  value ==> " + value);
        if (value.equals(1)) {
            gen.writeString("男");
        } else if (value.equals(0)) {
            gen.writeString("女");
        }
    }
}
```

#### PersonController 类

```java
@RestController
@Slf4j
public class PersonController {
	// 使用集合模拟数据库中数据存储
    private List<Person> persons = new ArrayList<>();
	
	// 用于初始化数据，@PostConstruct 注解标注的方法，在构造器执行之后自动执行，只会执行一次
    @PostConstruct
    public void init() {
        persons.add(new Person(1, "张三", 18, 1));
        persons.add(new Person(2, "李四", 33, 0));
    }

    @PostMapping("/save")
    public Person savePerson(@RequestBody Person person) {
        log.info("【 PersonController.savePerson() 】  person ===> " + person);
        persons.add(person);
        log.info("集合内容为 ===> " + persons);
        return person;
    }

    @GetMapping("/find")
    public Person findPersonById(Integer id) {
        Person p = null;
        for (Person person : persons) {
           if (person.getId().equals(id)) {
               p = person;
               log.info("【 PersonController.findPersonById() 】  查询结果为：person ===> " + person);
           }
        }
        return p;
    }
}
```

**测试**

<img src="img_Java%E5%9F%BA%E7%A1%80/20200713181102333.png" alt="在这里插入图片描述" style="zoom:80%;" />

控制台输出：

```java
【 GenderJsonDeserializer.deserialize() 】  p.getText() ==> 男，转换后的结果 ==> 1

【 PersonController.savePerson() 】  person ===> Person(id=3, name=王五, age=88, gender=1)

集合内容为 ===> [Person(id=1, name=张三, age=18, gender=1),
							 Person(id=2, name=李四, age=33, gender=0), 
							 Person(id=3, name=王五, age=88, gender=1)]
							 
【 GenderJsonSerializer.serialize() 】  value ==> 1
```

<img src="img_Java%E5%9F%BA%E7%A1%80/20200713181325537.png" alt="在这里插入图片描述" style="zoom:80%;" />



控制台输出：

```java
【 PersonController.findPersonById() 】  查询结果为：person ===> Person(id=2, name=李四, age=33, gender=0)

【 GenderJsonSerializer.serialize() 】  value ==> 0
```

