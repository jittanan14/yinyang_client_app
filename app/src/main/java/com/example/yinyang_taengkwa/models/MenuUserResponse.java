package com.example.yinyang_taengkwa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuUserResponse extends DefaultResponse {

    @SerializedName("data")
    @Expose
    private List<MenuUser> user;

    public MenuUserResponse(boolean status, String messages) {
        super(status, messages);
    }

    public List<MenuUser> getMenuUser() {
        return user;
    }

}
