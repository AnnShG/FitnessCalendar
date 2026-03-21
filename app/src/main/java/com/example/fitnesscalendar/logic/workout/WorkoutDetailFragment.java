package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesscalendar.databinding.WorkoutDetailScreenBinding;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.example.fitnesscalendar.R;

public class WorkoutDetailFragment extends Fragment {

    private WorkoutDetailScreenBinding binding;
    protected WorkoutViewModel workoutViewModel;
    private WorkoutGalleryAdapter galleryAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WorkoutDetailScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        galleryAdapter = new WorkoutGalleryAdapter(exerciseId -> {
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", exerciseId);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutsDetail_to_ExerciseDetail, bundle);
        });

        binding.rvExerciseGallery.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvExerciseGallery.setAdapter(galleryAdapter);

        //  Workout ID from Navigation Arguments
        long workoutId = getArguments() != null ? getArguments().getLong("workoutId", -1) : -1;

        if (workoutId != -1) {
            workoutViewModel.getFullWorkoutById(workoutId).observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    bindWorkoutData(record);
                }
            });
        }

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.editWorkoutButton.setOnClickListener(v -> {
            Bundle editBundle = new Bundle(); // key-value pair
            // Pass the workoutId that is currently viewing
            editBundle.putLong("workoutId", workoutId);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutsDetail_to_AddWorkoutScreen, editBundle);
        });
    }

    private void bindWorkoutData(FullWorkoutRecord record) {
        binding.addWorkoutTitle.setText(record.workout.getTitle());

        String countText = record.exercises.size() + " " +
                (record.exercises.size() == 1 ? "Exercise" : "Exercises");
        binding.exerciseCount.setText(countText);

        if (record.workout.getColour() != null) {
            binding.colourIndicator.setBackgroundTintList(
                    ColorStateList.valueOf(record.workout.getColour()));
        }

        String description = record.workout.getDescription();
        if (description == null || description.trim().isEmpty()) {
            binding.workoutDescription.setVisibility(View.GONE);
            binding.workoutDescriptionText.setVisibility(View.GONE);
        } else {
            binding.workoutDescription.setVisibility(View.VISIBLE);
            binding.workoutDescriptionText.setVisibility(View.VISIBLE);
            binding.workoutDescriptionText.setText(description);
        }

        String notes = record.workout.getNote();
        if (notes == null || notes.trim().isEmpty()) {
            binding.workoutNotes.setVisibility(View.GONE);
            binding.workoutNotesText.setVisibility(View.GONE);
        } else {
            binding.workoutNotes.setVisibility(View.VISIBLE);
            binding.workoutNotesText.setVisibility(View.VISIBLE);
            binding.workoutNotesText.setText(notes);
        }

        galleryAdapter.setExercises(record.exercises);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
