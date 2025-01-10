package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

/**
 * ViewModel responsible for editing existing properties.
 * This ViewModel interacts with the PropertyRepository to update property details.
 */
public class EditPropertyViewModel extends ViewModel {

    // Repository instance for managing property data
    private final PropertyRepository propertyRepository;

    /**
     * Constructor that initializes the ViewModel with a PropertyRepository instance.
     *
     * @param repository The repository handling property data operations.
     */
    public EditPropertyViewModel(PropertyRepository repository) {
        this.propertyRepository = repository;
    }

    /**
     * Updates an existing property in the database.
     * This operation is performed asynchronously.
     *
     * @param property The property object with updated values.
     */
    public void updateProperty(Property property) {
        propertyRepository.update(property);
    }

    /**
     * Retrieves a specific property by its ID as LiveData.
     * LiveData ensures that any UI component observing this data gets updates when the data changes.
     *
     * @param propertyId The ID of the property to retrieve.
     * @return LiveData containing the requested property.
     */
    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyRepository.getPropertyById(propertyId);
    }
}