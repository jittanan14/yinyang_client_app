package com.example.yinyang_taengkwa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yinyang_taengkwa.activities.DetailActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Question_list extends AppCompatActivity {

    ListView listview;
    ArrayList<String> questionArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_question);
        getSupportActionBar().hide();

        listview = findViewById(R.id.listview_question);

        questionArray = new ArrayList<>();
        questionArray.add(" ");
        questionArray.add("คุณมีอาการหน้าซีดบ่อยแค่ไหน ?");
        questionArray.add("คุณหายใจเบาบ่อยแค่ไหน ?");
        questionArray.add("คุณขี้หนาวบ่อยแค่ไหน ?");
        questionArray.add("คุณรู้สึกไม่ค่อยมีแรงบ่อยแค่ไหน ?");
        questionArray.add("คุณอุจจาระน้อยและค่อนข้างเหลวบ่อยแค่ไหน ?");
        questionArray.add("คุณปัสสาวะมากบ่อยแค่ไหน ?");
        questionArray.add("คุณรู้สึกท้องอืดบ่อยแค่ไหน  ?");
        questionArray.add("คุณร้อนในบ่อยแค่ไหน ?");
        questionArray.add("คุณปากแห้งบ่อยแค่ไหน ?");
        questionArray.add("คุณคอแห้งบ่อยแค่ไหน ?");
        questionArray.add("คุณข้ีหงุดหงิดบ่อยแค่ไหน ?");
        questionArray.add("คุณผิวแห้งบ่อยแค่ไหน ?");
        questionArray.add("คุณฝ่ามือและฝ่าเท้าร้อนบ่อยแค่ไหน ?");

        //yhang
        questionArray.add("คุณหน้าแดงบ่อยแค่ไหน ?");
        questionArray.add("คุณหายใจแรงบ่อยแค่ไหน ?");
        questionArray.add("คุณรู้สึกตัวร้อนบ่อยแค่ไหน ?");
        questionArray.add("คุณชอบด่ืมนํ้าเย็นบ่อยแค่ไหน ?");
        questionArray.add("คุณท้องผูกบ่อยแค่ไหน ?");
        questionArray.add("คุณปัสสาวะเหลืองเข้มบ่อยแค่ไหน ?");
        questionArray.add("ฝ่ามือและฝ่าเท้าของคุณเย็นง่ายบ่อยแค่ไหน ?");
        questionArray.add("คุณปัสสาวะบ่อยตอนกลางคืนบ่อยแค่ไหน ?");

//        CustomAdapter cus = new CustomAdapter(Question_list.this, R.layout.activity_list_question,);
//        listview.setAdapter(cus);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Question_list.this, DetailActivity.class);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
    }
}
