package com.example.androiddream.buslabs;

/**
 * Created by Ksou on 7/12/15.
 */
public class ValueCheck {

    public  boolean ValueDiff  ( Double newLat, Double newLon , Double oldLat , Double oldLon, Double Variance ) {



        Double varLat = Math.abs( Math.abs(newLat) - Math.abs(oldLat) );

        Double varLon = Math.abs( Math.abs(newLon) - Math.abs(oldLon) ) ;

        if( varLon > Variance || varLat > Variance){

            // check for lat

        //    if( varLat < Variance){
                return  true ;

         //   }


        }

// doesn't work !
        return false
                ;
    }

// Convert

    public  double convertKmToMi(double kilometers) {
        // Assume there are 0.621 miles in a kilometer.
        double miles = kilometers * 0.621;
        return miles;
    }

}
