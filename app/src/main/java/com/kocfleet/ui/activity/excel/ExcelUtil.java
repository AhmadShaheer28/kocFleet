package com.kocfleet.ui.activity.excel;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.kocfleet.R;
import com.kocfleet.app.KocfleetApplication;
import com.kocfleet.utils.Constants;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelUtil {
    private static final String TAG = ExcelUtil.class.getSimpleName();
    private static DataFormatter dataFormatter = new DataFormatter();
    private static FormulaEvaluator formulaEvaluator;

    public static List<Map<Integer, Object>> readExcelNew(Context context, String filePath, String fileName) {
        List<Map<Integer, Object>> list = null;
        Workbook wb;
        if (filePath == null) {
            return null;
        }
        String extString;
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            Log.e(TAG, "Please select the correct Excel file");
            return null;
        }
        extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is;
        try {
            if(fileName.equals(Constants.BOATS_CONDITION))
                is = KocfleetApplication.getAppContext().getResources().openRawResource(R.raw.boats_condition);
            else if(fileName.equals(Constants.BOATS_CERTIFICATES))
                is = KocfleetApplication.getAppContext().getResources().openRawResource(R.raw.boats_certificates);
            else
                is = KocfleetApplication.getAppContext().getResources().openRawResource(R.raw.safety_equipment);
            Log.i(TAG, "readExcel: " + extString);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // used to store data
                list = new ArrayList<>();
                // get the first sheet
                Sheet sheet = wb.getSheetAt(0);
                // get the first line header
                Row rowHeader = sheet.getRow(0);
                formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
                int cellsCount = rowHeader.getPhysicalNumberOfCells();
                //store header to the map
                Map<Integer, Object> headerMap = new HashMap<>();
                for (int c = 0; c < cellsCount; c++) {
                    Object value = getCellFormatValue(rowHeader.getCell(c));
                    String cellInfo = "header " + "; c:" + c + "; v:" + value;
                    Log.i(TAG, "readExcelNew: " + cellInfo);

                    headerMap.put(c, value);
                }
                //add  headermap to list
                list.add(headerMap);

                // get the maximum number of rows
                int rownum = sheet.getPhysicalNumberOfRows();
                // get the maximum number of columns
                int colnum = headerMap.size();
                //index starts from 1,exclude header.
                //if you want read line by line, index should from 0.
                for (int i = 1; i < rownum; i++) {
                    Row row = sheet.getRow(i);
                    //storing subcontent
                    Map<Integer, Object> itemMap = new HashMap<>();
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            Object value = getCellFormatValue(row.getCell(j));
                            String cellInfo = "r: " + i + "; c:" + j + "; v:" + value;
                            Log.i(TAG, "readExcelNew: " + cellInfo);
                            itemMap.put(j, value);
                        }
                    } else {
                        break;
                    }
                    list.add(itemMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "readExcelNew: import error " + e);
            Toast.makeText(context, "import error " + e, Toast.LENGTH_SHORT).show();
        }
        return list;
    }

    public static void writeExcelNew(Context context, List<Map<Integer, Object>> exportExcel, Uri uri) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Sheet1"));

            int colums = exportExcel.get(0).size();
            for (int i = 0; i < colums; i++) {
                //set the cell default width to 15 characters
                sheet.setColumnWidth(i, 15 * 256);
            }

            for (int i = 0; i < exportExcel.size(); i++) {
                Row row = sheet.createRow(i);
                Map<Integer, Object> integerObjectMap = exportExcel.get(i);
                for (int j = 0; j < colums; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(String.valueOf(integerObjectMap.get(j)));
                }
            }

            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            Log.i(TAG, "writeExcel: export successful");
            Toast.makeText(context, "export successful", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeExcel: error" + e);
            Toast.makeText(context, "export error" + e, Toast.LENGTH_SHORT).show();
        }
    }
    
    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            cellValue = dataFormatter.formatCellValue(cell, formulaEvaluator);
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}
