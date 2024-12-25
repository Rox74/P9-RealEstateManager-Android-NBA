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

@Entity(tableName = "property")
public class Property implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String type;
    public double price;
    public double surface;
    public int numberOfRooms;
    public String description;

    @Embedded
    public Address address;

    @TypeConverters(PhotoConverter.class)
    public List<Photo> photos;

    @TypeConverters(PointOfInterestConverter.class)
    public List<PointOfInterest> pointsOfInterest;

    public boolean isSold;
    public Date marketDate;
    public Date soldDate;
    public String agentName;

    // Constructeur public requis par Room
    public Property() {
    }

    public Property(String type, String agentName, double price) {
        this.type = type;
        this.agentName = agentName;
        this.price = price;

        // Champs avec valeurs par défaut
        this.surface = 0.0;
        this.numberOfRooms = 0;
        this.description = "No description available";
        this.address = new Address();
        this.photos = new ArrayList<>();
        this.pointsOfInterest = new ArrayList<>();
        this.isSold = false;
        this.marketDate = new Date();
    }

    // Parcelable implementation
    protected Property(Parcel in) {
        id = in.readInt();
        type = in.readString();
        price = in.readDouble();
        surface = in.readDouble();
        numberOfRooms = in.readInt();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeDouble(price);
        dest.writeDouble(surface);
        dest.writeInt(numberOfRooms);
        dest.writeString(description);
        dest.writeParcelable(address, flags);
        dest.writeTypedList(photos);
        dest.writeTypedList(pointsOfInterest);
        dest.writeByte((byte) (isSold ? 1 : 0));
        dest.writeLong(marketDate != null ? marketDate.getTime() : -1);
        dest.writeLong(soldDate != null ? soldDate.getTime() : -1);
        dest.writeString(agentName);
    }

    public static Property fromContentValues(ContentValues values) {
        Property property = new Property();
        if (values.containsKey("id")) property.id = values.getAsInteger("id");
        if (values.containsKey("type")) property.type = values.getAsString("type");
        if (values.containsKey("price")) property.price = values.getAsDouble("price");
        if (values.containsKey("surface")) property.surface = values.getAsDouble("surface");
        if (values.containsKey("numberOfRooms")) property.numberOfRooms = values.getAsInteger("numberOfRooms");
        if (values.containsKey("description")) property.description = values.getAsString("description");
        if (values.containsKey("isSold")) property.isSold = values.getAsBoolean("isSold");
        if (values.containsKey("marketDate")) property.marketDate = new Date(values.getAsLong("marketDate"));
        if (values.containsKey("soldDate")) property.soldDate = new Date(values.getAsLong("soldDate"));
        if (values.containsKey("agentName")) property.agentName = values.getAsString("agentName");

        // Address peut être géré de manière similaire si ses valeurs sont dans ContentValues
        // Ajoutez la logique pour `photos` et `pointsOfInterest` si nécessaire.

        return property;
    }
}