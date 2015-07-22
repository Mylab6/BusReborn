package com.example.androiddream.buslabs;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by Ksou on 7/11/15.
 */
public class UberCall {
    public  void GetUber ( Context that ) {

        startNewActivity(that,"com.ubercab" );

        /*PackageManager pm =  that.getPackageManager();
        try
        {
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            // Do something awesome - the app is installed! Launch App.
        }
        catch (PackageManager.NameNotFoundException e)
        {
            // No Uber app! Open Mobile Website.
        } */

    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            // We found the activity now start the activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

}
