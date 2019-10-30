package com.example.yinyang_taengkwa.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yinyang_taengkwa.Question;
import com.example.yinyang_taengkwa.Question_list;
import com.example.yinyang_taengkwa.R;
import com.example.yinyang_taengkwa.activities.ChooseActivity;
import com.example.yinyang_taengkwa.activities.EditprofileActivity;
import com.example.yinyang_taengkwa.activities.FavoriteActivity;
import com.example.yinyang_taengkwa.activities.RegisterActivity;
import com.example.yinyang_taengkwa.api.RetrofitClient;
import com.example.yinyang_taengkwa.models.User;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_profile extends Fragment {

    private CircleImageView userProfile;
    private TextView emailTextView, usernameTextView, genderTextView, birthdayTextView, elementTextView, foodLoseTextView, bodyTextView, numYhinTextView, numYhangTextView,
            favorite, choose_menu;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String PREF_NAME = "Log in";
    private LinearLayout editData;
    private LinearLayout layout_Gotoquestion, layout_choosequestion;
    private SwipeRefreshLayout swipeRefreshLayout;
    RetrofitClient retro;


    String url = "http://pilot.cp.su.ac.th/usr/u07580536/yhinyhang/images/profile/";

    public Fragment_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        retro = new RetrofitClient();

        sp = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sp.edit();

        layout_Gotoquestion = view.findViewById(R.id.layout_Gotoquestion);
        emailTextView = view.findViewById(R.id.email_text_view);
        usernameTextView = view.findViewById(R.id.username_text_view);
        genderTextView = view.findViewById(R.id.gender_text_view);
        editData = view.findViewById(R.id.layout_editData);
        birthdayTextView = view.findViewById(R.id.birth_text_view);
        elementTextView = view.findViewById(R.id.element_text_view);
        foodLoseTextView = view.findViewById(R.id.foodlose_text_view);
        bodyTextView = view.findViewById(R.id.body_text_view);
        numYhinTextView = view.findViewById(R.id.num_yhin_text_view);
        numYhangTextView = view.findViewById(R.id.num_yhang_text_view);
        userProfile = view.findViewById(R.id.user_profile);
        favorite = view.findViewById(R.id.favourite);
        choose_menu = view.findViewById(R.id.choose_menu);
        layout_choosequestion = view.findViewById(R.id.layout_choosequestion);

        //  Pull to Refresh
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);


        final String email = sp.getString("email", "");
        final String username = sp.getString("username", "");
        final String gender = sp.getString("gender", "");
        final String birthday = sp.getString("birthday", "");
        final String element = sp.getString("element", "");
        final String foodLose = sp.getString("foodLose", "");
        final String image = sp.getString("image", "");
        final String body = sp.getString("body", "");
        final String numYhin = sp.getString("numYhin", "");
        final String numYhang = sp.getString("numYhang", "");


        emailTextView.setText(email);
        usernameTextView.setText(username);
        birthdayTextView.setText(birthday);
        elementTextView.setText(element);
        if (foodLose.equals("-")) {
            foodLoseTextView.setText("ไม่แพ้วัตถุดิบอาหารชนิดใด");
        } else {
            foodLoseTextView.setText(foodLose);
        }
        bodyTextView.setText(body);
        numYhinTextView.setText(numYhin);
        numYhangTextView.setText(numYhang);

        if (gender.equals("m")) {
            genderTextView.setText("ชาย");
        } else {
            genderTextView.setText("หญิง");
        }


//        image = "5d51cd67d28ae.jpg";
        if (image.isEmpty() || image == null) {
            userProfile.setImageResource(R.drawable.ic_user);
        } else {
            String url1 = url.concat(image);
            Picasso.get().load(url1).into(userProfile);
        }


        layout_choosequestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Question_list.class);
                intent.putExtra("ch","1");
                startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FavoriteActivity.class));
            }
        });

        layout_Gotoquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Question.class);
                intent.putExtra("ch","0");
                startActivity(intent);
            }
        });

        choose_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseActivity.class));
            }
        });

        //Refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }, 1000);

            }
        });

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
                Intent intent = new Intent(getContext(), EditprofileActivity.class);
                intent.putExtra("image", image);
                intent.putExtra("username", username);
                intent.putExtra("foodLose", foodLose);
                startActivity(intent);
            }
        });


        Call<User> call2 = retro.getApi().getProfile(email);
        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                String username = user.getUsername();
                String food = user.getFood();
                String image2 = user.getImage_user();


                usernameTextView.setText(username);
                foodLoseTextView.setText(" " + food);

                String url1 = url.concat(image2);
                Picasso.get().load(url1).into(userProfile);

                editor.putString("image", image2);
                editor.commit();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        Call<User> call = retro.getApi().getYinyang(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                String email = user.getEmail();
                String username = user.getUsername();
                String gender = user.getGender();
                String birthday = user.getBirthday();
                String element = user.getElement();
                String food = user.getFood();
                String yin = user.getNum_yhin();
                String image2 = user.getImage_user();
                String yang = user.getNum_yhang();
                String body = user.getBody();

                numYhinTextView.setText(yin);
                numYhangTextView.setText(yang);
                bodyTextView.setText(body);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


            }
        });


        return view;


    }


    public void refreshData() {

        final String email = sp.getString("email", "");

        Call<User> call2 = retro.getApi().getProfile(email);
        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                String username = user.getUsername();
                String food = user.getFood();
                String image2 = user.getImage_user();


                usernameTextView.setText(username);
                foodLoseTextView.setText(" " + food);

                String url1 = url.concat(image2);
                Picasso.get().load(url1).into(userProfile);

                editor.putString("image", image2);
                editor.commit();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


        Call<User> call = retro.getApi().getYinyang(email);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                String email = user.getEmail();
                String username = user.getUsername();
                String gender = user.getGender();
                String birthday = user.getBirthday();
                String element = user.getElement();
                String food = user.getFood();
                String yin = user.getNum_yhin();
                String image2 = user.getImage_user();
                String yang = user.getNum_yhang();
                String body = user.getBody();

                numYhinTextView.setText(yin);
                numYhangTextView.setText(yang);
                bodyTextView.setText(body);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {


            }
        });
    }
}


