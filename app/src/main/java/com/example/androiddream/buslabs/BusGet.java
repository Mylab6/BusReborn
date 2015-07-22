package com.example.androiddream.buslabs;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androiddream.buslabs.dummy.DummyContent;

import net.louislam.android.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by androiddream on 5/29/15.
 */
public class BusGet {

    private void SaveBuses(String LocationID) {


        Date date = new Date();

        date.setTime(System.currentTimeMillis());


    }

   /* public static class Bus {
        public String id;
        public String content;

        public Bus(String id, String content) {
            this.id = id;
            this.content = content;
        }*/






        public void GetBus(final String StopID, Context applicationContext, final DataCallback
                dataCallback) {
            final RequestQueue queue = Volley.newRequestQueue( applicationContext);
            String url = "http://api.metro.net/agencies/lametro/stops/" + StopID  + "/predictions/" ;

// Request a string response from the provided URL.
            JsonRequest BusGet = new JsonObjectRequest(Request.Method.GET, url,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject responseTop) {
                            JSONArray response = null;
                            try {
                                response = responseTop.getJSONArray("items");

                                ProcessJSON(response ,  StopID , new DataCallback()  {
                                    @Override

                                public void ParsedJSON(List<BusCurrent> buses) {

                                        dataCallback.ParsedJSON(buses) ;
                                       }

                                            @Override
                                            public void SavedBuses(List<BusRecord> buses) {

                                            }

                                            @Override
                                            public void GetStops(List<BusStop> stops) {

                                            }

                                            @Override
                                            public void HttpStatus(String status) {

                                            }
                                        }

                                    );


                            } catch (JSONException e) {

                                dataCallback.HttpStatus(e.getMessage());
                                return;
                                //    e.printStackTrace();
                            }

                        }} , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    dataCallback.HttpStatus(error.toString() +" :Metro's API is down :(");

                }
            }) ;

            queue.add(BusGet);


        }


     void ProcessJSON(JSONArray response ,  String stopID , DataCallback dataCallback)
     {

         List<String> alt = new ArrayList<String>() ;
         List<BusCurrent> Buses = new ArrayList<BusCurrent>() ;


         // Get the date once

        Date DateO =   Calendar.getInstance().getTime() ;
         for (int i = 0; i < response.length(); i++) {

             BusCurrent newBus = new BusCurrent() ;
                newBus.id = i ;

// Data will come back like this
                                /*

                                {"items": [{"block_id": "0205911", "run_id": "20_259_0",
                                "seconds": 1358.0, "is_departing": false, "route_id": "20", "minutes": 22.0},
                                 {"block_id": "0205611", "run_id": "20_259_0", "seconds": 3179.0, "is_departing": false,
                                 "route_id": "20", "minutes": 52.0},
                                  {"block_id": "0205711",
                                  "run_id": "20_259_0", "seconds": 4861.0,
                                  "is_departing": false, "route_id": "20", "minutes": 81.0}]}



                                */
             try {
                 newBus.requestedAt = DateO ;
                 JSONObject BusOb = (JSONObject) response.get(i);

                 newBus.block_id = String.valueOf(BusOb.getInt("block_id"));
                 newBus.stopId = stopID ;
                 newBus.route = BusOb.getString("route_id") ;
                 newBus.seconds = (Double) BusOb.get("seconds");
                // newBus.stop_times = BusOb.getJSONArray("stop_times") ;
                 newBus.save();
                 alt.add( newBus.route) ;
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             //  newBus.id = BusOb
             Buses.add(   newBus) ;
         }
// Save the record

         BusRecord busSaved = new BusRecord() ;

         busSaved.stopId = stopID ;
        // busSaved.route = Buses.get()
         // save all routes in an array
        // String Routes = "" ;

         for( String rr : alt ) {

             busSaved.routeArray =  busSaved.routeArray  +" ," + rr  ;
         }
   //    busSaved.routeArray = alt ;
       busSaved.requestedAt =   DateO ;

         busSaved.save();
         // Create record
         //ArrayAdapter<BusCurrent> Colors = new ArrayAdapter<BusCurrent>(that,
           //      android.R.layout.simple_list_item_1 , Buses ) ;

         dataCallback.ParsedJSON(Buses) ;
        // mAdapter.setAdapter(Colors) ;


     }



}

