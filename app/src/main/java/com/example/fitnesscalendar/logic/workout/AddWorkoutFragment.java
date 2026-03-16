package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.AddWorkoutScreenBinding;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Workout;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class AddWorkoutFragment extends Fragment {

    private AddWorkoutScreenBinding binding;
    private WorkoutViewModel workoutViewModel;
    private Long currentUserId;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = AddWorkoutScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.getId();
            }
        });

        binding.addExerciseButton.setOnClickListener(v -> {// lambda - shorter
            Navigation.findNavController(view)
                    .navigate(R.id.action_AddWorkoutScreen_to_ExerciseSelectScreen);
        });

        binding.saveWorkoutButton.setOnClickListener(v -> {
            onSaveButtonClicked();
        });

        binding.cancelWorkoutButton.setOnClickListener(v -> {
                    NavHostFragment.findNavController(this)
                            .navigateUp();
                }
        );
    }


    private void onSaveButtonClicked() {
        String title = binding.workoutTitleInput.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a workout title", Toast.LENGTH_SHORT).show();
            return;
        }

        String Description = binding.workoutDescriptionInput.getText().toString();
        String note = binding.workoutNotesInput.getText().toString();

        Workout workout = new Workout();
        workout.setTitle(title);
        workout.setDescription(Description);
        workout.setNote(note);

        if (currentUserId != null) {
            workout.setOwnerId(currentUserId);
            workout.setUserCreated(true);
        } else {
            Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Exercise> exercises = new ArrayList<>();
        for (int i = 0; i < binding.exercisesContainer.getChildCount(); i++) {
            View exerciseRow = binding.exercisesContainer.getChildAt(i);

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
