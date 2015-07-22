package com.example.androiddream.buslabs;

import com.orm.SugarRecord;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by keith on 5/30/15.
 */
public class BusStop  extends SugarRecord<BusStop> {

    String block_id ;

    String route ;
    String routeArray ;
    Date  requestedAt  ;


    String stopId ;

    Integer id ;

    String time ;


    @Override
    public String toString() {
        double number = 0.9999999999999;
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        return "StopID: " +  stopId + " serves  " + routeArray  + " Last Requested at " + requestedAt ;
    }

}
