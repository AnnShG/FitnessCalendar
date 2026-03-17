package com.example.fitnesscalendar.logic.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;


import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExercisesListScreenBinding;

import lombok.NonNull;

public class ExercisesListFragment extends Fragment {

    public ExercisesListScreenBinding binding;
    protected ExerciseAdapter adapter;
    protected ExerciseViewModel exerciseViewModel;
    private Long currentUserId;
    protected View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            // Only inflate the default parent binding if the child hasn't set 'root' yet
            com.example.fitnesscalendar.databinding.ExercisesListScreenBinding parentBinding =
                    ExercisesListScreenBinding.inflate(inflater, container, false);
            root = parentBinding.getRoot();
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        // navigation to details logic
        adapter.setOnInfoClickListener(id -> { // lambda defines what happens when the users taps the item
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", id);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_ExercisesList_to_ExerciseDetail, bundle); // take bundle (envelope) with ex id
        });

        binding.exercisesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.exercisesRecyclerView.setAdapter(adapter);
        binding.exercisesRecyclerView.setNestedScrollingEnabled(false); // prevents list scrolling from stuck

        // Observe the Data
        exerciseViewModel.getAllFullExerciseRecords().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null) {
                adapter.setExercises(exercises); //redrawing the screen to refresh the list of the exes on the screen

                String countText = exercises.size() + " Exercises Found";
                binding.filteredExercises.setText(countText);
            }
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ExercisesListFragment.this)
                        .navigateUp()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
