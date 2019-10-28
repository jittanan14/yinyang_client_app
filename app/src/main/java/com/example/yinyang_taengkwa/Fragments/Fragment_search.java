package com.example.yinyang_taengkwa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yinyang_taengkwa.Adapter.MenuRecycleAdapter;
import com.example.yinyang_taengkwa.Adapter.SearchAdapter;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.DetailActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.Menuresponse;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_search extends Fragment {

    ListView lst_view_search;
    EditText editText_search;
    ImageView imgView_search;
    SearchAdapter adapter;
    Menuresponse res;
    TextView sort;
    Button bt_yin, bt_yang;


    public Fragment_search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Recycler View
        lst_view_search = view.findViewById(R.id.lstView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());


        // Recycler View


        lst_view_search = view.findViewById(R.id.lstView);
        editText_search = view.findViewById(R.id.editText_search);
        imgView_search = view.findViewById(R.id.imgView_search);
        sort = view.findViewById(R.id.Textview_sort);

        initSearch(view);
        setHasOptionsMenu(true);

        imgView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter(editText_search.getText().toString());

            }
        });

        editText_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == event.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText_search.getWindowToken(), 0);
                }

                filter(editText_search.getText().toString());
                return false;
            }
        });


        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        lst_view_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                Intent intent = new Intent(getContext(), DetailActivity.class);
//                                intent.putExtra("DETAIL", i);
//                                startActivity(intent);
                Toast.makeText(getContext(), "EiEi", Toast.LENGTH_SHORT).show();

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

                if (res.isStatus() == true) {
                    if (res.getMenu() != null) {

                        adapter = new SearchAdapter(getContext(), R.layout.listview_row_search, res.getMenu());

                        lst_view_search.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Menuresponse> call, Throwable t) {

            }
        });
    }


    public void filter(String data) {
        ArrayList<com.example.yinyang_taengkwa.models.Menu> arr = new ArrayList<>();

        for (com.example.yinyang_taengkwa.models.Menu menu : res.getMenu()) {
            if (menu.getName_menu().contains(data)) {
                arr.add(menu);
            }
        }

        if (arr.size() > 0) {
            adapter.filterList(arr);
        } else {
            adapter.filterList((ArrayList) null);
            Toast.makeText(getContext(), "ไม่พบรายการ", Toast.LENGTH_SHORT);
        }

    }

}
