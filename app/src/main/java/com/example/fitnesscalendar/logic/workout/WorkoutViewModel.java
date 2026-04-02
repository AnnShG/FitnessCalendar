package com.example.fitnesscalendar.logic.workout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.UserWithGoals;
import com.example.fitnesscalendar.repository.UserRepository;
import com.example.fitnesscalendar.repository.WorkoutRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class WorkoutViewModel extends AndroidViewModel {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    public WorkoutViewModel(@NotNull Application app) {
        super(app);
        workoutRepository = new WorkoutRepository(app);
        userRepository = new UserRepository(app);
    }

    public LiveData<UserWithGoals> getLoggedInUser() {
        return userRepository.getLatestUser();
    }

    public void saveWorkout(Workout workout, List<Long> exercises) {
        workoutRepository.insertFullWorkout(workout, exercises);
    }

    public LiveData<List<FullWorkoutRecord>> getFullWorkoutRecords(long userId) {
        return workoutRepository.getFullWorkoutRecords(userId);
    }

    public LiveData<FullWorkoutRecord> getFullWorkoutById(long id) {
        return workoutRepository.getFullWorkoutById(id);
    }

    public void updateWorkout(Workout workout, List<Long> exerciseIds) {
        workoutRepository.updateFullWorkout(workout, exerciseIds);
    }

    public void deleteWorkout(Workout workout) {
        workoutRepository.deleteWorkout(workout);
    }

    public void attachWorkoutToDates(long userId, long workoutId, Set<String> dateStrings) {
        workoutRepository.attachWorkoutToDates(userId, workoutId, dateStrings);
    }

}
