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

/**
 * WorkoutGalleryAdapter manages a grid list of exercise images within the Workout Details screen.
 */
public class WorkoutGalleryAdapter extends RecyclerView.Adapter<WorkoutGalleryAdapter.ViewHolder> {

    // The data source: a list of Exercise entities containing titles and media URIs
    private List<Exercise> exercises = new ArrayList<>();

    // Listeners to communicate clicks back to the Fragment
    private final OnImageClickListener imageListener;
    private OnInfoClickListener infoListener;

//    Interface to handle full-screen image expansion
    public interface OnImageClickListener {
        void onImageClick(String uri);
    }
    public interface OnInfoClickListener { // listener on eye
        void onInfoClick(long exerciseId);
    }

    public WorkoutGalleryAdapter(OnImageClickListener listener) {
        this.imageListener = listener;
    }

    // Updates the exercise list and refreshes the gallery
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }
    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
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
        long id = exercise.getExerciseId();
        String imageUri = exercise.getMediaUri();

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

        holder.binding.btnViewDetails.setOnClickListener(v -> {
            if (infoListener != null) infoListener.onInfoClick(id);
        });

//        Image Click: Opens Full Screen Image
        holder.binding.exerciseMedia.setOnClickListener(v -> {
            if (imageListener != null) imageListener.onImageClick(imageUri);
        });
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final WorkoutItemGalleryImageBinding binding;
        ViewHolder(WorkoutItemGalleryImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
