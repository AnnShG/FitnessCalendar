package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "workouts")
public class Workout {
    //ig, name, description, notes, user_created

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    public Long workoutId;

    @ColumnInfo(name = "owner_id")
    public Long ownerId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "notes")
    public String note;

    @ColumnInfo(name = "user_created")
    public Boolean userCreated;
}
