package com.example.nils.lec;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by nils on 05/01/15.
 */
public abstract class ApplicationActivity extends Activity{

    public final static String DEVICE_ADDRESS = "mac";

    protected BluetoothDevice bluetoothDevice;

    Activity activity;

    public void setBluetoothDevice ( BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        final BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                if (result.getDevice().getAddress().equals(getIntent().getStringExtra(DEVICE_ADDRESS))) {
                    bluetoothDevice = result.getDevice();

                    bluetoothLeScanner.stopScan(new ScanCallback() {
                        @Override
                        public void onScanResult(int callbackType, ScanResult result) {
                            super.onScanResult(callbackType, result);


                        }
                    });
                    onBluetoothDeviceFound();
                }
            }
        });
    }

    protected void onBluetoothDeviceFound() {}
}
