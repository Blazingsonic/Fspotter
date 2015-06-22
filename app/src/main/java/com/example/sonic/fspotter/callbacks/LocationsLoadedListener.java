package com.example.sonic.fspotter.callbacks;

import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;


/**
 * Created by Windows on 02-03-2015.
 */
public interface LocationsLoadedListener {
    public void onLocationsLoaded(ArrayList<Location> listLocations);
}
