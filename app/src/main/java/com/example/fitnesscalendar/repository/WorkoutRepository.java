package com.example.fitnesscalendar.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.dao.WorkoutDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.WorkoutExerciseCrossRef;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutRepository {

    private final WorkoutDao workoutDao;
    private final LiveData<List<FullWorkoutRecord>> allFullWorkoutRecords;

    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public WorkoutRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        workoutDao = db.workoutDao();
        allFullWorkoutRecords = workoutDao.getFullWorkoutRecords();
    }

    public void insertFullWorkout(Workout workout, List<Long> exerciseIds) {
        databaseExecutor.execute(() -> {
            long newWorkoutId = workoutDao.insert(workout);

            if (exerciseIds != null) {
                for (Long exId : exerciseIds) {
                    WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                    crossRef.workoutId = newWorkoutId; // new id push to cross ref bridge table
                    crossRef.exerciseId = exId; // exId that we took from the list push to cross ref

                    workoutDao.insertExerciseCrossRef(crossRef);
                }
            }
        });
    }
}
