package com.example.fitnesscalendar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;
import com.example.fitnesscalendar.databinding.SurveyPage3Binding;

//Handles button clicks (Mindset, Plus), observes ViewModel data, and sets up the Bottom Navigation visibility.
public class CalendarHomeFragment extends Fragment {
    private CalendarHomePageBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = CalendarHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            binding.plusButton.setOnClickListener(Log.v ->
            PopupMenu popup = new PopupMenu(requireContext(), Log.v);
            popup.getMenuInflater().inflate(R.menu.plus_dropdown_menu, popup.getMenu());popup.setOnMenuItemClickListener(item -> {
            // Handle menu item clicks
            return true;
        });
            popup.show();
            );
    }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }
}
