package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "exercises")
public class Exercise {
    //id, name, description,
    // picutur, steps, notes, difficulty level, user_created

    @PrimaryKey(autoGenerate = true)
    private Long exerciseId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "picture_path")
    public String picturePath;

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "difficulty_level")
    public String difficultyLevel;

    @ColumnInfo(name = "user_created")
    public Boolean userCreated;
}
