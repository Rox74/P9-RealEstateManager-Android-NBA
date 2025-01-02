package com.openclassrooms.realestatemanager.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.utils.MockDataProvider;
import com.openclassrooms.realestatemanager.view.fragment.PropertyListFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Chargez le fragment de liste par défaut dans le conteneur
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PropertyListFragment())
                    .commit();
        }

        // Vérifiez si les données mockées ont déjà été insérées
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isDataInserted = sharedPreferences.getBoolean("is_mock_data_inserted", false);

        if (!isDataInserted) {
            // Insérez les données mockées
            PropertyRepository repository = new PropertyRepository(getApplication());
            repository.insertMockData(MockDataProvider.getMockProperties());

            // Marquez les données comme insérées
            sharedPreferences.edit().putBoolean("is_mock_data_inserted", true).apply();
        }
    }
}