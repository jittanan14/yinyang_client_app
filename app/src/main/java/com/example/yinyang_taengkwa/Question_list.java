package com.example.yinyang_taengkwa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yinyang_taengkwa.Adapter.CustomAdapter;
import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.activities.DetailActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Question_list extends AppCompatActivity {

    ListView listview_question;
    ArrayList<String> questionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);

        getSupportActionBar().hide();

        listview_question = findViewById(R.id.listview_question);

        questionArray = new ArrayList<>();
//        questionArray.add(" ");
        questionArray.add("คุณมีอาการหน้าซีดมั้ย ?");
        questionArray.add("คุณหายใจเบามั้ย?");
        questionArray.add("คุณขี้หนาวบ่อยมั้ย ?");
        questionArray.add("คุณรู้สึกไม่ค่อยมีแรง บ่อยมั้ย ?");
        questionArray.add("คุณอุจจาระน้อยและค่อนข้างเหลว บ่อยมั้ย ?");
        questionArray.add("คุณปัสสาวะมาก บ่อยมั้ย ?");
        questionArray.add("คุณรู้สึกท้องอืด บ่อยมั้ย  ?");
        questionArray.add("คุณร้อนใน บ่อยมั้ย ?");
        questionArray.add("คุณปากแห้ง บ่อยมั้ย ?");
        questionArray.add("คุณคอแห้ง บ่อยมั้ย ?");
        questionArray.add("คุณข้ีหงุดหงิด บ่อยมั้ย ?");
        questionArray.add("คุณผิวแห้ง บ่อยมั้ย ?");
        questionArray.add("ฝ่ามือและฝ่าเท้าของคุณร้อน บ่อยมั้ย ?");

        //yhang
        questionArray.add("คุณมีอาการหน้าแดงมั้ย ?");
        questionArray.add("คุณหายใจแรงมั้ย ?");
        questionArray.add("คุณรู้สึกตัวร้อน บ่อยมั้ย ?");
        questionArray.add("คุณชอบด่ืมนํ้าเย็น บ่อยมั้ย ?");
        questionArray.add("คุณท้องผูก บ่อยมั้ย ?");
        questionArray.add("คุณปัสสาวะเหลืองเข้ม บ่อยมั้ย ?");
        questionArray.add("ฝ่ามือและฝ่าเท้าของคุณเย็น บ่อยมั้ย ?");
        questionArray.add("คุณปัสสาวะตอนกลางคืน บ่อยมั้ย ?");

        com.example.yinyang_taengkwa.Adapter.QuestionAdapter ques = new com.example.yinyang_taengkwa.Adapter.
                QuestionAdapter(Question_list.this, R.layout.activity_layout_question, questionArray);

        listview_question.setAdapter(ques);

        listview_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Question_list.this,Question.class);
                intent.putExtra("position_ques", (i+1));
                intent.putExtra("ch", 1);
                startActivity(intent);
            }
        });


    }
}
