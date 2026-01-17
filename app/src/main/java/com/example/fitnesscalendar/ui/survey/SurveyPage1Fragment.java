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
import com.example.fitnesscalendar.databinding.SurveyPage1Binding;

public class SurveyPage1Fragment extends Fragment {

    private SurveyPage1Binding binding;
    private SurveyViewModel viewModel;


    @Override
    public View onCreateView( // called when Android prepares UI
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SurveyPage1Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {  // UI exists/shown
        super.onViewCreated(view, savedInstanceState); // internal setup

        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created

        binding.button.setOnClickListener(v -> {// lambda - shorter
            viewModel.onNextClicked();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_SurveyPage1_to_SurveyPage2);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView(); // UI destroyed, but fragment may still exist
        binding = null; // prevents memory leaks
    }

}
