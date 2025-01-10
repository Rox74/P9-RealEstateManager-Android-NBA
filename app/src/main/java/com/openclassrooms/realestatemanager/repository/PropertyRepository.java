package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.openclassrooms.realestatemanager.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class that manages access to property data.
 * This class provides methods to fetch, insert, update, and delete property records
 * in the Room database.
 */
public class PropertyRepository {

    // Data Access Object (DAO) for property-related database operations
    private final PropertyDao propertyDao;

    // LiveData list of all properties to be observed by the UI
    private final LiveData<List<Property>> allProperties;

    /**
     * Constructor that initializes the repository and database access.
     *
     * @param application The application context required to access the database.
     */
    public PropertyRepository(Application application) {
        PropertyDatabase database = PropertyDatabase.getInstance(application);
        propertyDao = database.propertyDao();
        allProperties = propertyDao.getAllProperties();
    }

    /**
     * Retrieves a LiveData list of all properties from the database.
     * LiveData ensures that the UI is automatically updated when the data changes.
     *
     * @return LiveData containing the list of properties.
     */
    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    /**
     * Inserts a property into the database asynchronously.
     * The operation is executed on a background thread to prevent UI blocking.
     *
     * @param property The property object to insert.
     * @return LiveData<Boolean> indicating success (true) or failure (false).
     */
    public LiveData<Boolean> insert(Property property) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        PropertyDatabase.databaseWriteExecutor.execute(() -> {
            long insertedId = propertyDao.insert(property);
            result.postValue(insertedId != -1); // Checks if insertion was successful (id > 0)
        });

        return result;
    }

    /**
     * Updates an existing property in the database asynchronously.
     * The operation is executed on a background thread.
     *
     * @param property The property object to update.
     */
    public void update(Property property) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> propertyDao.update(property));
    }

    /**
     * Inserts a list of mock properties into the database asynchronously.
     * This method is useful for testing or populating the database with sample data.
     *
     * @param properties List of property objects to insert.
     */
    public void insertMockData(List<Property> properties) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> {
            for (Property property : properties) {
                propertyDao.insert(property);
            }
        });
    }

    /**
     * Retrieves a specific property by its ID as LiveData.
     * LiveData ensures automatic updates if the property data changes in the database.
     *
     * @param propertyId The ID of the property to retrieve.
     * @return LiveData containing the requested property.
     */
    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyDao.getPropertyById(propertyId);
    }

    /**
     * Searches for properties based on the given search criteria.
     * Filtering for minimum number of photos is applied manually after retrieving data.
     *
     * @param criteria The search criteria containing filters such as price, surface, and location.
     * @return LiveData containing the list of filtered properties.
     */
    public LiveData<List<Property>> searchProperties(SearchCriteria criteria) {
        return Transformations.map(propertyDao.searchProperties(
                criteria.minPrice, criteria.maxPrice, criteria.minSurface, criteria.maxSurface,
                criteria.minRooms, criteria.location
        ), properties -> {
            // Manually filter properties based on the minimum number of photos required
            List<Property> filteredProperties = new ArrayList<>();
            for (Property property : properties) {
                if (criteria.minPhotos == 0 || property.photos.size() >= criteria.minPhotos) {
                    filteredProperties.add(property);
                }
            }
            return filteredProperties;
        });
    }

    /**
     * Deletes all properties from the database asynchronously.
     * This is useful for testing or resetting the database.
     */
    public void deleteAllProperties() {
        PropertyDatabase.databaseWriteExecutor.execute(propertyDao::deleteAllProperties);
    }
}