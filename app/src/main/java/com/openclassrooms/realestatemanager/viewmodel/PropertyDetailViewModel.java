package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

public class PropertyDetailViewModel extends ViewModel {
    private final PropertyRepository repository;
    private final MutableLiveData<Property> selectedProperty = new MutableLiveData<>();

    public PropertyDetailViewModel(PropertyRepository repository) {
        this.repository = repository;
    }

    public LiveData<Property> getSelectedProperty() {
        return selectedProperty;
    }

    public void selectProperty(Property property) {
        selectedProperty.setValue(property);
    }

    public LiveData<Property> getPropertyById(int propertyId) {
        return repository.getPropertyById(propertyId);
    }
}