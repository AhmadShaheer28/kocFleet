package com.kocfleet.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.kocfleet.R;
import com.kocfleet.utils.Constants;

public class ActionDialog extends Dialog {

    private Button btnRead;
    private Button btnWrite;
    private ImageView btnClose;
    private Context context;
    private String fileName;
    private UserActionDelegate delegate;

    public ActionDialog(@NonNull Context context,String fileName, UserActionDelegate delegate) {
        super(context);
        this.context = context;
        this.delegate = delegate;
        this.fileName = fileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnRead = findViewById(R.id.read_btn);
        btnWrite = findViewById(R.id.write_btn);
        btnClose = findViewById(R.id.close);

        clickListeners();
    }

    private void clickListeners() {
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.userAction(Constants.FILE_READ, fileName);
                dismiss();
            }
        });
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.userAction(Constants.FILE_WRITE, fileName);
                dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface UserActionDelegate {
        void userAction(String action, String fileName);
    }
}
