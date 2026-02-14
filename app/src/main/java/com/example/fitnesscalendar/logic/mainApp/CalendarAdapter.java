package com.example.fitnesscalendar.logic.mainApp;

import android.graphics.Color; // Used to define colors programmatically
import android.view.Gravity; // Used to align text within the view
import android.view.View; // Base class for all UI components
import android.view.ViewGroup; // Base class for view containers and LayoutParams
import android.widget.TextView; // UI component to display text

import androidx.annotation.NonNull; // Annotation to indicate a parameter cannot be null
import androidx.recyclerview.widget.RecyclerView; // Base library for efficient list rendering

import java.util.List; // Java utility for handling lists of data

/**
 * Adapter for the Calendar RecyclerView.
 * This class handles the creation and binding of views for each day in the calendar grid.
 */

//Data Mapper
//    Takes your list of dates (1, 2, 3...) and maps them to the day_view.xml
//    This is where is said "If the user has a workout on the 5th, show the CircularStatusView."
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    // Internal list to hold the strings representing each day (e.g., "1", "2", or "")
    private final List<String> daysOfMonth;
    private OnItemListener onItemListener; // 1. Add Listener

    // Define the interface inside the Adapter class
    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }

    public CalendarAdapter(List<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }


    /**
     * Constructor to initialize the adapter with a list of days.
     * @param daysOfMonth The data source for the calendar grid.
     */
    public CalendarAdapter(List<String> daysOfMonth) {
        this.daysOfMonth = daysOfMonth; // Assign the provided list to the local variable
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new TextView programmatically for each day cell
        TextView dayText = new TextView(parent.getContext());

        // Define layout parameters to ensure the cell fills the grid column width with a fixed height
        dayText.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Width fills the 1/7th of the grid
                120 // Fixed height of 120 pixels for the calendar row
        ));

        // Center the text both horizontally and vertically within the cell
        dayText.setGravity(Gravity.CENTER);

        // Set the font size for the day number
        dayText.setTextSize(14);

        // Set the text color to black
        dayText.setTextColor(Color.BLACK);

        // Wrap the created TextView in a ViewHolder and return it
        return new CalendarViewHolder(dayText);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);

        // Set the click listener on the entire item view
        holder.itemView.setOnClickListener(v -> {
            if (onItemListener != null) {
                onItemListener.onItemClick(position, day);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items to be displayed in the RecyclerView grid
        return daysOfMonth.size();
    }

    /**
     * ViewHolder class to hold the views for each calendar day item.
     */
//    Holds the references to day_text and circular_status_view for a single day cell so the RecyclerView doesn't have to call findViewById repeatedly.
    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        // Reference to the TextView displaying the day number
        public final TextView dayOfMonth;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView); // Pass the view to the base RecyclerView.ViewHolder class

            // Since we created the TextView programmatically in onCreateViewHolder,
            // the itemView itself IS the TextView.
            this.dayOfMonth = (TextView) itemView;
        }
    }
}