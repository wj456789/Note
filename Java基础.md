# Java基础

## static关键字

方便在没有创建对象的情况下来进行调用（方法/变量），只要类被加载了，就可以通过类名去进行访问，不需要依赖于对象来进行访问。

static可以用来修饰类的成员方法、类的成员变量，另外可以编写static代码块来优化程序性能。

### static方法

静态方法没有对象，没有this，在静态方法中不能访问类的非静态成员变量和非静态成员方法，因为非静态成员方法/变量都是必须依赖具体的对象才能够被调用。但是在非静态成员方法中是可以访问静态成员方法/变量的。

### static变量

静态变量当且仅当在类初次加载时会被初始化，在内存中只有一个副本，被所有的对象所共享

### static代码块

静态块可以置于类中的任何地方，类中可以有多个static块。在类初次被加载的时候，会按照static块的顺序来执行每个static块，并且只会执行一次。

**PS:**

- 静态成员变量虽然独立于所在的对象，但是不代表不可以通过对象去访问，所有的静态方法和静态变量都可以通过所在类的实例对象访问（只要访问权限足够）。
- static是不允许用来修饰局部变量

## final关键字

- 修饰变量，被final修饰的变量必须要初始化，赋初值后不能再重新赋值，即变量被初始化后便不可改变。
  - 注意：局部变量不在我们讨论的范畴，因为局部变量本身就有作用范围，不使用private、public等词修饰。虽然final可以修饰局部变量，但是一般情况下，我们在实际应用中几乎不会这么干！没多大的意义！
  - 如果final修饰的变量是对象类型，那么不可更改指的是该变量不可以再指向别的对象，但是对象的值是可以更改的

- 修饰方法，被final修饰的方法代表不能重写。

- 修饰类，被final修饰的类，不能够被继承。

  注意：final修饰的类，类中的所有成员方法都被隐式地指定为final方法。

## 集合

Collection 接口的接口 对象的集合（单列集合）
├——-List 接口：元素按进入先后有序保存，可重复
│—————-├ LinkedList 接口实现类， 链表， 插入删除， 没有同步， 线程不安全
│—————-├ ArrayList 接口实现类， 数组， 随机访问， 没有同步， 线程不安全
│—————-└ Vector 接口实现类 数组， 同步， 线程安全
│ ———————-└ Stack 是Vector类的实现类
└——-Set 接口： 仅接收一次，不可重复，并做内部排序
├—————-└HashSet 使用hash表（数组）存储元素，无序
│————————└ LinkedHashSet 链表维护元素的插入次序
└ —————-TreeSet 底层实现为二叉树，元素排好序

Map 接口 键值对的集合 （双列集合）
├———Hashtable 接口实现类， 同步， 线程安全，不能放null
├———HashMap 接口实现类 ，没有同步， 线程不安全，key或value可以为null，key最多只能有一个null
│—————–├ LinkedHashMap 双向链表和哈希表实现
│—————–└ WeakHashMap
├ ——–TreeMap 红黑树对所有的key进行排序
└———IdentifyHashMap

Vector与ArrayList一样，也是通过数组实现的，不同的是它支持线程的同步，并且当Vector或ArrayList中的元素超过它的初始大小时，Vector会将容量翻倍，ArrayList只增加50%的大小。

参考：[java集合超详解](https://blog.csdn.net/feiyanaffection/article/details/81394745)

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

参考：[Java ConcurrentModificationException异常原因和解决方法](https://www.cnblogs.com/zhuyeshen/p/10956822.html)

## 变量

类中定义的变量称为成员变量，成员变量分为静态变量(全局变量)和实例变量，方法中定义的变量称为局部变量

## 抽象类和接口

抽象类和接口的作用就是让子类继承，在子类中根据具体情况去实现抽象方法。

- 抽象类并不是只能包含抽象方法，他也可以包含普通的成员方法和成员变量、构造方法、静态方法、静态成员变量。接口中只能有抽象方法和静态成员变量；
  - 抽象类中的抽象方法只能用public或protected修饰以便子类继承重写。接口中的抽象方法只能是 public 类型的，并且默认即为 public abstract 类型。
  - 其中抽象类中的静态成员变量的访问类型可以任意，但接口中定义的变量只是public static final 类型，并且默认即为 public static final 类型。

- 一个类可以实现多个接口，但只能继承一个抽象类；
- 抽象类和接口不能创建对象，即不能实例化；

- 继承抽象类的子类和实现接口的子类必须实现所有抽象方法，否则子类也是抽象类；

- 在jdk1.8以后增加default方法。default修饰的方法是可以在接口的实现类中不实现的。



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

​    

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