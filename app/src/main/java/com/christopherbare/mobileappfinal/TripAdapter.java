package com.christopherbare.mobileappfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Trip> {
    public TripAdapter(Context context, int resource, List<Trip> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trip trip = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.trip_item, parent, false);

//        TextView textViewName = convertView.findViewById(R.id.contactName);
//        TextView textViewEmail = convertView.findViewById(R.id.contactEmail);
//        TextView textViewPhone = convertView.findViewById(R.id.contactPhone);
//        ImageView imageView = convertView.findViewById(R.id.contactImage);

        //set the data from the person object
//        textViewName.setText(trip.getName());
//        textViewEmail.setText(trip.getEmail());
//        textViewPhone.setText(person.getPhone());
//        imageView.setImageResource(person.picID);



        return convertView;
    }

}

