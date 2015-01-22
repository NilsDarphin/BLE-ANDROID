package com.example.nils.lec.landraider_activity;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;

import android.bluetooth.BluetoothGatt;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LandraiderActivity extends ApplicationActivity {

    private Landraider landraider;

    private TextView landraiderTextView;
    private ProgressBar landraiderProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landraider);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        final View rightView = findViewById(R.id.rightView);
        final View leftView = findViewById(R.id.leftView);
        landraiderTextView = (TextView)findViewById(R.id.landraiderTextView);
        landraiderProgressBar = (ProgressBar) findViewById(R.id.landraiderProgressBar);

        landraiderProgressBar.animate();


        rightView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (landraider != null && landraider.isReady()) {
                    int rightPower = 0;

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            rightPower = (int) (rightView.getBottom() / 2 - event.getY());
                            break;

                        case MotionEvent.ACTION_UP:
                            rightPower = 0;
                    }

                    landraider.setRightOutputStatus(rightPower > 0);
                    landraider.setRightOutputPower((100 * Math.abs(rightPower)) / (rightView.getBottom() / 2));
                }
                return true;
            }
        });

        leftView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (landraider != null && landraider.isReady()) {
                    int leftPower = 0;

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            leftPower = (int) (leftView.getBottom() / 2 - event.getY());
                            break;

                        case MotionEvent.ACTION_UP:
                            leftPower = 0;
                    }

                    landraider.setLeftOutputStatus(leftPower > 0);
                    landraider.setLeftOutputPower((100 * Math.abs(leftPower)) / (rightView.getBottom() / 2));
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
            landraider = new Landraider(new Landraider.LandraiderCallbacks() {
                @Override
                public void onLandraiderReady() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            landraiderProgressBar.setVisibility(View.INVISIBLE);
                            landraiderTextView.setText("Ready !");
                            landraiderTextView.setTextColor(Color.GREEN);
                        }
                    });
                }
            });
            bluetoothDevice.connectGatt(this, false, landraider);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        landraider.stop();
    }
}
