package com.example.contact.presenters;

import android.content.Intent;

import com.example.contact.database.AppDatabase;
import com.example.contact.models.Contact;

public class ViewContactPresenter {
    MVPView view;
    AppDatabase database;
    public interface MVPView extends BaseMVPView {
        public void renderContact(Contact contact);
    }

    public ViewContactPresenter(MVPView view, int contactId) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContact(contactId);
    }

    public void loadContact(int id) {
        new Thread(() -> {
            Contact contact = view.getContextDatabase().getContactDao().findById(id);
            view.renderContact(contact);
        }).start();
    }
}
