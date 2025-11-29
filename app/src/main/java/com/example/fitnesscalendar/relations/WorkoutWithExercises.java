package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.entities.WorkoutExerciseCrossRef;

import java.util.List;

// query the entities
// fetch a workout with its exercises
// Room load many-to-many relations (rows)
public class WorkoutWithExercises {
    @Embedded
    public Workout workout;

    @Relation(
            parentColumn = "workoutId", // workout id PK column, the name should match in the entity
            entityColumn = "exerciseId", // FK column inside join table
            associateBy = @Junction(
                    value = WorkoutExerciseCrossRef.class,
                    parentColumn = "workoutId",
                    entityColumn = "exerciseId")
    )
    public List<Exercise> exercises;
}
