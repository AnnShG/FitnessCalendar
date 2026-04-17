package com.example.fitnesscalendar.logic.graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fitnesscalendar.databinding.GraphsScreenBinding;
import com.example.fitnesscalendar.logic.calendar.CalendarManager;
import com.example.fitnesscalendar.logic.workout.WorkoutViewModel;

import lombok.NonNull;

public class GraphScreenFragment extends Fragment {

    private GraphsScreenBinding binding;
    private WorkoutViewModel workoutViewModel;
    private CalendarManager calendarManager = new CalendarManager();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = GraphsScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);

        updateUI();

        binding.btnPrevMonth.setOnClickListener(v -> {
            calendarManager.goToPrevMonth();
            updateUI();
        });

        binding.btnNextMonth.setOnClickListener(v -> {
            calendarManager.goToNextMonth();
            updateUI();
        });
    }

    private void updateUI() {
        // date string updating
        binding.currentMonthText.setText(calendarManager.getHeaderString(false));

        long start = calendarManager.getStartOfMonthEpochDay();
        long end = calendarManager.getEndOfMonthEpochDay();

        workoutViewModel.getMonthlyStats(start, end).observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                calculateAndShowProgress(stats.first, stats.second);
            }
        });
    }

    private void calculateAndShowProgress(int total, int completed) {
        if (total == 0) {
            binding.disciplineProgressBar.setProgress(0, true);
            binding.tvDisciplinePercent.setText("0%");
            binding.tvDisciplineDescription.setText("No workouts scheduled for this month. Go to the calendar to plan your training!");
            return;
        }

        int percent = (completed * 100) / total;

        binding.disciplineProgressBar.setProgress(percent, true);
        binding.tvDisciplinePercent.setText(percent + "%");

        String description;
        if (percent == 100) {
            description = "Perfect! All planned workouts are completed. You're on fire!";
        } else if (percent >= 50) {
            description = String.format("You have completed %d out of %d workouts. More than halfway there!", completed, total);
        } else if (percent > 0) {
            description = String.format("You've done %d workouts. Every step counts, keep moving!", completed, total);
        } else {
            description = String.format("You have %d workouts planned. Time to get started!", total);
        }
        binding.tvDisciplineDescription.setText(description);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
