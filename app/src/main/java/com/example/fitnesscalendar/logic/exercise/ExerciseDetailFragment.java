package com.example.fitnesscalendar.logic.exercise;

import static androidx.core.content.ContextCompat.getColor;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ExerciseDetailScreenBinding;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.google.android.material.chip.Chip;

public class ExerciseDetailFragment extends Fragment {

    private ExerciseDetailScreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ExerciseDetailScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExerciseViewModel viewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

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



        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ExerciseDetailFragment.this)
                        .navigateUp()
        );

        binding.editExerciseButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("exerciseId", exerciseId); // Use the ID currently being viewed
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_ExerciseDetail_to_AddExerciseScreen, bundle);
        });
    }

    private void bindExerciseData(FullExerciseRecord record) {
        binding.exerciseTitle.setText(record.exercise.getTitle());
        binding.exerciseDescriptionText.setText(record.exercise.getDescription());
        binding.exerciseNotesText.setText(record.exercise.getNote());

        if (record.exercise.getMediaUri() != null && !record.exercise.getMediaUri().isEmpty()) {
            Glide.with(this)
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(binding.mainExerciseImage);
        }

        binding.primaryCategoryChipGroup.removeAllViews();

        if (record.categories != null && !record.categories.isEmpty()) {
            for (Category category : record.categories) {
                int bgColour, strokeColour;
                String group = category.getCategoryGroup();

                // Match colors to groups
                switch (group != null ? group : "") {
                    case "TYPE":
                        bgColour = getColor(requireContext(), R.color.exercise_chip_type_bg_colour);
                        strokeColour = getColor(requireContext(), R.color.exercise_chip_type_stroke_colour);
                        break;
                    case "BASIC":
                        bgColour = getColor(requireContext(), R.color.exercise_chip_basic_bg_colour);
                        strokeColour = getColor(requireContext(), R.color.exercise_chip_basic_stroke_colour);
                        break;
                    case "ADVANCED":
                        bgColour = getColor(requireContext(), R.color.exercise_chip_adv_bg_colour);
                        strokeColour = getColor(requireContext(), R.color.exercise_chip_adv_stroke_colour);
                        break;
                    default:
                        bgColour = Color.LTGRAY;
                        strokeColour = Color.GRAY;
                        break;
                }

                binding.primaryCategoryChipGroup.addView(createDetailChip(category, bgColour, strokeColour));
            }
        }

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

        String description = record.exercise.getDescription();
        if (description == null || description.trim().isEmpty()) {
            binding.exerciseDescriptionLabel.setVisibility(View.GONE);
            binding.exerciseDescriptionText.setVisibility(View.GONE);
        } else {
            binding.exerciseDescriptionLabel.setVisibility(View.VISIBLE);
            binding.exerciseDescriptionText.setVisibility(View.VISIBLE);
            binding.exerciseDescriptionText.setText(description);
        }

        String notes = record.exercise.getNote();
        if (notes == null || notes.trim().isEmpty()) {
            binding.exerciseNotesLabel.setVisibility(View.GONE);
            binding.exerciseNotesText.setVisibility(View.GONE);
        } else {
            binding.exerciseNotesLabel.setVisibility(View.VISIBLE);
            binding.exerciseNotesText.setVisibility(View.VISIBLE);
            binding.exerciseNotesText.setText(notes);
        }
    }

    private Chip createDetailChip(Category category, int bgColour, int strokeColour) {
        Chip chip = new Chip(requireContext());
        chip.setText(category.getName());

        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setFocusable(false);

        chip.setChipBackgroundColor(ColorStateList.valueOf(bgColour));
        chip.setChipStrokeColor(ColorStateList.valueOf(strokeColour));
        chip.setChipStrokeWidth(4f);

        chip.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend_font));
        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_black_colour));

        return chip;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
