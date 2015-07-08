package com.example.sonic.fspotter.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMap extends ActionBarActivity {

    private Toolbar mToolbar;
    private GoogleMap googleMap;

    private String mLocationName;
    private String mLocationLatitudeToString;
    private String mLocationLongitudeToString;
    private String mLocationMapIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setupToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();

        mLocationName = intent.getStringExtra("locationName");
        mLocationLatitudeToString = intent.getStringExtra("latitude");
        mLocationLongitudeToString = intent.getStringExtra("longitude");
        mLocationMapIconId = intent.getStringExtra("mapIconId");

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

            // latitude and longitude
            double locationLatitudeToDouble = Double.parseDouble(mLocationLatitudeToString);
            double locationLongitudeToDouble = Double.parseDouble(mLocationLongitudeToString);

            // create marker
            if (mLocationMapIconId.equals("Baum")) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.baum));
                // adding marker
                googleMap.addMarker(marker);

            } else if (mLocationMapIconId.equals("Berg")) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.berg));
                // adding marker
                googleMap.addMarker(marker);

            } else if (mLocationMapIconId.equals("Kirche")) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.kirche));
                // adding marker
                googleMap.addMarker(marker);

            } else if (mLocationMapIconId.equals("Leuchtturm")) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.leuchtturm));
                // adding marker
                googleMap.addMarker(marker);

            } else if (mLocationMapIconId.equals("Wasserfall")) {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.wasserfall));
                // adding marker
                googleMap.addMarker(marker);

            } else {
                MarkerOptions marker = new MarkerOptions().position(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).title(mLocationName).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.sonstiges));
                // adding marker
                googleMap.addMarker(marker);

            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(locationLatitudeToDouble, locationLongitudeToDouble)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
