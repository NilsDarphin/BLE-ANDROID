package com.example.nils.lec.io_activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.UUID;

/**
 * Created by nils on 21/01/15.
 */
public class IO extends BluetoothGattCallback {

    // The callback interface
    public interface IOCallbacks {
        void onIOReady();
    }

    private BluetoothGatt bluetoothGatt;

    private BluetoothGattService bluetoothGattAutomationService;

    private BluetoothGattCharacteristic digitalOutputCharacteristic;
    private BluetoothGattCharacteristic leftAnalogOutputCharacteristic;
    private BluetoothGattCharacteristic rightAnalogOutputCharacteristic;

    private boolean isUpdating = false;
    private boolean isReady = false;

    private IOCallbacks ioCallbacks;

    public IO(IOCallbacks ioCallbacks) {
        this.ioCallbacks = ioCallbacks;
    }

    private void sendUpdate() {

    }

    private void tryUpdate() {
        if (isUpdating == false) {
            sendUpdate();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.d("Debug", "Status changed");
        super.onConnectionStateChange(gatt, status, newState);
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            Log.d("Debug", "connected");
            this.bluetoothGatt = gatt;
            if (bluetoothGattAutomationService == null)
                bluetoothGatt.discoverServices();
        }
        else if (newState== BluetoothGatt.STATE_DISCONNECTED) {
            Log.d("Debug", "disconnected");

        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        isUpdating = false;

        tryUpdate();
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if (status == BluetoothGatt.GATT_SUCCESS) {

            // Bluetooth Automation-IO service
            bluetoothGattAutomationService = bluetoothGatt.getService(UUID.fromString("00001815-0000-1000-8000-00805F9B34FB"));
            if (bluetoothGattAutomationService != null) {
                for (BluetoothGattCharacteristic characteristic :  bluetoothGattAutomationService.getCharacteristics()) {

                    // Analog output
                    if (characteristic.getUuid().equals(UUID.fromString("00002a59-0000-1000-8000-00805F9B34FB"))) {
                        if (leftAnalogOutputCharacteristic == null) {
                            leftAnalogOutputCharacteristic = characteristic;
                        }
                        else if (rightAnalogOutputCharacteristic == null) {
                            rightAnalogOutputCharacteristic = characteristic;
                        }
                    }

                    // Digital output
                    if (characteristic.getUuid().equals(UUID.fromString("00002a57-0000-1000-8000-00805F9B34FB"))) {
                        if (digitalOutputCharacteristic == null) {
                            digitalOutputCharacteristic = characteristic;
                        }
                    }

                }
                if (digitalOutputCharacteristic != null && leftAnalogOutputCharacteristic != null && rightAnalogOutputCharacteristic != null) {
                    isReady = true;
                    ioCallbacks.onIOReady();
                }
            }
        }
    }


}
