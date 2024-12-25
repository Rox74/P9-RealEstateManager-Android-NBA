package com.openclassrooms.realestatemanager.model.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.model.entity.PointOfInterest;

import java.lang.reflect.Type;
import java.util.List;

public class PointOfInterestConverter {
    @TypeConverter
    public static String fromPoiList(List<PointOfInterest> points) {
        return new Gson().toJson(points);
    }

    @TypeConverter
    public static List<PointOfInterest> toPoiList(String data) {
        Type listType = new TypeToken<List<PointOfInterest>>() {}.getType();
        return new Gson().fromJson(data, listType);
    }
}