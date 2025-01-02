package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

public class AddPropertyViewModel extends ViewModel {

    private final PropertyRepository propertyRepository;

    public AddPropertyViewModel(PropertyRepository repository) {
        this.propertyRepository = repository;
    }

    public void insertProperty(Property property) {
        propertyRepository.insert(property);
    }
}