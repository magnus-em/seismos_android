package net.seismos.android.seismos.global;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.core.content.ContextCompat;

import net.seismos.android.seismos.detection.DetectionService;
import net.seismos.android.seismos.util.ResUtil;
import net.seismos.android.seismos.util.SplashScreenHelper;

public class GlobalApplicationState extends Application {


    private Context context;
    private boolean recording;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        ResUtil.init(context);
        registerActivityLifecycleCallbacks(new SplashScreenHelper());
        Preferences.init(context);
        recording = false;
         if (recording) {
             Intent serviceIntent = new Intent(this, DetectionService.class);
             serviceIntent.putExtra("input", "You've earned 0 sei today");
             ContextCompat.startForegroundService(this, serviceIntent);
         }

    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
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