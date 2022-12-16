package com.example.tree.views;

import android.content.Context;
import android.text.Editable;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.tree.InputData;

public class LabelInput extends LinearLayout {
    private String label;
    private AppCompatEditText input;

    public LabelInput(Context context, InputData data) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.label = data.getSnakeLabel();

        AppCompatTextView labelView = new AppCompatTextView(context);
        labelView.setText(data.getLabel());
        labelView.setTextSize(20);

        AppCompatEditText editView = new AppCompatEditText(context);
        editView.setText(data.getDefaultData());
        input = editView;

        addView(labelView);
        addView(editView);
    }

    public String getLabel() {
        return label;
    }

    public Editable getText() {
        return input.getText();
    }
}
