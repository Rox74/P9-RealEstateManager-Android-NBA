package com.openclassrooms.realestatemanager.model.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.model.entity.Property;

import java.util.List;

@Dao
public interface PropertyDao {
    @Insert
    long insert(Property property);

    @Update
    int update(Property property);

    @Query("SELECT * FROM property WHERE id = :id")
    LiveData<Property> getPropertyById(int id);

    @Query("SELECT * FROM property")
    LiveData<List<Property>> getAllProperties();

    @Query("SELECT * FROM property")
    Cursor getPropertiesCursor();

    @Query("SELECT * FROM property WHERE id = :id")
    Cursor getPropertyByIdCursor(long id);

    @Query("DELETE FROM property WHERE id = :id")
    int deleteById(long id);
}