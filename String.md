# String

## 常用方法

```java
boolean matches(String regex)			//正则比较
char[] toCharArray();					//生成字符数组
int indexOf(String str, int fromIndex)		//从fromIndex位置开始往后找到第一个str所在整个字符串位置
int lastIndexOf(String str, int fromIndex)	//从fromIndex位置开始往前找到第一个str所在整个字符串位置
```

## String方法

### String.format()

`format(String format, Object… args) `新字符串使用本地语言环境，制定字符串格式和参数生成格式化的新字符串。
`format(Locale locale, String format, Object… args)` 使用指定的语言环境，制定字符串格式和参数生成格式化的字符串。

| 转换符 | 详细说明           | 示例   |
| ------ | ------------------ | ------ |
| %s     | 字符串类型         | "小明" |
| %c     | 字符类型           | 'm'    |
| %b     | 布尔类型           | true   |
| %d     | 整数类型（十进制） | 88     |
| %f     | 浮点类型           | 8.888  |

```java
System.out.println(String.format("Hi,%d %s %s",12,"张三","李四"));
System.out.println("Hi,%d %s %s",12,"张三","李四");
System.out.println(String.format("%8s","asd"));

输出：
Hi,12 张三 李四
Hi,12 张三 李四    
     asd		//空格和字符总长度为8
```

输出：
Hi,12 张三 李四

参考：[String.format()的详细用法](https://blog.csdn.net/anita9999/article/details/82346552)



### String.join()

```java
public static void main(String[] args) {
    Stack<String> path = new Stack<>();
    path.push("1");
    path.push("2");
    path.push("3");
    path.push("4");
    System.out.println(("/" + String.join("/", path)));

    Deque<String> deque=new LinkedList<>();
    deque.push("1");
    deque.push("2");
    deque.push("3");
    deque.push("4");
    deque.offer("5");
    deque.offer("6");
    deque.offer("7");
    deque.offer("8");
    System.out.println(("/" + String.join("/", deque)));
}

输出：
/1/2/3/4
/4/3/2/1/5/6/7/8
```

如果栈和队列都使用数组实现底层，代码插入：

**栈：**

s[0] s[1] s[2] s[3]

1	2	3	4

本质push是在数组尾部插入

**队列：**

d[0] d[1] d[2] d[3] d[4] d[5] d[6] d[7]

4	3	2	1	5	6	7	8

本质push是在数组头部插入，offer在数组尾部插入

**String.join()**

是从数组头部链接到尾部





## StringUtils

StringUtils类中isEmpty与isBlank的区别
`org.apache.commons.lang.StringUtils`类提供了String的常用操作,最为常用的判空有如下两种isEmpty(String str)和isBlank(String str)。

**StringUtils.isEmpty(String str)** 

```java
//判断某字符串是否为空，为空的标准是 str==null 或 str.length()==0
System.out.println(StringUtils.isEmpty(null));        //true
System.out.println(StringUtils.isEmpty(""));          //true
System.out.println(StringUtils.isEmpty("   "));       //false
System.out.println(StringUtils.isEmpty("dd"));        //false

StringUtils.isNotEmpty(String str) 等价于 !isEmpty(String str)
```

**StringUtils.isBlank(String str)** 

```java
//判断某字符串是否为空或长度为0或由空白符(whitespace) 构成
System.out.println(StringUtils.isBlank(null));        //true
System.out.println(StringUtils.isBlank(""));          //true
System.out.println(StringUtils.isBlank("   "));       //true
System.out.println(StringUtils.isBlank("dd"));        //false    

StringUtils.isNotBlank(String str) 等价于 !isBlank(String str)
```

## 正则匹配

```java
//将给定的正则表达式编译到模式中，可以重用已编译的模式
Pattern pattern = Pattern.compile("正则表达式");
//给定输入到此模式的匹配器
Matcher matcher = pattern.matcher("匹配字符串");
boolean flag = matcher.matches();

//仅使用一次正则表达式
boolean flag = Pattern.matches("正则表达式", "匹配字符串");
```



















