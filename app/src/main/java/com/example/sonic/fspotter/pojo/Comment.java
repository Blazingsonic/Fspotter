package com.example.sonic.fspotter.pojo;

/**
 * Created by sonic on 27.06.15.
 */
public class Comment {

    private long id;
    private String locationName;
    private String comment;

    public Comment() {

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
