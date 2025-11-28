package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "quotes")
public class Quote {
    // id, text, author
@PrimaryKey(autoGenerate = true)
    private Long id;

@ColumnInfo(name = "quote_text")
    private String text;

@ColumnInfo(name = "author")
    private String author;
}
