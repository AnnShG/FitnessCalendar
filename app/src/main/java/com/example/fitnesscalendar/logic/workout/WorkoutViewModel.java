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

    public LiveData<List<FullWorkoutRecord>> getAllWorkouts(long userId) {
        return workoutRepository.getAvailableWorkouts(userId);
    }

//    public LiveData<FullWorkoutRecord> getFullWorkoutById(long id) {
//        return workoutRepository.getFullWorkoutById(id);
//    }

}
