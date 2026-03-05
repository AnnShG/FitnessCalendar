package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "exercises")
public class Exercise {
    //id, name, description,
    // picture, steps, notes, category, difficulty level, user_created

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    public Long exerciseId;

    @ColumnInfo(name = "media_uri")
    public String mediaUri;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "notes")
    public String notes;

//    @ColumnInfo(name = "difficulty_level")
//    public String difficultyLevel;

    @ColumnInfo(name = "user_created")
    public Boolean userCreated;
}
