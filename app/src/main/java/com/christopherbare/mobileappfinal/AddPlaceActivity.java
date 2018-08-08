package com.christopherbare.mobileappfinal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Place> places = new ArrayList<>();
    PlaceAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    String placeID;
    String latitude, longitude;

    @Override
    protected void onStart() {
        super.onStart();
        if(isConnected())
            new GetPlacesAsync().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        if(isConnected())
            new GetLocationAsync().execute();

        recyclerView = findViewById(R.id.placeRecyclerView);

        if(getIntent() != null && getIntent().getExtras() != null) {
            placeID = getIntent().getStringExtra("place_id");
        }

        adapter = new PlaceAdapter(this, places);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    private class GetPlacesAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            String result = null;
            try {
                String strUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key="
                        + "AIzaSyDibjKMrJAEjxFgTDhZUnGcu9mNYcwkXNQ"
                        + "&location="
                        + latitude + "," + longitude
                        + "&radius=1000";
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject object = new JSONObject(result);
                    JSONArray results = object.getJSONArray("results");

                    for(int i = 0; i < results.length(); i++){
                        Place place = new Place();
                        JSONObject term = results.getJSONObject(i);
                        JSONObject location = term.getJSONObject("geometry").getJSONObject("location");
                        place.setLat(location.getString("lat"));
                        place.setLng(location.getString("lng"));
                        place.setName(term.getString("name"));
                        place.setIcon(term.getString("icon"));
                        //change if needed
                        place.setPlaceID(term.getString("place_id"));
                        places.add(place);
                    }
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
            Toast.makeText(AddPlaceActivity.this, s, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
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
            Toast.makeText(AddPlaceActivity.this, s, Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


}
