package com.example.fitnesscalendar.relations;

import androidx.room.Entity;

@Entity(tableName = "calendar_day_workout_cross_ref",
        primaryKeys = {"calendarDayId", "workoutId"})
public class CalendarDayWorkoutCrossRef {
    public long calendarDayId;
    public long workoutId;
}
