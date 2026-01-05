package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Step;

import java.util.List;

@Dao
public interface StepDao {

    @Insert
    void insert(Step step);

    @Update
    void update(Step step);

    @Delete
    void delete(Step step);

    @Query("SELECT * FROM steps WHERE exercise_id = :exerciseId ORDER BY step_number ASC")
    List<Step> getStepsForExercise(long exerciseId);
}

