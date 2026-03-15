package com.example.fitnesscalendar.logic.exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.example.fitnesscalendar.relations.UserWithGoals;
import com.example.fitnesscalendar.repository.ExerciseRepository;
import com.example.fitnesscalendar.repository.UserRepository;

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

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    public ExerciseViewModel(@NotNull Application app) {
        super(app);
        exerciseRepository = new ExerciseRepository(app);
        this.userRepository = new UserRepository(app);
    }

    public LiveData<UserWithGoals> getLoggedInUser() {
        return userRepository.getLatestUser();
    }

    public LiveData<List<Category>> getAllCategories() {
        return exerciseRepository.getAllCategories();
    }

    public void saveExercise(Exercise exercise, List<Step> steps, List<Long> categoryIds) {
        exerciseRepository.insertFullExercise(exercise, steps, categoryIds);
    }

    public LiveData<FullExerciseRecord> getFullExerciseById(long id) {
        return exerciseRepository.getFullExerciseById(id);
    }

    public LiveData<List<FullExerciseRecord>> getAllFullExerciseRecords() {
        return exerciseRepository.getAllFullExerciseRecords();
    }

}
