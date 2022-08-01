package com.snappretail.trackingapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Databasesample.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    public abstract data_interface task_interface();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "sample_database").allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}