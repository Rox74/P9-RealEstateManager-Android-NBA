package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

/**
 * ViewModel responsible for managing property details.
 * This ViewModel provides functionality to select a property and retrieve property details.
 */
public class PropertyDetailViewModel extends ViewModel {

    // Repository handling property-related data operations
    private final PropertyRepository repository;

    // LiveData holding the selected property to be observed by UI components
    private final MutableLiveData<Property> selectedProperty = new MutableLiveData<>();

    /**
     * Constructor that initializes the ViewModel with a PropertyRepository instance.
     *
     * @param repository The repository handling property data operations.
     */
    public PropertyDetailViewModel(PropertyRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns LiveData containing the currently selected property.
     * UI components can observe this LiveData to update accordingly.
     *
     * @return LiveData holding the selected Property object.
     */
    public LiveData<Property> getSelectedProperty() {
        return selectedProperty;
    }

    /**
     * Sets the currently selected property.
     * This method updates the LiveData, which will notify observers automatically.
     *
     * @param property The property object to be selected.
     */
    public void selectProperty(Property property) {
        selectedProperty.setValue(property);
    }

    /**
     * Retrieves a property by its ID from the repository.
     * The returned LiveData allows automatic updates when the data changes in the database.
     *
     * @param propertyId The ID of the property to retrieve.
     * @return LiveData containing the requested Property object.
     */
    public LiveData<Property> getPropertyById(int propertyId) {
        return repository.getPropertyById(propertyId);
    }
}