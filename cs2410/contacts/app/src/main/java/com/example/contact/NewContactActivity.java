package com.example.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.example.contact.components.LabelInput;
import com.example.contact.models.Contact;
import com.example.contact.presenters.NewContactPresenter;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    NewContactPresenter presenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NewContactPresenter(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        LabelInput nameInput = new LabelInput(this, "Name");
        LabelInput numberInput = new LabelInput(this, "Phone Number");
        LabelInput emailInput = new LabelInput(this, "Email");

        AppCompatButton saveButton = new AppCompatButton(this);
        saveButton.setOnClickListener(view -> {
            presenter.saveContact(
                    nameInput.getText().toString(),
                    numberInput.getText().toString(),
                    emailInput.getText().toString()
            );
        });
        saveButton.setText(R.string.save);

        mainLayout.addView(nameInput);
        mainLayout.addView(numberInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(saveButton);

        setContentView(mainLayout);
    }

    @Override
    public void goBackToContactsPage(Contact contact) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", contact);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
