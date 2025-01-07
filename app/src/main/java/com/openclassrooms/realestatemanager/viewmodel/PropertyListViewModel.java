package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import java.util.List;

public class PropertyListViewModel extends ViewModel {
    private final PropertyRepository repository;
    private final LiveData<List<Property>> allProperties; // Toutes les propriétés
    private final MutableLiveData<SearchCriteria> searchCriteria = new MutableLiveData<>(); // Critères de recherche
    private final LiveData<List<Property>> filteredProperties; // Propriétés filtrées

    public PropertyListViewModel(PropertyRepository repository) {
        this.repository = repository;
        this.allProperties = repository.getAllProperties();

        // Met à jour les propriétés filtrées en fonction des critères de recherche
        this.filteredProperties = Transformations.switchMap(searchCriteria, criteria -> {
            if (criteria == null) {
                return allProperties; // Si aucun critère, retourne toutes les propriétés
            } else {
                return repository.searchProperties(criteria); // Retourne les propriétés correspondant aux critères
            }
        });

        // Initialiser les critères à null (pas de filtre)
        searchCriteria.setValue(null);
    }

    /**
     * Retourne toutes les propriétés disponibles.
     */
    public LiveData<List<Property>> getAllProperties() {
        return allProperties;
    }

    /**
     * Retourne les propriétés filtrées en fonction des critères.
     */
    public LiveData<List<Property>> getFilteredProperties() {
        return filteredProperties;
    }

    /**
     * Applique les critères de recherche.
     */
    public void searchProperties(SearchCriteria criteria) {
        searchCriteria.setValue(criteria);
    }

    /**
     * Réinitialise les critères de recherche pour afficher toutes les propriétés.
     */
    public void resetSearch() {
        searchCriteria.setValue(null);
    }
}