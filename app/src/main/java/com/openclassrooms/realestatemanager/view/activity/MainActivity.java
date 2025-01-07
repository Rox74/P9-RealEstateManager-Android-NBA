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
import com.openclassrooms.realestatemanager.view.fragment.EditPropertyFragment;
import com.openclassrooms.realestatemanager.view.fragment.LoanSimulatorFragment;
import com.openclassrooms.realestatemanager.view.fragment.PropertyListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModelFactory globally using AppInjector
        AppInjector.init(getApplication());

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Charge le fragment de liste par défaut
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PropertyListFragment())
                    .commit();
        }

        // Vérifier si les données mockées ont déjà été insérées
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDataInserted = sharedPreferences.getBoolean("is_mock_data_inserted", false);

        if (!isDataInserted) {
            // Insérer les données mockées
            PropertyRepository repository = new PropertyRepository(getApplication());
            repository.insertMockData(MockDataProvider.getMockProperties());

            // Marquer les données comme insérées
            sharedPreferences.edit().putBoolean("is_mock_data_inserted", true).apply();
        }

        setupMenu(); // Ajout du menu via MenuProvider
    }

    private void setupMenu() {
        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu); // Charge le menu principal
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

    private void openAddPropertyFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AddPropertyFragment())
                .addToBackStack(null)
                .commit();
    }

    private void openLoanSimulatorFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoanSimulatorFragment())
                .addToBackStack(null)
                .commit();
    }

    private void openCurrencyConverterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CurrencyConverterFragment())
                .addToBackStack(null)
                .commit();
    }
}