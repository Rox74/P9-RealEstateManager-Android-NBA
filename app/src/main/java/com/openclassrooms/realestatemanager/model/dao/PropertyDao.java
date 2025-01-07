package com.openclassrooms.realestatemanager.model.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.List;

/**
 * Data Access Object (DAO) for managing database operations on the Property entity.
 * This interface provides methods for inserting, updating, retrieving, and deleting
 * property records in the database.
 */
@Dao
public interface PropertyDao {

    /**
     * Inserts a new property into the database.
     *
     * @param property The property object to insert.
     * @return The row ID of the newly inserted property.
     */
    @Insert
    long insert(Property property);

    /**
     * Updates an existing property in the database.
     *
     * @param property The property object with updated values.
     * @return The number of rows affected by the update.
     */
    @Update
    int update(Property property);

    /**
     * Retrieves a property by its ID as a LiveData object.
     * LiveData ensures automatic updates when the data changes in the database.
     *
     * @param propertyId The unique ID of the property to retrieve.
     * @return A LiveData object containing the requested property.
     */
    @Query("SELECT * FROM property WHERE id = :propertyId")
    LiveData<Property> getPropertyById(int propertyId);

    /**
     * Retrieves all properties from the database as a LiveData object.
     * LiveData ensures that any UI component observing this data gets updates
     * whenever there are changes in the database.
     *
     * @return A LiveData object containing a list of all properties.
     */
    @Query("SELECT * FROM property")
    LiveData<List<Property>> getAllProperties();

    /**
     * Retrieves all properties as a Cursor object.
     * This is typically used for content providers or exporting data.
     *
     * @return A Cursor object containing all properties.
     */
    @Query("SELECT * FROM property")
    Cursor getPropertiesCursor();

    /**
     * Retrieves a property by its ID as a Cursor object.
     * This is typically used for content providers or exporting data.
     *
     * @param id The unique ID of the property to retrieve.
     * @return A Cursor object containing the requested property.
     */
    @Query("SELECT * FROM property WHERE id = :id")
    Cursor getPropertyByIdCursor(long id);

    /**
     * Deletes a property from the database by its ID.
     *
     * @param id The unique ID of the property to delete.
     * @return The number of rows affected by the deletion.
     */
    @Query("DELETE FROM property WHERE id = :id")
    int deleteById(long id);

    @Query("SELECT * FROM property WHERE " +
            "(:minPrice = 0 OR price >= :minPrice) AND " +
            "(:maxPrice = 0 OR price <= :maxPrice) AND " +
            "(:minSurface = 0 OR surface >= :minSurface) AND " +
            "(:maxSurface = 0 OR surface <= :maxSurface) AND " +
            "(:minRooms = 0 OR numberOfRooms >= :minRooms) AND " +
            "(:location IS NULL OR city LIKE '%' || :location || '%' OR state LIKE '%' || :location || '%')")
    LiveData<List<Property>> searchProperties(
            double minPrice, double maxPrice, double minSurface, double maxSurface,
            int minRooms, String location);
}