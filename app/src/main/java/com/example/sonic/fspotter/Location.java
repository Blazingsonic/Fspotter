package com.example.sonic.fspotter;

/**
 * Created by sonic on 15.06.15.
 */
public class Location {

    private String mLocationName;
    private String mDescription;
    private String mHints;
    private double mLongitude;
    private double mLatitude;
    private String mImage;

    public String getmLocationName() {
        return mLocationName;
    }

    public void setmLocationName(String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmHints() {
        return mHints;
    }

    public void setmHints(String mHints) {
        this.mHints = mHints;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }
}
