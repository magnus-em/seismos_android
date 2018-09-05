package net.seismos.android.seismos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener
{

    private static final String TAG = "MapFragment";

    MapView mMapView;
    private GoogleMap mMap;
    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);
    private static final LatLng JAPAN = new LatLng(35.6895, 139.6917);
    private static final LatLng USA = new LatLng(34.0633, -117.6509);
    private static final LatLng NEPAL = new LatLng(27.6487, 84.4173);
    private static final LatLng CHILE = new LatLng(-32.4499, -71.2326);
    private static final LatLng TURKEY = new LatLng(36.9914, 35.3308);
    private static final LatLng AUSTRALIA = new LatLng(-9.4438, 147.1803);

    private static final String urlSpec = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.geojson";
    private List<RecentEq> mRecentEqs;
    private JSONObject mEqsJson;

    private TextView featureText;
    private TextView magText;
    private CardView mapCard;
    private boolean isDetailVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        featureText =  rootView.findViewById(R.id.place_text);
        magText = rootView.findViewById(R.id.magnitude_text);
        mapCard = rootView.findViewById(R.id.mapcard);
        mapCard.setVisibility(View.INVISIBLE);
        isDetailVisible = false;

        mMapView.onResume(); // needed to get the map to display immediately
        new GetRecentEqsTask().execute();
        new AddRecentEqsLayer().execute();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

            // For showing a move to my location button
//                googleMap.setMyLocationEnabled(true);
         mMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(getContext(), R.raw.map_style));

         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE, 4));
         mMap.getUiSettings().setMapToolbarEnabled(false);
         mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(LatLng click) {
        mapCard.setVisibility(View.INVISIBLE);
        mMap.setPadding(0,0,0,0);
        isDetailVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void updateLocation(String location) {

        switch (location) {
            case "japan":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(JAPAN, 4));
                break;
            case "usa":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(USA, 4));
                break;
            case "nepal":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(NEPAL, 4));
                break;
            case "chile":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CHILE, 4));
                break;
            case "turkey":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TURKEY, 4));
                break;
            case "australia":
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA, 4));
                break;

        }

    }

    private class GetRecentEqsTask extends AsyncTask<Void, Void, List<RecentEq>> {
        @Override
        protected List<RecentEq> doInBackground(Void... params) {
            List<RecentEq> eqs = new RecentEqsQuery().fetchEqs();

            int i = 0;
            for (RecentEq eq : eqs) {
                Log.i(TAG, "EQ " + i + " stats:" + eq.getTitle() + "Lat: " +
                        eq.getLat() + " Long: " + eq.getLong());
                i++;
            }
            return eqs;
        }

        @Override
        protected void onPostExecute(List<RecentEq> eqs) {
            mRecentEqs = eqs;

        }



    }

    private class AddRecentEqsLayer extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject jsonBody = null;
            try {
                jsonBody = new JSONObject(new RecentEqsQuery().getUrlString());
            } catch (IOException ioe) {
                Log.e(TAG, "caught ioe " + ioe);
            } catch (JSONException joe) {
                Log.e(TAG, "caught joe " + joe);
            }
            return jsonBody;

        }



        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            if (jsonObject == null) {

                return;
            }
            mEqsJson = jsonObject;
            GeoJsonLayer layer = new GeoJsonLayer(mMap, mEqsJson);

            GeoJsonPointStyle pointStyle = layer.getDefaultPointStyle();
            BitmapDescriptor bm = BitmapDescriptorFactory.fromResource(R.drawable.map_feature_icon);
            pointStyle.setIcon(bm);

            layer.setOnFeatureClickListener(new GeoJsonLayer.OnFeatureClickListener() {
                @Override
                public void onFeatureClick(Feature feature) {
                    featureText.setText(feature.getProperty("place"));
                    magText.setText("Magnitude: " + feature.getProperty("mag"));
                    mapCard.setVisibility(View.VISIBLE);
                    mMap.setPadding(0,0,0,400);
                    isDetailVisible = true;
                }
            });
            int i = 0;

            for (GeoJsonFeature feature : layer.getFeatures()) {
                Log.i("features", feature.toString());
                //feature.getProperty("title");
                i++;
            }
            Log.i(TAG, "number of features: " + i);
            layer.addLayerToMap();


        }



    }


}
