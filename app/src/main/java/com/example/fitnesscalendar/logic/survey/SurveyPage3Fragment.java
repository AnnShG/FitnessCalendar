package com.example.fitnesscalendar.logic.survey;

import android.content.res.ColorStateList;
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
import com.google.android.material.card.MaterialCardView;

public class SurveyPage3Fragment extends Fragment {

    private SurveyPage3Binding binding;
    private SurveyViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = SurveyPage3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Shared ViewModel across the Activity
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created

        // --- GOAL SELECTION LOGIC ---
        binding.option1.setOnClickListener(v -> toggleGoalSelection(binding.option1, binding.circle1, "Lose Weight"));
        binding.option2.setOnClickListener(v -> toggleGoalSelection(binding.option2, binding.circle2, "Build Muscle"));
        binding.option3.setOnClickListener(v -> toggleGoalSelection(binding.option3, binding.circle3, "Get Stronger"));
        binding.option4.setOnClickListener(v -> toggleGoalSelection(binding.option4, binding.circle4, "Stay Fit"));
        binding.option5.setOnClickListener(v -> toggleGoalSelection(binding.option5, binding.circle5, "Recover after injury"));
        binding.option6.setOnClickListener(v -> toggleGoalSelection(binding.option6, binding.circle6, "Stay active"));


        // Restoration logic: loop through the saved goals and highlight them
        for (String goal : viewModel.getSelectedGoals()) {
            restoreSelectionUI(goal);
        }

        // --- NAVIGATION ---
        binding.continueButton.setOnClickListener(v -> {
            // 1. Get the custom goal from the EditText (if any)
            String customGoal = "";
            if (binding.userInputGoal.getText() != null) {
                customGoal = binding.userInputGoal.getText().toString().trim();
            }

            // 2. Validation: Check if both selections AND text field are empty
            if (viewModel.getSelectedGoals().isEmpty() && customGoal.isEmpty())  {
                android.widget.Toast.makeText(requireContext(),
                        "Please select at least one goal or write your own",
                        android.widget.Toast.LENGTH_SHORT).show();
            } else {
                if (!customGoal.isEmpty()) {
                    viewModel.toggleGoal(customGoal);
                }

                NavHostFragment.findNavController(this).navigate(R.id.action_SurveyPage3_to_SurveyPage4);
            }
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigateUp()
        );
    }

    private void toggleGoalSelection(MaterialCardView card, android.widget.ImageView circle, String goalValue) {
        // 1. Tell the ViewModel to add/remove this goal
        viewModel.toggleGoal(goalValue);

        // 2. Update the UI based on whether it is now selected
        boolean isSelected = viewModel.getSelectedGoals().contains(goalValue);

        int orange = getResources().getColor(R.color.chip_selected_orange, null);
        int gray = getResources().getColor(R.color.button_stroke_colour, null);

        if (isSelected) {
            card.setStrokeColor(ColorStateList.valueOf(orange));
            card.setStrokeWidth(4);
            circle.setImageResource(R.drawable.circle_selected); // Use your checkmark/orange circle
        } else {
            card.setStrokeColor(ColorStateList.valueOf(gray));
            card.setStrokeWidth(2);
            circle.setImageResource(R.drawable.circle_unselected);
        }
    }

    private void updateCardUI(MaterialCardView card, android.widget.ImageView circle) {
        int orange = getResources().getColor(R.color.chip_selected_orange, null);
        card.setStrokeColor(ColorStateList.valueOf(orange));
        card.setStrokeWidth(4);
        circle.setImageResource(R.drawable.circle_selected);
        circle.setImageTintList(ColorStateList.valueOf(orange));
    }



    private void restoreSelectionUI(String goal) {
        switch (goal) {
            case "Lose Weight": updateCardUI(binding.option1, binding.circle1); break;
            case "Build Muscle": updateCardUI(binding.option2, binding.circle2); break;
            case "Get Stronger": updateCardUI(binding.option3, binding.circle3); break;
            case "Stay Fit": updateCardUI(binding.option4, binding.circle4); break;
            case "Recover after injury": updateCardUI(binding.option5, binding.circle5); break;
            case "Stay active": updateCardUI(binding.option6, binding.circle6); break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
