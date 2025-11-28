package com.example.fitnesscalendar.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fitnesscalendar.dao.CalendarDayDao;
import com.example.fitnesscalendar.dao.QuoteDao;
import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.entities.User;

@Database(entities= {User.class, CalendarDay.class, QuoteDao.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract CalendarDayDao calendarDayDao();

    public abstract QuoteDao quoteDao();

}
