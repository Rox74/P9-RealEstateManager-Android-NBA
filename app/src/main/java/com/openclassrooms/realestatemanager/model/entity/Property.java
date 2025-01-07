package com.openclassrooms.realestatemanager.model.entity;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.openclassrooms.realestatemanager.model.converters.PhotoConverter;
import com.openclassrooms.realestatemanager.model.converters.PointOfInterestConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity class representing a real estate property.
 * Implements Parcelable to allow passing property objects between components.
 */
@Entity(tableName = "property")
public class Property implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id; // Unique identifier for the property

    public String type; // Property type (e.g., house, apartment)
    public double price; // Property price
    public double surface; // Surface area in square meters
    public int numberOfRooms; // Number of rooms
    public int numberOfBathrooms; // Number of bathrooms
    public int numberOfBedrooms; // Number of bedrooms
    public String description; // Property description

    @Embedded
    public Address address; // Address object storing location details

    @TypeConverters(PhotoConverter.class)
    public List<Photo> photos; // List of property photos

    @TypeConverters(PointOfInterestConverter.class)
    public List<PointOfInterest> pointsOfInterest; // List of nearby points of interest

    public boolean isSold; // Flag indicating if the property has been sold
    public Date marketDate; // Date when the property was listed on the market
    public Date soldDate; // Date when the property was sold (if applicable)
    public String agentName; // Name of the real estate agent responsible for the property

    // Default constructor required by Room
    public Property() {
    }

    /**
     * Constructor with all fields.
     */
    public Property(String type, double price, double surface, int numberOfRooms, int numberOfBathrooms, int numberOfBedrooms,
                    String description, Address address, List<Photo> photos, List<PointOfInterest> pointsOfInterest,
                    boolean isSold, Date marketDate, Date soldDate, String agentName) {
        this.type = type;
        this.price = price;
        this.surface = surface;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.numberOfBedrooms = numberOfBedrooms;
        this.description = description;
        this.address = address;
        this.photos = photos;
        this.pointsOfInterest = pointsOfInterest;
        this.isSold = isSold;
        this.marketDate = marketDate;
        this.soldDate = soldDate;
        this.agentName = agentName;
    }

    /**
     * Simplified constructor for quick property creation with essential details.
     */
    public Property(String type, String agentName, double price) {
        this.type = type;
        this.agentName = agentName;
        this.price = price;

        // Default values for other fields
        this.surface = 0.0;
        this.numberOfRooms = 0;
        this.numberOfBathrooms = 0;
        this.numberOfBedrooms = 0;
        this.description = "No description available";
        this.address = new Address();
        this.photos = new ArrayList<>();
        this.pointsOfInterest = new ArrayList<>();
        this.isSold = false;
        this.marketDate = new Date();
    }

    // Parcelable implementation

    /**
     * Constructor used for Parcel.
     */
    protected Property(Parcel in) {
        id = in.readInt();
        type = in.readString();
        price = in.readDouble();
        surface = in.readDouble();
        numberOfRooms = in.readInt();
        numberOfBathrooms = in.readInt();
        numberOfBedrooms = in.readInt();
        description = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        photos = in.createTypedArrayList(Photo.CREATOR);
        pointsOfInterest = in.createTypedArrayList(PointOfInterest.CREATOR);
        isSold = in.readByte() != 0;
        marketDate = new Date(in.readLong());
        soldDate = new Date(in.readLong());
        agentName = in.readString();
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the object's data to a Parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeDouble(price);
        dest.writeDouble(surface);
        dest.writeInt(numberOfRooms);
        dest.writeInt(numberOfBathrooms);
        dest.writeInt(numberOfBedrooms);
        dest.writeString(description);
        dest.writeParcelable(address, flags);
        dest.writeTypedList(photos);
        dest.writeTypedList(pointsOfInterest);
        dest.writeByte((byte) (isSold ? 1 : 0));
        dest.writeLong(marketDate != null ? marketDate.getTime() : -1);
        dest.writeLong(soldDate != null ? soldDate.getTime() : -1);
        dest.writeString(agentName);
    }

    /**
     * Converts ContentValues to a Property object, useful for ContentProviders.
     */
    public static Property fromContentValues(ContentValues values) {
        Property property = new Property();
        if (values.containsKey("id")) property.id = values.getAsInteger("id");
        if (values.containsKey("type")) property.type = values.getAsString("type");
        if (values.containsKey("price")) property.price = values.getAsDouble("price");
        if (values.containsKey("surface")) property.surface = values.getAsDouble("surface");
        if (values.containsKey("numberOfRooms")) property.numberOfRooms = values.getAsInteger("numberOfRooms");
        if (values.containsKey("numberOfBathrooms")) property.numberOfBathrooms = values.getAsInteger("numberOfBathrooms");
        if (values.containsKey("numberOfBedrooms")) property.numberOfBedrooms = values.getAsInteger("numberOfBedrooms");
        if (values.containsKey("description")) property.description = values.getAsString("description");
        if (values.containsKey("isSold")) property.isSold = values.getAsBoolean("isSold");
        if (values.containsKey("marketDate")) property.marketDate = new Date(values.getAsLong("marketDate"));
        if (values.containsKey("soldDate")) property.soldDate = new Date(values.getAsLong("soldDate"));
        if (values.containsKey("agentName")) property.agentName = values.getAsString("agentName");

        return property;
    }
}