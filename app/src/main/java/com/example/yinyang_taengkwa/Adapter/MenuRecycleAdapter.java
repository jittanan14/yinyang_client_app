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
import com.example.yinyang_taengkwa.Question;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.MainActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.example.yinyang_taengkwa.models.Menu;
import com.example.yinyang_taengkwa.models.MenuFavoriteResponse;
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
    SharedPreferences.Editor editor;

    Calendar calendar;
    double sum_yin_menu = 0.0;
    double sum_yang_menu = 0.0;
    RetrofitClient retro;
    double yin_user = 0.0;
    double yang_user = 0.0;

    private String faMenuOld = "";
    private String faMenuNew = "";

    private String[] fmArr;

    private String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/menu/";

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Menu item);
    }

    public MenuRecycleAdapter(Context mContext, List<Menu> mMenuList, String[] fmArr) {
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.fmArr = fmArr;
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

        holder.bind(menu, mListener);

        if (Double.valueOf(menu.getNum_yhin()) > Double.valueOf(menu.getNum_yhang())) {
            holder.categoryText.setBackgroundResource(R.drawable.bg_yin);
        } else if (Double.valueOf(menu.getNum_yhang()) > Double.valueOf(menu.getNum_yhin())) {
            holder.categoryText.setBackgroundResource(R.drawable.bg_yang);
        }


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

            favoriteToggle = itemView.findViewById(R.id.favorite_toggle_button);
            chooseCheck = itemView.findViewById(R.id.choose_menu);
            sp = mContext.getSharedPreferences("Log in", Context.MODE_PRIVATE);
            calendar = Calendar.getInstance();
            retro = new RetrofitClient();
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

//            for(String fm : fmArr) {
//                Log.e("Favorite Menu", fm);
//                if(fm.equals(String.valueOf(menu.getId_menu()))) {
//                    favoriteToggle.setChecked(false);
//                    favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
//                    break;
//                }
//            }
            if (fmArr != null) {

                if ("29,4,5,3,6".contains(String.valueOf(menu.getId_menu()))) {
                    favoriteToggle.setChecked(false);
                    favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                }
            }

//            if (menu.getFavorite() == 0) {
//                favoriteToggle.setChecked(false);
//                favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
//            } else {
//                favoriteToggle.setChecked(true);
//                favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
//            }

            favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                        //  menu.setFavorite(1);

                        String email = sp.getString("email", "");

                        getFavoriteMenu(email);

                        faMenuNew = faMenuOld + menu.getName_menu();

                        updateFavoriteMenu(email, faMenuNew);

//                        getFavoriteMenu(email);


//                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menu.getName_menu(), 1);
//                        call.enqueue(new Callback<DefaultResponse>() {
//                            @Override
//                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                                DefaultResponse res = response.body();
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//
//                            }
//                        });


//                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createFavoriteUser(email, menu.getName_menu(), 1);
//                        call.enqueue(new Callback<DefaultResponse>() {
//                            @Override
//                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                                DefaultResponse res = response.body();
//                                Log.e("fav", String.valueOf(menu.getId_menu()));
//                                Log.e("name", menu.getName_menu());
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//
//                            }
//                        });


                    } else {
                        favoriteToggle.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                        menu.setFavorite(0);

                        String email = sp.getString("email", "");

                        getFavoriteMenu(email);

//                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavorite(menu.getName_menu(), 0);
//                        call.enqueue(new Callback<DefaultResponse>() {
//                            @Override
//                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                                DefaultResponse res = response.body();
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//
//                            }
//                        });

//                        String email = sp.getString("email", "");
//                        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createFavoriteUser(email, menu.getId_menu(), 0);
//                        call.enqueue(new Callback<DefaultResponse>() {
//                            @Override
//                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                                DefaultResponse res = response.body();
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//
//                            }
//                        });

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

                        chooseAndUpdateyinyang_user(menu.getNum_yhin(), menu.getNum_yhang());


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

                                if (!res.isStatus()) {
                                    Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createMenuUser(user_id, date, menu_id[0]);
                                    call.enqueue(new Callback<DefaultResponse>() {
                                        @Override
                                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                                            //

                                        }

                                        @Override
                                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                            Log.e("Update Menu User", t.getMessage());
                                        }
                                    });
                                } else {
                                    Log.e("Get User", String.valueOf(res.isStatus()));

                                    MenuUser mu;
                                    if ((mu = checkDate(menuUserList, date)) != null) {
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

                        String email = sp.getString("email", " ");
                        String yin = sp.getString("numYhin", " ");
                        String yang = sp.getString("numYhang", " ");

                        Call<DefaultResponse> call3 = retro.getApi().updateYhinYhang(Double.parseDouble(yin), Double.parseDouble(yang), email);
                        call3.enqueue(new Callback<DefaultResponse>() {
                            @Override
                            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                DefaultResponse res = response.body();


                            }

                            @Override
                            public void onFailure(Call<DefaultResponse> call, Throwable t) {

                            }
                        });


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


                        final String user_id = sp.getString("email", "");
                        final String date = DateFormat.getDateInstance().format(calendar.getTime());
                        final String[] menu_id = {menu.getName_menu()};
                        final ArrayList<String> newList = new ArrayList<>();


                        Call<MenuUserResponse> call2 = RetrofitClient.getInstance().getApi().getMenuUser(user_id);
                        call2.enqueue(new Callback<MenuUserResponse>() {
                            @Override
                            public void onResponse(Call<MenuUserResponse> call, Response<MenuUserResponse> response) {
                                MenuUserResponse res = response.body();
                                List<MenuUser> menuUserList = res.getMenuUser();

                                if (res.isStatus()) {

                                    MenuUser menuOnDB = checkDate(menuUserList, date);

                                    Log.e("Menu ID", menuOnDB.getMenu_id());

                                    String[] menuOnDBArr = menuOnDB.getMenu_id().split(",");

                                    String menuUpDB = "";
                                    for (int i = 0; i < menuOnDBArr.length; i++) {
                                        if(!menuOnDBArr[i].equals(menu.getName_menu())) {
                                            if(i != (menuOnDBArr.length - 1))
                                                menuUpDB += menuOnDBArr[i] + ",";
                                            else
                                                menuUpDB += menuOnDBArr[i];
                                            System.out.println("DBlength : " + menuUpDB);
                                        }
                                    }

                                    if (menuUpDB.equals(",")) {
                                        menuUpDB = "";
                                    }

                                    Call<DefaultResponse> call2 = RetrofitClient.getInstance().getApi().updateMenuUser(user_id, menuUpDB);
                                    call2.enqueue(new Callback<DefaultResponse>() {
                                        @Override
                                        public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                                        }

                                        @Override
                                        public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                            Log.e("Update Menu User", t.getMessage());
                                        }
                                    });


                                    Log.e("Up menu to database", menuUpDB);
                                }
                            }

                            @Override
                            public void onFailure(Call<MenuUserResponse> call, Throwable t) {

                            }
                        });

                    }

                }

            });
        }

        private void getFavoriteMenu(String email) {
            Call<MenuFavoriteResponse> call = RetrofitClient.getInstance().getApi().getFavoriteMenu(email);
            call.enqueue(new Callback<MenuFavoriteResponse>() {
                @Override
                public void onResponse(Call<MenuFavoriteResponse> call, Response<MenuFavoriteResponse> response) {
                    MenuFavoriteResponse res = response.body();

                    if(res.isStatus()) {
                        faMenuOld = res.getFavoriteMenu();
                    }
                }

                @Override
                public void onFailure(Call<MenuFavoriteResponse> call, Throwable t) {
                    Log.e("Get Fa Menu RE", t.getMessage());
                }
            });
        }

        private void updateFavoriteMenu(String email, String favoriteMenu) {
            Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateFavoriteUser(email, favoriteMenu);
            call.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                    DefaultResponse res = response.body();

                    if(res.isStatus()) {
                        Toast.makeText(mContext, "Update Success", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                    Log.e("Update Favorite Menu", t.getMessage());
                }
            });
        }

    }


    public void filterList(ArrayList arr) {
        this.mMenuList = arr;
        notifyDataSetChanged();
    }

    private MenuUser checkDate(List<MenuUser> menuUserList, String date) {
        for (MenuUser mu : menuUserList) {
            if (date.equals(mu.getDate())) {
                return mu;
            }
        }
        return null;
    }


    Double yin_update = 0.0;
    Double yang_update = 0.0;

    public void chooseAndUpdateyinyang_user(String num_yhin, String num_yhang) {
        sp = mContext.getSharedPreferences("Log in", Context.MODE_PRIVATE);
        String email = sp.getString("email", " ");
        String yin = sp.getString("numYhin", " ");
        String yang = sp.getString("numYhang", " ");


        yin_user = Double.parseDouble(yin);
        yang_user = Double.parseDouble(yang);

        Log.e("beforeyin", String.valueOf(yin));
        Log.e("beforeyang", String.valueOf(yang));

        double num_yin_menu = Double.parseDouble(num_yhin);
        double num_yang_menu = Double.parseDouble(num_yhang);


        sum_yin_menu = num_yin_menu;
        sum_yang_menu = num_yang_menu;

        Log.e("Num Yin", String.valueOf(num_yin_menu));
        Log.e("Num Yang", String.valueOf(num_yang_menu));

        double yinmax = Math.abs(Double.parseDouble(String.format("%.2f", yin_user - 2.4)));
        double yinmin = Math.abs(Double.parseDouble(String.format("%.2f", yin_user - 2.6)));

        double yangmax = Math.abs(Double.parseDouble(String.format("%.2f", yang_user - 2.4)));
        double yangmin = Math.abs(Double.parseDouble(String.format("%.2f", yang_user - 2.6)));

        if (sum_yin_menu >= yinmin && sum_yin_menu <= yinmax) {
            if (sum_yin_menu == num_yin_menu) {
                sum_yin_menu = Double.parseDouble(String.format("%.2f", sum_yin_menu));
            } else {
                sum_yin_menu += Double.parseDouble(String.format("%.2f", sum_yin_menu));
            }
        }

        if (sum_yang_menu >= yangmin && sum_yang_menu <= yangmax) {
            if (sum_yang_menu == num_yang_menu) {
                sum_yang_menu = Double.parseDouble(String.format("%.2f", sum_yang_menu));
            } else {
                sum_yang_menu += Double.parseDouble(String.format("%.2f", sum_yang_menu));
            }

        }
        Calculate_yinyang_user();

        Call<DefaultResponse> call2 = retro.getApi().updateYhinYhang(yin_user, yang_user, email);

        call2.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("numYhin", String.valueOf(yin_user));
                editor.putString("numYhang", String.valueOf(yang_user));
                editor.commit();

                Log.e("Update ", "success");
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.e("Update YY", t.getMessage());
            }
        });

    }


    public void Calculate_yinyang_user() {
        sp = mContext.getSharedPreferences("Log in", Context.MODE_PRIVATE);
        editor = sp.edit();

        String email = sp.getString("email", " ");
        String yin = sp.getString("numYhin", " ");
        String yang = sp.getString("numYhang", " ");


        yin_user = Double.parseDouble(yin);
        yang_user = Double.parseDouble(yang);


        Log.e("beforeyin", String.valueOf(sum_yin_menu));
        Log.e("beforeyang", String.valueOf(sum_yang_menu));


        if (yin_user > 2.6 && yang_user > 2.6) {

            yin_user = Math.abs(yin_user - sum_yin_menu);
            yang_user = Math.abs(yang_user - sum_yang_menu);


        } else if (yin_user < 2.4 && yang_user < 2.4) {
            yin_user = Math.abs(yin_user + sum_yin_menu);
            yang_user = Math.abs(yang_user + sum_yang_menu);


        } else if (yin_user > 2.6 && yang_user < 2.4) {
            yin_user = Math.abs(yin_user - sum_yin_menu);
            yang_user = Math.abs(yang_user + sum_yang_menu);

        } else if (yin_user < 2.4 && yang_user > 2.6) {
            yin_user = Math.abs(yin_user + sum_yin_menu);
            yang_user = Math.abs(yang_user - sum_yang_menu);

        } else if (yin_user < 2.4 && yang_user >= 2.4 && yang_user <= 2.6) {
            yin_user = Math.abs(yin_user + sum_yin_menu);
            yang_user = Math.abs(yang_user + sum_yang_menu);
        } else if (yin_user >= 2.4 && yin_user <= 2.6 && yang_user < 2.4) {
            yin_user = Math.abs(yin_user + sum_yin_menu);
            yang_user = Math.abs(yang_user + sum_yang_menu);
        } else if (yin_user > 2.6 && yang_user >= 2.4 && yang_user <= 2.6) {
            yin_user = Math.abs(yin_user - sum_yin_menu);
            yang_user = Math.abs(yang_user + sum_yang_menu);
        } else if (yin_user >= 2.4 && yin_user <= 2.6 && yang_user > 2.6) {
            yin_user = Math.abs(yin_user + sum_yin_menu);
            yang_user = Math.abs(yang_user - sum_yang_menu);
        }


        Log.e("yin", String.valueOf(yin_user));
        Log.e("yang", String.valueOf(yang_user));


//        Call<DefaultResponse> call2 = retro.getApi().updateYhinYhang(yin_user, yang_user, email);
//
//        call2.enqueue(new Callback<DefaultResponse>() {
//            @Override
//            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
//                DefaultResponse res = response.body();
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<DefaultResponse> call, Throwable t) {
//                Log.e("Update YY", t.getMessage());
//            }
//        });


    }





}
