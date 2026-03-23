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
        version = 16,
        exportSchema = false
)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract GoalDao goalDao();
    public abstract CalendarDayDao calendarDayDao();
    public abstract ExerciseDao exerciseDao();
    public abstract CategoryDao categoryDao();
    public abstract StepDao stepDao();
    public abstract WorkoutDao workoutDao();
    public abstract ActivityDao activityDao();

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {@Override
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
        super.onCreate(db);
        fillCategories();
    }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Every time the app opens, ensure categories exist
            fillCategories();
        }
    };

    private static void fillCategories() {
        databaseWriteExecutor.execute(() -> {
            CategoryDao dao = INSTANCE.categoryDao();
            // check if table is empty before inserting to avoid duplicates
            if (dao.getCategoryCount() == 0) {
                dao.insertAll(getPredefinedCategories());
            }
        });
    }

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

    private static List<Category> getPredefinedCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1L, "Neck"));
        categories.add(new Category(2L, "Arms"));
        categories.add(new Category(3L, "Shoulders"));
        categories.add(new Category(4L, "Chest"));
        categories.add(new Category(5L, "Back"));
        categories.add(new Category(6L, "Abs"));
        categories.add(new Category(7L, "Glutes"));
        categories.add(new Category(8L, "Legs"));
        categories.add(new Category(9L, "Cardio"));
        categories.add(new Category(10L, "Full Body"));
        categories.add(new Category(11L, "Biceps"));
        categories.add(new Category(12L, "Triceps"));
        categories.add(new Category(13L, "Forearms"));
        categories.add(new Category(14L, "Side Delts"));
        categories.add(new Category(15L, "Front Delts"));
        categories.add(new Category(16L, "Rear Delts"));
        categories.add(new Category(17L, "Upper Chest"));
        categories.add(new Category(18L, "Middle Chest"));
        categories.add(new Category(19L, "Lower Chest"));
        categories.add(new Category(20L, "Upper Back"));
        categories.add(new Category(21L, "Lower Back"));
        categories.add(new Category(22L, "Upper Abs"));
        categories.add(new Category(23L, "Lower Abs"));
        categories.add(new Category(24L, "Obliques"));
        categories.add(new Category(25L, "Quadriceps"));
        categories.add(new Category(26L, "Hamstrings"));
        categories.add(new Category(27L, "Adductors"));
        categories.add(new Category(28L, "Abductors"));
        categories.add(new Category(29L, "Calves"));
        categories.add(new Category(30L, "Stretching"));
        return categories;
    }
}
