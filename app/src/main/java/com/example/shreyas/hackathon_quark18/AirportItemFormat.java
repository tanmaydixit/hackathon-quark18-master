package com.example.shreyas.hackathon_quark18;

import android.os.Parcel;
import android.os.Parcelable;


public class AirportItemFormat implements Parcelable {
    private String key;
    private double lat;
    private double lon;
    private String name;

    public AirportItemFormat() {
    }

    public AirportItemFormat(String key, double lat, double lon, String name) {
        this.key = key;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.name);
    }

    protected AirportItemFormat(Parcel in) {
        this.key = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<AirportItemFormat> CREATOR = new Parcelable.Creator<AirportItemFormat>() {
        @Override
        public AirportItemFormat createFromParcel(Parcel source) {
            return new AirportItemFormat(source);
        }

        @Override
        public AirportItemFormat[] newArray(int size) {
            return new AirportItemFormat[size];
        }
    };
}
