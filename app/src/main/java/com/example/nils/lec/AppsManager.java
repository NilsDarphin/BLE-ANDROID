package com.example.nils.lec;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

/**
 * Created by nils on 04/01/15.
 */
public class AppsManager {

    public interface DeviceFilter {
        public boolean isCompatible(BluetoothDevice bluetoothDevice);
    }

    public class App {
        private ItemList itemList;
        public DeviceFilter deviceFilter;

        public App(ItemList itemList, DeviceFilter deviceFilter) {
            this.itemList = itemList;
            this.deviceFilter = deviceFilter;
        }

        public ItemList getItemList() { return itemList; }
    }

    private ArrayList<App> apps;

    public AppsManager () {
        apps = new  ArrayList<App>();

        apps.add(new App(new ItemList("Proximity sensor", "Evaluate the distance between your phone and a LE Tag"), new DeviceFilter(){

            @Override
            public boolean isCompatible(BluetoothDevice bluetoothDevice) {
                return true;
            }
        }));

    }

    public ArrayList<App> getApps() { return apps; }
}