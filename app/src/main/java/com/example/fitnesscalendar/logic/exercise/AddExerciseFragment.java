package com.example.fitnesscalendar.logic.exercise;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.AddExerciseScreenBinding;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class AddExerciseFragment extends Fragment {

    private Long currentUserId;
    private String selectedMediaUri = "";

    private AddExerciseScreenBinding binding;
    private ExerciseViewModel exerciseViewModel;

    private int stepCount = 0;
    private long editingId = -1;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = AddExerciseScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Get the current User ID from the database
        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        exerciseViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.getId();
            }
        });

        binding.exerciseMediaView.setOnClickListener(v -> openGallery());

        binding.addStepButton.setOnClickListener(v -> addNewStep());

        // listener - observes the list of categories from the VM, and display them
//        exerciseViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
//            if (categories != null) {
//                renderCategories(categories);
//            }  else {
//                android.util.Log.d("CHIP_DEBUG", "Categories list is NULL");
//            }
//        });

        exerciseViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                renderCategories(categories);

                //  If edit mode, re-run the chip selection
                if (editingId != -1) {
                    exerciseViewModel.getFullExerciseById(editingId).observe(getViewLifecycleOwner(), record -> {
                        if (record != null && record.categories != null) {
                            List<Long> ids = new ArrayList<>();
                            for (Category c : record.categories) ids.add(c.getId());
                            selectChipsByIds(ids);
                        }
                    });
                }
            }
        });

        editingId = getArguments() != null ? getArguments().getLong("exerciseId", -1) : -1;

        if (editingId != -1) {
            binding.addExerciseTitle.setText("Edit Exercise");
            binding.deleteExerciseButton.setVisibility(View.VISIBLE);

            // Fetch and pre-fill data
            exerciseViewModel.getFullExerciseById(editingId).observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    prefillForm(record);
                }
            });

            binding.deleteExerciseButton.setOnClickListener(v -> showDeleteConfirmation(editingId));
        }

        binding.saveExerciseButton.setOnClickListener(v -> onSaveButtonClicked());

        binding.cancelExerciseButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigateUp();
            }
        );
    }

    private void addNewStep() {
        stepCount++;

        View stepView = getLayoutInflater().inflate(R.layout.new_item_step_row, binding.stepsContainer, false);

        TextView number = stepView.findViewById(R.id.stepNumber);
        number.setText(String.valueOf(stepCount));

        binding.stepsContainer.addView(stepView);
    }

    private void renderCategories(List<Category> categories) {
        binding.categoryChipGroup.removeAllViews(); // no duplicates

        for (Category category : categories) {
            // create the Chip programmatically
            Chip chip = new Chip(requireContext());
            chip.setText(category.getName());

            // store the ID from the database in the View's tag
            chip.setTag(category.getId());

            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipStrokeWidth(2f);
            chip.setChipStrokeColorResource(R.color.button_stroke_colour);
            chip.setChipBackgroundColorResource(android.R.color.transparent);
            chip.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.lexend_font));

            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.chip_selected_orange, null)));
                    chip.setTextColor(getResources().getColor(R.color.chip_selected_orange, null));
                    chip.setChipStrokeWidth(4f);
                } else {
                    chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.button_stroke_colour, null)));
                    chip.setTextColor(getResources().getColor(R.color.text_black_colour, null));
                    chip.setChipStrokeWidth(2f);
                }
            });

            binding.categoryChipGroup.addView(chip);
        }
    }

    private List<Long> getSelectedCategoryIds() {
        List<Long> selectedIds = new ArrayList<>();

        for (int i = 0; i < binding.categoryChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.categoryChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                // retrieve the ID directly from the tag
                Long dbId = (Long) chip.getTag();
                selectedIds.add(dbId);
            }
        }
        return selectedIds;
    }

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    // Persist permissions so the image shows up even after restart
                    requireContext().getContentResolver().takePersistableUriPermission(uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    selectedMediaUri = uri.toString();
                    binding.exerciseMediaView.setImageURI(uri);
                }
            });

    private void openGallery() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());
    }

    // turns DV objects (categories) into clickable UI elements
    private void onSaveButtonClicked() {
        // gather info from screen
        String title = binding.exerciseNameInput.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter an exercise title", Toast.LENGTH_SHORT).show();
            return;
        }

        String description = binding.exerciseDescriptionInput.getText().toString();
        String note = binding.exerciseNotesInput.getText().toString();

        Exercise exercise = new Exercise();
        exercise.setTitle(title);
        exercise.setDescription(description);
        exercise.setNote(note);
        exercise.setMediaUri(selectedMediaUri);

        if (currentUserId != null) {
            exercise.setOwnerId(currentUserId);
            exercise.setUserCreated(true);
        } else {
            Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Step> steps = new ArrayList<>();
        for (int i = 0; i < binding.stepsContainer.getChildCount(); i++) { // loops the row elements (child) in the container
            View stepRow = binding.stepsContainer.getChildAt(i); // gets a particular step row
            EditText input = stepRow.findViewById(R.id.stepInput);

            Step step = new Step();
            step.setDescription(input.getText().toString());
            step.setStepNumber(i + 1);
            steps.add(step);
        }

        List<Long> selectedCategoryIds = getSelectedCategoryIds();
        if (selectedCategoryIds.isEmpty()) {
            Toast.makeText(getContext(), "Please select at least one category", Toast.LENGTH_SHORT).show();
//            binding.exerciseCategoryLabel.setTextColor(Color.RED);
            return;
        }

        if (editingId != -1) {
            exercise.setExerciseId(editingId); // Important: attach the ID so Room knows to update
            exerciseViewModel.updateExercise(exercise, steps, selectedCategoryIds);
        } else {
            exerciseViewModel.saveExercise(exercise, steps, selectedCategoryIds);
        }

        Toast.makeText(getContext(), "Exercise Saved!", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigateUp();
    }

    private void prefillForm(FullExerciseRecord record) {
        binding.exerciseNameInput.setText(record.exercise.getTitle());
        binding.exerciseDescriptionInput.setText(record.exercise.getDescription());
        binding.exerciseNotesInput.setText(record.exercise.getNote());

        if (record.exercise.getMediaUri() != null && !record.exercise.getMediaUri().isEmpty()) {
            this.selectedMediaUri = record.exercise.getMediaUri();
            binding.exerciseMediaView.setImageURI(android.net.Uri.parse(selectedMediaUri));
        }

        binding.stepsContainer.removeAllViews();
        stepCount = 0;
        if (record.steps != null) {
            for (Step step : record.steps) {
                addNewStep();

                View lastStepView = binding.stepsContainer.getChildAt(binding.stepsContainer.getChildCount() - 1);
                EditText input = lastStepView.findViewById(R.id.stepInput);

                if (input != null) {
                    input.setText(step.getDescription());
                }
            }
        }

        if (record.categories != null) {
            List<Long> targetIds = new ArrayList<>();
            for (Category cat : record.categories) {
                targetIds.add(cat.getId());
            }
            selectChipsByIds(targetIds);
        }
    }

    private void selectChipsByIds(List<Long> targetIds) {
        for (int i = 0; i < binding.categoryChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.categoryChipGroup.getChildAt(i);
            Long chipId = (Long) chip.getTag();

            if (targetIds.contains(chipId)) {
                chip.setChecked(true);
            }
        }
    }

    private void showDeleteConfirmation(long id) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Exercise")
                .setMessage("Are you sure you want to delete this exercise? This will also remove it from any workouts.")
                .setPositiveButton("Delete", (dialog, which) -> {

                    exerciseViewModel.deleteExercise(id);

                    Toast.makeText(getContext(), "Exercise deleted", Toast.LENGTH_SHORT).show();

                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_AddExerciseScreen_to_ExercisesList);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
