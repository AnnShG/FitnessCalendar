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
        // Inside onViewCreated
// Inside onViewCreated
        binding.buttonMale.setOnClickListener(v -> handleGenderSelection(binding.buttonMale, "Male"));
        binding.buttonFemale.setOnClickListener(v -> handleGenderSelection(binding.buttonFemale, "Female"));
        binding.buttonNoAnswer.setOnClickListener(v -> handleGenderSelection(binding.buttonNoAnswer, "Other"));

//        binding.genderToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
//            if (isChecked) {
//                String selectedGender = "";
//
//                if (checkedId == R.id.buttonMale) {
//                    selectedGender = "Male";
//                } else if (checkedId == R.id.buttonFemale) {
//                    selectedGender = "Female";
//                } else if (checkedId == R.id.buttonNoAnswer) {
//                    selectedGender = "Other";
//                }
//
//                // Save to your ViewModel (Lombok generates setGender)
//                viewModel.setGender(selectedGender);
//            }
//        });

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

    // Helper Method
    private void handleGenderSelection(com.google.android.material.button.MaterialButton selectedButton, java.lang.String genderValue) {
        // 1. Save to ViewModel
        viewModel.setGender(genderValue);

        // 2. Define your colors
        int orangeColor = getResources().getColor(R.color.chip_selected_orange, null); // Make sure 'orange' is in colors.xml
        int grayStroke = getResources().getColor(R.color.button_stroke_colour, null);

        MaterialButton[] buttons = {binding.buttonMale, binding.buttonFemale, binding.buttonNoAnswer};

        for (MaterialButton btn : buttons) {
            // Force background to stay white
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));

            if (btn.getId() == selectedButton.getId()) {
                // Highlighted: Orange Stroke
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(orangeColor));
                btn.setStrokeWidth(6); // Bold orange stroke
            } else {
                // Default: Gray Stroke
                btn.setStrokeColor(android.content.res.ColorStateList.valueOf(grayStroke));
                btn.setStrokeWidth(2);
            }
        }
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
