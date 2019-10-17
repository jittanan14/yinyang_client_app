package com.example.yinyang_taengkwa.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuRecycleAdapter extends RecyclerView.Adapter<MenuRecycleAdapter.ViewHolder> {

    private Context mContext;
    private List<Menu> mMenuList;

    private String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/menu/";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Menu item);
    }

    public MenuRecycleAdapter(Context mContext, List<Menu> mMenuList) {
        this.mContext = mContext;
        this.mMenuList = mMenuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listview_row, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Menu menu = mMenuList.get(position);
        holder.menu = menu;


        holder.nameText.setText(menu.getName_menu());
        holder.categoryText.setText(menu.getCategory_menu());
        holder.yinText.setText(menu.getNum_yhin());
        holder.yangText.setText(menu.getNum_yhang());
        Picasso.get().load(url.concat(menu.getImage_menu())).into(holder.imageMenu);
        holder.setFavoriteToggle();


        if (Double.valueOf(menu.getNum_yhin()) > Double.valueOf(menu.getNum_yhang())) {
            holder.img_cate.setImageResource(R.drawable.ic_yin);
        } else if (Double.valueOf(menu.getNum_yhang()) > Double.valueOf(menu.getNum_yhin())) {
            holder.img_cate.setImageResource(R.drawable.ic_yang);
        }

        holder.bind(menu, mListener);
    }

    @Override
    public int getItemCount() {
        return mMenuList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView nameText, categoryText, yinText, yangText;
        public ImageView imageMenu;
        public ImageView img_cate;
        public ToggleButton favoriteToggle;

        public Menu menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name_menu_textview);
            categoryText = itemView.findViewById(R.id.category_textview);
            yinText = itemView.findViewById(R.id.num_yhin_text_view);
            yangText = itemView.findViewById(R.id.num_yhang_text_view);
            imageMenu = itemView.findViewById(R.id.imageView1);
            img_cate = itemView.findViewById(R.id.img_category);
            favoriteToggle = itemView.findViewById(R.id.favorite_toggle_button);

        }

        public void bind(final Menu item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }


        public void setFavoriteToggle() {
            if (menu.getFavorite() == 0) {
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
                        menu.setFavorite(1);

                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menu.getName_menu(), 1);
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
                        menu.setFavorite(0);

                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menu.getName_menu(), 0);
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
    public void filterList(ArrayList arr){
        this.mMenuList = arr;
        notifyDataSetChanged();
    }


}
