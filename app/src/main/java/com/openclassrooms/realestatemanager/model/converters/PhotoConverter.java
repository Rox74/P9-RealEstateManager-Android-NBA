package com.openclassrooms.realestatemanager.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.model.entity.Photo;

import java.lang.reflect.Type;
import java.util.List;

/**
 * PhotoConverter is a Room TypeConverter that allows the database to store
 * a List of Photo objects as a single JSON string and retrieve it back as a List.
 * This helps Room handle complex data types like lists.
 */
public class PhotoConverter {

    /**
     * Converts a List of Photo objects into a JSON string.
     * This is used when saving a list of photos into the database.
     *
     * @param photos The list of Photo objects to be converted.
     * @return A JSON representation of the list.
     */
    @TypeConverter
    public static String fromPhotoList(List<Photo> photos) {
        return new Gson().toJson(photos);
    }

    /**
     * Converts a JSON string back into a List of Photo objects.
     * This is used when retrieving a list of photos from the database.
     *
     * @param data The JSON string representation of the photo list.
     * @return A List of Photo objects.
     */
    @TypeConverter
    public static List<Photo> toPhotoList(String data) {
        Type listType = new TypeToken<List<Photo>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}