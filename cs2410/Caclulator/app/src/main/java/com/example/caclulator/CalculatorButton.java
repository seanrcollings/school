package com.example.caclulator;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;


public class CalculatorButton extends AppCompatButton {
    CalculatorButton(Context context, CalculatorButtonData data, final View.OnClickListener onClick) {
        super(context);
        setText(data.getText());

        setOnClickListener(onClick::onClick);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams() {
            {
                rowSpec = GridLayout.spec(data.getRow(), 1, 1);
                columnSpec = GridLayout.spec(data.getColumn(), data.getSize(), 1);
            }
        };
        GradientDrawable drawable = new GradientDrawable() {
            {
                int color = data.getType() == CalculatorButtonData.ButtonType.NUMBER ? R.color.colorPrimary : R.color.operatorColor;
                setColor(ResourcesCompat.getColor(getResources(), color, null));
                setStroke(2, Color.BLACK);
            }
        };


//        RippleDrawable rippleDrawable = new RippleDrawable(ColorStateList.valueOf(Color.BLUE), null, null) {
//            {
//                int color = data.getType() == CalculatorButtonData.ButtonType.NUMBER ? R.color.colorPrimary : R.color.operatorColor;
//                setColor({});
//                setStroke(2, Color.BLACK);
//            }
//        };
//
//        int[][] states = new int[][] { new int[] { android.R.attr.state_enabled} };
//        int[] colors = new int[] { Color.BLUE }; // sets the ripple color to blue
//
//        ColorStateList colorStateList = new ColorStateList(states, colors);
//        rippleDrawable.setColor(colorStateList);

        setTextSize(32);
        setTextColor(Color.WHITE);
        setBackground(drawable);
        setLayoutParams(params);
    }
}
