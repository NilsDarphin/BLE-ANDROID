package com.example.nils.lec.io_activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.nils.lec.ApplicationActivity;
import com.example.nils.lec.R;

public class IOActivity extends ApplicationActivity {

    private IO io;

    private View do0;
    private View do1;

    private View di0;
    private View di1;

    private View ao0;
    private View ao1;

    private View ai0;
    private View ai1;
    private View ai2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_io);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        do0 = findViewById(R.id.DO0);
        ((TextView) do0.findViewById(R.id.textViewES)).setText("DO0");
        ((Switch) do0.findViewById(R.id.switchDO)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (io != null)
                    io.writeDO0(isChecked);
            }
        });
        do1 = findViewById(R.id.DO1);
        ((TextView) do1.findViewById(R.id.textViewES)).setText("DO1");
        ((Switch) do1.findViewById(R.id.switchDO)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (io != null)
                    io.writeDO1(isChecked);
            }
        });

        di0 = findViewById(R.id.DI0);
        ((TextView) di0.findViewById(R.id.textViewES)).setText("DI0");
        di1 = findViewById(R.id.DI1);
        ((TextView) di1.findViewById(R.id.textViewES)).setText("DI1");

        ao0 = findViewById(R.id.AO0);
        ((TextView) ao0.findViewById(R.id.textViewES)).setText("AO0");
        ((SeekBar) ao0.findViewById(R.id.seekBarAO)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (io != null)
                    io.writeAO0(seekBar.getProgress());
            }
        });
        ao1 = findViewById(R.id.AO1);
        ((TextView) ao1.findViewById(R.id.textViewES)).setText("AO1");
        ((SeekBar) ao1.findViewById(R.id.seekBarAO)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (io != null)
                    io.writeAO1(seekBar.getProgress());
            }
        });

        ai0 = findViewById(R.id.AI0);
        ((TextView) ai0.findViewById(R.id.textViewES)).setText("AI0");
        ai1 = findViewById(R.id.AI1);
        ((TextView) ai1.findViewById(R.id.textViewES)).setText("AI1");
        ai2 = findViewById(R.id.AI2);
        ((TextView) ai2.findViewById(R.id.textViewES)).setText("AI2");


    }

    @Override
    protected void onBluetoothDeviceFound() {
        super.onBluetoothDeviceFound();

        Log.d("Debug", "onBluetoothDeviceFound");

        if (io == null) {
            io = new IO(new IO.IOCallbacks() {
                @Override
                public void onIOReady() {
                    Log.d("Debug", "Success");
                }

                @Override
                public void onDI0Read(boolean newState) {
                    if (newState)
                        ((ProgressBar) di0.findViewById(R.id.progressBarDI)).setProgress(1);
                    else
                        ((ProgressBar) di0.findViewById(R.id.progressBarDI)).setProgress(0);
                }

                @Override
                public void onDI1Read(boolean newState) {
                    if (newState)
                        ((ProgressBar) di1.findViewById(R.id.progressBarDI)).setProgress(1);
                    else
                        ((ProgressBar) di1.findViewById(R.id.progressBarDI)).setProgress(0);
                }

                @Override
                public void onAI0Read(final int newValue) {
                    ((ProgressBar) ai0.findViewById(R.id.progressBarAI)).setProgress(newValue);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) ai0.findViewById(R.id.valueAI)).setText(newValue + "mV");
                        }
                    });

                }

                @Override
                public void onAI1Read(final int newValue) {
                    ((ProgressBar) ai1.findViewById(R.id.progressBarAI)).setProgress(newValue);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) ai1.findViewById(R.id.valueAI)).setText(newValue + "mV");
                        }
                    });
                }

                @Override
                public void onAI2Read(final int newValue) {
                    ((ProgressBar) ai2.findViewById(R.id.progressBarAI)).setProgress(newValue);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) ai2.findViewById(R.id.valueAI)).setText(newValue + "mV");
                        }
                    });
                }
            });
            bluetoothDevice.connectGatt(this, false, io);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        io.stop();
    }
}
