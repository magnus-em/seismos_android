package net.seismos.android.seismos;

import android.util.Log;

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

public class RecentEqsQuery {

    private static final String TAG = "RecentEqQuery";
    private static final String urlSpec = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                                    ": with " + urlSpec);

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

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String getUrlString() throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<RecentEq> fetchEqs() {
        List<RecentEq> recentEqs = new ArrayList<>();
        try {
            String result = getUrlString(urlSpec);
            Log.i(TAG, "fetched JSON: "  + result);
            JSONObject jsonBody = new JSONObject(result);
            parseEqs(recentEqs, jsonBody);
            Log.i(TAG, "Earthquake 0 title: " + recentEqs.get(0).getTitle());
            Log.i(TAG, "Earthquake 0 mag: " + recentEqs.get(0).getMag());
            Log.i(TAG, "Earthquake 0 time " + recentEqs.get(0).getTime());
            Log.i(TAG, "Earthquake 0 stats: " + recentEqs.get(0).getLat() + " "
                                + recentEqs.get(0).getLong() + " "
                                + recentEqs.get(0).getDepth());

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items" + ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON");
        }
        return recentEqs;

    }


    private void parseEqs( List<RecentEq> eqs, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray features = jsonBody.getJSONArray("features");

        int length = features.length();
        Log.i(TAG, "length " + length);

        for (int i = 0; i < length; i++) {
           JSONObject jsonObject = features.getJSONObject(i);
           JSONObject eqJsonObject = jsonObject.getJSONObject("properties");
           RecentEq eq = new RecentEq();

           eq.setMag(eqJsonObject.getString("mag"));
           eq.setTitle(eqJsonObject.getString("title"));
           eq.setPlace(eqJsonObject.getString("place"));
           eq.setTime(eqJsonObject.getString("time"));

           eqJsonObject = jsonObject.getJSONObject("geometry");
           JSONArray jsonArray = eqJsonObject.getJSONArray("coordinates");
           eq.setLong((float)jsonArray.getDouble(0));
           eq.setLat((float)jsonArray.getDouble(1));
           eq.setDepth((float)jsonArray.getDouble(2));

           eqs.add(eq);
        }


    }


}
