package com.sku.archbiz2015.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sku.archbiz2015.item.GPSItem;
import com.sku.archbiz2015.network.NetworkGetImagePath;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by HunJin on 2015-11-16.
 * <p/>
 * When GPS is not exactly, this page intent
 * Loading thie page, get all GPS from radius of a few meter
 * Then make marker on map
 * If marker click, the next page intent
 */
public class MapSelectActivity extends AppCompatActivity {
    GoogleMap map;  //구글맵

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);

        Log.e("StartMap", "StartMapSelectActivity");
        Intent it = getIntent();
        ArrayList<Double> mLatitude = (ArrayList<Double>) it.getSerializableExtra("Latitude");
        ArrayList<Double> mLongitude = (ArrayList<Double>) it.getSerializableExtra("Longitude");
        ArrayList<String> mGPSName = (ArrayList<String>) it.getSerializableExtra("GPSName");

        MapsInitializer.initialize(getApplicationContext());
        init();
        MarkerOptions marker = new MarkerOptions();
        for (int i = 0; i < mLatitude.size(); i++) {
            LatLng latLng = new LatLng(mLatitude.get(i), mLongitude.get(i));
            marker.position(latLng);
            marker.title("위치");
            marker.snippet(mGPSName.get(i));
            map.addMarker(marker);
        }
        LatLng latLng = new LatLng(mLatitude.get(0), mLongitude.get(0));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(17));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                marker.showInfoWindow();
                return false;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.marker_window_layout, null);
                TextView txtMarkerLoadName = (TextView) view.findViewById(R.id.txtMarkerLoadName);
                txtMarkerLoadName.setText(marker.getSnippet());


                ImageView imgNextPage = (ImageView) view.findViewById(R.id.imgNextPage);
                imgNextPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("OnClick", "OnClick2");
                        Intent it = new Intent(getApplication(), SecondPageActivity.class);
                        it.putExtra("Latitude", marker.getPosition().latitude);
                        it.putExtra("Longitude", marker.getPosition().longitude);
                        it.putExtra("GPSName", marker.getSnippet());
                        startActivity(it);
                    }
                });
                return view;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e("onClick", "onClick");
                new NetworkGetImagePath(MapSelectActivity.this).execute(marker.getSnippet(), marker.getPosition().latitude, marker.getPosition().longitude);
//                Intent it = new Intent(getApplication(), SecondPageActivity.class);
//                it.putExtra("Latitude", marker.getPosition().latitude);
//                it.putExtra("Longitude", marker.getPosition().longitude);
//                it.putExtra("GPSName", marker.getSnippet());
//                startActivity(it);
            }
        });
    }

    protected void init() {
        GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapSelectActivity.this);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapSelect)).getMap();
    }
}
