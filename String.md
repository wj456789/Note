# String

## String.format()

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
//输出：
Hi,12 张三 李四
```

输出：
Hi,12 张三 李四

参考：[String.format()的详细用法](https://blog.csdn.net/anita9999/article/details/82346552)

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

参考：[Java学习笔记之Pattern类的用法详解(正则表达式)](https://www.cnblogs.com/sparkbj/articles/6207103.html)


















