package com.example.yinyang_taengkwa.models;

public class MenuUser {

    private String user_id, date, menu_id;

    public MenuUser(String user_id, String date, String menu_id) {
        this.user_id = user_id;
        this.date = date;
        this.menu_id = menu_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public String getMenu_id() {
        return menu_id;
    }
}