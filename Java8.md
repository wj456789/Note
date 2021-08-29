# Java8

- **Lambda 表达式** − Lambda 允许把函数作为一个方法的参数（函数作为参数传递到方法中）。
- **函数式接口 −** 支持 lambda表达式调用的接口
- **方法引用 −** 方法引用提供了非常有用的语法，可以直接引用已有Java类或对象（实例）的方法或构造器。与lambda联合使用，方法引用可以使语言的构造更紧凑简洁，减少冗余代码。

- **默认方法 −** 默认方法就是一个在接口里面有了一个实现的方法。
- **Stream API −**新添加的Stream API（java.util.stream） 把真正的函数式编程风格引入到Java中。
- **Date Time API −** 加强对日期与时间的处理。
- **Optional 类 −** Optional 类已经成为 Java 8 类库的一部分，用来解决空指针异常。

## Lamdba表达式

- Lambda 允许把函数作为一个方法的参数（函数作为参数传递到方法中）。
- 使用 Lambda 表达式对特殊接口进行简单的实现前提是接口中有且只有一个必须需要被实现的抽象方法。

### 基础语法

语法形式为 `() -> {}`，其中` ()` 用来描述参数列表，`{} `用来描述方法体，`->` 为 lambda运算符 ，读作(goes to)。

**Lambda表达式返回：实现接口的匿名内部类的实例对象**

### 实现

```java
/**无参无返回值*/
@FunctionalInterface
public interface NoReturnNoParam {
    void method();
} 

/**一个参数无返回*/
@FunctionalInterface
public interface NoReturnOneParam {
    void method(int a);
}

/**多参数无返回*/
@FunctionalInterface
public interface NoReturnMultiParam {
    void method(int a, int b);
}


/*** 无参有返回*/
@FunctionalInterface
public interface ReturnNoParam {
    int method();
}

/**一个参数有返回值*/
@FunctionalInterface
public interface ReturnOneParam {
    int method(int a);
}

/**多个参数有返回值*/
@FunctionalInterface
public interface ReturnMultiParam {
    int method(int a, int b);
}
```

```java
public class Test1 {
    public static void main(String[] args) {

        NoReturnNoparam noReturnNoParam = new NoReturnNoparam(){
            public void method(){
                ...
            }
        }
        
        //无参无返回
        NoReturnNoParam noReturnNoParam = () -> {
            System.out.println("NoReturnNoParam");
        };
        noReturnNoParam.method();

        //一个参数无返回
        NoReturnOneParam noReturnOneParam = (int a) -> {
            System.out.println("NoReturnOneParam param:" + a);
        };
        noReturnOneParam.method(6);

        //多个参数无返回
        NoReturnMultiParam noReturnMultiParam = (int a, int b) -> {
            System.out.println("NoReturnMultiParam param:" + "{" + a +"," + + b +"}");
        };
        noReturnMultiParam.method(6, 8);

        //无参有返回值
        ReturnNoParam returnNoParam = () -> {
            System.out.print("ReturnNoParam");
            return 1;
        };

        int res = returnNoParam.method();
        System.out.println("return:" + res);

        //一个参数有返回值
        ReturnOneParam returnOneParam = (int a) -> {
            System.out.println("ReturnOneParam param:" + a);
            return 1;
        };

        int res2 = returnOneParam.method(6);
        System.out.println("return:" + res2);

        //多个参数有返回值
        ReturnMultiParam returnMultiParam = (int a, int b) -> {
            System.out.println("ReturnMultiParam param:" + "{" + a + "," + b +"}");
            return 1;
        };

        int res3 = returnMultiParam.method(6, 8);
        System.out.println("return:" + res3);
    }
}
```

### 语法简化

```java
public class Test2 {
    public static void main(String[] args) {

        //1.简化参数类型，可以不写参数类型，但是必须所有参数都不写
        NoReturnMultiParam lamdba1 = (a, b) -> {
            System.out.println("简化参数类型");
        };
        lamdba1.method(1, 2);

        //2.简化参数小括号，如果只有一个参数则可以省略参数小括号
        NoReturnOneParam lambda2 = a -> {
            System.out.println("简化参数小括号");
        };
        lambda2.method(1);

        //3.简化方法体大括号，如果方法条只有一条语句，则可以省略方法体大括号
        NoReturnNoParam lambda3 = () -> System.out.println("简化方法体大括号");
        lambda3.method();

        //4.如果方法体只有一条语句，并且是 return 语句，则不仅可以省略方法体大括号,而且连return都可以省略
        ReturnOneParam lambda4 = a -> a+3;
        System.out.println(lambda4.method(5));

        ReturnMultiParam lambda5 = (a, b) -> a+b;
        System.out.println(lambda5.method(1, 1));
    }
}
```

### 变量作用域

Lambda表达式可以引用但是不能修改外部的局部变量(必须使用final修饰)；可以引用并修改外部不用final修饰的成员变量。

```java
//lambda 表达式只能引用标记了 final 的外层局部变量，不能在 lambda 内部修改定义在作用域外的局部变量，否则会编译错误。同时在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量。
public class Java8Tester {
    public static void main(String args[]) {
        final int num = 1;  //这里的final 写不写，Lambda表达式里都能引用，没写的化，内部默认你写了的
        //num这里调用可以，但不能改
        Converter<Integer, String> s = (param) -> System.out.println(String.valueOf(param + num));
        s.convert(2);  // 输出结果为 3
    }
 
    public interface Converter<T1, T2> {
        void convert(int i);
    }
}



public class Tester {
   static int salutation = 123;
   public static void main(String args[]){
      GreetingService greetService1 = message ->  {
          ////salutation这里可以改
          salutation++;
          System.out.println(salutation + message)
      };  
      greetService1.sayMessage("Runoob");
   }
   interface GreetingService {
      void sayMessage(String message);
   }
}




String first = "";  
Comparator<String> comparator = (first, second) -> Integer.compare(first.length(), second.length());  //编译会出错 
```

### 应用

```java
//lambda 表达式创建线程
//我们以往都是通过创建 Thread 对象，然后通过匿名内部类重写 run() 方法，一提到匿名内部类我们就应该想到可以使用 lambda 表达式来简化线程的创建过程。
Thread t = new Thread(() -> {
   for (int i = 0; i < 10; i++) {
      System.out.println(2 + ":" + i);
   }
});
t.start();
```



## 函数式接口

函数式接口指的是**有且只有一个未实现的方法的接口**，函数式接口是Java支持函数式编程的基础。

说白了就是满足前面lambda表达式对接口的要求的接口就是函数式接口。

### @FunctionalInterface

这个注解是专门来修饰函数式接口的，要求接口中的抽象方法只有一个。 

```java
@FunctionalInterface
interface GreetingService 
{
    void sayMessage(String message);
}
```

### 函数式接口

**JDK 1.8 之前已有的函数式接口:**	

- java.lang.Runnable
- java.util.concurrent.Callable
- java.security.PrivilegedAction
- java.util.Comparator
- java.io.FileFilter
- java.nio.file.PathMatcher
- java.lang.reflect.InvocationHandler
- java.beans.PropertyChangeListener
- java.awt.event.ActionListener
- javax.swing.event.ChangeListener

**JDK 1.8 新增加的函数接口：**

包java.util.function 专门用来支持 Java的函数式编程，该包中的函数式接口有：

| 序号 | 接口 & 描述                                                  |
| ---- | ------------------------------------------------------------ |
| 1    | BiConsumer<T,U>代表了一个接受两个输入参数的操作，并且不返回任何结果 |
| 2    | BiFunction<T,U,R>代表了一个接受两个输入参数的方法，并且返回一个结果 |
| 3    | BinaryOperator<T>代表了一个作用于于两个同类型操作符的操作，并且返回了操作符同类型的结果 |
| 4    | BiPredicate<T,U>代表了一个两个参数的boolean值方法            |
| 5    | BooleanSupplier代表了boolean值结果的提供方                   |
| 6    | Consumer<T>代表了接受一个输入参数并且无返回的操作            |
| 7    | DoubleBinaryOperator代表了作用于两个double值操作符的操作，并且返回了一个double值的结果。 |
| 8    | DoubleConsumer代表一个接受double值参数的操作，并且不返回结果。 |
| 9    | DoubleFunction<R>代表接受一个double值参数的方法，并且返回结果 |
| 10   | DoublePredicate代表一个拥有double值参数的boolean值方法       |
| 11   | DoubleSupplier代表一个double值结构的提供方                   |
| 12   | DoubleToIntFunction接受一个double类型输入，返回一个int类型结果。 |
| 13   | DoubleToLongFunction接受一个double类型输入，返回一个long类型结果 |
| 14   | DoubleUnaryOperator接受一个参数同为类型double,返回值类型也为double 。 |
| 15   | Function<T,R>接受一个输入参数，返回一个结果。                |
| 16   | IntBinaryOperator接受两个参数同为类型int,返回值类型也为int 。 |
| 17   | IntConsumer接受一个int类型的输入参数，无返回值 。            |
| 18   | IntFunction<R>接受一个int类型输入参数，返回一个结果 。       |
| 19   | IntPredicate：接受一个int输入参数，返回一个布尔值的结果。    |
| 20   | IntSupplier无参数，返回一个int类型结果。                     |
| 21   | IntToDoubleFunction接受一个int类型输入，返回一个double类型结果 。 |
| 22   | IntToLongFunction接受一个int类型输入，返回一个long类型结果。 |
| 23   | IntUnaryOperator接受一个参数同为类型int,返回值类型也为int 。 |
| 24   | LongBinaryOperator接受两个参数同为类型long,返回值类型也为long。 |
| 25   | LongConsumer接受一个long类型的输入参数，无返回值。           |
| 26   | LongFunction<R>接受一个long类型输入参数，返回一个结果。      |
| 27   | LongPredicateR接受一个long输入参数，返回一个布尔值类型结果。 |
| 28   | LongSupplier无参数，返回一个结果long类型的值。               |
| 29   | LongToDoubleFunction接受一个long类型输入，返回一个double类型结果。 |
| 30   | LongToIntFunction接受一个long类型输入，返回一个int类型结果。 |
| 31   | LongUnaryOperator接受一个参数同为类型long,返回值类型也为long。 |
| 32   | ObjDoubleConsumer<T>接受一个object类型和一个double类型的输入参数，无返回值。 |
| 33   | ObjIntConsumer<T>接受一个object类型和一个int类型的输入参数，无返回值。 |
| 34   | ObjLongConsumer<T>接受一个object类型和一个long类型的输入参数，无返回值。 |
| 35   | Predicate<T>接受一个输入参数，返回一个布尔值结果。           |
| 36   | Supplier<T>无参数，返回一个结果。                            |
| 37   | ToDoubleBiFunction<T,U>接受两个输入参数，返回一个double类型结果 |
| 38   | ToDoubleFunction<T>接受一个输入参数，返回一个double类型结果  |
| 39   | ToIntBiFunction<T,U>接受两个输入参数，返回一个int类型结果。  |
| 40   | ToIntFunction<T>接受一个输入参数，返回一个int类型结果。      |
| 41   | ToLongBiFunction<T,U>接受两个输入参数，返回一个long类型结果。 |
| 42   | ToLongFunction<T>接受一个输入参数，返回一个long类型结果。    |
| 43   | UnaryOperator<T>接受一个参数为类型T,返回值类型也为T。        |

### 实例

#### Function接口示例

Function作为一个函数式接口，主要方法apply接收一个参数，返回一个值； 除apply方法外，它还有compose与andThen及indentity三个方法

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
```

```java
public static void functionTest() {

    //一个输入与一个输出，我们可以计算一个数的很多运算
    Function<Integer, Integer> f = s -> s+1;
    Function<Integer, Integer> g = s -> s * 2;

    /**
     * 下面表示在执行F时，先执行G，并且执行F时使用G的输出当作输入。
     * 相当于以下代码：
     * Integer a = g.apply(1);
     * System.out.println(f.apply(a));
     */
    System.out.println(f.compose(g).apply(1));

    /**
     * 表示执行F的Apply后使用其返回的值当作输入再执行G的Apply；
     * 相当于以下代码
     * Integer a = f.apply(1);
     * System.out.println(g.apply(a));
     */
    System.out.println(f.andThen(g).apply(1));

    /**
     * identity方法会返回一个不进行任何处理的Function，即输出与输入值相等； 
     */
    System.out.println(Function.identity().apply("a"));
    }
｝
```

#### BiFunction接口示例

BiFunction的apply方法，接收两个参数，返回一个值

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
    
    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u) -> after.apply(apply(t, u));
    }
}
```

```java
public int compute3(int a, int b, BiFunction<Integer, Integer, Integer> biFunction) {
   return biFunction.apply(a, b);
}
//我们定义了一个方法，可以用来计算两个数的很多运算
test.compute3(2, 3, (v1, v2) -> v1 + v2) //5
test.compute3(2, 3, (v1, v2) -> v1 - v2) //-1
test.compute3(2, 3, (v1, v2) -> v1 * v2) //6

//BiFunction中有一个andThen方法，参数是一个Function，方法主要是将BiFunction返回的结果作为Function的参数，得出一个结果，举例：
public int compute4(int a, int b, BiFunction<Integer, Integer, Integer> biFunction,Function<Integer, Integer> function) {
    return biFunction.andThen(function).apply(a, b);
}

test.compute4(2, 3, (v1, v2) -> v1 + v2, v1 -> v1 * v1) //25
```







