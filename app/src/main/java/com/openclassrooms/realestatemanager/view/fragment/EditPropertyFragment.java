package com.openclassrooms.realestatemanager.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.openclassrooms.realestatemanager.view.adapter.PhotoAdapter;
import com.openclassrooms.realestatemanager.view.adapter.PointOfInterestAdapter;
import com.openclassrooms.realestatemanager.viewmodel.EditPropertyViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private Switch propertyStatusSwitch;
    private Button selectMarketDateButton, selectSoldDateButton;
    private TextView selectedMarketDateText, selectedSoldDateText;
    private Date marketDate, soldDate;
    private ActivityResultLauncher<Intent> photoPickerLauncher;
    private LinearLayout soldDateLayout;

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
        propertyStatusSwitch = view.findViewById(R.id.switch_property_status);
        selectMarketDateButton = view.findViewById(R.id.button_select_market_date);
        selectedMarketDateText = view.findViewById(R.id.text_selected_market_date);
        selectSoldDateButton = view.findViewById(R.id.button_select_sold_date);
        selectedSoldDateText = view.findViewById(R.id.text_selected_sold_date);
        soldDateLayout = view.findViewById(R.id.layout_sold_date);

        // Gestion de la visibilité des champs liés à la vente
        propertyStatusSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectSoldDateButton.setVisibility(View.VISIBLE);
                selectedSoldDateText.setVisibility(View.VISIBLE);
                soldDateLayout.setVisibility(View.VISIBLE); // Affiche le layout contenant la date de vente
            } else {
                selectSoldDateButton.setVisibility(View.GONE);
                selectedSoldDateText.setVisibility(View.GONE);
                soldDateLayout.setVisibility(View.GONE); // Cache le layout lorsque le bien n'est pas vendu
                soldDate = null;
                selectedSoldDateText.setText("Not selected");
            }
        });

        // Sélection de la date d’entrée sur le marché
        selectMarketDateButton.setOnClickListener(v -> showDatePicker(date -> {
            marketDate = date;
            selectedMarketDateText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        }));

        // Sélection de la date de vente
        selectSoldDateButton.setOnClickListener(v -> showDatePicker(date -> {
            soldDate = date;
            selectedSoldDateText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date));
        }));

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

    private void showDatePicker(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    listener.onDateSelected(selectedDate.getTime());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    interface OnDateSelectedListener {
        void onDateSelected(Date date);
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
        selectedProperty = property;

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

        // Initialiser les nouvelles données
        propertyStatusSwitch.setChecked(property.isSold);
        marketDate = property.marketDate != null ? property.marketDate : new Date();
        selectedMarketDateText.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(marketDate));

        if (property.isSold) {
            soldDate = property.soldDate;
            selectedSoldDateText.setText(soldDate != null ?
                    new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(soldDate) : "Not selected");
            selectSoldDateButton.setVisibility(View.VISIBLE);
            selectedSoldDateText.setVisibility(View.VISIBLE);
        } else {
            soldDate = null;
            selectedSoldDateText.setText("Not selected");
            selectSoldDateButton.setVisibility(View.GONE);
            selectedSoldDateText.setVisibility(View.GONE);
        }
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
        // Vérifier les champs obligatoires
        String type = typeEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String surfaceStr = surfaceEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String agentName = agentEditText.getText().toString().trim();

        if (type.isEmpty() || priceStr.isEmpty() || surfaceStr.isEmpty() || street.isEmpty() || city.isEmpty() || agentName.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // Convertir les valeurs numériques avec gestion des erreurs
        double price;
        double surface;
        try {
            price = Double.parseDouble(priceStr);
            surface = Double.parseDouble(surfaceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid price or surface value.", Toast.LENGTH_LONG).show();
            return;
        }

        // Champs optionnels avec valeurs par défaut
        int rooms = safeParseInt(roomsEditText.getText().toString().trim());
        int bathrooms = safeParseInt(bathroomsEditText.getText().toString().trim());
        int bedrooms = safeParseInt(bedroomsEditText.getText().toString().trim());
        String description = descriptionEditText.getText().toString().trim();
        String state = stateEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();
        String country = countryEditText.getText().toString().trim();

        // Vérifier si le bien est vendu et si une date de vente est fournie
        boolean isSold = propertyStatusSwitch.isChecked();
        if (isSold && soldDate == null) {
            Toast.makeText(getContext(), "Please select a sold date.", Toast.LENGTH_LONG).show();
            return;
        }

        // Création de l'adresse mise à jour
        Address newAddress = new Address(street, city, state, zipCode, country);

        // Vérifier si une mise à jour est nécessaire avant modification
        boolean isUpdated = false;

        if (!selectedProperty.type.equals(type)) {
            selectedProperty.type = type;
            isUpdated = true;
        }
        if (selectedProperty.price != price) {
            selectedProperty.price = price;
            isUpdated = true;
        }
        if (selectedProperty.surface != surface) {
            selectedProperty.surface = surface;
            isUpdated = true;
        }
        if (selectedProperty.numberOfRooms != rooms) {
            selectedProperty.numberOfRooms = rooms;
            isUpdated = true;
        }
        if (selectedProperty.numberOfBathrooms != bathrooms) {
            selectedProperty.numberOfBathrooms = bathrooms;
            isUpdated = true;
        }
        if (selectedProperty.numberOfBedrooms != bedrooms) {
            selectedProperty.numberOfBedrooms = bedrooms;
            isUpdated = true;
        }
        if (!selectedProperty.description.equals(description)) {
            selectedProperty.description = description;
            isUpdated = true;
        }
        if (!selectedProperty.address.equals(newAddress)) {
            selectedProperty.address = newAddress;
            isUpdated = true;
        }
        if (!selectedProperty.agentName.equals(agentName)) {
            selectedProperty.agentName = agentName;
            isUpdated = true;
        }

        // Mise à jour du statut du bien
        if (selectedProperty.isSold != isSold) {
            selectedProperty.isSold = isSold;
            isUpdated = true;
        }

        // Mise à jour des dates
        if (areDatesDifferent(selectedProperty.marketDate, marketDate)) {
            selectedProperty.marketDate = marketDate;
            isUpdated = true;
        }
        if (isSold && areDatesDifferent(selectedProperty.soldDate, soldDate)) {
            selectedProperty.soldDate = soldDate;
            isUpdated = true;
        }

        // Mise à jour explicite des listes avant sauvegarde
        if (!selectedProperty.photos.equals(photos)) {
            selectedProperty.photos = new ArrayList<>(photos);
            isUpdated = true;
        }
        if (!selectedProperty.pointsOfInterest.equals(pointsOfInterest)) {
            selectedProperty.pointsOfInterest = new ArrayList<>(pointsOfInterest);
            isUpdated = true;
        }

        // Vérifier si des changements ont été effectués avant d'envoyer la mise à jour
        if (isUpdated) {
            editPropertyViewModel.updateProperty(selectedProperty);

            // Envoyer un signal de mise à jour après modification
            Bundle result = new Bundle();
            result.putBoolean("property_updated", true);
            getParentFragmentManager().setFragmentResult("update_property_list", result);

            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "No changes detected.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Compare deux dates en tenant compte des valeurs nulles.
     */
    private boolean areDatesDifferent(Date date1, Date date2) {
        if (date1 == null && date2 == null) return false;
        if (date1 == null || date2 == null) return true;
        return !date1.equals(date2);
    }

    /**
     * Convertit une chaîne en entier avec gestion des erreurs.
     */
    private int safeParseInt(String text) {
        try {
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0; // Retourne 0 en cas d'erreur
        }
    }
}