package com.example.androiddream.buslabs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.louislam.android.InputListener;
import net.louislam.android.L;

import java.util.Timer;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SystemOfUnits;
import javax.measure.unit.Unit;


public class MainActivity extends ActionBarActivity implements  BusTimes.OnFragmentInteractionListener , ItemFragment.OnFragmentInteractionListener
, BusRecords.OnFragmentInteractionListener, StopLFragment.OnFragmentInteractionListener, OnMapReadyCallback  {

    private  TabHost mTabHost ;
    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//getActionBar().setIcon(R.drawable.ic_bus_white);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//        setupTab(new Button(this), "Tab1", BusTimes.class);
        //      setupTab(new Button(this), "Tab2", BusTimes.class);



        // Update values using data stored in the Bundle.
       // updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        //buildGoogleApiClient();

        TabHost th = (TabHost) findViewById(R.id.tabHost);

//        LocalActivityManager loc = new LocalActivityManager(getActionBar().addTab();) ;
        th.setup();


       /* TabHost.TabSpec ts = th.newTabSpec("Get");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Get");
        th.addTab(ts);*/

     // Map first !
        TabHost.TabSpec Map = th.newTabSpec("Map");
        Map.setContent(R.id.tab4);
        Map.setIndicator("Map");

        th.addTab(Map);


        TabHost.TabSpec records = th.newTabSpec("Record");
        records.setContent(R.id.tab2);
        records.setIndicator("Records");
        th.addTab(records);

        TabHost.TabSpec StopFind = th.newTabSpec("StopFind");
        StopFind.setContent(R.id.tab3);
        StopFind.setIndicator("GPSSearch");
        th.addTab(StopFind);


        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        layout.setAnchorPoint(0.3f);
   //     layout.
        layout.setCoveredFadeColor(Color.GRAY); //.setCoveredFadeColor(Color.rgb(255,165,0));  // .setCoveredFadeColor(Color.argb(1,255,165, 0));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        final GoogleMap mp = mapFragment.getMap();
        mp.setMyLocationEnabled(true);
      //  mp.getMyLocation();

        mp.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if(lat == 1){
                    // if we hacven't updated yet their no point in pulling this back
                    return;
                }

                LatLngBounds bounds =  mp.getProjection().getVisibleRegion().latLngBounds ;

                ValueCheck valueCheck = new ValueCheck() ;
           Double distanceInMetters =      SphericalUtil.computeDistanceBetween(  bounds.northeast, bounds.southwest ) ;

              Double Miles  = valueCheck.convertKmToMi(  distanceInMetters / 1000 ) ;
                if( Miles > 0.7){
                    Context context = getApplicationContext();
                    CharSequence text = "Zoom in for results" ;
                    int duration = Toast.LENGTH_SHORT;

                    if( (toastOld == null ) || !toastOld.getView().isShown() ) {
                        toastOld = Toast.makeText(context, text, duration);
                        toastOld.show();

                    }
                    return;

                }
                else  {

                    Radius = String.valueOf(Miles) ;
                }

               // UnitConverter toMiles ; //  KILOMETER.getConverterTo(MILE);

            //toMiles.
            Double newLat =  (cameraPosition.target.latitude) ;
                Double newLon= (cameraPosition.target.longitude) ;



               // if( )
            // this we need to check value and such
              //  ValueCheck valueCheck = new ValueCheck() ;
                // a mile is about 69 miles
                // lets do about half a mile
                if(  runOnce || valueCheck.ValueDiff(newLat, newLon,lat,lon,  (0.300/69)) ) {
                    // new becomes the old
                    lat = newLat ;
                    lon = newLon ;

                    StopLFragment fragment = (StopLFragment) getFragmentManager().findFragmentById(R.id.StopLFragment);
                    fragment.GetStops(lat, lon, Radius, mp);
                }

            //   Location loc = new Location () ;
             //   MapUpdate(   String.valueOf(cameraPosition.target.longitude ), String.valueOf(cameraPosition.target.latitude)  , mp ) ;
            }
        });

        // get first position
       SmartLocation.with(this).location()
                .oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
                MapUpdate(lat, lon, mp);
            }
        });

        SmartLocation.with(this).location()
                .continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {

                        // these can always happen

                        ValueCheck valueCheck = new ValueCheck();
                        // a mile is about 69 miles
                        // lets do about half a mile
                        Double newLat = location.getLatitude();
                        Double newLon = location.getLongitude();
                        if (runOnce || valueCheck.ValueDiff(newLat, newLon, lat, lon, (1.5 / 69))) {


                            // new becomes the old
                            lat = newLat;
                            lon = newLon;

                            MapUpdate(lat, lon, mp);
                            StopLFragment fragment = (StopLFragment) getFragmentManager().findFragmentById(R.id.StopLFragment);
                            fragment.GetStops(lat, lon, Radius, mp);
                        }
                        //mp.pa


                    }
                    //...
                });



    }

    public  boolean runOnce = true  ;
    public  void MapUpdate (Double Lat , Double Lon ,GoogleMap mp ){
        runOnce = false ;
        // lon = Lon ; //String.valueOf(location.getLongitude());
      //  lat =  Lat ;  // String.valueOf(location.getLatitude());
       // StopLFragment fragment = (StopLFragment) getFragmentManager().findFragmentById(R.id.StopLFragment);



        //    mp.
        //mp.addTileOverlay()
                        /*mp.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("Loc")); */
        mp.setMyLocationEnabled(true);

        LatLng Ln = new LatLng(Lat, Lon );


        CameraUpdateFactory.newLatLng(Ln);

        CameraPosition cp = new CameraPosition.Builder()
                .target(Ln)
                .zoom(17.3f)
                .tilt(30)
                .build();

        mp.animateCamera(CameraUpdateFactory.newCameraPosition(cp));

        //fragment.GetStops(lat, lon, Radius, mp);

        //mp.pa




    }

    Toast toastOld ;

String Radius = "0.3" ;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if ( id == R.id.gps){



      final      StopLFragment fragment = (StopLFragment) getFragmentManager().findFragmentById(R.id.StopLFragment);

         //   fragment.GPSRadius(".4");

            final Context One = this ;
            L.inputDialog(this, "Gps Radius ( in miles) Lat: " + lat + "Lon: " +lat, new InputListener() {
                @Override
                public void inputResult(String s) {

                    Radius = s ;

                    SmartLocation.with(One).location()
                            .oneFix()
                            .start(new OnLocationUpdatedListener() {


                                @Override
                                public void onLocationUpdated(Location location) {

                                    lon =  location.getLongitude() ;
                                    lat = location.getLatitude();
                                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                                            .findFragmentById(R.id.map);
                                    //mapFragment.getMapAsync(this);
                                    GoogleMap mp=  mapFragment.getMap() ;

                                    fragment.GetStops(lat, lon, Radius, mp);
                                }
                            });
                    //Radius = s ;

                }
            } ,Radius );
        }




        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_us) {

            //L.alert(getApplicationContext(),"");
            L.confirmDialog( this , "Delete Records ? " , new  DialogInterface.OnClickListener()
            {


                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final BusRecords fragment = (BusRecords) getFragmentManager().findFragmentById(R.id.BusRecordFragment);
                    fragment.Delete(

                            new ToastMsg() {
                        @Override
                        public void ToastI(String msg) {
                        // get the UI thread
                            // TODO , I think I can get away with this for now

                            Context context = getApplicationContext();
                            CharSequence text = msg ;
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            fragment.Update();


                        }
                    });
                }
            });


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public Timer oldTimer ;
    @Override
    public void onFragmentInteraction(final BusCurrent id) {

        final Context that = this ;
        L.confirmDialog(this, "Set timer for route: " + id.route + "  at stop: " + id.stopId + "?",

                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        NotifcationDreams(id.stopId, id.seconds, id.route);
                        // Call to set timer
                    }
                });
    }

    @Override
    public void UpdateRecords() {
        BusRecords fragment = (BusRecords) getFragmentManager().findFragmentById(R.id.BusRecordFragment);
        fragment.Update();
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void RefreshRecord(final BusRecord id) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");


                L.confirmDialog(MainActivity.this, "Get updated times for StopID:" + id.stopId, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BusTimes fragment = (BusTimes) getFragmentManager().findFragmentById(R.id.BusTimeFragment);
                                fragment.GetNew(id.stopId, "Stored");
                            }
                        });

            }
        });
            }

    @Override
    public void MapStopIdLook(final String id, final String title) {

       // BusTimes fragment = (BusTimes) getFragmentManager().findFragmentById(R.id.BusTimeFragment);
       // fragment.GetNew(id);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("UI thread", "I am the UI thread");
                BusTimes fragment = (BusTimes) getFragmentManager().findFragmentById(R.id.BusTimeFragment);
                fragment.GetNew(id, title);

                    // Skip the conformation

                /*
                L.confirmDialog(MainActivity.this, "Get updated times for StopID:" + id, new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BusTimes fragment = (BusTimes) getFragmentManager().findFragmentById(R.id.BusTimeFragment);
                                fragment.GetNew(id);
                            }
                        }); */

            }
        });


    }

    public  int notiId = 1 ;
  //  public boolean RunningNotif = false ;
    public void NotifcationDreams(String stopId, Double seconds, String route){

       // notiId ++ ;
        //RunningNotif = false ;
        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_bus_white)
                        .setContentTitle("Bus timer")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app

        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =// PendingIntent.getBroadcast(this.getApplicationContext(), 0, resultIntent, 0);
                stackBuilder.getPendingIntent(
                        0,

                        PendingIntent.FLAG_ONE_SHOT
                        //PendingIntent.FLAG_CANCEL_CURRENT
                );


    //  resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;


        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.cancelAll();
        mNotificationManager.notify(notiId, mBuilder.build());


        mBuilder.setDeleteIntent(resultPendingIntent);
     CountDown(mBuilder, seconds , mNotificationManager, route , notiId) ;
    }


//    android.location.Location mLastLocation ;
    Double lat = Double.valueOf(1);

    Double lon  = Double.valueOf(1);



    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


      ///  mapFragment.getMap().add
        //googleMap.addMarker(new MarkerOptions()
          //      .position(new LatLng(0, 0))
            //    .title("Marker"));

    }


    //  StopLFragment fragment = (StopLFragment) getFragmentManager().findFragmentById(R.id.StopLFragment);
    //  fragment.GetStops(lat.substring(0,12),lon.substring(0,12),"0.02");
    //

    // Acquire a reference to the system Location Manager
// Acquire a reference to the system Location Manager

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {


            if(hat !=null) {

                hat.interrupt() ;
                hat = null ;

            }
     //       .... code to handle cancel
        }

    }

Thread hat = null ;
    public  void CountDown(final NotificationCompat.Builder mBuilder, final Double seconds, final NotificationManager mNotificationManager, final String route,  final int notiId) {
       // mNotificationManager.

        if(hat !=null) {

   hat.interrupt() ;
hat = null ;

}


   hat =       new Thread() {
            boolean running = true ;
            @Override
            public  void  interrupt() {

                running = false ;

                mNotificationManager.cancelAll();
            }


           @Override
           public void run () {
               Double newSeconds = seconds;
               // RunningNotif = true ;

               while (newSeconds > 1 &&  running  ) {


                   // sleep for a second give or take
                   try {
                       Thread.sleep(1 * 1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   newSeconds--;

                   mBuilder.setProgress(seconds.intValue(), newSeconds.intValue(), false);
                   mBuilder.setContentText("Route:" + route + " arrives in " + newSeconds);
                   mNotificationManager.notify(notiId,
                           mBuilder.build());
               }
               //mNotificationManager.cancel(1);

               if (running) {
                   mBuilder.setContentText(" Bus here !");
                   mNotificationManager.notify(notiId,
                           mBuilder.build());
               }
               else {

                   mNotificationManager.cancelAll();
               }
           }
   };


        hat.setDaemon(false) ;




        hat.start();

        //Thread.interrupted();


                    }

}
