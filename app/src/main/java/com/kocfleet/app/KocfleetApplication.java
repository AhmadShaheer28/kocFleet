package com.kocfleet.app;

import android.app.Application;
import android.content.Context;

public class KocfleetApplication extends Application {
    private static Context applicationContext;

    public static Context getAppContext() {
        return applicationContext;
    }

    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        //Lingver.init(this, "en");

        //Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));

//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
//        }
    }
}
