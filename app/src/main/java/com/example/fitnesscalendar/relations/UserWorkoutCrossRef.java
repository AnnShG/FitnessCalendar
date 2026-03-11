package com.example.fitnesscalendar.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_id", "workout_id"})
public class UserWorkoutCrossRef {
    @ColumnInfo(name = "user_id")
    public long userId; // user id
    @ColumnInfo(name = "workout_id")
    public long workoutId;

}
