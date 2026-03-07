package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Workout;

import java.util.List;

public class FullWorkoutRecord {

    @Embedded
    public Workout workout;

    @Relation(
            parentColumn = "workout_id",
            entityColumn = "exercise_id",
            associateBy = @Junction(WorkoutExerciseCrossRef.class)
    )
    public List<Exercise> exercises;
}
