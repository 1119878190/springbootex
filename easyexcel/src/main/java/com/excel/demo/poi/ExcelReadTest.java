package com.excel.demo.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Date;

public class ExcelReadTest {

    String path = "E:\\other\\";

    /**
     * 读取excel 03版本
     *
     * @throws Exception
     */
    @Test
    public void testWrite03() throws Exception {

        // 获取流
        FileInputStream fileInputStream = new FileInputStream("E:\\other\\test.xls");
        // 1.获取工作簿
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        // 2.获取表
        Sheet sheet = workbook.getSheetAt(0);

        // 获取行
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(1);

        // 读取值的时候，一定要注意类型
        //String value = cell.getStringCellValue();
        double numericCellValue = cell.getNumericCellValue();
        fileInputStream.close();

        System.out.println(numericCellValue);

    }

    /**
     * 读取文件 不同类型
     * @throws Exception
     */
    @Test
    public void testCellType() throws Exception {
        // 获取文件流
        FileInputStream fileInputStream = new FileInputStream("E:\\other\\test.xls");

        // 获取工作簿
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        // 获取表
        Sheet sheetAt = workbook.getSheetAt(0);

        // 获取标题内容 第一行
        Row rowTitle = sheetAt.getRow(0);
        if (null != rowTitle) {
            // 获取第一行有多少列
            int cellCount = rowTitle.getPhysicalNumberOfCells();
            for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                Cell cell = rowTitle.getCell(cellNum);
                if (null != cell) {
                    int cellType = cell.getCellType();
                    String stringCellValue = cell.getStringCellValue();
                    System.out.print(stringCellValue + " | ");
                }
            }
            System.out.println();
            // 获取表中的数据
            int rowCount = sheetAt.getPhysicalNumberOfRows();// 获取有多少行
            for (int rowNum = 1; rowNum < rowCount; rowNum++) {
                Row row = sheetAt.getRow(rowNum);
                if (null != row) {
                    for (int cellNum = 0; cellNum < cellCount; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        // 匹配列的数据类型
                        if (cell != null) {
                            int cellType = cell.getCellType();
                            String value = "";
                            switch (cellType) {
                                case HSSFCell
                                        .CELL_TYPE_STRING://字符串
                                    value = cell.getStringCellValue();
                                    System.out.print(value);
                                    break;
                                case HSSFCell
                                        .CELL_TYPE_BOOLEAN:// 布尔
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    System.out.print(value);
                                    break;
                                case HSSFCell
                                        .CELL_TYPE_BLANK://空
                                    break;
                                case HSSFCell
                                        .CELL_TYPE_NUMERIC://数字：（日期，普通数字）
                                    if (HSSFDateUtil.isCellDateFormatted(cell)){
                                        Date date = cell.getDateCellValue();
                                        String s = new DateTime(date).toString("yyyy-MM-dd ");
                                        System.out.print(s);
                                    }else {
                                        // 不是日期格式，防止数字过长
                                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                        value = cell.toString();
                                        System.out.print(value);
                                    }
                                    break;
                                case HSSFCell
                                        .CELL_TYPE_ERROR:
                                    System.out.print("数据类型错误");
                                    break;
                                default:
                                    break;

                            }
                        }
                        if (cell.getColumnIndex()+1 == cellCount){
                            System.out.println();
                        }
                    }
                }
            }
        }
    }


    public void testFormula(){

    }
}
