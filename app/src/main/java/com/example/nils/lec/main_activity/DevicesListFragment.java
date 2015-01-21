package com.example.nils.lec.main_activity;

import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;
import com.example.nils.lec.proximity_activity.ProximityActivity;

import java.util.Vector;

/**
 * Created by nils on 04/01/15.
 */
public class DevicesListFragment extends ListFragment {

    public static final String ARG_POSITION = "Position" ;
    private ItemsAdapter itemsAdapter;
    private DevicesManager devicesManager;
    private AppsManager.App appSelected;

    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    Vector<BluetoothDevice> bluetoothDevices;

    public DevicesListFragment (AppsManager.App app) {
        super();

        this.appSelected = app;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (devicesManager == null) {


            //appSelected = ((MainActivity) getActivity()).getAppsManager().getApps().get(savedInstanceState.getInt(ARG_POSITION));

            devicesManager = new DevicesManager();
            itemsAdapter = new ItemsAdapter(getActivity(), devicesManager.getDevices());

            setListAdapter(itemsAdapter);


            BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();

            bluetoothAdapter.enable();

            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

            bluetoothDevices = new Vector<BluetoothDevice>();


            bluetoothLeScanner.startScan(new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);

                    BluetoothDevice bluetoothDevice = result.getDevice();

                    if (bluetoothDevices.contains(bluetoothDevice) == false) {
                        bluetoothDevices.add(bluetoothDevice);

                        // Compatibility test
                        if (appSelected.deviceFilter.isCompatible(bluetoothDevice)) {
                            devicesManager.add(new ItemList(bluetoothDevice.getName(), bluetoothDevice.toString()));
                            itemsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        bluetoothLeScanner.stopScan(null);

        Intent intent = new Intent(getActivity(), appSelected.getApplicationActivity());
        intent.putExtra(ApplicationActivity.DEVICE_ADDRESS, devicesManager.getDevices().get(position).getDescription());
        startActivity(intent);
    }
}