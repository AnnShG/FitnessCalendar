package com.example.fitnesscalendar.relations;

import androidx.room.ColumnInfo;


/**
 * A Projection class used to hold the results of a database JOIN query.
 * This class combines data from the 'calendar_days' and 'workouts' tables
 */
public class DateColourResult {
    public Long date;
    public Integer colour;

    @ColumnInfo(name = "workout_id") // to ensure the user cannot attach the same workout to the same day twice
    public Long workoutId;
}
