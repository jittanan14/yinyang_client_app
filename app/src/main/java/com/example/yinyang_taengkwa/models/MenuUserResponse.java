package com.example.yinyang_taengkwa.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuUserResponse {
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("messages")
    @Expose
    private String messages;

    @SerializedName("data")
    @Expose
    private List<MenuUser> user;

    public MenuUserResponse(boolean status, String messages, List<MenuUser> user) {
        this.status = status;
        this.messages = messages;
        this.user = user;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessages() {
        return messages;
    }

    public List<MenuUser> getMenuUser() {
        return user;
    }

}
