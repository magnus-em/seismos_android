package net.seismos.android.seismos.data.remote.usgs;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.local.EarthquakeDatabaseAccessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class EarthquakeListUpdateWorkerAtom extends Worker {
    private static String TAG = "EarthquakeListUpdateWorkerAtom";

    public EarthquakeListUpdateWorkerAtom(
            @NonNull Context context,
            WorkerParameters params) {
        super(context, params);
    }


    @Override
    public Result doWork() {
        ArrayList<Earthquake> earthquakes = new ArrayList<>(0);


        // Get the XML
        URL url;
        try {
            String quakeFeed = getApplicationContext().getString(R.string.earthquake_feed);
            url = new URL(quakeFeed);

            URLConnection connection;
            connection = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                // Parse the earthquake feed.
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();

                // Get a list of each earthquake entry.
                NodeList nl = docEle.getElementsByTagName("entry");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        // Check to see if our loading has been cancelled, in which
                        // case return what we have so far

                        Element entry = (Element) nl.item(i);
                        Element id = (Element) entry.getElementsByTagName("id").item(0);
                        Element title = (Element) entry.getElementsByTagName("title").item(0);
                        Element g =
                                (Element) entry.getElementsByTagName("georss:point").item(0);
                        Element when =
                                (Element) entry.getElementsByTagName("updated").item(0);
                        Element link =
                                (Element) entry.getElementsByTagName("link").item(0);

                        String idString = id.getFirstChild().getNodeValue();
                        String details = title.getFirstChild().getNodeValue();
                        String hostname = "http://earthquake.usgs.gov";
                        String linkString = hostname + link.getAttribute("href");
                        String point = g.getFirstChild().getNodeValue();
                        String dt = when.getFirstChild().getNodeValue();
                        SimpleDateFormat sdf =
                                new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
                        Date qdate = new GregorianCalendar(0, 0, 0).getTime();
                        try {
                            qdate = sdf.parse(dt);
                        } catch (ParseException e) {
                            Log.e(TAG, "Date parsing exception.", e);
                        }

                        String[] location = point.split(" ");
                        Location l = new Location("dummyGPS");
                        l.setLatitude(Double.parseDouble(location[0]));
                        l.setLongitude(Double.parseDouble(location[1]));

                        String magnitudeString = details.split(" ")[1];
                        int end = magnitudeString.length() - 1;
                        double magnitude = Double.parseDouble(magnitudeString) ;

                        if (details.contains("-"))
                            details = details.split("-")[1].trim();
                        else
                            details = "";




                    }
                }
            }
            httpConnection.disconnect();

            EarthquakeDatabaseAccessor.getInstance(getApplicationContext())
                    .earthquakeDAO()
                    .insertEarthquakes(earthquakes);
            Log.d(TAG, "successfully inserted " + earthquakes.size() + " earthquakes");
            Log.d(TAG, earthquakes.get(0).getId() + " " + earthquakes.get(0).getDetails() + " " + earthquakes.get(0).getDate());

            return  Result.success();
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException ", e);
            return Result.failure();
        } catch (IOException e) {
            Log.e(TAG, "IOException ", e);
            return Result.retry();
        } catch (ParserConfigurationException e) {
            Log.e(TAG, "Parser Configuration Exception", e);
            return Result.failure();
        } catch (SAXException e) {
            Log.e(TAG, "SAX Exception", e);
            return Result.failure();
        }
    }
}
