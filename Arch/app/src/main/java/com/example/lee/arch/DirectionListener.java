package com.example.lee.arch;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lee on 2015-10-08.
 */
public class DirectionListener implements SensorEventListener{

    SensorManager sensorManager;
    Sensor sensor;
    Context context;
    SensorItem sensorItem;
    ArrayList<SensorItem> list;
    TextView txtDirectionValue;
    public DirectionListener(Context context, TextView txtDirectionValue)
    {
        this.context = context;
        this.txtDirectionValue = txtDirectionValue;
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
            sensorItem = new SensorItem();

            sensorItem.setAzimuth("" + event.values[0]);
            sensorItem.setPitch("" + event.values[1]);
            sensorItem.setRoll("" + event.values[2]);
//            list.add(sensorItem);
            txtDirectionValue.setText("Azimuth : " + event.values[0] + ", Pitch : " + event.values[1]
            + ", Roll : " + event.values[2]);
            Log.i("sensor", sensorItem.getAzimuth());
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
