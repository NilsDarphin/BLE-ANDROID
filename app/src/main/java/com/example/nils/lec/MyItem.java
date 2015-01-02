package com.example.nils.lec;

/**
 * Created by nils on 02/01/15.
 */
public class MyItem {

    private String name;
    private String description;
    private int resId;

    public MyItem (String name, String description) {
        this.resId = R.drawable.ic_launcher;
        this.name = name;
        this.description = description;
    }

    public MyItem(int resId, String name, String description) {
        this.resId = resId;
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
