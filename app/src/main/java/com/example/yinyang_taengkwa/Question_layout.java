package com.example.yinyang_taengkwa;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Question_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_question);
        getSupportActionBar().hide();
    }
}
