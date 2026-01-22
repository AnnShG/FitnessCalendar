package com.example.fitnesscalendar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.fitnesscalendar.dao.ActivityDao;
import com.example.fitnesscalendar.dao.CalendarDayDao;
import com.example.fitnesscalendar.dao.CategoryDao;
import com.example.fitnesscalendar.dao.ExerciseDao;
import com.example.fitnesscalendar.dao.QuoteDao;
import com.example.fitnesscalendar.dao.StepDao;
import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.dao.WorkoutDao;
import com.example.fitnesscalendar.entities.Activity;
import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Quote;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.entities.UserWorkoutCrossRef;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.entities.WorkoutExerciseCrossRef;

@Database(entities= {User.class, CalendarDay.class, Quote.class, Exercise.class, Workout.class,
        Category.class, Activity.class, Step.class, WorkoutExerciseCrossRef.class, UserWorkoutCrossRef.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract CalendarDayDao calendarDayDao();
    public abstract QuoteDao quoteDao();
    public abstract ExerciseDao exerciseDao();
    public abstract WorkoutDao workoutDao();
    public abstract CategoryDao categoryDao();
    public abstract ActivityDao activityDao();
    public abstract StepDao stepDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitness_calendar_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
