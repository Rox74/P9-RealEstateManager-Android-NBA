package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;
import com.openclassrooms.realestatemanager.repository.MapRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {

    private final MapRepository repository;
    private final MutableLiveData<NominatimResponse> mapDataLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public MapViewModel(MapRepository repository) {
        this.repository = repository;
    }

    public LiveData<NominatimResponse> getMapDataLiveData() {
        return mapDataLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void fetchCoordinates(String address) {
        repository.fetchCoordinatesForAddress(address).enqueue(new Callback<NominatimResponse[]>() {
            @Override
            public void onResponse(Call<NominatimResponse[]> call, Response<NominatimResponse[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    mapDataLiveData.postValue(response.body()[0]); // Prendre le premier résultat
                } else {
                    errorLiveData.postValue("No data found for the given address");
                }
            }

            @Override
            public void onFailure(Call<NominatimResponse[]> call, Throwable t) {
                errorLiveData.postValue("Error: " + t.getMessage());
            }
        });
    }
}