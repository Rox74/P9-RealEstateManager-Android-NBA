package com.openclassrooms.realestatemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import java.util.List;

public class PropertyListViewModel extends ViewModel {
    private final PropertyRepository repository;
    private final LiveData<List<Property>> allProperties;

    public PropertyListViewModel(PropertyRepository repository) {
        this.repository = repository;
        this.allProperties = repository.getAllProperties();
    }

    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }
}