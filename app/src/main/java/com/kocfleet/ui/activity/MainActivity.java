package com.kocfleet.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kocfleet.R;
import com.kocfleet.ui.base.BaseActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends BaseActivity {

    Button btnBoatCondition;
    Button btnBoatCertificates;
    Button btnSafetyEquipments;

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
                ReadExcelDemo(is);
            }
        });
        btnBoatCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is =
                        getApplication().getResources().openRawResource(R.raw.boats_certificates);
                ReadExcelDemo(is);
            }
        });
        btnSafetyEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream is =
                        getApplication().getResources().openRawResource(R.raw.safety_equipment);
                ReadExcelDemo(is);
            }
        });
    }

    private void ReadExcelDemo(InputStream is) {
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
                        case Cell.CELL_TYPE_NUMERIC:
                            Log.d("MY_READ_XLSX", cell.getNumericCellValue() + "t");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            Log.d("MY_READ_XLSX", cell.getStringCellValue() + "t");
                            break;
                    }
                }
                System.out.println("");
            }
            is.close();
        } catch (Exception e) {
            Log.d("MY_READ_XLSX", e.getMessage());
        }
    }

}