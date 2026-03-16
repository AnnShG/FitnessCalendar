package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;

import java.util.List;

public class FullExerciseRecord {
    @Embedded
    public Exercise exercise;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "exercise_id"
    )
    public List<Step> steps;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "category_id",
            associateBy = @Junction(
                    value = ExerciseCategoryCrossRef.class,
                    parentColumn = "exercise_id",
                    entityColumn = "category_id"
            )
    )
    public List<Category> categories;
}
