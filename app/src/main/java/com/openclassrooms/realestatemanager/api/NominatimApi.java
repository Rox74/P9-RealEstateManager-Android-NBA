package com.openclassrooms.realestatemanager.api;

import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NominatimApi is a utility class that provides geocoding services using the
 * OpenStreetMap Nominatim API. It allows converting an address into geographical
 * coordinates (latitude and longitude).
 */
public class NominatimApi {

    // Base URL for the Nominatim API (OpenStreetMap's geocoding service)
    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";

    // Retrofit service interface for handling API requests
    private final NominatimService service;

    /**
     * Constructor that initializes a Retrofit instance and creates the service interface.
     */
    public NominatimApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Set the base URL for API requests
                .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
                .build();

        // Create an instance of the API service
        service = retrofit.create(NominatimService.class);
    }

    /**
     * Retrieves the geographical coordinates (latitude and longitude) for a given address.
     * The API will return an array of possible results, but we limit it to 1.
     *
     * @param address The address to be geocoded.
     * @return A Retrofit Call object to execute the API request asynchronously.
     */
    public Call<NominatimResponse[]> getCoordinatesForAddress(String address) {
        return service.getCoordinates(address, "json", 1);
    }

    /**
     * Retrofit interface that defines the Nominatim API endpoints.
     */
    private interface NominatimService {
        /**
         * Sends a GET request to the Nominatim API to retrieve geographical coordinates
         * for a given address.
         *
         * @param address The address to search for.
         * @param format The response format (JSON).
         * @param limit The number of results to return (1 to get only the most relevant match).
         * @return A Call object containing an array of NominatimResponse objects.
         */
        @GET("search")
        Call<NominatimResponse[]> getCoordinates(
                @Query("q") String address,
                @Query("format") String format,
                @Query("limit") int limit
        );
    }
}