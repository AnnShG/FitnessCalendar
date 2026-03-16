package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // required by Room
@AllArgsConstructor // for constructor with 2 fields
@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    public Long id;

    @ColumnInfo(name = "name")
    public String name;
}
