package com.openclassrooms.realestatemanager.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.repository.MapRepository;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodel.EditPropertyViewModel;
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.viewmodel.PropertyListViewModel;

/**
 * ViewModelFactory is a singleton factory that provides ViewModel instances.
 * It ensures that all ViewModels are created with their necessary dependencies.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE; // Singleton instance
    private final PropertyRepository propertyRepository;
    private final MapRepository mapRepository;

    /**
     * Private constructor to enforce the singleton pattern.
     * Initializes repositories required by ViewModels.
     *
     * @param application The Application instance used to initialize repositories.
     */
    private ViewModelFactory(Application application) {
        PropertyDatabase database = PropertyDatabase.getInstance(application);
        this.propertyRepository = new PropertyRepository(application);
        this.mapRepository = new MapRepository();
    }

    /**
     * Returns the singleton instance of ViewModelFactory.
     * Uses double-checked locking to ensure thread safety.
     *
     * @param application The Application instance required for initialization.
     * @return The singleton instance of ViewModelFactory.
     */
    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) { // Ensures thread safety
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Creates and returns an instance of the requested ViewModel.
     * Provides the required repository to each ViewModel.
     *
     * @param modelClass The ViewModel class to instantiate.
     * @return The ViewModel instance.
     * @throws IllegalArgumentException if the ViewModel class is unknown.
     */
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyListViewModel.class)) {
            return (T) new PropertyListViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(PropertyDetailViewModel.class)) {
            return (T) new PropertyDetailViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(AddPropertyViewModel.class)) {
            return (T) new AddPropertyViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(EditPropertyViewModel.class)) {
            return (T) new EditPropertyViewModel(propertyRepository);
        } else if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(mapRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}