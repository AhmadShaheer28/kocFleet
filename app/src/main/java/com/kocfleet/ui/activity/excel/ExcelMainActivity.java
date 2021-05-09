package com.kocfleet.ui.activity.excel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kocfleet.R;
import com.kocfleet.ui.RowClickListener;
import com.kocfleet.ui.adapter.ExcelAdapter;
import com.kocfleet.ui.adapter.ExcelWriteAdapter;
import com.kocfleet.utils.Constants;
import com.kocfleet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelMainActivity extends AppCompatActivity implements RowClickListener {
    public static final String TAG = ExcelMainActivity.class.getSimpleName();
    private Context mContext;
    String regex = "[0-9]+";
    private List<Map<Integer, String>> readExcelList = new ArrayList<>();
    private List<Map<Integer, String>> rowList = new ArrayList<>();
    private List<Map<String, Object>> clickedExcelList = new ArrayList<>();
    private List<Map<String, String>> saveList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExcelAdapter excelAdapter;
    private Button saveButton;
    private ExcelWriteAdapter excelWriteAdapter;
    private String action;
    private ProgressDialog mProgressDialog;
    private String fileName;
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

        if (action.equals(Constants.FILE_READ)) {
            setReadAdapter();
        } else {
            setWriteAdapter();
        }

        initViews();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                saveList.clear();
                saveList.addAll(excelWriteAdapter.saveCodeHere());
                if (saveList != null && !saveList.isEmpty())
                    new ProcessAndSaveData().execute();
            }
        });
    }

    private void initViews() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.FILE_PATH);
        fileName = intent.getStringExtra(Constants.FILE_NAME);
        showLoading();
        db.collection(fileName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int j = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.getId().equals(fileName + "ROW" + j)) {
                                clickedExcelList.add(doc.getData());
                            }
                        }
                        j++;
                    }
                    arrangeDataSequence();
                } else {
                    hideLoading();
                    Toast.makeText(mContext, "Failed to load data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void arrangeDataSequence() {
        Map<Integer, String> map;
        for (int j = 0; j < clickedExcelList.size(); j++) {
            map = new HashMap<>();
            int a = 0;
            for (Map.Entry<String, Object> entry : clickedExcelList.get(j).entrySet()) {
                for (Map.Entry<String, Object> entry2 : clickedExcelList.get(j).entrySet()) {
                    if (entry2.getKey().equals("cell" + a)) {
                        map.put(a, entry2.getValue().toString());
                        break;
                    }
                }
                a++;
            }
            readExcelList.add(map);
        }
        clickedExcelList.clear();
        if (action.equals(Constants.FILE_READ))
            excelAdapter.notifyDataSetChanged();
        else
            excelWriteAdapter.notifyDataSetChanged();
        hideLoading();
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

    @Override
    public void onRowClicked(Map<Integer, String> clickedRow) {
        if (rowList.isEmpty()) {
            rowList.add(readExcelList.get(0));
            rowList.add(readExcelList.get(1));
            rowList.add(readExcelList.get(2));
            if (fileName.equals(Constants.EQUIPMENTS) || fileName.equals(Constants.CERTIFICATES)) {
                rowList.add(readExcelList.get(3));
            }
            rowList.add(clickedRow);
            excelAdapter = new ExcelAdapter(rowList, this, -1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(excelAdapter);
        }
    }

    @Override
    public void onColumnCLicked(int position) {
        rowList.add(readExcelList.get(position));
        excelAdapter = new ExcelAdapter(readExcelList, this, position);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
    }

    @Override
    public void onBackPressed() {
        if (rowList != null && !rowList.isEmpty()) {
            rowList.clear();
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

    @SuppressLint("StaticFieldLeak")
    private class ProcessAndSaveData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            creatingNewListToSave();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideLoading();
            finish();
        }

        private void creatingNewListToSave() {
            Map<String, String> map;
            for (int j = 0; j < readExcelList.size(); j++) {
                map = new HashMap<>();
                int a = 0;
                if (j < 2) {
                    for (Map.Entry<Integer, String> entry : readExcelList.get(j).entrySet()) {
                        map.put("cell" + a, entry.getValue());
                        a++;
                    }
                    saveList.add(j, map);
                } else {
                    for (Map.Entry<Integer, String> entry : readExcelList.get(j).entrySet()) {
                        map.put("cell" + a, entry.getValue());
                        a++;
                    }
                    if (j > (saveList.size() - 1))
                        saveList.add(j, map);
                }
            }
            if (fileName.equals(Constants.EQUIPMENTS)) {
                saveTotalQuantity();
            }
            saveToDataBase();
        }

        private void saveTotalQuantity() {
            int col = 3;
            for (int i = 3; i < saveList.get(0).size(); i++) {
                int number = 0;
                for (int j = 4; j < saveList.size() - 1; j++) {
                    Map<String, String> mMap = saveList.get(j);
                    for (Map.Entry<String, String> entry : mMap.entrySet()) {
                        if (entry.getKey().equals("cell" + col) && entry.getValue().matches(regex)) {
                            number += Integer.parseInt(entry.getValue());
                            break;
                        }
                    }
                }
                replaceTotalQuantityValue(col, number);
                col++;
                Log.d(TAG, "saveTotalQuantity: " + number);
            }
        }

        private void replaceTotalQuantityValue(int col, int number) {
            for (Map.Entry<String, String> entry : saveList.get(saveList.size() - 1).entrySet()) {
                if (entry.getKey().equals("cell" + col)) {
                    entry.setValue(number + "");
                    break;
                }
            }
        }

        private void saveToDataBase() {
            for (int i = 0; i < saveList.size(); i++) {
                db.collection(fileName).document(fileName + "ROW" + i)
                        .set(saveList.get(i));
            }
        }

    }

}
