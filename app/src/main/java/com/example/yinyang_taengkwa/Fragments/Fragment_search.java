package com.example.yinyang_taengkwa.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.Adapter.SearchAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.DetailActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menuresponse;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_search extends Fragment {

    RecyclerView lst_view_search;
    EditText editText_search;
    ImageView imgView_search;
   MenuRecycleAdapter adapter;
    Menuresponse res;
    Button bt_yin,bt_yang;


    public Fragment_search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //Recycler View
        lst_view_search = view.findViewById(R.id.lstView);
        lst_view_search.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        lst_view_search.setLayoutManager(linearLayoutManager);
        bt_yin = view.findViewById(R.id.button_type_yin);
        bt_yang = view.findViewById(R.id.button_type_yang);


        //Recycler View


        lst_view_search = view.findViewById(R.id.lstView);
        editText_search = view.findViewById(R.id.editText_search);
        imgView_search = view.findViewById(R.id.imgView_serch);

        initSearch(view);
        setHasOptionsMenu(true);

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                   filter(editable.toString());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initSearch(final View view) {
        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
        call.enqueue(new Callback<Menuresponse>() {
            @Override
            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
                res = response.body();

                if(res.isStatus() == true) {
                    if(res.getMenu() != null) {

                        adapter = new MenuRecycleAdapter(getContext(),res.getMenu());
                        lst_view_search.setAdapter(adapter);

                        adapter.setOnItemClickListener(new MenuRecycleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(com.example.yinyang_taengkwa.models.Menu item) {
                                Intent intent = new Intent(getContext(), DetailActivity.class);
                                intent.putExtra("DETAIL", item);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {

            }
        });
    }



    public void filter(String data){
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for(com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()){
            if(menu.getName_menu().contains(data)){
                arr.add(menu);
            }
        }

        if(arr.size() > 0){
            adapter.filterList(arr);
        }
        else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(),"ไม่พบรายการ",Toast.LENGTH_SHORT);
        }

    }
}
