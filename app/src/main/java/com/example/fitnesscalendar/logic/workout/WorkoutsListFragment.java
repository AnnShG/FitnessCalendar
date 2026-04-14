package com.example.fitnesscalendar.logic.workout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.WorkoutsListScreenBinding;
import com.example.fitnesscalendar.entities.Category;
import com.example.fitnesscalendar.logic.filter.FilterViewModel;
import com.example.fitnesscalendar.logic.utils.CategoryStyleHelper;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;

public class WorkoutsListFragment extends Fragment {
    protected WorkoutsListScreenBinding binding;
    protected WorkoutViewModel workoutViewModel;
    protected FilterViewModel filterViewModel;
    protected WorkoutAdapter workoutAdapter;
    protected View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            binding = WorkoutsListScreenBinding.inflate(inflater, container, false);
            root = binding.getRoot();
        } else {
            binding = WorkoutsListScreenBinding.bind(root);
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();

        workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);
        filterViewModel = new ViewModelProvider(requireActivity()).get(FilterViewModel.class);

        // Sync FilterViewModel -> WorkoutViewModel
        filterViewModel.getWorkoutFilters().observe(getViewLifecycleOwner(), ids -> {
            if (ids != null) {
                Log.d("FILTER_DEBUG", "Syncing filters to WorkoutViewModel: " + ids.size());
                workoutViewModel.setFilters(ids);
            }
        });

        // Observe filtered results
        workoutViewModel.filteredWorkouts.observe(getViewLifecycleOwner(), list -> {
            if (list != null && binding != null) {
                workoutAdapter.setAllWorkouts(list);
                binding.filteredCountText.setText(list.size() + " Workouts Found");
            } else if (binding != null) {
                binding.filteredCountText.setText("0 Workouts Found");
            }
        });

        // Render chips whenever filter IDs or Categories change
        workoutViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                workoutViewModel.getFilterIds().observe(getViewLifecycleOwner(), selectedIds -> {
                    renderFilterChips(selectedIds, categories);
                });
            }
        });

        binding.searchWorkoutField.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                workoutViewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                workoutViewModel.setSearchQuery(newText);
                return true;
            }
        });

        binding.backButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());

        binding.filterWorkoutsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("filter_type", "workout");
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutsList_to_FilterScreen, bundle);
        });
    }

    private void setupRecyclerView() {
        if (workoutAdapter == null) {
            workoutAdapter = new WorkoutAdapter();
        }

        RecyclerView recyclerView = binding.workoutsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(workoutAdapter);

        workoutAdapter.setOnInfoClickListener(id -> {
            Bundle bundle = new Bundle();
            bundle.putLong("workoutId", id);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_WorkoutsList_to_WorkoutsDetail, bundle);
        });
    }

    private void renderFilterChips(List<Long> selectedIds, List<Category> allCategories) {
        if (binding == null) return;
        binding.selectedFilterChips.removeAllViews();

        if (selectedIds == null || allCategories == null) return;

        for (Long id : selectedIds) {
            Category category = null;
            for (Category cat : allCategories) {
                if (cat.getId().equals(id)) {
                    category = cat;
                    break;
                }
            }

            if (category != null) {
                Chip chip = new Chip(requireContext());
                chip.setText(category.getName());
                chip.setCloseIconVisible(true);

                CategoryStyleHelper.CategoryStyle style = CategoryStyleHelper.getStyleForGroup(category.getCategoryGroup());

                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), style.backgroundColor)));
                chip.setChipStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), style.strokeColor)));
                chip.setChipStrokeWidth(2f);
                chip.setCloseIconTint(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), style.strokeColor)));

                chip.setOnCloseIconClickListener(v -> {
                    List<Long> newList = new ArrayList<>(selectedIds);
                    newList.remove(id);
                    filterViewModel.setWorkoutFilters(newList);
                });

                binding.selectedFilterChips.addView(chip);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
