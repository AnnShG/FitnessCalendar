package com.example.fitnesscalendar.logic.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExercisesListScreenBinding;

import lombok.NonNull;

public class ExercisesListFragment extends Fragment {

    public ExercisesListScreenBinding binding;
    private ExerciseViewModel exerciseViewModel;
    private Long currentUserId;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ExercisesListScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

//        // 1. Setup the Adapter
//        // NOTE: Make sure your ExerciseAdapter accepts List<ExerciseSummary> or List<FullExerciseRecord>
//        ExerciseAdapter adapter = new ExerciseAdapter(id -> {
//            Bundle bundle = new Bundle();
//            bundle.putLong("exerciseId", id);
//
//            // Check your nav_graph.xml for the correct ID.
//            // It might be action_navigationExercises_to_exerciseItemFragment
//            NavHostFragment.findNavController(this)
//                    .navigate(R.id.action_exercisesList_to_exerciseDetail, bundle);
//        });
//
//        // 2. Fix the LayoutManager (Use standard GridLayoutManager)
//        binding.exercisesRecyclerView.setLayoutManager(new androidx.leanback.widget.GridLayoutManager(getContext(), 2));
//        binding.exercisesRecyclerView.setAdapter(adapter);
//        binding.exercisesRecyclerView.setNestedScrollingEnabled(false);
//
//        // 3. Observe the Data
//        // Using the "Full Record" path we established earlier
//        exerciseViewModel.getAllFullExerciseRecords().observe(getViewLifecycleOwner(), exercises -> {
//            if (exercises != null) {
//                adapter.setExercises(exercises);
//
//                // Now .size() will work because 'exercises' is the List inside the LiveData
//                String countText = exercises.size() + " Exercises Found";
//                binding.filteredExercises.setText(countText);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
