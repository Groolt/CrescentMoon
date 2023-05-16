package com.example.damb_qlnh.models;

import com.example.damb_qlnh.R;

public enum Categories {
    CATEGORIES1("Starter", R.drawable.test),
    CATEGORIES2("Main Course", R.drawable.test),
    CATEGORIES3("Side Dish", R.drawable.test),
    CATEGORIES4("Pudding", R.drawable.test),
    CATEGORIES5("Cold Starter", R.drawable.test);
    private String name;
    private int img;

    Categories(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
