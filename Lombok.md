# lombok

```
@Data
```

@Data自动生成：

- 无参构造
- get()set()方法
- equals()方法
- canEquals()方法
- hashCode()方法
- toString()方法

```
//自动生成包括父类的参数
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
```

```
//自定义构造函数
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
```

