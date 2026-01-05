package com.example.fitnesscalendar.entities;

import androidx.room.Entity;

@Entity(primaryKeys = {"id", "workoutId"})
public class UserWorkoutCrossRef {
    public long id; // user id
    public long workoutId;

}
