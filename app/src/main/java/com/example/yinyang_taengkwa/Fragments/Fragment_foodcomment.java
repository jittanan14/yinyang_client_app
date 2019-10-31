package com.example.yinyang_taengkwa.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.DetailActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menu;
import com.example.yinyang_taengkwa.models.MenuFavoriteResponse;
import com.example.yinyang_taengkwa.models.Menuresponse;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_foodcomment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textView;

    private SharedPreferences sp;
    private String PREF_NAME = "Log in";

    private List<Menu> menuListOld;
    private List<Menu> menuListRecommend;

    public Fragment_foodcomment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foodcomment, container, false);

        sp = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        menuListOld = new ArrayList<>();
        menuListRecommend = new ArrayList<>();

        textView = view.findViewById(R.id.text_view);

        //Recycler View
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        //Recycler View

        getAllMenu();

        // Inflate the layout for this fragment
        return view;
    }

    private void getAllMenu() {
        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
        call.enqueue(new Callback<Menuresponse>() {
            @Override
            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                Menuresponse res = response.body();

                if (res.isStatus()) {
                    menuListOld = res.getMenu();

                    Log.e("Get All Menu", menuListOld.get(0).getName_menu());

                    getFavoriteMenu();
                }
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {
                Log.e("Get All Menu", t.getMessage());
            }
        });
    }

    private void getFavoriteMenu() {
        String email_user = sp.getString("email", "");

        Call<MenuFavoriteResponse> call = RetrofitClient.getInstance().getApi().getFavoriteMenu(email_user);
        call.enqueue(new Callback<MenuFavoriteResponse>() {
            @Override
            public void onResponse(Call<MenuFavoriteResponse> call, Response<MenuFavoriteResponse> response) {
                MenuFavoriteResponse res = response.body();

                if (res.isStatus()) {
                    if (res.getFavoriteMenu() != null)
                        setRecyclerView(res.getFavoriteMenu().split(","));
                    else
                        setRecyclerView(null);
                }
            }

            @Override
            public void onFailure(Call<MenuFavoriteResponse> call, Throwable t) {
                Log.e("Get Favorite Menu on FC", t.getMessage());
            }
        });
    }

    private void setRecyclerView(String[] fmArr) {
//        Log.e("SETTTTT", fmArr[2]);

        if (menuListOld.size() != 0) {

            List<String> foodLoseArray = new ArrayList<>();

            final String numYhin = sp.getString("numYhin", "");
            final String numYhang = sp.getString("numYhang", "");
            final String body_user = sp.getString("body", "");

            for (int i = 0; i < menuListOld.size(); i++) {

                Double yin = Double.parseDouble(menuListOld.get(i).getNum_yhin());
                Double yang = Double.parseDouble(menuListOld.get(i).getNum_yhang());

                double disYin24;
                double disYin26;

                double disYang24;
                double disYang26;


//                foodLoseArray.add("ไก่");


                if (!findFoodLose(menuListOld.get(i), foodLoseArray)) {
                    Log.e("Food Lose", "ff");
                    if (body_user.equals("หยิน")) {
                        Log.e("Body", body_user);

                        Log.e("FFF", "aaaa");

                        if (Double.parseDouble(numYhang) < 2.4 && Double.parseDouble(numYhin) < 2.4) {
                            //กินหยินและหยางให้ถึง 2.5 เมนูอาหารไม่มี
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yang >= disYang24 && yang <= disYang26) && (yin >= disYin24 && yin <= disYin26)) {

                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if (Double.parseDouble(numYhang) > 2.6 && Double.parseDouble(numYhin) > 2.6) {

                            //ลดหยินและหยางให้เหลือแค่ 2.5 เมนูอาหารไม่มี
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                            if ((yin >= disYin26 && yin <= disYin24) && (yang >= disYang26 && yang <= disYang24)) {

                                menuListRecommend.add(menuListOld.get(i));

                            }

                        } else if (Double.parseDouble(numYhang) > 2.6 && (Double.parseDouble(numYhin) >= 2.4 && Double.parseDouble(numYhin) <= 2.6)) {
                            //กินหยางให้เหลือแค่ 2.5
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                            if ((yang >= disYang26 && yang <= disYang24) && (yin >= disYin26 && yin <= disYin24)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if (Double.parseDouble(numYhang) < 2.4 && (Double.parseDouble(numYhin) >= 2.4 && Double.parseDouble(numYhin) <= 2.6)) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang24 && yang <= disYang26)) {

                                menuListRecommend.add(menuListOld.get(i));

                            }

                        } else if ((Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6) && Double.parseDouble(numYhin) > 2.6) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yin >= disYin26 && yin <= disYin24) && (yang >= disYang24 && yang <= disYang26)) {

                                menuListRecommend.add(menuListOld.get(i));

                            }

                        } else if ((Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6) && Double.parseDouble(numYhin) < 2.4) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang24 && yang <= disYang26)) {

                                menuListRecommend.add(menuListOld.get(i));
                            }
                        }
                    } else if (body_user.equals("หยาง")) {

                        Log.e("Body", body_user);

                        Log.e("Yin", numYhin);

                        if (Double.parseDouble(numYhang) < 2.4 && Double.parseDouble(numYhin) < 2.4) {
                            //กินหยินและหยางให้ถึง 2.5 เมนูอาหารไม่มี
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            Log.e("If one", "geg");

                            if ((yang >= disYang24 && yang <= disYang26) && (yin >= disYin24 && yin <= disYin26)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if (Double.parseDouble(numYhang) > 2.6 && Double.parseDouble(numYhin) > 2.6) {

                            //ลดหยินและหยางให้เหลือแค่ 2.5 เมนูอาหารไม่มี
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                            if ((yin >= disYin26 && yin <= disYin24) && (yang >= disYang26 && yang <= disYang24)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if (Double.parseDouble(numYhang) > 2.6 && (Double.parseDouble(numYhin) >= 2.4 && Double.parseDouble(numYhin) <= 2.6)) {
                            //กินหยางให้เหลือแค่ 2.5
                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                            if ((yang >= disYang26 && yang <= disYang24) && (yin >= disYin26 && yin <= disYin24)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if (Double.parseDouble(numYhang) < 2.4 && (Double.parseDouble(numYhin) >= 2.4 && Double.parseDouble(numYhin) <= 2.6)) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            Log.e("YinYang", String.valueOf(disYang24));

                            if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang24 && yang <= disYang26)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }
                        } else if ((Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6) && Double.parseDouble(numYhin) > 2.6) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yin >= disYin26 && yin <= disYin24) && (yang >= disYang24 && yang <= disYang26)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        } else if ((Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6) && Double.parseDouble(numYhin) < 2.4) {

                            disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                            disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                            disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                            disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                            if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang24 && yang <= disYang26)) {
                                menuListRecommend.add(menuListOld.get(i));
                            }

                        }
                    }
                }
            }

            MenuRecycleAdapter adapter;

            if(menuListRecommend.size() > 0) {
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                adapter = new MenuRecycleAdapter(getContext(), menuListRecommend, fmArr);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Menu item) {
                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        intent.putExtra("DETAIL", item);
                        startActivity(intent);
                    }
                });
            } else {
                textView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void setFoodLose(List<String> flArray, String flText) {
        flArray.add(flText);
    }

    private Boolean findFoodLose(Menu menu, List<String> foodLose) {

        for (int i = 0; i < foodLose.size(); i++) {
            if (menu.getIngredient_menu().indexOf(foodLose.get(i)) >= 0)
                return true;
        }
        return false;
    }


}
