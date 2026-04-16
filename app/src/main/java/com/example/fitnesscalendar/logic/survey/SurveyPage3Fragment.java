package com.example.fitnesscalendar.logic.survey;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.SurveyPage3Binding;
import com.example.fitnesscalendar.entities.Goal;
import com.example.fitnesscalendar.logic.profile.UserViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SurveyPage3Fragment extends Fragment {

    private SurveyPage3Binding binding;
    private SurveyViewModel surveyViewModel;
    private UserViewModel userViewModel;
    private boolean isEditMode = false;
    private Long currentUserId = -1L;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = SurveyPage3Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Shared ViewModel across the Activity
        surveyViewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        if (getArguments() != null) {
            isEditMode = getArguments().getBoolean("isEditMode", false);
        }

        if (isEditMode) {
            binding.textView.setText("Edit goals");
            binding.survey3Root.setBackgroundResource(R.color.home_page_background_colour);
            binding.continueButton.setText("Save");
            binding.continueButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_black_colour));
        }


        // --- GOAL SELECTION LOGIC ---
        binding.option1.setOnClickListener(v -> toggleGoalSelection(binding.option1, binding.circle1, "Lose Weight"));
        binding.option2.setOnClickListener(v -> toggleGoalSelection(binding.option2, binding.circle2, "Build Muscle"));
        binding.option3.setOnClickListener(v -> toggleGoalSelection(binding.option3, binding.circle3, "Get Stronger"));
        binding.option4.setOnClickListener(v -> toggleGoalSelection(binding.option4, binding.circle4, "Stay Fit"));
        binding.option5.setOnClickListener(v -> toggleGoalSelection(binding.option5, binding.circle5, "Recover after injury"));
        binding.option6.setOnClickListener(v -> toggleGoalSelection(binding.option6, binding.circle6, "Stay active"));

// --- STATE RESTORATION ---
        if (surveyViewModel.getCustomGoal() != null) {
            binding.userInputGoal.setText(surveyViewModel.getCustomGoal());
        }

        // Restoration logic: loop through the saved goals and highlight them
        for (String goal : surveyViewModel.getSelectedGoals()) {
            restoreSelectionUI(goal);
        }

        // --- NAVIGATION ---
        binding.continueButton.setOnClickListener(v -> {
            if (isEditMode) {
                saveGoalsToDatabase();
            } else {
                // 1. Get the text from the EditText
                String userTypedGoal = binding.userInputGoal.getText().toString().trim();

                // 2. Save it to the ViewModel
                surveyViewModel.setCustomGoal(userTypedGoal);

                // 3. Validation: Check if they picked a card OR typed something
                if (surveyViewModel.getSelectedGoals().isEmpty() && userTypedGoal.isEmpty()) {
                    Toast.makeText(requireContext(), "Please select a goal or write your own", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to Page 4
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_SurveyPage3_to_SurveyPage4);
                }
            }
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigateUp()
        );

        // if the ViewModel has no goals yet, load them from the actual User Data
        userViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.id;

                if (surveyViewModel.getSelectedGoals().isEmpty()) {
                    Set<String> existingTitles = new HashSet<>();
                    String existingCustomGoal = null;

                    for (Goal g : userWithGoals.goals) {
                        if (g.isCustom()) {
                            existingCustomGoal = g.getGoalTitle();
                        } else {
                            existingTitles.add(g.getGoalTitle());
                        }
                    }

                    surveyViewModel.setSelectedGoals(existingTitles);

                    if (existingCustomGoal != null) {
                        surveyViewModel.setCustomGoal(existingCustomGoal);
                        binding.userInputGoal.setText(existingCustomGoal);
                    }

                    refreshCardHighlights();
                }
            }
        });
    }

    private void toggleGoalSelection(MaterialCardView card, android.widget.ImageView circle, String goalValue) {
        // 1. Tell the ViewModel to add/remove this goal
        surveyViewModel.toggleGoal(goalValue);

        // 2. Update the UI based on whether it is now selected
        boolean isSelected = surveyViewModel.getSelectedGoals().contains(goalValue);

        int orange = getResources().getColor(R.color.chip_selected_orange, null);
        int gray = getResources().getColor(R.color.button_stroke_colour, null);

        if (isSelected) {
            card.setStrokeColor(ColorStateList.valueOf(orange));
            card.setStrokeWidth(4);
            circle.setImageResource(R.drawable.survey_circle_selected); // Use your checkmark/orange circle
        } else {
            card.setStrokeColor(ColorStateList.valueOf(gray));
            card.setStrokeWidth(2);
            circle.setImageResource(R.drawable.survey_circle_unselected);
        }
    }

    private void updateCardUI(MaterialCardView card, android.widget.ImageView circle) {
        int orange = getResources().getColor(R.color.chip_selected_orange, null);
        card.setStrokeColor(ColorStateList.valueOf(orange));
        card.setStrokeWidth(4);
        circle.setImageResource(R.drawable.survey_circle_selected);
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

    private void refreshCardHighlights() {
        for (String goal : surveyViewModel.getSelectedGoals()) {
            restoreSelectionUI(goal);
        }
    }

    private void saveGoalsToDatabase() {
        List<String> selectedTitles = new ArrayList<>(surveyViewModel.getSelectedGoals());
        String customGoal = binding.userInputGoal.getText().toString().trim();

        if (currentUserId != null && currentUserId != -1) {
            userViewModel.updateUserGoals(currentUserId, selectedTitles, customGoal);

            Toast.makeText(getContext(), "Goals updated!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        } else {
            Toast.makeText(getContext(), "Error: User session lost", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
