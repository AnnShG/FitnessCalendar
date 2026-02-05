package com.example.fitnesscalendar;

import android.app.Application;

import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.User;

import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao userDao;
    // Define a single executor for the whole app
    private static final java.util.concurrent.ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public UserRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        userDao = db.userDao();
    }

    public void insert(User user) {
        // Add this line to debug:
        android.util.Log.d("REPO_DEBUG", "Inserting User: " + user.getName() + ", Goal: " + user.getGoals());

        databaseExecutor.execute(() -> userDao.insert(user));
//        Executors.newSingleThreadExecutor().execute(() ->
//                userDao.insert(user)
//        );
    }
}
