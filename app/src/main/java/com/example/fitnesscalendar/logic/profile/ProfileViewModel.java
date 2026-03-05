package com.example.fitnesscalendar.logic.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.fitnesscalendar.repository.UserRepository;
import com.example.fitnesscalendar.relations.UserWithGoals;

public class ProfileViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        // We initialize the repository here so it can talk to the Room DB
        this.repository = new UserRepository(application);
    }

    public LiveData<UserWithGoals> getProfileData() {
        return repository.getLatestUser();
    }
}
