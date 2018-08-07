package com.christopherbare.mobileappfinal;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    SendData data;
    Activity context;
    ArrayList<Trip> trips;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    ArrayList<ListItem> array = new ArrayList<>();
    PlaceAdapter adapter;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mapButton, placeButton;
        TextView tripName, city;
        RecyclerView placesRecyclerView;

        public ViewHolder(final View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripTextView);
            city = itemView.findViewById(R.id.cityTextView);
            mapButton = itemView.findViewById(R.id.mapImageView);
            placeButton = itemView.findViewById(R.id.addImageView);
            placesRecyclerView = itemView.findViewById(R.id.recyclerView);
        }

        public void bind(final Trip item, final SendData data) {
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.deleteTrip(item);
                }
            });
        }
    }

    public TripAdapter (Activity context, ArrayList<Trip> trips, SendData data) {
        this.context = context;
        this.trips = trips;
        this.data = data;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trip trip = trips.get(position);
        if (holder.city != null && holder.tripName != null && holder.placeButton != null && holder.mapButton != null) {
            holder.city.setText(trip.getPlace());
            holder.tripName.setText(trip.getTripName());

            holder.placeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO do stuff for the place button
                    Toast.makeText(context, "addPlace", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), AddPlaceActivity.class);
                    context.startActivity(intent);
                }
            });

            holder.mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO Stuff for maps
                    Toast.makeText(context, "map", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        /*if(trip.getPlaces().size() > 0){
            holder.placesRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.placesRecyclerView.setLayoutManager(mLayoutManager);
            adapter = new PlaceAdapter(context, trip.getPlaces());
            holder.placesRecyclerView.setAdapter(adapter);
        } else {
            holder.placesRecyclerView.setVisibility(View.GONE);
        }
*/
    }

    public interface SendData {
        void deleteTrip(Trip trip);



    }

    public class ListItem {
        Trip trip;
        Place place;
    }


}

