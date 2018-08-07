package com.christopherbare.mobileappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Trip> trips = new ArrayList<>();
    TripAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        database.child("trips").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  trips.clear();
                  for (DataSnapshot node : dataSnapshot.getChildren()) {

                      Trip trip = node.getValue(Trip.class);
                      trip.key = node.getKey();
                      trips.add(trip);
                  }
                  adapter.notifyDataSetChanged();
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

        adapter = new TripAdapter(MainActivity.this, trips, new TripAdapter.SendData() {

            @Override
            public void deleteTrip(Trip trip) {
                try {
                    if (database.child("trips").child(trip.key).getKey() != null) {
                        database.child("trips")
                                .child(trip.key)
                                .removeValue();
                    }
                } catch (Exception e) {
                    Log.e("demo", "deleteMessage: Exception", e);
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
