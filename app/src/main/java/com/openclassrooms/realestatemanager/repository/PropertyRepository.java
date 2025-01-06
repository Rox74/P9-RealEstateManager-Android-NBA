package com.openclassrooms.realestatemanager.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.List;

public class PropertyRepository {
    private final PropertyDao propertyDao;
    private final LiveData<List<Property>> allProperties;

    public PropertyRepository(Application application) {
        PropertyDatabase database = PropertyDatabase.getInstance(application);
        propertyDao = database.propertyDao();
        allProperties = propertyDao.getAllProperties();
    }

    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    public void insert(Property property) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> propertyDao.insert(property));
    }

    public void update(Property property) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> propertyDao.update(property));
    }

    // Méthode pour insérer les données mockées
    public void insertMockData(List<Property> properties) {
        PropertyDatabase.databaseWriteExecutor.execute(() -> {
            for (Property property : properties) {
                propertyDao.insert(property);
            }
        });
    }

    public LiveData<Property> getPropertyById(int propertyId) {
        return propertyDao.getPropertyById(propertyId);
    }
}