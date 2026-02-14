package com.example.fitnesscalendar.logic.mainApp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lombok.Setter;

/**
 * A custom LinearLayout that represents a calendar view.
 * It handles the display and navigation of a calendar using a RecyclerView.
 */

// class calculates which day the month starts on, how many days are in February, etc.
// It allows you to reuse the "Calendar Logic" in other parts of the app (like a "History" page) without rewriting the math


public class CalendarView extends LinearLayout {
    private final Calendar currentDate = Calendar.getInstance();
    private CalendarHomePageBinding binding;
    private CalendarAdapter adapter;
    private MaterialTextView monthAndYear;
    private final List<String> daysList = new ArrayList<>();


    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // Listener interface for date clicks
    public interface OnDateClickListener {
        void onDateClick(Calendar date);
    }

    @Setter
    private OnDateClickListener onDateClickListener;

    public CalendarView(Context context) {
        this(context, null);
    }


    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Initializes the UI components, inflates the layout, and sets up the RecyclerView.
     */

    private void init(Context context) {
        binding = CalendarHomePageBinding.inflate(LayoutInflater.from(context), this, true);

        // Set up RecyclerView with 7 columns (one for each day of the week)
        adapter = new CalendarAdapter(daysList);
        binding.calendarRecyclerView.setLayoutManager(new GridLayoutManager(context, 7));
        binding.calendarRecyclerView.setAdapter(adapter);

        // Arrow Listeners
        binding.calendarPrevButton.setOnClickListener(v -> moveMonth(-1));
        binding.calendarNextButton.setOnClickListener(v -> moveMonth(1));

        updateCalendar();
    }


    private void moveMonth(int offset) {
        currentDate.add(Calendar.MONTH, offset);
        updateCalendar();
    }

    private void updateCalendar() {
        daysList.clear();

        Calendar cal = (Calendar) currentDate.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        // Calculate leading empty slots (Monday-start logic)
        // Calendar.SUNDAY is 1, Calendar.MONDAY is 2
        int firstDay = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if (firstDay < 0) firstDay = 6; // Wrap Sunday to end

        // Add empty strings for padding
        for (int i = 0; i < firstDay; i++) {
            daysList.add("");
        }

        int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= max; i++) daysList.add(String.valueOf(i));

        // Update Title (e.g., "October 2023")
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthAndYear.setText(sdf.format(currentDate.getTime()));
        adapter.notifyDataSetChanged();
    }

//    private void init(Context context) {
//        // Inflate the layout for this custom view
//        LayoutInflater.from(context).inflate(R.layout.main_calendar_page, this, true);
//
//        // Find view components by ID
//        MaterialButton prev = findViewById(R.id.calendar_prev_button);
//        MaterialButton next = findViewById(R.id.calendar_next_button);
//        monthAndYear = findViewById(R.id.monthAndYear);
//        RecyclerView recyclerView = findViewById(R.id.calendar_recycler_view);
//
//        // Setup the adapter with the internal list
//        adapter = new CalendarAdapter(days);
//        // Calendar is always 7 columns wide
//        recyclerView.setLayoutManager(new GridLayoutManager(context, 7));
//        recyclerView.setAdapter(adapter);
//
//        // Set click listeners for navigation
//        prev.setOnClickListener(v -> changeMonth(-1));
//        next.setOnClickListener(v -> changeMonth(1));
//
//        // Initial render
//        updateCalendar();
//    }

    /**
     * Navigates the calendar forward or backward by the specified number of months.
     */
//    private void changeMonth(int offset) {
//        currentDate.add(Calendar.MONTH, offset);
//        updateCalendar();
//    }

    /**
     * Recalculates the day grid for the current month and updates the UI efficiently.
     */
//    private void updateCalendar() {
//        List<String> newDays = new ArrayList<>();
//
//        Calendar calendar = (Calendar) currentDate.clone();
//        calendar.set(Calendar.DAY_OF_MONTH, 1);
//
//        // Detect first day of the week from user settings (locale)
//        int userFirstDayOfWeek = Calendar.getInstance().getFirstDayOfWeek(); // Sunday = 1, Monday = 2, etc.
//        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK); // Sun = 1, Mon = 2, ...
//
//        // Calculate offset: how many empty slots before first day
//        int offset = firstDayOfMonth - userFirstDayOfWeek;
//        if (offset < 0) offset += 7; // wrap around
//
//        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//        // Add empty slots for previous month
//        for (int i = 0; i < offset; i++) {
//            newDays.add("");
//        }
//
//        // Add actual days
//        for (int i = 1; i <= maxDays; i++) {
//            newDays.add(String.valueOf(i));
//        }
//
//        // Update RecyclerView using DiffUtil
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return days.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newDays.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                return days.get(oldItemPosition).equals(newDays.get(newItemPosition));
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                return days.get(oldItemPosition).equals(newDays.get(newItemPosition));
//            }
//        });
//
//        days.clear();
//        days.addAll(newDays);
//        diffResult.dispatchUpdatesTo(adapter);
//
//        // Update month header
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
//        monthAndYear.setText(sdf.format(currentDate.getTime()));
//    }
}
