package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.databinding.WorkoutsListItemGridBinding;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;

import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private List<FullWorkoutRecord> workouts = new ArrayList<>();
    private final OnWorkoutClickListener listener;

    public interface OnWorkoutClickListener {
        void onWorkoutClick(long workoutId);
    }

    public WorkoutAdapter(OnWorkoutClickListener listener) {
        this.listener = listener;
    }

    public void setWorkouts(List<FullWorkoutRecord> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkoutsListItemGridBinding binding = WorkoutsListItemGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 1. Get the Full Record
        FullWorkoutRecord record = workouts.get(position);
        // 2. Extract the Workout entity from the record
        Workout workout = record.workout;

        holder.binding.workoutTitle.setText(workout.getTitle());
        holder.binding.workoutDescription.setText(workout.getDescription());

        if (workout.getColour() != null) {
            holder.binding.workoutIndicatorBar.setBackgroundTintList(
                    ColorStateList.valueOf(workout.getColour()));
        }

        holder.itemView.setOnClickListener(v -> listener.onWorkoutClick(workout.getWorkoutId()));
    }


    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final WorkoutsListItemGridBinding binding;
        ViewHolder(WorkoutsListItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
