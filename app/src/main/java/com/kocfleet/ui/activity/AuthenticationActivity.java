package com.kocfleet.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.kocfleet.R;
import com.kocfleet.ui.dialog.AuthDialog;

public class AuthenticationActivity extends AppCompatActivity {

    AuthDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        dialog = new AuthDialog(this);
        dialog.show();
        dialog.setDialogResult(new AuthDialog.OnDialogResult() {
            @Override
            public void finish(String username, String password) {
                if(username.equals("FMT") &&
                    password.equals("2015")) {
                    startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                    dialog.dismiss();
                    AuthenticationActivity.this.finish();
                } else {
                    Toast.makeText(AuthenticationActivity.this,
                            "Wrong username or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}