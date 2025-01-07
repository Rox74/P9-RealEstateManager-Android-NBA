package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.List;

/**
 * Repository class that manages access to property data.
 * This class provides methods to fetch, insert, and update property records
 * in the Room database.
 */
public class PropertyRepository {

    // Data Access Object (DAO) for property-related database operations
    private final PropertyDao propertyDao;

    // LiveData list of all properties to be observed by the UI
    private final LiveData<List<Property>> allProperties;

    /**
     * Constructor that initializes the repository.
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
     *
     * @return LiveData containing the list of properties.
     */
    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    /**
     * Inserts a property into the database asynchronously.
     *
     * @param property The property object to insert.
     */
    public void insert(Property property) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> propertyDao.insert(property));
    }

    /**
     * Updates an existing property in the database asynchronously.
     *
     * @param property The property object to update.
     */
    public void update(Property property) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> propertyDao.update(property));
    }

    /**
     * Inserts a list of mock properties into the database asynchronously.
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
     *
     * @param propertyId The ID of the property to retrieve.
     * @return LiveData containing the requested property.
     */
    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyDao.getPropertyById(propertyId);
    }
}