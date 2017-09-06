package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 6/12/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter( Context context, ArrayList<Earthquake> equake) {
        super(context, 0, equake);
    }



    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        /*display magnitude earthquake*/
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude_text_view);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        int magnitudeColor = getMagnitudeColor(currentEarthquake.getmMagnitude());

        magnitudeCircle.setColor(magnitudeColor);

        String formattedMagnitude = formatMagnitude(currentEarthquake.getmMagnitude());

        magnitudeView.setText(formattedMagnitude);

        /*display location earthquake */
        TextView earthquakeDistanceView = (TextView) listItemView.findViewById(R.id.distance_text_view);

        String earthquakeDistance = distance(currentEarthquake.getmPlace());

        earthquakeDistanceView.setText(earthquakeDistance);

        TextView earthquakeCountryView = (TextView) listItemView.findViewById(R.id.city_text_view);

        String earthquakeCountry = country(currentEarthquake.getmPlace());

        earthquakeCountryView.setText(earthquakeCountry);

        /*display date earthquake*/
        Date dateObject = new Date(currentEarthquake.getmDate());

        TextView earthquakeDateView = (TextView) listItemView.findViewById(R.id.date_text_view);

        String formattedDate = formatDate(dateObject);

        earthquakeDateView.setText(formattedDate);

        TextView earthquakeTimeView = (TextView) listItemView.findViewById(R.id.time_text_view);

        String formattedTime = formatTime(dateObject);

        earthquakeTimeView.setText(formattedTime);

        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String distance(String place) {
        String distance ;
        if(place.contains(" of")) {
            String[] parts = place.split(" of",2);
                distance = parts[0] + " of";
        }
        else {
            distance ="Near the";
        }
        return  distance;
    }

    private String country(String place) {
        String country ;
        if(place.contains(" of")) {
            String[] parts = place.split(" of",2);
            country = parts[1];

        }
        else {
            country = place;
        }
        return country;
    }

    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
