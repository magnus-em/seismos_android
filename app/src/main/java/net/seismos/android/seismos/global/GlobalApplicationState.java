package net.seismos.android.seismos.global;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import net.seismos.android.seismos.util.ResUtil;
import net.seismos.android.seismos.util.SplashScreenHelper;

public class GlobalApplicationState extends Application {


    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ResUtil.init(context);
        registerActivityLifecycleCallbacks(new SplashScreenHelper());
        Preferences.init(context);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}