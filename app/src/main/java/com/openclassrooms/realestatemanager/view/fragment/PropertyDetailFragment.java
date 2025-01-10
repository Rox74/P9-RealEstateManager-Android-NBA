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
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Fragment for displaying the details of a selected property.
 * This includes property description, images, location, and other relevant information.
 */
public class PropertyDetailFragment extends Fragment {
    private static final String ARG_PROPERTY = "property"; // Key for property argument
    private Property selectedProperty; // The selected property

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
    private TextView propertyStatusTextView;
    private TextView marketDateTextView;
    private TextView soldDateTextView;
    private TextView agentTextView;
    private LinearLayout soldDateSection; // Section for displaying sold date

    /**
     * Creates a new instance of PropertyDetailFragment with the provided property.
     *
     * @param property The property to display details for.
     * @return A new instance of PropertyDetailFragment.
     */
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

    /**
     * Initializes the PropertyDetailFragment view, setting up UI components and data observers.
     * This method inflates the layout, initializes RecyclerViews, ViewModels, and sets up data binding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_detail, container, false);

        // Initialize UI components
        photoRecyclerView = view.findViewById(R.id.property_photos);
        descriptionTextView = view.findViewById(R.id.property_description);
        surfaceTextView = view.findViewById(R.id.property_surface);
        roomsTextView = view.findViewById(R.id.property_rooms);
        bathroomsTextView = view.findViewById(R.id.property_bathrooms);
        bedroomsTextView = view.findViewById(R.id.property_bedrooms);
        locationTextView = view.findViewById(R.id.property_location);
        mapImageView = view.findViewById(R.id.property_map);
        propertyStatusTextView = view.findViewById(R.id.property_status);
        marketDateTextView = view.findViewById(R.id.property_market_date);
        soldDateTextView = view.findViewById(R.id.property_sold_date);
        soldDateSection = view.findViewById(R.id.property_sold_date_section);
        agentTextView = view.findViewById(R.id.property_agent);

        // Set up RecyclerView for property photos
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        PhotoAdapter photoAdapter = new PhotoAdapter(false, null); // Read-only mode, no deletion allowed
        photoRecyclerView.setAdapter(photoAdapter);

        // Initialize ViewModels using ViewModelFactory
        propertyDetailViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(PropertyDetailViewModel.class);

        mapViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(MapViewModel.class);

        // Retrieve property data from arguments
        if (getArguments() != null && getArguments().containsKey(ARG_PROPERTY)) {
            selectedProperty = getArguments().getParcelable(ARG_PROPERTY);
            propertyDetailViewModel.selectProperty(selectedProperty);
        }

        // Observe ViewModel data updates
        propertyDetailViewModel.getSelectedProperty().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                selectedProperty = property;
                descriptionTextView.setText(property.description);
                surfaceTextView.setText(property.surface + " m²");
                roomsTextView.setText(String.valueOf(property.numberOfRooms));
                bathroomsTextView.setText(String.valueOf(property.numberOfBathrooms));
                bedroomsTextView.setText(String.valueOf(property.numberOfBedrooms));
                locationTextView.setText(formatPropertyLocation(property.address));
                agentTextView.setText(property.agentName != null && !property.agentName.isEmpty()
                        ? property.agentName
                        : getString(R.string.not_specified));
                photoAdapter.setPhotos(property.photos);

                // Load static map image for the property location
                loadStaticMap(property.address);

                // Display property status
                propertyStatusTextView.setText(property.isSold ? "Sold" : "Available");

                // Display market entry date
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                marketDateTextView.setText(property.marketDate != null ? sdf.format(property.marketDate) : "Not specified");

                // Display sold date only if the property is sold
                if (property.isSold && property.soldDate != null) {
                    soldDateSection.setVisibility(View.VISIBLE); // Show the sold date section
                    soldDateTextView.setVisibility(View.VISIBLE); // Show the actual date
                    soldDateTextView.setText(sdf.format(property.soldDate));
                } else {
                    soldDateSection.setVisibility(View.GONE); // Hide the entire section
                    soldDateTextView.setVisibility(View.GONE); // Hide the sold date
                }
            }
        });

        // Handle click event on the map to display nearby points of interest
        mapImageView.setOnClickListener(v -> {
            if (selectedProperty != null && selectedProperty.pointsOfInterest != null && !selectedProperty.pointsOfInterest.isEmpty()) {
                PointOfInterestDialogFragment dialog = PointOfInterestDialogFragment.newInstance(selectedProperty.pointsOfInterest);
                dialog.show(getParentFragmentManager(), "PointOfInterestDialog");
            } else {
                Toast.makeText(getContext(), "No points of interest available", Toast.LENGTH_SHORT).show();
            }
        });

        setupMenu(); // Set up the dynamic menu

        // Listen for property updates after modifications
        getParentFragmentManager().setFragmentResultListener("update_property_list", this, (requestKey, bundle) -> {
            boolean updated = bundle.getBoolean("property_updated", false);
            if (updated && selectedProperty != null) {
                reloadPropertyDetails(selectedProperty.id);
            }
        });

        return view;
    }

    /**
     * Reloads the details of a property by fetching the latest data from the ViewModel.
     * This ensures that the displayed property information is up-to-date.
     *
     * @param propertyId The ID of the property to reload.
     */
    private void reloadPropertyDetails(int propertyId) {
        propertyDetailViewModel.getPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                populateFields(property);
            }
        });
    }

    /**
     * Populates the UI fields with the details of the selected property.
     * This method updates text fields, images, and other UI elements to reflect the property data.
     *
     * @param property The property whose details will be displayed.
     */
    private void populateFields(Property property) {
        selectedProperty = property;

        descriptionTextView.setText(property.description);
        surfaceTextView.setText(property.surface + " m²");
        roomsTextView.setText(String.valueOf(property.numberOfRooms));
        bathroomsTextView.setText(String.valueOf(property.numberOfBathrooms));
        bedroomsTextView.setText(String.valueOf(property.numberOfBedrooms));
        locationTextView.setText(formatPropertyLocation(property.address));

        // Update photos in the RecyclerView
        if (photoRecyclerView.getAdapter() instanceof PhotoAdapter) {
            ((PhotoAdapter) photoRecyclerView.getAdapter()).setPhotos(property.photos);
        }

        // Reload the map with the property’s location
        loadStaticMap(property.address);
    }

    /**
     * Sets up the options menu for the fragment, allowing the user to access the edit functionality.
     * This menu is dynamically added to the activity when the fragment is displayed.
     */
    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.detail_menu, menu); // Load the property detail menu
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

    /**
     * Opens the EditPropertyFragment to allow the user to modify the selected property.
     * The selected property's ID is passed as an argument to the edit fragment.
     */
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

    /**
     * Formats the address of a property into a multi-line string for display.
     *
     * @param address The address object containing street, city, state, zip code, and country.
     * @return A formatted string representing the property location.
     */
    private String formatPropertyLocation(Address address) {
        return address.street + "\n"
                + address.city + "\n"
                + address.state + " " + address.zipCode + "\n"
                + address.country;
    }

    /**
     * Loads a static map image based on the property's address.
     * Uses Yandex Static Maps API to display the location of the property.
     *
     * @param address The address of the property.
     */
    private void loadStaticMap(Address address) {
        // Check if address information is valid
        if (address == null || address.street == null || address.city == null || address.state == null) {
            Log.e("PropertyDetailFragment", "Address is null or incomplete");
            mapImageView.setImageResource(R.drawable.ic_placeholder_map);
            return;
        }

        // Check for internet connection before making API requests
        if (!Utils.isInternetAvailable(requireContext())) {
            Log.e("PropertyDetailFragment", "No internet connection");
            mapImageView.setImageResource(R.drawable.ic_offline_map);
            return;
        }

        // Construct full address string for geocoding
        String fullAddress = address.street + ", " + address.city + ", " + address.state;
        Log.d("PropertyDetailFragment", "Fetching coordinates for address: " + fullAddress);

        // Request coordinates from ViewModel
        mapViewModel.fetchCoordinates(fullAddress);

        // Observe LiveData for successful coordinate retrieval
        mapViewModel.getMapDataLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                double lat = response.lat;
                double lon = response.lon;

                Log.d("PropertyDetailFragment", "Coordinates found: lat=" + lat + ", lon=" + lon);

                // Construct the Yandex Static Map URL with zoom level and red marker
                String mapUrl = "https://static-maps.yandex.ru/1.x/?lang=en_US&ll="
                        + lon + "," + lat
                        + "&z=17&l=map&pt=" + lon + "," + lat + ",pm2rdm";

                Log.d("PropertyDetailFragment", "Generated map URL: " + mapUrl);

                // Load map image using Glide with placeholders for loading and error handling
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

        // Observe LiveData for errors during geocoding requests
        mapViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e("PropertyDetailFragment", "Error fetching coordinates: " + error);
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}