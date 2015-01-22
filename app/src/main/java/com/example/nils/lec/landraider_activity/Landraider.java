package com.example.nils.lec.landraider_activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.UUID;

/**
 * Created by nils on 21/01/15.
 */
public class Landraider extends BluetoothGattCallback {

    // The callback interface
    public interface LandraiderCallbacks {
        void onLandraiderReady();
    }

    private BluetoothGatt bluetoothGatt;

    private BluetoothGattService bluetoothGattAutomationService;

    private BluetoothGattCharacteristic digitalOutputCharacteristic;
    private BluetoothGattCharacteristic leftAnalogOutputCharacteristic;
    private BluetoothGattCharacteristic rightAnalogOutputCharacteristic;

    private boolean rightOutputStatus = true;
    private boolean leftOutputStatus = true;
    private int rightOutputPower = 0;
    private int leftOutputPower = 0;

    private boolean lastrightOutputStatus = true;
    private boolean lastLeftOutputStatus = true;
    private int lastRightOutputPower = 0;
    private int lastLeftOutputPower = 0;

    private boolean isUpdating = false;
    private boolean isReady = false;

    private LandraiderCallbacks landraiderCallbacks;

    public Landraider(LandraiderCallbacks landraiderCallbacks) {
        this.landraiderCallbacks = landraiderCallbacks;
    }

    private void sendUpdate() {
        if (rightOutputStatus != lastrightOutputStatus || leftOutputStatus != lastLeftOutputStatus) {
            int output = 0;

            if (rightOutputStatus)
                output += 4;
            if (leftOutputStatus)
                output += 1;

            digitalOutputCharacteristic.setValue(output, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            digitalOutputCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            if (bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic)) {
                lastrightOutputStatus = rightOutputStatus;
                lastLeftOutputStatus = leftOutputStatus;
                isUpdating = true;
            }
        }
        else if (leftOutputPower != lastLeftOutputPower || rightOutputPower != lastRightOutputPower) {

            if (Math.abs(leftOutputPower - lastLeftOutputPower) >= Math.abs(rightOutputPower - lastRightOutputPower)) {
                leftAnalogOutputCharacteristic.setValue((leftOutputPower*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
                leftAnalogOutputCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                if (bluetoothGatt.writeCharacteristic(leftAnalogOutputCharacteristic)) {
                    lastLeftOutputPower = leftOutputPower;
                    isUpdating = true;
                }
            }
            else {
                rightAnalogOutputCharacteristic.setValue((rightOutputPower*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
                rightAnalogOutputCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
                if (bluetoothGatt.writeCharacteristic(rightAnalogOutputCharacteristic)) {
                    lastRightOutputPower = rightOutputPower;
                    isUpdating = true;
                }
            }
        }
    }

    private void tryUpdate() {
        if (isUpdating == false) {
            sendUpdate();
        }
    }

    public void setRightOutputStatus(boolean rightOutputStatus) {
        this.rightOutputStatus = rightOutputStatus;
        tryUpdate();
    }

    public void setLeftOutputStatus(boolean leftOutputStatus) {
        this.leftOutputStatus = leftOutputStatus;
        tryUpdate();
    }

    public void setRightOutputPower(int rightOutputPower) {
        if (rightOutputPower > 100) this.rightOutputPower = 100;
        else if (rightOutputPower < 0) this.rightOutputPower = 0;
        else this.rightOutputPower = rightOutputPower;
        tryUpdate();
    }

    public void setLeftOutputPower(int leftOutputPower) {
        if (leftOutputPower > 100) this.leftOutputPower = 100;
        else if (leftOutputPower < 0) this.leftOutputPower = 0;
        else this.leftOutputPower = leftOutputPower;
        tryUpdate();
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
                    landraiderCallbacks.onLandraiderReady();
                }
            }
        }
    }


}
