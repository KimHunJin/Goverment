package com.sku.archbiz2015.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


/**
 * Created by HunJin on 2015-10-27.
 */
public class GpsInfo extends Service implements LocationListener {

    private final Context mContext;

    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat;
    double lon;

    // GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_UPDATES = 10;

    // GPS 정보 업데이트 시간 1/1000
    private static final long MIN_TIME_UPDATES = 1000;

    protected LocationManager locationManager;

    public GpsInfo(Context context) {
        this.mContext = context;
        Log.e("test","GpsInfo launch");
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {
                Log.e("Oh my GOD", "Oh My God");
            } else {
                this.isGetLocation = true;
                Log.e("getLocation", isGetLocation+"");
                if(isNetworkEnabled) {
                    Log.e("isNetwork", "isNetwork");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_UPDATES, this);

                    if(locationManager != null) {
                        Log.e("locationManager", "locationManager");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null) {
                            Log.e("Yes", "Oh Yeah!");
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnabled) {
                    Log.e("isGps?","isGps?");
                    if(location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_UPDATES,
                                MIN_DISTANCE_UPDATES,
                                this);
                        if(locationManager != null) {
                            Log.e("locationManager", "locationManager");
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                Log.e("Yes", "Oh Yeah!");
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopUsingGPS() {
        if(locationManager != null) {
            locationManager.removeUpdates(GpsInfo.this);
        }
    }

    public double getLatitude() {
        Log.e("getLat","getLatitude");
        if(location != null) {
            lat = location.getLatitude();
            Log.e("getLat",lat+"");
        }
        return lat;
    }

    public double getLongitude() {
        Log.e("getLon","getLongitude");
        if(location != null) {
            lon = location.getLongitude();
            Log.e("getLon",lon+"");
        }
        return lon;
    }

    public boolean isGetLocation() {
        Log.e("isGetLocation",this.isGetLocation+"");
        return this.isGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무 세팅");
        alertDialog.setMessage("GPS 세팅이 되지 않았을 수도 있습니다.\n 설정창으로 가시겠습니까?");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(it);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public IBinder onBind(Intent arg0) {
        Log.e("bind","bind");
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

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
