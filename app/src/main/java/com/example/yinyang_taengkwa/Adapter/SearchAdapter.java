package com.example.yinyang_taengkwa.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.example.yinyang_taengkwa.models.Menu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchAdapter extends ArrayAdapter<Menu> {
    Context mContext;
    List<Menu> menuList;
    int resId;
    public ToggleButton favoriteToggle;



    private String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/menu/";


    public SearchAdapter(Context context, int resource, List<Menu> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.menuList = objects;
        this.resId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(resId, null);

        TextView name = view.findViewById(R.id.name_menu_textview);
        TextView category = view.findViewById(R.id.category_textview);
        TextView textView_num_yhin_textview = view.findViewById(R.id.num_yhin_text_view);
        TextView textView_num_yhang_textview = view.findViewById(R.id.num_yhang_text_view);
        ImageView img_cate = view.findViewById(R.id.img_category);
        ImageView img_menu = view.findViewById(R.id.imageView1);
        favoriteToggle = view.findViewById(R.id.favorite_toggle_button);



        Menu menu = menuList.get(position);
        setFavoriteToggle(position);

        name.setText(menu.getName_menu());
        category.setText(menu.getCategory_menu());

        Picasso.get().load(url.concat(menu.getImage_menu())).into(img_menu);
        textView_num_yhin_textview.setText(menu.getNum_yhin());
        textView_num_yhang_textview.setText(menu.getNum_yhang());

        if (Double.valueOf(menu.getNum_yhin()) > Double.valueOf(menu.getNum_yhang())) {
            img_cate.setImageResource(R.drawable.ic_yin);
        } else {
            img_cate.setImageResource(R.drawable.ic_yang);
        }

//        ImageView imageView = (ImageView)view.findViewById(R.id.imageView1);
//        imageView.setBackgroundResource(resId[position]);

        return view;
    }

    public void filterList(ArrayList arr) {
        this.menuList = arr;
        notifyDataSetChanged();
    }

    public void setFavoriteToggle(final int position) {

        if (menuList.get(position).getFavorite() == 0) {
            favoriteToggle.setChecked(false);
            favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            favoriteToggle.setChecked(true);
            favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }

        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    menuList.get(position).setFavorite(1);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menuList.get(position).getName_menu(), 1);
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse res = response.body();

                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });

                } else {
                    favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    menuList.get(position).setFavorite(0);

                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menuList.get(position).getName_menu(), 0);
                    call.enqueue(new Callback<DefaultResponse>() {
                        @Override
                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                            DefaultResponse res = response.body();

                        }

                        @Override
                        public void onFailure(Call<DefaultResponse> call, Throwable t) {

                        }
                    });

                    Log.e("Toggle", String.valueOf(favoriteToggle.isChecked()));
                }


            }
        });


    }

}
