package com.openclassrooms.realestatemanager.repository;

import com.openclassrooms.realestatemanager.api.NominatimApi;
import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;

import retrofit2.Call;

public class MapRepository {

    private final NominatimApi api;

    public MapRepository() {
        api = new NominatimApi();
    }

    public Call<NominatimResponse[]> fetchCoordinatesForAddress(String address) {
        return api.getCoordinatesForAddress(address);
    }
}