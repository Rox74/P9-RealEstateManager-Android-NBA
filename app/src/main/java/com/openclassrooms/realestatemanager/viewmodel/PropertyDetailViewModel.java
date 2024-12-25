package com.openclassrooms.realestatemanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

public class PropertyDetailViewModel extends AndroidViewModel {
    private PropertyRepository repository;
    private MutableLiveData<Property> selectedProperty;

    public PropertyDetailViewModel(@NonNull Application application) {
        super(application);
        repository = new PropertyRepository(application);
        selectedProperty = new MutableLiveData<>();
    }

    public LiveData<Property> getSelectedProperty() {
        return selectedProperty;
    }

    public void selectProperty(Property property) {
        selectedProperty.setValue(property);
    }
}