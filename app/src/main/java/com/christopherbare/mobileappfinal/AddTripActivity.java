package com.christopherbare.mobileappfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.util.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddTripActivity extends AppCompatActivity {
    EditText tripNameET, cityET;
    Button addButton, searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripNameET = findViewById(R.id.tripNameEditText);
        cityET = findViewById(R.id.cityEditText);
        addButton = findViewById(R.id.addTripButton);
        searchButton = findViewById(R.id.searchButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to the database
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //search via the api for cities
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
                        getString(R.string.places_API_key) +
                        "&types=(cities)" +
                        "&input="
                        +cityET.getText().toString();
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    result = IOUtils.toString(connection.getInputStream(), "UTF8");
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
            finally

            {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        public GetKeywordAsync(){
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                String[] array = s.split(";");
                for (int i = 0; i < array.length; i++) {
                    keywords.add(array[i]);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, keywords);
                spinner.setAdapter(adapter);
                goButton.setClickable(true);
            } else {
                Toast.makeText(context, "Error retrieving spinner items", Toast.LENGTH_SHORT).show();
            }

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
