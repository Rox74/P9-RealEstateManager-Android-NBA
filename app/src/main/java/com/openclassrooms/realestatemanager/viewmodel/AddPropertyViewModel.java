package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

public class AddPropertyViewModel extends ViewModel {
    private final PropertyRepository propertyRepository;

    public AddPropertyViewModel(PropertyRepository repository) {
        this.propertyRepository = repository;
    }

    public LiveData<Boolean> insertProperty(Property property) {
        return propertyRepository.insert(property);
    }
}