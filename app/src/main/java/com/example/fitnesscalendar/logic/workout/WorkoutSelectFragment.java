package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.WorkoutsSelectScreenBinding;
import com.example.fitnesscalendar.databinding.WorkoutsListScreenBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class WorkoutSelectFragment extends WorkoutsListFragment {

    private WorkoutsSelectScreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WorkoutsSelectScreenBinding.inflate(inflater, container, false);

        super.binding = WorkoutsListScreenBinding.bind(binding.getRoot());

        super.root = binding.getRoot();
        return super.root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (workoutAdapter != null) {
            workoutAdapter.setSelectionMode(true);

            workoutAdapter.setOnSelectionChangedListener(count -> {
                updateSelectionButton(count);
            });
        }

        // get existing IDs passed from the  screen
        long[] existing = getArguments() != null ? getArguments().getLongArray("existing_ids") : null;
        if (existing != null && existing.length > 0) {
            List<Long> existingList = new ArrayList<>();
            for (long id : existing) existingList.add(id);

            // Set them in the adapter
            workoutAdapter.setSelectedIds(existingList);

            updateSelectionButton(existingList.size());
        }

        // This triggers the navigation to details
        workoutAdapter.setOnInfoClickListener(exerciseId -> {
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", exerciseId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutSelectScreen_to_WorkoutDetail, bundle);
        });

        binding.selectWorkoutBtn.setOnClickListener(v -> {
            List<Long> selectedIds = workoutAdapter.getSelectedWorkoutIds();
            // pass  IDs back to the AddWorkoutFragment via a SharedViewModel
            long[] idArray = selectedIds.stream().mapToLong(l -> l).toArray();

            Bundle result = new Bundle();
            result.putLongArray("selected_ids", idArray);

            // send result back to AddWorkoutFragment
            getParentFragmentManager().setFragmentResult("workout_selection", result);
            NavHostFragment.findNavController(this).navigateUp();
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(WorkoutSelectFragment.this)
                        .navigateUp()
        );
    }

    private void updateSelectionButton(int count) {
        if (count > 0) {
            binding.btnContainer.setVisibility(View.VISIBLE);
            String btnText = "Add " + count + (count == 1 ? " Workout" : " Workouts");
            binding.selectWorkoutBtn.setText(btnText);
        } else {
            binding.btnContainer.setVisibility(View.GONE);
        }
    }
}
