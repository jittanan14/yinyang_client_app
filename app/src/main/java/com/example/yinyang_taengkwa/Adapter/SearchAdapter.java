package com.example.yinyang_taengkwa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.models.Menu;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends ArrayAdapter<Menu> {
    Context mContext;
    List<Menu> menuList;
    int resId;
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

        Menu menu = menuList.get(position);

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

    public void filterList(ArrayList arr){
        this.menuList = arr;
        notifyDataSetChanged();
    }
}
