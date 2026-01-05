package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Activity;
import com.example.fitnesscalendar.entities.User;

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activities);

    @Update
    void update(Activity activities);

    @Delete
    void delete(Activity activities);
}
