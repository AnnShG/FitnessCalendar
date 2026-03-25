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


//    @Transaction
//    @Query("SELECT * FROM workouts WHERE workoutId = :workoutId")
//    public WorkoutWithExercises getWorkoutWithExercises(long workoutId);
}
