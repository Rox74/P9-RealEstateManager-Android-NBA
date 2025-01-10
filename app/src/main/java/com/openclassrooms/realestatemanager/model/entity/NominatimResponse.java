package com.openclassrooms.realestatemanager.model.entity;

/**
 * Model class representing a response from the Nominatim API.
 * This class holds the latitude and longitude of a queried location.
 */
public class NominatimResponse {

    /** Latitude of the location returned by the API. */
    public double lat;

    /** Longitude of the location returned by the API. */
    public double lon;
}