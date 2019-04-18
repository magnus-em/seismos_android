package net.seismos.android.seismos.data.remote.usgs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.local.EarthquakeDatabaseAccessor;
import net.seismos.android.seismos.data.model.Earthquake;

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

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UsgsJsonWorker extends Worker {
    private static final String TAG = "UsgsJsonWorker";

    public UsgsJsonWorker(@NonNull Context context, WorkerParameters params) {
        super(context, params);

    }

    @Override
    public Result doWork() {
        ArrayList<Earthquake> earthquakes;

        try {
            String quakeFeed = getApplicationContext().getString(R.string.earthquake_json_feed);
            earthquakes = fetchEqs(quakeFeed);

            EarthquakeDatabaseAccessor.getInstance(getApplicationContext())
                    .earthquakeDAO()
                    .insertEarthquakes(earthquakes);
            Log.d(TAG, "successfully inserted " + earthquakes.size() + " earthquakes");
            Log.i(TAG, "Earthquake 0 title: " + earthquakes.get(0).getTitle());
            Log.i(TAG, "eq 0 id: " + earthquakes.get(0).getId());
            Log.i(TAG, "eq 0 felt count: " + earthquakes.get(0).getFelt());

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
        } finally {
            connection.disconnect();
        }
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

    public ArrayList<Earthquake> fetchEqs(String urlSpec) throws IOException, JSONException {
        ArrayList<Earthquake> recentEqs = new ArrayList<>();

        String result = getUrlString(urlSpec);
        Log.i(TAG, "fetched JSON: " + result);
        parseEqs(recentEqs, result);

        return recentEqs;
    }
}
