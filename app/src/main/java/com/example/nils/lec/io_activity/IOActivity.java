package com.example.nils.lec.io_activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;

public class IOActivity extends ApplicationActivity {

    private IO io;

    private Switch do0;
    private Switch do1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_io);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        do0 = (Switch) findViewById(R.id.DO0).findViewById(R.id.switch1);
        do1 = (Switch) findViewById(R.id.DO1).findViewById(R.id.switch1);

    }

    @Override
    protected void onBluetoothDeviceFound() {
        super.onBluetoothDeviceFound();

        Log.d("Debug", "onBluetoothDeviceFound");
/*
        if (io == null) {
            io = new IO(new IO.IOCallbacks() {
                @Override
                public void onIOReady() {
                }
            });
            bluetoothDevice.connectGatt(this, false, io);
        }*/
    }

}
