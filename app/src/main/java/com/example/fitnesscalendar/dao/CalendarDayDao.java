package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.CalendarDay;

@Dao
public interface CalendarDayDao {
    @Insert
    void insert(CalendarDay days);

    @Update
    void update(CalendarDay days);

   @Delete
   void delete(CalendarDay days);

   // when the user chooses one of the days
    @Query("SELECT * FROM calendar_days WHERE id = :dayId")
    CalendarDay getCalendarDayById(int dayId);
}
