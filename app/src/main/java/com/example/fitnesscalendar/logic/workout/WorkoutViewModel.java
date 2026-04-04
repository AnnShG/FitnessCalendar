package com.example.fitnesscalendar.logic.workout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.DateColourResult;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.relations.PlannedWorkoutInfo;
import com.example.fitnesscalendar.relations.UserWithGoals;
import com.example.fitnesscalendar.repository.UserRepository;
import com.example.fitnesscalendar.repository.WorkoutRepository;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WorkoutViewModel serves as the architectural bridge between the UI (Fragments) and the Data Layer (Repositories)
 * It manages the UI's data state and coordinates operations across both Workout and User repositories.
 */
public class WorkoutViewModel extends AndroidViewModel {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;


    public WorkoutViewModel(@NotNull Application app) {
        super(app);
        // Initializing repositories to handle database operations
        workoutRepository = new WorkoutRepository(app);
        userRepository = new UserRepository(app);
    }

    public LiveData<UserWithGoals> getLoggedInUser() {
        return userRepository.getLatestUser();
    }

    public void saveWorkout(Workout workout, List<Long> exercises) {
        workoutRepository.insertFullWorkout(workout, exercises);
    }

//    Fetches all workouts created by a user.
    public LiveData<List<FullWorkoutRecord>> getFullWorkoutRecords(long userId) {
        return workoutRepository.getFullWorkoutRecords(userId);
    }

    // Retrieves detailed information for a single workout by its ID.
    public LiveData<FullWorkoutRecord> getFullWorkoutById(long id) {
        return workoutRepository.getFullWorkoutById(id);
    }

    public void updateWorkout(Workout workout, List<Long> exerciseIds) {
        workoutRepository.updateFullWorkout(workout, exerciseIds);
    }

    public void deleteWorkout(Workout workout) {
        workoutRepository.deleteWorkout(workout);
    }

//    Links a workout to multiple dates on the calendar
    public void attachWorkoutToDates(long userId, long workoutId, Set<Long> epochDays) {
        // Converts UI provided Set<Long> into Set<LocalDate> before passing to repo using Java Streams
        Set<LocalDate> localDates = epochDays.stream()
                .map(LocalDate::ofEpochDay)
                .collect(Collectors.toSet());

        workoutRepository.attachWorkoutToDates(userId, workoutId, localDates);
    }

    // Provides the data needed to draw colored dots on the calendar grid
    public LiveData<List<DateColourResult>> getWorkoutDotsForUser(long userId) {
        return workoutRepository.getWorkoutColorsForUser(userId);
    }

    // Retrieves a unique list of workouts that have been scheduled
    // Used to populate the management cards below the calendar
    public LiveData<List<PlannedWorkoutInfo>> getUniquePlannedWorkouts(long userId) {
        return workoutRepository.getUniquePlannedWorkouts(userId);
    }

    public void deleteWorkoutFromCalendar(long userId, long workoutId) {
        workoutRepository.deleteWorkoutFromCalendar(userId, workoutId);
    }

    public void updateWorkoutPlan(long userId, long workoutId, Set<Long> epochDays) {
        workoutRepository.updateWorkoutPlan(userId, workoutId, epochDays);
    }

}
