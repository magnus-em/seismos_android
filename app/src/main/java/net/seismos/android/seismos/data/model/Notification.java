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


    public Notification(String channel, Object object){
        thisObject = object;

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

            //friends and family
            NotificationChannel friendsnfamily = new NotificationChannel("Friends & Family Earthquake Warnings", "Friends & Family Earthquake Warnings", NotificationManager.IMPORTANCE_HIGH);
            friendsnfamily.enableLights(true);
            friendsnfamily.enableVibration(true);
            GlobalApplicationState.getContext().getSystemService(NotificationManager.class).createNotificationChannel(friendsnfamily);
        }

    }

    //this function decides details of the notification, such as image, title, message, and priority. based on channel and object type
    private NotificationCompat.Builder getNotification() {
        PendingIntent pendingIntent;
        String title;
        String message;
        int image;
        int priority;

        image = R.drawable.earthquake_safety; //decide image below based on object type and channel

        if(thisObject instanceof Earthquake){
            priority = NotificationCompat.PRIORITY_HIGH;

            if(thisChannel.equals("USGS Earthquake Warnings")){
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

                title = "USGS reports " + ((Earthquake) thisObject).getMagnitude() + " magnitude Earthquake.";
                message = "In " + ((Earthquake) thisObject).getLocation();


                //TODO separate this between friends and family and personal shake alert warnings
            }else{
                //TODO consider changing title based on severity of magnitude
                title = "ShakeAlert reports Earthquake near you! Magnitude " + ((Earthquake) thisObject).getMagnitude() + ".";
                message = "Get to cover!";
                //TODO go to warning page with safety instructions. the following is a placeholder
                Intent startActivityIntent = new Intent(GlobalApplicationState.getContext(), EqDetailsActivity.class);
                pendingIntent = PendingIntent.getActivity(GlobalApplicationState.getContext(), 0,
                        startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }

        }else{
            priority = NotificationCompat.PRIORITY_LOW;

            //TODO create intent based on what object it is (store offer, sei rewards, etc)
            //the following is a placeholder title and intent to load
            title = "Placeholder title.";
            message = "This needs to be fixed.";
            //TODO go to warning page with safety instructions. the following is a placeholder
            Intent startActivityIntent = new Intent(GlobalApplicationState.getContext(), EqDetailsActivity.class);
            pendingIntent = PendingIntent.getActivity(GlobalApplicationState.getContext(), 0,
                    startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        final NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(GlobalApplicationState.getContext(), thisChannel);

        notificationBuilder
                .setSmallIcon(image)
                .setColor(ContextCompat.getColor(GlobalApplicationState.getContext(), R.color.eq74GradientStart))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setShowWhen(true)

                .setPriority(priority)

                .setContentTitle(title)
                .setContentText(message);
        return notificationBuilder;

    }

    private boolean willNotificationBeShown(){
        //TODO check device location if the channel is shakealert or seismos, to determine if the earthquake is near me or friends/family, and decide whether or not to show notification
//        if(thisChannel.equals("ShakeAlert Earthquake Warnings") || thisChannel.equals("Seismos Earthquake Warnings")){
//            if(!isNearMe && !isNearFriendsAndFamilry){
//                return false;
//            }
//        }
        return true;
    }

    public void broadcastNotification() {
        createNotificationChannels();




        NotificationManagerCompat notificationManager
                = NotificationManagerCompat.from(GlobalApplicationState.getContext());
        if(willNotificationBeShown()){
            notificationManager.notify(0, getNotification().build());

        }


    }
}
