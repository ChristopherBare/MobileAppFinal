package com.christopherbare.mobileappfinal;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    Activity context;
    ArrayList<Place> places;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Trip trip;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewPlace;
        ImageView placeImageView;
        ImageView addPlaceImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPlace = itemView.findViewById(R.id.placeTextView);
            placeImageView = itemView.findViewById(R.id.placeImageView);
            addPlaceImageView = itemView.findViewById(R.id.addPlaceImageView);
        }
    }

    public PlaceAdapter(Activity context, ArrayList<Place> places, Trip trip) {
        this.context = context;
        this.places = places;
        this.trip = trip;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_place_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Place place = places.get(position);
        holder.textViewPlace.setText(place.getName());
        Picasso.get().load(place.getIcon()).into(holder.placeImageView);
        holder.addPlaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trip.getPlaces().add(place);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/trips/" + trip.key, trip);
                database.updateChildren(childUpdates);

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("place", place);
                context.startActivity(intent);
                context.finish();
            }
        });

    }


    @Override
    public int getItemCount() {
        return places.size();
    }




}
