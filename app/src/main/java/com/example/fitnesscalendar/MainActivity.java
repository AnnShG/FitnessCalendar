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
import com.example.fitnesscalendar.repository.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

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

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int id = destination.getId();
                if (id == R.id.CalendarHomePage || id == R.id.navigationProfile || id == R.id.navigationGraphs) {
                    bottomNav.setVisibility(View.VISIBLE);
                } else {
                    bottomNav.setVisibility(View.GONE);
                }
            });
        }
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_fragment_content_main);
//
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//
//            BottomNavigationView bottomNav = binding.bottomNavigation;
//            // This line handles all the click logic automatically!
//            NavigationUI.setupWithNavController(bottomNav, navController);
//
//            // Visibility Listener
//            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
//                int id = destination.getId();
//                if (id == R.id.CalendarHomePage || id == R.id.navigationProfile || id == R.id.navigationGraphs) {
//                    bottomNav.setVisibility(View.VISIBLE);
//                } else {
//                    bottomNav.setVisibility(View.GONE);
//                }
//            });
//
//            // Link Bottom Navigation to NavController
//            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
//        }
//
//    }


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