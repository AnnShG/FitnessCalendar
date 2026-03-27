package com.example.fitnesscalendar.logic.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitnesscalendar.databinding.WorkoutsSelectScreenBinding;
import com.example.fitnesscalendar.databinding.WorkoutsListScreenBinding;

import lombok.NonNull;

public class WorkoutSelectFragment extends WorkoutsListFragment {

    private WorkoutsSelectScreenBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WorkoutsSelectScreenBinding.inflate(inflater, container, false);

        super.binding = WorkoutsListScreenBinding.bind(binding.getRoot());

        super.root = binding.getRoot();
        return super.root;
    }
}
