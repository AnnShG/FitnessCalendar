package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "activities")
public class Activity {
    //id, date, completed

    @PrimaryKey(autoGenerate = true)
    private Long id;

//    @ColumnInfo(name = "date")
//    public Date date;

    @ColumnInfo(name = "completed")
    public Boolean completed;

}
