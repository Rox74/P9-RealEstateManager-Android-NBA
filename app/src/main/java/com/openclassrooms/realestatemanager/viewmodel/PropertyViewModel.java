package com.openclassrooms.realestatemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import java.util.List;

public class PropertyViewModel extends AndroidViewModel {
    private PropertyRepository repository;
    private LiveData<List<Property>> allProperties;

    public PropertyViewModel(@NonNull Application application) {
        super(application);
        repository = new PropertyRepository(application);
        allProperties = repository.getAllProperties();
    }

    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    public void insert(Property property) {
        repository.insert(property);
    }

    public void update(Property property) {
        repository.update(property);
    }
}