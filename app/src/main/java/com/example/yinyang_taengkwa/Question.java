package com.example.yinyang_taengkwa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yinyang_taengkwa.activities.LoginActivity;
import com.example.yinyang_taengkwa.activities.MainActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Question extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> questionArray;
    TextView Text_question;
    Button button_next;
    Button button_previous;
    Button button_confirmall;
    Button button_back_login;
    RadioButton radioButton;
    RadioGroup radioGroup;
    RetrofitClient retro ;
    String email;
    TextView textView_numberques;
    private SharedPreferences sp;
    private String PREF_NAME = "Log in";

    private int Score[];
    int yhin;
    int yhang;
    double sum_yhin  =0;
    double sum_yhang = 0;
    int index = 1;
    double max = Double.MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        getSupportActionBar().hide();

        sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Text_question = findViewById(R.id.Text_question);
        textView_numberques = findViewById(R.id.textview_numberques);
        button_next = findViewById(R.id.button_next_question);
        button_next.setOnClickListener(this);
        button_previous = findViewById(R.id.button_previous_question);
        button_previous.setOnClickListener(this);
        button_confirmall = findViewById(R.id.button_confirmall);
        button_confirmall.setOnClickListener(this);
        button_back_login = findViewById(R.id.button_back_login);
        button_back_login.setOnClickListener(this);
        radioGroup = findViewById(R.id.radio_answer);
        retro = new RetrofitClient();

        setQuestion();

         email    = sp.getString("email", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_previous_question:
                backQuestion();
                break;
            case R.id.button_next_question:
                nextQuestion();
                break;
            case R.id.button_confirmall :
                confirmAll();
                break;
            case R.id.button_back_login :
                Intent intent = new Intent(Question.this, LoginActivity.class);
                startActivity(intent);
        }
    }

    private void backQuestion() {
        if (index != 1)
            index--;

        Text_question.setText(index + ". " + questionArray.get(index));
        textView_numberques.setText(index+"/"+"21");

        setAnswer(Score[index]);

        if (index == 1) {
            setButton(0);
        } else {
            setButton(2);
        }
    }

    private void nextQuestion() {
        if (index < questionArray.size() - 1)
            index++;

        Text_question.setText(index + ". " + questionArray.get(index));
        textView_numberques.setText(index+"/"+"21");

        int score = Score[index];
        if (index == questionArray.size() - 1) {
            if (score > 0) {
                setAnswer(score);
                setButton(4);
            } else {
                setButton(3);
                radioGroup.clearCheck();
            }
        } else {
            if (score > 0) {
                setAnswer(score);
                setButton(2);
            } else {
                setButton(1);
                radioGroup.clearCheck();
            }
        }
    }

    private void setAnswer(int score) {
        switch (score) {
            case 1:
                radioButton = findViewById(R.id.radioButton1);
                radioButton.setChecked(true);
                break;
            case 2:
                radioButton = findViewById(R.id.radioButton2);
                radioButton.setChecked(true);
                break;
            case 3:
                radioButton = findViewById(R.id.radioButton3);
                radioButton.setChecked(true);
                break;
            case 4:
                radioButton = findViewById(R.id.radioButton4);
                radioButton.setChecked(true);
                break;
            case 5:
                radioButton = findViewById(R.id.radioButton5);
                radioButton.setChecked(true);
                break;
        }

        Log.e("Set Answer Score", String.valueOf(score));
    }

    private void setButton(int number) {

        /* Set Button
        0 >> first question have answer
        1 >> back&next question not have answer
        2 >> back&next question have answer
        3 >> last question not have answer
        4 >> last question have answer
        */

        switch (number) {
            case 0:
                button_previous.setVisibility(View.GONE);
                button_next.setEnabled(true);
                break;
            case 1:
                button_previous.setVisibility(View.VISIBLE);
                button_next.setVisibility(View.VISIBLE);
                button_next.setEnabled(false);
                button_confirmall.setVisibility(View.GONE);
                break;
            case 2:
                button_previous.setVisibility(View.VISIBLE);
                button_next.setVisibility(View.VISIBLE);
                button_next.setEnabled(true);
                button_confirmall.setVisibility(View.GONE);
                break;
            case 3:
                button_previous.setVisibility(View.VISIBLE);
                button_next.setVisibility(View.GONE);
                button_confirmall.setVisibility(View.VISIBLE);
                button_confirmall.setEnabled(false);
                break;
            case 4:
                button_next.setVisibility(View.GONE);
                button_confirmall.setVisibility(View.VISIBLE);
                button_confirmall.setEnabled(true);
                break;
        }
    }


    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton = findViewById(radioId);

//        Toast.makeText(this, String.valueOf(radioId), Toast.LENGTH_SHORT).show();

        Score[index] = getScore(radioId);
        radioButton = findViewById(radioId);



//        int score = Integer.parseInt(radioButton.getText().toString());
//        Score[index] = score;


        if (index == questionArray.size() - 1) {
            button_confirmall.setEnabled(true);
        }
        else {
            button_next.setEnabled(true);
        }

//        Toast.makeText(this, Integer.toString(score), Toast.LENGTH_SHORT).show();

    }

    private int getScore(int radioId) {
        switch (radioId) {
            case R.id.radioButton1 :
                return 1;
            case R.id.radioButton2 :
                return 2;
            case R.id.radioButton3 :
                return 3;
            case R.id.radioButton4 :
                return 4;
            case R.id.radioButton5 :
                return 5;
                default: return 0;
        }
    }


    public void setQuestion() {
        questionArray = new ArrayList<>();
        questionArray.add("");
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

        Score = new int[questionArray.size()];

        Text_question.setText(index + ". " + questionArray.get(1));
        textView_numberques.setText(index+"/"+"21");
        button_previous.setVisibility(View.GONE);
        button_confirmall.setVisibility(View.GONE);
    }

    private void confirmAll() {

        CalculateNumyhinyhang();


        AlertDialog.Builder builder = new AlertDialog.Builder(Question.this, R.style.AlertDialogCustom);

       //เหลือกรณีเท่ากับจะให้เป็นตามที่คำนวณวันเกิดเลย
        int c = 0;
        if (sum_yhin > sum_yhang){
            max = sum_yhin;
            c=1;
        }else if (sum_yhang > sum_yhin) {
            max = sum_yhang;
            c=2;
        }


        String s1 = String.format("%.2f",sum_yhin);
        String s2 = String.format("%.2f",sum_yhang);

        builder.setTitle("ระดับคะแนน");
        if (c==1) {
            //set message
            builder.setMessage("หยินของคุณคือ " + s1 + "\nหยางของคุณคือ " + s2+"\n\nร่างกายของคุณมีความเป็นหยินมากกว่า");
            builder.setIcon(R.drawable.ic_yin);
        }
        else if (c==2){
            builder.setMessage("หยินของคุณคือ " + s1 + "\nหยางของคุณคือ " + s2+"\n\nร่างกายของคุณมีความเป็นหยางมากกว่า");
            builder.setIcon(R.drawable.ic_yang);
        }

        //set cancelable
        builder.setCancelable(true);
        //set positive / yes button

        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Question.this, MainActivity.class);
                startActivity(intent);

            }
        });

        //create alert dialog
        AlertDialog alertdialog = builder.create();
        //show alert dialog
        alertdialog.show();

        Call<DefaultResponse> call = retro.getApi().updateYhinYhang(sum_yhin,sum_yhang,email);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
//                if(res.isStatus()){
//
//                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });

    }

    public void CalculateNumyhinyhang(){

        for(int i = 1; i<=Score.length; i++){

            if(i>=1 && i<=13 ){
                sum_yhin += Score[i];
            }
            else if (i>=14 && i<=21) {
                sum_yhang += Score[i];
            }
        }

        sum_yhin = sum_yhin/13;
        sum_yhang = sum_yhang/8;


    }


}
