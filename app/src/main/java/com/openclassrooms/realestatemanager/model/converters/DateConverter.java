package com.openclassrooms.realestatemanager.model.converters;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * DateConverter is a utility class that provides methods to convert
 * Date objects to Long timestamps and vice versa.
 * This is used for storing Date values in the Room database as Long.
 */
public class DateConverter {

    /**
     * Converts a Date object to a Long timestamp.
     * This is used when saving a Date into the database.
     *
     * @param date The Date object to convert.
     * @return The corresponding timestamp in milliseconds, or null if the date is null.
     */
    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Converts a Long timestamp back to a Date object.
     * This is used when retrieving a Date from the database.
     *
     * @param timestamp The timestamp in milliseconds.
     * @return The corresponding Date object, or null if the timestamp is null.
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}