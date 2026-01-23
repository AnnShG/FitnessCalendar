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
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SurveyPage2Fragment extends Fragment {

    private SurveyPage2Binding binding;
    private SurveyViewModel viewModel;
    private String selectedGender = ""; // To track gender selection



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using ViewBinding
        binding = SurveyPage2Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access the shared ViewModel (scoped to the Activity so Page 1-4 share data)
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyViewModel.class); // creates a new ViewModel if not created

        // --- GENDER SELECTION LOGIC ---
        // Since you don't have a RadioGroup, we handle each MaterialButton click
        binding.buttonMale.setOnClickListener(v -> selectGender(binding.buttonMale));
        binding.buttonFemale.setOnClickListener(v -> selectGender(binding.buttonFemale));
        binding.buttonNoAnswer.setOnClickListener(v -> selectGender(binding.buttonNoAnswer));

        // Pre-fill fields if the user is coming back from Page 3 (State Restoration)
        if (viewModel.getName() != null) {
            binding.userInputName.setText(viewModel.getName());
        }

        // Restore BirthDate
        if (viewModel.getBirthDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.userInputBirthDate.setText(format.format(viewModel.getBirthDate()));
        }

        // Restore Gender UI selection
        if (viewModel.getGender() != null) {
            String savedGender = viewModel.getGender();
            if (savedGender.equals(binding.buttonMale.getText().toString())) selectGender(binding.buttonMale);
            else if (savedGender.equals(binding.buttonFemale.getText().toString())) selectGender(binding.buttonFemale);
            else if (savedGender.equals(binding.buttonNoAnswer.getText().toString())) selectGender(binding.buttonNoAnswer);
        }


        binding.continueButton.setOnClickListener(v -> {
            // 1. Capture Name from EditText
            viewModel.setName(binding.userInputName.getText().toString());

            // 2. Capture Age (assuming userInputAge exists in your XML)
            String dateString = binding.userInputBirthDate.getText().toString();
            if (!dateString.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date date = format.parse(dateString);
                    viewModel.setBirthDate(date); // used setter to avoid private access
                } catch (ParseException e) {
                    binding.userInputBirthDate.setError("Use format dd/mm/yyyy");
                    return; // Stop navigation if date is wrong
                }
            }

            // 3. Save Gender
            if (!selectedGender.isEmpty()) {
                viewModel.setGender(selectedGender);
            }

            NavHostFragment.findNavController(SurveyPage2Fragment.this)
                    .navigate(R.id.action_SurveyPage2_to_SurveyPage3);
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage2Fragment.this)
                        .navigateUp()
        );
    }

    private void selectGender(View selectedButton) {
        int colorSelected = getResources().getColor(R.color.selected_button_stroke_colour, null);
        int colorDefault = getResources().getColor(R.color.button_stroke_colour, null);

        MaterialButton[] buttons = {binding.buttonMale, binding.buttonFemale, binding.buttonNoAnswer};

        for (MaterialButton btn : buttons) {
            if (btn == selectedButton) {
                btn.setChecked(true);
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(colorSelected));
                btn.setStrokeWidth(5);
                selectedGender = btn.getText().toString();
            } else {
                btn.setChecked(false);
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(colorDefault));
                btn.setStrokeWidth(1);
            }
            // CRITICAL: Tell the button to redraw itself with the new properties
            btn.refreshDrawableState();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
