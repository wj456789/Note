```xml
<!-- poi -->
      <dependency>
          <groupId>org.apache.poi</groupId>
          <artifactId>poi</artifactId>
          <version>3.17</version>
      </dependency>
      <dependency>
          <groupId>org.apache.poi</groupId>
          <artifactId>poi-ooxml</artifactId>
          <version>3.17</version>
      </dependency>
      <dependency>
          <groupId>org.apache.poi</groupId>
          <artifactId>poi-ooxml-schemas</artifactId>
          <version>3.17</version>
      </dependency>
 
 
      <!-- sax -->
      <dependency>
          <groupId>sax</groupId>
          <artifactId>sax</artifactId>
          <version>2.0.1</version>
      </dependency>
      <dependency>
          <groupId>xml-apis</groupId>
          <artifactId>xml-apis</artifactId>
          <version>1.4.01</version>
      </dependency>
      <dependency>
          <groupId>org.apache.xmlbeans</groupId>
          <artifactId>xmlbeans</artifactId>
          <version>2.6.0</version>
      </dependency>
      <dependency>
          <groupId>xerces</groupId>
          <artifactId>xercesImpl</artifactId>
          <version>2.11.0</version>
      </dependency>
```

## EXCEL常量类

```java
package com.yzx.osp.common.constant;
 
/**
 * @author qjwyss
 * @date 2018/9/19
 * @description EXCEL常量类
 */
public class ExcelConstant {
 
    /**
     * excel2007扩展名
     */
    public static final String EXCEL07_EXTENSION = ".xlsx";
 
 
    /**
     * 每个sheet存储的记录数 100W
     */
    public static final Integer PER_SHEET_ROW_COUNT = 1000000;
 
    /**
     * 每次向EXCEL写入的记录数(查询每页数据大小) 20W
     */
    public static final Integer PER_WRITE_ROW_COUNT = 200000;
 
 
    /**
     * 每个sheet的写入次数 5
     */
    public static final Integer PER_SHEET_WRITE_COUNT = PER_SHEET_ROW_COUNT / PER_WRITE_ROW_COUNT;
 
 
    /**
     * 读取excel的时候每次批量插入数据库记录数
     */
    public static final Integer PER_READ_INSERT_BATCH_COUNT = 10000;
 
 
 
}
```

##  读取EXCEL辅助类

```java
package com.yzx.osp.common.util;
 
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
 
/**
 * @author qjwyss
 * @date 2018/12/19
 * @description 读取EXCEL辅助类
 */
public class ExcelXlsxReaderWithDefaultHandler extends DefaultHandler {
 
    private ExcelReadDataDelegated excelReadDataDelegated;
 
    public ExcelReadDataDelegated getExcelReadDataDelegated() {
        return excelReadDataDelegated;
    }
 
    public void setExcelReadDataDelegated(ExcelReadDataDelegated excelReadDataDelegated) {
        this.excelReadDataDelegated = excelReadDataDelegated;
    }
 
    public ExcelXlsxReaderWithDefaultHandler(ExcelReadDataDelegated excelReadDataDelegated) {
        this.excelReadDataDelegated = excelReadDataDelegated;
    }
 
    /**
     * 单元格中的数据可能的数据类型
     */
    enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }
 
    /**
     * 共享字符串表
     */
    private SharedStringsTable sst;
 
    /**
     * 上一次的索引值
     */
    private String lastIndex;
 
    /**
     * 文件的绝对路径
     */
    private String filePath = "";
 
    /**
     * 工作表索引
     */
    private int sheetIndex = 0;
 
    /**
     * sheet名
     */
    private String sheetName = "";
 
    /**
     * 总行数
     */
    private int totalRows = 0;
 
    /**
     * 一行内cell集合
     */
    private List<String> cellList = new ArrayList<String>();
 
    /**
     * 判断整行是否为空行的标记
     */
    private boolean flag = false;
 
    /**
     * 当前行
     */
    private int curRow = 1;
 
    /**
     * 当前列
     */
    private int curCol = 0;
 
    /**
     * T元素标识
     */
    private boolean isTElement;
 
    /**
     * 异常信息，如果为空则表示没有异常
     */
    private String exceptionMessage;
 
    /**
     * 单元格数据类型，默认为字符串类型
     */
    private CellDataType nextDataType = CellDataType.SSTINDEX;
 
    private final DataFormatter formatter = new DataFormatter();
 
    /**
     * 单元格日期格式的索引
     */
    private short formatIndex;
 
    /**
     * 日期格式字符串
     */
    private String formatString;
 
    //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String preRef = null, ref = null;
 
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    private String maxRef = null;
 
    /**
     * 单元格
     */
    private StylesTable stylesTable;
 
 
    /**
     * 总行号
     */
    private Integer totalRowCount;
 
    /**
     * 遍历工作簿中所有的电子表格
     * 并缓存在mySheetList中
     *
     * @param filename
     * @throws Exception
     */
    public int process(String filename) throws Exception {
        filePath = filename;
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader xssfReader = new XSSFReader(pkg);
        stylesTable = xssfReader.getStylesTable();
        SharedStringsTable sst = xssfReader.getSharedStringsTable();
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (sheets.hasNext()) { //遍历sheet
            curRow = 1; //标记初始行为第一行
            sheetIndex++;
            InputStream sheet = sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
            sheetName = sheets.getSheetName();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource); //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
            sheet.close();
        }
        return totalRows; //返回该excel文件的总行数，不包括首列和空行
    }
 
    /**
     * 第一个执行
     *
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
 
        // 获取总行号  格式： A1:B5    取最后一个值即可
        if("dimension".equals(name)) {
            String dimensionStr = attributes.getValue("ref");
            totalRowCount = Integer.parseInt(dimensionStr.substring(dimensionStr.indexOf(":") + 2)) - 1;
        }
 
        //c => 单元格
        if ("c".equals(name)) {
            //前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue("r");
            } else {
                preRef = ref;
            }
 
            //当前单元格的位置
            ref = attributes.getValue("r");
            //设定单元格类型
            this.setNextDataType(attributes);
        }
 
        //当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }
 
        //置空
        lastIndex = "";
    }
 
 
    /**
     * 第二个执行
     * 得到单元格对应的索引值或是内容值
     * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
     * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastIndex += new String(ch, start, length);
    }
 
 
    /**
     * 第三个执行
     *
     * @param uri
     * @param localName
     * @param name
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
 
        //t元素也包含字符串
        if (isTElement) {//这个程序没经过
            //将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
            String value = lastIndex.trim();
            cellList.add(curCol, value);
            curCol++;
            isTElement = false;
            //如果里面某个单元格含有值，则标识该行不为空行
            if (value != null && !"".equals(value)) {
                flag = true;
            }
        } else if ("v".equals(name)) {
            //v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
            String value = this.getDataValue(lastIndex.trim(), "");//根据索引值获取对应的单元格值
            //补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                int len = countNullCell(ref, preRef);
                for (int i = 0; i < len; i++) {
                    cellList.add(curCol, "");
                    curCol++;
                }
            }
            cellList.add(curCol, value);
            curCol++;
            //如果里面某个单元格含有值，则标识该行不为空行
            if (value != null && !"".equals(value)) {
                flag = true;
            }
        } else {
            //如果标签名称为row，这说明已到行尾，调用optRows()方法
            if ("row".equals(name)) {
                //默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 1) {
                    maxRef = ref;
                }
                //补全一行尾部可能缺失的单元格
                if (maxRef != null) {
                    int len = countNullCell(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        cellList.add(curCol, "");
                        curCol++;
                    }
                }
 
                if (flag && curRow != 1) { //该行不为空行且该行不是第一行，则发送（第一行为列名，不需要）
                    // 调用excel读数据委托类进行读取插入操作
                    excelReadDataDelegated.readExcelDate(sheetIndex, totalRowCount, curRow, cellList);
                    totalRows++;
                }
 
                cellList.clear();
                curRow++;
                curCol = 0;
                preRef = null;
                ref = null;
                flag = false;
            }
        }
    }
 
    /**
     * 处理数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {
        nextDataType = CellDataType.NUMBER; //cellType为空，则表示该单元格类型为数字
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue("t"); //单元格类型
        String cellStyleStr = attributes.getValue("s"); //
        String columnData = attributes.getValue("r"); //获取单元格的位置，如A1,B1
 
        if ("b".equals(cellType)) { //处理布尔值
            nextDataType = CellDataType.BOOL;
        } else if ("e".equals(cellType)) {  //处理错误
            nextDataType = CellDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
        } else if ("s".equals(cellType)) { //处理字符串
            nextDataType = CellDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }
 
        if (cellStyleStr != null) { //处理日期
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if (formatString.contains("m/d/yy") || formatString.contains("yyyy/mm/dd") || formatString.contains("yyyy/m/d")) {
                nextDataType = CellDataType.DATE;
                formatString = "yyyy-MM-dd hh:mm:ss";
            }
 
            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }
 
    /**
     * 对解析出来的数据进行类型处理
     *
     * @param value   单元格的值，
     *                value代表解析：BOOL的为0或1， ERROR的为内容值，FORMULA的为内容值，INLINESTR的为索引值需转换为内容值，
     *                SSTINDEX的为索引值需转换为内容值， NUMBER为内容值，DATE为内容值
     * @param thisStr 一个空字符串
     * @return
     */
    @SuppressWarnings("deprecation")
    public String getDataValue(String value, String thisStr) {
        switch (nextDataType) {
            // 这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL: //布尔值
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR: //错误
                thisStr = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA: //公式
                thisStr = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                thisStr = rtsi.toString();
                rtsi = null;
                break;
            case SSTINDEX: //字符串
                String sstIndex = value.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));//根据idx索引值获取内容值
                    thisStr = rtss.toString();
                    rtss = null;
                } catch (NumberFormatException ex) {
                    thisStr = value.toString();
                }
                break;
            case NUMBER: //数字
                if (formatString != null) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    thisStr = value;
                }
                thisStr = thisStr.replace("_", "").trim();
                break;
            case DATE: //日期
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                // 对日期字符串作特殊处理，去掉T
                thisStr = thisStr.replace("T", " ");
                break;
            default:
                thisStr = " ";
                break;
        }
        return thisStr;
    }
 
    public int countNullCell(String ref, String preRef) {
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");
 
        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);
 
        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }
 
    public String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }
 
 
}
```

## 写数据委托接口

```java
package com.yzx.osp.common.util;
 
import java.util.List;
 
/**
 * @author qjwyss
 * @date 2018/12/19
 * @description 读取excel数据委托接口
 */
public interface ExcelReadDataDelegated {
 
    /**
     * 每获取一条记录，即写数据
     * 在flume里每获取一条记录即写，而不必缓存起来，可以大大减少内存的消耗，这里主要是针对flume读取大数据量excel来说的
     *
     * @param sheetIndex    sheet位置
     * @param totalRowCount 该sheet总行数
     * @param curRow        行号
     * @param cellList      行数据
     */
    public abstract void readExcelDate(int sheetIndex, int totalRowCount, int curRow, List<String> cellList);
 
}
```

## 读取工具类 

```java
package com.yzx.osp.common.util;
 
import com.yzx.osp.common.constant.ExcelConstant;
 
import java.util.List;
 
/**
 * @author qjwyss
 * @date 2018/12/19
 * @description 读取EXCEL工具类
 */
public class ExcelReaderUtil {
 
 
    public static void readExcel(String filePath, ExcelReadDataDelegated excelReadDataDelegated) throws Exception {
        int totalRows = 0;
        if (filePath.endsWith(ExcelConstant.EXCEL07_EXTENSION)) {
            ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler(excelReadDataDelegated);
            totalRows = excelXlsxReader.process(filePath);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xlsx!");
        }
        System.out.println("读取的数据总行数：" + totalRows);
    }
 
 
    public static void main(String[] args) throws Exception {
        String path = "E:\\temp\\5.xlsx";
        ExcelReaderUtil.readExcel(path, new ExcelReadDataDelegated() {
            @Override
            public void readExcelDate(int sheetIndex, int totalRowCount, int curRow, List<String> cellList) {
                System.out.println("总行数为：" + totalRowCount + " 行号为：" + curRow + " 数据：" + cellList);
            }
        });
    }
}
```

## 使用案例

```java
@Override
    public ResultVO<Void> importMobileManagerList(String filePath) throws Exception {
 
        logger.info("开始导入号码列表：" + DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
 
        List<Integer> errorRowNumber = new ArrayList<>();
        List<MobileManager> mobileManagerList = new ArrayList<>();
 
        MobileManagerVO mobileManagerVO = new MobileManagerVO();
        List<String> mobileList = this.mobileManagerMapper.selectMobileList(mobileManagerVO);
 
        SysAreaVO sysAreaVO = new SysAreaVO();
        List<SysAreaVO> sysAreaVOList = this.sysAreaMapper.selectSysAreaVOList(sysAreaVO);
 
        CustomerVO customerVO = new CustomerVO();
        List<String> customerIdList = this.customerDao.selectLineOrBusinessCustomerIdList(customerVO);
        List<CustomerVO> customerVOList = this.customerDao.selectCustomerIdAndAccountIdList(customerVO);
 
        // 用来存储EXCEL中的号码集合 用来去重
        List<String> excelMobileList = new ArrayList<>();
 
        ExcelReaderUtil.readExcel(filePath, new ExcelReadDataDelegated() {
            @Override
            public void readExcelDate(int sheetIndex, int totalRowCount, int curRow, List<String> cellList) {
 
                // 校验数据合法性
                Boolean legalFlag = true;
                Integer provinceId = null;
                Integer cityId = null;
 
                List<String> accountIdList = null;
 
                // 号码、成本号码费、成本低消费、客户号码费、客户低消费不能为空
                if (CommonUtil.checkStringIsNullOrLine(cellList.get(0)) || CommonUtil.checkStringIsNullOrLine(cellList.get(12))
                        || CommonUtil.checkStringIsNullOrLine(cellList.get(13)) || CommonUtil.checkStringIsNullOrLine(cellList.get(14))
                        || CommonUtil.checkStringIsNullOrLine(cellList.get(15))) {
                    legalFlag = false;
                }
 
 
                // 校验EXCEL中号码不能重复号码不能重复
                if(excelMobileList.contains(cellList.get(0).trim())) {
                    legalFlag = false;
                } else {
                    excelMobileList.add(cellList.get(0).trim());
                }
 
                // 客户类型为VBOSS并且分配了客户时，账户不能为空
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(7)) && !CommonUtil.checkStringIsNullOrLine(cellList.get(8))) {
                    if (cellList.get(7).trim().equals("VBOSS") && CommonUtil.checkStringIsNullOrLine(cellList.get(8))) {
                        legalFlag = false;
                    }
                }
 
                // 客户类型为空的时候客户账户不能有值
                if (CommonUtil.checkStringIsNullOrLine(cellList.get(7))) {
                    if (!CommonUtil.checkStringIsNullOrLine(cellList.get(8)) || !CommonUtil.checkStringIsNullOrLine(cellList.get(9))) {
                        legalFlag = false;
                    }
                }
 
                // 客户类型为bss的时候账户不能有值
                if (CommonUtil.checkStringIsNullOrLine(cellList.get(7))) {
                    if (cellList.get(7).trim().equals("BSS")) {
                        if (!CommonUtil.checkStringIsNullOrLine(cellList.get(9))) {
                            legalFlag = false;
                        }
                    }
                }
 
 
                // 号码、区号必须为数字
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(0))) {
                    if (!CommonUtil.checkIsInteger(cellList.get(0).trim())) {
                        legalFlag = false;
                    }
                }
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(1))) {
                    if (!CommonUtil.checkIsInteger(cellList.get(1).trim())) {
                        legalFlag = false;
                    }
                }
 
                // 运营商只能为 移动、联通、电信、铁通、其它之一
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(5))) {
                    if (!cellList.get(5).trim().equals("移动") && !cellList.get(5).trim().equals("联通")
                            && !cellList.get(5).trim().equals("电信") && !cellList.get(5).trim().equals("铁通")
                            && !cellList.get(5).trim().equals("其它")) {
                        legalFlag = false;
                    }
                }
 
                // 客户类型只能是 VBOSS或BSS之一
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(7))) {
                    if (!cellList.get(7).trim().equalsIgnoreCase("VBOSS") && !cellList.get(7).trim().equalsIgnoreCase("BSS")) {
                        legalFlag = false;
                    }
                }
 
                // 成本号码费、成本低消费、客户号码费、客户低消费只能为小数
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(12))) {
                    if (!CommonUtil.checkIsSmallNumber(cellList.get(12).trim())) {
                        legalFlag = false;
                    }
                }
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(13))) {
                    if (!CommonUtil.checkIsSmallNumber(cellList.get(13).trim())) {
                        legalFlag = false;
                    }
                }
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(14))) {
                    if (!CommonUtil.checkIsSmallNumber(cellList.get(14).trim())) {
                        legalFlag = false;
                    }
                }
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(15))) {
                    if (!CommonUtil.checkIsSmallNumber(cellList.get(15).trim())) {
                        legalFlag = false;
                    }
                }
 
 
                // 数据库校验
 
                // 校验号码是否存在
                if (!CollectionUtils.isEmpty(mobileList)) {
                    if (mobileList.contains(cellList.get(0).trim())) {
                        legalFlag = false;
                    }
                }
 
                // 校验省是否存在
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(2))) {
                    if (CollectionUtils.isEmpty(sysAreaVOList)) {
                        legalFlag = false;
                    } else {
                        Boolean hasFlag = false;
                        for (SysAreaVO eachSysAreaVO : sysAreaVOList) {
                            if (eachSysAreaVO.getAreaName().equals(cellList.get(2).trim())) {
                                hasFlag = true;
                                provinceId = eachSysAreaVO.getSaid();
                                break;
                            }
                        }
                        if (!hasFlag) {
                            legalFlag = false;
                        }
                    }
                }
 
 
                // 校验市是否存在
                if (!CommonUtil.checkStringIsNullOrLine(cellList.get(3))) {
                    if (CollectionUtils.isEmpty(sysAreaVOList)) {
                        legalFlag = false;
                    } else {
                        Boolean hasFlag = false;
                        for (SysAreaVO eachSysAreaVO : sysAreaVOList) {
                            if (eachSysAreaVO.getAreaName().equals(cellList.get(3).trim())) {
                                hasFlag = true;
                                cityId = eachSysAreaVO.getSaid();
                                break;
                            }
                        }
                        if (!hasFlag) {
                            legalFlag = false;
                        }
                    }
                }
 
 
                // 如果选择了客户类型并且分配了客户，则需要校验客户ID是否存在
                if(!CommonUtil.checkStringIsNullOrLine(cellList.get(7)) && !CommonUtil.checkStringIsNullOrLine(cellList.get(8))) {
 
                    // 校验客户ID是否存在
                    Boolean hasCustomerIdFlag = true;
                    if(CollectionUtils.isEmpty(customerIdList)) {
                        hasCustomerIdFlag = false;
                        legalFlag = false;
                    } else {
                        if(!customerIdList.contains(cellList.get(8).trim())) {
                            hasCustomerIdFlag = false;
                            legalFlag = false;
                        }
                    }
 
                    // 如果该客户ID存在，并且选中的客户类型是VBOSS，则需要校验账户和客户是否匹配
                    if(hasCustomerIdFlag) {
                        if(cellList.get(7).equals("VBOSS") && !CommonUtil.checkStringIsNullOrLine(cellList.get(9))) {
 
                            if(CollectionUtils.isEmpty(customerVOList)) {
                                legalFlag = false;
                            } else {
                                for (CustomerVO eachCustomerVO: customerVOList) {
                                    if(eachCustomerVO.getCustomerId().equals(cellList.get(8).trim())) {
                                        accountIdList = eachCustomerVO.getAccountIdList();
                                        break;
                                    }
                                }
 
                                if(CollectionUtils.isEmpty(accountIdList)) {
                                    legalFlag = false;
                                } else {
                                    if(!accountIdList.contains(cellList.get(9).trim())) {
                                        legalFlag = false;
                                    }
                                }
                            }
                        }
                    }
                }
 
 
                // 如果数据合法，则封装号码对象
                if (!legalFlag) {
                    if (!errorRowNumber.contains(curRow)) {
                        errorRowNumber.add(curRow);
                    }
                } else {
 
                    try {
 
                        MobileManager mobileManager = new MobileManager();
                        mobileManager.setMobile(cellList.get(0).trim());
                        mobileManager.setAreaCode(CommonUtil.checkStringIsNullOrLine(cellList.get(1)) ? null : cellList.get(1));
                        mobileManager.setProvinceId(provinceId == null ? null : provinceId);
                        mobileManager.setCityId(cityId == null ? null : cityId);
                        mobileManager.setType(CommonUtil.checkStringIsNullOrLine(cellList.get(4)) ? null : cellList.get(4));
 
                        if (!CommonUtil.checkStringIsNullOrLine(cellList.get(5))) {
                            Integer operator = null;
                            if (cellList.get(5).trim().equals("移动")) {
                                operator = MobileManagerConstant.OPERATOR_YIDONG;
                            } else if (cellList.get(5).trim().equals("联通")) {
                                operator = MobileManagerConstant.OPERATOR_LIANTONG;
                            } else if (cellList.get(5).trim().equals("电信")) {
                                operator = MobileManagerConstant.OPERATOR_DIANXIN;
                            } else if (cellList.get(5).trim().equals("铁通")) {
                                operator = MobileManagerConstant.OPERATOR_TIETONG;
                            } else if (cellList.get(5).trim().equals("其它")) {
                                operator = MobileManagerConstant.OPERATOR_ELSE;
                            }
                            mobileManager.setOperator(operator);
                        }
 
                        mobileManager.setSupplierName(CommonUtil.checkStringIsNullOrLine(cellList.get(6)) ? null : cellList.get(6));
 
 
                        if (!CommonUtil.checkStringIsNullOrLine(cellList.get(7))) {
                            Integer customerType = null;
                            if (cellList.get(7).trim().equalsIgnoreCase("VBOSS")) {
                                customerType = MobileManagerConstant.CUSTOMER_TYPE_VBOSS;
                                mobileManager.setAccountId(CommonUtil.checkStringIsNullOrLine(cellList.get(9)) ? null : cellList.get(9));
                            } else if (cellList.get(7).trim().equalsIgnoreCase("BSS")) {
                                customerType = MobileManagerConstant.CUSTOMER_TYPE_BSS;
                            }
                            mobileManager.setCustomerType(customerType);
                            mobileManager.setCustomerId(CommonUtil.checkStringIsNullOrLine(cellList.get(8)) ? null : cellList.get(8));
                            mobileManager.setState(CommonUtil.checkStringIsNullOrLine(cellList.get(8)) ?
                                    MobileManagerConstant.STATE_USEING : MobileManagerConstant.STATE_USEING);
                            mobileManager.setDistributeTime(CommonUtil.checkStringIsNullOrLine(cellList.get(8)) ? null : new Date());
                        } else {
                            mobileManager.setState(MobileManagerConstant.STATE_UNUSE);
                        }
 
 
                        mobileManager.setTerminalUser(CommonUtil.checkStringIsNullOrLine(cellList.get(10)) ? null : cellList.get(10));
 
                        if (!CommonUtil.checkStringIsNullOrLine(cellList.get(11))) {
                            mobileManager.setPlanRecoveryDate(DateUtil.formatDateStrWithLine2Date(cellList.get(11).trim()));
                        }
 
                        mobileManager.setCostMobileFee(CommonUtil.checkStringIsNullOrLine(cellList.get(12)) ? null : Double.parseDouble(cellList.get(12).trim()));
                        mobileManager.setCostLowFee(CommonUtil.checkStringIsNullOrLine(cellList.get(13)) ? null : Double.parseDouble(cellList.get(13).trim()));
                        mobileManager.setCustomerMobileFee(CommonUtil.checkStringIsNullOrLine(cellList.get(14)) ? null : Double.parseDouble(cellList.get(14).trim()));
                        mobileManager.setCustomerLowFee(CommonUtil.checkStringIsNullOrLine(cellList.get(15)) ? null : Double.parseDouble(cellList.get(15).trim()));
                        mobileManager.setRemark(CommonUtil.checkStringIsNullOrLine(cellList.get(16)) ? null : cellList.get(16).trim());
                        mobileManager.setCreateTime(new Date());
                        mobileManagerList.add(mobileManager);
 
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!errorRowNumber.contains(curRow)) {
                            errorRowNumber.add(curRow);
                        }
                    }
                }
 
 
                // 批量保存
                try {
                    if (mobileManagerList.size() == ExcelConstant.PER_READ_INSERT_BATCH_COUNT) {
                        mobileManagerMapper.saveMobileManagerBatch(mobileManagerList);
                        mobileManagerList.clear();
                    } else if (mobileManagerList.size() < ExcelConstant.PER_READ_INSERT_BATCH_COUNT) {
                        int lastInsertBatchCount = totalRowCount % ExcelConstant.PER_READ_INSERT_BATCH_COUNT == 0 ?
                                totalRowCount / ExcelConstant.PER_READ_INSERT_BATCH_COUNT :
                                totalRowCount / ExcelConstant.PER_READ_INSERT_BATCH_COUNT + 1;
                        if ((curRow - 1) >= ((lastInsertBatchCount - 1) * ExcelConstant.PER_READ_INSERT_BATCH_COUNT + 1)
                                && (curRow - 1) < lastInsertBatchCount * ExcelConstant.PER_READ_INSERT_BATCH_COUNT) {
                            if (curRow - 1 == totalRowCount) {
                                if(!CollectionUtils.isEmpty(mobileManagerList)) {
                                    mobileManagerMapper.saveMobileManagerBatch(mobileManagerList);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!errorRowNumber.contains(curRow)) {
                        errorRowNumber.add(curRow);
                    }
                }
 
            }
        });
 
        logger.info("导入号码列表完成：" + DateUtil.formatDate(new Date(), DateUtil.YYYY_MM_DD_HH_MM_SS));
 
        return ResultVO.getSuccess("批量导入VOIP用户完成, 共有" + errorRowNumber.size() + "条记录存在问题，失败行号为：" + errorRowNumber);
    }
```

