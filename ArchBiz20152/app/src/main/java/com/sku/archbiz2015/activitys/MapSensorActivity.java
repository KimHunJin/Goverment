package com.sku.archbiz2015.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sku.archbiz2015.R;
import com.sku.archbiz2015.item.SensorItem;
import com.sku.archbiz2015.utils.GpsInfo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapSensorActivity extends FragmentActivity implements GoogleMap.OnMapClickListener {

    TextView txtDirectionValue; //방향 값 텍스트뷰
    GoogleMap map;  //구글맵
    SensorItem sensorItem;
    long startTime;
    double currAzimuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MapsInitializer.initialize(getApplicationContext());

        init();

        //현재위치 버튼 클릭시 구글맵 나옴
        RelativeLayout reHere = (RelativeLayout)findViewById(R.id.hereLayout);
        reHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }

    public void onMapClick(LatLng point) {



        Point screenPt = map.getProjection().toScreenLocation(point);
        LatLng latLng = map.getProjection().fromScreenLocation(screenPt);
        Log.e("map", point.latitude + "  " + point.longitude);
    }

    private void init() {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapSensorActivity.this);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFrist)).getMap();
        map.clear();

        GpsInfo gps = new GpsInfo(MapSensorActivity.this);
        if(gps.isGetLocation()) {
            Log.e("isGpsLocation", "isGpsLocaion is true");
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Log.e("Location", latitude + "  " + longitude);

            final LatLng latLng = new LatLng(latitude,longitude);
            final LatLng[] clickLatLng = {latLng};

            final String city = getCity(latitude, longitude);
            makeMarker(latLng, city);

            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    map.clear();
                    Log.e("map", point.latitude + "  " + point.longitude);
                    clickLatLng[0] = new LatLng(point.latitude,point.longitude);
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(point);
                    map.addMarker(marker);
                }
            });


            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    startSensorService();

                    Intent it = new Intent(getApplication(),SecondPageActivity.class);
                    it.putExtra("Latitude", clickLatLng[0].latitude);
                    it.putExtra("Longitude", clickLatLng[0].longitude);
                    startActivity(it);
                    return false;
                }
            });

        } else {
            gps.showSettingsAlert();
        }
    }

    //센서값 받아오는 함수
    private SensorItem startSensorService()
    {
        SensorItem sensorItem = new SensorItem(); //센서값 받는 객체 생성

        //센서
        DirectionListener directionListener = new DirectionListener(getApplicationContext(), txtDirectionValue);
        directionListener.onListenerRegister();
        return sensorItem;
    }

    public class DirectionListener implements SensorEventListener {

        SensorManager sensorManager;
        Sensor sensor;
        Context context;

        public DirectionListener(Context context, TextView txtDirectionValue)
        {
            this.context = context;
            sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        public void onListenerRegister()
        {
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_UI);
        }

        public void onListenerUnregister()
        {
            sensorManager.unregisterListener(this);
        }



        public void onSensorChanged(SensorEvent event) {

            if(event.sensor.getType() == Sensor.TYPE_ORIENTATION)
            {
                if(currAzimuth-event.values[0]<2 || -2<currAzimuth-event.values[0])
                {
                    long currTime = System.currentTimeMillis();
                    if(currTime-startTime<2000 || currTime-startTime<1500)
                    {

                        onListenerUnregister();
                    }
                }

                Log.e("sensor",currAzimuth-event.values[0]+"" );
                sensorItem = new SensorItem();
                sensorItem.setAzimuth("" + event.values[0]);
                sensorItem.setPitch("" + event.values[1]);
                sensorItem.setRoll("" + event.values[2]);

            }

            currAzimuth = Double.parseDouble(sensorItem.getAzimuth());
            startTime = System.currentTimeMillis();
        }


        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private String getCity(double latitude, double longitude) {
        String cityName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = cityName;
        return s;
    }

    private void makeMarker(LatLng latLng, String city) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("현재 위치");
        options.snippet(city);
        map.addMarker(options).showInfoWindow();
    }


}
