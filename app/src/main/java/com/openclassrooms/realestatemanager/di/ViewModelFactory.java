package com.openclassrooms.realestatemanager.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.repository.MapRepository;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyListViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final PropertyRepository propertyRepository;
    private final MapRepository mapRepository;

    private ViewModelFactory(Application application) {
        PropertyDatabase database = PropertyDatabase.getInstance(application);
        this.propertyRepository = new PropertyRepository(application);
        this.mapRepository = new MapRepository();
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyListViewModel.class)) {
            return (T) new PropertyListViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(PropertyDetailViewModel.class)) {
            return (T) new PropertyDetailViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(AddPropertyViewModel.class)) {
            return (T) new AddPropertyViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(mapRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}