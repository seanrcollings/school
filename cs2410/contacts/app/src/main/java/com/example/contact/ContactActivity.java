package com.example.contact;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.contact.components.ContactListItem;
import com.example.contact.models.Contact;
import com.example.contact.presenters.ContactsPresenter;

import java.util.ArrayList;

public class ContactActivity extends BaseActivity implements ContactsPresenter.MVPView {
    LinearLayout mainLayout;
    LinearLayout contactLayout;
    ContactsPresenter contactsPresenter;
    ArrayList<ContactListItem> contactListItem = new ArrayList<>();
    private final int CREATE_NEW_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactsPresenter = new ContactsPresenter(this);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        contactLayout = new LinearLayout(this);
        contactLayout.setOrientation(LinearLayout.VERTICAL);
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(contactLayout);

        AppCompatButton button = new AppCompatButton(this);
        button.setText(R.string.add_contact);
        button.setOnClickListener(view -> {
            contactsPresenter.handleCreateNewContactPress();
        });

        mainLayout.addView(button);
        mainLayout.addView(scrollView);

        setContentView(mainLayout);
    }

    @Override
    public void goToNewContactsPage() {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivityForResult(intent, CREATE_NEW_CONTACT);
    }

    @Override
    public void viewContact(int id) {
        Intent intent = new Intent(this, ViewContactActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            ContactListItem listItem = new ContactListItem(this, contact, (id) -> {
                contactsPresenter.handleViewContactPress(id);
            });
            contactListItem.add(listItem);
            contactLayout.addView(listItem);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NEW_CONTACT && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Contact newContact = (Contact) data.getSerializableExtra("result");
            contactsPresenter.handleNewCreatedContact(newContact);
        }
    }
}