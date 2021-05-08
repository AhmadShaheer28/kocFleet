package com.kocfleet.ui.activity.excel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kocfleet.R;
import com.kocfleet.model.ExcelCellModel;
import com.kocfleet.ui.RowClickListener;
import com.kocfleet.ui.adapter.ExcelAdapter;
import com.kocfleet.ui.adapter.ExcelWriteAdapter;
import com.kocfleet.utils.Constants;
import com.kocfleet.utils.ExcelUtil;
import com.kocfleet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelMainActivity extends AppCompatActivity implements RowClickListener {
    public static final String TAG = ExcelMainActivity.class.getSimpleName();
    private Context mContext;
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

        if(action.equals(Constants.FILE_READ)) {
            setReadAdapter();
        } else {
            setWriteAdapter();
        }

        initViews();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveList.clear();
                saveList.addAll(excelWriteAdapter.saveCodeHere());
                
                creatingNewListToSave();
            }
        });
    }

    private void creatingNewListToSave() {
        showLoading();
        Map<String, String> map ;
        for (int j = 0; j < readExcelList.size(); j++) {
            map = new HashMap<>();
            int a = 0;
            if(j < 2) {
                for(Map.Entry<Integer, String> entry: readExcelList.get(j).entrySet()) {
                    map.put("cell"+a, entry.getValue());
                    a++;
                }
                saveList.add(j, map);
            } else {
                for (Map.Entry<Integer, String> entry : readExcelList.get(j).entrySet()) {
                    map.put("cell"+a, entry.getValue());
                    a++;
                }
                if(j > (saveList.size()-1))
                    saveList.add(j, map);
            }

        }
        saveToDataBase();
    }

    private void saveToDataBase() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                finish();
            }
        }, 3000);
        for (int i = 0; i < saveList.size(); i++) {
            db.collection(fileName).document(fileName+"ROW"+i)
                    .set(saveList.get(i));
        }

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
                    int j=0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for(QueryDocumentSnapshot doc : task.getResult()) {
                            if(doc.getId().equals(fileName+"ROW"+j)) {
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
        Map<Integer, String> map ;
        for (int j = 0; j < clickedExcelList.size(); j++) {
            map = new HashMap<>();
            int a = 0;
            for(Map.Entry<String, Object> entry: clickedExcelList.get(j).entrySet()) {
                for(Map.Entry<String, Object> entry2: clickedExcelList.get(j).entrySet()) {
                    if(entry2.getKey().equals("cell" + a)) {
                        map.put(a, entry2.getValue().toString());
                        break;
                    }
                }
                a++;
            }
            readExcelList.add(map);
        }
        clickedExcelList.clear();
        if(action.equals(Constants.FILE_READ))
            excelAdapter.notifyDataSetChanged();
        else
            excelWriteAdapter.notifyDataSetChanged();
        hideLoading();
        Log.d(TAG, "Error getting documents: ");
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

    /*private void importExcelDeal(String path, String fileName) {
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
    }*/

    /**
     * refresh RecyclerView
     */
    /*private void updateUI() {
        runOnUiThread(() -> {
            if (readExcelList != null && readExcelList.size() > 0) {
                if(action.equals(Constants.FILE_READ)) {
                    excelAdapter.notifyDataSetChanged();
                } else {
                    excelWriteAdapter.notifyDataSetChanged();
                }

            }
        });
    }*/

    @Override
    public void onRowClicked(Map<Integer, String> clickedRow) {
        if(rowList.isEmpty()) {
            rowList.add(readExcelList.get(0));
            rowList.add(readExcelList.get(1));
            rowList.add(readExcelList.get(2));
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
        if(rowList != null && !rowList.isEmpty()) {
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
}
