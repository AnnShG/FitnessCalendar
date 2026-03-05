package com.example.fitnesscalendar.logic.exercise;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesscalendar.repository.ExerciseRepository;

import org.jetbrains.annotations.NotNull;

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

//    @Getter
//    private final LiveData<List<Exercise>> allExercises;

    public ExerciseViewModel(@NotNull Application app) {
        super(app);
        repository = new ExerciseRepository(app);
//        allExercises = repository.getAllExercises();
    }

//    public void insert(Exercise exercise) {
//        repository.insert(exercise);
//    }

//    public LiveData<List<Exercise>> getAllExercises() {
//        return allExercises;
//    }

//    public void saveUserProfileToDatabase() {
//        Exercise newExercise = new Exercise();
//        newExercise.setExerciseId(this.getExerciseId());
//        newExercise.setTitle(this.getTitle());
//        newExercise.setDescription(this.getDescription());
//        newExercise.setMediaUri(this.getMediaUri());
//        newExercise.setNotes(this.getNotes());
////        newExercise.setDifficultyLevel(this.getDifficultyLevel());
//        newExercise.setUserCreated(this.getUserCreated());
//
//        repository.insert(newExercise);
//    }
}
