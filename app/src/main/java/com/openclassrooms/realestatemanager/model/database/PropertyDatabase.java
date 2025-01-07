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

/**
 * Singleton Room Database class for managing property-related data.
 * This database includes Property entities and utilizes TypeConverters
 * for storing complex data types (photos, points of interest, and dates).
 */
@Database(entities = {Property.class}, version = 1) // Define the database schema
@TypeConverters({PhotoConverter.class, PointOfInterestConverter.class, DateConverter.class}) // Convert complex data types
public abstract class PropertyDatabase extends RoomDatabase {

    // Singleton instance of the database to prevent multiple instances
    private static volatile PropertyDatabase INSTANCE;

    // Executor service with a fixed thread pool to perform database operations asynchronously
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    /**
     * Provides access to the DAO (Data Access Object) for performing database operations.
     *
     * @return The PropertyDao implementation.
     */
    public abstract PropertyDao propertyDao();

    /**
     * Returns the singleton instance of the database.
     * Ensures thread safety with double-checked locking.
     *
     * @param context The application context.
     * @return The singleton instance of PropertyDatabase.
     */
    public static PropertyDatabase getInstance(Context context) {
        if (INSTANCE == null) { // First check without synchronization for better performance
            synchronized (PropertyDatabase.class) { // Synchronize block to ensure thread safety
                if (INSTANCE == null) { // Second check within the synchronized block
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), // Use application context to avoid memory leaks
                            PropertyDatabase.class, // Database class reference
                            "property_database" // Database name
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}