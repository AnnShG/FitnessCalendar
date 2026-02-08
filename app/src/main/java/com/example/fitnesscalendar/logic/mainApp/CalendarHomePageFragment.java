package com.example.fitnesscalendar.logic.mainApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;

import lombok.NonNull;

public class CalendarHomePageFragment extends Fragment {
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

        // 'v' represents the View that was clicked
        binding.plusButton.setOnClickListener(v -> {
            // Create the PopupMenu anchored to the plusButton
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenuInflater().inflate(R.menu.plus_dropdown_menu, popup.getMenu());
            // Handle menu item clicks
            popup.setOnMenuItemClickListener(item -> {
                // Example: if (item.getItemId() == R.id.add_activity) { ... }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
