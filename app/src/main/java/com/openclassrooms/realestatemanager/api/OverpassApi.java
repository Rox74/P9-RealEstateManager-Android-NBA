package com.openclassrooms.realestatemanager.api;

import android.util.Log;

import com.openclassrooms.realestatemanager.model.entity.OverpassResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class OverpassApi {

    private static final String BASE_URL = "https://overpass-api.de/api/";

    private final OverpassService service;

    public OverpassApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(OverpassService.class);
    }

    public Call<OverpassResponse> getCoordinatesForAddress(String street, String city, String state) {
        String query = "[out:json];" +
                "(" +
                "node[\"addr:street\"=\"" + street + "\"][\"addr:city\"=\"" + city + "\"][\"addr:state\"=\"" + state + "\"];" +
                "way[\"addr:street\"=\"" + street + "\"][\"addr:city\"=\"" + city + "\"][\"addr:state\"=\"" + state + "\"];" +
                "relation[\"addr:street\"=\"" + street + "\"][\"addr:city\"=\"" + city + "\"][\"addr:state\"=\"" + state + "\"];" +
                ");" +
                "out center 1;";

        Log.d("OverpassApi", "Generated query: " + query);

        return service.getCoordinates(query);
    }

    private interface OverpassService {
        @GET("interpreter")
        Call<OverpassResponse> getCoordinates(@Query("data") String query);
    }
}