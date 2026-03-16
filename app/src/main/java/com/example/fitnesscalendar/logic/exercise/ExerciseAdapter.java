package com.example.fitnesscalendar.logic.exercise;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.entities.Exercise;
import com.example.fitnesscalendar.relations.FullExerciseRecord;
import com.example.fitnesscalendar.databinding.ListItemExerciseGridBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Setter;

// managing a list of items - helps RecyclerView to draw the items of the list
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<FullExerciseRecord> exercises = new ArrayList<>(); // data source
    private final OnExerciseClickListener listener;

    // listeners - how the Fragment talks to adapter
    private OnInfoClickListener infoListener;
    private OnSelectionChangedListener selectionListener;

    // state - selection logic
    private boolean isSelectionMode = false;
    private final Set<Long> selectedIds = new HashSet<>();

    public void setSelectionMode(boolean mode) {
        this.isSelectionMode = mode;
        notifyDataSetChanged();
    }
    public interface OnInfoClickListener {
        void onInfoClick(long exerciseId);
    }
    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int count);
    }
    public void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.selectionListener = listener;
    }

    public ExerciseAdapter(OnExerciseClickListener listener) {
        this.listener = listener;
    }

    public interface OnExerciseClickListener {
        void onExerciseClick(long exerciseId);
    }


    public void setExercises(List<FullExerciseRecord> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // links XML layout (list_item_exercise_grid) to the Java code
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_exercise_grid, parent, false);
        return new ViewHolder(view);
    }

    // runs for every row that appears on the screen
    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ViewHolder holder, int position) {
        FullExerciseRecord record = exercises.get(position);
        long id = record.exercise.getExerciseId();

        // Bind Text
        holder.binding.exerciseTitle.setText(record.exercise.getTitle());

        // Bind Selection UI (Checkbox)
        holder.binding.exerciseCheckbox.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.binding.exerciseCheckbox.setChecked(selectedIds.contains(id));

        // Use Glide to load the URI from the DB (bind image)
        if (record.exercise.getMediaUri() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(holder.image); // converts uri to a real image
        }  else {
            holder.image.setImageResource(R.drawable.ic_add_photo);
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
            }
        });

        holder.title.setText(record.exercise.getTitle());

        holder.itemView.setOnClickListener(v ->
                listener.onExerciseClick(record.exercise.exerciseId));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    // holds the references to the views for one row
    // ViewHolder - container for a single row in the list
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        CheckBox checkBox;
        final ListItemExerciseGridBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link the Java variables to the IDs in list_item_exercise_grid.xml
            title = itemView.findViewById(R.id.exerciseTitle);
            image = itemView.findViewById(R.id.exerciseImage);
            checkBox = itemView.findViewById(R.id.exerciseCheckbox);
        }

        public ViewHolder(ListItemExerciseGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
