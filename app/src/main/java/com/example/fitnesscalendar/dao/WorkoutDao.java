package com.example.fitnesscalendar.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.WorkoutExerciseCrossRef;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Query("DELETE FROM workout_exercise_cross_ref WHERE workout_id = :workoutId")
    void deleteExercisesForWorkout(long workoutId);

    @Delete
    void delete(Workout workout);

    // If you don't have CASCADE set in your Entity, you must also delete the links:
    @Query("DELETE FROM workout_exercise_cross_ref WHERE workout_id = :id")
    void deleteExerciseLinks(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExerciseCrossRef(WorkoutExerciseCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM workouts WHERE owner_id IS NULL OR owner_id = :userId")
    LiveData<List<FullWorkoutRecord>> getFullWorkoutRecords(long userId);

    @Transaction
    @Query("SELECT * FROM workouts WHERE workout_id = :workoutId")
    LiveData<FullWorkoutRecord> getFullWorkoutById(long workoutId);



//    @Transaction
//    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
//    public WorkoutWithExercises getWorkoutWithExercises(long workoutId);
}
