package com.openclassrooms.realestatemanager.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.openclassrooms.realestatemanager.notification.NotificationHelper;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.view.adapter.PointOfInterestAdapter;
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for adding a new property.
 * Handles user input, photo and point of interest management, and saving property details.
 */
public class AddPropertyFragment extends Fragment {

    // UI Elements
    private EditText typeEditText, priceEditText, surfaceEditText, roomsEditText,
            bathroomsEditText, bedroomsEditText, descriptionEditText,
            streetEditText, cityEditText, stateEditText, zipCodeEditText,
            countryEditText, agentEditText;
    private RecyclerView photoRecyclerView, pointsOfInterestRecyclerView;
    private PhotoAdapter photoAdapter;
    private PointOfInterestAdapter pointOfInterestAdapter;
    private Button addPhotoButton, addPointOfInterestButton, saveButton;
    private Switch propertyStatusSwitch;
    private Button selectMarketDateButton, selectSoldDateButton;
    private TextView selectedMarketDateText, selectedSoldDateText;
    private LinearLayout soldDateLayout;

    // Data Storage
    private List<Photo> photos = new ArrayList<>();
    private List<PointOfInterest> pointsOfInterest = new ArrayList<>();
    private AddPropertyViewModel addPropertyViewModel;
    private Date marketDate, soldDate;

    // Activity Result Launcher for Photo Selection
    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property, container, false);

        // Initialize UI elements
        typeEditText = view.findViewById(R.id.edit_text_type);
        priceEditText = view.findViewById(R.id.edit_text_price);
        surfaceEditText = view.findViewById(R.id.edit_text_surface);
        roomsEditText = view.findViewById(R.id.edit_text_rooms);
        bathroomsEditText = view.findViewById(R.id.edit_text_bathrooms);
        bedroomsEditText = view.findViewById(R.id.edit_text_bedrooms);
        descriptionEditText = view.findViewById(R.id.edit_text_description);
        streetEditText = view.findViewById(R.id.edit_text_street);
        cityEditText = view.findViewById(R.id.edit_text_city);
        stateEditText = view.findViewById(R.id.edit_text_state);
        zipCodeEditText = view.findViewById(R.id.edit_text_zip_code);
        countryEditText = view.findViewById(R.id.edit_text_country);
        agentEditText = view.findViewById(R.id.edit_text_agent_name);
        photoRecyclerView = view.findViewById(R.id.recycler_view_photos);
        pointsOfInterestRecyclerView = view.findViewById(R.id.recycler_view_points_of_interest);
        addPhotoButton = view.findViewById(R.id.button_add_photo);
        addPointOfInterestButton = view.findViewById(R.id.button_add_point_of_interest);
        saveButton = view.findViewById(R.id.button_save);
        propertyStatusSwitch = view.findViewById(R.id.switch_property_status);
        selectMarketDateButton = view.findViewById(R.id.button_select_market_date);
        selectedMarketDateText = view.findViewById(R.id.text_selected_market_date);
        selectSoldDateButton = view.findViewById(R.id.button_select_sold_date);
        selectedSoldDateText = view.findViewById(R.id.text_selected_sold_date);
        soldDateLayout = view.findViewById(R.id.layout_sold_date);

        // Initialize market date with the current date
        marketDate = new Date();
        selectedMarketDateText.setText(Utils.getTodayDate());

        // Handle visibility of sold property-related fields
        propertyStatusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectSoldDateButton.setVisibility(View.VISIBLE);
                selectedSoldDateText.setVisibility(View.VISIBLE);
                soldDateLayout.setVisibility(View.VISIBLE); // Show sold date layout
            } else {
                selectSoldDateButton.setVisibility(View.GONE);
                selectedSoldDateText.setVisibility(View.GONE);
                soldDateLayout.setVisibility(View.GONE); // Hide layout when property is not sold
                soldDate = null;
                selectedSoldDateText.setText(R.string.not_selected);
            }
        });

        // Select market date
        selectMarketDateButton.setOnClickListener(v -> showDatePicker(date -> {
            marketDate = date;
            selectedMarketDateText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        }));

        // Select sold date
        selectSoldDateButton.setOnClickListener(v -> showDatePicker(date -> {
            soldDate = date;
            selectedSoldDateText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        }));

        // Initialize RecyclerViews
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        photoAdapter = new PhotoAdapter(true, updatedPhotos -> {
            photos.clear();
            photos.addAll(updatedPhotos);
            photoAdapter.setPhotos(photos); // Explicit update
        });
        photoRecyclerView.setAdapter(photoAdapter);

        pointsOfInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pointOfInterestAdapter = new PointOfInterestAdapter(pointsOfInterest, true, updatedPointsOfInterest -> {
            pointsOfInterest.clear();
            pointsOfInterest.addAll(updatedPointsOfInterest);
        });
        pointsOfInterestRecyclerView.setAdapter(pointOfInterestAdapter);

        // Initialize ViewModel
        addPropertyViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(AddPropertyViewModel.class);

        // Handle "Add Photo" button click
        addPhotoButton.setOnClickListener(v -> showAddPhotoDialog());

        // Handle "Add Point of Interest" button click
        addPointOfInterestButton.setOnClickListener(v -> addPointOfInterest());

        // Handle "Save" button click
        saveButton.setOnClickListener(v -> saveProperty());

        // Initialize ActivityResultLauncher for photo selection
        photoPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    showPhotoDescriptionDialog(imageUri);
                }
            }
        });

        return view;
    }

    /**
     * Displays a date picker dialog to select a date.
     * The selected date is passed to the provided listener.
     *
     * @param listener Callback to handle the selected date.
     */
    private void showDatePicker(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    listener.onDateSelected(selectedDate.getTime()); // Pass selected date to listener
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Interface for handling date selection in the date picker dialog.
     */
    interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

    /**
     * Opens the photo picker to select an image from the device gallery.
     * Uses an implicit intent with type "image/*".
     */
    private void showAddPhotoDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        photoPickerLauncher.launch(intent);
    }

    /**
     * Displays a dialog for adding a description to the selected photo.
     * The user can enter a description, and the photo is then added to the list.
     *
     * @param imageUri The URI of the selected image.
     */
    private void showPhotoDescriptionDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo Description");

        // Inflate the custom layout for the dialog
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_photo, null, false);
        EditText descriptionEditText = dialogView.findViewById(R.id.edit_text_photo_description);

        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String description = descriptionEditText.getText().toString().trim();
                    if (!description.isEmpty()) {
                        // Add the photo with its description to the list
                        photos.add(new Photo(imageUri.toString(), description));
                        photoAdapter.setPhotos(photos);
                    } else {
                        Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Displays a dialog for adding a new point of interest.
     * The user must enter a name and type before confirming the addition.
     */
    private void addPointOfInterest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Point of Interest");

        // Inflate the custom layout for the dialog
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_point_of_interest, null, false);
        EditText nameEditText = dialogView.findViewById(R.id.edit_text_poi_name);
        EditText typeEditText = dialogView.findViewById(R.id.edit_text_poi_type);

        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String type = typeEditText.getText().toString().trim();

                    if (!name.isEmpty() && !type.isEmpty()) {
                        // Create a new point of interest and add it to the list
                        PointOfInterest newPOI = new PointOfInterest(name, type);
                        pointsOfInterest.add(newPOI);

                        // Update the adapter with the new list
                        pointOfInterestAdapter.setPointsOfInterest(new ArrayList<>(pointsOfInterest));
                    } else {
                        Toast.makeText(getContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Saves the property after validating all required fields.
     * If successful, it triggers a notification and updates the property list.
     */
    private void saveProperty() {
        // Update the lists with the latest modifications
        photoAdapter.setPhotos(new ArrayList<>(photos));
        pointOfInterestAdapter.setPointsOfInterest(new ArrayList<>(pointsOfInterest));

        // Required fields
        String type = typeEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String surfaceStr = surfaceEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String agentName = agentEditText.getText().toString().trim();

        // Validate required fields
        if (type.isEmpty() || priceStr.isEmpty() || surfaceStr.isEmpty() || street.isEmpty() || city.isEmpty() || agentName.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // Convert numeric values with error handling
        double price;
        double surface;
        try {
            price = Double.parseDouble(priceStr);
            surface = Double.parseDouble(surfaceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price or surface value.", Toast.LENGTH_LONG).show();
            return;
        }

        // Optional fields with default values
        int rooms = safeParseInt(roomsEditText.getText().toString().trim());
        int bathrooms = safeParseInt(bathroomsEditText.getText().toString().trim());
        int bedrooms = safeParseInt(bedroomsEditText.getText().toString().trim());
        String description = descriptionEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();

        // Determine if the property is sold
        boolean isSold = propertyStatusSwitch.isChecked();

        // Validation: If the property is sold, the sold date must be selected
        if (isSold && soldDate == null) {
            Toast.makeText(getContext(), "Please select a sold date.", Toast.LENGTH_LONG).show();
            return;
        }

        // Create the address object
        Address address = new Address(street, city, state, zipCode, country);

        // Create the property object with the gathered data
        Property property = new Property(
                type, price, surface, rooms, bathrooms, bedrooms, description,
                address, photos.isEmpty() ? new ArrayList<>() : new ArrayList<>(photos),
                new ArrayList<>(pointsOfInterest), isSold, marketDate, soldDate, agentName
        );

        // Observe insertion and wait for the result before showing the notification
        addPropertyViewModel.insertProperty(property).observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                // Show a notification if the insertion is successful
                NotificationHelper.showNotification(
                        requireContext(),
                        "Property Added",
                        "The property at " + address.street + ", " + address.city + " has been added successfully."
                );

                // Send an update signal after adding the property
                Bundle result = new Bundle();
                result.putBoolean("property_updated", true);
                getParentFragmentManager().setFragmentResult("update_property_list", result);

                // Close the fragment after adding the property
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Error saving property. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Safely parses a string into an integer.
     * If parsing fails, it returns 0.
     *
     * @param text The input string.
     * @return The parsed integer value or 0 if invalid.
     */
    private int safeParseInt(String text) {
        try {
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0; // Return 0 in case of error
        }
    }
}