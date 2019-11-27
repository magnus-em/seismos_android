package net.seismos.android.seismos.data.remote;

import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.seismos.android.seismos.data.model.Earthquake;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase
    //this describes that onMessageReceived is only triggered when app is in background IF notification is only data
    //also describes how to send http post request to server

    //http request is POST and sent to https://fcm.googleapis.com/fcm/send, looks like this:
//    {
//        "to": "/topics/warnings",
//            "data":{
//        "channel": "ShakeAlert Earthquake Warnings",
//                "title": "Here is a test!",
//                "body": "Testing from custom HTTP Request"
//    }
//    }



    private static final String TAG = "FCM Service";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        Log.d(TAG, "From: " + remoteMessage.getFrom());


            //public Notification(Context context, int channel, int importance, int priority, Object object, String title, String message, int image){
        Map<String, String> data = remoteMessage.getData();

        //TODO get earthquake details from notification payload, create an earthquake object and pass it in instead of new object
        net.seismos.android.seismos.data.model.Notification a = new net.seismos.android.seismos.data.model.Notification(data.get("channel"), new Earthquake());
    a.broadcastNotification();
    }

}
