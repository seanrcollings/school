package com.example.contact.presenters;

import com.example.contact.database.AppDatabase;
import com.example.contact.models.Contact;

public class NewContactPresenter {
    MVPView view;
    AppDatabase database;
    public interface MVPView extends BaseMVPView {
        public void goBackToContactsPage(Contact contact);
    }

    public NewContactPresenter(MVPView view) {
        this.view = view;
        this.database = view.getContextDatabase();
    }

    public void saveContact(String name, String number, String email) {
        new Thread(() -> {
            Contact contact = new Contact();
            contact.name = name;
            contact.number = number;
            contact.email = email;
            contact.id = (int)database.getContactDao().create(contact);
            view.goBackToContactsPage(contact);
        }).start();
    }
}
