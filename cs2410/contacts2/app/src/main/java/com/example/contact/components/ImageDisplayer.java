package com.example.contact.components;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contact.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ImageDisplayer extends FrameLayout {
    AppCompatImageView imageView;
    AppCompatTextView overlayTextView;

    public ImageDisplayer(@NonNull Context context) {
        super(context);
    }

    public ImageDisplayer(@NonNull Context context, PopupMenu.OnMenuItemClickListener popupOnClick) {
        this(context, "", "", popupOnClick);
    }

    public ImageDisplayer(Context context, String imageUri, String overlayText, PopupMenu.OnMenuItemClickListener popupOnClick) {
        super(context);

        setBackgroundColor(getResources().getColor(R.color.colorDarkBackground, null));
        imageView = new AppCompatImageView(context);

        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_person_24);
        } else {
            imageView.setImageURI(Uri.parse(imageUri));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);

        FloatingActionButton actions = new FloatingActionButton(context);
        actions.setImageResource(R.drawable.ic_baseline_more_vert_48);
        FrameLayout.LayoutParams actionsParams = new FrameLayout.LayoutParams(150, 150);
        actionsParams.setMargins(0, 24, 24, 0);
        actionsParams.gravity = (Gravity.END | Gravity.TOP);
        actions.setLayoutParams(actionsParams);

        PopupMenu popupMenu = new PopupMenu(context, actions);
        popupMenu.getMenu().add(0, 1, 1, "Edit Contact");
        popupMenu.getMenu().add(1, 2, 2, "Delete Contact");

        popupMenu.setOnMenuItemClickListener(popupOnClick);

        actions.setOnClickListener((view) ->  {
            popupMenu.show();
        });

        overlayTextView = new AppCompatTextView(context);
        overlayTextView.setText(overlayText);
        overlayTextView.setTextSize(30);
        overlayTextView.setTextColor(Color.WHITE);

        FrameLayout.LayoutParams overlayTextParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        overlayTextParams.setMargins(48, 0, 0, 24);
        overlayTextParams.gravity = (Gravity.START | Gravity.BOTTOM);
        overlayTextView.setLayoutParams(overlayTextParams);

        addView(imageView);
        addView(overlayTextView);
        addView(actions);
    }

    public void setImageUri(String imageUri) {
        imageView.setImageURI(Uri.parse(imageUri));
    }

    public void setText(String text) {
        overlayTextView.setText(text);
    }
}
