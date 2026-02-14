package com.example.fitnesscalendar.logic.mainApp;

import android.graphics.Color; // Used to define colors programmatically
import android.view.Gravity; // Used to align text within the view
import android.view.View; // Base class for all UI components
import android.view.ViewGroup; // Base class for view containers and LayoutParams
import android.widget.TextView; // UI component to display text

import androidx.annotation.NonNull; // Annotation to indicate a parameter cannot be null
import androidx.recyclerview.widget.RecyclerView; // Base library for efficient list rendering

import java.util.List; // Java utility for handling lists of data

//Data Mapper or a Translator
//    Takes the list of dates and maps them to the day_view - into the little boxes on the calendar
// fills the boxes with the dates (1,2,3,4) into MaterialTextView
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<String> daysOfMonth;
    private OnItemListener onItemListener;

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

    // create a day bow for every number
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView dayText = new TextView(parent.getContext());

        dayText.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, // Width fills the 1/7th of the grid
                120 // Fixed height of 120 pixels for the calendar row
        ));

        dayText.setGravity(Gravity.CENTER);

        dayText.setTextSize(20);

        dayText.setTextColor(Color.BLACK);

        // Wrap the created TextView in a ViewHolder and return it
        return new CalendarViewHolder(dayText);
    }

    // make the box with the number be touchable
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);

        holder.itemView.setOnClickListener(v -> {
            if (onItemListener != null) {
                onItemListener.onItemClick(position, day);
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    /**
     * ViewHolder class to hold the views for each calendar day item.
     */
    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        public final TextView dayOfMonth;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            this.dayOfMonth = (TextView) itemView;
        }
    }
}