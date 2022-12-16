package com.example.contact.presenters;

import com.example.contact.database.AppDatabase;
import com.example.contact.models.Contact;

import java.util.HashMap;

public class NewContactPresenter {
    MVPView view;
    AppDatabase database;
    Contact contact;

    public enum State {
        CREATE,
        EDIT
    }

    public State currentState;

    public interface MVPView extends BaseMVPView {
        public void goBackToPreviousPage(Contact contact);
        public void goToPhotos();
        public void displayPicture(String pictureUri);
        public void renderErrors(HashMap<String, String> errors);
        public void fillInContact(Contact contact);
        public void goToCamera();
    }

    public NewContactPresenter(MVPView view, int id) {
        this.view = view;
        this.database = view.getContextDatabase();

        currentState = id < 0 ? State.CREATE : State.EDIT;

        if (currentState == State.EDIT) { loadContact(id);}
    }

    public void loadContact(int id) {
        new Thread(() -> {
            contact = this.database.getContactDao().findById(id);
            view.fillInContact(contact);
        }).start();
    }

    public void saveContact(String name, String number, String email, String pictureUri) {
        new Thread(() -> {
            Contact contact = currentState == State.CREATE ? new Contact() : this.contact;
            contact.name = name;
            contact.number = number;
            contact.email = email;
            contact.pictureUri = pictureUri;

            if (contact.validate()) {
                if (currentState == State.CREATE) {
                    contact.id = (int)database.getContactDao().create(contact);
                } else {
                    database.getContactDao().update(contact);
                }
                view.goBackToPreviousPage(contact);
            } else {
                view.renderErrors(contact.errors);
            }

        }).start();
    }

    public void handleCancelPress() {
        view.goBackToPreviousPage(null);
    }

    public void handlePhotosSelected() {
        view.goToPhotos();
    }

    public void handleCameraSelected() {
        view.goToCamera();
    }

    public void handlePictureSelected(String pictureUri) {
        view.displayPicture(pictureUri);
    }
}
