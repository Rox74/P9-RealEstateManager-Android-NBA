package com.openclassrooms.realestatemanager.model.entity;

/**
 * Model class representing a response from the Nominatim API.
 * It contains the latitude and longitude of a queried location.
 */
public class NominatimResponse {
    public double lat; // Latitude of the location
    public double lon; // Longitude of the location
}