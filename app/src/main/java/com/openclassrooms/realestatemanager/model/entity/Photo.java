package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model class representing a photo associated with a property.
 * Implements Parcelable to allow passing photo objects between components.
 */
public class Photo implements Parcelable {
    public String uri; // URI of the photo
    public String description; // Description of the photo

    // Default constructor required by Room
    public Photo() {
    }

    // Constructor with all fields
    public Photo(String uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    // Constructor to read data from a Parcel
    protected Photo(Parcel in) {
        uri = in.readString();
        description = in.readString();
    }

    // Parcelable Creator to recreate Photo objects from a Parcel
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

    @Override
    public int describeContents() {
        return 0;
    }

    // Method to write the object's data to a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(description);
    }
}