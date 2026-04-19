package com.example.fitnesscalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.fitnesscalendar.databinding.ActivityMainBinding;
import com.example.fitnesscalendar.logic.filter.FilterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FilterViewModel filterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // inflate UI immediately
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // the graph inflated manually so the start destination was set BEFORE setting it on the controller
            NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);

            SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            boolean isCompleted = prefs.getBoolean("is_survey_completed", false);

            if (isCompleted) {
                navGraph.setStartDestination(R.id.CalendarHomePage);
            } else {
                navGraph.setStartDestination(R.id.SurveyPage1);
            }

            navController.setGraph(navGraph);

            BottomNavigationView bottomNav = binding.bottomNavigation;
            NavigationUI.setupWithNavController(bottomNav, navController);

            setupBottomNavListeners(bottomNav, navController, filterViewModel);
        }
    }

    private void setupBottomNavListeners(BottomNavigationView bottomNav, NavController navController, FilterViewModel filterViewModel) {
        // force "Reset" when clicking the graph icon again
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (navController.getCurrentDestination() != null &&
                    navController.getCurrentDestination().getId() == itemId) {
                return true;
            }

            NavOptions navOptions = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(false)
                    .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                    .build();

            navController.navigate(itemId, null, navOptions);
            return true;
        });

        bottomNav.setOnItemReselectedListener(item -> {
            navController.popBackStack(item.getItemId(), false);
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            boolean isSurvey =
                    id == R.id.SurveyPage1 || id == R.id.SurveyPage2 ||
                    id == R.id.SurveyPage3 || id == R.id.SurveyPage4 ||
                    id == R.id.FilterScreen;

            bottomNav.setVisibility(isSurvey ? View.GONE : View.VISIBLE);

            if (id != R.id.ExercisesList && id != R.id.ExerciseDetail && id != R.id.ExerciseSelectScreen) {
                filterViewModel.setExerciseFilters(new ArrayList<>());
            }
            if (id != R.id.WorkoutsList && id != R.id.WorkoutSelectScreen && id != R.id.WorkoutDetail) {
                filterViewModel.setWorkoutFilters(new ArrayList<>());
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            return navHostFragment.getNavController().navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

}
