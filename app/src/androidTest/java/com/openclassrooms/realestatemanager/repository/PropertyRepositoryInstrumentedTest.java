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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Instrumented test for the PropertyRepository class.
 * This test verifies database operations such as insertion, update, and search.
 */
@RunWith(AndroidJUnit4.class)
public class PropertyRepositoryInstrumentedTest {

    private PropertyRepository propertyRepository;
    private PropertyDao propertyDao;
    private PropertyDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    /**
     * Sets up the in-memory database and repository before each test.
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, PropertyDatabase.class)
                .allowMainThreadQueries()
                .build();
        propertyDao = database.propertyDao();
        propertyRepository = new PropertyRepository(ApplicationProvider.getApplicationContext());

        // Clear all properties before each test to ensure isolation
        propertyRepository.deleteAllProperties();
    }

    /**
     * Tests inserting a property and retrieving it from the database.
     */
    @Test
    public void insertPropertyAndRetrieveIt() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        // GIVEN - A property instance
        Property property = new Property("Apartment", 250000, 120, 3, 1, 2,
                "Spacious apartment in NYC",
                new Address("5th Avenue", "New York", "NY", "10001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "John Doe");

        // WHEN - Insert property into database
        propertyRepository.insert(property);
        TimeUnit.MILLISECONDS.sleep(500);

        // THEN - Verify that the property is correctly inserted and retrieved
        List<Property> properties = LiveDataTestUtil.getValue(propertyRepository.getAllProperties());
        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertEquals("Apartment", properties.get(0).type);

        // Cleanup after test
        propertyRepository.deleteAllProperties();
    }

    /**
     * Tests updating an existing property in the database.
     */
    @Test
    public void updateProperty() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        // GIVEN - An initial property
        Property property = new Property("House", 350000, 150, 5, 2, 3,
                "Beautiful house with garden",
                new Address("10th Street", "Los Angeles", "CA", "90001", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Alice Smith");

        // Insert property into database
        LiveData<Boolean> insertResult = propertyRepository.insert(property);
        assertTrue(LiveDataTestUtil.getValue(insertResult));

        // Retrieve inserted property
        List<Property> properties = LiveDataTestUtil.getValue(propertyRepository.getAllProperties());
        assertNotNull(properties);
        assertFalse(properties.isEmpty()); // Ensure the list is not empty

        // Update the property details
        Property updatedProperty = properties.get(0);
        updatedProperty.price = 400000;
        updatedProperty.isSold = true;

        // WHEN - Update the property
        propertyRepository.update(updatedProperty);
        TimeUnit.MILLISECONDS.sleep(500); // Allow time for database update

        // THEN - Verify that the property is updated correctly
        Property retrievedProperty = LiveDataTestUtil.getValue(propertyRepository.getPropertyById(updatedProperty.id));
        assertNotNull(retrievedProperty);
        assertEquals(400000, retrievedProperty.price, 0.01);
        assertTrue(retrievedProperty.isSold);

        // Cleanup after test
        propertyRepository.deleteAllProperties();
    }

    /**
     * Tests searching for properties using specific criteria.
     */
    @Test
    public void searchProperties() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);

        // GIVEN - Two properties in the database
        Property property1 = new Property("Studio", 150000, 50, 2, 1, 1,
                "Small studio in downtown",
                new Address("Broadway", "New York", "NY", "10002", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Realtor");

        Property property2 = new Property("Penthouse", 950000, 180, 6, 3, 4,
                "Penthouse with skyline view",
                new Address("Main Street", "San Francisco", "CA", "94101", "USA"),
                new ArrayList<>(), new ArrayList<>(), false, new Date(), null, "Luxury Realty");

        // Insert properties
        propertyRepository.insert(property1);
        propertyRepository.insert(property2);
        TimeUnit.MILLISECONDS.sleep(500);

        // Define search criteria
        SearchCriteria criteria = new SearchCriteria(200000, 1000000, 100, 300, 3, 0, null);

        // WHEN - Search for properties
        List<Property> searchResults = LiveDataTestUtil.getValue(propertyRepository.searchProperties(criteria));

        // THEN - Verify that only matching properties are returned
        assertNotNull(searchResults);
        assertEquals(1, searchResults.size());
        assertEquals("Penthouse", searchResults.get(0).type);

        // Cleanup after test
        propertyRepository.deleteAllProperties();
    }
}