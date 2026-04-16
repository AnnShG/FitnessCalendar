package com.example.fitnesscalendar.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.dao.GoalDao;
import com.example.fitnesscalendar.dao.UserDao;
import com.example.fitnesscalendar.database.AppDatabase;
import com.example.fitnesscalendar.entities.Goal;
import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.relations.UserWithGoals;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final GoalDao goalDao;

    // runs DB on background thread, because Android doesn't allow it to run on main thread
    // a single executor for the whole app
    private static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(2);

    public UserRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        userDao = db.userDao();
        goalDao = db.goalDao();
    }

    public void insertUserWithGoals(User user, List<Goal> goals) {
        databaseExecutor.execute(() -> {
            // 1. Insert User and capture the generated ID
            long newUserId = userDao.insert(user);

            // 2. Link each Goal to that User ID and insert
            for (Goal goal : goals) {
                goal.setUserId(newUserId);
                goalDao.insert(goal);
            }
        });
    }

    public LiveData<UserWithGoals> getLatestUser() {
        return userDao.getLatestUser();
    }

    public void updateGoal(Goal goal) {
        databaseExecutor.execute(() -> {
            goalDao.update(goal);
        });
    }

    public boolean hasUser() {
        return userDao.getUserCount() > 0;
    }

    public Executor getDatabaseExecutor() {
        return databaseExecutor;
    }


    public void updateUserGoals(Long userId, List<String> goalTitles, String customGoalText) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // dlete existing goals for this user
            goalDao.deleteGoalsByUserId(userId);

            // insert predefined goals
            for (String title : goalTitles) {
                Goal goal = new Goal();
                goal.setUserId(userId);
                goal.setGoalTitle(title);
                goal.setCustom(false);
                goalDao.insert(goal);
            }

            // insert custom goal if text is not empty
            if (customGoalText != null && !customGoalText.isEmpty()) {
                Goal custom = new Goal();
                custom.setUserId(userId);
                custom.setGoalTitle(customGoalText);
                custom.setCustom(true);
                goalDao.insert(custom);
            }
        });
    }
}
