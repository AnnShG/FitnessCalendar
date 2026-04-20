package com.example.fitnesscalendar.logic.workout;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
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
 * WorkoutGalleryAdapter manages a grid list of exercise media within the Workout Details screen.
 */
public class WorkoutGalleryAdapter extends RecyclerView.Adapter<WorkoutGalleryAdapter.ViewHolder> {

    private List<Exercise> exercises = new ArrayList<>(); // The data source: a list of Exercise entity
    // listeners to communicate clicks back to the fragment
    private final OnImageClickListener imageListener;
    private OnInfoClickListener infoListener;

    public interface OnImageClickListener { // handles full-screen image expansion
        void onImageClick(String uri);
    }
    public interface OnInfoClickListener { // listener on eye
        void onInfoClick(long exerciseId);
    }

    public WorkoutGalleryAdapter(OnImageClickListener listener) {
        this.imageListener = listener;
    }

    public void setExercises(List<Exercise> exercises) { // Updates the exercise list and refreshes the gallery
        this.exercises = exercises;
        notifyDataSetChanged();
    }
    public void setOnInfoClickListener(OnInfoClickListener listener) {
        this.infoListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewBinding for the gallery item
        WorkoutItemGalleryImageBinding binding = WorkoutItemGalleryImageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        long id = exercise.getExerciseId();
        String mediaUri = exercise.getMediaUri();

        holder.binding.exerciseTitle.setText(exercise.getTitle());

        if (mediaUri != null && !mediaUri.isEmpty()) {
            Uri uri = Uri.parse(mediaUri);
            if (isVideoUri(holder.itemView.getContext(), uri)) {
                // Video handling: show VideoView and play on loop
                holder.binding.exerciseMedia.setVisibility(View.GONE);
                holder.binding.exerciseVideo.setVisibility(View.VISIBLE);
                holder.binding.exerciseVideo.setVideoURI(uri);
                holder.binding.exerciseVideo.setOnPreparedListener(mp -> {
                    mp.setLooping(true);
                    holder.binding.exerciseVideo.start();
                });
            } else {
                holder.binding.exerciseVideo.setVisibility(View.GONE);
                holder.binding.exerciseMedia.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext())
                        .load(uri)
                        .fitCenter()
                        .placeholder(R.drawable.ic_add_photo)
                        .into(holder.binding.exerciseMedia);
            }
        } else {
            holder.binding.exerciseVideo.setVisibility(View.GONE);
            holder.binding.exerciseMedia.setVisibility(View.VISIBLE);
            holder.binding.exerciseMedia.setImageResource(R.drawable.ic_add_photo);
        }

        holder.binding.btnViewDetails.setOnClickListener(v -> {
            if (infoListener != null) infoListener.onInfoClick(id);
        });

        // detect clicks for full screen
        holder.itemView.setOnClickListener(v -> {
            if (imageListener != null) imageListener.onImageClick(mediaUri);
        });
    }

    private boolean isVideoUri(Context context, Uri uri) {
        String type = context.getContentResolver().getType(uri);
        return type != null && type.startsWith("video/");
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
