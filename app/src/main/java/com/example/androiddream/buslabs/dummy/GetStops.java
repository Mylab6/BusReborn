package com.example.androiddream.buslabs.dummy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.androiddream.buslabs.BusCurrent;
import com.example.androiddream.buslabs.BusRecord;
import com.example.androiddream.buslabs.BusStop;
import com.example.androiddream.buslabs.DataCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Ksou on 7/5/15.
 */
public class GetStops {

    public void GetStops(final String StopID, Context applicationContext, final DataCallback
            dataCallback) {
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);

        // my heroku server

        // https://nameless-hamlet-8321.herokuapp.com/api/stopsNroutes/34.0616945/-118.3087526/.02
        String url = "http://api.metro.net/agencies/lametro/stops/" + StopID  + "/predictions/" ;

// Request a string response from the provided URL.
        JsonRequest BusGet = new JsonObjectRequest(Request.Method.GET, url,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseTop) {
                        JSONArray response = null;
                        try {
                            response = responseTop.getJSONArray("items");


                          //  dataCallback.



                        } catch (JSONException e) {


                            return;
                            //    e.printStackTrace();
                        }

                    }} , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;

        queue.add(BusGet);


    }

}
