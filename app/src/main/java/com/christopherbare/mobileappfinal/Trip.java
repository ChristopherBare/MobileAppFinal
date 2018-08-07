package com.christopherbare.mobileappfinal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

class Trip implements Parcelable {
String tripName, key, place, placeID;
Place placeObj;
ArrayList<Place> places = new ArrayList<>();

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public Place getPlaceObj() {
        return placeObj;
    }

    public void setPlaceObj(Place placeObj) {
        this.placeObj = placeObj;
    }

    protected Trip(Parcel in) {
        tripName = in.readString();
        key = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
        places = in.createTypedArrayList(Place.CREATOR);
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Trip() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tripName);
        parcel.writeString(String.valueOf(place));
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public static Creator<Trip> getCREATOR() {
        return CREATOR;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
