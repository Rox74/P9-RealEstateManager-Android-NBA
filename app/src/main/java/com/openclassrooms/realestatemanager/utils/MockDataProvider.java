package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Photo;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class that provides mock data for property listings.
 * This is used to populate the database with sample properties for testing.
 */
public class MockDataProvider {

    /**
     * Generates a list of mock properties to be inserted into the database.
     *
     * @return A list of predefined Property objects.
     */
    public static List<Property> getMockProperties() {
        List<Property> mockProperties = new ArrayList<>();

        // ---------- Property 1: Luxury Condo in Manhattan ----------

        // Creating address for the first property
        Address address1 = new Address(
                "127 W 57th St",
                "Manhattan",
                "NY",
                "10019",
                "USA"
        );

        // Creating a list of photos for the first property
        List<Photo> photos1 = new ArrayList<>();
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo1", "Living Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo2", "Master Bedroom"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo3", "Office Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo4", "Dining Room"));

        // Creating a list of points of interest near the property
        List<PointOfInterest> pointsOfInterest1 = new ArrayList<>();
        pointsOfInterest1.add(new PointOfInterest("Central Park", "Park"));
        pointsOfInterest1.add(new PointOfInterest("Broadway Theater", "Entertainment"));

        // Adding the first property to the list
        mockProperties.add(new Property(
                "Condo", // Property type
                9800000, // Price in USD
                1072, // Surface area in square feet
                8, // Number of rooms
                2, // Number of bathrooms
                4, // Number of bedrooms
                "Luxury condo featuring 4 bedrooms, 2 baths, and breathtaking views of Manhattan.",
                address1,
                photos1,
                pointsOfInterest1,
                false, // Property is still available (not sold)
                new Date(), // Market entry date (current date)
                null, // Not sold yet
                "Realtor" // Agent responsible for the listing
        ));

        // ---------- Property 2: Beachfront House in Montauk ----------

        // Creating address for the second property
        Address address2 = new Address(
                "408 Old Montauk Hwy",
                "Montauk",
                "NY",
                "11954",
                "USA"
        );

        // Creating a list of photos for the second property
        List<Photo> photos2 = new ArrayList<>();
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo1", "Exterior View"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo2", "Living Room"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo3", "Kitchen"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo4", "Master Bedroom"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo5", "Backyard"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo6", "Pool Area"));

        // Creating a list of points of interest near the second property
        List<PointOfInterest> pointsOfInterest2 = new ArrayList<>();
        pointsOfInterest2.add(new PointOfInterest("Montauk Beach", "Nature"));
        pointsOfInterest2.add(new PointOfInterest("Montauk Lighthouse", "Historical"));

        // Adding the second property to the list
        mockProperties.add(new Property(
                "Single Family Residence", // Property type
                950000, // Price in USD
                374, // Surface area in square feet
                3, // Number of rooms
                1, // Number of bathrooms
                1, // Number of bedrooms
                "Charming family residence with 1 bedroom, 1 bath, and a large backyard, located in Montauk.",
                address2,
                photos2,
                pointsOfInterest2,
                false, // Property is still available (not sold)
                new Date(), // Market entry date (current date)
                null, // Not sold yet
                "Zillow" // Agent responsible for the listing
        ));

        // Returning the list of mock properties
        return mockProperties;
    }
}