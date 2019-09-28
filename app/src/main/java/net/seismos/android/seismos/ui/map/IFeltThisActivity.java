package net.seismos.android.seismos.ui.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.data.model.Earthquake;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class IFeltThisActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "EqDetailsActivity";

    private GoogleMap mMap;

    private double latitude;
    private double longitude;
    private Marker marker;
    private String place;
    private String title;
    private String id;
    private double magnitude;
    private Date date;

    private EarthquakeViewModel earthquakeViewModel;
    private Earthquake eq;
    private ArrayList<Earthquake> eqs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_felt_this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Intent intent = getIntent();

        latitude = intent.getDoubleExtra("lat", 0);
        longitude = intent.getDoubleExtra("long", 0);
        String lat = latitude > 0 ? "° N" : "° S";
        String lon = longitude > 0 ? "° W" : "° E";

        title = intent.getStringExtra("title");
        ((TextView)findViewById(R.id.placeTextTitle)).setText(title);

        date = new Date(intent.getLongExtra("date", 0));
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a z", Locale.getDefault());
        String time = format.format(date);
        ((TextView)findViewById(R.id.detailTimeText)).setText(time);




        ((TextView)findViewById(R.id.detectedBy))
                .setText(intent.getStringExtra("felt"));


        ((TextView)findViewById(R.id.depthCount))
                .setText(intent.getDoubleExtra("depth", 0) + " km");

        place = intent.getStringExtra("place");
        id = intent.getStringExtra("id");
        magnitude = intent.getDoubleExtra("mag", 0);

//        mag.setText(intent.getStringExtra("mag"));
//        title.setText(intent.getStringExtra("title"));
//        felt.setText(intent.getStringExtra("felt"));
//        tsunami.setText(intent.getStringExtra("tsunami"));
//        alert.setText(intent.getStringExtra("alert"));
//        types.setText(intent.getStringExtra("types"));
//        cdi.setText(intent.getStringExtra("cdi"));

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapfrag);
        mapFragment.getMapAsync(this);

    }


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
        if (magnitude < 5) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_49, 256, 300));
        } else if (magnitude< 6) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_59, 256, 300));
        } else if (magnitude< 6.5) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_64, 256, 300));
        } else if (magnitude< 7) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_69, 256, 300));
        } else if (magnitude< 7.5) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_74, 256, 300));
        } else if (magnitude < 8) {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_79, 256, 300));
        } else {
            marker.setIcon(resizeBitmap(R.drawable.active_pin_8, 256, 300));
        }

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude + 80, longitude),
                1);
        mMap.moveCamera(update);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

    }

    private BitmapDescriptor resizeBitmap(int res, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), res);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
