package com.openclassrooms.realestatemanager.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.openclassrooms.realestatemanager.notification.NotificationHelper;
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.view.adapter.PointOfInterestAdapter;
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPropertyFragment extends Fragment {

    private EditText typeEditText, priceEditText, surfaceEditText, roomsEditText,
            bathroomsEditText, bedroomsEditText, descriptionEditText,
            streetEditText, cityEditText, stateEditText, zipCodeEditText,
            countryEditText, agentEditText;
    private RecyclerView photoRecyclerView, pointsOfInterestRecyclerView;
    private PhotoAdapter photoAdapter;
    private PointOfInterestAdapter pointOfInterestAdapter;
    private Button addPhotoButton, addPointOfInterestButton, saveButton;
    private List<Photo> photos = new ArrayList<>();
    private List<PointOfInterest> pointsOfInterest = new ArrayList<>();
    private AddPropertyViewModel addPropertyViewModel;

    private ActivityResultLauncher<Intent> photoPickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_property, container, false);

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
        saveButton = view.findViewById(R.id.button_save);

        // Initialisation des RecyclerView
        photoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        photoAdapter = new PhotoAdapter(true, updatedPhotos -> {
            photos.clear();
            photos.addAll(updatedPhotos);
            photoAdapter.setPhotos(photos); // Mise à jour explicite
        });
        photoRecyclerView.setAdapter(photoAdapter);

        pointsOfInterestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pointOfInterestAdapter = new PointOfInterestAdapter(pointsOfInterest, true, updatedPointsOfInterest -> {
            pointsOfInterest.clear();
            pointsOfInterest.addAll(updatedPointsOfInterest);
        });
        pointsOfInterestRecyclerView.setAdapter(pointOfInterestAdapter);

        // Initialisation du ViewModel
        addPropertyViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(AddPropertyViewModel.class);

        // Gestion du clic sur "Add Photo"
        addPhotoButton.setOnClickListener(v -> showAddPhotoDialog());

        // Gestion du clic sur "Add Point of Interest"
        addPointOfInterestButton.setOnClickListener(v -> addPointOfInterest());

        // Gestion du clic sur "Save"
        saveButton.setOnClickListener(v -> saveProperty());

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
                        PointOfInterest newPOI = new PointOfInterest(name, type);
                        pointsOfInterest.add(newPOI); // Ajout à la liste principale
                        pointOfInterestAdapter.setPointsOfInterest(new ArrayList<>(pointsOfInterest)); // Mise à jour adaptateur
                    } else {
                        Toast.makeText(getContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveProperty() {
        // Mise à jour des listes avec les dernières suppressions et ajouts
        photoAdapter.setPhotos(new ArrayList<>(photos));
        pointOfInterestAdapter.setPointsOfInterest(new ArrayList<>(pointsOfInterest));

        String type = typeEditText.getText().toString().trim();
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        double surface = Double.parseDouble(surfaceEditText.getText().toString().trim());
        int rooms = Integer.parseInt(roomsEditText.getText().toString().trim());
        int bathrooms = Integer.parseInt(bathroomsEditText.getText().toString().trim());
        int bedrooms = Integer.parseInt(bedroomsEditText.getText().toString().trim());
        String description = descriptionEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();
        String agentName = agentEditText.getText().toString().trim();

        if (type.isEmpty() || price <= 0 || surface <= 0 || street.isEmpty() || city.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = new Address(street, city, state, zipCode, country);

        Property property = new Property(
                type, price, surface, rooms, bathrooms, bedrooms, description,
                address, new ArrayList<>(photos), new ArrayList<>(pointsOfInterest), false, new Date(), null, agentName
        );

        // Observer l'insertion et attendre le retour avant d'afficher la notification
        addPropertyViewModel.insertProperty(property).observe(getViewLifecycleOwner(), success -> {
            if (Boolean.TRUE.equals(success)) {
                // Notification uniquement si l'insertion a réussi
                NotificationHelper.showNotification(
                        requireContext(),
                        "Property Added",
                        "The property at " + address.street + ", " + address.city + " has been added successfully."
                );

                // Envoyer un signal de mise à jour après l'ajout
                Bundle result = new Bundle();
                result.putBoolean("property_updated", true);
                getParentFragmentManager().setFragmentResult("update_property_list", result);

                // Fermer le fragment après ajout
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                Toast.makeText(getContext(), "Error saving property. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}