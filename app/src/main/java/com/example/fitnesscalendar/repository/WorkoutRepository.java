package com.example.fitnesscalendar.repository;

import android.app.Application;

import com.example.fitnesscalendar.dao.WorkoutDao;
import com.example.fitnesscalendar.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutRepository {

    private final WorkoutDao workoutDao;

    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public WorkoutRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        workoutDao = db.workoutDao();
    }

}
