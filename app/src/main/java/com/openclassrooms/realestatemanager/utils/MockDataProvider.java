package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.model.entity.Address;
import com.openclassrooms.realestatemanager.model.entity.Photo;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockDataProvider {

    public static List<Property> getMockProperties() {
        List<Property> mockProperties = new ArrayList<>();

        // Propriété 1 - Condo
        Address address1 = new Address(
                "127 W 57th St",
                "Manhattan",
                "NY",
                "10019",
                "USA"
        );

        List<Photo> photos1 = new ArrayList<>();
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo1", "Living Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo2", "Master Bedroom"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo3", "Office Room"));
        photos1.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home1_photo4", "Dining Room"));

        List<PointOfInterest> pointsOfInterest1 = new ArrayList<>();
        pointsOfInterest1.add(new PointOfInterest("Central Park", "Park"));
        pointsOfInterest1.add(new PointOfInterest("Broadway Theater", "Entertainment"));

        mockProperties.add(new Property(
                "Condo",
                9800000,
                1072,
                8, // Rooms
                2, // Bathrooms
                4, // Bedrooms
                "Luxury condo featuring 4 bedrooms, 2 baths, and breathtaking views of Manhattan.",
                address1,
                photos1,
                pointsOfInterest1,
                false, // Toujours disponible
                new Date(), // Entrée sur le marché (date actuelle)
                null, // Pas encore vendu
                "Realtor"
        ));

        // Propriété 2 - Maison à Montauk
        Address address2 = new Address(
                "408 Old Montauk Hwy",
                "Montauk",
                "NY",
                "11954",
                "USA"
        );

        List<Photo> photos2 = new ArrayList<>();
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo1", "Exterior View"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo2", "Living Room"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo3", "Kitchen"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo4", "Master Bedroom"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo5", "Backyard"));
        photos2.add(new Photo("android.resource://com.openclassrooms.realestatemanager/drawable/home2_photo6", "Pool Area"));

        List<PointOfInterest> pointsOfInterest2 = new ArrayList<>();
        pointsOfInterest2.add(new PointOfInterest("Montauk Beach", "Nature"));
        pointsOfInterest2.add(new PointOfInterest("Montauk Lighthouse", "Historical"));

        mockProperties.add(new Property(
                "Single Family Residence",
                950000,
                374,
                3, // Rooms
                1, // Bathrooms
                1, // Bedrooms
                "Charming family residence with 1 bedroom, 1 bath, and a large backyard, located in Montauk.",
                address2,
                photos2,
                pointsOfInterest2,
                false, // Toujours disponible
                new Date(), // Entrée sur le marché (date actuelle)
                null, // Pas encore vendu
                "Zillow"
        ));

        return mockProperties;
    }
}