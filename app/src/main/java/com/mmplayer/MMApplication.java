package com.mmplayer;

import android.app.Application;

import com.mmplayer.crash.CrashHandler;

/**
 * Created by Elone on 16/4/20.
 */
public class MMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
