package com.example.yinyang_taengkwa.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.yinyang_taengkwa.Adapter.SearchAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menuresponse;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_search extends Fragment {

    ListView lst_view_search;
    EditText editText_search;
    ImageView imgView_search;
    SearchAdapter se;
    Menuresponse res;

    public Fragment_search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

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
                        se = new SearchAdapter(view.getContext(),R.layout.listview_row_search, res.getMenu());
                        lst_view_search.setAdapter(se);
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
            se.filterList(arr);
        }
        else {
            se.filterList((ArrayList) res.getMenu());
        }

    }
}
