package com.example.fitnesscalendar.logic.exercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.AddExerciseScreenBinding;
import com.example.fitnesscalendar.databinding.ProfileScreenBinding;

import lombok.NonNull;

public class AddExerciseFragment extends Fragment {

    private AddExerciseScreenBinding binding;
    private int stepCount = 0;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = AddExerciseScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addNewStep();

        binding.addStepButton.setOnClickListener(v -> {
            addNewStep();
        });
    }

    private void addNewStep() {
        stepCount++;

        View stepView = getLayoutInflater().inflate(R.layout.item_step_row, null);

        TextView number = stepView.findViewById(R.id.stepNumber);
        number.setText(String.valueOf(stepCount));

        binding.stepsContainer.addView(stepView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
