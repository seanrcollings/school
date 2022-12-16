package com.example.madlib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    // The story string must possess as many %s as there are elements in fieldNames
    protected String[] fieldNames = {"name", "profession", "adjective", "noun", "verb",  "monster", "location", "food"};
    protected String story = "%s, professional %s, uses their %s %s to %s %s. Then they head to %s to chow down on some %s";

    protected AppCompatEditText createEditText(String text, LinearLayout layout ) {
        AppCompatTextView textView = new AppCompatTextView(this);
        textView.setText(text.toUpperCase());

        AppCompatEditText editText = new AppCompatEditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(params);

        layout.addView(textView);
        layout.addView(editText);

        return editText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


        final AppCompatEditText[] fields = new AppCompatEditText[fieldNames.length];
        int index = 0;
        for(String name: fieldNames) {
            AppCompatEditText field = createEditText(name, layout);
            fields[index] = field;
            index++;
        }

        // Story View
        final AppCompatTextView storyView = new AppCompatTextView(this);

        // Button
        AppCompatButton button = new AppCompatButton(this);
        button.setText("Generate Story");
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String[] fieldStrings = new String[fields.length];

                int index = 0;
                for(AppCompatEditText field: fields) {
                    fieldStrings[index] = field.getText().toString();
                    index++;
                }

                String madlib = String.format(story, fieldStrings);

                storyView.setText(madlib);
                setContentView(storyView);
            }
        });

        layout.addView(button);
        setContentView(layout);

    }
}