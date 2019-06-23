package com.example.sololearn_newsfeed.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DBWrapper {

    private static AppDatabase instance = null;

    public static void create(Context context) {

        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, AppDatabase.class, "database").build();
                }
            }
        }
    }

    public static AppDatabase getDatabase() {
        if (instance == null) {
            throw new IllegalStateException("Database not created");
        }
        return instance;
    }
}
