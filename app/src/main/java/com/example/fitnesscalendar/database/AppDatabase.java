package com.example.fitnesscalendar.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.fitnesscalendar.dao.*;
import com.example.fitnesscalendar.entities.*;
import com.example.fitnesscalendar.relations.*;

/**
 * The Main Database Class for the application.
 * This class serves as the central hub for Room persistence, registering all
 * tables (Entities) and providing access points (DAOs) to interact with the data.
 */
@Database(
        entities= {
                User.class, CalendarDay.class, Quote.class, Exercise.class, Workout.class,
                Category.class, Step.class, Goal.class,  UserWorkoutCrossRef.class,
                ExerciseCategoryCrossRef.class, WorkoutExerciseCrossRef.class, CalendarDayWorkoutCrossRef.class,
                AiMessage.class
},
        version = 23,
        exportSchema = false
)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4; // number of bg threads to use for database operations
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Singleton instance to prevent multiple database objects being opened simultaneously
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract GoalDao goalDao();
    public abstract CalendarDayDao calendarDayDao();
    public abstract ExerciseDao exerciseDao();
    public abstract CategoryDao categoryDao();
    public abstract StepDao stepDao();
    public abstract WorkoutDao workoutDao();
    public abstract AiDao aiDao();

    /**
     * Database Lifecycle Callback.
     * Logic here runs when the database is first created or every time it is opened.
     */
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {@Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        fillCategories(); // Pre-populate data only once upon first installation
    }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Every time the app starts, ensure categories present
            fillCategories();
        }
    };

    /**
     * Pre-populates the 'categories' table with default values
     */
    private static void fillCategories() {
        databaseWriteExecutor.execute(() -> {
            CategoryDao dao = INSTANCE.categoryDao();
            List<Category> predefined = getPredefinedCategories();
            for (Category cat : predefined) {
                dao.insert(cat); // Strategy.IGNORE handles the rest
            }
        });
    }

    /**
     * Singleton getter for the database.
     * Uses double-check to ensure thread safety.
     */
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, "fitness_calendar_db")
                                .addCallback(roomCallback)
                                .fallbackToDestructiveMigration()
                                .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Hardcoded list of default exercise categories.
     */
    private static List<Category> getPredefinedCategories() {
        List<Category> categories = new ArrayList<>();

        // Define names in a simple array.
        // You can easily insert a new name anywhere in this list.
        String[] names = {
                "Neck", "Arms", "Shoulders", "Chest", "Back", "Abs", "Glutes", "Legs", "Stretching",
                "Cardio", "Full Body", "Biceps", "Triceps", "Forearms", "Side Delts",
                "Front Delts", "Rear Delts", "Upper Chest", "Middle Chest", "Lower Chest",
                "Upper Back", "Lower Back", "Upper Abs", "Lower Abs", "Obliques",
                "Quadriceps", "Hamstrings", "Adductors", "Abductors", "Calves"
        };

        for (String name : names) {
            // the null is passed for the ID so Room auto-generates it
            categories.add(new Category(null, name));
        }
        return categories;
    }
}
