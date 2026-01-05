package com.example.fitnesscalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.databinding.SurveyPage3Binding;
import com.example.fitnesscalendar.databinding.SurveyPage4Binding;

public class SurveyPage4Fragment extends Fragment {

    private SurveyPage4Binding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SurveyPage4Binding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
