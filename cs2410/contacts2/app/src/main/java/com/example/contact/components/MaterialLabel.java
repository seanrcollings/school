package com.example.contact.components;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;


public class MaterialLabel extends LinearLayout {
    private MaterialTextView textView;
    private ImageView rightIconView;
    private ImageView leftIconView;

    public MaterialLabel(Context context) {
        super(context);
    }

    public MaterialLabel(Context context, int leftIcon, int rightIcon) {
        this(context, "", leftIcon, rightIcon);
    }

    public MaterialLabel(Context context, String text, int leftIcon, int rightIcon) {
        super(context);

        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardViewParams.setMargins(48, 48, 48 ,48);
        setLayoutParams(cardViewParams);

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(48, 5, 48 ,5);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(layoutParams);

        textView = new MaterialTextView(context);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.weight = 1;
        textView.setLayoutParams(textParams);
        textView.setText(text);
        textView.setTextSize(27);


        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.setMargins(0, 0, 48 ,0);
        iconParams.gravity = Gravity.CENTER;

        leftIconView = new ImageView(context);
        leftIconView.setImageDrawable(AppCompatResources.getDrawable(getContext(), leftIcon));
        leftIconView.setLayoutParams(iconParams);

        layout.addView(leftIconView);
        layout.addView(textView);

        if (rightIcon > 0) {
            rightIconView = new ImageView(context);
            rightIconView.setImageDrawable(AppCompatResources.getDrawable(getContext(), rightIcon));
            LinearLayout.LayoutParams rightIconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rightIconParams.gravity = (Gravity.CENTER | Gravity.LEFT);
            rightIconView.setLayoutParams(rightIconParams);
            layout.addView(rightIconView);
        }

        addView(layout);
    }

    public void setPrimaryActionListener(OnClickListener listener) {
        leftIconView.setOnClickListener(listener);
    }

    public void setSecondaryActionListener(OnClickListener listener) {
        if (rightIconView == null) return;
        rightIconView.setOnClickListener(listener);
    }

    public String getText() {
        return textView.getText().toString();
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
