package com.example.nils.lec.io_activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
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
        void onDI0Read(boolean newState);
        void onDI1Read(boolean newState);
        void onAI0Read(int newValue);
        void onAI1Read(int newValue);
        void onAI2Read(int newValue);
    }

    private BluetoothGatt bluetoothGatt;

    private BluetoothGattService bluetoothGattAutomationService;

    private BluetoothGattCharacteristic digitalOutputCharacteristic;
    private BluetoothGattCharacteristic digitalInputCharacteristic;
    private BluetoothGattCharacteristic analogOutputCharacteristic0;
    private BluetoothGattCharacteristic analogOutputCharacteristic1;
    private BluetoothGattCharacteristic analogInputCharacteristic0;
    private BluetoothGattCharacteristic analogInputCharacteristic1;
    private BluetoothGattCharacteristic analogInputCharacteristic2;

    private boolean do0Output;
    private boolean do1Output;

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

    public void writeDO0(boolean newOutput) {
        if (isReady()) {
            int output = 0;

            do0Output = newOutput;

            if (do0Output)
                output += 1;
            if (do1Output)
                output += 4;

            digitalOutputCharacteristic.setValue(output, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            digitalOutputCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic);
        }
    }

    public void writeDO1(boolean newOutput) {
        if (isReady()) {
            int output = 0;

            do1Output = newOutput;

            if (do0Output)
                output += 1;
            if (do1Output)
                output += 4;

            digitalOutputCharacteristic.setValue(output, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            digitalOutputCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            bluetoothGatt.writeCharacteristic(digitalOutputCharacteristic);
        }
    }

    public void writeAO0(int newValue) {
        if (isReady()) {
            int output = 0;

            if (newValue >= 100)
                output = 100;
            else if (newValue <= 0)
                output = 0;
            else output = newValue;

            analogOutputCharacteristic0.setValue((output*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
            analogOutputCharacteristic0.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            bluetoothGatt.writeCharacteristic(analogOutputCharacteristic0);
        }
    }

    public void writeAO1(int newValue) {
        if (isReady()) {
            int output = 0;

            if (newValue >= 100)
                output = 100;
            else if (newValue <= 0)
                output = 0;
            else output = newValue;

            analogOutputCharacteristic1.setValue((output*0xffff)/100, BluetoothGattCharacteristic.FORMAT_UINT16, 0);
            analogOutputCharacteristic1.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            bluetoothGatt.writeCharacteristic(analogOutputCharacteristic1);
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
                        if (analogOutputCharacteristic0 == null) {
                            analogOutputCharacteristic0 = characteristic;
                        }
                        else if (analogOutputCharacteristic1 == null) {
                            analogOutputCharacteristic1 = characteristic;
                        }
                    }

                    // Digital output
                    if (characteristic.getUuid().equals(UUID.fromString("00002a57-0000-1000-8000-00805F9B34FB"))) {
                        if (digitalOutputCharacteristic == null) {
                            digitalOutputCharacteristic = characteristic;
                        }
                    }

                    // Analog Input
                    if (characteristic.getUuid().equals(UUID.fromString("00002a58-0000-1000-8000-00805F9B34FB"))) {
                        if (analogInputCharacteristic0 == null) {
                            analogInputCharacteristic0 = characteristic;
                        }
                        else if (analogInputCharacteristic1 == null) {
                            analogInputCharacteristic1 = characteristic;
                        }
                        else if (analogInputCharacteristic2 == null) {
                            analogInputCharacteristic2 = characteristic;
                        }
                    }

                    // Digital Input
                    if (characteristic.getUuid().equals(UUID.fromString("00002a56-0000-1000-8000-00805F9B34FB"))) {
                        if (digitalInputCharacteristic == null) {
                            digitalInputCharacteristic = characteristic;
                        }
                    }

                }
                if (digitalOutputCharacteristic != null  && digitalInputCharacteristic != null && analogOutputCharacteristic0 != null && analogOutputCharacteristic1 != null && analogInputCharacteristic0 != null && analogInputCharacteristic1 != null && analogInputCharacteristic2 != null) {
                    isReady = true;
                    ioCallbacks.onIOReady();
                    Log.d("Debug", "Gatt ready !");
                    bluetoothGatt.setCharacteristicNotification(digitalInputCharacteristic, true);
                    BluetoothGattDescriptor descriptor = digitalInputCharacteristic.getDescriptor(
                            UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);

        Log.d("Debug", "Good !");
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        Log.d("Debug", "Changed !");
        byte[] data = characteristic.getValue();

        ioCallbacks.onDI0Read((data[0] & 0x01) == 0x01);
        ioCallbacks.onDI1Read((data[0] & 0x02) == 0x02);
    }
}
