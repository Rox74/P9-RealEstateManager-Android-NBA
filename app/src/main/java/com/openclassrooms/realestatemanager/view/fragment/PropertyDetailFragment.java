package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel;

public class PropertyDetailFragment extends Fragment {
    private static final String ARG_PROPERTY = "property";

    private PropertyDetailViewModel propertyDetailViewModel;
    private MapViewModel mapViewModel;
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

        // Initialisation des ViewModels
        propertyDetailViewModel = new ViewModelProvider(this).get(PropertyDetailViewModel.class);
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        if (getArguments() != null && getArguments().containsKey(ARG_PROPERTY)) {
            Property property = getArguments().getParcelable(ARG_PROPERTY);
            propertyDetailViewModel.selectProperty(property);
        }

        // Observer les données du ViewModel
        propertyDetailViewModel.getSelectedProperty().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                descriptionTextView.setText(property.description);
                detailsTextView.setText(formatPropertyDetails(property));
                photoAdapter.setPhotos(property.photos);

                // Charger la carte via la méthode
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
        if (address == null || address.street == null || address.city == null || address.state == null) {
            Log.e("PropertyDetailFragment", "Address is null or incomplete");
            mapImageView.setImageResource(R.drawable.ic_placeholder_map);
            return;
        }

        // Vérification de la connexion Internet
        if (!Utils.isInternetAvailable(requireContext())) {
            Log.e("PropertyDetailFragment", "No internet connection");
            mapImageView.setImageResource(R.drawable.ic_offline_map);
            return;
        }

        String fullAddress = address.street + ", " + address.city + ", " + address.state;

        Log.d("PropertyDetailFragment", "Fetching coordinates for address: " + fullAddress);

        mapViewModel.fetchCoordinates(fullAddress);

        mapViewModel.getMapDataLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                double lat = response.lat;
                double lon = response.lon;

                Log.d("PropertyDetailFragment", "Coordinates found: lat=" + lat + ", lon=" + lon);

                // Construire l'URL de la carte avec style détaillé et marqueur rouge
                String mapUrl = "https://static-maps.yandex.ru/1.x/?lang=en_US&ll="
                        + lon + "," + lat
                        + "&z=17&l=map&pt=" + lon + "," + lat + ",pm2rdm";

                Log.d("PropertyDetailFragment", "Generated map URL: " + mapUrl);

                // Charger la carte via Glide
                Glide.with(this)
                        .load(mapUrl)
                        .placeholder(R.drawable.ic_placeholder_map)
                        .error(R.drawable.ic_error_map)
                        .into(mapImageView);
            } else {
                Log.e("PropertyDetailFragment", "No coordinates found for the address");
                mapImageView.setImageResource(R.drawable.ic_error_map);
            }
        });

        mapViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("PropertyDetailFragment", "Error fetching coordinates: " + error);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}