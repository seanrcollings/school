package com.example.contact.models;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class Contact implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "number")
    public String number;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "picture_uri")
    public String pictureUri;

    @Ignore
    public HashMap<String, String> errors = new HashMap<>();

    public boolean validate() {
        errors.clear();
        validateName();
        validateEmail();
        validateNumber();
        return errors.size() == 0;
    }

    private void validateName() {
        if (name.equals("")) {
            errors.put("name", "Name cannot be blank");
        }
    }

    private void validateEmail() {
        if (email.equals("")) return;
        // https://stackoverflow.com/questions/8204680/java-regex-email
        Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                                              Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL.matcher(email);
        if (!matcher.find()) {
            errors.put("email", "Not a valid Email");
        }
    }

    private void validateNumber() {
        if (number.equals("")) {
            errors.put("number", "Number cannot be blank");
            return;
        }

        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            errors.put("number", "Phone number can only contain numbers");
        }
    }
}
