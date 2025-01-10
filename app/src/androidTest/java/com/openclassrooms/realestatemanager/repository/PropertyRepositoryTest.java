package com.openclassrooms.realestatemanager.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Property;
import com.openclassrooms.realestatemanager.model.entity.SearchCriteria;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class PropertyRepositoryTest {

    private PropertyRepository propertyRepository;
    private PropertyDao propertyDao;
    private PropertyDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        propertyDao = database.propertyDao();
        propertyRepository = new PropertyRepository(ApplicationProvider.getApplicationContext());

        // Supprimer toutes les propriétés avant chaque test
        propertyRepository.deleteAllProperties();
    }

    /**
     * Test insertion d'un bien immobilier et vérification de sa récupération.
     */
    @Test
    public void insertPropertyAndRetrieveIt() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        Property property = new Property("Apartment", 250000, 120, 3, 1, 2,
                "Spacious apartment in NYC",
                new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe");

        propertyRepository.insert(property);
        TimeUnit.MILLISECONDS.sleep(500);

        List<Property> properties = LiveDataTestUtil.getValue(propertyRepository.getAllProperties());
        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertEquals("Apartment", properties.get(0).type);

        // Suppression des données après le test
        propertyRepository.deleteAllProperties();
    }

    /**
     * Test mise à jour d'un bien immobilier.
     */
    @Test
    public void updateProperty() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        // Création d'une propriété initiale
        Property property = new Property("House", 350000, 150, 5, 2, 3,
                "Beautiful house with garden",
                new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith");

        // Insertion du bien
        LiveData<Boolean> insertResult = propertyRepository.insert(property);
        assertTrue(LiveDataTestUtil.getValue(insertResult));

        // Récupération du bien inséré
        List<Property> properties = LiveDataTestUtil.getValue(propertyRepository.getAllProperties());
        assertNotNull(properties);
        assertFalse(properties.isEmpty()); // Vérifier que la liste n'est pas vide

        // Récupérer la propriété insérée
        Property updatedProperty = properties.get(0);
        updatedProperty.price = 400000;
        updatedProperty.isSold = true;

        // Mise à jour du bien
        propertyRepository.update(updatedProperty);
        TimeUnit.MILLISECONDS.sleep(500); // Laisser le temps à la base de données de se mettre à jour

        // Récupération du bien après mise à jour
        Property retrievedProperty = LiveDataTestUtil.getValue(propertyRepository.getPropertyById(updatedProperty.id));
        assertNotNull(retrievedProperty);
        assertEquals(400000, retrievedProperty.price, 0.01);
        assertTrue(retrievedProperty.isSold);

        // Suppression des données après le test
        propertyRepository.deleteAllProperties();
    }

    /**
     * Test recherche de biens avec critères.
     */
    @Test
    public void searchProperties() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        Property property1 = new Property("Studio", 150000, 50, 2, 1, 1,
                "Small studio in downtown",
                new Address("Broadway", "New York", "NY", "10002", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Realtor");

        Property property2 = new Property("Penthouse", 950000, 180, 6, 3, 4,
                "Penthouse with skyline view",
                new Address("Main Street", "San Francisco", "CA", "94101", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Luxury Realty");

        propertyRepository.insert(property1);
        propertyRepository.insert(property2);
        TimeUnit.MILLISECONDS.sleep(500);

        SearchCriteria criteria = new SearchCriteria(200000, 1000000, 100, 300, 3, 0, null);
        List<Property> searchResults = LiveDataTestUtil.getValue(propertyRepository.searchProperties(criteria));

        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        assertEquals("Penthouse", searchResults.get(0).type);

        // Suppression des données après le test
        propertyRepository.deleteAllProperties();
    }
}