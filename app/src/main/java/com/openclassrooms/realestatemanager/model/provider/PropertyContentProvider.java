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

public class PropertyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider";
    public static final String TABLE_NAME = "property";
    public static final Uri URI_PROPERTY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    private static final int CODE_PROPERTY_DIR = 1;  // URI for the entire table
    private static final int CODE_PROPERTY_ITEM = 2; // URI for a specific item

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_PROPERTY_DIR);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", CODE_PROPERTY_ITEM);
    }

    @Override
    public boolean onCreate() {
        return true; // No initialization needed for Room
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CODE_PROPERTY_DIR:
                cursor = PropertyDatabase.getInstance(getContext())
                        .propertyDao()
                        .getPropertiesCursor();
                break;

            case CODE_PROPERTY_ITEM:
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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_DIR) {
            Context context = getContext();
            if (context != null) {
                long id = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .insert(Property.fromContentValues(values));
                if (id != -1) {
                    Uri insertedUri = ContentUris.withAppendedId(uri, id);
                    context.getContentResolver().notifyChange(insertedUri, null);
                    return insertedUri;
                }
            }
        }
        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_ITEM) {
            Context context = getContext();
            if (context != null) {
                long id = ContentUris.parseId(uri);
                int count = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .deleteById(id);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            }
        }
        throw new IllegalArgumentException("Failed to delete row from " + uri);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        if (uriMatcher.match(uri) == CODE_PROPERTY_ITEM) {
            Context context = getContext();
            if (context != null) {
                long id = ContentUris.parseId(uri);
                Property property = Property.fromContentValues(values);
                property.id = (int) id;
                int count = PropertyDatabase.getInstance(context)
                        .propertyDao()
                        .update(property);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            }
        }
        throw new IllegalArgumentException("Failed to update row at " + uri);
    }

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