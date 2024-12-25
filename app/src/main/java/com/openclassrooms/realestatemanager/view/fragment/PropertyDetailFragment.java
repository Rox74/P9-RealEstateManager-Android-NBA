package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel;

public class PropertyDetailFragment extends Fragment {
    private static final String ARG_PROPERTY = "property";

    private PropertyDetailViewModel propertyDetailViewModel;
    private RecyclerView photoRecyclerView;
    private TextView descriptionTextView;
    private TextView detailsTextView;
    private ImageView mapImageView;

    public static PropertyDetailFragment newInstance(Property property) {
        PropertyDetailFragment fragment = new PropertyDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROPERTY, property);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail, container, false);

        // Initialisation des vues
        photoRecyclerView = view.findViewById(R.id.property_photos);
        descriptionTextView = view.findViewById(R.id.property_description);
        detailsTextView = view.findViewById(R.id.property_details);
        mapImageView = view.findViewById(R.id.property_map);

        // Configuration RecyclerView pour les photos
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PhotoAdapter photoAdapter = new PhotoAdapter();
        photoRecyclerView.setAdapter(photoAdapter);

        // Initialisation du ViewModel
        propertyDetailViewModel = new ViewModelProvider(this).get(PropertyDetailViewModel.class);

        if (getArguments() != null && getArguments().containsKey(ARG_PROPERTY)) {
            Property property = getArguments().getParcelable(ARG_PROPERTY);
            propertyDetailViewModel.selectProperty(property);
        }

        // Observation des détails de la propriété
        propertyDetailViewModel.getSelectedProperty().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                descriptionTextView.setText(property.description);
                detailsTextView.setText(formatPropertyDetails(property));
                photoAdapter.setPhotos(property.photos);

                // Charger la carte (API Google Maps Static)
                loadStaticMap(property.address);
            }
        });

        return view;
    }

    private String formatPropertyDetails(Property property) {
        return "Surface: " + property.surface + " m²\n"
                + "Rooms: " + property.numberOfRooms + "\n"
                + "Agent: " + property.agentName;
    }

    private void loadStaticMap(Address address) {
        // Exemple de chargement d'une carte statique à partir de l'adresse
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
                + address.street + "," + address.city + "&zoom=15&size=600x300&key=YOUR_API_KEY";
        Glide.with(this).load(mapUrl).into(mapImageView); // Utilise Glide pour charger l'image
    }
}