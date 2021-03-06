package com.example.sonic.fspotter.json;


/**
 * Created by Windows on 02-03-2015.
 */
public class Endpoints {
    public static String getRequestUrlLocations(int limit) {

        return "http://46.101.165.28/task_manager/v1/locations";
        /*return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;*/
    }

    public static String getRequestUrlRatings(int limit) {

        return "http://46.101.165.28/task_manager/v1/ratings";
        /*return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;*/
    }

    public static String getRequestUrlComments(int limit) {

        return "http://46.101.165.28/task_manager/v1/comments";
        /*return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;*/
    }

    public static String getRequestUrlImages(int limit) {

        return "http://46.101.165.28/task_manager/v1/images";
        /*return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;*/
    }

    public static String getRequestUrlUpcomingMovies(int limit) {

        return "https://gowdy.iriscouch.com/gowdy/_design/gowdy/_view/alleKneipen";
        /*return URL_UPCOMING
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;*/
    }
}
