package com.example.sonic.fspotter.json;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.network.CustomJsonObjectRequest;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by Windows on 02-03-2015.
 */
public class Requestor {
    public static JSONObject requestLocationsJSON(RequestQueue requestQueue, String url) {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET,
                url,
                (String)null, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            L.m(e + "");
        } catch (ExecutionException e) {
            L.m(e + "");
        } catch (TimeoutException e) {
            L.m(e + "");
        }
        return response;
    }

    public static JSONObject requestRatingsJSON(RequestQueue requestQueue, String url) {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET,
                url,
                (String)null, requestFuture, requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            L.m(e + "");
        } catch (ExecutionException e) {
            L.m(e + "");
        } catch (TimeoutException e) {
            L.m(e + "");
        }
        return response;
    }
}
