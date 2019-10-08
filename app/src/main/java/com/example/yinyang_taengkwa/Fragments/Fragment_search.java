package com.example.yinyang_taengkwa.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yinyang_taengkwa.R;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_search extends Fragment {


    public Fragment_search() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);


//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

//        setHasOptionsMenu(true);


        //lst_view_search = view.findViewById(R.id.lstView);
        // setHasOptionsMenu(true);

        //initSearch(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void initSearch(final View view) {
//        Call<Menuresponse> call = RetrofitClient.getInstance().getApi().getmenu();
//        call.enqueue(new Callback<Menuresponse>() {
//            @Override
//            public void onResponse(Call<Menuresponse> call, Response<Menuresponse> response) {
//                Menuresponse res = response.body();
//
//                if(res.isStatus() == true) {
//                    if(res.getMenu() != null) {
//                        SearchAdapter se = new SearchAdapter(view.getContext(),R.layout.listview_row_search, res.getMenu());
//                        lst_view_search.setAdapter(se);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Menuresponse> call, Throwable t) {
//                Log.e("Get menu search", t.getMessage());
//            }
//        });
//    }
}
