package com.example.contact.components;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.Layout;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.example.contact.R;
import com.example.contact.models.Contact;
import com.google.android.material.card.MaterialCardView;

public class ContactListItem extends LinearLayout {
    public Contact contact;

    public interface OnClickListener {
        public void onClick(int id);
    }

    public ContactListItem(Context context, Contact contact, OnClickListener onClickListener) {
        super(context);
        this.contact = contact;
        setTag(contact.id);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        setLayoutParams(params);
        setOrientation(HORIZONTAL);

        setOnClickListener((view) -> {
            onClickListener.onClick(contact.id);
        });

        MaterialCardView imageView = new MaterialCardView(context);
        LayoutParams cardParams = new LayoutParams(150, 150);
        imageView.setLayoutParams(cardParams);
        imageView.setRadius(75f);

        LayoutParams internalParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internalParams.gravity = Gravity.CENTER;

        AppCompatImageView image = new AppCompatImageView(context);
        imageView.addView(image);

        if (contact.pictureUri.equals("")) {
            image.setBackgroundDrawable(
                    new TextDrawable(
                            contact.name.substring(0, 1),
                            getResources().getColor(R.color.colorDarkBackground, null)
                    )
            );
        } else {
            image.setImageURI(Uri.parse(contact.pictureUri));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        AppCompatTextView text = new AppCompatTextView(context);
        text.setText(contact.name);
        text.setTextSize(30);
        LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.CENTER;
        textParams.setMargins(10, 0, 0, 0);
        text.setLayoutParams(textParams);


        addView(imageView);
        addView(text);
    }
}
