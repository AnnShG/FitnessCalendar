package com.example.fitnesscalendar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.entities.User;

@Database(entities= {User.class}, version = 1) // array list with users
public abstract class AppDatabase extends RoomDatabase {

    // create one database only once - INSTANCE
    private static volatile AppDatabase INSTANCE; // singleton instance

    public abstract UserDao userDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) { // not synced
            synchronized (AppDatabase.class) { // one thread at a time
                if (INSTANCE == null) { // second check if it is synced
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitness_calendar.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}
