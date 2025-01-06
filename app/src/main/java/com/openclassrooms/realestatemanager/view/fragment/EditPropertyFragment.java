package com.openclassrooms.realestatemanager.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.view.adapter.PointOfInterestAdapter;
import com.openclassrooms.realestatemanager.viewmodel.EditPropertyViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditPropertyFragment extends Fragment {

    private EditText typeEditText, priceEditText, surfaceEditText, roomsEditText,
            bathroomsEditText, bedroomsEditText, descriptionEditText,
            streetEditText, cityEditText, stateEditText, zipCodeEditText,
            countryEditText, agentEditText;
    private RecyclerView photoRecyclerView, pointsOfInterestRecyclerView;
    private PhotoAdapter photoAdapter;
    private PointOfInterestAdapter pointOfInterestAdapter;
    private Button addPhotoButton, addPointOfInterestButton, updateButton;
    private List<Photo> photos = new ArrayList<>();
    private List<PointOfInterest> pointsOfInterest = new ArrayList<>();
    private EditPropertyViewModel editPropertyViewModel;
    private Property selectedProperty;
    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_property, container, false);

        // Initialisation des vues
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
        updateButton = view.findViewById(R.id.button_update);

        // Initialisation des RecyclerView
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        photoAdapter = new PhotoAdapter(true, updatedPhotos -> {
            photos.clear();
            photos.addAll(updatedPhotos);
        });
        photoRecyclerView.setAdapter(photoAdapter);

        pointsOfInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pointOfInterestAdapter = new PointOfInterestAdapter(pointsOfInterest, true, updatedPointsOfInterest -> {
            pointsOfInterest.clear();
            pointsOfInterest.addAll(updatedPointsOfInterest);
        });
        pointsOfInterestRecyclerView.setAdapter(pointOfInterestAdapter);

        // Initialisation du ViewModel
        editPropertyViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(EditPropertyViewModel.class);

        // Récupérer l'ID de la propriété à modifier
        if (getArguments() != null) {
            int propertyId = getArguments().getInt("property_id", -1);
            if (propertyId != -1) {
                loadSelectedProperty(propertyId);
            }
        }

        // Gestion du clic sur "Add Photo"
        addPhotoButton.setOnClickListener(v -> showAddPhotoDialog());

        // Gestion du clic sur "Add Point of Interest"
        addPointOfInterestButton.setOnClickListener(v -> addPointOfInterest());

        // Gestion du clic sur "Update Property"
        updateButton.setOnClickListener(v -> updateProperty());

        // Initialisation du launcher pour sélectionner une photo
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

    private void loadSelectedProperty(int propertyId) {
        editPropertyViewModel.getPropertyById(propertyId).observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                selectedProperty = property;
                populateFields(property);
            } else {
                Log.e("EditPropertyFragment", "Property with ID " + propertyId + " not found.");
            }
        });
    }

    private void populateFields(Property property) {
        typeEditText.setText(property.type);
        priceEditText.setText(String.valueOf(property.price));
        surfaceEditText.setText(String.valueOf(property.surface));
        roomsEditText.setText(String.valueOf(property.numberOfRooms));
        bathroomsEditText.setText(String.valueOf(property.numberOfBathrooms));
        bedroomsEditText.setText(String.valueOf(property.numberOfBedrooms));
        descriptionEditText.setText(property.description);

        if (property.address != null) {
            streetEditText.setText(property.address.street);
            cityEditText.setText(property.address.city);
            stateEditText.setText(property.address.state);
            zipCodeEditText.setText(property.address.zipCode);
            countryEditText.setText(property.address.country);
        }

        agentEditText.setText(property.agentName);

        photos.clear();
        photos.addAll(property.photos);
        photoAdapter.setPhotos(photos);

        pointsOfInterest.clear();
        pointsOfInterest.addAll(property.pointsOfInterest);
        pointOfInterestAdapter.notifyDataSetChanged();
    }

    private void showAddPhotoDialog() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        photoPickerLauncher.launch(intent);
    }

    private void showPhotoDescriptionDialog(Uri imageUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo Description");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_photo, null, false);
        EditText descriptionEditText = dialogView.findViewById(R.id.edit_text_photo_description);

        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String description = descriptionEditText.getText().toString().trim();
                    if (!description.isEmpty()) {
                        photos.add(new Photo(imageUri.toString(), description));
                        photoAdapter.setPhotos(photos);
                    } else {
                        Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addPointOfInterest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Point of Interest");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_point_of_interest, null, false);
        EditText nameEditText = dialogView.findViewById(R.id.edit_text_poi_name);
        EditText typeEditText = dialogView.findViewById(R.id.edit_text_poi_type);

        builder.setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameEditText.getText().toString().trim();
                    String type = typeEditText.getText().toString().trim();
                    if (!name.isEmpty() && !type.isEmpty()) {
                        pointsOfInterest.add(new PointOfInterest(name, type));
                        pointOfInterestAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateProperty() {
        selectedProperty.type = typeEditText.getText().toString().trim();
        selectedProperty.price = Double.parseDouble(priceEditText.getText().toString().trim());
        selectedProperty.surface = Double.parseDouble(surfaceEditText.getText().toString().trim());
        selectedProperty.numberOfRooms = Integer.parseInt(roomsEditText.getText().toString().trim());
        selectedProperty.numberOfBathrooms = Integer.parseInt(bathroomsEditText.getText().toString().trim());
        selectedProperty.numberOfBedrooms = Integer.parseInt(bedroomsEditText.getText().toString().trim());
        selectedProperty.description = descriptionEditText.getText().toString().trim();

        selectedProperty.address = new Address(
                streetEditText.getText().toString().trim(),
                cityEditText.getText().toString().trim(),
                stateEditText.getText().toString().trim(),
                zipCodeEditText.getText().toString().trim(),
                countryEditText.getText().toString().trim()
        );

        // 🔹 Ajout de la mise à jour explicite des listes avant sauvegarde
        selectedProperty.photos = new ArrayList<>(photos);
        selectedProperty.pointsOfInterest = new ArrayList<>(pointsOfInterest);

        editPropertyViewModel.updateProperty(selectedProperty);
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}