package com.example.fitnesscalendar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.entities.User;

@Database(entities= {User.class}, version = 1) // array list with users
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE; // singleton instance

    public abstract UserDao userDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitnes" +
                                    "s_calendar.db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

}
