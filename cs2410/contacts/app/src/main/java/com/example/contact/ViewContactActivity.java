package com.example.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import com.example.contact.models.Contact;
import com.example.contact.presenters.ViewContactPresenter;

public class ViewContactActivity extends BaseActivity implements ViewContactPresenter.MVPView {
    ViewContactPresenter presenter;
    LinearLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contactId = getIntent().getIntExtra("id", 1);
        presenter = new ViewContactPresenter(this, contactId);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 30, 30, 30);
        mainLayout.setLayoutParams(params);

        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        AppCompatTextView name = new AppCompatTextView(this);
        name.setText(contact.name);
        name.setTextSize(60);
        name.setGravity(Gravity.CENTER);

        AppCompatTextView number = new AppCompatTextView(this);
        number.setText(String.format("%s: %s", getString(R.string.call), contact.number));
        number.setTextSize(30);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.number));
        number.setOnClickListener((view) -> {
            startActivity(callIntent);
        });


        AppCompatTextView email = new AppCompatTextView(this);
        email.setText(String.format("%s: %s", getString(R.string.email), contact.email));
        email.setTextSize(30);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.email));
        email.setOnClickListener((view) -> {
            startActivity(emailIntent);
        });

        mainLayout.addView(name);
        mainLayout.addView(number);
        mainLayout.addView(email);
    }
}
