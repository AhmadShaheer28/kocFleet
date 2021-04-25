package com.kocfleet.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import com.kocfleet.R;
import com.kocfleet.ui.base.BaseActivity;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));

    }
}