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

@RunWith(MockitoJUnitRunner.class)
public class EditPropertyViewModelTest {

    private EditPropertyViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new EditPropertyViewModel(propertyRepository);
    }

    /**
     * Test de mise à jour d'une propriété.
     */
    @Test
    public void updateProperty_success() {
        // GIVEN - Une propriété existante
        Property property = new Property("House", 350000, 150, 5, 2, 3,
                "Beautiful house with garden",
                new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith");

        // WHEN - Appel à updateProperty()
        viewModel.updateProperty(property);

        // THEN - Vérification que la méthode update du repository a été appelée
        verify(propertyRepository).update(property);
    }

    /**
     * Test récupération d'une propriété par son ID.
     */
    @Test
    public void getPropertyById_success() throws InterruptedException {
        // GIVEN - Une propriété existante avec un ID spécifique
        int propertyId = 0;
        Property property = new Property("Villa", 500000, 200, 6, 3, 4,
                "Luxury villa with swimming pool",
                new Address("Palm Street", "Miami", "FL", "33001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Michael Johnson");

        MutableLiveData<Property> expectedLiveData = new MutableLiveData<>();
        expectedLiveData.setValue(property);
        TimeUnit.MILLISECONDS.sleep(500); // Attendre l'insertion

        when(propertyRepository.getPropertyById(propertyId)).thenReturn(expectedLiveData);

        // WHEN - Appel à getPropertyById()
        LiveData<Property> resultLiveData = viewModel.getPropertyById(propertyId);
        Property result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Vérification que la propriété récupérée correspond bien à l'attendue
        assertNotNull(result);
        assertEquals(propertyId, result.id);
        assertEquals("Villa", result.type);
        assertEquals("Miami", result.address.city);
        verify(propertyRepository).getPropertyById(propertyId);
    }
}