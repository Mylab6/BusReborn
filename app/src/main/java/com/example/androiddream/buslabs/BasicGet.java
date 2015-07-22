package com.example.androiddream.buslabs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.format.Time;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ksou on 7/5/15.
 */
public class BasicGet {


    //public  String Radius ;
    //   https://nameless-hamlet-8321.herokuapp.com/api/stopsNroutes/
///api/stopsNearby/:lat/:lon/:radius
    //  https://nameless-hamlet-8321.herokuapp.com/api/stopsNroutes/34.0616945/-118.3087526/.02
    /*public void GetStops(final String lat, final
    String lon , final  String Radius, Context applicationContext, final DataCallback
                                       dataCallback){

        List<BusRecord> Buses =  new ArrayList<BusRecord>() ;

        dataCallback.SavedBuses(Buses) ;
    }*/



    public void GetStops(final String lat, final
             String lon , final  String Radius, Context applicationContext, final DataCallback
            dataCallback) {
        final RequestQueue queue = Volley.newRequestQueue(applicationContext);

       // Calendar cal = Calendar.getInstance();

        String url = "https://lit-wave-3087.herokuapp.com/stops/SearchRaduis?lat=" + lat +"&lon=" + lon + "&radius=" + Radius ;

                //34.0616945/-118.3087526/.02"

// Request a string response from the provided URL.
        JsonRequest BusGet = new JsonArrayRequest(Request.Method.GET, url,

                new Response.Listener<JSONArray>() {
                   // @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(JSONArray responseTop) {

                        //    JSONArray response =  new JSONArray(responseTop) ;

                        ProcessJSON(responseTop, dataCallback);
                        // response

                    }} , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                    dataCallback.HttpStatus(  error.toString());
                error.printStackTrace();
            }
        }
//                JSONArray response =  new JSONArray(responseTop) ;
        //ProcessJSON(response, dataCallback);


        ) ;

        queue.add(BusGet);


    }




    void ProcessJSON(JSONArray response , DataCallback dataCallback)
    {

        List<String> alt = new ArrayList<String>() ;
        List<BusRecord> Buses = new ArrayList<BusRecord>() ;


        // Get the date once

        Date DateO =   Calendar.getInstance().getTime() ;
        for (int i = 0; i < response.length(); i++) {

            BusRecord newBus = new BusRecord() ;
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

                JSONObject BusOb =  response.getJSONObject(i) ;
                newBus.requestedAt = DateO ;
                //newBus.block_id =
                newBus.stopId = BusOb.getString("stop_id");

              //  JSONArray closer =   BusOb.getJSONArray("stop_times");    //new JSONArray(BusOb) ;

              //  JSONArray closer = busRoutes.getJSONArray("items");

                try {
                    newBus.route = BusOb.getString("stop_desc");  /// closer.getJSONObject(0).getString("stop_headsign");  //((JSONObject)busRoutes.get(0)).getString("display_name") ;
                } catch (Exception err) {

                newBus.route = "" ;
                }
               // newBus.seconds = (Double) BusOb.get("seconds");
            newBus.routeArray =   newBus.route + "  @" + BusOb.getString("stop_name") ;

               newBus.Lat =   BusOb.getString("stop_lat") ;
                newBus.Lon =   BusOb.getString("stop_lon") ;
                newBus.StopName = BusOb.getString("stop_name") ;
               // newBus.DisplayName = BusOb.getJSONObject("stopData").getString("stop_name") ;
                //  newBus.route = ((JSONObject)busRoutes.get(0)).getString("display_name") ;
               // newBus.save();
              //  alt.add( newBus.route) ;
                Buses.add(   newBus) ;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //  newBus.id = BusOb
            //Buses.add(   newBus) ;
        }
// Save the record

  //      //BusRecord busSaved = new BusRecord() ;
    //    busSaved.stopId =  newBus.stopId ;
        // busSaved.route = Buses.get()
        // save all routes in an array
        // String Routes = "" ;

       // for( String rr : alt ) {

        //    busSaved.routeArray =  busSaved.routeArray  +" ," + rr  ;
       // /}*/
        //    busSaved.routeArray = alt ;
      //  busSaved.requestedAt =   DateO ;

      //  busSaved.save();
        // Create record
        //ArrayAdapter<BusCurrent> Colors = new ArrayAdapter<BusCurrent>(that,
        //      android.R.layout.simple_list_item_1 , Buses ) ;

        dataCallback.SavedBuses(Buses); ;
        // mAdapter.setAdapter(Colors) ;


    }




}
