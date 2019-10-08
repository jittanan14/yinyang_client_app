package com.example.yinyang_taengkwa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.example.yinyang_taengkwa.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();

        cdt = new CountDownTimer(   1000, 50) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}
