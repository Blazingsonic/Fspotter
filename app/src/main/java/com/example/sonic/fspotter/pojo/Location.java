package com.example.sonic.fspotter.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.sonic.fspotter.logging.L;

import java.util.Date;


/**
 * Created by Windows on 09-02-2015.
 */
public class Location implements Parcelable {
    public static final Parcelable.Creator<Location> CREATOR
            = new Parcelable.Creator<Location>() {
        public Location createFromParcel(Parcel in) {
            L.m("create from parcel :Location");
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    private long id;
    private String locationName;
    private String description;
    private double latitude;
    private double longitude;
    private String hints;
    private String mapIconId;
    private long rating;

    public Location() {

    }

    public Location(Parcel input) {
        id = input.readLong();
        locationName = input.readString();
        description = input.readString();
        latitude = input.readInt();
        longitude = input.readDouble();
        hints = input.readString();
        mapIconId = input.readString();
        rating = input.readInt();

    }

    public Location(long id,
                    String locationName,
                    String description,
                    double latitude,
                    double longitude,
                    String hints,
                    String mapIconId,
                    int rating) {
            this.id = id;
            this.locationName = locationName;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.hints = hints;
            this.mapIconId = mapIconId;
            this.rating = rating;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public String getMapIconId() {
        return mapIconId;
    }

    public void setMapIconId(String mapIconId) {
        this.mapIconId = mapIconId;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "\nID: " + id +
                "\nTitle " + locationName +
                "\nDate " + description +
                "\nSynopsis " + longitude +
                "\nScore " + latitude +
                "\nhints " + hints +
                "\nmapIconId " + mapIconId +
                "\nrating " + rating +
                "\n";
    }

    @Override
    public int describeContents() {
//        L.m("describe Contents Location");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        L.m("writeToParcel Location");
        dest.writeLong(id);
        dest.writeString(locationName);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(hints);
        dest.writeString(mapIconId);
        dest.writeLong(rating);

    }
}
