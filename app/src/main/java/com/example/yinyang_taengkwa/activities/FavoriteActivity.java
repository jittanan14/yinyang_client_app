package com.example.yinyang_taengkwa.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menu;
import com.example.yinyang_taengkwa.models.MenuFavoriteResponse;
import com.example.yinyang_taengkwa.models.Menuresponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button back;

    private SharedPreferences sp;
    private String PREF_NAME = "Log in";

    private List<Menu> menuListOld;
    private List<Menu> menuListNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().hide();

        initInstance();
        initLogic();
    }

    private void initInstance() {
        sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        menuListOld = new ArrayList<>();
        menuListNew = new ArrayList<>();

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
    }

    private void initLogic() {
        getAllMenu();
    }

    private void getAllMenu() {
        Call<Menuresponse> callGetMenu = RetrofitClient.getInstance().getApi().getmenu();
        callGetMenu.enqueue(new Callback<Menuresponse>() {
            @Override
            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                Menuresponse res = response.body();

                if(res.isStatus()) {
                    menuListOld = res.getMenu();

                    getFavoriteMenu();
                }

                Log.e("Get All Menu", String.valueOf(menuListOld.get(1).getId_menu()));
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {
                Log.e("Get All Menu", t.getMessage());
            }
        });
    }

    private void getFavoriteMenu() {
        String email_user = sp.getString("email", "");

        Call<MenuFavoriteResponse> callGetFavorite = RetrofitClient.getInstance().getApi().getFavoriteMenu(email_user);
        callGetFavorite.enqueue(new Callback<MenuFavoriteResponse>() {
            @Override
            public void onResponse(Call<MenuFavoriteResponse> call, Response<MenuFavoriteResponse> response) {
                MenuFavoriteResponse res = response.body();

                if(res.isStatus()) {
                    if(res.getFavoriteMenu() != null)
                        setRecyclerView(res.getFavoriteMenu().split(","));
                }

//                Log.e("Get Favorite Menu", favoriteMenu);
            }

            @Override
            public void onFailure(Call<MenuFavoriteResponse> call, Throwable t) {
                Log.e("Get Favotire Menu", t.getMessage());
            }
        });
    }

    private void setRecyclerView(String[] fmArr) {
        for (String fm : fmArr) {
            Log.e("Faaa", fm);
            for(Menu menu : menuListOld) {
                if(fm.equals(String.valueOf(menu.getId_menu()))) {
                    menuListNew.add(menu);

                    Log.e("Menu New", menu.getName_menu());
                }
            }

        }

        MenuRecycleAdapter adapter = new MenuRecycleAdapter(getApplicationContext(), menuListNew, fmArr);

        recyclerView.setAdapter(adapter);
    }
}

