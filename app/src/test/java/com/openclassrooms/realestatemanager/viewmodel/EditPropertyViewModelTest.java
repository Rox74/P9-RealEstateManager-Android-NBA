package com.openclassrooms.realestatemanager.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.repository.PropertyRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for the EditPropertyViewModel class.
 * This class tests the update and retrieval of properties.
 */
@RunWith(MockitoJUnitRunner.class)
public class EditPropertyViewModelTest {

    private EditPropertyViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    /**
     * Sets up the test environment before each test.
     * Initializes the mocked PropertyRepository and the ViewModel instance.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new EditPropertyViewModel(propertyRepository);
    }

    /**
     * Tests the successful update of a property.
     * Ensures that the repository correctly processes the property update.
     */
    @Test
    public void updateProperty_success() {
        // GIVEN - An existing property object
        Property property = new Property("House", 350000, 150, 5, 2, 3,
                "Beautiful house with garden",
                new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith");

        // WHEN - Calling updateProperty()
        viewModel.updateProperty(property);

        // THEN - Verify that the update method in the repository was called
        verify(propertyRepository).update(property);
    }

    /**
     * Tests retrieving a property by its ID.
     * Ensures that the repository correctly returns the expected property.
     */
    @Test
    public void getPropertyById_success() throws InterruptedException {
        // GIVEN - An existing property with a specific ID
        int propertyId = 0;
        Property property = new Property("Villa", 500000, 200, 6, 3, 4,
                "Luxury villa with swimming pool",
                new Address("Palm Street", "Miami", "FL", "33001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Michael Johnson");

        // Simulating the repository returning a LiveData object containing the property
        MutableLiveData<Property> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(property);
        TimeUnit.MILLISECONDS.sleep(500); // Wait for the insertion

        when(propertyRepository.getPropertyById(propertyId)).thenReturn(expectedLiveData);

        // WHEN - Calling getPropertyById()
        LiveData<Property> resultLiveData = viewModel.getPropertyById(propertyId);
        Property result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Verify that the retrieved property matches the expected one
        assertNotNull(result);
        assertEquals(propertyId, result.id);
        assertEquals("Villa", result.type);
        assertEquals("Miami", result.address.city);
        verify(propertyRepository).getPropertyById(propertyId);
    }
}