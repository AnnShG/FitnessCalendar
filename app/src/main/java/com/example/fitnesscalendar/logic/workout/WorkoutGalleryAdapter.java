package com.example.fitnesscalendar.logic.workout;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.WorkoutItemGalleryImageBinding;
import com.example.fitnesscalendar.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class WorkoutGalleryAdapter extends RecyclerView.Adapter<WorkoutGalleryAdapter.ViewHolder> {
    private List<Exercise> exercises = new ArrayList<>();

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use ViewBinding for the gallery item
        WorkoutItemGalleryImageBinding binding = WorkoutItemGalleryImageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        holder.binding.exerciseTitle.setText(exercise.getTitle());

        if (exercise.getMediaUri() != null && !exercise.getMediaUri().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(exercise.getMediaUri()))
                    .fitCenter()
                    .placeholder(R.drawable.ic_add_photo)
                    .into(holder.binding.exerciseMedia);
        } else {
            holder.binding.exerciseMedia.setImageResource(R.drawable.ic_add_photo);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final WorkoutItemGalleryImageBinding binding;
        ViewHolder(WorkoutItemGalleryImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
