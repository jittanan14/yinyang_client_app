package com.example.yinyang_taengkwa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yinyang_taengkwa.Fragments.Fragment_foodcomment;
import com.example.yinyang_taengkwa.Fragments.Fragment_graph;
import com.example.yinyang_taengkwa.Fragments.Fragment_profile;
import com.example.yinyang_taengkwa.Fragments.Fragment_search;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    //Fragments
    private final Fragment_foodcomment foodFregment = new Fragment_foodcomment();
    private final Fragment_search searchFragment = new Fragment_search();
    private final Fragment_graph graphFragment = new Fragment_graph();
    private final Fragment_profile profileFragment = new Fragment_profile();


    RetrofitClient retro;
    EditText text_email;
    EditText pass_word;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    String PREF_NAME = "Log in";
    TextView text_create;
    ActionBar toolbar;
    Button logout,search;
    ListView lstView ;
    MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        toolbar = getSupportActionBar();
        logout = findViewById(R.id.button_logout);
        text_create = findViewById(R.id.text_create);
        logout = findViewById(R.id.button_logout);
        search = findViewById(R.id.button_search);

        sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

//        Fragment_search lst = (Fragment_search)getSupportFragmentManager().findFragmentByTag("Fragment_search");
//        if(lst == null){
//            lst = new Fragment_search();
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(android.R.id.content,lst,"Fragment_search");
//            transaction.commit();
//        }

        edit = sp.edit();
//        searchView = findViewById(R.id.search_view);
//        lstView = (ListView)findViewById(R.id.list_view_search);
//        getToServer(lstView);
        logout.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

        if (!sp.getBoolean("SIGNIN", false)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    //set message
                    builder.setMessage("คุณต้องการออกจากระบบหรือไม่");
                    //set cancelable
                    builder.setCancelable(true);
                    //set positive / yes button
                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            edit.clear();
                            edit.commit();

                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    });

                    //set negative / no button
                    builder.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    //create alert dialog
                    AlertDialog alertdialog = builder.create();
                    //show alert dialog
                    alertdialog.show();
                }
            });

            loadFragment(new Fragment_foodcomment());

            BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
            navigation.setOnNavigationItemSelectedListener(this);

//        }

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.food_comment:
                text_create.setText("รายการอาหารที่แนะนำ");
                loadFragment(foodFregment);
                logout.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                return true;
            case R.id.search:
                text_create.setText("ค้นหาเมนูอาหาร");
                loadFragment(searchFragment);
                search.setVisibility(View.VISIBLE);
                logout.setVisibility(View.GONE);

                return true;
            case R.id.graph:
                text_create.setText("ประวัติการรับประทานอาหาร");
                loadFragment(graphFragment);
                logout.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                return true;
            case R.id.profile:
                text_create.setText("โปรไฟล์ของฉัน");
                loadFragment(profileFragment);
                logout.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                return true;
        }

        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

//    public void getToServer(final View view) {
//        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
//        call.enqueue(new Callback<Menuresponse>() {
//            @Override
//            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
//                Menuresponse res = response.body();
//                List<com.example.jittanan.yhinyhang.models.Menu> menu = res.getMenu();
//
//                if(res.isStatus() == true) {
//
//                    if (menu.size() != 0) {
//                        CustomAdapter cus = new CustomAdapter(view.getContext(), R.layout.fragment_search, menu);
//                        lstView.setAdapter(cus);
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Menuresponse> call, Throwable t) {
//
//            }
//        });
//    }


    public boolean OnCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, (android.view.Menu) menu);
        return true;
    }
}



