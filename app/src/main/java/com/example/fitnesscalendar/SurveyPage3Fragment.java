package com.example.fitnesscalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.databinding.SurveyPage3Binding;

public class SurveyPage3Fragment extends Fragment {

    private SurveyPage3Binding binding;

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

        binding.continueButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigate(R.id.action_SurveyPage3_to_SurveyPage4)
        );

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage3Fragment.this)
                        .navigateUp()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
