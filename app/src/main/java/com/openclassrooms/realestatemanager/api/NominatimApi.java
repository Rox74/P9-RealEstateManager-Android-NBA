package com.openclassrooms.realestatemanager.api;

import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NominatimApi {

    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";

    private final NominatimService service;

    public NominatimApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(NominatimService.class);
    }

    public Call<NominatimResponse[]> getCoordinatesForAddress(String address) {
        return service.getCoordinates(address, "json", 1);
    }

    private interface NominatimService {
        @GET("search")
        Call<NominatimResponse[]> getCoordinates(
                @Query("q") String address,
                @Query("format") String format,
                @Query("limit") int limit
        );
    }
}