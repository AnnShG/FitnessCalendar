package com.example.fitnesscalendar.logic.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.R;
import com.example.fitnesscalendar.databinding.CalendarHomePageBinding;
import com.example.fitnesscalendar.logic.workout.WorkoutViewModel;
import com.example.fitnesscalendar.relations.DateColourResult;
import com.example.fitnesscalendar.relations.PlannedWorkoutInfo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.NonNull;

// directly interacts with the UI - change the month view (arrows) and title, indicated the touched day
/**
 * CalendarHomePageFragment serves as the main screen of the app.
 * It displays a dynamic calendar grid, handles month navigation, and provides
 * a button for adding exercises, workouts, and planning programs.
 */
public class CalendarHomePageFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private CalendarHomePageBinding binding;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private final List<String> daysList = new ArrayList<>(); //  Holds the current month's day strings 1,2,3,4
    private CalendarAdapter adapter;
    private DailyWorkoutAdapter dailyAdapter;
    CalendarManager calendarManager = new CalendarManager(); // handles  all date calcs and format.

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = CalendarHomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Plus Button Logic: Opens a dropdown menu.
         * Includes a "Reflection" hack to force Android to show icons inside the PopupMenu,
         * as icons are hidden by default in standard PopupMenus.
         */
        binding.plusButton.setOnClickListener(v -> { // 'v' represents the View that was clicked
            // Create the PopupMenu anchored to the plusButton
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenuInflater().inflate(R.menu.plus_dropdown_menu, popup.getMenu());

            try {
                // Access the private field "mPopup" inside the PopupMenu class which holds the actual window logic
                Field field = popup.getClass().getDeclaredField("mPopup");
                field.setAccessible(true);
                Object menuPopupHelper = field.get(popup);
                // Invoking hidden 'setForceShowIcon' method to enable icons
                Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
                Method method = cls.getDeclaredMethod("setForceShowIcon", boolean.class);
                method.invoke(menuPopupHelper, true);
            } catch (Exception e) {
                android.util.Log.w("CalendarHome", "Could not force icons in PopupMenu", e);
            }

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId(); // item (tag) id that is one menu.xml
                if (id == R.id.action_add_exercise) {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_CalendarHomePage_to_AddExerciseScreen);
                    return true;
                } else if (id == R.id.action_add_workout) {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_CalendarHomePage_to_AddWorkoutScreen);
                    return true;
                } else if (id == R.id.action_plan_program) {
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_CalendarHomePage_to_PlanProgramScreen);
                    return true;
                }
                return false;
            });
            popup.show();
        });

        // Initialize the Calendar Adapter and attach it to the RecyclerView
        adapter = new CalendarAdapter(daysList, this);
        binding.calendarRecyclerView.setAdapter(adapter);

        WorkoutViewModel workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        // Fetches the logged-in user
        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                long userId = userWithGoals.user.id;

                // Once the user is known, observes the workout "Dots" from the DB
                // Automatically updates the main calendar dots whenever the DB changes
                workoutViewModel.getWorkoutDotsForUser(userId).observe(getViewLifecycleOwner(), plans -> {
                    if (plans != null && adapter != null) {
                        adapter.setPlannedWorkouts(plans);
                        adapter.setHighlightedDates(new HashSet<>(), calendarManager);
                    }
                });

                // Used GridLayout without adapter to demonstrate the legend under the calendar
                workoutViewModel.getUniquePlannedWorkouts(userId).observe(getViewLifecycleOwner(), list -> {
                    if (list != null) {
                        // group workouts by color
                        Map<Integer, List<String>> grouped = new HashMap<>(); // key - colour, value - list of titles
                        for (PlannedWorkoutInfo info : list) {
                            grouped.computeIfAbsent(info.colour, k -> new ArrayList<>()).add(info.title); // if colour exists, add, else create
                        }

                        // clear and fill the container
                        binding.legendContainer.removeAllViews();
                        for (Map.Entry<Integer, List<String>> entry : grouped.entrySet()) {
                            addLegendRow(entry.getKey(), String.join(", ", entry.getValue()));
                        }
                    }
                });
            }
        });
        /**
         * Initializing the BottomSheetBehavior from the included layout
         * 1st dailyPlanWindow is the ID from the <include>
         * 2nd dailyPlanWindow is the root ID inside plan_daily.xml
         */
        bottomSheetBehavior = BottomSheetBehavior.from(binding.dailyPlanWindow.dailyPlanWindow);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN); // by default it is hidden

        // // Set up the callback to show the Bottom Navigation Bar again when the window is closed
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) { // must be initialised
                if (newState == BottomSheetBehavior.STATE_HIDDEN || newState == BottomSheetBehavior.STATE_COLLAPSED) { // collapsed - the user hided it
                    if (getActivity() != null) {
                        View navBar = getActivity().findViewById(R.id.bottom_navigation);
                        if (navBar != null) navBar.setVisibility(View.VISIBLE);
                    }
                    adapter.setHighlightedDates(new HashSet<>(), calendarManager);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {} // must be initialised
        });

        dailyAdapter = new DailyWorkoutAdapter(new DailyWorkoutAdapter.OnDailyTaskActionListener() {
            @Override    public void onDeleteTask(DateColourResult item) {
                // workoutViewModel.removeWorkoutFromDate(userId, item.workoutId, item.date);
            }

            @Override
            public void onToggleCompletion(DateColourResult item, boolean isCompleted) {
            }
        });

        binding.calendarPrevButton.setOnClickListener(v -> {
            calendarManager.goToPrevMonth();
            updateUI();
        });

        binding.calendarNextButton.setOnClickListener(v -> {
            calendarManager.goToNextMonth();
            updateUI();
        });

        binding.mindsetButton.setOnClickListener(v -> {
            MindsetDialog dialog = new MindsetDialog();
            dialog.show(getParentFragmentManager(), "MindsetDialog");
        });

        updateUI();
    }

    /**
     * Called when a specific date is tapped on the grid.
     */
    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText != null && !dayText.isEmpty()) {
            // hide the Activity's Bottom Navigation Bar immediately
            if (getActivity() != null) {
                View navBar = getActivity().findViewById(R.id.bottom_navigation);
                if (navBar != null) navBar.setVisibility(View.GONE);
            }

            // .post() ensures the layout has updated before expanding the window
            binding.dailyPlanWindow.dailyPlanWindow.post(() -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            });

            // highlight selected day
            Set<Long> highlights = new HashSet<>();
            highlights.add(calendarManager.getEpochDayForDay(dayText));
            adapter.setHighlightedDates(highlights, calendarManager);
        }
    }

    /**
     * Refreshes the visual state of the calendar.
     * Synchronizes the header text and the day grid with the CalendarManager.
     */
private void updateUI() {
        binding.monthAndYear.setText(calendarManager.getHeaderString());
        List<String> days = calendarManager.getDaysOfMonthList();
        adapter.setDays(days);
    }

    // Helper method to create the small legend rows
    private void addLegendRow(int color, String titles) {
        View view = getLayoutInflater().inflate(R.layout.calendar_legend_item, binding.legendContainer, false);

        View dot = view.findViewById(R.id.legendColorDot);
        TextView tv = view.findViewById(R.id.legendWorkoutNames);

        dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        tv.setText(titles);

        binding.legendContainer.addView(view);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
