package com.example.nils.lec;

import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.Vector;

/**
 * Created by nils on 04/01/15.
 */
public class DevicesListFragment extends ListFragment {

    public static final String ARG_POSITION = "Position" ;
    private ItemsAdapter itemsAdapter;
    private DevicesManager devicesManager;
    private AppsManager.App appSelected;

    private static final long SCAN_PERIOD = 10000;

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

                        if (appSelected.deviceFilter.isCompatible(bluetoothDevice)) {
                            Log.d("Debug", "Compatible");
                            devicesManager.add(new ItemList(bluetoothDevice.getName(), bluetoothDevice.toString()));
                            itemsAdapter.notifyDataSetChanged();
                        }
                        else
                            Log.d("Debug", "Non compatible");
                    }
                }


                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);

                    Log.d("Debug", "Scan failed = " + errorCode);
                }
            });
        }

    }
}