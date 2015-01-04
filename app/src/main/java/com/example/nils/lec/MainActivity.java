package com.example.nils.lec;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Handler;


public class MainActivity extends FragmentActivity {

    /*
    private static final long SCAN_PERIOD = 10000;

    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    Vector<BluetoothDevice> bluetoothDevices;

    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            AppsListFragment appsListFragment = new AppsListFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            appsListFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.fragment_container, appsListFragment, "apps").commit();
        }
    }

    @Override
    public void onBackPressed() {
        // Call to previous state
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        // Else slide_to_left
        else super.onBackPressed();
    }

    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("Debug", "Position = " + position);


        myItems.clear();
        myItemsAdapter = new MyItemsAdapter(this, myItems);
        setListAdapter(myItemsAdapter);

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothAdapter.enable();

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        bluetoothDevices = new Vector<BluetoothDevice>();

        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                BluetoothDevice bluetoothDevice = result.getDevice();

                if (bluetoothDevices.contains(bluetoothDevice)) {
                    Log.d("Debug", "Device already detected");
                }
                else {
                    bluetoothDevices.add(bluetoothDevice);
                    Log.d("Debug", "New device detected = " + bluetoothDevice.toString());
                    myItems.add(new MyItem("Proximity Sensor", bluetoothDevice.toString()));

                    //myItemsAdapter = new MyItemsAdapter(this, myItems);

                    setListAdapter(myItemsAdapter);
                }

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);

                Log.d("Debug", "Scan failed = " + errorCode);
            }
        });

    }
    */


}
