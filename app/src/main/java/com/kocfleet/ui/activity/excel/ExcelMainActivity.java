package com.kocfleet.ui.activity.excel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kocfleet.R;
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.ui.RowClickListener;
import com.kocfleet.ui.adapter.ExcelAdapter;
import com.kocfleet.ui.adapter.ExcelWriteAdapter;
import com.kocfleet.utils.Constants;
import com.kocfleet.utils.ExcelUtil;
import com.kocfleet.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelMainActivity extends AppCompatActivity implements RowClickListener {
    public static final String TAG = ExcelMainActivity.class.getSimpleName();
    private Context mContext;
    private List<Map<Integer, ExcelCellModel>> readExcelList = new ArrayList<>();
    private List<Map<Integer, ExcelCellModel>> clickedExcelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExcelAdapter excelAdapter;
    private Button saveButton;
    private ExcelWriteAdapter excelWriteAdapter;
    private String action;
    private ProgressDialog mProgressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.excel_activity_main);
        mContext = this;
        recyclerView = findViewById(R.id.excel_content_rv);
        saveButton = findViewById(R.id.save_btn);

        Intent intent = getIntent();
        action = intent.getStringExtra(Constants.FILE_ACTION);

        if(action.equals(Constants.FILE_READ)) {
            setReadAdapter();
        } else {
            setWriteAdapter();
        }

        initViews();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excelWriteAdapter.saveCodeHere();
            }
        });
    }

    private void initViews() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.FILE_PATH);
        String fileName = intent.getStringExtra(Constants.FILE_NAME);
        importExcelDeal(path, fileName);
    }

    private void setReadAdapter() {
        saveButton.setVisibility(View.GONE);
        excelAdapter = new ExcelAdapter(readExcelList, this, -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
    }

    private void setWriteAdapter() {
        saveButton.setVisibility(View.VISIBLE);
        excelWriteAdapter = new ExcelWriteAdapter(readExcelList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelWriteAdapter);
    }

    private void importExcelDeal(String path, String fileName) {
        new Thread(() -> {
            Log.i(TAG, "doInBackground: Importing...");
            runOnUiThread(this::showLoading);

            List<Map<Integer, ExcelCellModel>> readExcelNew = ExcelUtil.readExcelNew(mContext, path, fileName);

            Log.i(TAG, "onActivityResult:readExcelNew " + ((readExcelNew != null) ? readExcelNew.size() : ""));

            if (readExcelNew != null && readExcelNew.size() > 0) {
                readExcelList.clear();
                readExcelList.addAll(readExcelNew);
                runOnUiThread(this::hideLoading);
                updateUI();
                Log.i(TAG, "run: successfully imported");
            } else {
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(mContext, "no data", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * refresh RecyclerView
     */
    private void updateUI() {
        runOnUiThread(() -> {
            if (readExcelList != null && readExcelList.size() > 0) {
                if(action.equals(Constants.FILE_READ)) {
                    excelAdapter.notifyDataSetChanged();
                } else {
                    excelWriteAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onRowClicked(Map<Integer, ExcelCellModel> clickedRow) {
        if(clickedExcelList.isEmpty()) {
            clickedExcelList.add(readExcelList.get(0));
            clickedExcelList.add(readExcelList.get(2));
            clickedExcelList.add(clickedRow);
            excelAdapter = new ExcelAdapter(clickedExcelList, this, -1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(excelAdapter);
        }
    }

    @Override
    public void onColumnCLicked(int position) {
        clickedExcelList.add(readExcelList.get(position));
        excelAdapter = new ExcelAdapter(readExcelList, this, position);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
    }

    @Override
    public void onBackPressed() {
        if(clickedExcelList != null && !clickedExcelList.isEmpty()) {
            clickedExcelList = new ArrayList<>();
            setReadAdapter();
        } else {
            super.onBackPressed();
        }
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = Utils.showLoadingDialog(this);
    }
}
