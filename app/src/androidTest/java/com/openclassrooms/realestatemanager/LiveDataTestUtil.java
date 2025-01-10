package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for testing LiveData in unit tests.
 * <p>
 * This class provides a helper method to retrieve the current value from a LiveData object.
 * Since LiveData is designed to be lifecycle-aware, this method allows observing the data
 * in a synchronous manner during tests.
 */
public class LiveDataTestUtil {

    /**
     * Retrieves the current value of a LiveData object.
     * <p>
     * This method observes the LiveData indefinitely until it receives a value,
     * at which point it removes the observer and returns the retrieved data.
     * A timeout of 2 seconds is set to prevent infinite waiting.
     *
     * @param liveData The LiveData instance to observe.
     * @param <T>      The type of data held by LiveData.
     * @return The current value stored in the LiveData.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1]; // Array to hold the observed value
        CountDownLatch latch = new CountDownLatch(1); // Synchronization latch

        // Observer to retrieve the value from LiveData
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T o) {
                data[0] = o; // Store the observed value
                latch.countDown(); // Release the latch
                liveData.removeObserver(this); // Remove observer after receiving data
            }
        };

        // Observe LiveData permanently for this test
        liveData.observeForever(observer);

        // Wait up to 2 seconds for data to be set
        latch.await(2, TimeUnit.SECONDS);

        // Return the observed value
        return (T) data[0];
    }
}