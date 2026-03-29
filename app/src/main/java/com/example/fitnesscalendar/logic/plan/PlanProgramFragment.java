package com.example.fitnesscalendar.logic.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.databinding.AddExerciseScreenBinding;
import com.example.fitnesscalendar.databinding.PlanProgramScreenBinding;

import lombok.NonNull;

public class PlanProgramFragment extends Fragment {

    private PlanProgramScreenBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = PlanProgramScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
