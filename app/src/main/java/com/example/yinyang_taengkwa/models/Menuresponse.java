package com.example.yinyang_taengkwa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menuresponse extends DefaultResponse {

    @SerializedName("data")
    @Expose
    private List<Menu> menu;

    public Menuresponse(boolean status, String messages, List<Menu> menu) {
        super(status, messages);
        this.menu = menu;
    }

    public List<Menu> getMenu() {
        return menu;
    }
}
