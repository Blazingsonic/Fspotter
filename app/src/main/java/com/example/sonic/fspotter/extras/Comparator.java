package com.example.sonic.fspotter.extras;

import com.example.sonic.fspotter.pojo.Comment;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.pojo.Rating;

import java.util.ArrayList;

/**
 * Created by sonic on 23.06.15.
 */
public class Comparator {
    public static ArrayList<Location> updateLocationsWithRatings (ArrayList<Location> locations, ArrayList<Rating> ratings) {

        ArrayList<Location> updatedLocations = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            Location currentLocation = locations.get(i);
            for (int j = 0; j < ratings.size(); j++) {
                Rating currentRating = ratings.get(j);
                if (currentLocation.getId() == currentRating.getId()) {
                    currentLocation.setRating(currentRating.getRating());
                }
            }
            updatedLocations.add(currentLocation);
        }

        return updatedLocations;
    }

    public static ArrayList<Location> updateLocationsWithComments (ArrayList<Location> locations, ArrayList<Comment> comments) {

        ArrayList<Location> updatedLocations = new ArrayList<>();

        for (int i = 0; i < locations.size(); i++) {
            Location currentLocation = locations.get(i);
            for (int j = 0; j < comments.size(); j++) {
                Comment currentComment = comments.get(j);
                if (currentLocation.getId() == currentComment.getId()) {
                    currentLocation.setComments(currentComment.getComment());
                }
            }
            updatedLocations.add(currentLocation);
        }

        return updatedLocations;
    }
}
