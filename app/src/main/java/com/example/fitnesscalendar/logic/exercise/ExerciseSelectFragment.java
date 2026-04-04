package com.example.fitnesscalendar.logic.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExercisesListScreenBinding;
import com.example.fitnesscalendar.databinding.ExercisesSelectScreenBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class ExerciseSelectFragment extends ExercisesListFragment {

    private ExercisesSelectScreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // child binding
        binding = ExercisesSelectScreenBinding.inflate(inflater, container, false);

        super.binding = ExercisesListScreenBinding.bind(binding.getRoot()); // parent's binding var

        super.root = binding.getRoot(); // Pass the root to the parent
        return super.root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // shared viewModel
//        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        // super - parent - sets up the recyclerView and Adapter
        super.onViewCreated(view, savedInstanceState);

        if (adapter != null) {
            adapter.setSelectionMode(true); // Tell adapter to show checkboxes and eye

            adapter.setOnSelectionChangedListener(count -> {
                updateSelectionButton(count); //  helper method
            });
        }

        // get existing IDs passed from the Workout screen
        long[] existing = getArguments() != null ? getArguments().getLongArray("existing_ids") : null;
        if (existing != null && existing.length > 0) {
            List<Long> existingList = new ArrayList<>();
            for (long id : existing) existingList.add(id);

            // Set them in the adapter
            adapter.setSelectedIds(existingList);

            updateSelectionButton(existingList.size());
        }

        // This triggers the navigation to details
        adapter.setOnInfoClickListener(exerciseId -> {
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", exerciseId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_ExerciseSelectScreen_to_ExerciseDetail, bundle);
        });

        binding.selectExerciseBtn.setOnClickListener(v -> {
            List<Long> selectedIds = adapter.getSelectedExerciseIds();
            // pass  IDs back to the AddWorkoutFragment via a SharedViewModel
            long[] idArray = selectedIds.stream().mapToLong(l -> l).toArray();

            Bundle result = new Bundle();
            result.putLongArray("selected_ids", idArray);

            // send result back to AddWorkoutFragment
            getParentFragmentManager().setFragmentResult("exercise_selection", result);
            NavHostFragment.findNavController(this).navigateUp();
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ExerciseSelectFragment.this)
                        .navigateUp()
        );

        binding.cancelExerciseSelectBtn.setOnClickListener(v ->
                NavHostFragment.findNavController(ExerciseSelectFragment.this)
                        .navigateUp()
        );
    }

    private void updateSelectionButton(int count) {
        if (count > 0) {
            binding.btnContainer.setVisibility(View.VISIBLE);
            String btnText = "Add " + count + (count == 1 ? " Exercise" : " Exercises");
            binding.selectExerciseBtn.setText(btnText);
        } else {
            binding.btnContainer.setVisibility(View.GONE);
        }
    }

}
