package com.example.fitnesscalendar.logic.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

        // 1. Initialize the adapter (you already declared 'goalAdapter' at the top)
        goalAdapter = new GoalAdapter(goal -> showEditDialog(goal));

        // 2. Setup the RecyclerView
        binding.goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.goalsRecyclerView.setAdapter(goalAdapter);

        // 3. Initialize the correct ViewModel (ProfileViewModel instead of SurveyViewModel)
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // 4. Observe the data from the DB, and pushes new to the list
        viewModel.getProfileData().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null && userWithGoals.user != null) {
                // Set the User Name from the embedded user object
                binding.userNameTitle.setText(userWithGoals.user.getName());

                // Pass the list of goals to the adapter
                if (userWithGoals.goals != null) {
                    goalAdapter.setGoals(userWithGoals.goals);
                }
            }
        });
    }

    private void showEditDialog(Goal goal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Edit Goal");

        // Add an EditText to the dialog
        final EditText input = new EditText(requireContext());
        input.setText(goal.getGoalText());
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newText = input.getText().toString().trim();
            if (!newText.isEmpty()) {
                goal.setGoalText(newText);
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
