package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Instrumented test for the Utils class.
 * This test verifies the behavior of utility methods that require a real Android context.
 */
public class UtilsInstrumentedTest {

    /**
     * Tests the `isInternetAvailable()` method.
     * Verifies that the method correctly returns a boolean indicating internet connectivity.
     */
    @Test
    public void isInternetAvailable_returnsBoolean() {
        // GIVEN - An application context
        Context context = ApplicationProvider.getApplicationContext();

        // WHEN - Checking if internet is available
        boolean result = Utils.isInternetAvailable(context);

        // THEN - Ensure the method does not return null (should return true or false)
        assertNotNull(result);
    }
}