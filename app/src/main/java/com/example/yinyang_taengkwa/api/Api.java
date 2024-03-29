package com.example.yinyang_taengkwa.api;


import com.example.yinyang_taengkwa.models.DefaultResponse;
import com.example.yinyang_taengkwa.models.LoginResponse;
import com.example.yinyang_taengkwa.models.MenuFavoriteResponse;
import com.example.yinyang_taengkwa.models.MenuUserResponse;
import com.example.yinyang_taengkwa.models.Menuresponse;
import com.example.yinyang_taengkwa.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("create_user.php")
    Call<DefaultResponse> createUser(
            @Field("email_user") String email_user,
            @Field("pass_user") String pass_user,
            @Field("name_user") String name_user,
            @Field("gender_user") String gender_user,
            @Field("Birth_user") String Birth_user,
            @Field("element_user") String element_user,
            @Field("food_user") String food_user,
            @Field("Pic_user") String Pic_user,
            @Field("body_user") String body_user,
            @Field("num_yhin") double num_yhin,
            @Field("num_yhang") double num_yhang
    );

    @FormUrlEncoded
    @POST("loginuser.php")
    Call<LoginResponse> userLogin(
            @Field("email_user") String email,
            @Field("pass_user") String password
    );

    @FormUrlEncoded
    @POST("update_yhinyhang.php")
    Call<DefaultResponse> updateYhinYhang (
            @Field("num_yhin") double num_yhin,
            @Field("num_yhang") double num_yhang,
            @Field("email_user") String email
    );

    @FormUrlEncoded
    @POST("update_favorite.php")
    Call<DefaultResponse> updateFavorite (
            @Field("name_menu") String name_menu,
            @Field("favorite_menu") int favorite_menu
    );

    @FormUrlEncoded
    @POST("update_choosemenu.php")
    Call<DefaultResponse> updateChoose (
            @Field("name_menu") String name_menu,
            @Field("choose_menu") int choose_menu
    );

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<DefaultResponse> updateProfile (
            @Field("email_user") String email_user,
            @Field("Pic_user") String Pic_user,
            @Field("name_user") String name_user,
            @Field("food_user") String food_user
    );

    @FormUrlEncoded
    @POST("getyinyang.php")
    Call<User> getYinyang(
            @Field("email_user") String email

    );

    @FormUrlEncoded
    @POST("getprofile.php")
    Call<User> getProfile(
            @Field("email_user") String email

    );

    @GET("getmenu.php")
    Call<Menuresponse> getmenu();

    @FormUrlEncoded
    @POST("create_menu_user.php")
    Call<DefaultResponse> createMenuUser(
            @Field("user_id") String user_id,
            @Field("date") String date,
            @Field("menu_id") String menu_id
    );

    @FormUrlEncoded
    @POST("update_menu_user.php")
    Call<DefaultResponse> updateMenuUser (
            @Field("user_id") String user_id,
            @Field("menu_id") String menu_id
    );


    @FormUrlEncoded
    @POST("get_menu_user.php")
    Call<MenuUserResponse> getMenuUser(
            @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("create_favorite_user.php")
    Call<DefaultResponse> createFavoriteUser(
            @Field("email_user") String user_id,
            @Field("name_menu") String name_menu
    );

    @FormUrlEncoded
    @POST("update_favorite_user.php")
    Call<DefaultResponse> updateFavoriteUser(
            @Field("email_user") String email_user,
            @Field("name_menu") String name_menu
    );

    @FormUrlEncoded
    @POST("get_favorite_menu.php")
    Call<MenuFavoriteResponse> getFavoriteMenu(
            @Field("email_user") String email_user
    );

}
