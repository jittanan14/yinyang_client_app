package com.example.yinyang_taengkwa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menuresponse {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("messages")
    @Expose
    private String messages;

    @SerializedName("data")
    @Expose
    private List<Menu> menu;

    public Menuresponse(boolean status, String messages, List<Menu> menu) {
        this.status = status;
        this.messages = messages;
        this.menu = menu;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }

    public List<Menu> getMenu() {
        return menu;
    }
}
