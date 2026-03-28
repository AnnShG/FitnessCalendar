package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.databinding.WorkoutsListItemGridBinding;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.logic.exercise.ExerciseAdapter;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private List<FullWorkoutRecord> workouts = new ArrayList<>();
    private final Set<Long> selectedIds = new HashSet<>();

    private boolean isSelectionMode = false;
//    private final OnWorkoutClickListener listener;

    private OnInfoClickListener infoListener;
    private OnSelectionChangedListener selectionListener;

//    public interface OnWorkoutClickListener {
//        void onWorkoutClick(long workoutId);
//    }
    public interface OnInfoClickListener { // listener on eye
        void onInfoClick(long workoutId); // this method must be implemented in the fragment
    }
    public interface OnSelectionChangedListener { // listener on changes how many exes were selected
        void onSelectionChanged(int count);
    }
//
//    public WorkoutAdapter(OnWorkoutClickListener listener) {
//        this.listener = listener;
//    }

    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }

    public void setWorkouts(List<FullWorkoutRecord> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean mode) {
        this.isSelectionMode = mode;
        notifyDataSetChanged();
    }

    public List<Long> getSelectedWorkoutIds() {
        return new ArrayList<>(selectedIds);
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
        FullWorkoutRecord record = workouts.get(position);
        long id = record.workout.getWorkoutId();

        // extract the Workout entity from the record
        Workout workout = record.workout;

        holder.binding.workoutTitle.setText(workout.getTitle());
        holder.binding.workoutDescription.setText(workout.getDescription());

        if (workout.getColour() != null) {
            holder.binding.workoutIndicatorBar.setBackgroundTintList(
                    ColorStateList.valueOf(workout.getColour()));
        }

//        holder.itemView.setOnClickListener(v -> listener.onWorkoutClick(workout.getWorkoutId()));

        // Eye icon - click logic
        holder.binding.btnViewDetails.setOnClickListener(v -> {
            if (infoListener != null) infoListener.onInfoClick(id);
        });

        // Entire item - selection logic
        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                if (selectedIds.contains(id)) {
                    selectedIds.remove(id);
                } else {
                    selectedIds.add(id);
                }
                notifyItemChanged(position); // Refresh only this item
                if (selectionListener != null) {
                    selectionListener.onSelectionChanged(selectedIds.size());
                }
            } else {
                // Logic for the original list (Browse mode)
                // If not selecting, a click on the row also opens details
                if (infoListener != null) infoListener.onInfoClick(id);
            }
        });
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
