package com.kocfleet.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kocfleet.R;
import com.kocfleet.ui.base.BaseActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends BaseActivity {

    Button btnBoatCondition;
    Button btnBoatCertificates;
    Button btnSafetyEquipments;
    ArrayList<List<String>> table;
    private int mRow = 0;
    private int mCol = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBoatCondition = findViewById(R.id.boat_condition);
        btnBoatCertificates = findViewById(R.id.boat_certificates);
        btnSafetyEquipments = findViewById(R.id.safety_equipments);

        clickListeners();


    }

    private void clickListeners() {
        btnBoatCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is =
                        getApplication().getResources().openRawResource(R.raw.boats_condition);
                getExcelSize(is);
            }
        });
        btnBoatCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is =
                        getApplication().getResources().openRawResource(R.raw.boats_certificates);
                getExcelSize(is);
            }
        });
        btnSafetyEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is =
                        getApplication().getResources().openRawResource(R.raw.safety_equipment);
                getExcelSize(is);
            }
        });
    }

    private void getExcelSize(InputStream is) {
        int col = 0;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    col++;
                }
            }
            getExcelSheetData(is, col);
        } catch (Exception e) {
            Log.d("MY_READ_XLSX", e.getMessage());
        }
    }
    
    private void getExcelSheetData(InputStream is, int col) {
        table = new ArrayList<>(col);
        for(int i = 0; i < col; i++)  {
            table.add(new ArrayList<>());
        }
        try {

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(is);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            //Iterate through each rows one by one
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                //For each row, iterate through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    //Check the cell type and format accordingly
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            table.get(mRow).add("");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            table.get(mRow).add(cell.getStringCellValue());
                            //Log.d("MY_READ_XLSX", cell.getStringCellValue() + "");
                            break;

                    }
                }
                mRow++;
            }
            Toast.makeText(this, "File read successfully!", Toast.LENGTH_SHORT).show();
            is.close();
        } catch (Exception e) {
            Log.d("MY_READ_XLSX", e.getMessage());
        }
    }

}