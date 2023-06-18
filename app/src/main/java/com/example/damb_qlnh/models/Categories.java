package com.example.damb_qlnh.models;

import com.example.damb_qlnh.R;

public enum Categories {
    CATEGORIES1("Appetizers", R.drawable.stater),
    CATEGORIES2("Main Courses", R.drawable.test),
    CATEGORIES3("Side Dishes", R.drawable.midedish),
    CATEGORIES4("Desserts", R.drawable.pudding),
    CATEGORIES5("Drinks", R.drawable.coldstarter);
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
