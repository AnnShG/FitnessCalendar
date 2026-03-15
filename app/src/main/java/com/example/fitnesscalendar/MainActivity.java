package com.example.fitnesscalendar;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fitnesscalendar.databinding.ActivityMainBinding;
import com.example.fitnesscalendar.repository.ExerciseRepository;
import com.example.fitnesscalendar.repository.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private ExerciseRepository exerciseRepository;
//    ExerciseRepository
    UserRepository userRepo = new UserRepository(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
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
                if (item.getItemId() == R.id.NavigationProfile) {
                    navController.popBackStack(R.id.NavigationProfile, false);
                }
            });

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.CalendarHomePage || id == R.id.NavigationProfile || id == R.id.NavigationGraphs ||
                        id == R.id.ExercisesList|| id == R.id.ExerciseDetail) {
                    bottomNav.setVisibility(View.VISIBLE);
                } else {
                    bottomNav.setVisibility(View.GONE);
                }
            });
        }

        userRepo.getDatabaseExecutor().execute(() -> {
            // Check if categories are already in the DB
            if (exerciseRepository.getCategoryCount() == 0) {
                // Use your existing Repository/DAO to populate the "Master List"
                exerciseRepository.prePopulateCategories();
            }
        });
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