package com.example.fitnesscalendar.entities;

import androidx.room.PrimaryKey;

public class Goal {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
}
