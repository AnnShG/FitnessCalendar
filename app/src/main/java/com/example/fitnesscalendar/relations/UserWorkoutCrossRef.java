package com.example.fitnesscalendar.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"id", "workoutId"})
public class UserWorkoutCrossRef {
    public long id; // user id
    public long workoutId;

}
