//package com.example.fitnesscalendar.logic.exercise;
//
//import android.net.Uri;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.fitnesscalendar.relations.FullExerciseRecord;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
//    private List<FullExerciseRecord> exercises = new ArrayList<>();
//    private OnExerciseClickListener listener;
//
//    public interface OnExerciseClickListener {
//        void onExerciseClick(long exerciseId);
//    }
//
//    public ExerciseAdapter(OnExerciseClickListener listener) {
//        this.listener = listener;
//    }
//
//    public void setExercises(List<FullExerciseRecord> exercises) {
//        this.exercises = exercises;
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        FullExerciseRecord record = exercises.get(position);
//        holder.title.setText(record.exercise.getTitle());
//
//        // Use Glide to load the URI from the DB
//        if (record.exercise.getMediaUri() != null) {
//            Glide.with(holder.itemView.getContext())
//                    .load(Uri.parse(record.exercise.getMediaUri()))
//                    .into(holder.image);
//        }
//
//        holder.itemView.setOnClickListener(v ->
//                listener.onExerciseClick(record.exercise.exerciseId));
//    }
//
//
//}
