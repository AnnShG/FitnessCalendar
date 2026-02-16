package com.example.fitnesscalendar.logic.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lombok.NonNull;

// directly interacts with the UI - change the month view (arrows) and title, indicated the touched day
public class CalendarHomePageFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private CalendarHomePageBinding binding;
    private final Calendar currentDate = Calendar.getInstance();
    private final List<String> daysList = new ArrayList<>(); // takes a list of numbers/dates 1,2,3,4
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

        binding.calendarPrevButton.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1);
            updateCalendarUI();
        });

        binding.calendarNextButton.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1);
            updateCalendarUI();
        });

        binding.mindsetButton.setOnClickListener(v -> {
            MindsetDialog dialog = new MindsetDialog();
            dialog.show(getParentFragmentManager(), "MindsetDialog");
        });

        updateCalendarUI();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()) {
            String message = "Selected Date: " + dayText + " " +
                    new SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                            .format(currentDate.getTime());

            android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCalendarUI() {
        daysList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        binding.monthAndYear.setText(sdf.format(currentDate.getTime()));

        Calendar cal = (Calendar) currentDate.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDayOfWeek < 0) firstDayOfWeek = 6;

        for (int i = 0; i < firstDayOfWeek; i++) daysList.add("");

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
