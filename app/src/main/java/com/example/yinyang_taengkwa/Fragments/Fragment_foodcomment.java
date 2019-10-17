package com.example.yinyang_taengkwa.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.DetailActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menu;
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

    RecyclerView recyclerView;
    private SharedPreferences sp;
    private String PREF_NAME = "Log in";

    public Fragment_foodcomment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foodcomment, container, false);

        sp = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);


        //Recycler View
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        getToServer(view);
        //Recycler View

        // Inflate the layout for this fragment
        return view;
    }

    public void getToServer(final View view) {
        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
        call.enqueue(new Callback<Menuresponse>() {
            @Override
            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                Menuresponse res = response.body();
                List<Menu> menu = res.getMenu();
                List<Menu> menu_recommend = new ArrayList<>();

                if (res.isStatus() == true) {

                    if (menu.size() != 0) {

                        final String numYhin = sp.getString("numYhin", "");
                        final String numYhang = sp.getString("numYhang", "");
                        final String body_user = sp.getString("body", "");
                        final String food_lose = sp.getString("foodLose", "");

                        for (int i = 0; i < menu.size(); i++) {

                            Double yin = Double.parseDouble(menu.get(i).getNum_yhin());
                            Double yang = Double.parseDouble(menu.get(i).getNum_yhang());

                            double disYin24;
                            double disYin26;

                            double disYang24;
                            double disYang26;

//                            menu.get(i).getIngredient_menu().indexOf(food_lose) == -1

                            String[] foodLose = { "ปลา", "แฮม", "มะระ", "มะเขือ", "เนื้อ"};

                            if (!findFoodLose(menu.get(i), foodLose)) {
                                if (body_user.equals("หยิน")) {

                                    if (Double.parseDouble(numYhin) < 2.4 && (Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6)) {
                                        //กินหยินเพิ่มขึ้นให้ถึง 2.5
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                                        if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang26 && yang <= disYang24)) {
                                            if (menu.get(i).getIngredient_menu().indexOf("ลูกชิ้นปลา") < 0) {
//                                            menu_recommend.add(menu.get(i));

                                                Log.e("Food Lose", String.valueOf(menu.get(i).getIngredient_menu().indexOf("ลูกชิ้นปลา")));
                                            }

                                        }
                                    } else if (Double.parseDouble(numYhin) < 2.4 || Double.parseDouble(numYhang) < 2.4) {
                                        //กินหยินและหยางให้ถึง 2.5 เมนูอาหารไม่มี
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                                        if ((yin >= disYin24 && yin <= disYin26) && (yang >= disYang24 && yang <= disYang26)) {
                                            menu_recommend.add(menu.get(i));
                                        }

                                    } else if (Double.parseDouble(numYhin) > 2.6 && Double.parseDouble(numYhang) > 2.6) {
                                        //ลดหยินและหยางให้เหลือแค่ 2.5 เมนูอาหารไม่มี

                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                                        if ((yin >= disYin26 && yin <= disYin24) || (yang >= disYang26 && yang <= disYang24)) {

                                            menu_recommend.add(menu.get(i));

                                        }


                                    } else if (Double.parseDouble(numYhin) > 2.6 && (Double.parseDouble(numYhang) >= 2.4 && Double.parseDouble(numYhang) <= 2.6)) {
                                        //ลดหยินให้เท่ากับ 2.5
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                                        if ((yin >= disYin26 && yin <= disYin24) && (yang >= disYang26 && yang <= disYang24)) {
                                            menu_recommend.add(menu.get(i));
                                        }
                                    }
                                } else if (body_user.equals("หยาง")) {

                                    if (Double.parseDouble(numYhang) > 2.6 && (Double.parseDouble(numYhin) >= 2.4 && Double.parseDouble(numYhin) <= 2.6)) {
                                        //กินหยางให้เหลือแค่ 2.5
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                                        if ((yang >= disYang26 && yang <= disYang24) && (yin >= disYin26 && yin <= disYin24)) {
                                            menu_recommend.add(menu.get(i));
                                        }

                                    } else if (Double.parseDouble(numYhang) < 2.4 && Double.parseDouble(numYhin) < 2.4) {
                                        //กินหยินและหยางให้ถึง 2.5 เมนูอาหารไม่มี
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));

                                        if ((yang >= disYang24 && yang <= disYang26) || (yin >= disYin24 && yin <= disYin26)) {
                                            menu_recommend.add(menu.get(i));
                                        }
                                    } else if (Double.parseDouble(numYhang) > 2.6 && Double.parseDouble(numYhin) > 2.6) {

                                        //ลดหยินและหยางให้เหลือแค่ 2.5 เมนูอาหารไม่มี
                                        disYin24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.4)));
                                        disYin26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhin) - 2.6)));

                                        disYang24 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.4)));
                                        disYang26 = Math.abs(Double.parseDouble(String.format("%.2f", Double.parseDouble(numYhang) - 2.6)));


                                        if ((yin >= disYin26 && yin <= disYin24) || (yang >= disYang26 && yang <= disYang24)) {

                                            menu_recommend.add(menu.get(i));

                                        }

                                    } else if (Double.parseDouble(numYhang) < 2.5 && Double.parseDouble(numYhin) == 2.5) {
                                        //กินหยางให้เพิ่มขึ้นเท่ากับ 2.5
                                    }
                                }

                            }

                            MenuRecycleAdapter adapter = new MenuRecycleAdapter(getContext(), menu_recommend);
                            recyclerView.setAdapter(adapter);

                            adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Menu item) {
                                    Intent intent = new Intent(getContext(), DetailActivity.class);
                                    intent.putExtra("DETAIL", item);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {
                Log.e("Get Menu", t.getMessage());
            }
        });

    }

    private Boolean findFoodLose(Menu menu, String[] foodLose) {

        for(int i=0; i<foodLose.length; i++) {
            if(menu.getIngredient_menu().indexOf(foodLose[i]) > 0)
                return true;
        }
        return false;
    }


}
