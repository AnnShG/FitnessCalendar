package com.example.fitnesscalendar.logic.survey;

import android.content.Context;
import android.content.SharedPreferences;
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

        binding.continueButton.setOnClickListener(v -> {
            viewModel.saveUserProfileToDatabase();

            SharedPreferences prefs = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            prefs.edit().putBoolean("is_survey_completed", true).apply();

            Toast.makeText(requireContext(), "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();

            NavHostFragment.findNavController(this).navigate(
                    R.id.action_SurveyPage4_to_CalendarHomePage,
                    null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(R.id.SurveyPage1, true)
                            .build()
            );
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
