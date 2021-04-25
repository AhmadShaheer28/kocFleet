package com.kocfleet.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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
                startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                AuthenticationActivity.this.finish();
            }
        });

    }
}