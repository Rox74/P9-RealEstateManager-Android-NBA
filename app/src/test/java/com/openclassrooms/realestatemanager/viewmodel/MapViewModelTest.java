package com.openclassrooms.realestatemanager.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.realestatemanager.LiveDataTestUtil;
import com.openclassrooms.realestatemanager.model.entity.NominatimResponse;
import com.openclassrooms.realestatemanager.repository.MapRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    private MapViewModel viewModel;

    @Mock
    private MapRepository mapRepository;

    @Mock
    private Call<NominatimResponse[]> mockCall;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new MapViewModel(mapRepository);
    }

    /**
     * Test fetchCoordinates() - Succès avec une réponse valide.
     */
    @Test
    public void fetchCoordinates_success() throws InterruptedException {
        // GIVEN - Une adresse valide et un mock de réponse API
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";

        // Création d'un mock de NominatimResponse avec des coordonnées correctes
        NominatimResponse mockResponse = new NominatimResponse();
        mockResponse.lat = 37.422;
        mockResponse.lon = -122.084;

        NominatimResponse[] responseArray = new NominatimResponse[]{mockResponse};
        Response<NominatimResponse[]> successResponse = Response.success(responseArray);

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, successResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Appel à fetchCoordinates()
        viewModel.fetchCoordinates(address);
        NominatimResponse result = LiveDataTestUtil.getValue(viewModel.getMapDataLiveData());

        // THEN - Vérification que les bonnes coordonnées sont retournées
        assertNotNull(result);
        assertEquals(37.422, result.lat, 0.0001);
        assertEquals(-122.084, result.lon, 0.0001);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }

    /**
     * Test fetchCoordinates() - Réponse vide.
     */
    @Test
    public void fetchCoordinates_noDataFound() throws InterruptedException {
        // GIVEN - Une réponse vide
        String address = "Invalid Address";
        Response<NominatimResponse[]> emptyResponse = Response.success(new NominatimResponse[0]);

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, emptyResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Appel à fetchCoordinates()
        viewModel.fetchCoordinates(address);
        String errorMessage = LiveDataTestUtil.getValue(viewModel.getErrorLiveData());

        // THEN - Vérifie que l'erreur est bien déclenchée
        assertNotNull(errorMessage);
        assertEquals("No data found for the given address", errorMessage);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }

    /**
     * Test fetchCoordinates() - Erreur réseau.
     */
    @Test
    public void fetchCoordinates_networkError() throws InterruptedException {
        // GIVEN - Simuler une erreur réseau
        String address = "Some Address";
        String errorMsg = "Network failure";

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new IOException(errorMsg));
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Appel à fetchCoordinates()
        viewModel.fetchCoordinates(address);
        String errorMessage = LiveDataTestUtil.getValue(viewModel.getErrorLiveData());

        // THEN - Vérification que l'erreur est bien retournée
        assertNotNull(errorMessage);
        assertEquals("Error: " + errorMsg, errorMessage);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }
}