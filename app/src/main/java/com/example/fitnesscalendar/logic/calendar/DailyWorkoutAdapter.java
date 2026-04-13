package com.example.fitnesscalendar.logic.calendar;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.databinding.DailyWorkoutItemBinding;
import com.example.fitnesscalendar.relations.DateColourResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the workouts displayed inside the sliding daily plan window.
 * It maps DateColourResult data to the daily_workout_item layout.
 */
public class DailyWorkoutAdapter extends RecyclerView.Adapter<DailyWorkoutAdapter.ViewHolder> {

    private List<DateColourResult> items = new ArrayList<>();
    private final OnDailyTaskActionListener listener;

    /**
     * Interface to communicate delete and completion actions back to the Fragment.
     */
    public interface OnDailyTaskActionListener {
        void onDeleteTask(DateColourResult item);
        void onToggleCompletion(DateColourResult item, boolean isCompleted);
        void onTitleClick(long workoutId);
    }

    public DailyWorkoutAdapter(OnDailyTaskActionListener listener) {
        this.listener = listener;
    }

    public void setWorkouts(List<DateColourResult> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DailyWorkoutItemBinding binding = DailyWorkoutItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateColourResult item = items.get(position);

        holder.binding.workoutTitle.setText(item.title);

        if (item.colour != null) {
            holder.binding.workoutColorDot.setBackgroundTintList(ColorStateList.valueOf(item.colour));
        }

        holder.binding.workoutTitle.setOnClickListener(v -> {
            if (listener != null) listener.onTitleClick(item.workoutId);
        });
        holder.binding.btnDeleteDaily.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteTask(item);
        });

        // Reset listener to null before setting checked state to avoid unwanted triggers
        holder.binding.workoutCheckbox.setOnCheckedChangeListener(null);
        holder.binding.workoutCheckbox.setChecked(item.isCompleted);

        // --- restriction to complete future workouts ---
        long todayEpoch = LocalDate.now().toEpochDay();
        boolean isFuture = item.date > todayEpoch;

        if (isFuture) {
            holder.binding.workoutCheckbox.setEnabled(false);
            holder.binding.workoutCheckbox.setAlpha(0.5f); // Visual cue that it's disabled
        } else {
            holder.binding.workoutCheckbox.setEnabled(true);
            holder.binding.workoutCheckbox.setAlpha(1.0f);
        }

        holder.binding.workoutCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onToggleCompletion(item, isChecked);
            }
        });

        if (item.isCompleted) {
            holder.binding.getRoot().setCardBackgroundColor(Color.parseColor("#F5F5F5"));
            holder.binding.workoutTitle.setTextColor(Color.GRAY);
            holder.binding.workoutColorDot.setAlpha(0.3f);
        } else {
            holder.binding.getRoot().setCardBackgroundColor(Color.WHITE);
            holder.binding.workoutTitle.setTextColor(Color.BLACK);
            holder.binding.workoutColorDot.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final DailyWorkoutItemBinding binding;

        public ViewHolder(@NonNull DailyWorkoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
