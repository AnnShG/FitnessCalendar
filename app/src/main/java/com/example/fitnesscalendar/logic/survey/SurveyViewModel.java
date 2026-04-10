package com.example.fitnesscalendar.logic.survey;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.fitnesscalendar.entities.Goal;
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
    @Setter
    @Getter
    private String customGoal;

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


        // 2. Prepare the List of Goal Entities from the selected Set
        ArrayList<Goal> goalEntities = new ArrayList<>();

        // Convert the Set to an ArrayList for the Entity
        if (this.getSelectedGoals() != null) {
            for (String goalText : this.getSelectedGoals()) {
                Goal goalEntry = new Goal();
                goalEntry.setGoalTitle(goalText);
                goalEntry.setCustom(false);
                goalEntities.add(goalEntry);
            }
        }

        if (this.getCustomGoal() != null && !this.getCustomGoal().isEmpty()) {
            Goal customEntry = new Goal();
            customEntry.setGoalTitle(this.getCustomGoal());
            customEntry.setCustom(true); // Mark as custom for the Profile logic
            goalEntities.add(customEntry);
        }
        // 2. Use the REPOSITORY (not database) to save
        // The repository handles the background thread
        repository.insertUserWithGoals(newUser, goalEntities);
    }

}
