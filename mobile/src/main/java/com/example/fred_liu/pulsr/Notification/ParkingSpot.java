package com.example.fred_liu.pulsr.Notification;

import android.graphics.Bitmap;

/**
 * Created by fredliu on 12/3/17.
 */

public class ParkingSpot {
    private Bitmap image;
    private String date;
    private String time;
    private String location;
    private String heartRate;
    private String steps;
    private String cals;
    private String playing;

    public ParkingSpot(Bitmap image, String date, String time, String location, String heartRate, String steps, String cals, String playing) {
        super();
        this.image = image;
        this.date = date;
        this.time = time;
        this.location = location;
        this.heartRate = heartRate;
        this.steps = steps;
        this.cals = cals;
        this.playing = playing;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getCals() {
        return cals;
    }

    public void setCals(String cals) {
        this.cals = cals;
    }

    public String getPlaying() {
        return playing;
    }

    public void setPlaying(String playing) {
        this.playing = playing;
    }
}
