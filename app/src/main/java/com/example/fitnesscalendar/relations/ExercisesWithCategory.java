package com.example.fitnesscalendar.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;

import java.util.List;

// for READING data

public class ExercisesWithCategory {
    @Embedded
    public Exercise exercise;

    @Relation(
            parentColumn = "exercise_id",
            entityColumn = "category_id",
            associateBy = @Junction(
                    value = ExerciseCategoryCrossRef.class,
                    parentColumn = "exercise_id", // must match name in ExerciseCategoryCrossRef
                    entityColumn = "category_id"  // must match name in ExerciseCategoryCrossRef
            )
    )
    public List<Category> categories;
}
