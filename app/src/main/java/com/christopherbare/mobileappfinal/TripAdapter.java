package com.christopherbare.mobileappfinal;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        ImageView deleteButton, placeButton;
        TextView tripName, city;
        RecyclerView placesRecyclerView;

        public ViewHolder(final View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.tripTextView);
            city = itemView.findViewById(R.id.cityTextView);
            deleteButton = itemView.findViewById(R.id.deleteImageView);
            placeButton = itemView.findViewById(R.id.placeTextView);
            placesRecyclerView = itemView.findViewById(R.id.recyclerView);
        }

        public void bind(final Trip item, final SendData data) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.deleteMessage(item);
                }
            });
        }
    }

    public TripAdapter (Activity context, ArrayList<Trip> messages, SendData data) {
        this.context = context;
        this.trips = messages;
        this.data = data;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public TripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

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

        holder.placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.addComment(trip);
            }
        });

        if(trip.getPlaces().size() > 0){
            holder.placesRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.placesRecyclerView.setLayoutManager(mLayoutManager);
            adapter = new PlaceAdapter(context, trip.getPlace());
            holder.placesRecyclerView.setAdapter(adapter);
        } else {
            holder.placesRecyclerView.setVisibility(View.GONE);
        }

    }

    public interface SendData {
        void deleteMessage(Trip trip);
        void addComment(Trip trip);
    }

    public class ListItem {
        Trip trip;
        Place place;
    }


}

