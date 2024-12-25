package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class PointOfInterest implements Parcelable {
    public String name;
    public String type;

    // Constructeur public requis par Room
    public PointOfInterest() {
    }

    protected PointOfInterest(Parcel in) {
        name = in.readString();
        type = in.readString();
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
    }
}