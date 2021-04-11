package com.ruoyi.common.utils.poi;

import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.WordUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author:万登枫
 * @date: 2019/4/29
 */
public class ExcelTemplateUtil {
    /**
     * 根据模板生成 excel 文件
     * @param data 数据
     * @param templatePath 模板文件路径
     * @param fileName 生成 excel 输出流，可保存成文件或返回到前端等
     */
    public static void process(Object data, String templatePath, String fileName) {

        if (data == null || StringUtil.isEmpty(templatePath)) {
            return;
        }
        OutputStream os = null;

        try {
            os = new FileOutputStream(ExcelUtil.getAbsoluteFile(fileName));

            String excelTemplatePath = ExcelTemplateUtil.class.getClassLoader().
                    getResource("excelTemplate").getPath();
            templatePath = excelTemplatePath+ File.separator + templatePath;

            if(templatePath.substring(templatePath.lastIndexOf(".")+1).equals("xls")){
                HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(new File(templatePath)));
                HSSFSheet sheet;
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                    sheet=workbook.getSheetAt(i);
                    processSheet(data, sheet);
                }
                workbook.write(os);
            }else if(templatePath.substring(templatePath.lastIndexOf(".")+1).equals("xlsx")){
                XSSFWorkbook workbook =new XSSFWorkbook(new FileInputStream(new File(templatePath)));
                XSSFSheet sheet;
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                    sheet=workbook.getSheetAt(i);
                    processSheet(data, sheet);
                }
                workbook.write(os);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSheet(Object data, XSSFSheet sheet) {
        Map<Integer, Map<Integer, Cell>> listRecord = new LinkedHashMap<>();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                try {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.matches(".*\\$\\{[\\w.()]+}.*")) {
                        fillCell(cell, cellValue, data);
                    } else if (cellValue.matches(".*\\$\\{[\\w.]+\\[#][\\w.]+}.*")) {
                        Map<Integer, Cell> rowRecord = listRecord.get(i);
                        if (rowRecord == null) {
                            rowRecord =new HashMap<>();
                            listRecord.put(i, rowRecord);
                        }
                        rowRecord.put(j, cell);
                    }
                } catch (Exception ignored) {

                }
            }
        }

        Map<String, List> listInData = new HashMap<>();
        Map<String, CellStyle> listCellStyle = new HashMap<>();
        Map<Cell, String> listCellPath = new HashMap<>();

        for (Integer rowNum:listRecord.keySet()){
            Map<Integer, Cell> colMap=listRecord.get(rowNum);
            Pattern p = Pattern.compile("\\$\\{[\\w.\\[#\\]]+}");
            Set<String> listPath = new HashSet<>();
            for (Integer colNum:colMap.keySet()){
                Cell cell= colMap.get(colNum);
                String cellValue = cell.getStringCellValue();
                Matcher m = p.matcher(cellValue);
                if (m.find()) {
                    String reg = m.group();
                    String regPre = reg.substring(2, reg.indexOf("["));
                    String regSuf = reg.substring(reg.lastIndexOf("].") + 2, reg.length() - 1);
                    listPath.add(regPre);
                    listCellStyle.put(String.format("%s.%s", regPre, regSuf), cell.getCellStyle());
                    listCellPath.put(cell, String.format("%s#%s", regPre, regSuf));
                }
            }
            int maxRow = 0;
            for (String s : listPath) {
                Object list = getAttributeByPath(data, s);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list instanceof List) {
                    int len = ((List) list).size();
                    maxRow = maxRow > len ? maxRow : len;
                    listInData.put(s, ((List) list));
                } else {
                    throw new IllegalArgumentException(String.format("%s is not a list but a %s", s, list.getClass().getSimpleName()));
                }
            }
            if (maxRow > 1) {
                int endRow = sheet.getLastRowNum();
                sheet.shiftRows(rowNum + 1, endRow + 1, maxRow - 1);
            }
        }

        for (Integer rowNum:listRecord.keySet()){
            Map<Integer, Cell> colMap=listRecord.get(rowNum);
            for (Map.Entry<Integer, Cell> entry : colMap.entrySet()) {
                Integer colNum = entry.getKey();
                Cell cell = entry.getValue();
                String path = listCellPath.get(cell);
                String[] pathData = path.split("#");
                List list = listInData.get(pathData[0]);
                int baseRowIndex = cell.getRowIndex();
                int colIndex = cell.getColumnIndex();
                CellStyle style = listCellStyle.get(String.format("%s.%s", pathData[0], pathData[1]));
                if(list == null || list.size() ==0){
                    XSSFRow row = sheet.getRow(baseRowIndex);
                    if (row == null) {
                        row = sheet.createRow(baseRowIndex);
                    }
                    sheet.removeRow(row);
                }else{
                    for (int i = 0; i < list.size(); i++) {
                        int rowIndex = baseRowIndex + i;
                        XSSFRow row = sheet.getRow(rowIndex);
                        if (row == null) {
                            row = sheet.createRow(rowIndex);
                        }
                        Cell cellToFill = row.getCell(colIndex);
                        if (cellToFill == null) {
                            cellToFill = row.createCell(colIndex);
                        }
                        cellToFill.setCellStyle(style);
                        if("index".equals(pathData[1])){
                            setCellValue(cellToFill, i+1);
                        }else{
                            setCellValue(cellToFill, getAttribute(list.get(i), pathData[1]));
                        }

                    }
                }

            }
        }

    }

    private static void processSheet(Object data, HSSFSheet sheet) {
        Map<Integer, Map<Integer, Cell>> listRecord = new LinkedHashMap<>();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                try {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.matches(".*\\$\\{[\\w.()]+}.*")) {
                        fillCell(cell, cellValue, data);
                    } else if (cellValue.matches(".*\\$\\{[\\w.]+\\[#][\\w.]+}.*")) {
                        Map<Integer, Cell> rowRecord = listRecord.get(i);
                        if (rowRecord == null) {
                            rowRecord =new HashMap<>();
                            listRecord.put(i, rowRecord);
                        }
                        rowRecord.put(j, cell);
                    }
                } catch (Exception ignored) {

                }
            }
        }

        Map<String, List> listInData = new HashMap<>();
        Map<String, CellStyle> listCellStyle = new HashMap<>();
        Map<Cell, String> listCellPath = new HashMap<>();

        for (Integer rowNum:listRecord.keySet()){
            Map<Integer, Cell> colMap=listRecord.get(rowNum);
            Pattern p = Pattern.compile("\\$\\{[\\w.\\[#\\]]+}");
            Set<String> listPath = new HashSet<>();
            for (Integer colNum:colMap.keySet()){
                Cell cell= colMap.get(colNum);
                String cellValue = cell.getStringCellValue();
                Matcher m = p.matcher(cellValue);
                if (m.find()) {
                    String reg = m.group();
                    String regPre = reg.substring(2, reg.indexOf("["));
                    String regSuf = reg.substring(reg.lastIndexOf("].") + 2, reg.length() - 1);
                    listPath.add(regPre);
                    listCellStyle.put(String.format("%s.%s", regPre, regSuf), cell.getCellStyle());
                    listCellPath.put(cell, String.format("%s#%s", regPre, regSuf));
                }
            }
            int maxRow = 0;
            for (String s : listPath) {
                Object list = getAttributeByPath(data, s);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list instanceof List) {
                    int len = ((List) list).size();
                    maxRow = maxRow > len ? maxRow : len;
                    listInData.put(s, ((List) list));
                } else {
                    throw new IllegalArgumentException(String.format("%s is not a list but a %s", s, list.getClass().getSimpleName()));
                }
            }
            if (maxRow > 1) {
                int endRow = sheet.getLastRowNum();
                sheet.shiftRows(rowNum + 1, endRow + 1, maxRow - 1);
            }
        }

        for (Integer rowNum:listRecord.keySet()){
            Map<Integer, Cell> colMap=listRecord.get(rowNum);
            for (Map.Entry<Integer, Cell> entry : colMap.entrySet()) {
                Integer colNum = entry.getKey();
                Cell cell = entry.getValue();
                String path = listCellPath.get(cell);
                String[] pathData = path.split("#");
                List list = listInData.get(pathData[0]);
                int baseRowIndex = cell.getRowIndex();
                int colIndex = cell.getColumnIndex();
                CellStyle style = listCellStyle.get(String.format("%s.%s", pathData[0], pathData[1]));
                if(list == null || list.size() == 0){
                    HSSFRow row = sheet.getRow(baseRowIndex);
                    if (row == null) {
                        row = sheet.createRow(baseRowIndex);
                    }
                    sheet.removeRow(row);
                }else{
                    for (int i = 0; i < list.size(); i++) {
                        int rowIndex = baseRowIndex + i;
                        HSSFRow row = sheet.getRow(rowIndex);
                        if (row == null) {
                            row = sheet.createRow(rowIndex);
                        }
                        row.setHeight((short)(60*20));
                        Cell cellToFill = row.getCell(colIndex);
                        if (cellToFill == null) {
                            cellToFill = row.createCell(colIndex);
                        }
                        cellToFill.setCellStyle(style);
                        if("index".equals(pathData[1])){
                            setCellValue(cellToFill, i+1);
                        }else{
                            setCellValue(cellToFill, getAttribute(list.get(i), pathData[1]));
                        }

                    }
                }

            }
        }

    }

    /**
     * @param cell 要置换的单元格
     * @param expression 单元格内的置换标记
     * @param data 数据源
     */
    private static void fillCell(Cell cell, String expression, Object data) {
        Pattern p = Pattern.compile("\\$\\{[\\w.\\[\\]()]+}");
        Matcher m = p.matcher(expression);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String exp = m.group();
            String path = exp.substring(2, exp.length() - 1);
            Object value = getAttributeByPath(data, path);
            m.appendReplacement(sb, value == null ? "" : value.toString());
        }
        setCellValue(cell, sb.toString());
    }

    /**
     * @param cell 单元格
     * @param value 值
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Date) {
            cell.setCellValue(DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss",(Date) value));
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Character) {
            cell.setCellValue((Character) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     *
     * @param obj 访问对象
     * @param path 属性路径，形如(cls.type, cls.students.size())
     * @return
     */
    private static Object getAttributeByPath(Object obj, String path) {
        String[] paths = path.split("\\.");
        Object o = obj;
        for (String s : paths) {
            o = getAttribute(o, s);
        }
        return o;
    }

    private static Object getAttribute(Object obj, String member) {
        if (obj == null) {
            return null;
        }
        boolean isMethod = member.endsWith("()");
        if (!isMethod && obj instanceof Map) {
            return ((Map) obj).get(member);
        }
        try {
            Class<?> cls = obj.getClass();
            if (isMethod) {
                Method method = cls.getDeclaredMethod(member.substring(0, member.length() - 2));
                return method.invoke(obj);
            } else {
                Field field = cls.getDeclaredField(member);
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    //设置响应头
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
//                fileName = new String(fileName.getBytes(),"UTF-8");
                fileName = URLEncoder.encode(fileName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
