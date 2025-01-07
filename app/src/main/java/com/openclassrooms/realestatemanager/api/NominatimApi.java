package com.openclassrooms.realestatemanager.api;

import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NominatimApi {

    // Base URL for the Nominatim API (OpenStreetMap geocoding service)
    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";

    // Retrofit service interface for making HTTP requests
    private final NominatimService service;

    // Constructor to initialize the Retrofit instance and create the service
    public NominatimApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // Set the base URL
                .addConverterFactory(GsonConverterFactory.create()) // Convert JSON responses using Gson
                .build();

        // Create an instance of the service interface
        service = retrofit.create(NominatimService.class);
    }

    /**
     * Retrieves the geographical coordinates (latitude and longitude) for a given address.
     *
     * @param address The address to be geocoded.
     * @return A Retrofit Call object to execute the API request.
     */
    public Call<NominatimResponse[]> getCoordinatesForAddress(String address) {
        return service.getCoordinates(address, "json", 1);
    }

    // Retrofit service interface defining the API endpoints
    private interface NominatimService {
        /**
         * Makes a GET request to the Nominatim API to retrieve coordinates for an address.
         *
         * @param address The query address.
         * @param format The response format (JSON).
         * @param limit The number of results to return (1 to get only the best match).
         * @return A Call object containing an array of NominatimResponse.
         */
        @GET("search")
        Call<NominatimResponse[]> getCoordinates(
                @Query("q") String address,
                @Query("format") String format,
                @Query("limit") int limit
        );
    }
}