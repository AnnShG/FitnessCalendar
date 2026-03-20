package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.databinding.WorkoutDetailScreenBinding;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;

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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(WorkoutDetailFragment.this)
                        .navigateUp()
        );
    }

    private void bindWorkoutData(FullWorkoutRecord record) {
        binding.addWorkoutTitle.setText(record.workout.getTitle());
        binding.workoutDescription.setText(record.workout.getDescription());
        binding.workoutNotes.setText(record.workout.getNote());

        String countLabel = record.exercises.size() + " " +
                (record.exercises.size() == 1 ? "Exercise" : "Exercises");
        binding.exerciseCount.setText(countLabel);

        if (record.workout.getColour() != null) {
            binding.colourIndicator.setBackgroundTintList(
                    ColorStateList.valueOf(record.workout.getColour()));
        }

        galleryAdapter.setExercises(record.exercises);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
