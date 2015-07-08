package com.example.sonic.fspotter.fragments;

import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.example.sonic.fspotter.callbacks.LocationsLoadedListener;
import com.example.sonic.fspotter.database.DBLocations;
import com.example.sonic.fspotter.fspotter.MyApplication;
import com.example.sonic.fspotter.logging.L;
import com.example.sonic.fspotter.pojo.Location;
import com.example.sonic.fspotter.task.TaskLoadLocations;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMap#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMap extends Fragment implements LocationsLoadedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = FragmentMap.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    EditText etLocation;
    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    MarkerOptions markerOptionsSearch;
    LatLng latLngSearch;

    //The key used to store arraylist of movie objects to and from parcelable
    private static final String STATE_MOVIES = "state_movies";
    //the arraylist containing our list of box office his
    private ArrayList<Location> mListLocations = new ArrayList<>();

    public FragmentMap() {
    }

    public static FragmentMap newInstance(String param1, String param2) {

        FragmentMap fragment = new FragmentMap();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, param1);
        bundle.putString(ARG_PARAM2, param2);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.i(TAG, " " + mParam1 + ", " + mParam2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate and return the layout
        View v = inflater.inflate(R.layout.fragment_map, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //if this fragment starts for the first time, load the list of movies from a database
        mListLocations = MyApplication.getWritableDatabase().readLocations(DBLocations.LOCATIONS);
        //if the database is empty, trigger an AsycnTask to download movie list from the web

        if (!mListLocations.isEmpty()) {
            initializeMap(mListLocations);
        }

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        // Getting reference to btn_find of the layout activity_main
        Button btn_find = (Button) v.findViewById(R.id.btn_find);
        etLocation = (EditText) v.findViewById(R.id.et_location);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting user input location
                String location = etLocation.getText().toString();

                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
                // Hides keyboard after clicking "Find"
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etLocation.getWindowToken(), 0);
            }
        });

        return v;
    }

    private void initializeMap (ArrayList<Location> listLocations) {
        googleMap = mMapView.getMap();

        if (googleMap == null) {
            Toast.makeText(getActivity(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (listLocations != null) {
                initializeMarkers(listLocations);
            }
        }
    }

    private void initializeMarkers(ArrayList<Location> listLocations) {
        Log.v("MARKER", listLocations.toString());
        ArrayList<Marker> mMarkers = new ArrayList<>();

        for (int i = 0; i < listLocations.size(); i++) {
            BitmapDescriptor bitmapMarker;
            // latitude and longitude
            double locationLatitudeToDouble = listLocations.get(i).getLatitude();
            double locationLongitudeToDouble = listLocations.get(i).getLongitude();

            // create marker
            if (listLocations.get(i).getMapIconId().equals("Baum")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.baum);

            } else if (listLocations.get(i).getMapIconId().equals("Berg")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.berg);

            } else if (listLocations.get(i).getMapIconId().equals("Kirche")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.kirche);

            } else if (listLocations.get(i).getMapIconId().equals("Leuchtturm")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.leuchtturm);

            } else if (listLocations.get(i).getMapIconId().equals("Wasserfall")) {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.wasserfall);

            } else {
                bitmapMarker = BitmapDescriptorFactory.fromResource(R.drawable.sonstiges);
            }

            mMarkers.add(googleMap.addMarker(new MarkerOptions().position(new LatLng(listLocations.get(i).getLatitude(), listLocations.get(i).getLongitude())).title(listLocations.get(i).getLocationName()).icon(bitmapMarker)));
        }
        // CameraPosition should be our current location
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(42.82348923, 8.34987234)).zoom(7).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mListLocations = MyApplication.getWritableDatabase().readLocations(DBLocations.LOCATIONS);
        if (!mListLocations.isEmpty()) {
            initializeMarkers(mListLocations);
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

        /*if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    /**
     * Called when the AsyncTask finishes load the list of movies from the web
     */
    @Override
    public void onLocationsLoaded(ArrayList<Location> listLocations) {
        mListLocations = listLocations;
        if (mListLocations != null) {
            initializeMarkers(mListLocations);
        }
    }

    private void handleNewLocation(android.location.Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Hier sind Sie!");
        googleMap.addMarker(options);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) getActivity());
        }
        else {
            handleNewLocation(location);
        };
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the Error
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    // Hides the keyboard by clicking anywhere outside of an edittext
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }*/

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getActivity().getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(getActivity().getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Adding Markers on Google Map for each matching address
            //assert addresses != null;

            if (addresses != null) {
                for(int i=0;i<addresses.size();i++){

                    Address address = (Address) addresses.get(i);

                    // Creating an instance of GeoPoint, to display in Google Map
                    latLngSearch = new LatLng(address.getLatitude(), address.getLongitude());

                    String addressText = String.format("%s, %s",
                            address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                            address.getCountryName());

                    markerOptionsSearch = new MarkerOptions();
                    markerOptionsSearch.position(latLngSearch);
                    markerOptionsSearch.title(addressText);

                    googleMap.addMarker(markerOptionsSearch);

                    // Locate the first location
                    if(i==0)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLngSearch));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLngSearch)            // Sets the center of the map to location user
                            .zoom(10)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }
}
