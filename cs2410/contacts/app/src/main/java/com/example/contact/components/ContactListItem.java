package com.example.contact.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contact.models.Contact;

public class ContactListItem extends LinearLayout {
    Contact contact;

    public interface OnClickListener {
        public void onClick(int id);
    }

    public ContactListItem(Context context, Contact contact, OnClickListener onClickListener) {
        super(context);
        this.contact = contact;

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        setLayoutParams(params);

        AppCompatTextView text = new AppCompatTextView(context);
        text.setText(contact.name);
        text.setTextSize(30);
        text.setOnClickListener((view) -> {
            onClickListener.onClick(contact.id);
        });

        addView(text);
    }
}
