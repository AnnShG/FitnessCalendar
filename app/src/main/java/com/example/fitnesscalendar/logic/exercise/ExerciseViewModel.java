package com.example.fitnesscalendar.logic.exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.repository.ExerciseRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ExerciseViewModel extends AndroidViewModel {
    @Setter
    @Getter
    private Long exerciseId;
    @Setter
    @Getter
    private String title;
    @Setter
    @Getter
    private String description;
    @Setter
    @Getter
    private String mediaUri;
    @Setter
    @Getter
    public String notes;
    @Setter
    @Getter
    public String difficultyLevel;
    @Setter
    @Getter
    public Boolean userCreated;

    private final ExerciseRepository repository;

    public ExerciseViewModel(@NotNull Application app) {
        super(app);
        repository = new ExerciseRepository(app);
    }

    public void saveExercise(Exercise exercise, List<Step> steps, List<Long> categoryIds) {
        repository.insertFullExercise(exercise, steps, categoryIds);
    }

}
