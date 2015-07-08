package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by Windows on 18-02-2015.
 */
public class LocationSorter {
    public void sortLocationsByName(ArrayList<Location> locations){
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return lhs.getLocationName().toLowerCase().compareTo(rhs.getLocationName().toLowerCase());
            }
        });
    }
    public void sortLocationsByDate(ArrayList<Location> locations){

        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                Date lhsDate=lhs.getCreatedAt();
                Date rhsDate=rhs.getCreatedAt();
                Log.v("CREATED AT", rhsDate.toString());
                Log.v("CREATED AT", lhsDate.toString());
                if(lhs.getCreatedAt()!=null && rhs.getCreatedAt()!=null)
                {
                    return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
                }
                else {
                    return 0;
                }
            }
        });
    }
    public void sortLocationsByRating(ArrayList<Location> locations){
        Collections.sort(locations, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                double ratingLhs=lhs.getRating();
                double ratingRhs=rhs.getRating();
                if(ratingLhs<ratingRhs)
                {
                    return 1;
                }
                else if(ratingLhs>ratingRhs)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }
}
