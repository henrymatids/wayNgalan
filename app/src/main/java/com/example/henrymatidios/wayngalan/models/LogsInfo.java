package com.example.henrymatidios.wayngalan.models;

/**
 * @author Henry Matidios
 * @since  28/08/2017
 */

public class LogsInfo {
    private String date;
    private String location;
    private String time;

    public LogsInfo(){}

    public LogsInfo(String date, String time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
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
}
