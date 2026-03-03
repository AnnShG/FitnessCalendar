package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;

import java.util.List;

public class ExerciseWithSteps {
    @Embedded
    public Exercise Exercise;

    @Relation(
            parentColumn = "id",
            entityColumn = "exercise_id"
    )
    public List<Step> steps;

}
