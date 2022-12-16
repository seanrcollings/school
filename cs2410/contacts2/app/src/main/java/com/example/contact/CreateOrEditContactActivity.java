package com.example.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.contact.components.ImageSelector;
import com.example.contact.components.MaterialInput;
import com.example.contact.models.Contact;
import com.example.contact.presenters.NewContactPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class CreateOrEditContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    NewContactPresenter presenter;
    private ImageSelector imageSelector;
    private MaterialInput nameInput;
    private MaterialInput numberInput;
    private MaterialInput emailInput;

    private final int PICK_IMAGE = 1;
    private final int TAKE_PICTURE = 2;

    private final int REQUEST_CAMERA_PERMISSIONS = 3;

    private String currentPhotoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);


        imageSelector = new ImageSelector(this);
        imageSelector.setOnClickListener((view) -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Get a Photo")
                    .setItems(new String[] {"From Camera", "From Photos"}, (dialog, which) -> {
                        if (which == 0) {
                            presenter.handleCameraSelected();
                        } else {
                            presenter.handlePhotosSelected();
                        }
                    })
                    .show();
        });

        nameInput = new MaterialInput(this, "Name", R.drawable.ic_baseline_person_24);
        numberInput = new MaterialInput(this, "Phone Number", R.drawable.ic_baseline_local_phone_24);
        emailInput = new MaterialInput(this, "Email", R.drawable.ic_baseline_email_24);

        MaterialButton saveButton = new MaterialButton(this);
        saveButton.setText(R.string.save);
        saveButton.setOnClickListener(view -> {
            presenter.saveContact(
                    nameInput.getText().toString(),
                    numberInput.getText().toString(),
                    emailInput.getText().toString(),
                    imageSelector.getImageUri()
            );
        });

        MaterialButton cancelButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener((view) -> {
            presenter.handleCancelPress();
        });

        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setGravity(Gravity.RIGHT);
        buttonsLayout.setPadding(48, 0, 48, 0);
        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(saveButton);

        mainLayout.addView(imageSelector);
        mainLayout.addView(nameInput);
        mainLayout.addView(numberInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(buttonsLayout);

        int id = getIntent().getIntExtra("id", -1);
        presenter = new NewContactPresenter(this, id);

        setContentView(mainLayout);
    }

    @Override
    public void goBackToPreviousPage(Contact contact) {
        if (contact == null) {
            setResult(Activity.RESULT_CANCELED, null);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", contact);
            setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void goToCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp  + ".jpg";
            File imageFile = new File(getExternalFilesDirs(Environment.DIRECTORY_PICTURES)[0], imageFileName);
            currentPhotoPath = imageFile.getAbsolutePath();
            Uri photoUri = FileProvider.getUriForFile(this, "com.example.contact.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, TAKE_PICTURE);
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS);
        }
    }

    @Override
    public void displayPicture(String pictureUri) {
        imageSelector.setImageUri(pictureUri);
    }

    @Override
    public void renderErrors(HashMap<String, String> errors) {
        runOnUiThread(() -> {
            nameInput.setError(errors.get("name"));
            emailInput.setError(errors.get("email"));
            numberInput.setError(errors.get("number"));
        });
    }

    @Override
    public void fillInContact(Contact contact) {
        runOnUiThread(() -> {
            imageSelector.setImageUri(contact.pictureUri);
            nameInput.setText(contact.name);
            numberInput.setText(contact.number);
            emailInput.setText(contact.email);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE  && resultCode == Activity.RESULT_OK) {
            String pictureURI = data.getData().toString();
            presenter.handlePictureSelected(pictureURI);
        } else if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            presenter.handlePictureSelected(currentPhotoPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.handleCameraSelected();
                } else {
                    Toast.makeText(
                            this,
                            "You must allow the camera to be used to use this feature",
                            Toast.LENGTH_SHORT
                    ).show();
                }
                break;
        }
    }
}
