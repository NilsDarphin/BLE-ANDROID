package com.example.nils.lec.main_activity;

import android.bluetooth.BluetoothDevice;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.ItemList;
import com.example.nils.lec.R;
import com.example.nils.lec.io_activity.IOActivity;
import com.example.nils.lec.landraider_activity.LandraiderActivity;
import com.example.nils.lec.proximity_activity.ProximityActivity;

import java.util.ArrayList;

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

                return true;

            }
        }, ProximityActivity.class));

        apps.add(new App(new ItemList(R.drawable.landraider, "Land Raider", "Control a bluetooth car"), new DeviceFilter(){

            @Override
            public boolean isCompatible(BluetoothDevice bluetoothDevice) {
                if (bluetoothDevice.getName().equals("BLE AutomationIO"))
                    return true;
                return false;
            }
        }, LandraiderActivity.class));

        apps.add(new App(new ItemList("IO", "Control your IOs !"), new DeviceFilter(){

            @Override
            public boolean isCompatible(BluetoothDevice bluetoothDevice) {
                if (bluetoothDevice.getName().equals("BLE AutomationIO"))
                    return true;
                return false;
            }
        }, IOActivity.class));

    }

    public ArrayList<App> getApps() { return apps; }
}