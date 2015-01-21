package com.example.nils.lec.landraider_activity;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;

import android.bluetooth.BluetoothGatt;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LandraiderActivity extends ApplicationActivity {

    private BluetoothGatt bluetoothGatt;
    private Landraider landraider;

    private boolean rightOutput = false;
    private boolean leftOutput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landraider);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final View rightView = findViewById(R.id.rightView);
        final View leftView = findViewById(R.id.leftView);

        rightView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int rightPower = 0;

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        rightPower = (int) (rightView.getBottom()/2 - event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        rightPower = 0;
                }

                if (landraider != null) {
                    landraider.updateRightOutputStatus(rightPower > 0);
                    landraider.updateRightOutputPower((100*Math.abs(rightPower))/(rightView.getBottom()/2));
                }

                return true;
            }
        });

        leftView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int leftPower = 0;

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        leftPower = (int) (leftView.getBottom()/2 - event.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        leftPower = 0;
                }

                if (landraider != null) {
                    landraider.updateLeftOutputStatus(leftPower > 0);
                    landraider.updateLeftOutputPower((100*Math.abs(leftPower))/(rightView.getBottom()/2));
                }

                return true;
            }
        });
    }

    @Override
    protected void onBluetoothDeviceFound() {
        super.onBluetoothDeviceFound();

        Log.d("Debug", "onBluetoothDeviceFound");

        if (landraider == null) {
            landraider = new Landraider();
            bluetoothGatt = bluetoothDevice.connectGatt(this, false, landraider);
        }
    }
}
