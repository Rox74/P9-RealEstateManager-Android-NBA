package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Photo;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.view.adapter.PropertyAdapter;
import com.openclassrooms.realestatemanager.viewmodel.PropertyListViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyListFragment extends Fragment {
    private PropertyListViewModel propertyListViewModel;
    private RecyclerView recyclerView;
    private PropertyAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);

        // Configuration du RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);

        // Initialisation du LayoutManager (obligatoire)
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialisation de l'adapteur
        adapter = new PropertyAdapter(property -> {
            if (isTablet()) {
                showPropertyDetailInTablet(property);
            } else {
                navigateToPropertyDetail(property);
            }
        });

        // Lier l'adapteur au RecyclerView
        recyclerView.setAdapter(adapter);

        // Initialisation du ViewModel avec ViewModelFactory
        propertyListViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(PropertyListViewModel.class);

        // Observer les données du ViewModel
        propertyListViewModel.getAllProperties().observe(getViewLifecycleOwner(), properties -> {
            if (properties != null) {
                adapter.setProperties(properties);
            }
        });

        return view;
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet); // Définir "is_tablet" dans res/values/bools.xml
    }

    private void navigateToPropertyDetail(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment) // Utilisez fragment_container pour les téléphones
                .addToBackStack(null)
                .commit();
    }

    private void showPropertyDetailInTablet(Property property) {
        PropertyDetailFragment detailFragment = PropertyDetailFragment.newInstance(property);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.property_detail_container, detailFragment) // Utilisez property_detail_container pour les tablettes
                .commit();
    }
}