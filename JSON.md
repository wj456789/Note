# JSON

## JSON字符串转换

json有2种类型：
一种是对象，object -> {key:value,key:value,...} 。
另一种是数组，array -> [value,value,...] 。

解析json大体上有2种方式，一种是直接在内存中生成object或array，通过手工指定key来获取值；另一种是借助javabean来进行映射获取值。

## fastJson

### 依赖

```java
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.74</version>
</dependency>
```

### 解析JSON字符串

#### JSON字符串类型：对象

```java
JSONObject jsonObject = JSONObject.parseObject(jsonStr);
String key = jsonObject.getString("key");
```

```java
//JSONObject方法如下
static JSONObject parseObject(String text)
static <T> T parseObject(String text, Class<T> clazz)
//jsonObject方法如下
Object get(Object key)
<T> T getObject(String key, Class<T> clazz)
String getString(String key)
JSONArray getJSONArray(String key)
JSONObject getJSONObject(String key)
```

#### JSON字符串类型：数组

```java
JSONArray jsonArray = JSONArray.parseArray(jsonStr);
String str = jsonArray.getString(0);
```

```java
//JSONArray方法如下
static JSONArray parseArray(String text)
static <T> List<T> parseArray(String text, Class<T> clazz)
//jsonArray方法如下
Object get(int index)
<T> T getObject(int index, Class<T> clazz)
String getString(int index)
JSONArray getJSONArray(int index)
JSONObject getJSONObject(int index)    
```



### 生成JSON字符串

```java
String jsonStr = JSONObject.toJSONString(obj);
```



## Gson

### 依赖

```java
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
```

### 解析JSON字符串

JSON字符串反序列化，得到Java对象

#### 解析方式：不使用Java类

##### JSON字符串类型：对象

```java
{
    "sex": '男',
    "hobby":["baskte","tennis"],
    "introduce": {
        "name":"tom",
        "age":23
    }
}
```

```java
// json解析器，解析json数据
JsonParser parser = new JsonParser();
JsonElement element = parser.parse(json);
// json属于对象类型时
if (element.isJsonObject()) {  
    // 转化为对象
    JsonObject object = element.getAsJsonObject();  

    // 1. value为string时，取出string
    String sex = object.get("sex").getAsString();

    // 2. value为array时，取出array
    JsonArray hobbies = object.getAsJsonArray("hobby");
    for (int i = 0; i < hobbies.size(); i++) {
        String hobby = hobbies.get(i).getAsString();
    }

    // 3. value为object时，取出object
    JsonObject introduce = object.getAsJsonObject("introduce");
    String name = introduce.get("name").getAsString();
    int age = introduce.get("age").getAsInt();
}
```

##### JSON字符串类型：数组

```java
[
    "cake",
    2,
    {"brother":"tom","sister":"lucy"},
    ["red","orange"]
]
```

```java
// json解析器，解析json数据
JsonParser parser = new JsonParser();
JsonElement element = parser.parse(json);
// json属于数组类型
if (element.isJsonArray()) {  
    JsonArray array = element.getAsJsonArray();
    
 	// 1. value为string时，取出string
    String array_1 = array.get(0).getAsString();

    // 2. value为int时，取出int
    int array_2 = array.get(1).getAsInt();

    // 3. value为object时，取出object
    JsonObject array_3 = array.get(2).getAsJsonObject();
    String brother = array_3.get("brother").getAsString();
    String sister = array_3.get("sister").getAsString();

    // 4. value为array时，取出array
    JsonArray array_4 = array.get(3).getAsJsonArray();
    for (int i = 0; i < array_4.size(); i++) {
    System.out.println(array_4.get(i).getAsString());
    }

}
```

## 



#### 解析方式：使用Java类

```java
//通用代码
Gson gson = new Gson();
BeanType bean = gson.fromJson(jsonData, BeanType.class);
```

```java
//gson方法，其中gson必须传入第二个参数
<T> T fromJson(String json, Class<T> classOfT)
```



##### JSON字符串类型：对象

```java
Gson gson1 = new Gson();
MyEntry entry1 = gson1.fromJson(json1, MyEntry.class);
```

```java
{
  "name": "tom",
  "age": 0,
  "money": 2999,
  "hobbies": [
    "basket",
    "tennis"
  ],
  "collections": {
    "2": "paint",
    "3": "mouse"
  }
}
```

```java
public class MyEntry{
    private String name;
    private int age;
    private int money;
    private List<String> hobbies;
	private Map<Integer, String> collections;
}
```

##### JSON字符串类型：数组

```java
["apple", "banana", "pear"]
```

```java
Gson gson2 = new Gson();
// 传入的java类型是String[].class
String[] fruits = gson2.fromJson(json2, String[].class);  
```

或者使用List

```java
Gson gson2 = new Gson();
List<String> fruitList = gson2.fromJson(json2, new TypeToken<List<String>>(){}.getType());
```

`new TypeToken<List<String>>(){}`TypeToken()这个构造方法是protected修饰的，不能直接被非同包给new出来，需要用一个匿名内部类先继承再new，然后通过继承的那个类来拿持有泛型。

##### JSON字符串类型：使用泛型

```java
//当Json字符串为以下两种时，其中字段data ，有时候是对象，有时候是数组
{"code":"0","message":"success","data":{}}
{"code":"0","message":"success","data":[]}


//可以使用泛型，使用Result<T> 来映射json数据，使用MyEntry 类来映射json 数据的data 部分
public class Result<T>{
    public int code;
    public String message;
    public T data;
}
```

##### JSON字符串类型：泛型对象

```java
{
  "code": 0,
  "message": "success",
  "data": 
    {
      "name": "tom",
      "age": 32,
      "address": "street one",
      "salary": 4999
    }
}
```

```java
Gson typeGson1 = new Gson();
// 动态生成所需的java类的类型
Type type1 = new TypeToken<Result<MyEntry>>(){}.getType();
// 动态生成java对象
Result<MyEntry> result1 = typeGson1.fromJson(typeJson1, type1);
```

##### JSON字符串类型：泛型数组

```java
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "name": "tom",
      "age": 32,
      "address": "street one",
      "salary": 4999
    },
    {
      "name": "lucy",
      "age": 24,
      "address": "street three",
      "salary": 2333
    }
  ]
}
```

```java
Gson typeGson2 = new Gson();
// 再次动态生成java类型
Type type2 = new TypeToken<Result<List<MyEntry>>>(){}.getType();
// 再次动态生成java对象
Result<List<MyEntry>> result2 = typeGson2.fromJson(typeJson2, type2);
```

### 生成JSON字符串

Java对象序列化，得到JSON字符串

```java
Gson gson2 = new Gson();
String json2 = gson2.toJson(entry2);
```

参考：[GSON](https://www.jianshu.com/p/75a50aa0cad1)