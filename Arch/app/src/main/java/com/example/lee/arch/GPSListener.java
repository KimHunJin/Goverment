package com.example.lee.arch;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationResult;

import java.util.Timer;

/**
 * Created by lee on 2015-10-07.
 */
public class GPSListener implements LocationListener {

    double latitude, longitude;
    long minTime = 2000;
    long minDistance = 0;
    LocationManager lm;
    Location lastLocation;
    boolean gps_enable = false;
    boolean network_enable = false;
    String provide;


    public Location getLocation(Context context) {
        if (lm == null) {
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        try {
            gps_enable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enable = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enable && !network_enable) {
                return null;
            }
            if (gps_enable) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            }
            if (network_enable) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
            }

            lastLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return lastLocation;
    }


    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
