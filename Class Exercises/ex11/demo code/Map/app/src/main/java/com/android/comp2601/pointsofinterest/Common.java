package com.android.comp2601.pointsofinterest;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by seansteelebenjamin on 16-02-07.
 */
public class Common {

    static final String STATE_CITY_NAME = "city_name";
    static final String LOCATION_BUNDLE = "location";

    public static HashMap<String, LatLng> createMapCoordinates(){

        HashMap<String, LatLng> mCityCoord = new HashMap<String, LatLng>();


        LatLng ottawa = new LatLng(45.4214, -75.6919);
        mCityCoord.put("Ottawa", ottawa);

        LatLng toronto = new LatLng(43.653226, -79.383184);
        mCityCoord.put("Toronto", toronto);

        LatLng simcoe = new LatLng(42.837263, -80.304042);
        mCityCoord.put("Simcoe", simcoe);

        return mCityCoord;
    }
}
