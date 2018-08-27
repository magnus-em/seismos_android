package net.seismos.android.seismos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MapFragment extends SupportMapFragment {

    private static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);
    private static final LatLng HONGKONG = new LatLng(22.39, 114.1095);
    private static final LatLng NYC = new LatLng(40.7, -74);
    private static final LatLng LONDON = new LatLng(51.5, 0.12);
    private static final String urlSpec = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojson";


    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        new GetRecentEqsTask().execute();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMapStyle(MapStyleOptions
                        .loadRawResourceStyle(getContext(), R.raw.style_json));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE, 4));

            }
        });
    }

    public void updateLocation(String location) {

        switch (location) {
            case "hk":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HONGKONG, 5));
                break;
            case "seattle":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE, 5));
                break;
            case "nyc":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NYC, 5));
                break;
            case "london":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LONDON, 5));
                break;

        }

    }

    private class GetRecentEqsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            new RecentEqsQuery().fetchEqs();
            return null;
        }


    }


}
