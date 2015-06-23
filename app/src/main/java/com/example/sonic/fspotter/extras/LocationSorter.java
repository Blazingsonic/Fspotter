package com.example.sonic.fspotter.extras;

import com.example.sonic.fspotter.pojo.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by Windows on 18-02-2015.
 */
public class LocationSorter {
    public void sortLocationsByName(ArrayList<Location> movies){
        Collections.sort(movies, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                return lhs.getLocationName().compareTo(rhs.getLocationName());
            }
        });
    }
    public void sortLocationsByDate(ArrayList<Location> movies){

        Collections.sort(movies, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                /*Date lhsDate=lhs.getDescription();
                Date rhsDate=rhs.getDescription();
                if(lhs.getDescription()!=null && rhs.getDescription()!=null)
                {
                    return rhs.getDescription().compareTo(lhs.getDescription());
                }
                else {
                    return 0;
                }*/
                return 0;
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
