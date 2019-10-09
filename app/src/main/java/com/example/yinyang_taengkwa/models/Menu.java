package com.example.yinyang_taengkwa.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Menu implements Parcelable  {

    private String name,num_yhin,num_yhang,category,ingredient,howto,image;
    private int favorite;

    public Menu(String name, String num_yhin, String num_yhang, String category, String ingredient, String howto, String image, int favorite){
        this.name = name;
        this.num_yhin = num_yhin;
        this.num_yhang = num_yhang;
        this.category = category;
        this.ingredient = ingredient;
        this.howto = howto;
        this.image = image;
        this.favorite = favorite;

    }

    protected Menu(Parcel in) {
        name = in.readString();
        num_yhin = in.readString();
        num_yhang = in.readString();
        category = in.readString();
        ingredient = in.readString();
        howto = in.readString();
        image = in.readString();
        favorite = in.readInt();
    }

    public static final Creator<Menu> CREATOR = new Creator<Menu>() {
        @Override
        public Menu createFromParcel(Parcel in) {
            return new Menu(in);
        }

        @Override
        public Menu[] newArray(int size) {
            return new Menu[size];
        }
    };

    public String getName_menu() { return name;}

    public String getNum_yhin() { return num_yhin;}

    public String getNum_yhang(){ return num_yhang;}

    public String getCategory_menu() { return category;}

    public String getIngredient_menu() { return ingredient;}

    public String getHowto_food() { return  howto;}

    public String getImage_menu() { return image;}

    public int getFavorite() {
        return favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(num_yhin);
        parcel.writeString(num_yhang);
        parcel.writeString(category);
        parcel.writeString(ingredient);
        parcel.writeString(howto);
        parcel.writeString(image);
        parcel.writeInt(favorite);
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;

    }
}
