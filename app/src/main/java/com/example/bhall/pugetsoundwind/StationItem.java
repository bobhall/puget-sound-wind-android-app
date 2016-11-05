package com.example.bhall.pugetsoundwind;

/**
 * Created by bhall on 8/13/16.
 */
public class StationItem {
    String name;
    String weather;
    String timestamp;

    public enum StationType {FERRY, CGR, NOAA, UNKNOWN};

    StationType station_type;

    public StationItem(String name, String weather, String timestamp, StationType stationtype) {
        this.name = name;
        this.weather = weather;
        this.timestamp = timestamp;
        this.station_type = stationtype;
    }
}
