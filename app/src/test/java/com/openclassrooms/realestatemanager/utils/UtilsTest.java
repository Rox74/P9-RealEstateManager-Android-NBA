package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Unit tests for the Utils class.
 * These tests validate the correct behavior of utility methods for currency conversion
 * and date formatting.
 */
public class UtilsTest {

    /**
     * Tests the conversion of US Dollars to Euros.
     * Ensures that the conversion rate is correctly applied.
     */
    @Test
    public void convertDollarToEuro_correctConversion() {
        // Given - A value in US Dollars
        double dollars = 100.0;

        // When - Converting to Euros
        double euros = Utils.convertDollarToEuro(dollars);

        // Then - The result should match the expected conversion with a small tolerance
        assertEquals(96.0, euros, 0.01);
    }

    /**
     * Tests the conversion of Euros to US Dollars.
     * Ensures that the conversion rate is correctly applied.
     */
    @Test
    public void convertEuroToDollar_correctConversion() {
        // Given - A value in Euros
        double euros = 100.0;

        // When - Converting to US Dollars
        double dollars = Utils.convertEuroToDollar(euros);

        // Then - The result should match the expected conversion with a small tolerance
        assertEquals(104.0, dollars, 0.01);
    }

    /**
     * Tests the formatting of today's date.
     * Ensures that the returned date follows the "dd/MM/yyyy" format.
     */
    @Test
    public void getTodayDate_returnsCorrectFormat() {
        // Given - Expected date format
        SimpleDateFormat expectedFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String expectedDate = expectedFormat.format(new Date());

        // When - Retrieving today's formatted date from the utility method
        String actualDate = Utils.getTodayDate();

        // Then - The result should match the expected date format
        assertEquals(expectedDate, actualDate);
    }
}