package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

/**
 * ViewModel responsible for adding new properties.
 * This ViewModel interacts with the PropertyRepository to insert new properties into the database.
 */
public class AddPropertyViewModel extends ViewModel {

    // Repository instance to manage property data
    private final PropertyRepository propertyRepository;

    /**
     * Constructor that initializes the ViewModel with a PropertyRepository instance.
     *
     * @param repository The repository handling property data operations.
     */
    public AddPropertyViewModel(PropertyRepository repository) {
        this.propertyRepository = repository;
    }

    /**
     * Inserts a new property into the database.
     * The operation is asynchronous and returns a LiveData<Boolean> indicating success or failure.
     *
     * @param property The property object to insert.
     * @return LiveData<Boolean> that emits true if the insertion is successful, false otherwise.
     */
    public LiveData<Boolean> insertProperty(Property property) {
        return propertyRepository.insert(property);
    }
}