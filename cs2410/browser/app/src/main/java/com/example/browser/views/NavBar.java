package com.example.browser.views;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.browser.History;

public class NavBar extends LinearLayout {
    public NavBar(Context context, final History history) {
        super(context);

        // URL BAR
        final AppCompatEditText urlBar = new AppCompatEditText(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                10
        );
        urlBar.setLayoutParams(params);
        urlBar.setHint("Type in a URL");
        urlBar.setText(history.getCurrent());
        urlBar.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        LayoutParams buttonParams = new LayoutParams(150, 150 );
        // SEARCH BUTTON
        AppCompatButton urlButton = new AppCompatButton(context);
        urlButton.setText("G");
        urlButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlBar.getText().toString();
                history.addToHistory(url);
            }
        });
        urlButton.setLayoutParams(buttonParams);


        // HISTORY BUTTONS
        AppCompatButton backButton = new AppCompatButton(context);
        backButton.setText("B");
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                history.moveBack();
                urlBar.setText(history.getCurrent());
            }
        });
        backButton.setLayoutParams(buttonParams);


        AppCompatButton forwardButton = new AppCompatButton(context);
        forwardButton.setText("F");
        forwardButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                history.moveForward();
                urlBar.setText(history.getCurrent());
            }
        });
        forwardButton.setLayoutParams(buttonParams);

        addView(backButton);
        addView(forwardButton);
        addView(urlBar);
        addView(urlButton);


    }
}
