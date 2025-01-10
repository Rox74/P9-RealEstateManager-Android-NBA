package com.openclassrooms.realestatemanager.model.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.model.database.PropertyDatabase;
import com.openclassrooms.realestatemanager.model.entity.Property;

/**
 * ContentProvider for accessing the Property database via a content URI.
 * This allows external applications to query, insert, update, and delete property records.
 */
public class PropertyContentProvider extends ContentProvider {

    // Authority of the content provider (must match the AndroidManifest declaration)
    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";

    // Table name associated with this content provider
    public static final String TABLE_NAME = "property";

    // Base content URI for accessing properties
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    // Constants for URI matching
    private static final int CODE_PROPERTY_DIR = 1;  // URI for the entire table (all properties)
    private static final int CODE_PROPERTY_ITEM = 2; // URI for a specific property (by ID)

    // UriMatcher to differentiate between different query types
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Match URI for retrieving all properties
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_PROPERTY_DIR);

        // Match URI for retrieving a single property by ID
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", CODE_PROPERTY_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true; // No special initialization needed for Room
    }

    /**
     * Queries the database for properties based on the provided URI.
     *
     * @param uri           The URI to query.
     * @param projection    The columns to return.
     * @param selection     The selection criteria.
     * @param selectionArgs The selection arguments.
     * @param sortOrder     The sort order for the results.
     * @return A Cursor object containing the query results.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_PROPERTY_DIR:
                // Retrieve all properties from the database
                cursor = PropertyDatabase.getInstance(getContext())
                        .propertyDao()
                        .getPropertiesCursor();
                break;

            case CODE_PROPERTY_ITEM:
                // Retrieve a single property by its ID
                long id = ContentUris.parseId(uri);
                cursor = PropertyDatabase.getInstance(getContext())
                        .propertyDao()
                        .getPropertyByIdCursor(id);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // Notify listeners when data changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Inserts a new property into the database.
     *
     * @param uri    The URI where the property should be inserted.
     * @param values The property data to insert.
     * @return The URI of the newly inserted property.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_DIR) {
            Context context = getContext();
            if (context != null) {
                // Insert a new property and get its generated ID
                long id = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .insert(Property.fromContentValues(values));

                if (id != -1) {
                    Uri insertedUri = ContentUris.withAppendedId(uri, id);
                    // Notify observers that the database has been updated
                    context.getContentResolver().notifyChange(insertedUri, null);
                    return insertedUri;
                }
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    /**
     * Deletes a property from the database using its ID.
     *
     * @param uri           The URI identifying the property to delete.
     * @param selection     Unused (since deletion is based on ID).
     * @param selectionArgs Unused (since deletion is based on ID).
     * @return The number of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_ITEM) {
            Context context = getContext();
            if (context != null) {
                long id = ContentUris.parseId(uri);
                int count = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .deleteById(id);
                // Notify observers that the database has been updated
                context.getContentResolver().notifyChange(uri, null);
                return count;
            }
        }
        throw new IllegalArgumentException("Failed to delete row from " + uri);
    }

    /**
     * Updates an existing property in the database.
     *
     * @param uri           The URI identifying the property to update.
     * @param values        The new values to update the property with.
     * @param selection     Unused (since update is based on ID).
     * @param selectionArgs Unused (since update is based on ID).
     * @return The number of rows affected.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_ITEM) {
            Context context = getContext();
            if (context != null) {
                long id = ContentUris.parseId(uri);
                Property property = Property.fromContentValues(values);
                property.id = (int) id; // Ensure the ID is set correctly
                int count = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .update(property);
                // Notify observers that the database has been updated
                context.getContentResolver().notifyChange(uri, null);
                return count;
            }
        }
        throw new IllegalArgumentException("Failed to update row at " + uri);
    }

    /**
     * Returns the MIME type of data provided by this ContentProvider.
     *
     * @param uri The URI to check.
     * @return The MIME type as a string.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CODE_PROPERTY_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;
            case CODE_PROPERTY_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}