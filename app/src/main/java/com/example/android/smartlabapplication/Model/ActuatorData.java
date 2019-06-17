package com.example.android.smartlabapplication.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL on 3/19/2019.
 */

public class ActuatorData {

    private String actuatorId;
    private String command;
    private  String url;

    public ActuatorData()
    {
       url = "http://130.206.117.3:1026/v2/entities/urn:ngsi-ld:"; // URL to call

    }
    public String getUrl()
    {
        return url + actuatorId +"/attrs";
    }
    public JSONObject getApiObject()
    {
JSONObject js = new JSONObject();

        try {
            JSONObject jsonobjectOne = new JSONObject();

            jsonobjectOne.put("type", "command");
            jsonobjectOne.put("value", "");
            js.put(command, jsonobjectOne);


        } catch (JSONException e) {
            e.printStackTrace();
        }
return js;

    }
    public void setActuatorId(String actuatorId) {
        this.actuatorId = actuatorId;
    }


    public void setCommand(String command) {
        this.command = command;
    }

}
