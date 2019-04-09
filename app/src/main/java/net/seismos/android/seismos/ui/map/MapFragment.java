package net.seismos.android.seismos.ui.map;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.Earthquake;
import net.seismos.android.seismos.data.EarthquakeViewModel;
import net.seismos.android.seismos.data.RecentEqsQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

public class MapFragment extends Fragment implements MapContract.View,
        OnMapReadyCallback {

    private static final String TAG = "MapFragment";

    MapContract.Presenter mPresenter;

    GoogleMap mMap;
    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);

    private BottomSheetBehavior sheetBehavior;


    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter =
            new EarthquakeRecyclerViewAdapter(mEarthquakes);

    public EarthquakeViewModel earthquakeViewModel;

    private Marker activeMarker;

    private float mMinimumMagnitude = 4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mRecyclerView = root.findViewById(R.id.earthquakeList);

        final View scrolling = root.findViewById(R.id.scrollingIndicator);

        FrameLayout layoutBottomSheet = root.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == STATE_EXPANDED) {
                    view.setBackground(getResources().getDrawable(R.drawable.nonrounded_bottom_sheet));
                    scrolling.setVisibility(View.INVISIBLE);
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.rounded_bottom_sheet));
                    scrolling.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and request the GoogleMap
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mEarthquakeAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the Earthquake View Model for the parent Activity
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);
        // Get the data from the View Model, and observe and changes
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                // When the View Model changes, update the list
                if (earthquakes != null)
                    setEarthquakes(earthquakes);
            }
        });


    }



    public void setEarthquakes(List<Earthquake> earthquakes) {

        for (Earthquake earthquake: earthquakes) {
            if (earthquake.getMagnitude() >= mMinimumMagnitude) {
                if (!mEarthquakes.contains(earthquake)) {
                    mEarthquakes.add(earthquake);
                    mEarthquakeAdapter.notifyItemInserted(mEarthquakes.indexOf(earthquake));
                    if (mMap != null) {
                        double lat = earthquake.getLocation().getLatitude();
                        double lon= earthquake.getLocation().getLongitude();
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lon));
                        BitmapDescriptor icon;
                        if (earthquake.getMagnitude() < 5) {
                             icon = resizeBitmap(R.drawable.map_pin_49, 90, 90);
                        } else if (earthquake.getMagnitude() < 6) {
                             icon = resizeBitmap(R.drawable.map_pin_59, 100, 100);
                        } else if (earthquake.getMagnitude() < 6.5) {
                             icon = resizeBitmap(R.drawable.map_pin_64, 120, 120);
                        } else if (earthquake.getMagnitude() < 7) {
                             icon = resizeBitmap(R.drawable.map_pin_69, 150, 150);
                        } else if (earthquake.getMagnitude() < 7.5) {
                             icon = resizeBitmap(R.drawable.map_pin_74, 175, 175);
                        } else if (earthquake.getMagnitude() < 8) {
                             icon = resizeBitmap(R.drawable.map_pin_79, 200, 200);
                        } else {
                             icon = resizeBitmap(R.drawable.map_pin_8, 200, 200);
                        }
                        markerOptions.icon(icon);
                        mMap.addMarker(markerOptions);
                    }

                }
            }
        }
    }

    private BitmapDescriptor resizeBitmap(int res, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), res);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions
                .loadRawResourceStyle(getContext(), R.raw.map_style));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE, 4));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0, 0, 0, 0);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mPresenter.mapClicked();

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng markerPos = marker.getPosition();
                for (Earthquake earthquake : mEarthquakes) {
                    LatLng eqPos = new LatLng(earthquake.getLocation().getLatitude(), earthquake.getLocation().getLongitude());
                    if (markerPos.equals(eqPos)) {
                        mEarthquakes.add(0, earthquake);
                        mEarthquakeAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                marker.setIcon(resizeBitmap(R.drawable.active_pin, 120, 168));

                if (activeMarker != null) {
                    LatLng oldMarkerPos = activeMarker.getPosition();
                    for (Earthquake earthquake : mEarthquakes) {
                        LatLng eqPos = new LatLng(earthquake.getLocation().getLatitude(), earthquake.getLocation().getLongitude());
                        if (oldMarkerPos.equals(eqPos)) {
                            BitmapDescriptor icon;
                            if (earthquake.getMagnitude() < 5) {
                                icon = resizeBitmap(R.drawable.map_pin_49, 90, 90);
                            } else if (earthquake.getMagnitude() < 6) {
                                icon = resizeBitmap(R.drawable.map_pin_59, 100, 100);
                            } else if (earthquake.getMagnitude() < 6.5) {
                                icon = resizeBitmap(R.drawable.map_pin_64, 120, 120);
                            } else if (earthquake.getMagnitude() < 7) {
                                icon = resizeBitmap(R.drawable.map_pin_69, 150, 150);
                            } else if (earthquake.getMagnitude() < 7.5) {
                                icon = resizeBitmap(R.drawable.map_pin_74, 175, 175);
                            } else if (earthquake.getMagnitude() < 8) {
                                icon = resizeBitmap(R.drawable.map_pin_79, 200, 200);
                            } else {
                                icon = resizeBitmap(R.drawable.map_pin_8, 200, 200);
                            }
                            activeMarker.setIcon(icon);
                            break;
                        }
                    }
                }
                activeMarker = marker;
                return false;
            }
        });
    }

    @Override
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != STATE_EXPANDED) {
            sheetBehavior.setState(STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void showSomething() {
        Log.d(TAG, "showSomething");


    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
