package com.example.fitnesscalendar.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.WorkoutExerciseCrossRef;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    long insert(Workout workout);

//    INSERT INTO workout_exercise_cross_ref (workout_id, exercise_id) VALUES (?, ?)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWorkoutExerciseCrossRef(WorkoutExerciseCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM workouts WHERE owner_id IS NULL OR owner_id = :userId")
    LiveData<List<FullWorkoutRecord>> getFullWorkoutRecords(long userId);

    @Transaction
    @Query("SELECT * FROM workouts WHERE workout_id = :workoutId")
    LiveData<FullWorkoutRecord> getFullWorkoutById(long workoutId);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout_exercise_cross_ref WHERE workout_id = :workoutId")
    void deleteExercisesForWorkout(long workoutId);

    @Query("DELETE FROM calendar_day_workout_cross_ref " +
            "WHERE workout_id = :workoutId " +
            "AND calendar_day_id IN (SELECT calendar_day_id FROM calendar_days WHERE user_id = :userId) " +
            "AND is_completed = 0")
    void deleteOnlyPlannedWorkoutsFromCalendar(long userId, long workoutId);

    @Transaction
    @Query("SELECT DISTINCT w.* FROM workouts w " +
            "INNER JOIN workout_exercise_cross_ref we ON w.workout_id = we.workout_id " +
            "INNER JOIN exercise_category_cross_ref ec ON we.exercise_id = ec.exercise_id " +
            "WHERE w.owner_id = :userId " +
            "AND (:searchQuery IS NULL OR w.title LIKE '%' || :searchQuery || '%') " +
            "AND ec.category_id IN (:categoryIds)")
    LiveData<List<FullWorkoutRecord>> getWorkoutsFiltered(long userId, List<Long> categoryIds, String searchQuery);

    @Transaction
    @Query("SELECT * FROM workouts WHERE owner_id = :userId " +
            "AND (:searchQuery IS NULL OR title LIKE '%' || :searchQuery || '%')")
    LiveData<List<FullWorkoutRecord>> getWorkoutsBySearchOnly(long userId, String searchQuery);

    @Query("SELECT * FROM categories ORDER BY category_group ASC")
    LiveData<List<Category>> getAllCategories();
}
