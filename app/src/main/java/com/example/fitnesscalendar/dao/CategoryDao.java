package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Category;

@Dao
public interface CategoryDao {
    @Insert
    void insert(Category categories);

    @Update
    void update(Category categories);

    @Delete
    void delete(Category categories);
}
