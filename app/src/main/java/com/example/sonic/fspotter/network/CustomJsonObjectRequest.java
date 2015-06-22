package com.example.sonic.fspotter.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonic on 22.06.15.
 */
public class CustomJsonObjectRequest extends JsonObjectRequest
{
    public CustomJsonObjectRequest(int method, String url, String jsonRequest,Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        headers.put("Authorization", "6c7e40dc7756f2132331a7893f1a97be");
        return headers;
    }

}
