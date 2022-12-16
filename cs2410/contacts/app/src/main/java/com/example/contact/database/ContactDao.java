package com.example.contact.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contact.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    Contact findById(int id);

    @Insert
    long create(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
