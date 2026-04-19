package com.example.fitnesscalendar.logic.survey;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.fitnesscalendar.repository.UserRepository;
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
            binding.backButton.setVisibility(View.INVISIBLE);
        }

        // --- GOAL SELECTION LOGIC ---
        binding.option1.setOnClickListener(v -> toggleGoalSelection(binding.option1, binding.circle1, UserRepository.GOAL_LOSE_WEIGHT));
        binding.option2.setOnClickListener(v -> toggleGoalSelection(binding.option2, binding.circle2, UserRepository.GOAL_BUILD_MUSCLE));
        binding.option3.setOnClickListener(v -> toggleGoalSelection(binding.option3, binding.circle3, UserRepository.GOAL_GET_STRONGER));
        binding.option4.setOnClickListener(v -> toggleGoalSelection(binding.option4, binding.circle4, UserRepository.GOAL_STAY_FIT));
        binding.option5.setOnClickListener(v -> toggleGoalSelection(binding.option5, binding.circle5, UserRepository.GOAL_RECOVER_INJURY));
        binding.option6.setOnClickListener(v -> toggleGoalSelection(binding.option6, binding.circle6, UserRepository.GOAL_STAY_ACTIVE));

        // --- STATE RESTORATION ---
        if (surveyViewModel.getCustomGoal() != null) {
            binding.userInputGoal.setText(surveyViewModel.getCustomGoal());
        }

        // Listen for real-time changes to the custom goal to prevent data loss when navigating away
        binding.userInputGoal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the ViewModel immediately as the user types
                surveyViewModel.setCustomGoal(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        restoreSelectionUI();

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
                if (surveyViewModel.getSelectedGoals().isEmpty()) {
                    Toast.makeText(requireContext(), "Please select an option goal", Toast.LENGTH_SHORT).show();
                } else {
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

                if (isEditMode) {
                    surveyViewModel.getSelectedGoals().clear();

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

    /**
     * Toggles the selection state of a goal in the ViewModel and updates the UI.
     */
    private void toggleGoalSelection(MaterialCardView card, ImageView circle, String goalValue) {
        // 1. Tell the ViewModel to add/remove this goal
        surveyViewModel.toggleGoal(goalValue);

        // 2. Update the UI based on whether it is now selected
        boolean isSelected = surveyViewModel.getSelectedGoals().contains(goalValue);
        updateState(card, circle, isSelected);
    }

    /**
     * Refreshes the selection UI for all goal options based on the current state in the ViewModel.
     */
    private void restoreSelectionUI() {
        Set<String> selected = surveyViewModel.getSelectedGoals();

        updateState(binding.option1, binding.circle1, selected.contains(UserRepository.GOAL_LOSE_WEIGHT));
        updateState(binding.option2, binding.circle2, selected.contains(UserRepository.GOAL_BUILD_MUSCLE));
        updateState(binding.option3, binding.circle3, selected.contains(UserRepository.GOAL_GET_STRONGER));
        updateState(binding.option4, binding.circle4, selected.contains(UserRepository.GOAL_STAY_FIT));
        updateState(binding.option5, binding.circle5, selected.contains(UserRepository.GOAL_RECOVER_INJURY));
        updateState(binding.option6, binding.circle6, selected.contains(UserRepository.GOAL_STAY_ACTIVE));
    }

    private void refreshCardHighlights() {
        restoreSelectionUI();
    }

    /**
     * Updates the visual state of a goal card and its indicator circle.
     */
    private void updateState(MaterialCardView card, ImageView circle, boolean isSelected) {
        int orange = ContextCompat.getColor(requireContext(), R.color.chip_selected_orange);
        int gray = ContextCompat.getColor(requireContext(), R.color.button_stroke_colour);

        if (isSelected) {
            card.setStrokeColor(ColorStateList.valueOf(orange));
            card.setStrokeWidth(4);
            circle.setImageResource(R.drawable.survey_circle_selected);
            circle.setImageTintList(ColorStateList.valueOf(orange));
        } else {
            card.setStrokeColor(ColorStateList.valueOf(gray));
            card.setStrokeWidth(2);
            circle.setImageResource(R.drawable.survey_circle_unselected);
            circle.setImageTintList(null);
        }
    }

    private void saveGoalsToDatabase() {
        List<String> selectedTitles = new ArrayList<>(surveyViewModel.getSelectedGoals());
        String customGoal = binding.userInputGoal.getText().toString().trim();

        if (currentUserId != null && currentUserId != -1) {
            userViewModel.updateUserGoals(currentUserId, selectedTitles, customGoal);

            // clear the shared ViewModel state so it doesn't duplicate
            surveyViewModel.getSelectedGoals().clear();
            surveyViewModel.setCustomGoal(null);

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
