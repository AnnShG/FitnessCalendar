package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.entities.Workout;

import java.util.List;

public class DayWithWorkouts {
    @Embedded
    public CalendarDay day;

    @Relation(
            parentColumn = "id",
            entityColumn = "workoutId",
            associateBy = @Junction(CalendarDayWorkoutCrossRef.class)    )
    public List<Workout> workouts;
}