package com.example.androiddream.buslabs;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import org.json.JSONArray;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by keith on 5/30/15.
 */
public class BusCurrent  extends SugarRecord<BusCurrent> {


   // Integer records ;
    // an array list of bus times, we never want to save this, as is the DB is going to be massive
    @Ignore
   JSONArray stop_times ;
    String block_id ;

    String route ;

    Date  requestedAt  ;

  String lat ;
    String lon ; 
    String stopId ;

    Integer id ;

    Double seconds = 0.0  ;


    @Override
    public String toString() {
        //double number = 0.9999999999999;
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        return  "Route: " +  route + "Arrives in " + numberFormat.format(seconds/60) + " minutes  id: " + id;
    }

}
