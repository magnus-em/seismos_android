package net.seismos.android.seismos.global;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Notification;
import net.seismos.android.seismos.detection.DetectionService;
import net.seismos.android.seismos.util.ResUtil;
import net.seismos.android.seismos.util.SplashScreenHelper;

import static com.firebase.ui.auth.AuthUI.TAG;

public class GlobalApplicationState extends Application {


    private static Context context;
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


         //subscribes to warning topic in firebase
        FirebaseMessaging.getInstance().subscribeToTopic("warnings")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = getString(R.string.msg_subscribed);
//                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
//                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                })
        ;

    }

    public static Context getContext(){
        return context;
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