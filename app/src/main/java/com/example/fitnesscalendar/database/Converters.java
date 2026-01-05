package com.example.fitnesscalendar.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimeStamp(Long value) { // from Date to Long
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) { // from Long to Date
        return date == null ? null : date.getTime();
    }
}
