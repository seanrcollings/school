package com.example.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.contact.database.AppDatabase;
import com.example.contact.presenters.BaseMVPView;

public class BaseActivity extends AppCompatActivity implements BaseMVPView {
    @Override
    public AppDatabase getContextDatabase() {
        return Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "contacts").build();
    }
}
