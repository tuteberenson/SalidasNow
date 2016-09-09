package com.example.a41396969.salidasnow;

import android.app.Application;
import android.content.Context;

/**
 * Created by berenson on 05/09/2016.
 */

public class SalidasNow extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SalidasNow.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SalidasNow.context;
    }}
