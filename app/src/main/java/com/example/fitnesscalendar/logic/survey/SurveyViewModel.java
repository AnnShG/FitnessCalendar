package com.example.fitnesscalendar.logic.survey;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesscalendar.repository.UserRepository;
import com.example.fitnesscalendar.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

public class SurveyViewModel extends AndroidViewModel {

    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private Date birthDate;
    @Setter
    @Getter
    private String gender;
    @Setter
    @Getter
    private Set<String> selectedGoals = new HashSet<>();

    public void toggleGoal(String goals) {
        if (selectedGoals.contains(goals)) {
            selectedGoals.remove(goals); // If it's already there, take it out (unselect)
        } else {
            selectedGoals.add(goals); // If it's not there, add it (select)
        }
    }

    private final UserRepository repository;

    public SurveyViewModel(@NotNull Application app) {
        super(app);
        repository = new UserRepository(app);
    }

    public void saveUserProfileToDatabase() {
        // 1. Create the User object
        User newUser = new User();
        newUser.setName(this.getName());
        newUser.setBirthDate(this.getBirthDate());
        newUser.setGender(this.getGender());

        // Convert the Set to an ArrayList for the Entity
//        newUser.setGoals(new ArrayList<>(this.getSelectedGoals()));
        if (this.getSelectedGoals() != null) {
            newUser.setGoals(new ArrayList<>(this.getSelectedGoals()));
        }
        // 2. Use the REPOSITORY (not 'database') to save
        // The repository handles the background thread, so just call insert:
        repository.insert(newUser);
    }

}
