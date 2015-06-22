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
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }
    public void sortLocationsByDate(ArrayList<Location> movies){

        Collections.sort(movies, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                Date lhsDate=lhs.getReleaseDateTheater();
                Date rhsDate=rhs.getReleaseDateTheater();
                if(lhs.getReleaseDateTheater()!=null && rhs.getReleaseDateTheater()!=null)
                {
                    return rhs.getReleaseDateTheater().compareTo(lhs.getReleaseDateTheater());
                }
                else {
                    return 0;
                }

            }
        });
    }
    public void sortLocationsByRating(ArrayList<Location> movies){
        Collections.sort(movies, new Comparator<Location>() {
            @Override
            public int compare(Location lhs, Location rhs) {
                int ratingLhs=lhs.getAudienceScore();
                int ratingRhs=rhs.getAudienceScore();
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
