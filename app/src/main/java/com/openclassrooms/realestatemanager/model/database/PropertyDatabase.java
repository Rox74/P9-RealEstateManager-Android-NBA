package com.openclassrooms.realestatemanager.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.model.converters.DateConverter;
import com.openclassrooms.realestatemanager.model.converters.PhotoConverter;
import com.openclassrooms.realestatemanager.model.converters.PointOfInterestConverter;
import com.openclassrooms.realestatemanager.model.dao.PropertyDao;
import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Property.class}, version = 1)
@TypeConverters({PhotoConverter.class, PointOfInterestConverter.class, DateConverter.class})
public abstract class PropertyDatabase extends RoomDatabase {
    private static volatile PropertyDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public abstract PropertyDao propertyDao();

    public static PropertyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PropertyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    PropertyDatabase.class, "property_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}