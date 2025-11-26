package com.example.fitnesscalendar.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Long id;
}
