package net.seismos.android.seismos.data.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.global.GlobalApplicationState;
import net.seismos.android.seismos.ui.map.EqDetailsActivity;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Notification {
    private String thisChannel;
    //thisObject is used to create the intent that is shown when notification is clicked
    private Object thisObject;
    private String thisMessage;
    private String thisTitle;


    public Notification(String channel, Object object, String title, String message){
        thisObject = object;
        thisMessage = message;
        thisTitle = title;
        thisChannel = channel;
    }


    // it's safe to call this method repeatedly because creating an existing notification channel performs no operation
    //creates all notification channels on app start
    public static void createNotificationChannels() {
        // use the support lib notifications because they do all the conditional checking for
        // older devices that don't support newer features
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //uncategorized channel
            NotificationChannel uncategorized = new NotificationChannel("Uncategorized", "Uncategorized", NotificationManager.IMPORTANCE_HIGH);
            uncategorized.enableLights(true);
            uncategorized.enableVibration(true);
            GlobalApplicationState.getContext().getSystemService(NotificationManager.class).createNotificationChannel(uncategorized);

            //usgs channel
            NotificationChannel usgs = new NotificationChannel("USGS Earthquake Warnings", "USGS Earthquake Warnings", NotificationManager.IMPORTANCE_HIGH);
            usgs.enableLights(true);
            usgs.enableVibration(true);
            GlobalApplicationState.getContext().getSystemService(NotificationManager.class).createNotificationChannel(usgs);

            //shakealert channel
            NotificationChannel shakealert = new NotificationChannel("ShakeAlert Earthquake Warnings", "ShakeAlert Earthquake Warnings", NotificationManager.IMPORTANCE_HIGH);
            shakealert.enableLights(true);
            shakealert.enableVibration(true);
            GlobalApplicationState.getContext().getSystemService(NotificationManager.class).createNotificationChannel(shakealert);

            //seismos
            NotificationChannel seismos = new NotificationChannel("Seismos Earthquake Warnings", "Seismos Earthquake Warnings", NotificationManager.IMPORTANCE_HIGH);
            seismos.enableLights(true);
            seismos.enableVibration(true);
            GlobalApplicationState.getContext().getSystemService(NotificationManager.class).createNotificationChannel(seismos);
        }

    }

    public void broadcastNotification() {
        createNotificationChannels();
        // TODO: change this implementation so that you just pass the ID of the EQ with the intent
        // and then then the EQ class retrieves the earthquake from the DB
        PendingIntent pendingIntent;
        if(thisObject instanceof Earthquake){
            Earthquake earthquake = (Earthquake)thisObject;
            Intent startActivityIntent = new Intent(GlobalApplicationState.getContext(), EqDetailsActivity.class);
            startActivityIntent.putExtra("title", earthquake.getTitle());
            startActivityIntent.putExtra("mag", Double.toString(earthquake.getMagnitude()));
            startActivityIntent.putExtra("lat", earthquake.getLatitude());
            startActivityIntent.putExtra("long", earthquake.getLongitude());
            startActivityIntent.putExtra("place", earthquake.getPlace());
            startActivityIntent.putExtra("date", Long.toString(earthquake.getTime()));
            startActivityIntent.putExtra("felt", Integer.toString(earthquake.getFelt()));
            startActivityIntent.putExtra("tsunami", Integer.toString(earthquake.getTsunami()));
            startActivityIntent.putExtra("alert", earthquake.getAlert());
            startActivityIntent.putExtra("types", earthquake.getTypes());
            startActivityIntent.putExtra("cdi", Double.toString(earthquake.getCdi()));


            pendingIntent = PendingIntent.getActivity(GlobalApplicationState.getContext(), 0,
                    startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }else{

            //TODO these comments
            //other types of notification objects (that will lead to a different intent)
            //store offers? reminders? congrats on progress?
            //the following 2 lines initialize a placeholder intent. will be replaced with whatever is needed
            Intent startActivityIntent = new Intent(GlobalApplicationState.getContext(), EqDetailsActivity.class);
            pendingIntent = PendingIntent.getActivity(GlobalApplicationState.getContext(), 0,
                    startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        final NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(GlobalApplicationState.getContext(), thisChannel);

        int image;
        int priority;
        //TODO separate usgs/shakealert/seismos channels to use a different icon. should probably use same priority and importance though
        //TODO add support for other notification channels, such as store offers, friends and family warnings, sei earnings. must also add those channels to the create function below
        if(thisChannel.equals("USGS Earthquake Warnings") || thisChannel.equals("ShakeAlert Earthquake Warnings") || thisChannel.equals("Seismos Earthquake Warnings")){
            priority = NotificationCompat.PRIORITY_HIGH;
            image = R.drawable.aftertheearthquake_icon; //TODO need to pick a different icon
        }else{
            //this is a place holder in case the notification channel sent from db is malformed
            priority = NotificationCompat.PRIORITY_LOW;
            image = R.drawable.aftertheearthquake_icon;
        }
        Log.d(TAG, "notification channel is: " + thisChannel);

        notificationBuilder
                .setSmallIcon(image)
                .setColor(ContextCompat.getColor(GlobalApplicationState.getContext(), R.color.eq74GradientStart))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setShowWhen(true)

                .setPriority(priority)

                .setContentTitle(thisTitle)
                .setContentText(thisMessage);

        NotificationManagerCompat notificationManager
                = NotificationManagerCompat.from(GlobalApplicationState.getContext());

        notificationManager.notify(0, notificationBuilder.build());

        Log.d(TAG, "THERE WAZ A NOTIFICATION: " + thisTitle);

    }
}
