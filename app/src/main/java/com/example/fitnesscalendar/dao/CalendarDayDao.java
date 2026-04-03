package com.example.fitnesscalendar.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.relations.CalendarDayWorkoutCrossRef;

import java.util.List;

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
    default long getOrCreateDayId(long userId, long epochDay) {
        Long id = getDayIdByDate(userId, epochDay);
        if (id != null) {
            return id;
        } else {
            CalendarDay newDay = new CalendarDay();
            newDay.userId = userId;
            newDay.date = epochDay;
            return insertCalendarDay(newDay);
        }
    }

    @Query("SELECT calendar_day_id FROM calendar_days WHERE user_id = :userId AND date = :date")
    Long getDayIdByDate(long userId, long date);

    @Insert
    long insertCalendarDay(CalendarDay day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCalendarDayWorkoutCrossRef(CalendarDayWorkoutCrossRef crossRef);

    @Transaction
    @Query("SELECT cd.date, w.colour FROM calendar_days cd " +
            "INNER JOIN calendar_day_workout_cross_ref ref ON cd.calendar_day_id = ref.calendar_day_id " +
            "INNER JOIN workouts w ON ref.workout_id = w.workout_id " +
            "WHERE cd.user_id = :userId")
    LiveData<List<DateColorResult>> getWorkoutColorsForUser(long userId);

    class DateColorResult {
        public Long date;
        public Integer colour;
    }


}
