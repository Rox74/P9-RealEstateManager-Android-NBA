package com.openclassrooms.realestatemanager.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.di.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel;

public class PropertyDetailFragment extends Fragment {
    private static final String ARG_PROPERTY = "property";
    private Property selectedProperty;

    private PropertyDetailViewModel propertyDetailViewModel;
    private MapViewModel mapViewModel;
    private RecyclerView photoRecyclerView;
    private TextView descriptionTextView;
    private TextView surfaceTextView;
    private TextView roomsTextView;
    private TextView bathroomsTextView;
    private TextView bedroomsTextView;
    private TextView locationTextView;
    private ImageView mapImageView;

    public static PropertyDetailFragment newInstance(Property property) {
        PropertyDetailFragment fragment = new PropertyDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROPERTY, property);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail, container, false);

        // Initialisation des vues
        photoRecyclerView = view.findViewById(R.id.property_photos);
        descriptionTextView = view.findViewById(R.id.property_description);
        surfaceTextView = view.findViewById(R.id.property_surface);
        roomsTextView = view.findViewById(R.id.property_rooms);
        bathroomsTextView = view.findViewById(R.id.property_bathrooms);
        bedroomsTextView = view.findViewById(R.id.property_bedrooms);
        locationTextView = view.findViewById(R.id.property_location);
        mapImageView = view.findViewById(R.id.property_map);

        // Configuration RecyclerView pour les photos
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PhotoAdapter photoAdapter = new PhotoAdapter(false, null);  // Pas de suppression en mode lecture seule
        photoRecyclerView.setAdapter(photoAdapter);

        // Initialisation des ViewModels avec ViewModelFactory
        propertyDetailViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(PropertyDetailViewModel.class);

        mapViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(MapViewModel.class);

        if (getArguments() != null && getArguments().containsKey(ARG_PROPERTY)) {
            selectedProperty = getArguments().getParcelable(ARG_PROPERTY);
            propertyDetailViewModel.selectProperty(selectedProperty);
        }

        // Observer les données du ViewModel
        propertyDetailViewModel.getSelectedProperty().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                selectedProperty = property; // Stocke la propriété pour l'édition
                descriptionTextView.setText(property.description);
                surfaceTextView.setText(property.surface + " m²");
                roomsTextView.setText(String.valueOf(property.numberOfRooms));
                bathroomsTextView.setText(String.valueOf(property.numberOfBathrooms));
                bedroomsTextView.setText(String.valueOf(property.numberOfBedrooms));
                locationTextView.setText(formatPropertyLocation(property.address));
                photoAdapter.setPhotos(property.photos);

                // Charger la carte via la méthode
                loadStaticMap(property.address);
            }
        });

        setupMenu(); // Ajout du menu dynamique

        return view;
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu); // Charge le menu spécifique au fragment
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_edit_property) {
                    openEditProperty();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void openEditProperty() {
        if (selectedProperty != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("property_id", selectedProperty.id);

            EditPropertyFragment editFragment = new EditPropertyFragment();
            editFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(getContext(), "No property selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatPropertyLocation(Address address) {
        return address.street + "\n"
                + address.city + "\n"
                + address.state + " " + address.zipCode + "\n"
                + address.country;
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