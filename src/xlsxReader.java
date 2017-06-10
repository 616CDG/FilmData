package utility;

import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by chentiange on 2017/5/19.
 */
public class xlsxReader {
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public xlsxReader(String filepath) {
        if (filepath == null) {
            return;
        }
        try {
            InputStream is = new FileInputStream(filepath);
            wb = new XSSFWorkbook(is);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String[] readExcelTitle() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        System.out.println("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = (String) getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception("Workbook对象为空！");
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i, cellValue);
        }
        return content;
    }


    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {// 如果是纯数字

                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    public static boolean isValid(char c) {

        if (c >= 'a' && c <= 'z') {
            return true;
        } else if (c >= 'A' && c <= 'Z') {
            return true;
        } else if ((c == '-') || (c == '_') || (c == '.') || (c == '/')|| (c == ' ')) {
            return true;
        } else if (c >= '0' && c <= '9') {
            return true;
        }

        return false;
    }

    public static String handleString(String s) {
        int l = s.length();
        int in = 0;
        for (int i = 0; i < l; i++) {
            if (isValid(s.charAt(i)))
                in++;
        }
        return s.substring(0, in);
    }

    public static int getMonth(String m){
        String[] ms = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        for (int i = 0;i < ms.length;i++){
            if (m.equals(ms[i])){
                return i + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String repo = "";
        String repoBefore = "";
        String handover = "";
        String takeover = "";
        int start_y = 0;
        int start_m = 0;
        int start_d = 0;
        int e_y = 0;
        int e_m = 0;
        int e_d = 0;
        String allrepos = "";

        String repo_ret = "";
        String handover_ret = "";
        String takeover_ret = "";
        String s_y_ret = "";
        String s_m_ret = "";
        String s_d_ret = "";
        String e_y_ret = "";
        String e_m_ret = "";
        String e_d_ret = "";

        String t1 = "",t2 = "";

        try {
            String filepath = "/Users/chentiange/Downloads/top300 projects.xlsx";
            xlsxReader excelReader = new xlsxReader(filepath);
            Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
            HashSet usernames = new HashSet<String>();
            System.out.println("获得Excel表格的内容:");


            for (int i = 1; i <= map.size(); i++) {
                String douStr = (String) map.get(i).get(0);
                String had = (String) map.get(i).get(5);
                if ((douStr.length() > 0)&&(had.equals("yes")||had.equals("Yes"))) {

                    String temp = (String) map.get(i).get(1);
                    temp = handleString(temp);
                    if (temp != null && !temp.equals("")){
                        repoBefore = repo;
                        repo = temp;
                    }

                }

                if ((!had.equals("no"))&&(!had.equals("No")&&(!had.equals("no "))&&(!had.equals("")))){
                    if (!repo.equals(repoBefore)){
                        allrepos += repo + ",";
                    }


                }
            }

            System.out.println(allrepos);
//            System.out.println(repo_ret);
//            System.out.println(handover_ret);
//            System.out.println(takeover_ret);
//            System.out.println(s_y_ret);
//            System.out.println(s_m_ret);
//            System.out.println(s_d_ret);
//            System.out.println(e_y_ret);
//            System.out.println(e_m_ret);
//            System.out.println(e_d_ret);


            for (Object name : usernames) {
                System.out.println((String) name);
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
