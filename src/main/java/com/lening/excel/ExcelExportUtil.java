package com.lening.excel;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class ExcelExportUtil<T> {
    //从哪一行开始插入数据
    private int rowIndex;
    //获取指定位置的xls表的元素样式
    private int styleIndex;
    //private String templatePath;

    //需要该类的Class获取声明字段
    private Class clazz;
    //实体类的声明字段
    private Field fields[];

    //这个需要导入指定样式的xls文件
    public ExcelExportUtil(Class clazz,int rowIndex,int styleIndex) {
        this.clazz = clazz;
        this.rowIndex = rowIndex;
        this.styleIndex = styleIndex;
        fields = clazz.getDeclaredFields();//获取声明字段
    }
    //通常使用第二个
    public ExcelExportUtil(Class clazz,int rowIndex) {
        this.clazz = clazz;
        this.rowIndex = rowIndex;
        this.styleIndex = 0;
        fields = clazz.getDeclaredFields();//获取声明字段
    }
    /**
     *  基于注解导出2
     * @param response
     * @param objs  引入数据源
     * @param fileName  文件名称
     * @throws Exception
     */
    public void myExport(HttpServletResponse response,List<T> objs, String fileName) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("测试xls导出");
        //String[] title = {"编号", "名称", "年龄"};
        Row rowTitle = sheet.createRow(0);
        for (int j=0;j<fields.length;j++) {
            ExcelAttribute ea = fields[j].getAnnotation(ExcelAttribute.class);
           if(ea!=null){
               if(ea.sort()!=-1&&ea.name()!=""){
                   Cell cell = rowTitle.createCell(ea.sort());
                   cell.setCellValue(ea.name());
               }
           }
        }
        AtomicInteger datasAi = new AtomicInteger(rowIndex);
        for (T t : objs) {
            Row row = sheet.createRow(datasAi.getAndIncrement());
            for (int i = 0; i < fields.length; i++) {
                ExcelAttribute eab = fields[i].getAnnotation(ExcelAttribute.class);
             if(eab!=null){
                 if(eab.sort()!=-1&&eab.name()!=""){
                     Cell cell = row.createCell(eab.sort());
                     for (Field field : fields) {
                         if (field.isAnnotationPresent(ExcelAttribute.class)) {
                             field.setAccessible(true);
                             ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                                if (ea.sort()!=-1&&i==ea.sort()) { //列序号
                                    if (field.get(t) != null) {
                                        cell.setCellValue(field.get(t).toString());
                                    } else {
                                        cell.setCellValue("");
                                    }
                                }
                         }
                     }
                 }
             }

            }
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
    }



    /**
     * 基于注解导出1
     * (包含样式导出,需要引入一个样式xls表)
     * @param ,
     */
    public void export(HttpServletResponse response,InputStream is, List<T> objs, String fileName) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle[] styles = getTemplateStyles(sheet.getRow(styleIndex));
        AtomicInteger datasAi = new AtomicInteger(rowIndex);
        System.out.println("rowIndex:"+datasAi.getAndIncrement());
        for (T t : objs) {
            Row row = sheet.createRow(datasAi.getAndIncrement());
            System.out.println(styles.length);
            for(int i=0;i<styles.length;i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(styles[i]);
                for (Field field : fields) {
                    if(field.isAnnotationPresent(ExcelAttribute.class)){
                        field.setAccessible(true);
                        ExcelAttribute ea = field.getAnnotation(ExcelAttribute.class);
                        if(i == ea.sort()) { //列序号
                            if(field.get(t)==null){
                                cell.setCellValue("");
                            }else{
                                cell.setCellValue(field.get(t).toString());
                            }

                        }
                    }
                }
            }
        }
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
    }

    /**
     * 获取指定行的样式
     * @param row
     * @return
     */
    public CellStyle[] getTemplateStyles(Row row) {
        CellStyle [] styles = new CellStyle[row.getLastCellNum()];
        for(int i=0;i<row.getLastCellNum();i++) {
            styles[i] = row.getCell(i).getCellStyle();
        }
        return styles;
    }


}