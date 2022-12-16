package com.example.contact.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.contact.R;
import com.google.android.material.card.MaterialCardView;

public class ContactInfo extends MaterialCardView {
    LinearLayout mainLayout;
    MaterialLabel number;
    MaterialLabel email;

    public ContactInfo(Context context) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);

        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        number = new MaterialLabel(context, R.drawable.ic_baseline_local_phone_24, R.drawable.ic_baseline_textsms_24);
        email = new MaterialLabel(context, R.drawable.ic_baseline_email_24, -1);

        mainLayout.addView(number);
        mainLayout.addView(new HorizontalRuler(context));
        mainLayout.addView(email);

        addView(mainLayout);
    }

    public void setEmail(String email) {this.email.setText(email);}

    public void setNumber(String number) {this.number.setText(number);}

    public void setPhoneCallAction(OnClickListener onClick) {
        number.setPrimaryActionListener(onClick);
    }

    public void setSMSAction(OnClickListener onClick) {
        number.setSecondaryActionListener(onClick);
    }

    public void setEmailAction(OnClickListener onClick) {
        email.setPrimaryActionListener(onClick);
    }

}
