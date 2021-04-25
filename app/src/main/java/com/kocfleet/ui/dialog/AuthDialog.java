package com.kocfleet.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.kocfleet.R;


public class AuthDialog extends Dialog {

    Activity activity;
    private OnDialogResult mDialogResult;
    private EditText etUsername;
    private EditText etPassword;
    private ImageView ivClose;
    private Button btnEnter;

    public AuthDialog(@NonNull Activity activity) {
        super(activity, R.style.MaterialDialogSheet);
        this.activity   = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        etUsername = findViewById(R.id.enter_name);
        etPassword = findViewById(R.id.enter_email);
        ivClose = findViewById(R.id.close);
        btnEnter = findViewById(R.id.send_btn);

        clickListeners();
    }

    private void clickListeners() {

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogResult.finish("", "");
                dismiss();
                /*if(etPassword.getText().toString().isEmpty()) {
                    //Utils.showAlert(activity, "Message", "Please enter password.");
                } else if(etUsername.getText().toString().isEmpty()) {
                    //Utils.showAlert(activity, "Message", "Please enter username.");
                } else {
                    if( mDialogResult != null ){
                        mDialogResult.finish("", "");
                        dismiss();
                    }
                }*/
            }
        });

    }

    public void setData(String name, String email) {

    }

    public void setDialogResult(OnDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnDialogResult{
        void finish(String username, String password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity.finish();
    }
}
