package com.example.fitnesscalendar.logic.survey;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.SurveyPage4Binding;

public class SurveyPage4Fragment extends Fragment {

    private SurveyPage4Binding binding;
    private SurveyViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SurveyPage4Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the SAME ViewModel used in all previous fragments
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class);

//        binding.continueButton.setOnClickListener(v -> {
//            // Call the method that handles validation, saving, and navigation
//            saveSurveyAndFinish();
//        });

        // Inside SurveyPage4Fragment.java
        binding.continueButton.setOnClickListener(v -> {
            // 1. Save data
            viewModel.saveUserProfileToDatabase();

            // 2. Navigate - Double check this ID in your nav_graph.xml!
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_SurveyPage4_to_CalendarHomePage);
        });

    }

//    private void saveSurveyAndFinish() {
        // 1. Gather all data from the ViewModel
//        String name = viewModel.getName();
//        Date birthDate = viewModel.getBirthDate();
//        String gender = viewModel.getGender();
//        Set<String> goals = viewModel.getSelectedGoals();

        // Since Room operations should be asynchronous, we call a method in the ViewModel
        // which handles the background thread work.
//        try {
//            viewModel.saveUserProfileToDatabase();
//
//            // Feedback to user
//            String name = viewModel.getName() != null ? viewModel.getName() : "User";
//            Toast.makeText(requireContext(), "Survey Saved! Welcome " + name, Toast.LENGTH_SHORT).show();
//
//            // 3. Navigate to the main app environment
//            NavHostFragment.findNavController(this)
//                    .navigate(R.id.action_SurveyPage4_to_CalendarHomePage);
//            } catch (Exception e) {
//            // This will tell you WHY it's crashing if it's a code error
//            Toast.makeText(requireContext(), "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
