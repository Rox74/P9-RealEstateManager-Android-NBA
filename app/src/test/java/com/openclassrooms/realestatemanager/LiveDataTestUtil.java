package com.openclassrooms.realestatemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for testing LiveData objects.
 * Provides a method to retrieve the value from a LiveData instance synchronously.
 */
public class LiveDataTestUtil {

    /**
     * Retrieves the current value of a LiveData object.
     * This method waits for the LiveData to emit a value and then returns it.
     *
     * @param liveData The LiveData instance to observe.
     * @param <T>      The type of data stored in the LiveData.
     * @return The observed value of the LiveData.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        // Observer that retrieves the LiveData value when changed
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T o) {
                data[0] = o;  // Store the emitted value
                latch.countDown();  // Signal that the value has been received
                liveData.removeObserver(this);  // Remove observer after retrieving the value
            }
        };

        // Observe the LiveData object forever (manually removed after value retrieval)
        liveData.observeForever(observer);

        // Wait for a maximum of 2 seconds for the LiveData to emit a value
        latch.await(2, TimeUnit.SECONDS);

        // Return the retrieved value (cast to the appropriate type)
        return (T) data[0];
    }
}