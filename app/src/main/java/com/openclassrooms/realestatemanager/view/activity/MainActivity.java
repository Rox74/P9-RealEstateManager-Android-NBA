package com.openclassrooms.realestatemanager.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.AppInjector;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.utils.MockDataProvider;
import com.openclassrooms.realestatemanager.view.fragment.AddPropertyFragment;
import com.openclassrooms.realestatemanager.view.fragment.CurrencyConverterFragment;
import com.openclassrooms.realestatemanager.view.fragment.LoanSimulatorFragment;
import com.openclassrooms.realestatemanager.view.fragment.PropertyListFragment;

/**
 * MainActivity is the entry point of the application.
 * It initializes dependencies, sets up the UI, and manages fragment transactions.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModelFactory globally using AppInjector
        AppInjector.init(getApplication());

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Load the default property list fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PropertyListFragment())
                    .commit();
        }

        // Check if mock data has already been inserted
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDataInserted = sharedPreferences.getBoolean("is_mock_data_inserted", false);

        if (!isDataInserted) {
            // Insert mock property data for demonstration purposes
            PropertyRepository repository = new PropertyRepository(getApplication());
            repository.insertMockData(MockDataProvider.getMockProperties());

            // Mark data as inserted to avoid duplicate entries
            sharedPreferences.edit().putBoolean("is_mock_data_inserted", true).apply();
        }

        // Set up the menu for navigation actions
        setupMenu();
    }

    /**
     * Sets up the main menu using MenuProvider.
     * Handles navigation between different application features.
     */
    private void setupMenu() {
        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu); // Inflate the main menu
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_add_property) {
                    openAddPropertyFragment();
                    return true;
                } else if (menuItem.getItemId() == R.id.action_loan_simulator) {
                    openLoanSimulatorFragment();
                    return true;
                } else if (menuItem.getItemId() == R.id.action_currency_converter) {
                    openCurrencyConverterFragment();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Opens the AddPropertyFragment to allow users to add a new property listing.
     */
    private void openAddPropertyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddPropertyFragment())
                .addToBackStack(null) // Enables back navigation
                .commit();
    }

    /**
     * Opens the LoanSimulatorFragment for mortgage and loan calculations.
     */
    private void openLoanSimulatorFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoanSimulatorFragment())
                .addToBackStack(null) // Enables back navigation
                .commit();
    }

    /**
     * Opens the CurrencyConverterFragment to convert between different currencies.
     */
    private void openCurrencyConverterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CurrencyConverterFragment())
                .addToBackStack(null) // Enables back navigation
                .commit();
    }
}