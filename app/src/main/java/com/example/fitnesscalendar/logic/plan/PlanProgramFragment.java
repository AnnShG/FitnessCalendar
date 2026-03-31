package com.example.fitnesscalendar.logic.plan;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.PlanProgramScreenBinding;
import com.example.fitnesscalendar.logic.calendar.CalendarAdapter;
import com.example.fitnesscalendar.logic.calendar.CalendarManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;

public class PlanProgramFragment extends Fragment {

    private PlanProgramScreenBinding binding;
    private CalendarAdapter adapter;

    private CalendarManager manager = new CalendarManager();
    private Set<String> highlightedDates = new HashSet<>();
    private final List<String> daysList = new ArrayList<>(); // takes a list of numbers/dates 1,2,3,4


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
            if (!dayText.isEmpty()) {
                // get the unique key for the date
                String dateKey = manager.getDateKeyForDay(dayText);

                if (highlightedDates.contains(dateKey)) {
                    highlightedDates.remove(dateKey);
                } else {
                    highlightedDates.add(dateKey);
                }

                adapter.setHighlightedDates(highlightedDates, manager);

                if (!highlightedDates.isEmpty()) {
                    binding.btnApply.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.calendarRecyclerView.setAdapter(adapter);

        updateUI();

        getParentFragmentManager().setFragmentResultListener("workout_selection", getViewLifecycleOwner(), (requestKey, bundle) -> {
            long workoutId = bundle.getLong("workoutId");
            String workoutName = bundle.getString("workoutName");
            int workoutColor = bundle.getInt("workoutColor");

            // 2. Show the "Selected Workout" Card
            binding.selectedWorkoutCard.setVisibility(View.VISIBLE);
            binding.selectedWorkoutTitle.setText("Attaching: " + workoutName);

            // 3. Update the circle
            binding.workoutCircle.setBackgroundTintList(ColorStateList.valueOf(workoutColor));

            // 4. Store the ID locally so you can use it when "Apply" is clicked
            this.currentSelectedWorkoutId = workoutId;

            binding.btnAttachWorkout.setVisibility(View.GONE);
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
            // 1. Get the set of highlighted dates from your adapter
            Set<String> datesToSave = adapter.getHighlightedDateKeys();

            if (datesToSave.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one date", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Logic to save to DB (using your CalendarDay and CrossRef logic)
            workoutViewModel.attachWorkoutToDates(currentSelectedWorkoutId, datesToSave);

            // 3. Reset UI or navigate back
            Toast.makeText(getContext(), "Program Applied!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        });
//
//        Calendar cal = manager.getCurrentCalendar();
//        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayText));
//        Date selectedDate = cal.getTime(); // Now you have the Date to save in DB!

    }

    private void updateUI() {
        binding.monthAndYear.setText(manager.getHeaderString());
        List<String> days = manager.getDaysOfMonthList();
        adapter.setDays(days);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
