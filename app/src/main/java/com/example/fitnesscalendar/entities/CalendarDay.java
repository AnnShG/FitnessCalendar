package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "calendar_days")
public class CalendarDay {
    @PrimaryKey(autoGenerate = true)
    private long id;

//    @ColumnInfo(name = "date")
//    private Date date;

    @ColumnInfo(name = "completed")
    private boolean completed;

}
