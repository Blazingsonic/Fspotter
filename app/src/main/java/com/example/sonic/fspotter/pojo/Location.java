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
    private String image;
    private String comments;
    private Date createdAt;

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
        image = input.readString();
        comments = input.readString();
        long dateMillis = input.readLong();
        createdAt = (dateMillis == -1 ? null : new Date(dateMillis));

    }

    public Location(long id,
                    String locationName,
                    String description,
                    double latitude,
                    double longitude,
                    String hints,
                    String mapIconId,
                    int rating,
                    String image,
                    String comments,
                    Date createdAt) {
            this.id = id;
            this.locationName = locationName;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
            this.hints = hints;
            this.mapIconId = mapIconId;
            this.rating = rating;
            this.image = image;
            this.comments = comments;
            this.createdAt = createdAt;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
                "\nimage " + image +
                "\ncomments " + comments +
                "\ncreatedAt " + createdAt +
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
        dest.writeString(image);
        dest.writeString(comments);
        dest.writeLong(createdAt == null ? -1 : createdAt.getTime());

    }
}
