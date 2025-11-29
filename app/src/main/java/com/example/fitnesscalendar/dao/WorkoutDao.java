package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.entities.Workout;

@Dao
public interface WorkoutDao {

    @Insert
    void insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);
}
