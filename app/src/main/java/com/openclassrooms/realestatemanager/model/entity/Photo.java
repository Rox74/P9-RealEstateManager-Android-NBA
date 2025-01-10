package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class representing a photo associated with a property.
 * Implements Parcelable to allow passing photo objects between components.
 */
public class Photo implements Parcelable {

    /** URI of the photo (file path or online link). */
    public String uri;

    /** Description or caption for the photo. */
    public String description;

    /**
     * Default constructor required by Room.
     * Used for database operations.
     */
    public Photo() {
    }

    /**
     * Constructor initializing all fields.
     *
     * @param uri         The URI of the photo.
     * @param description The description of the photo.
     */
    public Photo(String uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    /**
     * Constructor to recreate a Photo object from a Parcel.
     *
     * @param in Parcel containing serialized Photo data.
     */
    protected Photo(Parcel in) {
        uri = in.readString();
        description = in.readString();
    }

    /**
     * Parcelable Creator for Photo.
     * Allows passing Photo objects between Android components.
     */
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    /**
     * Describes the contents of the Parcelable object.
     * Generally returns 0 as a default value.
     *
     * @return Always returns 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Photo object data into a Parcel for storage or transmission.
     *
     * @param dest  The Parcel object to write data into.
     * @param flags Additional flags (unused in this case).
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(description);
    }
}