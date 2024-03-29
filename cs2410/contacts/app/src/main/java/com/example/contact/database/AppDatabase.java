package com.example.contact.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.contact.models.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao getContactDao();
}
