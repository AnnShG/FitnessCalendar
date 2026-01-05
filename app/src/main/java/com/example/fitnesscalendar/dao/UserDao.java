package com.example.fitnesscalendar.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.User;
import com.example.fitnesscalendar.relations.UsersWithWorkouts;
import com.example.fitnesscalendar.relations.WorkoutWithExercises;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);

    @Transaction
    @Query("SELECT * FROM users WHERE id = :id")
    public UsersWithWorkouts getUserWithExercises(long id);

}
