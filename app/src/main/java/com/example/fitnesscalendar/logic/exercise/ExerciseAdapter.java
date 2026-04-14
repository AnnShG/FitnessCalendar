package com.example.fitnesscalendar.logic.exercise;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.example.fitnesscalendar.databinding.ExerciseListItemGridBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// managing a list of items - helps RecyclerView to draw the items of the list - takes data raw from DB and translates it
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<FullExerciseRecord> displayedExercises = new ArrayList<>(); // current filtered list
    private final Set<Long> selectedIds = new HashSet<>(); // contains selected unique! ids on selection list
    private boolean isSelectionMode = false; // switch between simple list and selection list

    // listeners - how the Fragment talks to adapter
    private OnInfoClickListener infoListener;
    private OnSelectionChangedListener selectionListener;

    public interface OnInfoClickListener { // listener on eye
        void onInfoClick(long exerciseId); // this method must be implemented in the fragment
    }
    public interface OnSelectionChangedListener { // listener on changes how many exes were selected
        void onSelectionChanged(int count);
    }

    // set method allows the fragment to pass logic into the adapter
    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }
    public void setAllExercises(List<FullExerciseRecord> newList) {
        this.displayedExercises = newList;
        notifyDataSetChanged();
    }
    public void setSelectionMode(boolean mode) {
        this.isSelectionMode = mode; // shared field
        notifyDataSetChanged();
    }

    public List<Long> getSelectedExerciseIds() {
        return new ArrayList<>(selectedIds);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // turns XML layout (list_item_exercise_grid) into Java object - ViewHolder
        ExerciseListItemGridBinding binding = ExerciseListItemGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // runs for every row that appears on the screen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        FullExerciseRecord record = allExercises.get(position);
        FullExerciseRecord record = displayedExercises.get(position);
        long id = record.exercise.getExerciseId();
        String imageUri = record.exercise.getMediaUri();

        // Bind Text
        holder.binding.exerciseTitle.setText(record.exercise.getTitle());

        // Use Glide to load the URI from the DB (bind image)
        if (record.exercise.getMediaUri() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(holder.binding.exerciseImage); // converts uri to a real image
        }  else {
            holder.binding.exerciseImage.setImageResource(R.drawable.ic_add_photo);
        }

        // the Eye is displayed only in "Selection Mode"
        holder.binding.btnViewDetails.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.binding.btnViewDetails.setOnClickListener(v -> { // system event - finger touched the screen
            if (infoListener != null) infoListener.onInfoClick(id); // my event - finger touched the eye - passed id
        });

        boolean isSelected = selectedIds.contains(id);
        if (isSelected) {
            // Active State
            holder.binding.getRoot().setStrokeColor(ColorStateList.valueOf(Color.parseColor("#FF8C00"))); // Orange
            holder.binding.getRoot().setStrokeWidth(6);
            holder.binding.getRoot().setCardElevation(20f);
        } else {
            // Inactive State
            holder.binding.getRoot().setStrokeWidth(2);
            holder.binding.getRoot().setStrokeColor(ColorStateList.valueOf(Color.TRANSPARENT));
            holder.binding.getRoot().setCardElevation(2f);
        }

        // Entire item - selection logic
        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                if (selectedIds.contains(id)) {
                    selectedIds.remove(id);
                } else { // if doesn't contain
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
        return displayedExercises.size();
    }

    public void setSelectedIds(List<Long> existingIds) {
        if (existingIds != null) {
            this.selectedIds.clear();
            this.selectedIds.addAll(existingIds);
            notifyDataSetChanged();

            // Trigger the listener to update the "Add X Exercises" button text
            if (selectionListener != null) {
                selectionListener.onSelectionChanged(selectedIds.size());
            }
        }
    }

    // holds the references to the views for one row
    // ViewHolder - container for a single row in the list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ExerciseListItemGridBinding binding;

        public ViewHolder(ExerciseListItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
