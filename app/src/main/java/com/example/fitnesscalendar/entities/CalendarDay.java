package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

@Data
@Entity(tableName = "calendar_days")
public class CalendarDay {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "completed")
    public boolean completed;

}
