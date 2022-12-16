package com.example.contact.presenters;

import com.example.contact.database.AppDatabase;
import com.example.contact.models.Contact;

public class ViewContactPresenter {
    MVPView view;
    AppDatabase database;
    Contact contact;

    public interface MVPView extends BaseMVPView {
        public void renderContact(Contact contact);
        public void goToEditContactPage(int id);

        public void makePhoneCall(String number);
        public void sendTextMessage(String number);
        public void sendEmail(String email);

        void goBackAfterDelete(int id);
    }

    public ViewContactPresenter(MVPView view, int contactId) {
        this.view = view;
        this.database = view.getContextDatabase();
        loadContact(contactId);
    }

    public void loadContact(int id) {
        new Thread(() -> {
            contact = this.database.getContactDao().findById(id);
            view.renderContact(contact);
        }).start();
    }

    public void editContact() {
        view.goToEditContactPage(contact.id);
    }


    public void handleContactUpdate(Contact updatedContact) {
        contact = updatedContact;
        view.renderContact(contact);
    }

    public void deleteContact() {
        new Thread(() -> {
            this.database.getContactDao().delete(contact);
        }).start();
        view.goBackAfterDelete(contact.id);
    }


    public void handlePhoneCallPress(String number) {
        view.makePhoneCall(number);
    }

    public void handleTextMessagePress(String number) {
        view.sendTextMessage(number);
    }

    public void handleEmailPress(String email) {
        view.sendEmail(email);
    }
}
