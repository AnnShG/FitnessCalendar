package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.entities.Workout;

import java.util.List;

public class UserWithWorkouts {
    @Embedded
    public User user;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "workout_id",
            associateBy = @Junction(
                    value = UserWorkoutCrossRef.class,
                    parentColumn = "user_id",
                    entityColumn = "workout_id")
    )
    public List<Workout> workouts;
}
