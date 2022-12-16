package com.example.contact;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.contact.components.ContactListItem;
import com.example.contact.models.Contact;
import com.example.contact.presenters.ContactsPresenter;

import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ContactActivity extends BaseActivity implements ContactsPresenter.MVPView {
    FrameLayout mainLayout;
    LinearLayout contactLayout;
    ContactsPresenter presenter;
    ArrayList<ContactListItem> contactListItems = new ArrayList<>();
    private final int CREATE_NEW_CONTACT = 1;
    private int VIEW_CONTACT = 2;
    public static final int DELETE_CONTACT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ContactsPresenter(this);

        mainLayout = new FrameLayout(this);

        contactLayout = new LinearLayout(this);
        contactLayout.setOrientation(LinearLayout.VERTICAL);

        FloatingActionButton button = new FloatingActionButton(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 48, 48);
        params.gravity = (Gravity.BOTTOM | Gravity.RIGHT);
        button.setLayoutParams(params);
        button.setOnClickListener(view -> {
            presenter.handleCreateNewContactPress();
        });
        button.setImageResource(R.drawable.ic_baseline_add_24);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(contactLayout);

        mainLayout.addView(button);
        mainLayout.addView(scrollView);

        setContentView(mainLayout);
    }

    @Override
    public void goToNewContactsPage() {
        Intent intent = new Intent(this, CreateOrEditContactActivity.class);
        startActivityForResult(intent, CREATE_NEW_CONTACT);
    }

    @Override
    public void viewContact(int id) {
        Intent intent = new Intent(this, ViewContactActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, VIEW_CONTACT);
    }

    @Override
    public void removeContact(int id) {
        ContactListItem item = contactLayout.findViewWithTag(id);
        if (item != null) contactLayout.removeView(item);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            ContactListItem listItem = new ContactListItem(this, contact, (id) -> {
                presenter.handleViewContactPress(id);
            });

            contactListItems.add(listItem);
            contactLayout.addView(listItem);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_NEW_CONTACT && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Contact newContact = (Contact) data.getSerializableExtra("result");
            presenter.handleNewCreatedContact(newContact);
        } else if (requestCode == VIEW_CONTACT && resultCode == DELETE_CONTACT) {
            presenter.handleDeletedContact(data.getIntExtra("id", -1));
        }
    }
}