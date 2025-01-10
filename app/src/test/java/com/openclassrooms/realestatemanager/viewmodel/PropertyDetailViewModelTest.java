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

@RunWith(MockitoJUnitRunner.class)
public class PropertyDetailViewModelTest {

    private PropertyDetailViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new PropertyDetailViewModel(propertyRepository);
    }

    /**
     * Test `selectProperty()` - Vérifie que la propriété sélectionnée est bien stockée.
     */
    @Test
    public void selectProperty_setsSelectedProperty() throws InterruptedException {
        // GIVEN - Une propriété fictive
        Property property = new Property("House", 450000, 180, 5, 2, 3,
                "Beautiful house with garden",
                new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith");

        // WHEN - Sélection de la propriété
        viewModel.selectProperty(property);
        Property selected = LiveDataTestUtil.getValue(viewModel.getSelectedProperty());

        // THEN - Vérifie que la bonne propriété est stockée
        assertNotNull(selected);
        assertEquals("House", selected.type);
        assertEquals(450000, selected.price, 0.01);
    }

    /**
     * Test `getPropertyById()` - Vérifie que la bonne propriété est récupérée par son ID.
     */
    @Test
    public void getPropertyById_returnsProperty() throws InterruptedException {
        // GIVEN - Une propriété fictive
        Property property = new Property("Apartment", 300000, 100, 4, 1, 2,
                "Nice apartment in downtown",
                new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe");

        MutableLiveData<Property> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(property);

        // Simulation du comportement du repository
        when(propertyRepository.getPropertyById(1)).thenReturn(expectedLiveData);

        // WHEN - Récupération de la propriété
        LiveData<Property> resultLiveData = viewModel.getPropertyById(1);
        Property result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Vérifie que la bonne propriété est retournée
        assertNotNull(result);
        assertEquals("Apartment", result.type);
        assertEquals("New York", result.address.city);
        verify(propertyRepository).getPropertyById(1);
    }
}