package com.openclassrooms.realestatemanager.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.model.entity.Photo;

import java.lang.reflect.Type;
import java.util.List;

public class PhotoConverter {
    @TypeConverter
    public static String fromPhotoList(List<Photo> photos) {
        return new Gson().toJson(photos);
    }

    @TypeConverter
    public static List<Photo> toPhotoList(String data) {
        Type listType = new TypeToken<List<Photo>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}