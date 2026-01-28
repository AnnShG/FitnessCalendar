package com.example.fitnesscalendar.ui.survey;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.fitnesscalendar.UserRepository;
import com.example.fitnesscalendar.entities.User;

import org.jetbrains.annotations.NotNull;

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

    public void toggleGoal(String goal) {
        if (selectedGoals.contains(goal)) {
            selectedGoals.remove(goal); // If it's already there, take it out (unselect)
        } else {
            selectedGoals.add(goal); // If it's not there, add it (select)
        }
    }

    public Set<String> getSelectedGoals() {
        return selectedGoals;
    }

    private final UserRepository repository;

    public SurveyViewModel(@NotNull Application app) {
        super(app);
        repository = new UserRepository(app);
    }

    public void onNextClicked() {
        if (name == null || name.isEmpty()) return; // Basic validation

        User user = new User();
        user.name = name;
        user.birthDate = birthDate;
        user.gender = gender;
//        user.goal = selectedGoals;

        repository.insert(user);

    }
}
