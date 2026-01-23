package com.example.fitnesscalendar.ui.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.SurveyPage3Binding;

public class SurveyPage3Fragment extends Fragment {

    private SurveyPage3Binding binding;
    private SurveyViewModel viewModel;
    private String selectedGoal = null;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SurveyPage3Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Shared ViewModel across the Activity
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created

        // Set listener for the "Lose Weight" goal
        binding.cardLoseWeight.setOnClickListener(v -> {
            selectedGoal = getString(R.string.lose_weight);

            // Save the selection to the ViewModel immediately
            viewModel.setGoal(selectedGoal);

            // Update the UI (pass the view that was selected)
            updateSelectionUI(binding.circle1);
        });

        binding.continueButton.setOnClickListener(v -> {
            // Validation: Ensure a goal was selected before moving forward
            if (selectedGoal != null) {
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigate(R.id.action_SurveyPage3_to_SurveyPage4);
            } else {
                // Optional: Show a message to the user to select an option
            }
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigateUp()
        );
    }

    private void updateSelectionUI(View selectedCircle) {
        // Hide all check/selection circles first to "reset" the UI
        binding.circle1.setVisibility(View.INVISIBLE);
        // binding.circle2.setVisibility(View.INVISIBLE); // Add other circles as defined in your XML

        // Make the selected circle visible to provide feedback to the user
        selectedCircle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
