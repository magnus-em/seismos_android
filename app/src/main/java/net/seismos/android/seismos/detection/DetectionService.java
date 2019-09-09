package net.seismos.android.seismos.detection;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.global.DashActivity;

public class DetectionService extends Service {
    public static final String CHANNEL_ID = "ForegroundDetectionService";

    private Looper serviceLooper;
    private MyHandler serviceHandler;
    private FirebaseFirestore db;
    private boolean killFlag = false;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManager;


    // Handler that receives messages from the thread
    private final class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }


        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.


            final DocumentReference docRef =  db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

            while (!killFlag) {

                db.runTransaction(transaction -> {
                    DocumentSnapshot snapshot = transaction.get(docRef);
                    int balance = snapshot.getLong("earnedToday").intValue();
                    balance = balance + 20;
                    transaction.update(docRef, "earnedToday", balance);
                    return balance;
                }).addOnSuccessListener(integer -> {

                    Log.d("DETECTIONSERVICE", "Integer: " + integer);

                    notificationManager.notify(1,
                            builder.setContentText("You have earned " + integer + " sei").build());
                }).addOnFailureListener(e -> Log.d("DETECTIONSERVICE", "FAILURE: " + e.getMessage()));

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // Restore interrupt status.
                    Thread.currentThread().interrupt();
                }
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job

            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new MyHandler(serviceLooper);
        db = FirebaseFirestore.getInstance();

         notificationManager
                = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("input");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, DashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Actively detecting")
                .setContentText(input)
                .setSmallIcon(R.drawable.nav_icon_seismos)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();


        startForeground(1, notification);


        // this is where you now do heavy work on a background thread

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);


        // and then at some point, stopSelf()

        return START_NOT_STICKY;

    }



    @Override
    public void onDestroy() {
        Log.d("DETECTIONSERVICE", "SERVICE STOPPED, ONDESTROY");
        super.onDestroy();
        killFlag = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground service channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

}
