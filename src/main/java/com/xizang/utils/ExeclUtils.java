package com.xizang.utils;


import com.xizang.data.WaterBean;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.xizang.utils.DataUtils.parseData;
import static com.xizang.utils.DataUtils.show;

/**
 * @Author ： 杨冲
 * @DateTime ： 2023/6/11 15:45
 */
public class ExeclUtils{
    public static void main(String[] args) {
        String str = "/Users/yangchong/Desktop/bb_new/bb.xlsx";
        String str1 = "/Users/yangchong/Desktop/bb_new/bb_d.xlsx";
        List<List<String>> lists = readExcel(str);
        writeExcel(str1, parseData(lists));
    }

    public static void writeExcel(String path, List<List<WaterBean>> data) {
        try(FileOutputStream fos = new FileOutputStream(path)) {
            File file = new File(path);
            if (!file.exists()) file.createNewFile();
            // 获取文件输入流

            //1.创建WorkBook对象
            Workbook workbook = new XSSFWorkbook();

            //2.创建sheet页
            Sheet sheet = workbook.createSheet("结果");
            //初始化表头
            int index = 0;
            for (int i = 0; i < data.size(); i++) {
                List<WaterBean> waterBeans = data.get(i);
                for (int j = 0; j < waterBeans.size(); j++) {
                    WaterBean bean = waterBeans.get(j);
                    Row row = sheet.createRow(index);
                    row.createCell(0).setCellValue(bean.getCode());
                    row.createCell(1).setCellValue(bean.getName());
                    row.createCell(2).setCellValue(bean.getTime().format(DateUtils.normalFormatter));
                    row.createCell(3).setCellValue(bean.getHigh());

                    index++;

                }
                index++;
            }
            //5.数据刷到Excel文件中
            workbook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> readExcel(String path) {
        List<List<String>> datas = new ArrayList<>();

        try {
            // 获取文件输入流
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            // 定义一个org.apache.poi.ss.usermodel.Workbook的变量
            Workbook workbook = null;
            // 截取路径名 . 后面的后缀名，判断是xls还是xlsx
            // 如果这个判断不对，就把equals换成 equalsIgnoreCase()
            if (path.endsWith("xls")){
                workbook = new HSSFWorkbook(inputStream);
            }else if (path.endsWith("xlsx")){
                workbook = new XSSFWorkbook(inputStream);
            }

            // 获取第一张表
            Sheet sheet = workbook.getSheetAt(0);
            // sheet.getPhysicalNumberOfRows()获取总的行数
            // 循环读取每一行
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                // 循环读取每一个格
                Row row = sheet.getRow(i);
                // row.getPhysicalNumberOfCells()获取总的列数
                List<String> line = new ArrayList<>();
                for (int index = 0; index < row.getPhysicalNumberOfCells(); index++) {
                    Cell cell = row.getCell(index);
                    // 转换为字符串类型
                    //System.out.println(cell.getCellTypeEnum().name());
                    //System.out.println(getValue(cell));
                    //cell.setCellType(CellType.STRING);
                    //String id = cell.getStringCellValue();
                    //System.out.print(id+ "\t");

                    // 获取得到字符串
                    line.add(getValue(cell));

                }
                datas.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    private static String getValue(Cell xssfCell) {
        if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = xssfCell.getDateCellValue();
                return sdf.format(date);

            } else {
                String xssfCell2 = String.valueOf(xssfCell.getNumericCellValue());
                DecimalFormat df = new DecimalFormat("#.#########");
                return df.format(Double.valueOf(xssfCell2));
            }
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }
}