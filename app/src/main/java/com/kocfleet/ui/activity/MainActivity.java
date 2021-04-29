package com.kocfleet.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kocfleet.R;
import com.kocfleet.ui.activity.excel.ExcelMainActivity;
import com.kocfleet.ui.base.BaseActivity;
import com.kocfleet.utils.Constants;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
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
    ArrayList<String> cols;

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
                Intent intent = new Intent(MainActivity.this, ExcelMainActivity.class);
                intent.putExtra(Constants.FILE_PATH, Constants.FILE_PATH_STRING);
                intent.putExtra(Constants.FILE_NAME, Constants.BOATS_CONDITION);
                startActivity(intent);
            }
        });
        btnBoatCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExcelMainActivity.class);
                intent.putExtra(Constants.FILE_PATH, Constants.FILE_PATH_STRING);
                intent.putExtra(Constants.FILE_NAME, Constants.BOATS_CERTIFICATES);
                startActivity(intent);
            }
        });
        btnSafetyEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExcelMainActivity.class);
                intent.putExtra(Constants.FILE_PATH, Constants.FILE_PATH_STRING);
                intent.putExtra(Constants.FILE_NAME, Constants.SAFETY_EQUIPMENTS);
                startActivity(intent);
            }
        });
    }

    private void getExcelSize(InputStream is) {
        cols = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(is);
            XSSFSheet sheet = workbook.getSheetAt(0);

            DataFormatter dataFormatter = new DataFormatter();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cols.add(dataFormatter.formatCellValue(cell, formulaEvaluator));
                }
            }
            is.close();
        } catch (Exception e) {
            Log.d("MY_READ_XLSX", e.getMessage());
        }
    }

}