package com.example.fitnesscalendar.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "workout_exercise_cross_ref",
        primaryKeys = {"workout_id", "exercise_id"},
        indices = {@Index("exercise_id")} )// improves query performance

public class WorkoutExerciseCrossRef {
    @ColumnInfo(name = "workout_id")
    public long workoutId; // FK

    @ColumnInfo(name = "exercise_id")
    public long exerciseId; // FK
}
