# Java基础

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
public class AnnotationUse{}
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

### 使用序列化的方式来实现对象的深拷贝

对象必须是实现了Serializable接口才可以。比如Map本身没有实现 Serializable 这个接口，所以这种方式不能序列化Map，也就是不能深拷贝Map。但是HashMapMap实现 Serializable 这个接口，所以可以通过HashMap深拷贝。

```java
HashMap<String,Object> map = new HashMap<String,Object>();
HashMap<String,Object> mapNew = new HashMap<String,Object>();
mapNew = CloneUtils.clone(map);
```

