package com.example.androiddream.buslabs;

import java.util.List;

/**
 * Created by androiddream on 6/2/15.
 */
public interface DataCallback {

    public  void ParsedJSON( List<BusCurrent> buses) ;

    public  void SavedBuses( List<BusRecord> buses)  ;

    public  void GetStops ( List< BusStop> stops) ;
    public  void HttpStatus ( String status ) ;
    //public  void ToastIt ( String msg) ;
}
