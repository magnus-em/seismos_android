package net.seismos.android.seismos.data.remote.usgs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.local.DayUpdateDB;
import net.seismos.android.seismos.data.local.DayUpdateDBAccessor;
import net.seismos.android.seismos.data.local.EarthquakeDatabaseAccessor;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.global.Preferences;
import net.seismos.android.seismos.ui.map.EqDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateUsgsJsonWorker extends Worker {

    private static final String TAG = "UpdateUsgsJsonWorker";
    private static final String NOTIFICATION_CHANNEL = "earthquake";

    public static final int NOTIFICATION_ID = 1;


    public UpdateUsgsJsonWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        ArrayList<Earthquake> newEqs;
       List<Earthquake> earthquakes;

        try {
            // for purposes of quick loading when first opening the app
            newEqs = fetchEqs(getApplicationContext().getString(R.string.earthquake_json_feed_sig_month));

            earthquakes = DayUpdateDBAccessor
                    .getInstance(getApplicationContext())
                    .earthquakeDAO()
                    .loadAllEarthquakesBlocking();


            Earthquake largestNewEarthquake = null;


            for (Earthquake newEq : newEqs) {
                if (earthquakes.contains(newEq)) {
                    continue;
                }
                if (largestNewEarthquake == null ||
                        newEq.getMagnitude() >
                                largestNewEarthquake.getMagnitude()) {
                    largestNewEarthquake = newEq;
                }
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int minMag = prefs.getInt(Preferences.PREF_EQ_NOTIF_MIN, 4);

            if (largestNewEarthquake != null &&
                    largestNewEarthquake.getMagnitude() >= minMag) {
                // Trigger a notification

                broadcastNotification(largestNewEarthquake);

                Log.d(TAG, "THERE WAZ AN EQ: " + largestNewEarthquake.getTitle());
            }





            DayUpdateDBAccessor.getInstance(getApplicationContext())
                    .earthquakeDAO()
                    .insertEarthquakes(newEqs);
            Log.d(TAG, "inserted day update earthquakes count: " + newEqs.size());

            Log.d(TAG, "MINMAG: " + minMag);



            return Result.success();


        } catch (IOException ioe) {
            Log.e(TAG, "IO Exception" + ioe);
            return Result.retry();
        } catch (JSONException je) {
            Log.e(TAG, "JSON Exception" + je);
            return Result.failure();
        }
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception e) {
            Log.d(TAG, "Exception in geting URL bytes from usgs: " +  e.toString());
        } finally {
            connection.disconnect();
        }
        return null;
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    private void parseEqs(List<Earthquake> eqs, String jsonString)
            throws IOException, JSONException {
        ArrayList<Earthquake> earthquakes = (ArrayList<Earthquake>)eqs;
        JSONObject jsonBody = new JSONObject(jsonString);

        // disregard the metadata and bbox
        JSONArray features = jsonBody.getJSONArray("features");
        int eqCount = features.length();
        Log.i(TAG, "Eq count: " + eqCount);

        for (int i = 0; i < eqCount; i++) {
            JSONObject eqObject = features.getJSONObject(i);
            JSONObject properties = eqObject.getJSONObject("properties");
            Earthquake eq = new Earthquake();

            eq.setMag(properties.isNull("mag") ? (double)0 : properties.getDouble("mag"));
            eq.setTitle(properties.isNull("title") ? "" : properties.getString("title"));
            eq.setPlace(properties.isNull("place") ? "" : properties.getString("place"));
            eq.setTime(properties.isNull("time") ? (long)0 : properties.getLong("time"));
            eq.setUpdated(properties.isNull("updated") ? (long)0 : properties.getLong("updated"));
            eq.setTz(properties.isNull("tz") ? 0 : properties.getInt("tz"));
            eq.setUrl(properties.isNull("url") ? "" : properties.getString("url") ) ;
            eq.setDetail(properties.isNull("detail") ? "" : properties.getString("detail"));
            eq.setFelt(properties.isNull("felt") ? 0 : properties.getInt("felt"));
            eq.setCdi(properties.isNull("cdi") ? (double)0 : properties.getDouble("cdi"));
            eq.setMmi(properties.isNull("mmi") ? (double)0 : properties.getDouble("mmi"));
            eq.setAlert(properties.isNull("alert") ? "" : properties.getString("alert"));
            eq.setStatus(properties.isNull("status") ? "" : properties.getString("status"));
            eq.setTsunami(properties.isNull("tsunami") ? 0 : properties.getInt("tsunami"));
            eq.setSig(properties.isNull("sig") ? 0 : properties.getInt("sig"));
            eq.setNet(properties.isNull("net") ? "" : properties.getString("net"));
            eq.setCode(properties.isNull("code") ? "" : properties.getString("code"));
            eq.setIds(properties.isNull("ids") ? "" : properties.getString("ids"));
            eq.setSources(properties.isNull("sources") ? "" : properties.getString("sources"));
            eq.setTypes(properties.isNull("types") ? "" : properties.getString("types"));
            eq.setNst(properties.isNull("nst") ? 0 : properties.getInt("nst"));
            eq.setDmin(properties.isNull("dmin") ? (double)0 : properties.getDouble("dmin"));
            eq.setRms(properties.isNull("rms") ? (double)0 : properties.getDouble("rms"));
            eq.setGap(properties.isNull("gap") ? (double)0 : properties.getDouble("gap"));
            eq.setMagType(properties.isNull("magType") ? "" : properties.getString("magType"));
            eq.setType(properties.isNull("type") ? "" : properties.getString("type"));

            JSONObject geometry = eqObject.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            eq.setLongitude(coordinates.getDouble(0));
            eq.setLatitude(coordinates.getDouble(1));
            eq.setDepth(coordinates.getDouble(2));
            eq.setId(eqObject.getString("id"));
            earthquakes.add(eq);
        }
    }

    private ArrayList<Earthquake> fetchEqs(String urlSpec) throws IOException, JSONException {
        ArrayList<Earthquake> recentEqs = new ArrayList<>();
        String result = getUrlString(urlSpec);
        Log.i(TAG, "fetched JSON: " + result);
        parseEqs(recentEqs, result);
        return recentEqs;
    }


    // it's safe to call this method repeatedly because creating an existing notification channel
    // performs no operation

    private void createNotificationChannel() {

        // use the support lib notifications because they do all the conditional checking for
        // older devices that don't support newer features

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getResources().getString(R.string.earthquake_channel_name);

            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL,
                    name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);

            // Register the notification channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager =
                    getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void broadcastNotification(Earthquake earthquake) {
        createNotificationChannel();

        Intent startActivityIntent = new Intent(getApplicationContext(), EqDetailsActivity.class);


        // TODO: change this implementation so that you just pass the ID of the EQ with the intent
        // and then then the EQ class retrieves the earthquake from the DB

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


        PendingIntent launchIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder earthquakeNotificationBuilder
                = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL);

        earthquakeNotificationBuilder
                .setSmallIcon(R.drawable.nav_icon_seismos)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.eq74GradientStart))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(launchIntent)
                .setAutoCancel(true)
                .setShowWhen(true)

                .setPriority(NotificationCompat.PRIORITY_HIGH)

                .setContentTitle("Recent magnitude " + earthquake.getMagnitude() + " earthquake")
                .setContentText(earthquake.getPlace());



//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(earthquake.getDetails()));

        NotificationManagerCompat notificationManager
                = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(NOTIFICATION_ID, earthquakeNotificationBuilder.build());


    }


}
