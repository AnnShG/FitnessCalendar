package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExercisesListScreenBinding;
import com.example.fitnesscalendar.databinding.WorkoutsListScreenBinding;

import lombok.NonNull;

public class WorkoutsListFragment extends Fragment {
    protected WorkoutsListScreenBinding binding;
    protected WorkoutViewModel workoutViewModel;
    protected WorkoutAdapter workoutAdapter;
    protected View root;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            binding = WorkoutsListScreenBinding.inflate(inflater, container, false);
            root = binding.getRoot();
        } else {
            binding = WorkoutsListScreenBinding.bind(root);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        workoutAdapter = new WorkoutAdapter(id -> {
            Bundle bundle = new Bundle();
            bundle.putLong("workoutId", id);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutsList_to_WorkoutsDetail, bundle);
        });

        binding.workoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.workoutsRecyclerView.setAdapter(workoutAdapter);
        binding.workoutsRecyclerView.setNestedScrollingEnabled(false);

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp());

        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                long userId = userWithGoals.user.id;

                workoutViewModel.getFullWorkoutRecords(userId).observe(getViewLifecycleOwner(), list -> {
                    if (list != null) {
                        workoutAdapter.setWorkouts(list);
                        binding.filteredWorkouts.setText(list.size() + " Workouts Found");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
