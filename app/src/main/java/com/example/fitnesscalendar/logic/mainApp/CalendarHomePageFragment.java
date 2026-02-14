package com.example.fitnesscalendar.logic.mainApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;

public class CalendarHomePageFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private CalendarHomePageBinding binding;
    private final Calendar currentDate = Calendar.getInstance();
    private final List<String> daysList = new ArrayList<>();
    private CalendarAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = CalendarHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 'v' represents the View that was clicked
//        binding.plusButton.setOnClickListener(v -> {
//            // Create the PopupMenu anchored to the plusButton
//            PopupMenu popup = new PopupMenu(requireContext(), v);
//            popup.getMenuInflater().inflate(R.menu.plus_dropdown_menu, popup.getMenu());
//            // Handle menu item clicks
//            popup.setOnMenuItemClickListener(item -> {
//                // Example: if (item.getItemId() == R.id.add_activity) { ... }
//                return true;
//            });
//            popup.show();
//        });

        adapter = new CalendarAdapter(daysList, this);
        binding.calendarRecyclerView.setAdapter(adapter);

        // 2. Setup Arrows
        binding.calendarPrevButton.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1);
            updateCalendarUI();
        });

        binding.calendarNextButton.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1);
            updateCalendarUI();
        });

        // 3. Initial Fill
        updateCalendarUI();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()) {
            // This is where you will add logic to open a detail view
            String message = "Selected Date: " + dayText + " " +
                    new SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                            .format(currentDate.getTime());

            android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show();

            // Example for future:
            // openDateDetailFragment(dayText, currentDate.get(Calendar.MONTH), currentDate.get(Calendar.YEAR));
        }
    }

    private void updateCalendarUI() {
        daysList.clear();

        // Calculate Month/Year text
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        binding.monthAndYear.setText(sdf.format(currentDate.getTime()));

        // Calculate Days
        Calendar cal = (Calendar) currentDate.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // Monday Start Logic
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDayOfWeek < 0) firstDayOfWeek = 6;

        for (int i = 0; i < firstDayOfWeek; i++) daysList.add(""); // Empty cells

        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) daysList.add(String.valueOf(i));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
