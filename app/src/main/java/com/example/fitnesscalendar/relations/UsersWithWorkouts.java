package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.entities.UserWorkoutCrossRef;
import com.example.fitnesscalendar.entities.Workout;

import java.util.List;

public class UsersWithWorkouts {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "workoutId",
            associateBy = @Junction(
                    value = UserWorkoutCrossRef.class,
                    parentColumn = "id",
                    entityColumn = "workoutId")
    )
    public List<Workout> workouts;
}
