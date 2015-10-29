package com.example.lee.arch;

/**
 * Created by lee on 2015-10-08.
 */
public class SensorItem {

    private String azimuth;
    private String pitch;
    private String roll;

    public String getAzimuth() {
        return azimuth;
    }

    public String getPitch() {
        return pitch;
    }

    public String getRoll() {
        return roll;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }
}
