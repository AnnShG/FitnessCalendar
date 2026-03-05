package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Goal;
import com.example.fitnesscalendar.entities.User;

import java.util.List;

public class UserWithGoals {
    @Embedded
    public User user;

    @Relation(
            parentColumn = "user_id", // PK in user entity
            entityColumn = "user_id" // FK in goal entity
    )
    public List<Goal> goals;
}
