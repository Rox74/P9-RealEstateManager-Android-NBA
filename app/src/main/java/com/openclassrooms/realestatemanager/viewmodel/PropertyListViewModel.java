package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import java.util.List;

/**
 * ViewModel responsible for managing the list of properties.
 * It provides LiveData for all properties and filtered properties based on search criteria.
 */
public class PropertyListViewModel extends ViewModel {

    // Repository handling property-related data operations
    private final PropertyRepository repository;

    // LiveData containing the full list of properties
    private final LiveData<List<Property>> allProperties;

    // MutableLiveData holding the search criteria set by the user
    private final MutableLiveData<SearchCriteria> searchCriteria = new MutableLiveData<>();

    // LiveData containing the filtered list of properties based on the search criteria
    private final LiveData<List<Property>> filteredProperties;

    /**
     * Constructor that initializes the ViewModel with a PropertyRepository instance.
     * It observes search criteria and dynamically updates the filtered properties list.
     *
     * @param repository The repository handling property data operations.
     */
    public PropertyListViewModel(PropertyRepository repository) {
        this.repository = repository;
        this.allProperties = repository.getAllProperties();

        // Updates the filtered properties list whenever search criteria change
        this.filteredProperties = Transformations.switchMap(searchCriteria, criteria -> {
            if (criteria == null) {
                return allProperties; // If no criteria, return all properties
            } else {
                return repository.searchProperties(criteria); // Return filtered properties
            }
        });

        // Initialize search criteria to null (no filter applied)
        searchCriteria.setValue(null);
    }

    /**
     * Returns LiveData containing the full list of properties.
     * UI components can observe this LiveData to receive automatic updates.
     *
     * @return LiveData containing all properties.
     */
    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    /**
     * Returns LiveData containing the filtered list of properties based on search criteria.
     * UI components can observe this LiveData for automatic updates.
     *
     * @return LiveData containing the filtered properties.
     */
    public LiveData<List<Property>> getFilteredProperties() {
        return filteredProperties;
    }

    /**
     * Applies the given search criteria and updates the filtered properties list.
     * This triggers an update in the observed LiveData.
     *
     * @param criteria The search criteria to apply.
     */
    public void searchProperties(SearchCriteria criteria) {
        searchCriteria.setValue(criteria);
    }

    /**
     * Resets the search criteria, causing the filtered properties list to display all properties again.
     */
    public void resetSearch() {
        searchCriteria.setValue(null);
    }
}