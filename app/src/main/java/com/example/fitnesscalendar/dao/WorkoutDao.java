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
import com.example.fitnesscalendar.relations.WorkoutWithExercises;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

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
