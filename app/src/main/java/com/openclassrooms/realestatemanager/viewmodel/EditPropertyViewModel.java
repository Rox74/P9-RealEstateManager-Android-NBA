package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

public class EditPropertyViewModel extends ViewModel {

    private final PropertyRepository propertyRepository;

    public EditPropertyViewModel(PropertyRepository repository) {
        this.propertyRepository = repository;
    }

    // Mettre à jour une propriété existante
    public void updateProperty(Property property) {
        propertyRepository.update(property);
    }

    // Récupérer une propriété spécifique par son ID
    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyRepository.getPropertyById(propertyId);
    }
}