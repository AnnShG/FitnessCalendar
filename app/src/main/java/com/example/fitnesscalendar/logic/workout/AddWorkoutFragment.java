package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.AddWorkoutScreenBinding;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.logic.exercise.ExerciseViewModel;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class AddWorkoutFragment extends Fragment {

    private AddWorkoutScreenBinding binding;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private Long currentUserId;
    private final List<Long> selectedExerciseIdList = new ArrayList<>();
    private int selectedWorkoutColor = 0xFFFF5722;
    private long existingWorkoutId = -1;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = AddWorkoutScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);

        // DB observation
        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> { // calls lambda everytime the data changes (LiveData)
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.getId();
            }
        });

        // Fragment to fragment communication (exercise selection)
        getParentFragmentManager().setFragmentResultListener("exercise_selection", // registers a listener for a result with the key "exercise_selection"
                getViewLifecycleOwner(), // Ties the listener to the fragment’s view lifecycle
                (requestKey, bundle) -> {
            long[] selectedIds = bundle.getLongArray("selected_ids");
            if (selectedIds != null) {
                selectedExerciseIdList.clear();
                binding.exercisesContainer.removeAllViews();
                loadSelectedExercisesIntoUI(selectedIds);
            }
        });

        setupColourSelection();

        // edit mode screen opened?
        if (getArguments() != null) {
            existingWorkoutId = getArguments().getLong("workoutId", -1);
        }

        if (existingWorkoutId != -1) {
            binding.addWorkoutTitle.setText("Edit Workout");

            binding.deleteWorkoutButton.setVisibility(View.VISIBLE);
            binding.deleteWorkoutButton.setOnClickListener(v -> {
                showDeleteConfirmationDialog();
            });

            workoutViewModel.getFullWorkoutById(existingWorkoutId)
                    .observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    prefillForm(record);
                }
            });
        }

            binding.addExerciseButton.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                // Convert List<Long> to long[] to send in bundle
                long[] existing = selectedExerciseIdList.stream().mapToLong(l -> l).toArray();
                bundle.putLongArray("existing_ids", existing);

            Navigation.findNavController(view)
                    .navigate(R.id.action_AddWorkoutScreen_to_ExerciseSelectScreen, bundle);
             });

        binding.saveWorkoutButton.setOnClickListener(v -> {
            onSaveButtonClicked();
        });

        binding.cancelWorkoutButton.setOnClickListener(v -> {
                    NavHostFragment.findNavController(this)
                            .navigateUp();
                }
        );
    }

    private void setupColourSelection() {
        binding.colorGreen.setOnClickListener(v -> selectColour(0xFF4CAF50, v));
        binding.colorBlue.setOnClickListener(v -> selectColour(0xFF2196F3, v));
        binding.colorPurple.setOnClickListener(v -> selectColour(0xFF9C27B0, v));
        binding.colorRed.setOnClickListener(v -> selectColour(0xFFF44336, v));
        binding.colorDarkBlue.setOnClickListener(v -> selectColour(0xFF3F51B5, v));
        binding.colorGrey.setOnClickListener(v -> selectColour(0xFF888588, v));
        binding.colorYellow.setOnClickListener(v -> selectColour(0xFFF2D607, v));
    }

    private void selectColour(int color, View view) {
        this.selectedWorkoutColor = color; // this will be stored in the Workout table

        View[] colors = {binding.colorGreen, binding.colorBlue, binding.colorPurple,
                binding.colorRed, binding.colorDarkBlue, binding.colorGrey, binding.colorYellow}; // array of clickable views

        // setting transparency to not chosen circles
        for (View v : colors) {
            v.setAlpha(0.5f);
            if (v instanceof ShapeableImageView) {
                ((ShapeableImageView) v).setStrokeWidth(0f);
            }
        }

        view.setAlpha(1.0f); // the one view (v) the user clicked
        if (view instanceof ShapeableImageView) {
            ((ShapeableImageView) view).setStrokeWidth(6f);
            ((ShapeableImageView) view).setStrokeColor(
                    android.content.res.ColorStateList.valueOf(getResources().getColor(R.color.black_colour, null))
            );
        }
    }

    private void loadSelectedExercisesIntoUI(long[] selectedIds) {
        for (int i = 0; i < selectedIds.length; i++) {
            long id = selectedIds[i];
            selectedExerciseIdList.add(id);

            //  async database call
            final int position = i + 1;

            exerciseViewModel.getFullExerciseById(id).observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    inflateExerciseRow(record, position);
                }
            });
        }
    }

    private void inflateExerciseRow(FullExerciseRecord record, int position) {
        View rowView = getLayoutInflater().inflate(R.layout.workout_exercise_item_row, binding.exercisesContainer, false);

        TextView indexTv = rowView.findViewById(R.id.leftSideControlIndex);
        TextView titleTv = rowView.findViewById(R.id.titleLabel);
        ImageView img = rowView.findViewById(R.id.exerciseImage);
        ImageView deleteBtn = rowView.findViewById(R.id.leftSideControlDelete);

        //categories should be here
        indexTv.setText(String.valueOf(position));
        titleTv.setText(record.exercise.getTitle());

        if (record.exercise.getMediaUri() != null) {
            Glide.with(this)
                    .load(record.exercise.getMediaUri())
                    .fitCenter()
                    .into(img);
        }

        deleteBtn.setOnClickListener(v -> {
            binding.exercisesContainer.removeView(rowView);

            Long idToRemove = record.exercise.getExerciseId(); // remove by value
            selectedExerciseIdList.remove(idToRemove);

            recalculateIndices(); //  update 1, 2, 3... labels
        });

        binding.exercisesContainer.addView(rowView);
    }

    private void recalculateIndices() {
        for (int i = 0; i < binding.exercisesContainer.getChildCount(); i++) {
            View child = binding.exercisesContainer.getChildAt(i);
            TextView indexTv = child.findViewById(R.id.leftSideControlIndex);
            if (indexTv != null) indexTv.setText(String.valueOf(i + 1));
        }
    }

    private void onSaveButtonClicked() {
        String title = binding.workoutTitleInput.getText().toString().trim();
        if (title.isEmpty()) {
            binding.workoutTitleInput.setError("Title is required");
            return;
        }

        String Description = binding.workoutDescriptionInput.getText().toString();
        String note = binding.workoutNotesInput.getText().toString();

        Workout workout = new Workout();
        workout.setTitle(title);
        workout.setColour(selectedWorkoutColor);
        workout.setDescription(Description);
        workout.setNote(note);

        workout.setColour(selectedWorkoutColor);

        if (selectedExerciseIdList.isEmpty()) {
            Toast.makeText(getContext(), "Please add at least one exercise to this workout", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentUserId != null) {
            workout.setOwnerId(currentUserId);
            workout.setUserCreated(true);
        } else {
            Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (existingWorkoutId != -1) {
            workout.setWorkoutId(existingWorkoutId); // attach the ID to the object
            workoutViewModel.updateWorkout(workout, selectedExerciseIdList);
            Toast.makeText(getContext(), "Workout Updated!", Toast.LENGTH_SHORT).show();
        } else {
            workoutViewModel.saveWorkout(workout, selectedExerciseIdList);
            Toast.makeText(getContext(), "Workout Saved!", Toast.LENGTH_SHORT).show();
        }

        NavHostFragment.findNavController(this).navigateUp();
    }


    private void prefillForm(FullWorkoutRecord record) {
        binding.workoutTitleInput.setText(record.workout.getTitle());
        binding.workoutDescriptionInput.setText(record.workout.getDescription());
        binding.workoutNotesInput.setText(record.workout.getNote());

        if (record.workout.getColour() != null) {
            int savedColor = record.workout.getColour();
            setupColourSelection();

            View colorView = null;
            if (savedColor == 0xFF4CAF50) colorView = binding.colorGreen;
            else if (savedColor == 0xFF2196F3) colorView = binding.colorBlue;
            else if (savedColor == 0xFF9C27B0) colorView = binding.colorPurple;
            else if (savedColor == 0xFFF44336) colorView = binding.colorRed;
            else if (savedColor == 0xFF3F51B5) colorView = binding.colorDarkBlue;
            else if (savedColor == 0xFF888588) colorView = binding.colorGrey;
            else if (savedColor == 0xFFF2D607) colorView = binding.colorYellow;

            if (colorView != null) {
                selectColour(savedColor, colorView);
            }
        }

        if (record.exercises != null && !record.exercises.isEmpty()) {
//            selectedExerciseIdList.clear();
//            binding.exercisesContainer.removeAllViews();

            if (selectedExerciseIdList.isEmpty()) {
                // Extract IDs from the list of exercises
                long[] exerciseIds = new long[record.exercises.size()];
                for (int i = 0; i < record.exercises.size(); i++) {
                    exerciseIds[i] = record.exercises.get(i).getExerciseId();
                }
                loadSelectedExercisesIntoUI(exerciseIds);
            }
        }
    }

    private void showDeleteConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Workout")
                .setMessage("Are you sure you want to delete this workout? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteCurrentWorkout();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteCurrentWorkout() {
        if (existingWorkoutId != -1) {
            Workout workoutToDelete = new Workout();
            workoutToDelete.setWorkoutId(existingWorkoutId);

            workoutViewModel.deleteWorkout(workoutToDelete);

            Toast.makeText(getContext(), "Workout deleted", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_AddWorkoutScreen_to_WorkoutsList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
