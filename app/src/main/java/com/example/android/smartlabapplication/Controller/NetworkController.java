package com.example.android.smartlabapplication.Controller;

import android.content.Context;
import android.net.ConnectivityManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.smartlabapplication.ObserverDesignPattern.Subject;
import com.example.android.smartlabapplication.ObserverDesignPattern.UIObserver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 3/8/2019.
 */

public class NetworkController implements Subject {


    private Context context;
    private static NetworkController instance;
    private ArrayList<UIObserver> mObservers;
    private RequestQueue queue ;
    private String responseString;
    private NetworkController()
    {
        mObservers = new ArrayList<>();
    }
    public static NetworkController getInstance()
    {
        if(instance==null)
            instance = new NetworkController();
        return  instance;
    }
    public boolean networkAvailable(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

    public String getResponseString()
    {
        return responseString;
    }

    public void sendRequestToFiware(String url, JSONObject js, int type) {
JsonObjectRequest jsonObjReq;
if(type==1) {
    jsonObjReq = new JsonObjectRequest(
            Request.Method.PATCH, url, js,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    responseString = "command executed successfully";
                    notifyObservers();

                }
            }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

    responseString = "failed to execute command";
    notifyObservers();
        }
    }) {

        /**
         * Passing some request headers
         */
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("fiware-service", "openiot");
            headers.put("fiware-servicepath", "/");
            return headers;
        }
    };
}
else
{
    jsonObjReq = new JsonObjectRequest(
            Request.Method.GET, url, js,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                         responseString = response.getJSONObject("count").get("value").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        responseString = "oouldn't get reading";
                    }
                    notifyObservers();

                }
            }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {

        responseString = "failed to get sensor data";
        notifyObservers();
        }
    }) {

        /**
         * Passing some request headers
         */
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("fiware-service", "openiot");
            headers.put("fiware-servicepath", "/");
            return headers;
        }
    };

// Add the request to the RequestQueue.
}
// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void registerObserver(UIObserver uiObserver) {
        if(!mObservers.contains(uiObserver)) {
            mObservers.add(uiObserver);
        }

    }

    @Override
    public void removeObserver(UIObserver uiObserver) {
        if(mObservers.contains(uiObserver)) {
            mObservers.remove(uiObserver);
        }

    }

    @Override
    public void notifyObservers() {
        for (UIObserver observer: mObservers) {
            observer.onResponse();
        }

    }
}
