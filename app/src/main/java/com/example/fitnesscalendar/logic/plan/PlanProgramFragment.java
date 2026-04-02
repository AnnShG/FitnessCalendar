package com.example.fitnesscalendar.logic.plan;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.PlanProgramScreenBinding;
import com.example.fitnesscalendar.logic.calendar.CalendarAdapter;
import com.example.fitnesscalendar.logic.calendar.CalendarManager;
import com.example.fitnesscalendar.logic.workout.WorkoutViewModel;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;

public class PlanProgramFragment extends Fragment {

    private PlanProgramScreenBinding binding;
    private CalendarAdapter adapter;
    private WorkoutViewModel workoutViewModel;
    private CalendarManager manager = new CalendarManager();
    private Set<String> highlightedDates = new HashSet<>();
    private final List<String> daysList = new ArrayList<>(); // takes a list of numbers/dates 1,2,3,4

    private long currentSelectedWorkoutId = -1;
    private long currentUserId = -1;
    private boolean isWorkoutSelected = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = PlanProgramScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  listener for date selection
        adapter = new CalendarAdapter(daysList, (position, dayText) -> {
            if (!isWorkoutSelected) {
                Toast.makeText(getContext(), "Please select a workout first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!dayText.isEmpty()) {
                // get the unique key for the date
                String dateKey = manager.getDateKeyForDay(dayText);

                if (highlightedDates.contains(dateKey)) {
                    highlightedDates.remove(dateKey);
                } else {
                    highlightedDates.add(dateKey);
                }

                adapter.setHighlightedDates(highlightedDates, manager);

                updateApplyButtonVisibility();
            }
        });

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.id;
            }
        });

        binding.calendarRecyclerView.setAdapter(adapter);

        updateUI();

        binding.btnApply.setVisibility(View.GONE);
        binding.btnAttachWorkout.setVisibility(View.VISIBLE);

        getParentFragmentManager().setFragmentResultListener("workout_selection", getViewLifecycleOwner(), (requestKey, bundle) -> {
            long workoutId = bundle.getLong("workoutId");
            String workoutTitle = bundle.getString("workoutTitle");
            int workoutColor = bundle.getInt("workoutColor");

            this.isWorkoutSelected = true;

            // Show the "Selected Workout" Card
            binding.selectedWorkoutCard.setVisibility(View.VISIBLE);
            binding.selectedWorkoutTitle.setText("Attaching: " + workoutTitle);
            binding.workoutCircle.setBackgroundTintList(ColorStateList.valueOf(workoutColor));

            // store the ID locally so you can use it when "Apply" is clicked
            this.currentSelectedWorkoutId = workoutId;

            binding.btnAttachWorkout.setText("Replace Workout");

            binding.btnAttachWorkout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.workout_replace_btn_bg_colour, null)));
            binding.btnAttachWorkout.setTextColor(getResources().getColor(R.color.text_black_colour, null));

            if (binding.btnAttachWorkout instanceof com.google.android.material.button.MaterialButton) {
                com.google.android.material.button.MaterialButton mBtn = (com.google.android.material.button.MaterialButton) binding.btnAttachWorkout;
                mBtn.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.workout_replace_btn_stroke_colour, null)));
                mBtn.setStrokeWidth(2);
            }

            updateApplyButtonVisibility();
        });

        binding.calendarPrevButton.setOnClickListener(v -> {
            manager.goToPrevMonth();
            updateUI();
        });

        binding.calendarNextButton.setOnClickListener(v -> {
            manager.goToNextMonth();
            updateUI();
        });

        binding.btnAttachWorkout.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_PlanProgramScreen_to_WorkoutSelectScreen);
        });

        binding.btnApply.setOnClickListener(v -> {
            Set<String> datesToSave = adapter.getHighlightedDateKeys();

            // Now currentUserId will be resolved
            if (currentUserId != -1 && currentSelectedWorkoutId != -1 && !datesToSave.isEmpty()) {

                // Pass the userId to the ViewModel
                workoutViewModel.attachWorkoutToDates(currentUserId, currentSelectedWorkoutId, datesToSave);

                Toast.makeText(getContext(), "Workout successfully scheduled!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_PlanProgramScreen_to_CalendarHomePage);
            } else if (currentSelectedWorkoutId == -1) {
                Toast.makeText(getContext(), "Please select a workout first", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please select at least one date", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI() {
        binding.monthAndYear.setText(manager.getHeaderString());
        List<String> days = manager.getDaysOfMonthList();
        adapter.setDays(days);
    }

    private void updateApplyButtonVisibility() {
        if (isWorkoutSelected && !highlightedDates.isEmpty()) {
            binding.btnApply.setVisibility(View.VISIBLE);
        } else {
            binding.btnApply.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}