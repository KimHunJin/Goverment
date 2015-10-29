package com.sku.archbiz2015;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;
import com.hkm.slider.Tricks.ViewPagerEx;
import com.sku.archbiz2015.modules.NumZero;
import com.sku.archbiz2015.utils.DataProvider;
import com.sku.archbiz2015.utils.GpsInfo;

import java.util.HashMap;

public class SecondPageActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, GoogleMap.OnMapClickListener {

    private SliderLayout mDemoSlider;
    private boolean numbered = false;
    GoogleMap mapSecond;  //구글맵

    @SuppressLint("ResourceAsColor")
    private void setupSlider() {
        // remember setup first
        mDemoSlider.setPresetTransformer(TransformerL.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.red_pink_24, R.color.red_pink_26);
        final NumZero n = new NumZero(this);
        mDemoSlider.setNumLayout(n);
        mDemoSlider.presentation(SliderLayout.PresentationConfig.Numbers);
        mDemoSlider.setPresetTransformer("Accordion");

        //and data second. it is a must because you will except the data to be streamed into the pipline.
//        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        MapsInitializer.initialize(getApplicationContext());

        Intent it = getIntent();
        double latitude = it.getExtras().getDouble("Latitude");
        double longitude = it.getExtras().getDouble("Longitude");

        mapInit(latitude, longitude);

        setupSlider();

        ImageView imgPicture = (ImageView)findViewById(R.id.imgPicture);
        imgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "aaaa", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView imgMoreBuilding = (ImageView)findViewById(R.id.btnMoreBuilding);
        imgMoreBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(), GirdViewTest.class);
                startActivity(it);
            }
        });

        final ImageView imgShowPicture = (ImageView)findViewById(R.id.imgShowPicture);
        final ImageView imgShowMap = (ImageView)findViewById(R.id.imgShowMap);
        imgShowPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoSlider.setVisibility(View.VISIBLE);
                imgShowPicture.setVisibility(View.INVISIBLE);
                imgShowMap.setVisibility(View.VISIBLE);
            }
        });

        imgShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoSlider.setVisibility(View.INVISIBLE);
                imgShowPicture.setVisibility(View.VISIBLE);
                imgShowMap.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void mapInit(double latitude, double longitude) {
        final LatLng latLng = new LatLng(latitude,longitude);

        mapSecond = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapSecond)).getMap();
        mapSecond.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mapSecond.animateCamera(CameraUpdateFactory.zoomTo(17));

        mapSecond.getUiSettings().setScrollGesturesEnabled(false);

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        mapSecond.addMarker(options).showInfoWindow();
    }

    public void onMapClick(LatLng point) {
        Point screenPt = mapSecond.getProjection().toScreenLocation(point);

        LatLng latLng = mapSecond.getProjection().fromScreenLocation(screenPt);

        Log.e("mapSecond", point.latitude + "  " + point.longitude);
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
