package com.example.sonic.fspotter.pojo;

/**
 * Created by sonic on 23.06.15.
 */
public class Rating {

    private long id;
    private String locationName;
    private long rating;

    public Rating() {

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

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
