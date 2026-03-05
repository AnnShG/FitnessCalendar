package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "categories")
public class Category {
    //id, name

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private Long id;

    @ColumnInfo(name = "name")
    public String name;
}
