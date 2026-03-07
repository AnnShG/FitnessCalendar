package com.example.fitnesscalendar.logic.workout;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesscalendar.repository.WorkoutRepository;

import org.jetbrains.annotations.NotNull;

public class WorkoutViewModel extends AndroidViewModel {

    private final WorkoutRepository workoutRepository;

    public WorkoutViewModel(@NotNull Application app) {
        super(app);
        workoutRepository = new WorkoutRepository(app);
    }


}
