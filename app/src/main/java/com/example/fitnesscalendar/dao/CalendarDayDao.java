package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.relations.CalendarDayWorkoutCrossRef;

@Dao
public interface CalendarDayDao {
    @Insert
    void insert(CalendarDay days);

    @Update
    void update(CalendarDay days);

   @Delete
   void delete(CalendarDay days);

   // when the user chooses one of the days
    @Query("SELECT * FROM calendar_days WHERE user_id = :dayId")
    CalendarDay getCalendarDayById(int dayId);

    @Transaction
    default long getOrCreateDayId(long userId, String date) {
        Long id = getDayIdByDate(userId, date);
        if (id != null) {
            return id;
        } else {
            CalendarDay newDay = new CalendarDay();
            newDay.userId = userId;
            newDay.date = date;
            return insertCalendarDay(newDay);
        }
    }

    @Query("SELECT calendar_day_id FROM calendar_days WHERE user_id = :userId AND date = :date")
    Long getDayIdByDate(long userId, String date);

    @Insert
    long insertCalendarDay(CalendarDay day);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCalendarDayWorkoutCrossRef(CalendarDayWorkoutCrossRef crossRef);
}
