package com.sku.archbiz2015.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sku.archbiz2015.R;
import com.sku.archbiz2015.network.NetworkCheckGPS;
import com.sku.archbiz2015.utils.GpsInfo;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private static final double VALUE = 0.0001;
    double latitude, longitude;
    ArrayList<Double> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final GpsInfo gps = new GpsInfo(HomeActivity.this);
        if(!gps.isGetLocation()) {
            gps.showSettingsAlert();
        }

        LinearLayout liNow = (LinearLayout)findViewById(R.id.linearNow);
        liNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // 현재 위치를 계산
                new NetworkCheckGPS(HomeActivity.this).execute(1,latitude,longitude, VALUE);

/*                Intent it = new Intent(getApplication(), SecondPageActivity.class);
                it.putExtra("Latitude", latitude);
                it.putExtra("Longitude", longitude);
                it.putExtra("value",VALUE);
                startActivity(it);*/
            }
        });

        LinearLayout liMap = (LinearLayout)findViewById(R.id.linearMap);
        liMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(), MapSensorActivity.class);
                startActivity(it);
            }
        });

        LinearLayout liCamera = (LinearLayout)findViewById(R.id.linearCamera);
        liCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(),CameraActivity.class);
                startActivity(it);
            }
        });

        LinearLayout liHistory = (LinearLayout)findViewById(R.id.linearHistory);
        liHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"준비 중 입니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
