package com.example.fitnesscalendar.logic.exercise;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExerciseDetailScreenBinding;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.google.android.material.chip.Chip;

public class ExerciseDetailFragment extends Fragment {

    private ExerciseDetailScreenBinding binding;
    private ExerciseViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ExerciseDetailScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        // Get the ID passed from the List Screen
        long exerciseId = getArguments() != null ? getArguments().getLong("exerciseId") : -1;

        if (exerciseId != -1) {
            // Observe the Full Record (Exercise + Steps + Categories)
            viewModel.getFullExerciseById(exerciseId).observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    System.out.println("DEBUG: Category Count = " + record.categories.size());
                    bindExerciseData(record);
                }
            });
        }
    }

    private void bindExerciseData(FullExerciseRecord record) {
        binding.addExerciseTitle.setText(record.exercise.getTitle());
        binding.exerciseDescriptionText.setText(record.exercise.getDescription());
        binding.exerciseNotesText.setText(record.exercise.getNote());

        if (record.exercise.getMediaUri() != null && !record.exercise.getMediaUri().isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(binding.mainExerciseImage);
        }

        binding.categoryChipGroup.removeAllViews();
        if (record.categories != null && !record.categories.isEmpty()) {
            for (Category category : record.categories) {
                Chip chip = new Chip(requireContext());
                chip.setText(category.getName());

                chip.setCheckable(false);
                chip.setClickable(false);
                chip.setChipStrokeWidth(2f);
                chip.setChipStrokeColorResource(R.color.button_stroke_colour);
                chip.setChipBackgroundColorResource(android.R.color.transparent);
                chip.setTextColor(getResources().getColor(R.color.black_colour, null));

                binding.categoryChipGroup.addView(chip);
            }
        }

//        binding.editExerciseButton.setOnClickListener(v -> {
//            // Logic to navigate to AddExerciseFragment with the ID for editing
//        });
//
//        binding.deleteExerciseButton.setOnClickListener(v -> {
//            // Logic to call viewModel.delete(record.exercise) and navigateUp()
//        });

        binding.stepsContainer.removeAllViews();
        if (record.steps != null) {
            for (int i = 0; i < record.steps.size(); i++) {
                View stepRow = getLayoutInflater().inflate(R.layout.existing_item_step_row, binding.stepsContainer, false);

                TextView tvNumber = stepRow.findViewById(R.id.stepNumber);
                TextView tvText = stepRow.findViewById(R.id.stepText);

                tvNumber.setText(String.valueOf(i + 1));
                tvText.setText(record.steps.get(i).getDescription());

                binding.stepsContainer.addView(stepRow);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
