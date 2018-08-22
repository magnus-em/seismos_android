package net.seismos.android.seismos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);
    private static final LatLng HONGKONG = new LatLng(22.39, 114.1095);
    private static final LatLng NYC = new LatLng(40.7, -74);
    private static final LatLng LONDON = new LatLng(51.5, 0.12);

    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}
