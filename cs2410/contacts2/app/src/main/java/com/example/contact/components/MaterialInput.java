package com.example.contact.components;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import com.example.contact.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class MaterialInput extends TextInputLayout {
    TextInputEditText input;

    public MaterialInput(@NonNull Context context, String hint, int icon) {
        super(context, null, R.attr.textInputStyle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setStartIconDrawable(icon);

        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);

        setErrorEnabled(true);

        input = new TextInputEditText(getContext());
        input.setHint(hint);
        input.setGravity(Gravity.START);

        addView(input);

    }

    public Editable getText() {
        return input.getText();
    }

    public void setText(String text) { input.setText(text);}
}
