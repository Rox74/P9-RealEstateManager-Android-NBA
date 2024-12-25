package com.openclassrooms.realestatemanager.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;
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
    }
}