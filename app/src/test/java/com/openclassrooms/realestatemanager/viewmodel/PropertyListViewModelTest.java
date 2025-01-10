package com.openclassrooms.realestatemanager.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PropertyListViewModelTest {

    private PropertyListViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MutableLiveData<List<Property>> allPropertiesLiveData;
    private MutableLiveData<List<Property>> filteredPropertiesLiveData;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simuler LiveData de toutes les propriétés
        allPropertiesLiveData = new MutableLiveData<>();
        filteredPropertiesLiveData = new MutableLiveData<>();

        when(propertyRepository.getAllProperties()).thenReturn(allPropertiesLiveData);

        viewModel = new PropertyListViewModel(propertyRepository);
    }

    /**
     * Test `getAllProperties()` - Vérifie que toutes les propriétés sont bien retournées.
     */
    @Test
    public void getAllProperties_returnsAllProperties() throws InterruptedException {
        // GIVEN - Une liste de propriétés simulée
        List<Property> properties = Arrays.asList(
                new Property("House", 450000, 180, 5, 2, 3,
                        "Beautiful house with garden",
                        new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith"),
                new Property("Apartment", 300000, 100, 4, 1, 2,
                        "Nice apartment in downtown",
                        new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe")
        );

        allPropertiesLiveData.setValue(properties);

        // WHEN - Récupération de la liste via le ViewModel
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getAllProperties());

        // THEN - Vérifie que les bonnes propriétés sont retournées
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("House", result.get(0).type);
        assertEquals("Apartment", result.get(1).type);
    }

    /**
     * Test `searchProperties()` - Vérifie que les propriétés filtrées sont bien retournées.
     */
    @Test
    public void searchProperties_appliesFilter() throws InterruptedException {
        // GIVEN - Une liste filtrée simulée
        List<Property> filteredProperties = Collections.singletonList(
                new Property("Penthouse", 900000, 200, 6, 3, 4,
                        "Luxury penthouse with skyline view",
                        new Address("Main Street", "San Francisco", "CA", "94101", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Luxury Realty")
        );

        SearchCriteria criteria = new SearchCriteria(500000, 1000000, 150, 300, 3, 0, null);

        when(propertyRepository.searchProperties(criteria)).thenReturn(filteredPropertiesLiveData);
        filteredPropertiesLiveData.setValue(filteredProperties);

        // WHEN - Application des critères de recherche
        viewModel.searchProperties(criteria);
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getFilteredProperties());

        // THEN - Vérifie que seules les propriétés filtrées sont retournées
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Penthouse", result.get(0).type);
    }

    /**
     * Test `resetSearch()` - Vérifie que toutes les propriétés sont réaffichées après un reset.
     */
    @Test
    public void resetSearch_displaysAllPropertiesAgain() throws InterruptedException {
        // GIVEN - Deux scénarios : propriétés filtrées puis réinitialisation
        List<Property> properties = Arrays.asList(
                new Property("House", 450000, 180, 5, 2, 3,
                        "Beautiful house with garden",
                        new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith"),
                new Property("Apartment", 300000, 100, 4, 1, 2,
                        "Nice apartment in downtown",
                        new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe")
        );

        allPropertiesLiveData.setValue(properties);

        // WHEN - Réinitialisation de la recherche
        viewModel.resetSearch();
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getFilteredProperties());

        // THEN - Vérifie que toutes les propriétés sont réaffichées
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}