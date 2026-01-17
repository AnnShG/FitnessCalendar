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
import com.example.fitnesscalendar.databinding.SurveyPage2Binding;

public class SurveyPage2Fragment extends Fragment {

    private SurveyPage2Binding binding;
    private SurveyViewModel viewModel;
    private String selectedGoal = null;



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SurveyPage2Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created


        cardStayActive.setOnClickListener(v -> {
            selectedGoal = getString(R.string.stay_active);
            updateSelectionUI(circle6);
        });

        binding.continueButton.setOnClickListener(v -> {
            viewModel.setGoal(binding.goalEditText.getText().toString());
            viewModel.onNextClicked();

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_SurveyPage2_to_SurveyPage3);
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage2Fragment.this)
                        .navigateUp()
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
