package com.example.fitnesscalendar.ui.survey;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.fitnesscalendar.UserRepository;
import com.example.fitnesscalendar.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class SurveyViewModel extends AndroidViewModel {

    public String name;
    public Date birthDate;
    public String gender;
    public String goal;

    private final UserRepository repository;

    public SurveyViewModel(@NotNull Application app) {
        super(app);
        repository = new UserRepository(app);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setbirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void onNextClicked() {
        User user = new User();
        user.name = name;
        user.birthDate = birthDate;
        user.gender = gender;
        user.goal = goal;

        repository.insert(user);

    }
}
