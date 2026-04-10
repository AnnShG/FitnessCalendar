package com.example.fitnesscalendar.logic.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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
    private CalendarAdapter adapter;
    private DailyWorkoutAdapter dailyAdapter;
    private WorkoutViewModel workoutViewModel;
    CalendarManager calendarManager = new CalendarManager(); // handles  all date calcs and format.
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private final List<String> daysList = new ArrayList<>(); //  Holds the current month's day strings 1,2,3,4
    private List<DateColourResult> allUserPlans = new ArrayList<>();
    private final Set<Long> checkedWorkoutIds = new HashSet<>();
    private final Set<Long> unCheckedWorkoutIds = new HashSet<>();
    private final Set<Long> selectedDays = new HashSet<>();
    private long currentUserId = -1;
    private long selectedEpochDay = -1; // Stores the date clicked by the user
    private int selectedDayWorkoutCount = 0;


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

        this.workoutViewModel = new ViewModelProvider(requireActivity()).get(WorkoutViewModel.class);

        // Fetches the logged-in user
        workoutViewModel.getLoggedInUser().observe(getViewLifecycleOwner(), userWithGoals -> {
            if (userWithGoals != null) {
                this.currentUserId = userWithGoals.user.id;

                // Once the user is known, observes the workout "Dots" from the DB
                // Automatically updates the main calendar dots whenever the DB changes
                workoutViewModel.getWorkoutDotsForUser(currentUserId).observe(getViewLifecycleOwner(), plans -> {
                    if (plans != null && adapter != null) {
                        this.allUserPlans = plans;
                        adapter.setPlannedWorkouts(plans);
                        adapter.setHighlightedDates(selectedDays, calendarManager);

                        refreshMonthDetails();
                    }
                });

                // Used GridLayout without adapter to demonstrate the legend under the calendar
//                workoutViewModel.getUniquePlannedWorkouts(currentUserId).observe(getViewLifecycleOwner(), list -> {
//                    if (list != null) {
//                        // group workouts by color
//                        Map<Integer, List<String>> grouped = new HashMap<>(); // key - colour, value - list of titles
//                        for (PlannedWorkoutInfo info : list) {
//                            grouped.computeIfAbsent(info.colour, k -> new ArrayList<>()).add(info.title); // if colour exists, add, else create
//                        }
//
//                        // clear and fill the container
//                        binding.legendContainer.removeAllViews();
//                        for (Map.Entry<Integer, List<String>> entry : grouped.entrySet()) {
//                            addLegendRow(entry.getKey(), String.join(" | ", entry.getValue()));
//                        }
//                    }
//                });
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
                        if (navBar != null) {
                            navBar.setVisibility(View.VISIBLE);
                            navBar.setAlpha(1.0f);
                        }
                    }
                    // the grey circle should be visible after the workout was completed
                    selectedDays.clear();
                    adapter.setHighlightedDates(selectedDays, calendarManager);
                    checkedWorkoutIds.clear();
                    unCheckedWorkoutIds.clear();
                    updateDailyActionButton();
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        // handle "Attach Workout" button click in the sliding window
        binding.dailyPlanWindow.btnAttachWorkoutDaily.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_CalendarHomePage_to_WorkoutSelectScreen);
        });

//      Listen for the result coming back from WorkoutSelectFragment
        getParentFragmentManager().setFragmentResultListener("workout_selection", getViewLifecycleOwner(), (requestKey, bundle) -> {
            long workoutId = bundle.getLong("workoutId");

            if (currentUserId != -1 && selectedEpochDay != -1) {

                // VALIDATION CHECK - Reuse adapter logic to check if workout exists on THIS specific day
                if (adapter != null && adapter.isWorkoutAlreadyPlanned(selectedEpochDay, workoutId)) {
                    Toast.makeText(getContext(),
                            "This workout is already scheduled for this day",
                            Toast.LENGTH_SHORT).show();
                    return; // don't save to DB
                }
                // Reuse existing ViewModel method to save to DB
                // the single date is wrapped in a Set to match the method params
                Set<Long> dateSet = new HashSet<>();
                dateSet.add(selectedEpochDay);

                workoutViewModel.attachWorkoutToDates(currentUserId, workoutId, dateSet);

                Toast.makeText(getContext(), "Workout attached successfully!", Toast.LENGTH_SHORT).show();
            }
        });
//        dailyAdapter initialization
        dailyAdapter = new DailyWorkoutAdapter(new DailyWorkoutAdapter.OnDailyTaskActionListener() {
            @Override
            public void onDeleteTask(DateColourResult item) {
                // Remove workout from the specific day
                if (currentUserId != -1) {
                    workoutViewModel.deleteSpecificWorkoutPlan(currentUserId, item.workoutId, item.date);
                    Toast.makeText(getContext(), "Workout removed from calendar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onToggleCompletion(DateColourResult item, boolean isChecked) {
                if (item.isCompleted) {
                    // If it was already completed in DB and user UNCHECKS it
                    if (!isChecked) {
                        unCheckedWorkoutIds.add(item.workoutId);
                    } else {
                        unCheckedWorkoutIds.remove(item.workoutId);
                    }
                } else {
                    // If it was NOT completed in DB and user CHECKS it
                    if (isChecked) {
                        checkedWorkoutIds.add(item.workoutId);
                    } else {
                        checkedWorkoutIds.remove(item.workoutId);
                    }
                }
                updateDailyActionButton();
            }

            @Override
            public void onTitleClick(long workoutId) {
                // go to Workout Detail screen
                Bundle bundle = new Bundle();
                bundle.putLong("workoutId", workoutId);
                NavHostFragment.findNavController(CalendarHomePageFragment.this)
                        .navigate(R.id.action_CalendarHomePage_to_WorkoutDetail, bundle);
            }
        });

        // Initialize the RecyclerView
        binding.dailyPlanWindow.dailyWorkoutsRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));
        binding.dailyPlanWindow.dailyWorkoutsRecyclerView.setAdapter(dailyAdapter);


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
            this.selectedEpochDay = calendarManager.getEpochDayForDay(dayText);

            // hide the Activity's Bottom Navigation Bar immediately
            if (getActivity() != null) {
                View navBar = getActivity().findViewById(R.id.bottom_navigation);
                if (navBar != null) navBar.setVisibility(View.GONE);
            }

            // .post() ensures the layout has updated before expanding the window
            binding.dailyPlanWindow.dailyPlanWindow.postDelayed(() -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }, 50);

            // grey circle should always be visible to user
            selectedDays.clear();
            selectedDays.add(selectedEpochDay);
            adapter.setHighlightedDates(selectedDays, calendarManager);

            // observe workouts for the selected day
            workoutViewModel.getWorkoutsForSpecificDay(currentUserId, selectedEpochDay).observe(getViewLifecycleOwner(), workouts -> {
                selectedDayWorkoutCount = (workouts != null) ? workouts.size() : 0;

                if (workouts != null && !workouts.isEmpty()) {
                    // If workouts exist, show the list and hide the "No plans" text
                    dailyAdapter.setWorkouts(workouts);
                    binding.dailyPlanWindow.dailyWorkoutsRecyclerView.setVisibility(View.VISIBLE);
                    binding.dailyPlanWindow.noPlansText.setVisibility(View.GONE);
                } else {
                    // If day is empty, show the "No plans" text and hide the list
                    binding.dailyPlanWindow.dailyWorkoutsRecyclerView.setVisibility(View.GONE);
                    binding.dailyPlanWindow.noPlansText.setVisibility(View.VISIBLE);
                }
                updateDailyActionButton();
            });
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

        refreshMonthDetails(); // Trigger recalculation for the new month
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

    /**
     * Dynamically updates the text and logic of the main action button in the sliding window.
     * Transitions between:
     * "Attach Workout" (Default)
     * "Complete" (When new items are checked)
     * "Update Status" (When existing items are unchecked or both actions are happening)
     */
    private void updateDailyActionButton() {
        boolean hasCompletions = !checkedWorkoutIds.isEmpty();
        boolean hasUndos = !unCheckedWorkoutIds.isEmpty();

        if (hasCompletions || hasUndos) {
            // If both actions are happening
            binding.dailyPlanWindow.btnAttachWorkoutDaily.setVisibility(View.VISIBLE);
            String btnText = (hasCompletions && !hasUndos) ? "Complete" : "Update Status";
            binding.dailyPlanWindow.btnAttachWorkoutDaily.setText(btnText);

            binding.dailyPlanWindow.btnAttachWorkoutDaily.setOnClickListener(v -> {
                if (currentUserId == -1 || selectedEpochDay == -1) return;

                // Process completions (true)
                for (Long workoutId : checkedWorkoutIds) {
                    workoutViewModel.updateWorkoutCompletion(currentUserId, workoutId, selectedEpochDay, true);
                }

                for (Long id : unCheckedWorkoutIds) {
                    workoutViewModel.updateWorkoutCompletion(currentUserId, id, selectedEpochDay, false);
                }

                // clear temporary tracking sets
                checkedWorkoutIds.clear();
                unCheckedWorkoutIds.clear();

                Toast.makeText(getContext(), "Schedule updated!", Toast.LENGTH_SHORT).show();
                updateDailyActionButton();
            });
        } else {
            binding.dailyPlanWindow.btnAttachWorkoutDaily.setText("Attach Workout");
            binding.dailyPlanWindow.btnAttachWorkoutDaily.setOnClickListener(v -> {
                if (selectedDayWorkoutCount >= 3) {
                    Toast.makeText(getContext(),
                            "Maximum of 3 workouts per day reached",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Only navigate if the limit is not reached
                    NavHostFragment.findNavController(this)
                            .navigate(R.id.action_CalendarHomePage_to_WorkoutSelectScreen);
                }
            });
        }
    }

    private void refreshMonthDetails() {
        if (allUserPlans == null || binding == null) return;

        long start = calendarManager.getStartOfMonthEpochDay();
        long end = calendarManager.getEndOfMonthEpochDay();

        Set<Long> completedDaysCounter = new HashSet<>();

        Map<Integer, List<String>> monthWorkoutsLegend = new HashMap<>();

        for (DateColourResult plan : allUserPlans) {
            // process data that falls within the viewed month
            if (plan.date >= start && plan.date <= end) {

                if (plan.isCompleted) {
                    completedDaysCounter.add(plan.date);
                }

                // collect workout titles for the legend (planned OR completed)
                monthWorkoutsLegend.computeIfAbsent(plan.colour, k -> new ArrayList<>()).add(plan.title);
            }
        }

        binding.trainingDaysCount.setText(String.valueOf(completedDaysCounter.size()));

        binding.legendContainer.removeAllViews();
        if (monthWorkoutsLegend.isEmpty()) {
            binding.legendContainer.setVisibility(View.GONE);
        } else {
            binding.legendContainer.setVisibility(View.VISIBLE);
            for (Map.Entry<Integer, List<String>> entry : monthWorkoutsLegend.entrySet()) {
                // unique titles only
                List<String> uniqueTitles = new ArrayList<>(new HashSet<>(entry.getValue()));
                addLegendRow(entry.getKey(), String.join("  |  ", uniqueTitles));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
