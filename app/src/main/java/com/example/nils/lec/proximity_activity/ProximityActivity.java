package com.example.nils.lec.proximity_activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;

public class ProximityActivity extends ApplicationActivity {

    private ProgressBar progressBar;
    private BluetoothGatt bluetoothGatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onBluetoothDeviceFound() {
        super.onBluetoothDeviceFound();

        bluetoothGatt = bluetoothDevice.connectGatt(this, true, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);

                if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
                    bluetoothGatt.readRemoteRssi();
                }
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);

                progressBar.setProgress(rssi + 105);

                if (bluetoothGatt != null)
                    bluetoothGatt.readRemoteRssi();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        bluetoothGatt.disconnect();
        bluetoothGatt = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_proximity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
