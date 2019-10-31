package com.example.yinyang_taengkwa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuFavoriteResponse extends DefaultResponse {

    @SerializedName("data")
    @Expose
    private String favoriteMenu;

    public MenuFavoriteResponse(boolean status, String messages, String favoriteMenu) {
        super(status, messages);
        this.favoriteMenu = favoriteMenu;
    }

    public String getFavoriteMenu() {
        return favoriteMenu;
    }

}
