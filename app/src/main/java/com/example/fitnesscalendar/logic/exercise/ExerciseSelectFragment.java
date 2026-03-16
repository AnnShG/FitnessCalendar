package com.example.fitnesscalendar.logic.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExercisesSelectScreenBinding;

public class ExerciseSelectFragment extends ExercisesListFragment {

    private ExercisesSelectScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ExercisesSelectScreenBinding.inflate(inflater, container, false);
        super.root = binding.getRoot(); // Pass the root to the parent
        return super.root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // shared viewModel
        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        // super - parent - sets up the recyclerView and Adapter
        super.onViewCreated(view, savedInstanceState);

        if (adapter != null) {
            adapter.setSelectionMode(true); // Tell adapter to show checkboxes
            adapter.setOnSelectionChangedListener(count -> {
                if (count > 0) {
                    binding.buttonContainer.setVisibility(View.VISIBLE);
                    String btnText = "Add " + count + (count == 1 ? " Exercise" : " Exercises");
                    binding.selectExerciseButton.setText(btnText);
                } else {
                    binding.buttonContainer.setVisibility(View.GONE);
                }
            });
        }

        // This triggers the navigation to details
        adapter.setOnInfoClickListener(exerciseId -> {
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", exerciseId);
            // Ensure this action exists in your nav_graph.xml
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_ExerciseSelectScreen_to_ExerciseDetail, bundle);
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ExerciseSelectFragment.this)
                        .navigateUp()
        );
    }

}
