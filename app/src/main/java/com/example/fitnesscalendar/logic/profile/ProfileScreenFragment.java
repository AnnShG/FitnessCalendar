package com.example.fitnesscalendar.logic.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ProfileScreenBinding;
import com.example.fitnesscalendar.entities.Goal;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.Nullable;

import lombok.NonNull;

public class ProfileScreenFragment extends Fragment {

    private ProfileScreenBinding binding;
    private GoalAdapter goalAdapter;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ProfileScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalAdapter = new GoalAdapter(goal -> showEditDialog(goal));

        // setup the RecyclerView
        binding.goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.goalsRecyclerView.setAdapter(goalAdapter);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getProfileData().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null && userWithGoals.user != null) {
                binding.userNameTitle.setText(userWithGoals.user.getName());

                if (userWithGoals.goals != null) {
                    goalAdapter.setGoals(userWithGoals.goals);
                }
            }
        });

        binding.allExercisesRow.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_NavigationProfile_to_ExercisesList);
        });

        binding.allWorkoutsRow.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_NavigationProfile_to_WorkoutsList);
        });

    }

    private void showEditDialog(Goal goal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Goal");

        // Add an EditText to the dialog
        final EditText input = new EditText(requireContext());
        input.setText(goal.getGoalTitle());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newText = input.getText().toString().trim();
            if (!newText.isEmpty()) {
                goal.setGoalTitle(newText);
                // 3. Tell ViewModel to save the change
                viewModel.updateGoal(goal);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
