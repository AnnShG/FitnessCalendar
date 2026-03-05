package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Goal;

import java.util.List;

@Dao
public interface GoalDao {
    @Insert
    void insert(Goal goal);

    @Update
    void update(Goal goal);

    @Delete
    void delete(Goal goal);

    // get all goals for a specific user
    @Query("SELECT * FROM goals WHERE user_id = :userId")
    List<Goal> getGoalsForUser(long userId);

    // Delete a specific goal by its ID
    @Query("DELETE FROM goals WHERE goal_id = :goalId")
    void deleteGoalById(long goalId);
}
