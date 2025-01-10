package com.openclassrooms.realestatemanager.viewmodel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

/**
 * Unit tests for the AddPropertyViewModel class.
 * This class tests the behavior of inserting properties into the repository.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddPropertyViewModelTest {

    private AddPropertyViewModel viewModel;

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
        viewModel = new AddPropertyViewModel(propertyRepository);
    }

    /**
     * Tests the successful insertion of a property.
     * Ensures that the repository correctly processes the property insertion.
     */
    @Test
    public void insertProperty_success() throws InterruptedException {
        // GIVEN - A valid property object
        Property property = new Property("Apartment", 250000, 120, 3, 1, 2,
                "Spacious apartment in NYC",
                new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe");

        // Simulate the repository's behavior when inserting a property
        MutableLiveData<Boolean> expectedResult = new MutableLiveData<>();
        expectedResult.setValue(true);
        when(propertyRepository.insert(property)).thenReturn(expectedResult);

        // WHEN - Calling insertProperty()
        LiveData<Boolean> resultLiveData = viewModel.insertProperty(property);
        Boolean result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Verify that the insertion was successful
        assertNotNull(result);
        assertTrue(result);
        verify(propertyRepository).insert(property); // Ensure the method was called on the repository
    }

    /**
     * Tests the failure scenario of inserting a property.
     * Ensures that the repository handles invalid property insertions properly.
     */
    @Test
    public void insertProperty_failure() throws InterruptedException {
        // GIVEN - An invalid property (empty fields to simulate a failure)
        Property property = new Property("", 0, 0, 0, 0, 0,
                "", new Address("", "", "", "", ""),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "");

        // Simulate a failed insertion scenario
        MutableLiveData<Boolean> expectedResult = new MutableLiveData<>();
        expectedResult.setValue(false);
        when(propertyRepository.insert(property)).thenReturn(expectedResult);

        // WHEN - Calling insertProperty()
        LiveData<Boolean> resultLiveData = viewModel.insertProperty(property);
        Boolean result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Verify that the insertion failed
        assertNotNull(result);
        assertFalse(result);
        verify(propertyRepository).insert(property); // Ensure the method was called on the repository
    }
}