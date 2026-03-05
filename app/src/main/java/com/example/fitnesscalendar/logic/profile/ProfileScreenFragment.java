package com.example.fitnesscalendar.logic.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.ProfileScreenBinding;

import lombok.NonNull;

public class ProfileScreenFragment extends Fragment {

    private ProfileScreenBinding binding;
    private GoalAdapter goalAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ProfileScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goalAdapter = new GoalAdapter();
        // Setup the RecyclerView
        // they run as soon as the screen is ready
        binding.goalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.goalsRecyclerView.setAdapter(goalAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
