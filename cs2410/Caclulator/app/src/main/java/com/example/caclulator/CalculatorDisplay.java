package com.example.caclulator;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatEditText;

public class CalculatorDisplay extends AppCompatEditText {
    public CalculatorDisplay(Context context) {
        super(context);
        setTextSize(32);
        setGravity(Gravity.CENTER);
        setEnabled(false);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams() {
            {
                rowSpec = GridLayout.spec(0, 1, 1.5f);
                columnSpec = GridLayout.spec(0, 4, 1);
            }
        };
        setLayoutParams(params);
        setBackgroundColor(getResources().getColor(R.color.displayColor, null));
        setId(R.id.calculatorDisplay);
    }
}