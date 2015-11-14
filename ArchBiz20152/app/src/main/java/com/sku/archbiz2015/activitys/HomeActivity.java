package com.sku.archbiz2015.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sku.archbiz2015.R;
import com.sku.archbiz2015.utils.GpsInfo;

public class HomeActivity extends AppCompatActivity {


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
                Intent it = new Intent(getApplication(), SecondPageActivity.class);
                it.putExtra("Latitude", gps.getLatitude());
                it.putExtra("Longitude", gps.getLongitude());
                startActivity(it);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
