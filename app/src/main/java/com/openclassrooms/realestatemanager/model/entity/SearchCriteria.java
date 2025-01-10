package com.openclassrooms.realestatemanager.model.entity;

/**
 * Model class representing search criteria for filtering property listings.
 * This class holds the constraints used for querying properties based on
 * price, surface area, number of rooms, photos, and location.
 */
public class SearchCriteria {

    public double minPrice; // Minimum price filter
    public double maxPrice; // Maximum price filter
    public double minSurface; // Minimum surface area filter (in square meters)
    public double maxSurface; // Maximum surface area filter (in square meters)
    public int minRooms; // Minimum number of rooms filter
    public int minPhotos; // Minimum number of photos required for the property
    public String location; // Location filter (city or state)

    /**
     * Constructor for defining search criteria with various property constraints.
     *
     * @param minPrice   Minimum property price.
     * @param maxPrice   Maximum property price.
     * @param minSurface Minimum surface area in square meters.
     * @param maxSurface Maximum surface area in square meters.
     * @param minRooms   Minimum number of rooms required.
     * @param minPhotos  Minimum number of photos required.
     * @param location   Location filter (city or state).
     */
    public SearchCriteria(double minPrice, double maxPrice, double minSurface, double maxSurface,
                          int minRooms, int minPhotos, String location) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minSurface = minSurface;
        this.maxSurface = maxSurface;
        this.minRooms = minRooms;
        this.minPhotos = minPhotos;
        this.location = location;
    }
}