package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class representing a Point of Interest (POI) related to a property.
 * Implements Parcelable to allow passing POI objects between components.
 */
public class PointOfInterest implements Parcelable {
    public String name; // Name of the point of interest
    public String type; // Type or category of the point of interest

    // Default constructor required by Room
    public PointOfInterest() {
    }

    // Constructor with all fields
    public PointOfInterest(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Constructor to read data from a Parcel
    protected PointOfInterest(Parcel in) {
        name = in.readString();
        type = in.readString();
    }

    // Parcelable Creator to recreate PointOfInterest objects from a Parcel
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

    @Override
    public int describeContents() {
        return 0;
    }

    // Method to write the object's data to a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
    }
}