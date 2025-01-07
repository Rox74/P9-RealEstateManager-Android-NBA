package com.openclassrooms.realestatemanager.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;

import java.lang.reflect.Type;
import java.util.List;

/**
 * PointOfInterestConverter is a Room TypeConverter that enables Room Database
 * to store a List of PointOfInterest objects as a single JSON string and retrieve
 * it back as a List. This allows handling complex data types in Room.
 */
public class PointOfInterestConverter {

    /**
     * Converts a List of PointOfInterest objects into a JSON string.
     * This method is used when storing a list of points of interest in the database.
     *
     * @param points The list of PointOfInterest objects to be converted.
     * @return A JSON representation of the list.
     */
    @TypeConverter
    public static String fromPoiList(List<PointOfInterest> points) {
        return new Gson().toJson(points);
    }

    /**
     * Converts a JSON string back into a List of PointOfInterest objects.
     * This method is used when retrieving a list of points of interest from the database.
     *
     * @param data The JSON string representation of the point of interest list.
     * @return A List of PointOfInterest objects.
     */
    @TypeConverter
    public static List<PointOfInterest> toPoiList(String data) {
        Type listType = new TypeToken<List<PointOfInterest>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}