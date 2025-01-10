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

/**
 * Unit tests for the PropertyListViewModel class.
 * This class verifies the retrieval, filtering, and reset functionality for properties.
 */
@RunWith(MockitoJUnitRunner.class)
public class PropertyListViewModelTest {

    private PropertyListViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MutableLiveData<List<Property>> allPropertiesLiveData;
    private MutableLiveData<List<Property>> filteredPropertiesLiveData;

    /**
     * Sets up the test environment before each test.
     * Initializes the mocked PropertyRepository and ViewModel instance.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simulate LiveData for all properties
        allPropertiesLiveData = new MutableLiveData<>();
        filteredPropertiesLiveData = new MutableLiveData<>();

        when(propertyRepository.getAllProperties()).thenReturn(allPropertiesLiveData);

        viewModel = new PropertyListViewModel(propertyRepository);
    }

    /**
     * Tests `getAllProperties()` - Ensures that all properties are correctly retrieved.
     */
    @Test
    public void getAllProperties_returnsAllProperties() throws InterruptedException {
        // GIVEN - A simulated list of properties
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

        // WHEN - Retrieving the list via ViewModel
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getAllProperties());

        // THEN - Verify that the correct properties are returned
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("House", result.get(0).type);
        assertEquals("Apartment", result.get(1).type);
    }

    /**
     * Tests `searchProperties()` - Ensures that properties are correctly filtered based on search criteria.
     */
    @Test
    public void searchProperties_appliesFilter() throws InterruptedException {
        // GIVEN - A simulated filtered property list
        List<Property> filteredProperties = Collections.singletonList(
                new Property("Penthouse", 900000, 200, 6, 3, 4,
                        "Luxury penthouse with skyline view",
                        new Address("Main Street", "San Francisco", "CA", "94101", "USA"),
                        new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Luxury Realty")
        );

        SearchCriteria criteria = new SearchCriteria(500000, 1000000, 150, 300, 3, 0, null);

        when(propertyRepository.searchProperties(criteria)).thenReturn(filteredPropertiesLiveData);
        filteredPropertiesLiveData.setValue(filteredProperties);

        // WHEN - Applying search criteria
        viewModel.searchProperties(criteria);
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getFilteredProperties());

        // THEN - Verify that only the filtered properties are returned
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Penthouse", result.get(0).type);
    }

    /**
     * Tests `resetSearch()` - Ensures that all properties are displayed again after resetting the search filter.
     */
    @Test
    public void resetSearch_displaysAllPropertiesAgain() throws InterruptedException {
        // GIVEN - Two scenarios: filtered properties, then resetting
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

        // WHEN - Resetting the search filter
        viewModel.resetSearch();
        List<Property> result = LiveDataTestUtil.getValue(viewModel.getFilteredProperties());

        // THEN - Verify that all properties are displayed again
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}