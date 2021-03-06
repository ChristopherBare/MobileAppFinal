package com.christopherbare.mobileappfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AddTripActivity extends AppCompatActivity {
    EditText tripNameET, cityET;
    Button addButton, searchButton;
    RecyclerView citiesList;
    CityAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    static ArrayList<String> cities = new ArrayList<>();
    static String city;
    DatabaseReference dbReference;
    static ArrayList<String> place_ids = new ArrayList<>();
    static String placeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripNameET = findViewById(R.id.tripNameEditText);
        cityET = findViewById(R.id.cityEditText);
        addButton = findViewById(R.id.addTripButton);
        searchButton = findViewById(R.id.searchButton);
        dbReference = FirebaseDatabase.getInstance().getReference();
        citiesList = findViewById(R.id.citiesList);
        citiesList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        citiesList.setLayoutManager(layoutManager);
        adapter = new CityAdapter(this, cities, place_ids, new CityAdapter.SendData() {
            @Override
            public void selectCity(String citySelect, String place) {
                city = citySelect;
                placeID = place;
            }
        });

        citiesList.setAdapter(adapter);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip = new Trip();
                Log.i("demo", "onClick: tripManeEDNSNDFLKNL" + tripNameET.getText().toString());
                trip.setTripName(tripNameET.getText().toString());
                trip.setPlace(city);
                trip.setPlaceID(placeID);

                dbReference.child("trips")
                        .push()
                        .setValue(trip);

                Intent intent = new Intent(AddTripActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cityET != null && !cityET.getText().toString().isEmpty() && cityET.getText().toString() != "" && isConnected()){
                    new GetCitiesAsync().execute();
                } else {
                    Toast.makeText(AddTripActivity.this, "Enter in a value", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class GetCitiesAsync extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            String result = null;
            try

            {
                String strUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=" +
                        "AIzaSyDibjKMrJAEjxFgTDhZUnGcu9mNYcwkXNQ"
                        +
                        "&types=(cities)" +
                        "&input="
                        +cityET.getText().toString();
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONObject object = new JSONObject(result);
                    JSONArray predictions = object.getJSONArray("predictions");

                    for(int i = 0; i < predictions.length(); i++){
                        JSONArray terms = predictions.getJSONObject(i).getJSONArray("terms");
                        String city = terms.getJSONObject(0).get("value") + ", " + terms.getJSONObject(1).get("value");
                        String pID = predictions.getJSONObject(i).getString("place_id");
                        cities.add(city);
                        place_ids.add(pID);
                    }
                    Log.i("Tag", "internet");
                } else {
                    Log.i("Tag", "no internet" + connection.getResponseCode());

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Tag", "errror");
            } catch (IOException e)

            {
                e.printStackTrace();
                Log.i("Tag", "errror");
            } //Handle the exceptions
            catch (JSONException e) {
                e.printStackTrace();
            } finally

            {
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
            Toast.makeText(AddTripActivity.this, s, Toast.LENGTH_SHORT).show();
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
