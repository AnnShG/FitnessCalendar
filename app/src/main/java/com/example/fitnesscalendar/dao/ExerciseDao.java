package com.example.fitnesscalendar.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.relations.ExerciseCategoryCrossRef;
import com.example.fitnesscalendar.relations.ExerciseSummary;
import com.example.fitnesscalendar.relations.FullExerciseRecord;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    // method creates the "Link" between an Exercise and a Category.
    @Insert(onConflict = OnConflictStrategy.REPLACE) // prevents linking the same Ex to the same Cat
    void insertCategoryCrossRef(ExerciseCategoryCrossRef crossRef);

    @Transaction // required because Room runs several queries (exercises, steps, categories)
    @Query("SELECT * FROM exercises")
    LiveData<List<FullExerciseRecord>> getFullExerciseRecords();

    @Query("SELECT * FROM exercises")
    LiveData<List<ExerciseSummary>> getExerciseSummaries();

//    @Transaction
//    @Query("SELECT * FROM exercises")
//    public LiveData<List<ExercisesWithCategory>> getExercisesWithCategories();
}
