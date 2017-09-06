package com.example.android.quakereport;

/**
 * Created by Administrator on 6/12/2017.
 */

public class Earthquake {

    /*name city quake quaked*/
    private String mPlace;

    private double mMagnitude;

    private long mDate;

    private String mUrl;


    public Earthquake(double magnitude, String place, long date, String url) {
        this.mPlace = place;
        this.mMagnitude = magnitude;
        this.mDate = date;
        this.mUrl = url;
    }

    public String getmPlace() {
        return mPlace;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public long getmDate() {
        return mDate;
    }

    public String getmUrl() {
        return mUrl;
    }
}
