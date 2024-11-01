# HBase

## 读写优化

在HBase中，`Scan`对象是用于定义扫描操作的配置，包括扫描的起始和结束行、列族和列过滤器等。`setCache`和`setBatch`方法是`Scan`对象中用于优化扫描性能的两个重要方法。

- `setCache(int cache)`

  setCache 方法用于设置扫描结果的缓存大小。缓存大小指的是HBase客户端在扫描过程中，从服务器端一次获取的行数。较大的缓存大小可以减少客户端与服务器之间的网络交互次数，从而提高扫描性能。但是，如果缓存设置得过大，可能会消耗客户端过多的内存资源。

  示例用法：

  ```java
  Scan scan = new Scan();
  scan.setCache(100); // 设置缓存大小为100行
  ```

- `setBatch(int batch)`

  setBatch 方法用于设置扫描结果的批处理大小。批处理大小指的是客户端在接收到一批扫描结果后，继续向服务器请求下一批结果之前，需要处理（例如，解析、过滤）的行数。较小的批处理大小可以减少客户端在处理结果时的内存占用，但可能会增加网络交互次数。较大的批处理大小则相反。

  需要注意的是，`setBatch`方法在某些HBase版本中可能已经被废弃，因为现代的HBase客户端通常会自动管理批处理的大小。取而代之的是`setScannerCaching`方法，该方法的行为与`setCache`方法类似，但在名称上更清晰地区分了扫描缓存与RPC层级的批处理。

  示例用法（如果`setBatch`仍然可用）：

  ```java
  Scan scan = new Scan();
  scan.setBatch(10); // 设置批处理大小为10行
  ```

  或者，使用`setScannerCaching`方法：

  ```java
  Scan scan = new Scan();
  scan.setScannerCaching(100); // 设置扫描缓存大小为100行
  ```

  在实际应用中，应该根据具体的扫描需求和客户端的内存资源来合理设置`setCache`（或`setScannerCaching`）和`setBatch`方法的值，以达到最佳的扫描性能。

