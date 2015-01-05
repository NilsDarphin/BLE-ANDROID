package com.example.nils.lec.main_activity;

import java.util.ArrayList;

/**
 * Created by nils on 04/01/15.
 */
public class DevicesManager {

    private ArrayList<ItemList> devices;

    public DevicesManager () {
        devices = new  ArrayList<ItemList>();
    }

    public void add (ItemList itemList) {
        devices.add(itemList);
    }

    public ArrayList<ItemList> getDevices() { return devices; }
}
