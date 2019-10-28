package com.example.yinyang_taengkwa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.yinyang_taengkwa.Fragments.Fragment_foodcomment;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.example.yinyang_taengkwa.models.Menu;
import com.example.yinyang_taengkwa.models.MenuUser;
import com.example.yinyang_taengkwa.models.MenuUserResponse;
import com.example.yinyang_taengkwa.models.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuRecycleAdapter extends RecyclerView.Adapter<MenuRecycleAdapter.ViewHolder> {

    private Context mContext;
    private List<Menu> mMenuList;
    SharedPreferences sp;
    Calendar calendar;

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
        holder.setChoose_Menu();


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
        public ToggleButton favoriteToggle, chooseCheck;

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
            chooseCheck = itemView.findViewById(R.id.choose_menu);
            sp = mContext.getSharedPreferences("Log in", Context.MODE_PRIVATE);
            calendar = Calendar.getInstance();
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


        public void setChoose_Menu() {

            if (menu.getChoose_Menu() == 0) {
                chooseCheck.setChecked(false);
                chooseCheck.setBackgroundResource(R.drawable.checkmark_choose);
            } else {
                chooseCheck.setChecked(true);
                chooseCheck.setBackgroundResource(R.drawable.checkmark2);

            }

            chooseCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        chooseCheck.setBackgroundResource(R.drawable.checkmark2);
                        menu.setChoose(1);

                        Log.e("Toggle", String.valueOf(chooseCheck.isChecked()));

                        Call<DefaultResponse> call1 = RetrofitClient.getInstance().getApi().updateChoose(menu.getName_menu(), 1);
                        call1.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                                DefaultResponse res = response.body();

                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                                Log.e("set 1", t.getMessage());
                            }
                        });


                        final String user_id = sp.getString("email", "");
                        final String date = DateFormat.getDateInstance().format(calendar.getTime());
                        final String[] menu_id = {menu.getName_menu()};


                        Call<MenuUserResponse> call2 = RetrofitClient.getInstance().getApi().getMenuUser(user_id);
                        call2.enqueue(new Callback<MenuUserResponse>() {
                            @Override
                            public void onResponse(Call<MenuUserResponse> call2, Response<MenuUserResponse> response) {
                                MenuUserResponse res = response.body();
                                List<MenuUser> menuUserList = res.getMenuUser();

                                if(!res.isStatus()) {
                                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createMenuUser(user_id, date, menu_id[0]);
                                    call.enqueue(new Callback<DefaultResponse>() {
                                        @Override
                                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                            Log.e("Update Menu User", t.getMessage());
                                        }
                                    });
                                } else {
                                    Log.e("Get User", String.valueOf(res.isStatus()));

                                    MenuUser mu;
                                    if((mu = checkDate(menuUserList, date)) != null) {
                                        menu_id[0] = mu.getMenu_id() + "," + menu_id[0];

                                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateMenuUser(user_id, menu_id[0]);
                                        call.enqueue(new Callback<DefaultResponse>() {
                                            @Override
                                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                                            }

                                            @Override
                                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                                Log.e("Update Menu User", t.getMessage());
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MenuUserResponse> call2, Throwable t) {
                                Log.e("Get Menu User", t.getMessage());
                            }
                        });


                    } else {
                        chooseCheck.setBackgroundResource(R.drawable.checkmark_choose);
                        menu.setChoose(0);

                        Log.e("Toggle", String.valueOf(chooseCheck.isChecked()));

                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateChoose(menu.getName_menu(), 0);
                        call.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse res = response.body();

                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            });

        }


    }

    public void filterList(ArrayList arr) {
        this.mMenuList = arr;
        notifyDataSetChanged();
    }

    private MenuUser checkDate(List<MenuUser> menuUserList, String date) {
        for(MenuUser mu : menuUserList) {
            if(date.equals(mu.getDate())) {
                return mu;
            }
        }
        return null;
    }


}
