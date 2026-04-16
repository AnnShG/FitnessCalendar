package com.example.fitnesscalendar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fitnesscalendar.databinding.ActivityMainBinding;
import com.example.fitnesscalendar.logic.filter.FilterViewModel;
import com.example.fitnesscalendar.repository.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FilterViewModel filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            UserRepository repo = new UserRepository(getApplication());

            BottomNavigationView bottomNav = binding.bottomNavigation;
            NavigationUI.setupWithNavController(bottomNav, navController);


            repo.getDatabaseExecutor().execute(() -> {
                if (repo.hasUser()) {
                    runOnUiThread(() -> {
                        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
                        navGraph.setStartDestination(R.id.CalendarHomePage);
                        navController.setGraph(navGraph);
                    });
                }
            });

            // force "Reset" when clicking the profile icon again
            bottomNav.setOnItemReselectedListener(item -> {
                int itemId = item.getItemId();
                if (item.getItemId() == R.id.NavigationProfile) {
                    navController.popBackStack(R.id.NavigationProfile, false);
                } else if (itemId == R.id.CalendarHomePage) {
                    navController.popBackStack(R.id.CalendarHomePage, false);
                }
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.CalendarHomePage || id == R.id.NavigationProfile || id == R.id.NavigationGraphs ||
                        id == R.id.ExercisesList || id == R.id.ExerciseDetail || id == R.id.WorkoutsList ||
                        id == R.id.WorkoutDetail || id == R.id.PlanProgramScreen || id == R.id.ExerciseSelectScreen) {
                    bottomNav.setVisibility(View.VISIBLE);
                } else {
                    bottomNav.setVisibility(View.GONE);
                }

                if (id != R.id.ExercisesList && id != R.id.ExerciseDetail &&
                        id != R.id.FilterScreen && id != R.id.ExerciseSelectScreen) {
                    filterViewModel.setExerciseFilters(new java.util.ArrayList<>());
                }

                if (id != R.id.WorkoutsList && id != R.id.WorkoutDetail && id != R.id.FilterScreen) {
                    filterViewModel.setWorkoutFilters(new java.util.ArrayList<>());
                }

                bottomNav.setOnItemReselectedListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.NavigationProfile) {
                        navController.popBackStack(R.id.NavigationProfile, false);
                    } else if (itemId == R.id.CalendarHomePage) {
                        navController.popBackStack(R.id.CalendarHomePage, false);
                    }
                });

            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        if (navHostFragment != null) {
            return NavigationUI.navigateUp(navHostFragment.getNavController(), appBarConfiguration)
                    || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

}