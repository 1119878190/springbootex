package com.excel.demo.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.FileOutputStream;

public class ExcelWriteTest {

    String path = "E:\\other\\";

    /**
     * 生成excel 03版本
     * @throws Exception
     */
    @Test
    public void testWrite03() throws Exception {
        // 1.创建工作簿
        Workbook workbook = new HSSFWorkbook();
        // 2.创建一个工作表
        Sheet sheet = workbook.createSheet("sheet1");
        // 3.创建一个行 (1,1)
        Row row1 = sheet.createRow(0);
        // 4.创建一个单元格
        Cell cell1 = row1.createCell(0);
        // 写入数据
        cell1.setCellValue("今日新增观众");
        // (1,2)
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue(666);

        //创建第二行(2,1)
        Row row2 = sheet.createRow(1);
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("第二行第一列");

        // (2,2)
        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

        // 生成一张表（IO流） 03版本使用xls结尾！！
        FileOutputStream fileOutputStream = new FileOutputStream(path + "test.xls");
        workbook.write(fileOutputStream);

        // 关闭流
        fileOutputStream.close();

        System.out.println("excel生成完毕");
    }

    /**
     * 生成excel 07版本
     * @throws Exception
     */
    @Test
    public void testWrite07() throws Exception {
        // 1.创建工作簿
        Workbook workbook = new XSSFWorkbook();
        // 2.创建一个工作表
        Sheet sheet = workbook.createSheet("sheet1");
        // 3.创建一个行 (1,1)
        Row row1 = sheet.createRow(0);
        // 4.创建一个单元格
        Cell cell1 = row1.createCell(0);
        // 写入数据
        cell1.setCellValue("今日新增观众");
        // (1,2)
        Cell cell2 = row1.createCell(1);
        cell2.setCellValue(666);

        //创建第二行(2,1)
        Row row2 = sheet.createRow(1);
        Cell cell21 = row2.createCell(0);
        cell21.setCellValue("第二行第一列");

        // (2,2)
        Cell cell22 = row2.createCell(1);
        String time = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        cell22.setCellValue(time);

        // 生成一张表（IO流） 07版本使用xlsx结尾！！
        FileOutputStream fileOutputStream = new FileOutputStream(path + "test07.xlsx");
        workbook.write(fileOutputStream);

        // 关闭流
        fileOutputStream.close();

        System.out.println("excel生成完毕");
    }

    /**
     * 03大数据
     */
    @Test
    public void testWrite03BigData() throws Exception {
        // 计算时间差
        long begin = System.currentTimeMillis();

        //创建工作簿
        Workbook workbook = new HSSFWorkbook();
        //创建表
        Sheet sheet001 = workbook.createSheet();
        // 写入数据
        for (int rowNumber = 0; rowNumber < 65536; rowNumber++) {
            // 创建行
            Row row1 = sheet001.createRow(rowNumber);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                // 创建列
                Cell cell = row1.createCell(cellNum);
                // 写入数据
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(path+"test03BigData.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("时间="+(double)(end-begin)/1000);
    }

    /**
     * 07大数据 耗时较长  优化：缓存
     */
    @Test
    public void testWrite07BigData() throws Exception {
        // 计算时间差
        long begin = System.currentTimeMillis();

        //创建工作簿
        Workbook workbook = new XSSFWorkbook();
        //创建表
        Sheet sheet001 = workbook.createSheet();
        // 写入数据
        for (int rowNumber = 0; rowNumber < 100000; rowNumber++) {
            // 创建行
            Row row1 = sheet001.createRow(rowNumber);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                // 创建列
                Cell cell = row1.createCell(cellNum);
                // 写入数据
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(path+"test07BigData.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        long end = System.currentTimeMillis();
        System.out.println("时间="+(double)(end-begin)/1000);
    }

    /**
     * 07大数据 SXSSF
     */
    @Test
    public void testWrite07BigDataS() throws Exception {
        // 计算时间差
        long begin = System.currentTimeMillis();

        //创建工作簿
        Workbook workbook = new SXSSFWorkbook();
        //创建表
        Sheet sheet001 = workbook.createSheet();
        // 写入数据
        for (int rowNumber = 0; rowNumber < 100000; rowNumber++) {
            // 创建行
            Row row1 = sheet001.createRow(rowNumber);
            for (int cellNum = 0; cellNum < 10; cellNum++) {
                // 创建列
                Cell cell = row1.createCell(cellNum);
                // 写入数据
                cell.setCellValue(cellNum);
            }
        }
        System.out.println("over");
        FileOutputStream fileOutputStream = new FileOutputStream(path+"test07BigDataS.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        // 清除临时文件
        ((SXSSFWorkbook)workbook).dispose();
        long end = System.currentTimeMillis();
        System.out.println("时间="+(double)(end-begin)/1000);
    }

}
