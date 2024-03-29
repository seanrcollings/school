package com.example.contact.presenters;

import com.example.contact.database.AppDatabase;
import com.example.contact.models.Contact;

import java.util.ArrayList;

public class ContactsPresenter {
    private MVPView view;
    private AppDatabase database;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public interface MVPView extends BaseMVPView {
        void goToNewContactsPage();
        void renderContact(Contact contact);
        void viewContact(int id);
    }

    public ContactsPresenter(MVPView view) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContacts();
    }

    public void loadContacts() {
        new Thread(() -> {
            contacts = (ArrayList<Contact>)database.getContactDao().getAll();
            contacts.forEach(contact -> {
                view.renderContact(contact);
            });
        }).start();
    }

    public void handleCreateNewContactPress() {
        new Thread(() -> {
            view.goToNewContactsPage();
        }).start();
    }

    public void handleViewContactPress(int id) {
        new Thread(() -> {
            view.viewContact(id);
        }).start();
    }

    public void handleNewCreatedContact(Contact newContact) {
        contacts.add(newContact);
        view.renderContact(newContact);
    }
}
