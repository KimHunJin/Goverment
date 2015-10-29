package com.example.lee.arch;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BuilderActivity extends AppCompatActivity {

    GoogleMap map;
    MapFragment mapFragment;
    TextView txtStreetAddress;
    ImageView imgPicture, imgAlbum; //사진, 앨범
    ImageView imgQuery1,imgQuery2, imgQuery3, imgQuery4; //RoundImageView 4개짜리
    Intent intent;
    LatLng currentPosition;
    Double Latitude, Longitude;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builderinfo);
        txtStreetAddress = (TextView)findViewById(R.id.txtStreetAddress);

        intent = getIntent();
        Latitude = intent.getExtras().getDouble("Latitude");
        Longitude = intent.getExtras().getDouble("Longitude");
        imgPicture = (ImageView)findViewById(R.id.imgPicture);
        imgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"toast",Toast.LENGTH_SHORT).show();
            }
        });
        currentPosition = new LatLng(Latitude, Longitude);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapSecond);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(false);
        map.addMarker(new MarkerOptions()
        .title("현재 위치")
        .position(currentPosition));
        drawMarker(Latitude, Longitude);

    }

    private void drawMarker(double Latitude, double Longitude)
    {

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        map.addMarker(new MarkerOptions().position(currentPosition)
                .snippet("Lat : " + Latitude + "Lag : " + Longitude)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재 위치"));

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
