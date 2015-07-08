package com.example.sonic.fspotter.extras;

import android.util.Log;

import com.example.sonic.fspotter.pojo.Rating;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonic on 23.06.15.
 */
public class RatingAverager {
    public static ArrayList averageRatings (ArrayList<Rating> ratings) {
        ArrayList<Rating> averagedRatings = new ArrayList<>();

        for (int i = 0; i < ratings.size(); i++) {
            Rating currentRating = ratings.get(i);
            List<Long> ratingList = new ArrayList<>();
            for (int j = 0; j < ratings.size(); j++) {
                Rating otherRating = ratings.get(j);

                if (currentRating.getId() == otherRating.getId()) {
                    ratingList.add(currentRating.getRating());
                }
            }
            // make average
            long sum = 0;
            if (ratingList != null) {
                for (int s = 0; s < ratingList.size(); s++) {
                    sum += ratingList.get(s);
                }
                sum = sum / ratingList.size();
                Log.v("SUM RATINGS", currentRating.getLocationName().toString() + " " + (String.valueOf(sum)));
            }
            // make new rating object with average rating
            Rating averagedRating = new Rating();
            averagedRating.setId(ratings.get(i).getId());
            averagedRating.setLocationName(ratings.get(i).getLocationName());
            averagedRating.setRating(sum);

            Log.v("AVERAGED RATING", String.valueOf(averagedRating.getRating()));

            averagedRatings.add(averagedRating);
        }
        //Log.v("AVERAGE RATINGS", averagedRatings.toString());

        return averagedRatings;
    }
}
