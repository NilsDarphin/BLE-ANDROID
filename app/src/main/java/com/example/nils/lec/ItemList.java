package com.example.nils.lec;

/**
 * Created by nils on 04/01/15.
 */
public  class ItemList {
    public int resId;
    public String name;
    public String description;

    public ItemList(int resId, String name, String description) {
        this.resId = resId;
        this.name = name;
        this.description = description;
    }

    public ItemList(String name, String description) {
        this.resId = R.drawable.ic_launcher;
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getResId() {
        return resId;
    }

}