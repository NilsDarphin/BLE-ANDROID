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

    private BluetoothGatt bluetoothGatt;

    private BluetoothGattService bluetoothGattAutomationService;

    private BluetoothGattCharacteristic digitalOutputCharacteristic;
    private BluetoothGattCharacteristic leftAnalogOutputCharacteristic;
    private BluetoothGattCharacteristic rightAnalogOutputCharacteristic;

    private boolean digitalOutputChanged = false;
    private boolean leftAnalogOutputChanged = false;
    private boolean rightAnalogOutputChanged = false;

    private boolean rightOutputStatus = true;
    private boolean leftOutputStatus = true;
    private int rightOutputPower = 0;
    private int leftOutputPower = 0;

    private int updateStatus = 0;
    private boolean isUpdating = false;

    public Landraider() {}

    public void sendUpdate() {
        switch(updateStatus) {
            case 0:
                if (digitalOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic))
                        digitalOutputChanged = false;
                    break;
                }
                if (leftAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(leftAnalogOutputCharacteristic))
                        leftAnalogOutputChanged = false;
                    break;
                }
                if (rightAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(rightAnalogOutputCharacteristic))
                        rightAnalogOutputChanged = false;
                    break;
                }
            case 1:
                if (leftAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(leftAnalogOutputCharacteristic))
                        leftAnalogOutputChanged = false;
                    break;
                }
                if (rightAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(rightAnalogOutputCharacteristic))
                        rightAnalogOutputChanged = false;
                    break;
                }
                if (digitalOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic))
                        digitalOutputChanged = false;

                    break;
                }
            case 2:
                if (rightAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(rightAnalogOutputCharacteristic))
                        rightAnalogOutputChanged = false;
                    break;
                }
                if (digitalOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic))
                        digitalOutputChanged = false;

                    break;
                }
                if (leftAnalogOutputChanged) {
                    if (bluetoothGatt.writeCharacteristic(leftAnalogOutputCharacteristic))
                        leftAnalogOutputChanged = false;
                    break;
                }
        }

        updateStatus++;
        if (updateStatus == 3)
            updateStatus = 0;
    }

    public void updateLeftOutputStatus(boolean newStatus) {
        if (newStatus != leftOutputStatus) {
            int output = 0;

            leftOutputStatus = newStatus;
            digitalOutputChanged = true;

            if (rightOutputStatus)
                output += 4;
            if (leftOutputStatus)
                output += 1;

            if (digitalOutputCharacteristic != null)
                digitalOutputCharacteristic.setValue(output, BluetoothGattCharacteristic.FORMAT_UINT8, 0);

            sendUpdate();
        }
    }

    public void updateRightOutputStatus(boolean newStatus) {
        if (newStatus != rightOutputStatus) {
            int output = 0;

            rightOutputStatus = newStatus;
            digitalOutputChanged = true;

            if (rightOutputStatus)
                output += 4;
            if (leftOutputStatus)
                output += 1;

            if (digitalOutputCharacteristic != null)
                digitalOutputCharacteristic.setValue(output, BluetoothGattCharacteristic.FORMAT_UINT8, 0);

            sendUpdate();
        }
    }

    public void updateLeftOutputPower(int newPower) {
        Log.d("Debug", "New Left Power = " + newPower);
        int power;
        if (newPower < 0) power = 0;
        else if (newPower > 100) power = 100;
        else power = newPower;

        Log.d("Debug", "Left Power = " + power);
        if (power != rightOutputPower) {
            Log.d("Debug", "New Left Power = " + power);
            leftOutputPower = power;
            leftAnalogOutputChanged = true;

            if (leftAnalogOutputCharacteristic != null)
                leftAnalogOutputCharacteristic.setValue((power*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);

            sendUpdate();
        }
    }

    public void updateRightOutputPower(int newPower) {
        int power;
        if (newPower < 0) power = 0;
        else if (newPower > 100) power = 100;
        else power = newPower;

        if (power != leftOutputPower) {
            rightOutputPower = power;
            rightAnalogOutputChanged = true;

            if (rightAnalogOutputCharacteristic != null)
                rightAnalogOutputCharacteristic.setValue((power*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);

            sendUpdate();
        }
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

        sendUpdate();
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
                if (digitalOutputCharacteristic != null && leftAnalogOutputCharacteristic != null && rightAnalogOutputCharacteristic != null)
                    Log.d("Debug", "OK chars");
            }
        }
    }


}
