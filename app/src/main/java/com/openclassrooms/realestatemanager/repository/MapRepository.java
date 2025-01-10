package com.openclassrooms.realestatemanager.repository;

import com.openclassrooms.realestatemanager.api.NominatimApi;
import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;

import retrofit2.Call;

/**
 * Repository class for handling geocoding operations using the Nominatim API.
 * This repository is responsible for fetching latitude and longitude coordinates
 * for a given address.
 */
public class MapRepository {

    // Instance of the Nominatim API service
    private final NominatimApi api;

    /**
     * Constructor that initializes the Nominatim API instance.
     * This API is used to perform geocoding requests.
     */
    public MapRepository() {
        api = new NominatimApi();
    }

    /**
     * Fetches geographical coordinates (latitude and longitude) for a given address.
     * This method sends a request to the Nominatim API and retrieves an array of possible results.
     *
     * @param address The address to be geocoded.
     * @return A Retrofit Call object containing an array of NominatimResponse objects.
     */
    public Call<NominatimResponse[]> fetchCoordinatesForAddress(String address) {
        return api.getCoordinatesForAddress(address);
    }
}