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

## Gson

使用Gson解析json成对象时默认的是将json里对应字段的值解析到java对象里对应字段的属性里面。 可以使用@SerializedName注解来将对象里的属性跟json里字段对应值匹配起来。 

```java
json数据如下：
{
    "id":"1"
    "n":"kyoya"
    "p":"123456"
    "s":"0"
}

public class User{
    private String id;
 
    @SerializedName("n")
    private String userName;
 
    @SerializedName("p")
    private String password;
 
    @SerializedName("s")
    private String sex;
}
// 使用Gson解析json字符串或使用Gson生成json字符串都可以相互匹配
```

# 正则表达式

| 构造                                           | 匹配                                                         |
| ---------------------------------------------- | ------------------------------------------------------------ |
|                                                |                                                              |
| 字符                                           |                                                              |
| *x*                                            | 字符 *x*                                                     |
| `\\`                                           | 反斜线字符                                                   |
| `\0`*n*                                        | 带有八进制值 `0` 的字符 *n* (0 `<=` *n* `<=` 7)              |
| `\0`*nn*                                       | 带有八进制值 `0` 的字符 *nn* (0 `<=` *n* `<=` 7)             |
| `\0`*mnn*                                      | 带有八进制值 `0` 的字符 *mnn*（0 `<=` *m* `<=` 3、0 `<=` *n* `<=` 7） |
| `\x`*hh*                                       | 带有十六进制值 `0x` 的字符 *hh*                              |
| `\u`*hhhh*                                     | 带有十六进制值 `0x` 的字符 *hhhh*                            |
| `\t`                                           | 制表符 (`'\u0009'`)                                          |
| `\n`                                           | 新行（换行）符 (`'\u000A'`)                                  |
| `\r`                                           | 回车符 (`'\u000D'`)                                          |
| `\f`                                           | 换页符 (`'\u000C'`)                                          |
| `\a`                                           | 报警 (bell) 符 (`'\u0007'`)                                  |
| `\e`                                           | 转义符 (`'\u001B'`)                                          |
| `\c`*x*                                        | 对应于 *x* 的控制符                                          |
|                                                |                                                              |
| 字符类                                         |                                                              |
| `[abc]`                                        | `a`、`b` 或 `c`（简单类）                                    |
| `[^abc]`                                       | 任何字符，除了 `a`、`b` 或 `c`（否定）                       |
| `[a-zA-Z]`                                     | `a` 到 `z` 或 `A` 到 `Z`，两头的字母包括在内（范围）         |
| `[a-d[m-p]]`                                   | `a` 到 `d` 或 `m` 到 `p`：`[a-dm-p]`（并集）                 |
| `[a-z&&[def]]`                                 | `d`、`e` 或 `f`（交集）                                      |
| `[a-z&&[^bc]]`                                 | `a` 到 `z`，除了 `b` 和 `c`：`[ad-z]`（减去）                |
| `[a-z&&[^m-p]]`                                | `a` 到 `z`，而非 `m` 到 `p`：`[a-lq-z]`（减去）              |
|                                                |                                                              |
| 预定义字符类                                   |                                                              |
| `.`                                            | 任何字符（与行结束符可能匹配也可能不匹配）                   |
| `\d`                                           | 数字：`[0-9]`                                                |
| `\D`                                           | 非数字： `[^0-9]`                                            |
| `\s`                                           | 空白字符：`[ \t\n\x0B\f\r]`                                  |
| `\S`                                           | 非空白字符：`[^\s]`                                          |
| `\w`                                           | 单词字符：`[a-zA-Z_0-9]`                                     |
| `\W`                                           | 非单词字符：`[^\w]`                                          |
|                                                |                                                              |
| POSIX 字符类（仅 US-ASCII）                    |                                                              |
| `\p{Lower}`                                    | 小写字母字符：`[a-z]`                                        |
| `\p{Upper}`                                    | 大写字母字符：`[A-Z]`                                        |
| `\p{ASCII}`                                    | 所有 ASCII：`[\x00-\x7F]`                                    |
| `\p{Alpha}`                                    | 字母字符：`[\p{Lower}\p{Upper}]`                             |
| `\p{Digit}`                                    | 十进制数字：`[0-9]`                                          |
| `\p{Alnum}`                                    | 字母数字字符：`[\p{Alpha}\p{Digit}]`                         |
| `\p{Punct}`                                    | 标点符号：`!"#$%&'()*+,-.\:;<=>?@[\]^_`{                     |
| `\p{Graph}`                                    | 可见字符：`[\p{Alnum}\p{Punct}]`                             |
| `\p{Print}`                                    | 可打印字符：`[\p{Graph}\x20]`                                |
| `\p{Blank}`                                    | 空格或制表符：`[ \t]`                                        |
| `\p{Cntrl}`                                    | 控制字符：`[\x00-\x1F\x7F]`                                  |
| `\p{XDigit}`                                   | 十六进制数字：`[0-9a-fA-F]`                                  |
| `\p{Space}`                                    | 空白字符：`[ \t\n\x0B\f\r]`                                  |
|                                                |                                                              |
| java.lang.Character 类（简单的 java 字符类型） |                                                              |
| `\p{javaLowerCase}`                            | 等效于 java.lang.Character.isLowerCase()                     |
| `\p{javaUpperCase}`                            | 等效于 java.lang.Character.isUpperCase()                     |
| `\p{javaWhitespace}`                           | 等效于 java.lang.Character.isWhitespace()                    |
| `\p{javaMirrored}`                             | 等效于 java.lang.Character.isMirrored()                      |
|                                                |                                                              |
| Unicode 块和类别的类                           |                                                              |
| `\p{InGreek}`                                  | Greek 块（简单块）中的字符                                   |
| `\p{Lu}`                                       | 大写字母（简单类别）                                         |
| `\p{Sc}`                                       | 货币符号                                                     |
| `\P{InGreek}`                                  | 所有字符，Greek 块中的除外（否定）                           |
| `[\p{L}&&[^\p{Lu}]] `                          | 所有字母，大写字母除外（减去）                               |
|                                                |                                                              |
| 边界匹配器                                     |                                                              |
| `^`                                            | 行的开头                                                     |
| `$`                                            | 行的结尾                                                     |
| `\b`                                           | 单词边界                                                     |
| `\B`                                           | 非单词边界                                                   |
| `\A`                                           | 输入的开头                                                   |
| `\G`                                           | 上一个匹配的结尾                                             |
| `\Z`                                           | 输入的结尾，仅用于最后的结束符（如果有的话）                 |
| `\z`                                           | 输入的结尾                                                   |
|                                                |                                                              |
| Greedy 数量词                                  |                                                              |
| *X*`?`                                         | *X*，一次或一次也没有                                        |
| *X*`*`                                         | *X*，零次或多次                                              |
| *X*`+`                                         | *X*，一次或多次                                              |
| *X*`{`*n*`}`                                   | *X*，恰好 *n* 次                                             |
| *X*`{`*n*`,}`                                  | *X*，至少 *n* 次                                             |
| *X*`{`*n*`,`*m*`}`                             | *X*，至少 *n* 次，但是不超过 *m* 次                          |
|                                                |                                                              |
| Reluctant 数量词                               |                                                              |
| *X*`??`                                        | *X*，一次或一次也没有                                        |
| *X*`*?`                                        | *X*，零次或多次                                              |
| *X*`+?`                                        | *X*，一次或多次                                              |
| *X*`{`*n*`}?`                                  | *X*，恰好 *n* 次                                             |
| *X*`{`*n*`,}?`                                 | *X*，至少 *n* 次                                             |
| *X*`{`*n*`,`*m*`}?`                            | *X*，至少 *n* 次，但是不超过 *m* 次                          |
|                                                |                                                              |
| Possessive 数量词                              |                                                              |
| *X*`?+`                                        | *X*，一次或一次也没有                                        |
| *X*`*+`                                        | *X*，零次或多次                                              |
| *X*`++`                                        | *X*，一次或多次                                              |
| *X*`{`*n*`}+`                                  | *X*，恰好 *n* 次                                             |
| *X*`{`*n*`,}+`                                 | *X*，至少 *n* 次                                             |
| *X*`{`*n*`,`*m*`}+`                            | *X*，至少 *n* 次，但是不超过 *m* 次                          |
|                                                |                                                              |
| Logical 运算符                                 |                                                              |
| *XY*                                           | *X* 后跟 *Y*                                                 |
| *X*`|`*Y*                                      | *X* 或 *Y*                                                   |
| `(`*X*`)`                                      | X，作为捕获组                                                |
|                                                |                                                              |
| Back 引用                                      |                                                              |
| `\`*n*                                         | 任何匹配的 *n*th 捕获组                                      |
|                                                |                                                              |
| 引用                                           |                                                              |
| `\`                                            | Nothing，但是引用以下字符                                    |
| `\Q`                                           | Nothing，但是引用所有字符，直到 `\E`                         |
| `\E`                                           | Nothing，但是结束从 `\Q` 开始的引用                          |
|                                                |                                                              |
| 特殊构造（非捕获）                             |                                                              |
| `(?:`*X*`)`                                    | *X*，作为非捕获组                                            |
| `(?idmsux-idmsux) `                            | Nothing，但是将匹配标志i d m s u x on - off                  |
| `(?idmsux-idmsux:`*X*`)`                       | *X*，作为带有给定标志 i d m s u x on - off                   |
| `(?=`*X*`)`                                    | *X*，通过零宽度的正 lookahead                                |
| `(?!`*X*`)`                                    | *X*，通过零宽度的负 lookahead                                |
| `(?<=`*X*`)`                                   | *X*，通过零宽度的正 lookbehind                               |
| `(?<!`*X*`)`                                   | *X*，通过零宽度的负 lookbehind                               |
| `(?>`*X*`)`                                    | *X*，作为独立的非捕获组                                      |

参考：[Java学习笔记之Pattern类的用法详解(正则表达式)](https://www.cnblogs.com/sparkbj/articles/6207103.html)

## 元字符

| (?:pattern)  | 非获取匹配，匹配pattern但不获取匹配结果，不进行存储供以后使用。这在使用或字符“(\|)”来组合一个模式的各个部分是很有用。例如“industr(?:y\|ies)”就是一个比“industry\|industries”更简略的表达式。 |
| ------------ | ------------------------------------------------------------ |
| (?=pattern)  | 非获取匹配，正向肯定预查，在任何匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。例如，“Windows(?=95\|98\|NT\|2000)”能匹配“Windows2000”中的“Windows”，但不能匹配“Windows3.1”中的“Windows”。预查不消耗字符，也就是说，在一个匹配发生后，在最后一次匹配之后立即开始下一次匹配的搜索，而不是从包含预查的字符之后开始。 |
| (?!pattern)  | 非获取匹配，正向否定预查，在任何不匹配pattern的字符串开始处匹配查找字符串，该匹配不需要获取供以后使用。例如“Windows(?!95\|98\|NT\|2000)”能匹配“Windows3.1”中的“Windows”，但不能匹配“Windows2000”中的“Windows”。 |
| (?<=pattern) | 非获取匹配，反向肯定预查，与正向肯定预查类似，只是方向相反。例如，“(?<=95\|98\|NT\|2000)Windows”能匹配“2000Windows”中的“Windows”，但不能匹配“3.1Windows”中的“Windows”。 |
| (?<!pattern) | 非获取匹配，反向否定预查，与正向否定预查类似，只是方向相反。例如“(?<!95\|98\|NT\|2000)Windows”能匹配“3.1Windows”中的“Windows”，但不能匹配“2000Windows”中的“Windows”。这个地方不正确，有问题 |

**方法1： 匹配，捕获(存储)**

 正则表达式：

```
(?<=(href=")).{1,200}(?=(">))
```

​    解释：(?<=(href=")) 表示 **匹配**以(href=")**开头**的字符串，并且**捕获(存储)**到分组中

​        (?=(">)) 表示 **匹配**以(">)**结尾**的字符串，并且**捕获(存储)**到分组中

**方法2： 匹配，不捕获(不存储)**

 正则表达式：

```
(?<=(**?:**href=")).{1,200}(?=(**?:**">))
```

​    解释：(?<=(**?:**href=")) 表示 **匹配**以(href=")**开头**的字符串，并且**不捕获(不存储)**到分组中

​        (?=(**?:**">)) 表示 **匹配**以(">)**结尾**的字符串，并且**不捕获(不存储)**到分组中

### (?=pattern)

正向肯定预查，在字符串开始处匹配pattern。这是一个非获取匹配，该匹配不需要获取供以后使用，需要和其他的获取匹配配合使用，通过获取匹配获取字符串，字符串开始处匹配pattern。

```java
System.out.println(Pattern.matches("(?=95|98|NT|2000)[\\s\\S]*","2000YVTYTV"));
System.out.println(Pattern.matches("(?=95|98|NT|2000)[\\s\\S]*","AS2000YVTYTV"));
输出：
true
false
```

### (?<=pattern)

与正向肯定预查类似，只是方向相反，在字符串末尾处匹配pattern

```java
System.out.println(Pattern.matches("[\\s\\S]*(?<=95|98|NT|2000)","YVTYTV2000"));
System.out.println(Pattern.matches("[\\s\\S]*(?<=95|98|NT|2000)","AS2000YV"));
输出：
true
false
```

```java
System.out.println("as/asd/as/as".replaceAll("(?<=/).*?(?=/)","*"));		// as/*/*/as
System.out.println("as/asd/as/as".replaceAll("(?=/).*?(?<=/)","*"));		// as*asd*as*as
```



## ?i、?s、?m、?x等

```java
(?i) 表示所在位置右侧的表达式开启忽略大小写模式
(?s) 表示所在位置右侧的表达式开启单行模式，更改句点字符 (.) 的含义，以使它与每个字符（而不是除 \n 之外的所有字符）匹配，通常在匹配有换行的文本时使用
(?m) 表示所在位置右侧的表示式开启指定多行模式，更改 ^ 和 $ 的含义，以使它们分别与任何行的开头和结尾匹配,而不只是与整个字符串的开头和结尾匹配，只有在正则表达式中涉及到多行的“^”和“$”的匹配时，才使用Multiline模式,
上面的匹配模式可以组合使用，比如(?is),(?im)，另外，还可以用(?i:exp)或者(?i)exp(?-i)来指定匹配的有效范围

附：
.表示除\n之外的任意字符

public static void main(String[] args) {
    System.out.println("1.xLSx".matches("^.+\\.(?i)(xlsx)$"));
    System.out.println("\t\n1.xlsx".matches("^(?s)(.+\\.xlsx)$"));
    System.out.println("\t\n1.xlsx".matches(".+\\.xlsx$"));
    System.out.println("789\na".matches("^\\d+\n\\w*$"));
    System.out.println("789\na".matches("(?m)(^\\d+$\n\\w*)"));
    System.out.println("789\na".matches("^\\d+$\n\\w*"));
    
}

true
true
false
true
true
false

(?x)：表示如果加上该修饰符，表达式中的空白字符将会被忽略，除非它已经被转义。 
(?e)：表示本修饰符仅仅对于replacement有用，代表在replacement中作为PHP代码。 
(?A)：表示如果使用这个修饰符，那么表达式必须是匹配的字符串中的开头部分。比如说"/a/A"匹配"abcd"。 
(?E)：与"m"相反，表示如果使用这个修饰符，那么"$"将匹配绝对字符串的结尾，而不是换行符前面，默认就打开了这个模式。 
(?U)：表示和问号的作用差不多，用于设置"贪婪模式"。
```

## 捕获组

> 捕获组:本质上是用小括号给正则表达式分组，从左到右计算开括号给分组编号，然后使用find循环查找，使用group获取查找出来的分组
>
> 捕获组是把正则表达式中多个字符当一个单独单元进行处理的方法，它通过对括号内的字符分组来创建。
> 捕获组是通过从左至右计算其开括号来编号。例如，在表达式((A)(B(C)))，有四个这样的组：((A)(B(C)))、(A)、(B(C))、(C)。
>
> Matcher类的groupCount 方法返回一个 int 值，表示matcher对象当前有多个捕获组。
> group(0)）是一个特殊的组，代表整个表达式。该组不包括在 groupCount 的返回值中。

```java
public static void main(String[] args) {
	Pattern pat = Pattern.compile("(\\D*)(\\d+)(.*)");
	Matcher matcher = pat.matcher("This order was placed for QT3000! OK?");
	while (matcher.find()) {
	//group配合find使用，输出匹配到的字符串
		System.out.println(matcher.group(0));
		System.out.println(matcher.group(1));
		System.out.println(matcher.group(2));
		System.out.println(matcher.group(3));
	}
}

输出
Found value: This order was placed for QT3000! OK?
Found value: This order was placed for QT
Found value: 3000
Found value: ! OK?
```



## Pattern预编译

在频繁调用的场景（例如在方法体内或循环语句中）中，定义Pattern会导致重复预编译正则表达式，降低程序执行效率。另外，对于JDK中的某些API会接受字符串格式的正则表达式作为参数，如`String.replaceAll`、`String.split`等，对于这些API的使用也要考虑性能问题。 

```java
【反例】
public class RegexExp {
    // 该方法被频繁调用
    private boolean isLowerCase(String str) {
        Pattern pattern = Pattern.compile("[a-z]+");
        if (pattern.matcher(str).find()) {
            return true;
        }
        return false;
    }
}
【正例】
public class RegexExp {
    private static final Pattern CHARSET_REG = Pattern.compile("[a-z]+");
    // 该方法被频繁调用
    private boolean isLowerCase(String str) {
        if (CHARSET_REG.matcher(str).find()) {
            return true;
        }
        return false;
    }
}

【反例】
public void doSomething() {
    ...
    for(String temp : strList) {
        temp.replaceAll(regex, "XXX");
        ...
     }
     ...
}
【正例】
public void doSomething() {
    ...
    Pattern pattern = Pattern.compile(regex);  
    for(String temp : strList) {
        pattern.matcher(temp).replaceAll("XXX");
        ...
     }
     ...
}
```



## 转义

常规字符串只转义一次，正则表达式中的\转义两次，并且每次转义中每个字符只能使用一次

常规字符串中`\\\\`经过转义后为`\\`

正则表达式中`\\\\`第一次转义,前两个`\\`转义为`\`,后两个`\\`也转义为`\`,第二次`\\`再转义为`\`

反过来`\`匹配正则表达式，第一次匹配`\\`,第二次匹配`\\\\`,最后正则表达式为`\\\\`

`\\\\`-->`\\`-->`\`

`\\d`-->`\d`-->`(任意一个数字)`

`\\\\\\d`-->`\\\d`-->`\(任意一个数字)`

## 常用正则表达式

### 密码

```java
//至少一个数字，至少一个小写字母，至少一个大写字母，至少一个特殊字符,位数在8-16位
System.out.println(Pattern.matches(
    	"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*?-])[\\S\\s]{8,16}$","5680dasA@das"));
```

### 数字范围

```java
//匹配1-255之间的正整数
System.out.println(Pattern.matches("^(25[0-5]|2[0-4]\d|1\d\d|[1-9]\d|[1-9])$","253"));
```

### IP和端口

```java
//IPv4的地址格式，总长度 32位=4段*8位，每段之间用.分割， 每段都是0-255之间的十进制数值
System.out.println("10.45.168.74".matches("^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$"));
//端口号0-65535
System.out.println("8080".matches("^([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5])$"));
```



















