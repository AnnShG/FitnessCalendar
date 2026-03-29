package com.example.fitnesscalendar.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;

@Data
@Entity(tableName = "calendar_days",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE))
public class CalendarDay {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "calendar_day_id")
    public long calendarDayId;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "completed")
    public boolean completed;

}
