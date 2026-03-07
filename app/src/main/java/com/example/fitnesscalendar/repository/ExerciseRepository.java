package com.example.fitnesscalendar.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.dao.ExerciseDao;
import com.example.fitnesscalendar.dao.StepDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.relations.ExerciseCategoryCrossRef;
import com.example.fitnesscalendar.relations.ExerciseSummary;
import com.example.fitnesscalendar.relations.FullExerciseRecord;

import java.util.List;
import java.util.concurrent.Executors;

import lombok.Getter;

public class ExerciseRepository {

    private final ExerciseDao exerciseDao;
    private final StepDao stepDao;

    // full record path for the single exercise detail screen
    @Getter // get  allFullExerciseRecords
    private final LiveData<List<FullExerciseRecord>> allFullExerciseRecords;
    private final LiveData<List<ExerciseSummary>> allExerciseSummaries;

    // runs DB on background thread, because Android doesn't allow it to run on main thread
    private static final java.util.concurrent.ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public ExerciseRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app); // Give me the database instance

        // retrieving DAOs for exercises and steps, now the repo can talk to DB
        exerciseDao = db.exerciseDao();
        stepDao = db.stepDao();
        // Initialize the read path
        allFullExerciseRecords = exerciseDao.getFullExerciseRecords();
        allExerciseSummaries = exerciseDao.getExerciseSummaries();
    }

    public LiveData<List<ExerciseSummary>> getExerciseSummaries() {
        return allExerciseSummaries;
    }

    // this method inserts steps and categories inside the exercise (WRITING into DB) in the bg to not freeze the UI
    public void insertFullExercise(Exercise exercise, List<Step> steps, List<Long> categoryIds) {
        // use the fixed thread pool defined at the top
        databaseExecutor.execute(() -> {

            // 1. Insert the parent Exercise entity.
            long newExerciseId = exerciseDao.insert(exercise); //auto-generated id (15) by Room

            // 2. Insert child 'Step' entity (1:M Relationship).
            if (steps != null) {
                // For each element inside the collection steps, take one element and call it step (var)
                // (int i = 0; i < steps.size(); i++)     Step step = steps.get(i);
                for (Step step : steps) {
                    // assign the parent's generated ID to the child's FK field to link them in the database.
                    step.setExerciseId(newExerciseId);
                    // Persist the step to the 'steps' table via its specific DAO.
                    stepDao.insert(step);
                }
            }

            // 3. Process Category associations (M:M Relationship).
            if (categoryIds != null) {
                // For each category ID inside the list categoryIds ([3, 5, 6]), call it catId.
                for (Long catId : categoryIds) {
                    // Create an instance (row) of the Join Table (Bridge) entity to save there rows
                    ExerciseCategoryCrossRef crossRef = new ExerciseCategoryCrossRef();
                    // Link the parent Exercise ID with the existing Category ID
                    // Every row is saved inside crossRef (bridge) table with each next loop
                    crossRef.exerciseId = newExerciseId; // newId (15) assign to the id from bridge table
                    crossRef.categoryId = catId; // 3 (from the list) is assigned to categoryId, then 4

                    // Persist the relationship to the cross-reference table.
                    exerciseDao.insertCategoryCrossRef(crossRef);
                }
            }
        });
    }
}
