package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class representing a Point of Interest (POI) related to a property.
 * Implements Parcelable to allow passing POI objects between components.
 */
public class PointOfInterest implements Parcelable {

    /** Name of the point of interest. */
    public String name;

    /** Type or category of the point of interest (e.g., school, park, store). */
    public String type;

    /**
     * Default constructor required by Room.
     * Used for database operations.
     */
    public PointOfInterest() {
    }

    /**
     * Constructor initializing all fields.
     *
     * @param name The name of the point of interest.
     * @param type The type or category of the point of interest.
     */
    public PointOfInterest(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Constructor to recreate a PointOfInterest object from a Parcel.
     *
     * @param in Parcel containing serialized PointOfInterest data.
     */
    protected PointOfInterest(Parcel in) {
        name = in.readString();
        type = in.readString();
    }

    /**
     * Parcelable Creator for PointOfInterest.
     * Allows passing PointOfInterest objects between Android components.
     */
    public static final Creator<PointOfInterest> CREATOR = new Creator<PointOfInterest>() {
        @Override
        public PointOfInterest createFromParcel(Parcel in) {
            return new PointOfInterest(in);
        }

        @Override
        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
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
     * Writes the PointOfInterest object data into a Parcel for storage or transmission.
     *
     * @param dest  The Parcel object to write data into.
     * @param flags Additional flags (unused in this case).
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
    }
}