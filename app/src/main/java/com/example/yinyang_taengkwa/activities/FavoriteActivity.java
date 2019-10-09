package com.example.yinyang_taengkwa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menu;
import com.example.yinyang_taengkwa.models.Menuresponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().hide();

        back = findViewById(R.id.button_back_profile);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Recycler View
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FavoriteActivity.this);

        recyclerView.setLayoutManager(linearLayoutManager);

        //Recycler View

            Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
            call.enqueue(new Callback<Menuresponse>() {
                @Override
                public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                    Menuresponse res = response.body();
                    List<Menu> menu = res.getMenu();
                    List<Menu> menuFav = new ArrayList<>();

                    if (res.isStatus() == true) {
                        if (menu.size() != 0) {
                         for(int i = 0; i<menu.size(); i++){
                             if(menu.get(i).getFavorite()==1){
                                 menuFav.add(menu.get(i));
                             }
                         }
                            MenuRecycleAdapter adapter = new MenuRecycleAdapter(FavoriteActivity.this, menuFav);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Menu item) {
                                    Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
                                    intent.putExtra("DETAIL", item);
                                    startActivity(intent);
                                }
                            });

                        }
                    }

                }

                @Override
                public void onFailure(Call<Menuresponse> call, Throwable t) {
                    Log.e("Get Menu", t.getMessage());
                }
            });





        }
    }

