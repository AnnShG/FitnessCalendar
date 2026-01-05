package com.example.fitnesscalendar.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"workoutId", "exerciseId"},
        indices = {@Index("exerciseId")} )// improves query performance)
public class WorkoutExerciseCrossRef {
    public long workoutId; // FK
    public long exerciseId; // FK
}
