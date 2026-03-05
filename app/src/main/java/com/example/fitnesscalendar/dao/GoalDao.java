package com.example.fitnesscalendar.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Goal;
import com.example.fitnesscalendar.entities.Step;

public interface GoalDao {
    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

}
