package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    public String street;
    public String city;
    public String state;
    public String zipCode;
    public String country;

    // Constructeur public requis par Room
    public Address() {
    }

    // Constructeur avec tous les champs
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    protected Address(Parcel in) {
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        country = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipCode);
        dest.writeString(country);
    }
}