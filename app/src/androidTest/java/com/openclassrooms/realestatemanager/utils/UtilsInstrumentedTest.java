package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsInstrumentedTest {

    @Test
    public void isInternetAvailable_returnsBoolean() {
        // Given
        Context context = ApplicationProvider.getApplicationContext();

        // When
        boolean result = Utils.isInternetAvailable(context);

        // Then
        assertNotNull(result); // Vérifie que la méthode ne retourne pas null
    }
}