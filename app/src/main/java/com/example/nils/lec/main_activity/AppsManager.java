package com.example.nils.lec.main_activity;

import android.bluetooth.BluetoothDevice;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.proximity_activity.ProximityActivity;

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
        private Class<? extends ApplicationActivity> applicationActivityClass;

        public App(ItemList itemList, DeviceFilter deviceFilter, Class<? extends ApplicationActivity> applicationActivityClass) {
            this.itemList = itemList;
            this.deviceFilter = deviceFilter;
            this.applicationActivityClass = applicationActivityClass;
        }

        public ItemList getItemList() {
            return itemList;
        }

        public Class<? extends ApplicationActivity> getApplicationActivity() {
            return applicationActivityClass;
        }
    }

    private ArrayList<App> apps;

    public AppsManager () {
        apps = new  ArrayList<App>();

        apps.add(new App(new ItemList("Proximity sensor", "Evaluate the distance between your phone and a LE Tag"), new DeviceFilter(){

            @Override
            public boolean isCompatible(BluetoothDevice bluetoothDevice) {

                if (bluetoothDevice.getName().equals("WICED Sense Kit"))
                    return true;

                return false;
            }
        }, ProximityActivity.class));

    }

    public ArrayList<App> getApps() { return apps; }
}