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

/**
 * Unit tests for the MapViewModel class.
 * This class tests fetching coordinates based on an address using the MapRepository.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    private MapViewModel viewModel;

    @Mock
    private MapRepository mapRepository;

    @Mock
    private Call<NominatimResponse[]> mockCall;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    /**
     * Sets up the test environment before each test.
     * Initializes the mocked MapRepository and the ViewModel instance.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new MapViewModel(mapRepository);
    }

    /**
     * Tests fetchCoordinates() - Success case with a valid API response.
     * Ensures that valid coordinates are retrieved and stored in LiveData.
     */
    @Test
    public void fetchCoordinates_success() throws InterruptedException {
        // GIVEN - A valid address and a mocked API response
        String address = "1600 Amphitheatre Parkway, Mountain View, CA";

        // Mock a NominatimResponse with valid latitude and longitude
        NominatimResponse mockResponse = new NominatimResponse();
        mockResponse.lat = 37.422;
        mockResponse.lon = -122.084;

        NominatimResponse[] responseArray = new NominatimResponse[]{mockResponse};
        Response<NominatimResponse[]> successResponse = Response.success(responseArray);

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        // Simulate a successful API response
        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, successResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Calling fetchCoordinates()
        viewModel.fetchCoordinates(address);
        NominatimResponse result = LiveDataTestUtil.getValue(viewModel.getMapDataLiveData());

        // THEN - Verify that the correct coordinates are retrieved
        assertNotNull(result);
        assertEquals(37.422, result.lat, 0.0001);
        assertEquals(-122.084, result.lon, 0.0001);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }

    /**
     * Tests fetchCoordinates() - No data found in the response.
     * Ensures that an appropriate error message is set when no results are returned.
     */
    @Test
    public void fetchCoordinates_noDataFound() throws InterruptedException {
        // GIVEN - A response with no data
        String address = "Invalid Address";
        Response<NominatimResponse[]> emptyResponse = Response.success(new NominatimResponse[0]);

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        // Simulate an API response returning no results
        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, emptyResponse);
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Calling fetchCoordinates()
        viewModel.fetchCoordinates(address);
        String errorMessage = LiveDataTestUtil.getValue(viewModel.getErrorLiveData());

        // THEN - Verify that the correct error message is set
        assertNotNull(errorMessage);
        assertEquals("No data found for the given address", errorMessage);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }

    /**
     * Tests fetchCoordinates() - Network error scenario.
     * Ensures that an appropriate error message is set when a network failure occurs.
     */
    @Test
    public void fetchCoordinates_networkError() throws InterruptedException {
        // GIVEN - Simulate a network failure
        String address = "Some Address";
        String errorMsg = "Network failure";

        when(mapRepository.fetchCoordinatesForAddress(address)).thenReturn(mockCall);

        // Simulate an API call failure due to network issues
        doAnswer(invocation -> {
            Callback<NominatimResponse[]> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new IOException(errorMsg));
            return null;
        }).when(mockCall).enqueue(any());

        // WHEN - Calling fetchCoordinates()
        viewModel.fetchCoordinates(address);
        String errorMessage = LiveDataTestUtil.getValue(viewModel.getErrorLiveData());

        // THEN - Verify that the correct error message is set
        assertNotNull(errorMessage);
        assertEquals("Error: " + errorMsg, errorMessage);
        verify(mapRepository).fetchCoordinatesForAddress(address);
    }
}