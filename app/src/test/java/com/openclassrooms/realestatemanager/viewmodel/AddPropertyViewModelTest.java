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

@RunWith(MockitoJUnitRunner.class)
public class AddPropertyViewModelTest {

    private AddPropertyViewModel viewModel;

    @Mock
    private PropertyRepository propertyRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new AddPropertyViewModel(propertyRepository);
    }

    @Test
    public void insertProperty_success() throws InterruptedException {
        // GIVEN - Une propriété valide
        Property property = new Property("Apartment", 250000, 120, 3, 1, 2,
                "Spacious apartment in NYC",
                new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe");

        // Simulation du comportement de propertyRepository.insert()
        MutableLiveData<Boolean> expectedResult = new MutableLiveData<>();
        expectedResult.setValue(true);
        when(propertyRepository.insert(property)).thenReturn(expectedResult);

        // WHEN - Appel à insertProperty()
        LiveData<Boolean> resultLiveData = viewModel.insertProperty(property);
        Boolean result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Vérification que l'insertion a bien été effectuée
        assertNotNull(result);
        assertTrue(result);
        verify(propertyRepository).insert(property); // Vérifie que la méthode a bien été appelée
    }

    @Test
    public void insertProperty_failure() throws InterruptedException {
        // GIVEN - Une propriété invalide (juste pour simuler un échec)
        Property property = new Property("", 0, 0, 0, 0, 0,
                "", new Address("", "", "", "", ""),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "");

        // Simulation d'un échec d'insertion
        MutableLiveData<Boolean> expectedResult = new MutableLiveData<>();
        expectedResult.setValue(false);
        when(propertyRepository.insert(property)).thenReturn(expectedResult);

        // WHEN - Appel à insertProperty()
        LiveData<Boolean> resultLiveData = viewModel.insertProperty(property);
        Boolean result = LiveDataTestUtil.getValue(resultLiveData);

        // THEN - Vérification que l'insertion a échoué
        assertNotNull(result);
        assertFalse(result);
        verify(propertyRepository).insert(property);
    }
}