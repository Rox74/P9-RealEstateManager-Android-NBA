package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
    public String uri;
    public String description;

    // Constructeur public requis par Room
    public Photo() {
    }

    // Constructeur avec tous les champs
    public Photo(String uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    protected Photo(Parcel in) {
        uri = in.readString();
        description = in.readString();
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(description);
    }
}