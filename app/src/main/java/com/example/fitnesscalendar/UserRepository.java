package com.example.fitnesscalendar;

import android.app.Application;

import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.User;

import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        userDao = db.userDao();
    }

    public void insert(User user) {
        Executors.newSingleThreadExecutor().execute(() ->
                userDao.insert(user)
        );
    }
}
