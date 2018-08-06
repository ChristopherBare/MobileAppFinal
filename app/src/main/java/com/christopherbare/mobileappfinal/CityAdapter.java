package com.christopherbare.mobileappfinal;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    Activity context;
    ArrayList<String> cities;
    SendData data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCity;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCity = itemView.findViewById(R.id.city);
        }


    }

    public CityAdapter(Activity context, ArrayList<String> cities, SendData data) {
        this.context = context;
        this.cities = cities;
        this.data = data;
    }

    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_row_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String city = cities.get(position);
        holder.textViewCity.setText(city);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.selectCity(city);
            }
        });
    }

    public interface SendData {
        void selectCity(String string);
    }

}
