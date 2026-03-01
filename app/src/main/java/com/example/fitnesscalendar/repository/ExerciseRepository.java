package com.example.fitnesscalendar.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.dao.ExerciseDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.Exercise;

import java.util.List;
import java.util.concurrent.Executors;

public class ExerciseRepository {

    private ExerciseDao exerciseDao;
//    private final LiveData<List<Exercise>> allExercises;

    private static final java.util.concurrent.ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public ExerciseRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        exerciseDao = db.exerciseDao();
//        allExercises = exerciseDao.getAllExercises();
    }

//    public LiveData<List<Exercise>> getAllExercises() {
//        return allExercises;
//    }

    public void insert(Exercise exercise) {
        android.util.Log.d("REPO_DEBUG", "Inserting User: " + exercise.getTitle());

        Executors.newSingleThreadExecutor().execute(() -> {
            exerciseDao.insert(exercise);
        });
    }
}
