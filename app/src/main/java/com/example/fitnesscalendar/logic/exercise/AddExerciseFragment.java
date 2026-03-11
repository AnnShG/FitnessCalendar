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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.AddExerciseScreenBinding;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.entities.Step;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.NonNull;

public class AddExerciseFragment extends Fragment {

    private Long currentUserId;
    private String selectedMediaUri = "";

    private AddExerciseScreenBinding binding;
    private ExerciseViewModel exerciseViewModel;

    private int stepCount = 0;

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

        setupDynamicCategories();

        binding.exerciseMediaView.setOnClickListener(v -> openGallery());

        binding.addStepButton.setOnClickListener(v -> {
            addNewStep();
        });

        binding.saveExerciseButton.setOnClickListener(v -> {
            onSaveButtonClicked();
        });

        binding.cancelExerciseButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigateUp();
            }
        );
    }

    private void addNewStep() {
        stepCount++;

        View stepView = getLayoutInflater().inflate(R.layout.new_item_step_row, null);

        TextView number = stepView.findViewById(R.id.stepNumber);
        number.setText(String.valueOf(stepCount));

        binding.stepsContainer.addView(stepView);
    }

    private void setupDynamicCategories() {
        List<String> categories = Arrays.asList(
                "Legs", "Arms", "Chest", "Back", "Shoulders", "Core", "Cardio", "Full Body"
        );

        binding.categoryChipGroup.removeAllViews();

        for (String categoryName : categories) {
            // create the Chip programmatically
            Chip chip = new Chip(requireContext());
            chip.setText(categoryName);
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
                    chip.setTextColor(getResources().getColor(R.color.black_colour, null));
                    chip.setChipStrokeWidth(2f);
                }
            });

            binding.categoryChipGroup.addView(chip);
        }
    }

    private List<Long> getSelectedCategoryIds() {
        List<Long> selectedIds = new ArrayList<>();

        // Logic: Map the text of the chip to the Database IDs
        // 1=Legs,2=Arms, 3=Chest, etc.
        for (int i = 0; i < binding.categoryChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) binding.categoryChipGroup.getChildAt(i);
            if (chip.isChecked()) {
                String name = chip.getText().toString();
                // Simple mapping logic
                switch (name) {
                    case "Legs":      selectedIds.add(1L); break;
                    case "Arms":      selectedIds.add(2L); break;
                    case "Chest":     selectedIds.add(3L); break;
                    case "Back":      selectedIds.add(4L); break;
                    case "Shoulders": selectedIds.add(5L); break;
                    case "Core":      selectedIds.add(6L); break;
                    case "Cardio":    selectedIds.add(7L); break;
                    case "Full Body": selectedIds.add(8L); break;
                }
            }
        }
        return selectedIds;
    }

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    // Persist permissions so the image shows up even after restart
                    getContext().getContentResolver().takePersistableUriPermission(uri,
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

        exerciseViewModel.saveExercise(exercise, steps, selectedCategoryIds);

        Toast.makeText(getContext(), "Exercise Saved!", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
