package com.example.contact.components;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HorizontalRuler extends View {

    public HorizontalRuler(Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
        params.setMargins(10, 0, 10, 0);
        setBackgroundColor(Color.GRAY);
        setLayoutParams(params);
    }
}
