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
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Utilisez requireContext() pour éviter les contextes nuls

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

        // Charger les données fictives ou via ViewModel
        loadMockProperties();

        return view;
    }

    private void loadMockProperties() {
        List<Property> mockProperties = new ArrayList<>();

        // Propriété 1 - Condo
        Address address1 = new Address(
                "127 W 57th St",
                "Manhattan",
                "NY",
                "10019",
                "USA"
        );

        List<Photo> photos1 = new ArrayList<>();
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo1", "Living Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo2", "Master Bedroom"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo3", "Office Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo4", "Dining Room"));

        List<PointOfInterest> pointsOfInterest1 = new ArrayList<>();
        pointsOfInterest1.add(new PointOfInterest("Central Park", "Park"));
        pointsOfInterest1.add(new PointOfInterest("Broadway Theater", "Entertainment"));

        mockProperties.add(new Property(
                "Condo",
                9800000,
                1072,
                8,
                "Luxury condo featuring 4 bedrooms, 2 baths, and breathtaking views of Manhattan.",
                address1,
                photos1,
                pointsOfInterest1,
                false, // Toujours disponible
                new Date(), // Entrée sur le marché (date actuelle)
                null, // Pas encore vendu
                "Realtor"
        ));

        // Propriété 2 - Maison à Montauk
        Address address2 = new Address(
                "408 Old Montauk Hwy",
                "Montauk",
                "NY",
                "11954",
                "USA"
        );

        List<Photo> photos2 = new ArrayList<>();
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo1", "Exterior View"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo2", "Living Room"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo3", "Kitchen"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo4", "Master Bedroom"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo5", "Backyard"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo6", "Pool Area"));

        List<PointOfInterest> pointsOfInterest2 = new ArrayList<>();
        pointsOfInterest2.add(new PointOfInterest("Montauk Beach", "Nature"));
        pointsOfInterest2.add(new PointOfInterest("Montauk Lighthouse", "Historical"));

        mockProperties.add(new Property(
                "Single Family Residence",
                950000,
                374,
                3,
                "Charming family residence with 1 bedroom, 1 bath, and a large backyard, located in Montauk.",
                address2,
                photos2,
                pointsOfInterest2,
                false, // Toujours disponible
                new Date(), // Entrée sur le marché (date actuelle)
                null, // Pas encore vendu
                "Zillow"
        ));

        // Définit les données fictives dans l'adapteur
        adapter.setProperties(mockProperties);
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