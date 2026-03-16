package com.example.fitnesscalendar.logic.exercise;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.relations.FullExerciseRecord;

import java.util.ArrayList;
import java.util.List;

// managing a list of items, ViewHolder - container for a single row in the list
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private List<FullExerciseRecord> exercises = new ArrayList<>();
    private final OnExerciseClickListener listener;

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
        holder.title.setText(record.exercise.getTitle());

        // Use Glide to load the URI from the DB
        if (record.exercise.getMediaUri() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(record.exercise.getMediaUri()))
                    .into(holder.image); // converts uri to a real image
        }  else {
            holder.image.setImageResource(R.drawable.ic_add_photo);
        }

        holder.itemView.setOnClickListener(v ->
                listener.onExerciseClick(record.exercise.exerciseId));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    // holds the references to the views for one row
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Link the Java variables to the IDs in list_item_exercise_grid.xml
            title = itemView.findViewById(R.id.exerciseTitle);
            image = itemView.findViewById(R.id.exerciseImage);
        }
    }
}
