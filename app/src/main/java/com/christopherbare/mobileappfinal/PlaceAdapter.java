package com.christopherbare.mobileappfinal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    Activity context;
    ArrayList<Place> places;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewPlace = itemView.findViewById(R.id.placeTextView);
        }
    }

    public PlaceAdapter(Activity context, ArrayList<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Place place = places.get(position);
        holder.textViewPlace.setText(place.getCity());
    }


    @Override
    public int getItemCount() {
        return places.size();
    }


}
