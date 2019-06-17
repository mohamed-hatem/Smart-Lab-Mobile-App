package com.example.android.smartlabapplication.Model;

/**
 * Created by DELL on 6/4/2019.
 */

public class SensorData {
    private String sensorId;
    private String sensorType;

    public SensorData(String id , String type)
    {
        this.sensorId = id;
        this.sensorType = type;
    }
    public String getUrl()
    {
        return "http://147.27.60.31:1026/v2/entities/urn:ngsi-ld:"+sensorId+"?type="+sensorType;
    }
}
