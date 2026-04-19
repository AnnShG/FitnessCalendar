package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.WorkoutsSelectScreenBinding;
import com.example.fitnesscalendar.databinding.WorkoutsListScreenBinding;
import com.example.fitnesscalendar.entities.Workout;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

/**
 * WorkoutSelectFragment allows the user to pick a specific workout to be scheduled on the calendar.
 * It extends WorkoutsListFragment to reuse the search, filtering, and database observation logic,
 * but adds a selection interface (orange border on cards) and a confirmation button.
 */
public class WorkoutSelectFragment extends WorkoutsListFragment {

    private WorkoutsSelectScreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WorkoutsSelectScreenBinding.inflate(inflater, container, false);

//         manually binded the parent's layout variables.
        super.binding = WorkoutsListScreenBinding.bind(binding.getRoot());

        super.root = binding.getRoot();
        return super.root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (workoutAdapter != null) {
            workoutAdapter.setSelectionMode(true); // activates the visual "Orange Border"

            // Listen for changes in selection to update the select button text
            workoutAdapter.setOnSelectionChangedListener(count -> {
                updateSelectionButton(count);
            });
        }

        // get existing IDs passed from the screen
        long[] existing = getArguments() != null ? getArguments().getLongArray("existing_ids") : null;
        if (existing != null && existing.length > 0) {
            List<Long> existingList = new ArrayList<>();
            for (long id : existing) existingList.add(id);

            // Set them in the adapter
            workoutAdapter.setSelectedIds(existingList);

            updateSelectionButton(existingList.size());
        }

        // triggers the navigation to detail screen
        if (workoutAdapter != null) {
            workoutAdapter.setOnInfoClickListener(workoutId -> {
                Bundle bundle = new Bundle();
                bundle.putLong("workoutId", workoutId);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_WorkoutSelectScreen_to_WorkoutDetail, bundle);
            });
        }

        // check if workouts are already selected when the view is created/recreated
        if (workoutAdapter != null) {
            int currentSelectionCount = workoutAdapter.getSelectedIds().size();
            updateSelectionButton(currentSelectionCount);
        }

//        When the "Add Workout" button is clicked, selected workout data is packed
//        into a result bundle and sent back to the PlanProgramFragment.
        binding.selectWorkoutBtn.setOnClickListener(v -> {
            Workout selectedWorkout = workoutAdapter.getSelectedWorkout();

            if (selectedWorkout != null) {
                Bundle result = new Bundle();
                result.putLong("workoutId", selectedWorkout.getWorkoutId());
                result.putString("workoutTitle", selectedWorkout.getTitle());
                result.putInt("workoutColor", selectedWorkout.getColour());

                getParentFragmentManager().setFragmentResult("workout_selection", result);

                NavHostFragment.findNavController(this).navigateUp();
            }
        });

        binding.filterWorkoutsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("filter_type", "workout");
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutSelectScreen_to_FilterScreen, bundle);
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(WorkoutSelectFragment.this)
                        .navigateUp()
        );

        binding.cancelWorkoutSelectBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(WorkoutSelectFragment.this)
                        .navigateUp()
        );
    }

    private void updateSelectionButton(int count) {
        if (count > 0) {
            binding.btnContainer.setVisibility(View.VISIBLE);
            String btnText = "Add " + count + " Workout";
            binding.selectWorkoutBtn.setText(btnText);
        } else {
            binding.btnContainer.setVisibility(View.GONE);
        }
    }
}
