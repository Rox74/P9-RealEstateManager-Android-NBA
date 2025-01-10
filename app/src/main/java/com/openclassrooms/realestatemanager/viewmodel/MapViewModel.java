package com.openclassrooms.realestatemanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;
import com.openclassrooms.realestatemanager.repository.MapRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel responsible for managing map-related operations.
 * This ViewModel interacts with the MapRepository to fetch geographical coordinates
 * (latitude and longitude) for a given address using the Nominatim API.
 */
public class MapViewModel extends ViewModel {

    // Repository instance for managing map data requests
    private final MapRepository repository;

    // LiveData holding the map data (coordinates)
    private final MutableLiveData<NominatimResponse> mapDataLiveData = new MutableLiveData<>();

    // LiveData for error messages when API requests fail
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    /**
     * Constructor that initializes the ViewModel with a MapRepository instance.
     *
     * @param repository The repository handling map data operations.
     */
    public MapViewModel(MapRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns LiveData containing the map data (coordinates).
     * This allows UI components to observe changes in location data.
     *
     * @return LiveData holding the NominatimResponse object.
     */
    public LiveData<NominatimResponse> getMapDataLiveData() {
        return mapDataLiveData;
    }

    /**
     * Returns LiveData containing error messages.
     * UI components can observe this to display appropriate error messages when API requests fail.
     *
     * @return LiveData holding error messages as Strings.
     */
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    /**
     * Fetches geographical coordinates for a given address using the Nominatim API.
     * The response is observed and stored in LiveData, which updates the UI automatically.
     *
     * @param address The address to be geocoded.
     */
    public void fetchCoordinates(String address) {
        repository.fetchCoordinatesForAddress(address).enqueue(new Callback<NominatimResponse[]>() {
            @Override
            public void onResponse(Call<NominatimResponse[]> call, Response<NominatimResponse[]> response) {
                if (response.isSuccessful() && response.body() != null && response.body().length > 0) {
                    mapDataLiveData.postValue(response.body()[0]); // Store the first result
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