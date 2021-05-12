package com.kocfleet.ui.activity.excel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.kocfleet.model.ExcelCellModel;
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
    private List<Map<Integer, ExcelCellModel>> rowList = new ArrayList<>();
    private List<Map<String, Object>> clickedExcelList = new ArrayList<>();
    private List<Map<String, String>> saveList = new ArrayList<>();
    private List<Map<String, String>> editedList = new ArrayList<>();
    private List<Map<Integer, ExcelCellModel>> finalExcelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExcelAdapter excelAdapter;
    private Button saveButton;
    private ExcelWriteAdapter excelWriteAdapter;
    private String action;
    private ProgressDialog mProgressDialog;
    private String fileName;
    private int selectedRowPosition = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int selectedColor = 0;
    private String[] colors = new String[]{"#eefdec", "#c7c7c7", "#f0b099", "#afb3e9"};
    private String mColor = colors[selectedColor];


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
                editedList.clear();
                editedList.addAll(excelWriteAdapter.saveCodeHere());
                if (editedList != null && !editedList.isEmpty())
                    creatingNewListToSave();
            }
        });
    }

    /* Writing methods start  */

    private void creatingNewListToSave() {
        Map<String, String> map;
        for (int j = 0; j < finalExcelList.size(); j++) {
            map = new HashMap<>();
            int a = 0;
            for (Map.Entry<Integer, ExcelCellModel> entry : finalExcelList.get(j).entrySet()) {
                map.put("cell" + a, entry.getValue().getValue());
                a++;
            }
            if(j == selectedRowPosition) {
                if (fileName.equals(Constants.EQUIPMENTS) || fileName.equals(Constants.CERTIFICATES))
                    saveList.add(j, editedList.get(2));
                else
                    saveList.add(j, editedList.get(1));
            }
            else
                saveList.add(j, map);
            /*if (j < 2) {
                for (Map.Entry<Integer, ExcelCellModel> entry : finalExcelList.get(j).entrySet()) {
                    map.put("cell" + a, entry.getValue().getValue());
                    a++;
                }
                saveList.add(j, map);
            } else {
                for (Map.Entry<Integer, ExcelCellModel> entry : finalExcelList.get(j).entrySet()) {
                    map.put("cell" + a, entry.getValue().getValue());
                    a++;
                }
                if (j > (saveList.size() - 1))
                    saveList.add(j, map);
            }*/
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                finish();
            }
        }, 2000);
    }

    /* Writing methods end  */

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
        addColorsToList();
        if (action.equals(Constants.FILE_READ))
            excelAdapter.notifyDataSetChanged();
        else
            excelWriteAdapter.notifyDataSetChanged();
        hideLoading();
    }

    private void addColorsToList() {
        Map<Integer, ExcelCellModel> map ;
        Map<Integer, String> map2 ;
        ExcelCellModel model ;
        for (int i = 0; i < readExcelList.size(); i++) {
            map = new HashMap<>();
            map2 = readExcelList.get(i);
            for (int j=0; j<readExcelList.get(i).size(); j++) {
                model = new ExcelCellModel();
                if (j == 1 || j == 2) {
                    if (j != 2 && !map2.get(2).equals("")) {
                        selectedColor = selectedColor + 1;
                    }
                    mColor = colors[selectedColor % 4];
                    model.setColor(mColor);
                }
                else {
                    model.setColor("#FFFFFF");
                }
                if (j == 0) {
                    model.setColor("#cbf7c7");
                }
                if (i == 2) {
                    if (i != 0) {
                        model.setColor("#4169E1");
                    }
                }
                model.setValue(map2.get(j)+"");
                map.put(j, model);
            }
            finalExcelList.add(map);
        }
    }

    private void setReadAdapter() {
        saveButton.setVisibility(View.GONE);
        excelAdapter = new ExcelAdapter(finalExcelList, this, -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
    }

    private void setWriteAdapter() {
        saveButton.setVisibility(View.GONE);
        excelWriteAdapter = new ExcelWriteAdapter(finalExcelList, this, this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelWriteAdapter);
    }

    @Override
    public void onRowClicked(Map<Integer, ExcelCellModel> clickedRow) {
        if (rowList.isEmpty()) {
            rowList.add(finalExcelList.get(0));
            rowList.add(finalExcelList.get(1));
            rowList.add(finalExcelList.get(2));
            if (fileName.equals(Constants.EQUIPMENTS) || fileName.equals(Constants.CERTIFICATES)) {
                rowList.add(finalExcelList.get(3));
            }
            rowList.add(clickedRow);
            excelAdapter = new ExcelAdapter(rowList, this, -1);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(excelAdapter);
        }
    }

    @Override
    public void onWriteRowClicked(Map<Integer, ExcelCellModel> clickedRow, int position) {
        saveButton.setVisibility(View.VISIBLE);
        selectedRowPosition = position;
        if (rowList.isEmpty()) {
            rowList.add(finalExcelList.get(0));
            rowList.add(finalExcelList.get(1));
            rowList.add(finalExcelList.get(2));
            if (fileName.equals(Constants.EQUIPMENTS) || fileName.equals(Constants.CERTIFICATES)) {
                rowList.add(finalExcelList.get(3));
            }
            rowList.add(clickedRow);
            excelWriteAdapter = new ExcelWriteAdapter(rowList, this, this, true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(excelWriteAdapter);
        }
    }

    @Override
    public void onColumnCLicked(int position) {
        rowList.add(finalExcelList.get(position));
        excelAdapter = new ExcelAdapter(finalExcelList, this, position);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(excelAdapter);
    }

    @Override
    public void onBackPressed() {
        if (rowList != null && !rowList.isEmpty()) {
            rowList.clear();
            if (action.equals(Constants.FILE_READ)) {
                setReadAdapter();
            } else {
                setWriteAdapter();
            }
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
