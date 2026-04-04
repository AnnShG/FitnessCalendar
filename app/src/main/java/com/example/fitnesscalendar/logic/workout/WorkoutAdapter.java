package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.WorkoutsListItemGridBinding;
import com.example.fitnesscalendar.entities.Workout;
import com.example.fitnesscalendar.relations.FullWorkoutRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * WorkoutAdapter handles the display of workout cards in a list: browse and selection modes
 */
public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private List<FullWorkoutRecord> workouts = new ArrayList<>(); // The data source
    private Long selectedWorkoutId = null; // Tracks selected workout ID

    private boolean isSelectionMode = false;

    private OnInfoClickListener infoListener;
    private OnSelectionChangedListener selectionListener;

    public interface OnInfoClickListener { // handles clicks on the 'Eye' icon
        void onInfoClick(long workoutId); // this method must be implemented in the fragment
    }
    public interface OnSelectionChangedListener { // listener on changes how many exes were selected
        void onSelectionChanged(int count);
    }

    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }

    // Updates the dataset and refreshes the entire list
    public void setWorkouts(List<FullWorkoutRecord> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    public void setSelectionMode(boolean mode) {
        this.isSelectionMode = mode;
        notifyDataSetChanged();
    }

    // retrieves the actual Workout object for the currently selected item
    // used when the user confirms workout selection
    public Workout getSelectedWorkout() {
        if (selectedWorkoutId == null) return null;

        for (FullWorkoutRecord record : workouts) {
            if (record.workout.getWorkoutId().equals(selectedWorkoutId)) {
                return record.workout;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WorkoutsListItemGridBinding binding = WorkoutsListItemGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // binds the workout data to the UI and applies conditional styling based on selection state
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FullWorkoutRecord record = workouts.get(position);
        long id = record.workout.getWorkoutId();

        // extract the Workout entity from the record
        Workout workout = record.workout;

        holder.binding.workoutTitle.setText(workout.getTitle());
        holder.binding.workoutDescription.setText(workout.getDescription());

        // update the indicator bar colour
        if (workout.getColour() != null) {
            holder.binding.workoutIndicatorBar.setBackgroundTintList(
                    ColorStateList.valueOf(workout.getColour()));
        }

        // the Eye is displayed only in "Selection Mode"
        int selectionVisibility = isSelectionMode ? View.VISIBLE : View.GONE;
        holder.binding.btnViewDetails.setVisibility(selectionVisibility);

        // Eye icon - click logic
        holder.binding.btnViewDetails.setOnClickListener(v -> {
            if (infoListener != null) infoListener.onInfoClick(id);
        });

        boolean isSelected = Objects.equals(id, selectedWorkoutId);

        if (isSelected) {
            // orange stroke and "Shine"
            holder.binding.getRoot().setStrokeColor(ColorStateList.valueOf(
                    holder.itemView.getContext().getResources().getColor(R.color.chip_selected_orange, null)));
            holder.binding.getRoot().setStrokeWidth(6);
            holder.binding.getRoot().setCardElevation(15f); // shine effect
        } else {
            // default Look
            holder.binding.getRoot().setStrokeColor(ColorStateList.valueOf(Color.TRANSPARENT));
            holder.binding.getRoot().setStrokeWidth(2);
            holder.binding.getRoot().setCardElevation(4f);
        }

        // Entire item - selection logic
        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                if (Objects.equals(selectedWorkoutId, id)) {
                    selectedWorkoutId = null;
                } else {
                    selectedWorkoutId = id;
                }
                notifyDataSetChanged(); // refresh the UI for new borders

                if (selectionListener != null) {
                    selectionListener.onSelectionChanged(selectedWorkoutId != null ? 1 : 0);
                }
            } else {
                // Logic for the original list (Browse mode)
                // If not selecting, a click on the card also opens details
                if (infoListener != null) infoListener.onInfoClick(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    // Pre-selects specific workouts.
    public void setSelectedIds(List<Long> existingIds) {
        if (existingIds != null && !existingIds.isEmpty()) {
            this.selectedWorkoutId = existingIds.get(0);
            notifyDataSetChanged();

            // Trigger the listener to update the "Add X Workout" button text
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(1);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final WorkoutsListItemGridBinding binding;
        ViewHolder(WorkoutsListItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
