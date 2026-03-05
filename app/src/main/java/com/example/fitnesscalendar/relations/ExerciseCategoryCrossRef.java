package com.example.fitnesscalendar.relations;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

// bridge - join table

// unique row in this table is the combination of an exercise and category
@Entity(primaryKeys = {"exercise_id", "category_id"}, tableName = "exercise_category_cross_ref")
public class ExerciseCategoryCrossRef {
    @ColumnInfo(name = "exercise_id") // Maps Java field to the primary key name
    public long exerciseId;

    @ColumnInfo(name = "category_id")
    public long categoryId;
}
