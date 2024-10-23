package com.gmyboy.codelab.accesibility.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.gmyboy.codelab.accesibility.R;

public class CoordinateDialog extends Dialog {
    private SeekBar seekBarX;
    private SeekBar seekBarY;
    private EditText intervalInput;
    private Button confirmButton;
    private int maxCoordinate = 100; // Max value for SeekBar

    private OnCoordinateSetListener onCoordinateSetListener;

    public CoordinateDialog(@NonNull Context context, OnCoordinateSetListener listener) {
        super(context);
        this.onCoordinateSetListener = listener;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_coordinate); // Use a custom layout
        setTitle("Set Coordinates and Interval");

        seekBarX = findViewById(R.id.seekBarX);
        seekBarY = findViewById(R.id.seekBarY);
        intervalInput = findViewById(R.id.intervalInput);
        confirmButton = findViewById(R.id.confirmButton);

        // Set max value for SeekBars
        seekBarX.setMax(maxCoordinate);
        seekBarY.setMax(maxCoordinate);

        // Set the Confirm button click listener
        confirmButton.setOnClickListener(v -> {
            int x = seekBarX.getProgress();
            int y = seekBarY.getProgress();
            int interval;
            try {
                interval = Integer.parseInt(intervalInput.getText().toString());
            } catch (NumberFormatException e) {
                interval = 0; // Default to 0 if invalid input
            }

            if (onCoordinateSetListener != null) {
                onCoordinateSetListener.onCoordinateSet(x, y, interval);
            }
            dismiss(); // Close the dialog
        });
    }

    public interface OnCoordinateSetListener {
        void onCoordinateSet(int x, int y, int interval);
    }
}
