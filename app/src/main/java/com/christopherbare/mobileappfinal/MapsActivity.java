package com.christopherbare.mobileappfinal;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "demo";
    private GoogleMap mMap;
    private String placeID;
    private Trip trip;
    String latitude = "";
    String longitude = "";
    float lat = 0;
    float lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Log.i("demo", "onCreate: Reached the intent");
            placeID = getIntent().getStringExtra("place_id");
            Log.i(TAG, "onCreate: placeid" + placeID);
            trip = getIntent().getParcelableExtra("trip");
            new GetLocationAsync().execute();
        }
        Log.i("demo", "onCreate: trip naem" + latitude + " " + longitude);


    }

    private class GetLocationAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            String result = null;
            try {
                String strUrl = "https://maps.googleapis.com/maps/api/place/details/json?key="
                        + "AIzaSyDibjKMrJAEjxFgTDhZUnGcu9mNYcwkXNQ&place_id="
                        + placeID;
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject object = new JSONObject(result);
                    JSONObject results = object.getJSONObject("result");
                    JSONObject geometry = results.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    latitude = location.getString("lat");
                    longitude = location.getString("lng");
                    trip.setLat(latitude);
                    trip.setLng(longitude);


                    Log.i("Tag", "internet");
                } else {
                    Log.i("Tag", "no internet" + connection.getResponseCode());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Tag", "errror");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("Tag", "errror");
            } //Handle the exceptions
            catch (JSONException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lat = Float.parseFloat(latitude);
            lng = Float.parseFloat(longitude);
        }
    }

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng tripLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(tripLocation).title(trip.getTripName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(tripLocation));
        }
    }

