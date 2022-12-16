package com.example.contact.presenters;

import com.example.contact.database.AppDatabase;

public interface BaseMVPView {
    public AppDatabase getContextDatabase();
}
