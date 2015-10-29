package com.example.lee.arch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {

    TextView txtAddress, txtDirectionValue; //방향 값 텍스트뷰
    Button btnPosition; //현재위치 버튼
    String msg;
    private GoogleMap map;  //구글맵
    Location lastLocation;  //위치저장 로케이션
    MapFragment mapFragment;  //맵프래그먼트
    LinearLayout mapForm;  //구글맵을 담는 폼
    Intent intent;

    private double mLng;
    private double mLat;

    static final LatLng latLng = new LatLng( 37.56, 126.97);  //초기 위치 서울울
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapForm = (LinearLayout)findViewById(R.id.mapForm);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDirectionValue = (TextView)findViewById(R.id.txtDirectionValue);
        btnPosition = (Button) findViewById(R.id.btnPosition);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrist);


        startLocationService();//GPS로 위도,경도 받아옴

                //현재위치 버튼 클릭시 구글맵 나옴
                btnPosition.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        mLat = lastLocation.getLatitude();
                        mLng = lastLocation.getLongitude();
                        startSensorService();
                        txtAddress.setText(msg);
                        mapForm.setVisibility(View.VISIBLE);
                        map = mapFragment.getMap();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng));
                        map.setMyLocationEnabled(true);
                        map.addMarker(new MarkerOptions()
                        .position(new LatLng(mLat,mLng))
                        .title("좌표 : " + mLat + mLng));
                        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                            public boolean onMarkerClick(Marker marker) {
                                intent = new Intent(getApplication() , BuilderActivity.class);
                                intent.putExtra("Latitude", mLat);
                                intent.putExtra("Longitude", mLng);
                                startActivity(intent);
                                return true;
                            }
                        });
                    }
                });

    }


    //구글맵 마커표시해주는 함수
    private void drawMarker(Location location) {

        map.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        map.addMarker(new MarkerOptions().position(currentPosition)
                .snippet("Lat : " + location.getLatitude() + "Lag : " + location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("현재 위치"));

    }

    private void startLocationService() {

        GPSListener gpsListener = new GPSListener(); //위도, 경도 받아오는 객체생성
        lastLocation = gpsListener.getLocation(getApplicationContext()); //위도 경도 받아오는 함수
    }

    //센서값 받아오는 함수
    private void startSensorService()
    {
        SensorItem sensorItem = new SensorItem(); //센서값 받는 객체 생성

        //센서
        DirectionListener directionListener = new DirectionListener(getApplicationContext(), txtDirectionValue);
        directionListener.onListenerRegister();
        String azimuth = sensorItem.getAzimuth();
        String pitch= sensorItem.getPitch();
        String roll = sensorItem.getRoll();
    }

    @Override
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
            Intent it = new Intent(getApplication(), tst.class);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
