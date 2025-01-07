package com.openclassrooms.realestatemanager.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents an address with street, city, state, zip code, and country information.
 * Implements Parcelable to allow passing Address objects between components.
 */
public class Address implements Parcelable {

    // Address fields
    public String street;
    public String city;
    public String state;
    public String zipCode;
    public String country;

    /**
     * Default constructor required by Room (for database operations).
     */
    public Address() {
    }

    /**
     * Constructor initializing all fields.
     *
     * @param street  The street address.
     * @param city    The city name.
     * @param state   The state or province.
     * @param zipCode The postal or zip code.
     * @param country The country name.
     */
    public Address(String street, String city, String state, String zipCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
    }

    /**
     * Constructor to recreate an Address object from a Parcel.
     *
     * @param in Parcel containing serialized Address data.
     */
    protected Address(Parcel in) {
        street = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        country = in.readString();
    }

    /**
     * Parcelable creator for Address.
     * Allows passing Address objects between Android components (e.g., Activities, Fragments).
     */
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

    /**
     * Describes the contents of the Parcelable object (always 0 for most cases).
     *
     * @return Always returns 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the Address object data into a Parcel for storage or transmission.
     *
     * @param dest  The Parcel object to write data into.
     * @param flags Additional flags (unused in this case).
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipCode);
        dest.writeString(country);
    }
}