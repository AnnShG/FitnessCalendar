package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;

import com.example.fitnesscalendar.entities.Exercise;

// For representing the user the list of the exercises with its title and picture
public class ExerciseSummary {
    @Embedded
    public Exercise exercise;
}
