package com.example.contact.components;

import android.content.Context;
import android.text.Editable;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;


public class LabelInput extends LinearLayout {
    private AppCompatEditText input;

    public LabelInput(Context context, String label) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        AppCompatTextView labelView = new AppCompatTextView(context);
        labelView.setText(label);
        labelView.setTextSize(20);

        AppCompatEditText editView = new AppCompatEditText(context);
        input = editView;

        addView(labelView);
        addView(editView);
    }

    public Editable getText() {
        return input.getText();
    }
}
