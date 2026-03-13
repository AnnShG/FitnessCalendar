package com.example.fitnesscalendar.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.fitnesscalendar.dao.ActivityDao;
import com.example.fitnesscalendar.dao.CalendarDayDao;
import com.example.fitnesscalendar.dao.CategoryDao;
import com.example.fitnesscalendar.dao.ExerciseDao;
import com.example.fitnesscalendar.dao.GoalDao;
import com.example.fitnesscalendar.dao.StepDao;
import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.dao.WorkoutDao;
import com.example.fitnesscalendar.entities.Activity;
import com.example.fitnesscalendar.entities.CalendarDay;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Goal;
import com.example.fitnesscalendar.entities.Quote;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.relations.ExerciseCategoryCrossRef;
import com.example.fitnesscalendar.relations.UserWorkoutCrossRef;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.WorkoutExerciseCrossRef;

@Database(
        entities= {
                User.class, CalendarDay.class, Quote.class, Exercise.class, Workout.class,
                Category.class, Activity.class, Step.class, Goal.class,  UserWorkoutCrossRef.class,
                ExerciseCategoryCrossRef.class, WorkoutExerciseCrossRef.class
},
        version = 12,
        exportSchema = false
)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract GoalDao goalDao();
    public abstract CalendarDayDao calendarDayDao();
    public abstract ExerciseDao exerciseDao();
    public abstract CategoryDao categoryDao();
    public abstract StepDao stepDao();
    public abstract WorkoutDao workoutDao();
    public abstract ActivityDao activityDao();

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // This runs the very first time the database is created
            databaseWriteExecutor.execute(() -> {
                CategoryDao dao = getDatabase(null).categoryDao(); // Ensure you have a CategoryDao
                dao.insert(new Category(1L, "Legs"));
                dao.insert(new Category(2L, "Arms"));
                dao.insert(new Category(3L, "Chest"));
                dao.insert(new Category(4L, "Back"));
                dao.insert(new Category(5L, "Shoulders"));
                dao.insert(new Category(6L, "Core"));
                dao.insert(new Category(7L, "Cardio"));
                dao.insert(new Category(8L, "Full Body"));
            });
        }
    };

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fitness_calendar_db"
                    )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
