package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;

import java.util.List;

// for READING
public class ExerciseWithSteps {
    @Embedded //Tells Room to take all fields from the Exercise entity and put them into this object
    public Exercise Exercise; // includes all columns from the Exercise table

    @Relation(
            parentColumn = "id", // exercise id PK column
            entityColumn = "exercise_id" // FK inside steps entity
    )
    public List<Step> steps; // Room will automatically find all steps where FK matches PK.

}
