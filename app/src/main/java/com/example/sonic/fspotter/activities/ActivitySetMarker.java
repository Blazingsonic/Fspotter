package com.example.sonic.fspotter.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonic.fspotter.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivitySetMarker extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView latitude;
    TextView longitude;
    String mLatitude = "NA";
    String mLongitude = "NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_marker);
        setUpMapIfNeeded();

        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);

        Button ConfirmButton = (Button)
                findViewById(R.id.btn_confirm);


        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                mLatitude = String.valueOf(latLng.latitude);
                mLongitude = String.valueOf(latLng.longitude);

                //Fill textfield with latitude and longitude
                TextView latitude = (TextView)
                        findViewById(R.id.latitude);
                latitude.setText("Breitengrad: " + mLatitude);

                TextView longitude = (TextView)
                        findViewById(R.id.longitude);
                longitude.setText("Längengrad: " + mLongitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });

        // ClickListener implementieren für den Button zum Wechsel der Activity
        ConfirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Neues Intent anlegen
                Intent nextScreen = new Intent(getApplicationContext(), ActivityMain.class);

                //Intent mit den Daten füllen
                if (!mLatitude.equals("NA") && !mLongitude.equals("NA")) {
                    nextScreen.putExtra("Latitude", mLatitude);
                    nextScreen.putExtra("Longitude", mLongitude);

                    // Log schreiben für Logausgabe
                    Log.e("n", latitude.getText() + "." + longitude.getText());

                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(ActivitySetMarker.this, "Set marker first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
