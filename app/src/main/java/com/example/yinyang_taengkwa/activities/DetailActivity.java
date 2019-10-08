package com.example.yinyang_taengkwa.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.models.Menu;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    ScrollView scrollView;
    Button back;
    String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/menu/";
    ImageView img_menu;
    TextView name_menu,cate_menu,yin_menu,yang_menu,textView_yin,textView_yang;
    RelativeLayout relative;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        scrollView = findViewById(R.id.scroll_view);

        back = findViewById(R.id.button_back_listmenu);
        img_menu = findViewById(R.id.img_menu_detail);
        name_menu = findViewById(R.id.name_menu_detail);
        cate_menu = findViewById(R.id.cate_menu_detail);
        yin_menu = findViewById(R.id.yin_menu_detail);
        yang_menu = findViewById(R.id.yang_menu_detail);
        relative = findViewById(R.id.relative);
        textView_yin = findViewById(R.id.textview_yin);
        textView_yang = findViewById(R.id.textview_yang);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Menu menu = getIntent().getParcelableExtra("DETAIL");

        name_menu.setText(menu.getName_menu());
        cate_menu.setText(menu.getCategory_menu());
        yin_menu.setText(menu.getNum_yhin());
        yang_menu.setText(menu.getNum_yhang());

        String url1 = url.concat(menu.getImage_menu());
        Picasso.get().load(url1).into(img_menu);

        if(menu.getCategory_menu().equals("หยาง")){
            scrollView.setBackgroundColor(getResources().getColor(android.R.color.white));
            name_menu.setTextColor(getResources().getColor(android.R.color.black));
            cate_menu.setTextColor(getResources().getColor(android.R.color.black));
            yin_menu.setTextColor(getResources().getColor(android.R.color.black));
            yang_menu.setTextColor(getResources().getColor(android.R.color.black));
            textView_yin.setTextColor(getResources().getColor(android.R.color.black));
            textView_yang.setTextColor(getResources().getColor(android.R.color.black));

        }



    }


}

