package com.example.fitnesscalendar.logic.calendar;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.databinding.DailyWorkoutItemBinding;
import com.example.fitnesscalendar.relations.DateColourResult;

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

        holder.binding.workoutTitle.setText(item.title != null ? item.title : "Workout");

        if (item.colour != null) {
            holder.binding.workoutColorDot.setBackgroundTintList(ColorStateList.valueOf(item.colour));
        }

        holder.binding.btnDeleteDaily.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteTask(item);
        });

        holder.binding.workoutCheckbox.setOnCheckedChangeListener(null); // prevent triggering during binding
//         holder.binding.workoutCheckbox.setChecked(item.isCompleted); // Uncomment if you add isCompleted to DB

        holder.binding.workoutCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) listener.onToggleCompletion(item, isChecked);
        });
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
