package com.example.fitnesscalendar.logic.calendar;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Data Mapper or a Translator
//    Takes the list of dates and maps them to the day_view - into the little boxes on the calendar
// fills the boxes with the dates (1,2,3,4) into MaterialTextView
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<String> daysOfMonth;
    private OnItemListener onItemListener;
    private Set<String> highlightedDates = new HashSet<>();
    private CalendarManager calendarManager;

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }

    public CalendarAdapter(List<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    public void setHighlightedDates(Set<String> dates, CalendarManager manager) {
        this.highlightedDates = dates;
        this.calendarManager = manager;
        notifyDataSetChanged();
    }

    public void setDays(List<String> days) {
        this.daysOfMonth.clear();
        this.daysOfMonth.addAll(days);
        notifyDataSetChanged();
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

        return new CalendarViewHolder(dayText);
    }


    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = daysOfMonth.get(position);
        holder.dayOfMonth.setText(day);

        // make the box with the number be touchable
        holder.itemView.setOnClickListener(v -> {
            if (onItemListener != null) {
                onItemListener.onItemClick(position, day);
            }
        });

        //  grey circle highlight on the calendar
        if (calendarManager != null && !day.isEmpty()) {
            String dateKey = calendarManager.getDateKeyForDay(day);
            if (highlightedDates.contains(dateKey)) {
                holder.dayOfMonth.setBackgroundResource(R.drawable.plan_selected_day_circle);
            } else {
                holder.dayOfMonth.setBackground(null);
            }
        } else {
            holder.dayOfMonth.setBackground(null);
        }
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