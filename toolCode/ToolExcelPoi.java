package com.shzx.application.common.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ToolExcelPoi {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public ToolExcelPoi() {
    }

    public static void main(String[] args) {
        LinkedHashMap<String, List<String>> result = read(new File("/Users/xu/Documents/github/tap-water-om/doc/需求文档/入库导入模版.xlsx"));
        Iterator var2 = result.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            List<String> list = (List)result.get(key);
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                String str = (String)var5.next();
                System.out.println(key + "--" + str);
            }
        }

        write(result, "/Users/xu/Documents/github/tap-water-om/doc/需求文档/2/入库导入模版1.xlsx");
    }

    public static LinkedHashMap<String, List<String>> read(File file) {
        LinkedHashMap result = new LinkedHashMap();

        try {
            FileInputStream in = new FileInputStream(file);
            checkExcelVaild(file);
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator var5 = sheet.iterator();

            while(true) {
                Row row;
                ArrayList list;
                do {
                    if (!var5.hasNext()) {
                        return result;
                    }

                    row = (Row)var5.next();
                    list = new ArrayList();
                } while(StringUtils.isBlank(row.getCell(0).toString()));

                int end = row.getLastCellNum();

                for(int i = 0; i < end; ++i) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        Object obj = getValue(cell);
                        list.add(obj + "");
                    }
                }

                result.put(row.getRowNum() + "", list);
            }
        } catch (EncryptedDocumentException var12) {
            var12.printStackTrace();
        } catch (FileNotFoundException var13) {
            var13.printStackTrace();
        } catch (IOException var14) {
            var14.printStackTrace();
        } catch (Exception var15) {
            var15.printStackTrace();
        }

        return result;
    }

    public static LinkedHashMap<String, List<String>> read(InputStream in) {
        LinkedHashMap result = new LinkedHashMap();

        try {
            Workbook workbook = WorkbookFactory.create(in);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator var4 = sheet.iterator();

            while(true) {
                Row row;
                ArrayList list;
                do {
                    if (!var4.hasNext()) {
                        return result;
                    }

                    row = (Row)var4.next();
                    list = new ArrayList();
                } while(StringUtils.isBlank(row.getCell(0).toString()));

                int end = row.getLastCellNum();

                for(int i = 0; i < end; ++i) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        Object obj = getValue(cell);
                        list.add(obj + "");
                    }
                }

                result.put(row.getRowNum() + "", list);
            }
        } catch (EncryptedDocumentException var11) {
            var11.printStackTrace();
        } catch (FileNotFoundException var12) {
            var12.printStackTrace();
        } catch (IOException var13) {
            var13.printStackTrace();
        } catch (Exception var14) {
            var14.printStackTrace();
        }

        return result;
    }

    public static void write(LinkedHashMap<String, List<String>> dataList, String filePath) {
        FileOutputStream out = null;

        try {
            File finalXlsxFile = new File(filePath);
            if (!finalXlsxFile.exists()) {
                createExcel(filePath);
            }

            Workbook workBook = getWorkbok(finalXlsxFile);
            Sheet sheet = workBook.getSheetAt(0);
            out = new FileOutputStream(filePath);
            workBook.write(out);
            int num = 0;

            for(Iterator var7 = dataList.keySet().iterator(); var7.hasNext(); ++num) {
                String key = (String)var7.next();
                List<String> list = (List)dataList.get(key);
                Row row = sheet.createRow(num);

                for(int k = 0; k < list.size(); ++k) {
                    Cell first = row.createCell(k);
                    first.setCellValue(list.get(k) == null ? "" : (String)list.get(k));
                }
            }

            out = new FileOutputStream(filePath);
            workBook.write(out);
        } catch (Exception var21) {
            var21.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException var20) {
                var20.printStackTrace();
            }

        }

        System.out.println("数据导出成功");
    }

    public static Workbook getWorkbok(InputStream in, File file) throws IOException {
        Workbook wb = null;
        if (file.getName().endsWith("xls")) {
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith("xlsx")) {
            wb = new XSSFWorkbook(in);
        }

        return (Workbook)wb;
    }

    public static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith("xls")) {
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith("xlsx")) {
            wb = new XSSFWorkbook(in);
        }

        return (Workbook)wb;
    }

    public static Workbook getWorkbok(String filePath) throws IOException {
        File file = new File(filePath);
        Workbook wb = null;
        if (file.getName().endsWith("xls")) {
            wb = new HSSFWorkbook();
        } else if (file.getName().endsWith("xlsx")) {
            wb = new XSSFWorkbook();
        }

        return (Workbook)wb;
    }

    public static void createExcel(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            (new File(filePath.substring(0, filePath.lastIndexOf(File.separator)))).mkdirs();
        }

        Workbook workbook = getWorkbok(filePath);
        FileOutputStream fileOutputStream = null;
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
        workbook.createSheet();

        try {
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
        } catch (Exception var18) {
            var18.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }

            try {
                workbook.close();
            } catch (IOException var16) {
                var16.printStackTrace();
            }

        }

    }

    public static void checkExcelVaild(File file) throws Exception {
        if (!file.exists()) {
            throw new Exception("文件不存在");
        } else if (!file.isFile() || !file.getName().endsWith("xls") && !file.getName().endsWith("xlsx")) {
            throw new Exception("文件不是Excel");
        }
    }

    private static Object getValue(Cell cell) {
        Object obj = null;
        switch(cell.getCellTypeEnum()) {
            case BOOLEAN:
                obj = cell.getBooleanCellValue();
                break;
            case ERROR:
                obj = cell.getErrorCellValue();
                break;
            case NUMERIC:
                obj = cell.getNumericCellValue();
                break;
            case STRING:
                obj = cell.getStringCellValue();
        }

        return obj;
    }
}