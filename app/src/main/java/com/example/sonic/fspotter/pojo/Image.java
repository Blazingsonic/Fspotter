package com.example.sonic.fspotter.pojo;

/**
 * Created by sonic on 03.07.15.
 */
public class Image {

    private long id;
    private String locationName;
    private String image;

    public Image() {

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
