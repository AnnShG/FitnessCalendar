package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.Data;

// step is a child of Exercise
@Data
@Entity(
        tableName = "steps",
        foreignKeys = @ForeignKey(
                entity = Exercise.class, // parent entity
                parentColumns = "exerciseId",
                childColumns = "exercise_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("exercise_id")} // for each set of steps its own exercise
)
public class Step {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "exercise_id")
    public long exerciseId; // foreign key to Exercise

    @ColumnInfo(name = "step_number")
    public int stepNumber;

    @ColumnInfo(name = "description")
    public String description;
}
