package net.seismos.android.seismos.detection;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import net.seismos.android.seismos.ui.global.DashActivityNav;

public class DetectionService2 extends IntentService {

    public DetectionService2() {
        super("DetectionService2");
    }

    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }

    Intent notificationIntent = new Intent(this, DashActivityNav.class);
    PendingIntent pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, 0);

//    Notification notification =
//            new android.app.Notification.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
//                    .setContentTitle(getText(R.string.notification_title))
//                    .setContentText(getText(R.string.notification_message))
//                    .setSmallIcon(R.drawable.icon)
//                    .setContentIntent(pendingIntent)
//                    .setTicker(getText(R.string.ticker_text))
//                    .build();
//
//    startForeground(ONGOING_NOTIFICATION_ID, notification);


}
