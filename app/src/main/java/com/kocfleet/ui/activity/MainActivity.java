package com.kocfleet.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kocfleet.R;
import com.kocfleet.ui.activity.excel.ExcelMainActivity;
import com.kocfleet.ui.base.BaseActivity;
import com.kocfleet.ui.dialog.ActionDialog;
import com.kocfleet.ui.dialog.AuthDialog;
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

public class MainActivity extends BaseActivity implements ActionDialog.UserActionDelegate {

    Button btnBoatCondition;
    Button btnBoatCertificates;
    Button btnSafetyEquipments;
    ActionDialog dialog;
    AuthDialog authDialog;
    Context context;
    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        instance = this;

        btnBoatCondition = findViewById(R.id.boat_condition);
        btnBoatCertificates = findViewById(R.id.boat_certificates);
        btnSafetyEquipments = findViewById(R.id.safety_equipments);

        clickListeners();


    }

    private void clickListeners() {
        btnBoatCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ActionDialog(context, Constants.BOATS_CONDITION, MainActivity.this);
                dialog.show();
            }
        });
        btnBoatCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ActionDialog(context, Constants.BOATS_CERTIFICATES, MainActivity.this);
                dialog.show();
            }
        });
        btnSafetyEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ActionDialog(context, Constants.SAFETY_EQUIPMENTS, MainActivity.this);
                dialog.show();
            }
        });
    }

    private void createIntentForExcel(String path, String fileName) {
        Intent intent = new Intent(MainActivity.this, ExcelMainActivity.class);
        intent.putExtra(Constants.FILE_PATH, path);
        intent.putExtra(Constants.FILE_NAME, fileName);
        startActivity(intent);
    }

    private void showAuthenticationDialog() {
        authDialog = new AuthDialog(this);
        authDialog.show();
        authDialog.setDialogResult(new AuthDialog.OnDialogResult() {
            @Override
            public void finish(String username, String password) {
                if(username.equals("FMT") &&
                        password.equals("2015")) {
                    authDialog.dismiss();
                    Toast.makeText(MainActivity.this,
                            "Not Implemented yet.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Wrong username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void userAction(String action, String fileName) {
        if(action.equals(Constants.FILE_READ)) {
            createIntentForExcel(Constants.FILE_PATH_STRING, fileName);
        } else {
            showAuthenticationDialog();
        }
    }

}