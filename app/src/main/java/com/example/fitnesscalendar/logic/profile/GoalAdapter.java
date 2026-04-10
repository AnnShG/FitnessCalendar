package com.example.fitnesscalendar.logic.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.databinding.ItemGoalBinding;
import com.example.fitnesscalendar.entities.Goal;

import java.util.ArrayList;
import java.util.List;

// Adapter is a part of a Recycler system - dynamically creates that frames that may fit the screen
public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private OnGoalEditListener listener;

    private List<Goal> goals = new ArrayList<>();

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    @FunctionalInterface
    public interface OnGoalEditListener {
        void onEditClick(Goal goal);
    }

    //  constructor to receive the listener
    public GoalAdapter(OnGoalEditListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    // builds new rows (frames) for adding the data to the screen
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGoalBinding binding = ItemGoalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new GoalViewHolder(binding);
    }

    @Override
    // Fills the frames (rows) with the goal
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal currentGoal = goals.get(position);
        holder.binding.goalText.setText(currentGoal.getGoalTitle());

        // Visibility logic
        holder.binding.editGoalButton.setVisibility(currentGoal.isCustom() ? View.VISIBLE : View.GONE);

        // Set the click listener on the pencil icon
        holder.binding.editGoalButton.setOnClickListener(v -> {
            listener.onEditClick(currentGoal);
        });

        // Logic to show edit button only for custom goals
        if (currentGoal.isCustom()) {
            holder.binding.editGoalButton.setVisibility(View.VISIBLE);
        } else {
            holder.binding.editGoalButton.setVisibility(View.GONE);
        }
    }

    @Override
    // Calculates how many rows the RecyclerView needs to draw
    public int getItemCount() {
        return goals.size();
    }

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        public final ItemGoalBinding binding;

        public GoalViewHolder(ItemGoalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
