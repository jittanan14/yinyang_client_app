package com.example.yinyang_taengkwa.Fragments;


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
    public Fragment_foodcomment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_foodcomment, container, false);


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

                if (res.isStatus() == true) {

                    if (menu.size() != 0) {
                        MenuRecycleAdapter adapter = new MenuRecycleAdapter(getContext(), menu);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Menu item) {
                                Intent intent = new Intent(getContext(), DetailActivity.class);
                                intent.putExtra("DETAIL", item);
                                startActivity(intent);
                            }
                        });

//                        final String numYhin  = sp.getString("numYhin", "");
//                        final String numYhang = sp.getString("numYhang", "");
//                        for(int i=0; menu.size()<=i; i++) {
//                            Menu me = menu.get(i);
//                            if(me.getCategory_menu().equals("หยิน")) {
//                                if (Double.parseDouble(numYhin) < 2.5 && Double.parseDouble(numYhang) == 2.5) {
//                                        //กินหยินเพิ่มขึ้นให้ถึง 2.5
//                                }else if(Double.parseDouble(numYhin) < 2.5 && Double.parseDouble(numYhang) < 2.5){
//                                        //กินหยินและหยางให้ถึง 2.5
//                                }else if (Double.parseDouble(numYhin) > 2.5 && Double.parseDouble(numYhang) > 2.5){
//                                        //ลดหยินและหยางให้เหลือแค่ 2.5
//                                }else if (Double.parseDouble(numYhin) > 2.5 && Double.parseDouble(numYhang) == 2.5){
//                                        //ลดหยินให้เท่ากับ 2.5
//                                }
//                            }else if (me.getCategory_menu().equals("หยาง")){
//                                if (Double.parseDouble(numYhang) > 2.5 && Double.parseDouble(numYhin) == 2.5) {
//                                    //กินหยางให้เหลือแค่ 2.5
//                                }else if(Double.parseDouble(numYhang) < 2.5 && Double.parseDouble(numYhin) < 2.5){
//                                    //กินหยินและหยางให้ถึง 2.5
//                                }else if (Double.parseDouble(numYhang)  > 2.5 && Double.parseDouble(numYhin) > 2.5){
//                                    //ลดหยินและหยางให้เหลือแค่ 2.5
//                                }else if (Double.parseDouble(numYhang) < 2.5 && Double.parseDouble(numYhin) == 2.5){
//                                    //กินหยางให้เพิ่มขึ้นเท่ากับ 2.5
//                                }
//                            }
//
//                        }

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
