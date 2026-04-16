package com.example.fitnesscalendar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fitnesscalendar.databinding.ActivityMainBinding;
import com.example.fitnesscalendar.logic.filter.FilterViewModel;
import com.example.fitnesscalendar.repository.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


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

            // force "Reset" when clicking the graph icon again
            bottomNav.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (navController.getCurrentDestination() != null && // check if current screen opened
                        navController.getCurrentDestination().getId() == itemId) {
                    return true;
                }

                NavOptions navOptions = new NavOptions.Builder() // always go to the root of the graph
                        .setLaunchSingleTop(true)
                        .setRestoreState(false) // prevents fragment recreation
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

                if (id != R.id.ExercisesList && id != R.id.ExerciseDetail &&
                        id != R.id.FilterScreen && id != R.id.ExerciseSelectScreen) {
                    filterViewModel.setExerciseFilters(new ArrayList<>());
                }

                if (id != R.id.WorkoutsList && id != R.id.WorkoutSelectScreen &&
                        id != R.id.WorkoutDetail && id != R.id.FilterScreen) {
                    filterViewModel.setWorkoutFilters(new ArrayList<>());
                }
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