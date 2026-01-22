package com.example.fitnesscalendar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fitnesscalendar.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 1. Initialize ViewBinding
        // Use ViewBinding to set the layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2. Setup Navigation Controller
        // Note: We use the ID of the FragmentContainerView in your activity_main.xml
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_survey);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        return (navController != null && navController.navigateUp())
                || super.onSupportNavigateUp();
    }

}