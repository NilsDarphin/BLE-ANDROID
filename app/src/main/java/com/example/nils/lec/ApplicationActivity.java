package com.example.nils.lec;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;

/**
 * Created by nils on 05/01/15.
 */
public abstract class ApplicationActivity extends Activity{

    public final static String DEVICE_ADDRESS = "mac";

    protected BluetoothDevice bluetoothDevice;

    public void setBluetoothDevice ( BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
