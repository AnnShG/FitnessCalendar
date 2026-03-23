package com.example.fitnesscalendar.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.dao.WorkoutDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.WorkoutExerciseCrossRef;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Combines multiple DAO calls if needed - database operations
// Provides a single interface to the ViewModel
// Can implement caching, mapping, or complex queries
// Repository hides the database complexity
public class WorkoutRepository {

    private final WorkoutDao workoutDao;

    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public WorkoutRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        workoutDao = db.workoutDao();
    }

    public void insertFullWorkout(Workout workout, List<Long> exerciseIds) {
        databaseExecutor.execute(() -> { // all DB operations run on bg thread
            long newWorkoutId = workoutDao.insert(workout);

            if (exerciseIds != null) {
                for (Long exId : exerciseIds) {
                    WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                    crossRef.workoutId = newWorkoutId; // new id push to cross ref bridge table
                    crossRef.exerciseId = exId; // exId that we took from the list push to cross ref

                    workoutDao.insertWorkoutExerciseCrossRef(crossRef);
                }
            }
        });
    }

    public LiveData<List<FullWorkoutRecord>> getFullWorkoutRecords(long userId) {
        return workoutDao.getFullWorkoutRecords(userId);
    }

    public LiveData<FullWorkoutRecord> getFullWorkoutById(long id) {
        return workoutDao.getFullWorkoutById(id);
    }

    public void updateFullWorkout(Workout workout, List<Long> existingExercisesIds) {
        databaseExecutor.execute(() -> {
            // update the main workout entity (without exercises)
            workoutDao.update(workout);

            // clear out the old exercise associations in the bridge table
            workoutDao.deleteExercisesForWorkout(workout.getWorkoutId());

            // Re-insert the current list of exercises
            if (existingExercisesIds != null) {
                for (Long exId : existingExercisesIds) {
                    WorkoutExerciseCrossRef crossRef = new WorkoutExerciseCrossRef();
                    crossRef.workoutId = workout.getWorkoutId();
                    crossRef.exerciseId = exId;
                    workoutDao.insertWorkoutExerciseCrossRef(crossRef);
                }
            }
        });
    }

    public void deleteWorkout(Workout workout) {
        databaseExecutor.execute(() -> {
            workoutDao.delete(workout); // clean workout table
        });
    }

}
