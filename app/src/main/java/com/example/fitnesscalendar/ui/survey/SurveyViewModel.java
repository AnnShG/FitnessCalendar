package com.example.fitnesscalendar.ui.survey;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.fitnesscalendar.UserRepository;
import com.example.fitnesscalendar.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

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
    private String goal;

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
        user.goal = goal;

        repository.insert(user);

    }
}
