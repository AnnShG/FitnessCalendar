package com.example.fitnesscalendar.ui.survey;

import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SurveyPage2Fragment extends Fragment {

    private SurveyPage2Binding binding;
    private SurveyViewModel viewModel;

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
        binding.buttonMale.setOnClickListener(v -> handleGenderSelection(binding.buttonMale, "Male"));
        binding.buttonFemale.setOnClickListener(v -> handleGenderSelection(binding.buttonFemale, "Female"));
        binding.buttonNoAnswer.setOnClickListener(v -> handleGenderSelection(binding.buttonNoAnswer, "Prefer not to say"));

        // --- STATE RESTORATION ---
        // Pre-fill fields if the user is coming back from Page 3
        if (viewModel.getName() != null) {
            binding.userInputName.setText(viewModel.getName());
        }

        // Restore BirthDate
        if (viewModel.getBirthDate() != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.userInputBirthDate.setText(format.format(viewModel.getBirthDate()));
        }

        // Restore Orange Highlight if gender was already selected
        String savedGender = viewModel.getGender();
        if (savedGender != null) {
            switch (savedGender) {
                case "Male":
                    handleGenderSelection(binding.buttonMale, "Male");
                    break;
                case "Female":
                    handleGenderSelection(binding.buttonFemale, "Female");
                    break;
                case "Prefer not to say":
                    handleGenderSelection(binding.buttonNoAnswer, "Prefer not to say");
                    break;
            }
        }

        // 1. Make the field un-editable so the keyboard doesn't appear
        binding.userInputBirthDate.setFocusable(false);
        binding.userInputBirthDate.setClickable(true);

        binding.userInputBirthDate.setOnClickListener(v -> {
            // Get current date for the picker default
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

            // Create the DatePickerDialog with the Spinner theme
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth, // This creates the 3-column scrollable look
                    (view1, selectedYear, selectedMonth, selectedDay) -> {

                        // Format the date for the UI
                        String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.userInputBirthDate.setText(dateString);

                        // Save to ViewModel as a Date object
                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedYear, selectedMonth, selectedDay);
                        viewModel.setBirthDate(selectedCal.getTime());
                    },
                    year, month, day
            );
            // Make the background transparent so it looks like a clean popup
            if (datePickerDialog.getWindow() != null) {
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            datePickerDialog.show();
        });


        // --- NAVIGATION ---
        binding.continueButton.setOnClickListener(v -> {
            // 1. Capture Name from EditText
            if (binding.userInputName.getText() != null) {
                viewModel.setName(binding.userInputName.getText().toString());
            }

            // Validation: Ensure birth date is selected
            if (viewModel.getBirthDate() == null) {
                binding.userInputBirthDate.setError("Please select your birth date");
                return;
            }

            NavHostFragment.findNavController(SurveyPage2Fragment.this)
                    .navigate(R.id.action_SurveyPage2_to_SurveyPage3);
        });

        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(SurveyPage2Fragment.this)
                        .navigateUp()
        );
    }

    // Helper Method
    private void handleGenderSelection(MaterialButton selectedButton, String genderValue) {
        // 1. Save to ViewModel
        viewModel.setGender(genderValue);

        // 2. Define colors
        int orangeStroke = getResources().getColor(R.color.chip_selected_orange, null); // Make sure 'orange' is in colors.xml
        int grayStroke = getResources().getColor(R.color.button_stroke_colour, null);

        MaterialButton[] buttons = {binding.buttonMale, binding.buttonFemale, binding.buttonNoAnswer};

        for (MaterialButton btn : buttons) {
            // Force background to stay white
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));

            if (btn.getId() == selectedButton.getId()) {
                // Highlighted: Orange Stroke
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(orangeStroke));
                btn.setStrokeWidth(6); // Bold orange stroke
            } else {
                // Default: Gray Stroke
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(grayStroke));
                btn.setStrokeWidth(2);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
