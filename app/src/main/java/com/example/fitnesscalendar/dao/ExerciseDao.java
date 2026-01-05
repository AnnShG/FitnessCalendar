package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

//    @Query("SELECT * FROM exercises")
//    List<Exercise> getAllExercises();
//
//    @Query("SELECT * FROM exercises WHERE id = :exerciseId")
//    Exercise getExerciseById(int exerciseId);
}
