package com.example.fitnesscalendar.logic.exercise;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.example.fitnesscalendar.databinding.ListItemExerciseGridBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// managing a list of items - helps RecyclerView to draw the items of the list
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<FullExerciseRecord> exercises = new ArrayList<>(); // data source
    // state - selection logic
    private boolean isSelectionMode = false;
    private final Set<Long> selectedIds = new HashSet<>();
//    private final OnExerciseClickListener listener;

    // listeners - how the Fragment talks to adapter
    private OnInfoClickListener infoListener;
    private OnSelectionChangedListener selectionListener;

    public interface OnInfoClickListener {
        void onInfoClick(long exerciseId); // this method must be implemented in the fragment
    }
    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }
    public interface OnExerciseClickListener {
        void onExerciseClick(long exerciseId);
    }

    // set method allows the fragment to pass logic into the adapter
    // listener on eye
    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }
    public void setExercises(List<FullExerciseRecord> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }
    public void setSelectionMode(boolean mode) {
        this.isSelectionMode = mode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // links XML layout (list_item_exercise_grid) to the Java code
        ListItemExerciseGridBinding binding = ListItemExerciseGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // runs for every row that appears on the screen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FullExerciseRecord record = exercises.get(position);
        long id = record.exercise.getExerciseId();

        // Bind Text
        holder.binding.exerciseTitle.setText(record.exercise.getTitle());

        // We only show the Eye and Checkbox if we are in "Selection Mode"
        int selectionVisibility = isSelectionMode ? View.VISIBLE : View.GONE;

        // Bind Selection UI (Checkbox) and eye
        holder.binding.exerciseCheckbox.setVisibility(selectionVisibility);
        holder.binding.btnViewDetails.setVisibility(selectionVisibility);

        holder.binding.exerciseCheckbox.setChecked(selectedIds.contains(id));

        // Use Glide to load the URI from the DB (bind image)
        if (record.exercise.getMediaUri() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(holder.binding.exerciseImage); // converts uri to a real image
        }  else {
            holder.binding.exerciseImage.setImageResource(R.drawable.ic_add_photo);
        }

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
        return exercises.size();
    }

    // holds the references to the views for one row
    // ViewHolder - container for a single row in the list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ListItemExerciseGridBinding binding;

        public ViewHolder(ListItemExerciseGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
