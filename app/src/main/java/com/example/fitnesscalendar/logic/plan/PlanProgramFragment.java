package com.example.fitnesscalendar.logic.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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

        binding.calendarPrevButton.setOnClickListener(v -> {
            manager.goToPrevMonth();
            updateUI();
        });

        binding.calendarNextButton.setOnClickListener(v -> {
            manager.goToNextMonth();
            updateUI();
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
