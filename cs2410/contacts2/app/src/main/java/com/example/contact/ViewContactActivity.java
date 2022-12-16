package com.example.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.contact.components.ContactInfo;
import com.example.contact.components.ImageDisplayer;
import com.example.contact.components.MaterialLabel;
import com.example.contact.models.Contact;
import com.example.contact.presenters.ViewContactPresenter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ViewContactActivity extends BaseActivity implements ViewContactPresenter.MVPView {
    ViewContactPresenter presenter;
    LinearLayout mainLayout;

    ImageDisplayer imageDisplayer;
    ContactInfo infoView;
    Contact contact;

    private final int EDIT_CONTACT = 1;
    private final int REQUEST_PHONE_PERMISSIONS = 2;
    private final int REQUEST_SMS_PERMISSIONS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int contactId = getIntent().getIntExtra("id", 1);
        presenter = new ViewContactPresenter(this, contactId);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayout.setLayoutParams(params);

        imageDisplayer = new ImageDisplayer(this, (item) -> {
            if (item.getItemId() == 1) {
                presenter.editContact();
            } else if (item.getItemId() == 2) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Delete This Contact?")
                        .setMessage("Deleting a Contact is permanent, are you sure?")
                        .setNegativeButton("Cancel", (dialog, which) -> {})
                        .setPositiveButton("Delete", (dialog, which) -> {
                            presenter.deleteContact();
                        })
                        .show();
            }
            return true;
        });

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800);
        imageDisplayer.setLayoutParams(imageParams);

        infoView = new ContactInfo(this);

        LinearLayout detailLayout = new LinearLayout(this);
        detailLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams detailParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        detailParams.setMargins(30, 30, 30, 30);
        detailLayout.setLayoutParams(detailParams);

        mainLayout.addView(imageDisplayer);
        mainLayout.addView(infoView);

        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        this.contact = contact;
        runOnUiThread(() -> {
            imageDisplayer.setImageUri(contact.pictureUri);
            imageDisplayer.setText(contact.name);

            infoView.setNumber(contact.number);
            infoView.setPhoneCallAction((view) -> presenter.handlePhoneCallPress(contact.number));
            infoView.setSMSAction((view) -> presenter.handleTextMessagePress(contact.number));

            infoView.setEmail(contact.email);
            infoView.setEmailAction((view) -> presenter.handleEmailPress(contact.email));
        });
    }

    @Override
    public void goToEditContactPage(int id) {
        Intent intent = new Intent(this, CreateOrEditContactActivity.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, EDIT_CONTACT);
    }

    @Override
    public void goBackAfterDelete(int id) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(ContactActivity.DELETE_CONTACT, intent);
        finish();
    }

    @Override
    public void makePhoneCall(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(callIntent);
        } else {
            requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSIONS);
        }
    }

    @Override
    public void sendTextMessage(String number) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent textIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));
            startActivity(textIntent);
        } else {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSIONS);
        }
    }

    @Override
    public void sendEmail(String email) {
        Intent textIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
        startActivity(textIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CONTACT && resultCode == Activity.RESULT_OK) {
            Contact updatedContact = (Contact) data.getSerializableExtra("result");
            presenter.handleContactUpdate(updatedContact);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handlePhoneCallPress(contact.number);
            } else {
                Toast.makeText(
                        this,
                        "You must give permissions to make a phone call",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

        else if (requestCode == REQUEST_SMS_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handleTextMessagePress(contact.number);
            } else {
                Toast.makeText(
                        this,
                        "You must give permissions to send an SMS",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
