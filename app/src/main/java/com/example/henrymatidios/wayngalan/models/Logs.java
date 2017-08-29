package com.example.henrymatidios.wayngalan.models;

/**
 * Created by Henry Matidios on 26/08/2017.
 */

public class Logs {
    private String key;
    public LogsInfo values;

    public Logs(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
