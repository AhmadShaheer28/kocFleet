package com.kocfleet.ui.activity.excel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kocfleet.R;
import com.kocfleet.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelMainActivity extends AppCompatActivity {
    public static final String TAG = ExcelMainActivity.class.getSimpleName();
    private Context mContext;
    private List<Map<Integer, Object>> readExcelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExcelAdapter excelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_activity_main);
        mContext = this;

        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.excel_content_rv);
        excelAdapter = new ExcelAdapter(readExcelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.FILE_PATH);
        String fileName = intent.getStringExtra(Constants.FILE_NAME);
        importExcelDeal(path, fileName);
    }

    private void importExcelDeal(String path, String fileName) {
        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");
            runOnUiThread(() -> Toast.makeText(mContext, "Importing...", Toast.LENGTH_SHORT).show());

            List<Map<Integer, Object>> readExcelNew = ExcelUtil.readExcelNew(mContext, path, fileName);

            Log.i(TAG, "onActivityResult:readExcelNew " + ((readExcelNew != null) ? readExcelNew.size() : ""));

            if (readExcelNew != null && readExcelNew.size() > 0) {
                readExcelList.clear();
                readExcelList.addAll(readExcelNew);
                updateUI();

                Log.i(TAG, "run: successfully imported");
                runOnUiThread(() -> Toast.makeText(mContext, "successfully imported", Toast.LENGTH_SHORT).show());
            } else {
                runOnUiThread(() -> Toast.makeText(mContext, "no data", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * refresh RecyclerView
     */
    private void updateUI() {
        runOnUiThread(() -> {
            if (readExcelList != null && readExcelList.size() > 0) {
                excelAdapter.notifyDataSetChanged();
            }
        });
    }

}
