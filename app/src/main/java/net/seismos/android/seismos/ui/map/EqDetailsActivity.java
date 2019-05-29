package net.seismos.android.seismos.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.seismos.android.seismos.R;

public class EqDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "EqDetailsActivity";

    private GoogleMap mMap;

    private double latitude;
    private double longitude;
    private Marker marker;
    private String place;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_details);

        Toolbar toolbar = findViewById(R.id.eq_details_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView placeText = findViewById(R.id.placeTextTitle);
        TextView locationText = findViewById(R.id.detailLocationText);


        Intent intent = getIntent();

        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);
        place = intent.getStringExtra("place");

        placeText.setText(place);
        locationText.setText("Lat: " + latitude + " Long: " + longitude);

//        mag.setText(intent.getStringExtra("mag"));
//        title.setText(intent.getStringExtra("title"));
//        date.setText(intent.getStringExtra("date"));
//        felt.setText(intent.getStringExtra("felt"));
//        tsunami.setText(intent.getStringExtra("tsunami"));
//        alert.setText(intent.getStringExtra("alert"));
//        types.setText(intent.getStringExtra("types"));
//        cdi.setText(intent.getStringExtra("cdi"));

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);
//        mapFragment.getMapAsync(this);
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0, 0, 0, 0);

        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(latitude, longitude));

        marker = mMap.addMarker(options);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),
                                                                    3);


        mMap.moveCamera(update);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(false);

        //mMap.setMaxZoomPreference(1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
