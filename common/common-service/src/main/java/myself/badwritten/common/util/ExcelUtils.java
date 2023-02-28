package myself.badwritten.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * excel
 * 
 * dty
 *
 */
public class ExcelUtils {
    
    
    /** * 导出Excel * 
     * @param sheetName sheet名称
     *  * @param title 标题 
     *  * @param values 内容
     *  * @param wb HSSFWorkbook对象 
     *  * @return */ 
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb,String mergeCell,Integer cellNum ){
        
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setColumnWidth(0, 5000);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        //声明列对象
        HSSFCell cell = null;
        if (StringUtils.isNotEmpty(mergeCell)) {
          //合并单元格
          CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,cellNum);//起始行,结束行,起始列,结束列
          sheet.addMergedRegion(callRangeAddress);
          cell = row.createCell(0);
          cell.setCellValue(mergeCell);
          row = sheet.createRow(1);
          // HSSFCell cell = null;
          for(int i=0;i<title.length;i++){ 
              cell = row.createCell(i); 
              cell.setCellValue(title[i]); 
          }
          //创建内容 
          for(int i=0;i<values.length;i++){ 
              row = sheet.createRow(i + 2); 
              for(int j=0;j<values[i].length;j++){ 
                  //将内容按顺序赋给对应的列对象 
                  row.createCell(j).setCellValue(values[i][j]); 
              } 
           }
        }else {
         // HSSFCell cell = null; 
            //创建标题 
            for(int i=0;i<title.length;i++){ 
                cell = row.createCell(i); 
                cell.setCellValue(title[i]); 
            } 
            //创建内容 
            for(int i=0;i<values.length;i++){
                row = sheet.createRow(i + 1);
                for(int j=0;j<values[i].length;j++){ 
                    //将内容按顺序赋给对应的列对象 
                    row.createCell(j).setCellValue(values[i][j]); 
                } 
            } 
        }
          
        return wb;
          
    }

    /**
     * 注意：对象是XSSFWorkbook对象
     * 获取样式居中，边框，自动换行等得通用样式
     *
     * @param workbook XSSFWorkbook对象
     * @param font  字体格式
     * @param fontName 字体名称
     * @return XSSFCellStyle样式对象
     */
    public static XSSFCellStyle getXssfCellStyle(XSSFWorkbook workbook, XSSFFont font,String fontName) {
        // 设置字体名字
        if (fontName != null){
            font.setFontName(fontName);
        }
        // 设置样式;
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(true);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * 返回string类型的值
     * @param cell 单元格
     * @return
     */
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType();
        switch (type) {
            case BLANK:
                return "";
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    return DateUtils.format(cell.getDateCellValue(), "yyyy-MM-dd");
                } else {
                    return new DecimalFormat("#.##").format(cell.getNumericCellValue());
                }
            case STRING:
                return cell.getStringCellValue().trim();
            default:
                return "";
        }
    }

    /**
     * 解析excel
     * @param file excel文件
     * @return list 且每excel行为一组数据的string数组
     */
    public static List<String[]> resolveExcel(File file){
        if (file == null){
            throw new RuntimeException("文件不存在");
        }
        List<String[]> itemList = new ArrayList<>();
        XSSFWorkbook workbook = null;
        try {
            FileInputStream is = new FileInputStream(file);
            workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            XSSFRow row;
            String[] item;
            for (int i = 1, rowSize = lastRowNum + 1; i < rowSize; i++){
                row = sheet.getRow(i);
                if (row != null) {
                    int lastCellNum = row.getLastCellNum();
                    item = new String[10];
                    String value;
                    for (int k = 0; k < lastCellNum; k++) {
                        if (StringUtils.isNotEmpty(ExcelUtils.getCellValue(row.getCell(k)))){
                            value = ExcelUtils.getCellValue(row.getCell(k));
                        } else {
                            value = "";
                        }
                        item[k] = value;
                    }
                    itemList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return itemList;
    }
}
