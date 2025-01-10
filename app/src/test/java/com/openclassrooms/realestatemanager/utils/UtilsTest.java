package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Unit test for the Utils class.
 */
public class UtilsTest {

    @Test
    public void convertDollarToEuro_correctConversion() {
        // Given
        double dollars = 100.0;

        // When
        double euros = Utils.convertDollarToEuro(dollars);

        // Then
        assertEquals(96.0, euros, 0.01); // Vérifie avec une marge d'erreur
    }

    @Test
    public void convertEuroToDollar_correctConversion() {
        // Given
        double euros = 100.0;

        // When
        double dollars = Utils.convertEuroToDollar(euros);

        // Then
        assertEquals(104.0, dollars, 0.01); // Vérifie avec une marge d'erreur
    }

    @Test
    public void getTodayDate_returnsCorrectFormat() {
        // Given
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String expectedDate = expectedFormat.format(new Date());

        // When
        String actualDate = Utils.getTodayDate();

        // Then
        assertEquals(expectedDate, actualDate);
    }
}
