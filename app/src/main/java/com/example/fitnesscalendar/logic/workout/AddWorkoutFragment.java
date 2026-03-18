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

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class AddWorkoutFragment extends Fragment {

    private AddWorkoutScreenBinding binding;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private Long currentUserId;
    private final List<Long> selectedExerciseIdList = new ArrayList<>();


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

        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.getId();
            }
        });

        getParentFragmentManager().setFragmentResultListener("exercise_selection", getViewLifecycleOwner(), (requestKey, bundle) -> {
            long[] selectedIds = bundle.getLongArray("selected_ids");
            if (selectedIds != null) {
                loadSelectedExercisesIntoUI(selectedIds);
            }
        });

            binding.addExerciseButton.setOnClickListener(v -> {// lambda - shorter
            Navigation.findNavController(view)
                    .navigate(R.id.action_AddWorkoutScreen_to_ExerciseSelectScreen);
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

    private void loadSelectedExercisesIntoUI(long[] selectedIds) {
        binding.exercisesContainer.removeAllViews();
        selectedExerciseIdList.clear(); // Clear local list

        for (int i = 0; i < selectedIds.length; i++) {
            long id = selectedIds[i];
            selectedExerciseIdList.add(id); // Store for saving later

            int position = i + 1;
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

        indexTv.setText(String.valueOf(position));
        titleTv.setText(record.exercise.getTitle());

        if (record.exercise.getMediaUri() != null) {
            Glide.with(this).load(record.exercise.getMediaUri()).into(img);
        }

        deleteBtn.setOnClickListener(v -> {
            binding.exercisesContainer.removeView(rowView);
            selectedExerciseIdList.remove(record.exercise.getExerciseId());
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
        String title = binding.workoutTitleInput.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a workout title", Toast.LENGTH_SHORT).show();
            return;
        }

        String Description = binding.workoutDescriptionInput.getText().toString();
        String note = binding.workoutNotesInput.getText().toString();

        Workout workout = new Workout();
        workout.setTitle(title);
        workout.setDescription(Description);
        workout.setNote(note);

        //  Workouts must have a colour for the calendar
        workout.setColour(0xFFFF5722); // for now the default colour - orange

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

        workoutViewModel.saveWorkout(workout, selectedExerciseIdList);

        Toast.makeText(getContext(), "Workout Saved!", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(this).navigateUp();

//        List<Exercise> exercises = new ArrayList<>();
//        for (int i = 0; i < binding.exercisesContainer.getChildCount(); i++) {
//            View exerciseRow = binding.exercisesContainer.getChildAt(i);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
