package net.seismos.android.seismos.ui.map;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.chip.Chip;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.global.Preferences;
import net.seismos.android.seismos.ui.global.DashActivity;
import net.seismos.android.seismos.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

public class MapFragment extends Fragment implements MapContract.View,
        OnMapReadyCallback, EarthquakeRecyclerViewAdapter.OnEqClickListener{
    private static final String TAG = "MapFragment";

    private static final int FILTERS_RESULT = 10;
    private static final int ALERTS_RESULT = 11;
    MapContract.Presenter mPresenter;

    GoogleMap mMap = null;


    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);
    private Marker oldMarker = null;
    private Earthquake oldEarthquake = null;
    private int oldEarthquakeIndex = 0;

    private BottomSheetBehavior sheetBehavior;
    private RecyclerView mRecyclerView;

    private ArrayList<Earthquake> renderedEqs = new ArrayList<>();
    private ArrayList<Earthquake> allEarthquakes = new ArrayList<>();

    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter =
            new EarthquakeRecyclerViewAdapter(renderedEqs, this);

    public EarthquakeViewModel earthquakeViewModel;

    private float mMinimumMagnitude;

    ArrayList<Marker> markers = new ArrayList<>();

    SharedPreferences preferences;

    SharedPreferences.OnSharedPreferenceChangeListener listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NAVDEBUG", "onCreate called in MapFragment");
        if (savedInstanceState != null) {
            Log.d("MAPDEBUG", "RECREATED");
        } else {
            Log.d("MAPDEBUG", "NOT RECREATED");
        }

         preferences = getActivity().getSharedPreferences(Preferences.PREFERENCES, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mRecyclerView = root.findViewById(R.id.earthquakeList);
        final View scrolling = root.findViewById(R.id.scrollingIndicator);
        final FloatingActionButton filterFab = root.findViewById(R.id.locationFab);
        filterFab.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.filter_icon));
        filterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FiltersActivity.class);
                startActivityForResult(intent, FILTERS_RESULT);
            }
        });


        FrameLayout layoutBottomSheet = root.findViewById(R.id.bottom_sheet);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == STATE_EXPANDED) {
                    view.setBackground(getResources().getDrawable(R.drawable.nonrounded_bottom_sheet));
                    filterFab.hide();
                    scrolling.setVisibility(View.INVISIBLE);
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.rounded_bottom_sheet));
                    filterFab.show();
                    scrolling.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v>0)
                    filterFab.hide();
            }
        });

        Chip alertsChip = root.findViewById(R.id.alerts_chip);
        alertsChip.setOnClickListener((View v) -> {
                Intent intent = new Intent(getActivity(), AlertsActivity.class);
                startActivityForResult(intent, ALERTS_RESULT);
        });


        // this is the same as the setOnClickListener because the Close Icon blocks the touch event
        alertsChip.setOnCloseIconClickListener((View v) -> {
                Intent intent = new Intent(getActivity(), AlertsActivity.class);
                startActivityForResult(intent, ALERTS_RESULT);
        });

        preferences = getActivity().getSharedPreferences(Preferences.PREFERENCES, 0);

        mMinimumMagnitude = Float.valueOf(preferences.getString(Preferences.PREF_MIN_MAG, "5"));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Obtain the SupportMapFragment and request the GoogleMap

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mMap == null) {
            mapFragment.getMapAsync(this);
        }

        double[] coord = ((DashActivity)getContext()).getCoord();
        if (coord != null) {
            navigateToLatLng(new LatLng(coord[0], coord[1]));
            ((DashActivity)getContext()).resetCoord();
        }

        Bundle bundle = getArguments();

        if (bundle != null) {
            float lat = bundle.getFloat("lat");

            Log.d(TAG, "float param: " + lat);
                Log.d(TAG, "FLOAT ARRAY PARAMETER NOT NULL");


        }

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mEarthquakeAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("MAPDEBUG", "onActivityCreated() called");
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);
        earthquakeViewModel.getEarthquakes().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                if (earthquakes != null) {
                    allEarthquakes = new ArrayList<>(earthquakes);
                    renderEqs();
                }
            }
        });

//        earthquakeViewModel.getSignificantEqs().observe(this, new Observer<List<Earthquake>>() {
//            @Override
//            public void onChanged(@Nullable List<Earthquake> earthquakes) {
//                if (earthquakes != null) {
//                    allEarthquakes = new ArrayList<>(earthquakes);
//                    renderEqs();
//                }
//            }
//        });

        preferences.registerOnSharedPreferenceChangeListener(listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d(TAG, "onsharedprefs listener called");
                if (key.equals(Preferences.PREF_MIN_MAG)) {
                    mMinimumMagnitude = Float.valueOf(sharedPreferences.getString(Preferences.PREF_MIN_MAG, "5"));

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MAPDEBUG", "MapFragment ONRESUME CALLED");
        Log.d("MAPDEBUG", "render size: " + renderedEqs.size());
        Log.d("MAPDEBUG", "allsize: " + allEarthquakes.size());
        renderEqs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MAPDEBUG", "onDestroy called");
    }

    private void renderEqs() {
        mMinimumMagnitude = Float.valueOf(preferences.getString(Preferences.PREF_MIN_MAG, "5"));

        renderedEqs.clear();
        oldMarker = null;
        oldEarthquake = null;
        oldEarthquakeIndex = 0;

        if (mMap != null) {
            mMap.clear();
        }

        markers.clear();

        for (Earthquake earthquake : allEarthquakes) {
            if (earthquake.getMagnitude() >= mMinimumMagnitude) {
                renderedEqs.add(earthquake);
                mEarthquakeAdapter.notifyDataSetChanged(); // use a different less resource intensive dataset changed notifier
            }
        }

        for (Earthquake e : renderedEqs) {
            if (mMap != null) {
                double lat = e.getLatitude();
                double lng = e.getLongitude();
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lng));
                BitmapDescriptor icon;
                if (e.getMagnitude() < 5) {
                    icon = resizeBitmap(R.drawable.map_pin_49, 60, 60);
                } else if (e.getMagnitude() < 6) {
                    icon = resizeBitmap(R.drawable.map_pin_59, 80, 80);
                } else if (e.getMagnitude() < 6.5) {
                    icon = resizeBitmap(R.drawable.map_pin_64, 120, 120);
                } else if (e.getMagnitude() < 7) {
                    icon = resizeBitmap(R.drawable.map_pin_69, 150, 150);
                } else if (e.getMagnitude() < 7.5) {
                    icon = resizeBitmap(R.drawable.map_pin_74, 175, 175);
                } else if (e.getMagnitude() < 8) {
                    icon = resizeBitmap(R.drawable.map_pin_79, 200, 200);
                } else {
                    icon = resizeBitmap(R.drawable.map_pin_8, 200, 200);
                }
                markerOptions.icon(icon);
                markers.add(mMap.addMarker(markerOptions));
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEATTLE, 1));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setPadding(0, 0, 0, 0);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                toggleBottomSheet();
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                markerSelection(marker);
                return false;
            }
        });

        Log.d("MAPDEBUG", "onMapReady() called");
        renderEqs();


        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        //mMap.setMaxZoomPreference(1);
    }

    @Override
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() == STATE_HIDDEN) {
            sheetBehavior.setState(STATE_COLLAPSED);
        } else if (sheetBehavior.getState() == STATE_COLLAPSED) {
            sheetBehavior.setState(STATE_HIDDEN);
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

    @Override
    public void onItemClicked(Earthquake eq) {

        Intent intent = new Intent(getActivity(), EqDetailsActivity.class);
        intent.putExtra("title", eq.getTitle());
        intent.putExtra("mag", Double.toString(eq.getMagnitude()));
        intent.putExtra("lat", eq.getLatitude());
        Log.d(TAG, "LAT:" + eq.getLatitude());
        Log.d(TAG, "preference: " + preferences.getString(Preferences.PREF_MIN_MAG, "nonthing"));
        intent.putExtra("long", eq.getLongitude());
        intent.putExtra("place", eq.getPlace());
        intent.putExtra("date", Long.toString(eq.getTime()));
        intent.putExtra("felt", Integer.toString(eq.getFelt()));
        intent.putExtra("tsunami", Integer.toString(eq.getTsunami()));
        intent.putExtra("alert", eq.getAlert());
        intent.putExtra("types", eq.getTypes());
        intent.putExtra("cdi", Double.toString(eq.getCdi()));

        startActivity(intent);
    }

    @Override
    public void navigateToLatLng(final LatLng latLng) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            for (Marker marker : markers) {
                if (marker.getPosition().equals(latLng)) {
                    markerSelection(marker);
                }
            }
        }

    }

    private void markerSelection(Marker marker) {

        Earthquake markedEarthquake = null;
        LatLng markerPos = marker.getPosition();
        for (Earthquake earthquake : renderedEqs) {
            LatLng eqPos = new LatLng(earthquake.getLatitude(), earthquake.getLongitude());
            if (markerPos.equals(eqPos)) {

                if (oldEarthquake != null) {
                    renderedEqs.remove(oldEarthquake);
                    renderedEqs.add(oldEarthquakeIndex, oldEarthquake);
                }

                // prohibit same marker reselection leading to additional identical earthquakes in db
                if (oldMarker == null) {
                    oldMarker = marker;
                    oldEarthquakeIndex = renderedEqs.indexOf(earthquake);
                    oldEarthquake = earthquake;
                    renderedEqs.remove(earthquake);
                    renderedEqs.add(0, earthquake);
                    mEarthquakeAdapter.notifyDataSetChanged();
                } else {
                    if (!oldMarker.equals(marker)) {
                        oldEarthquakeIndex = renderedEqs.indexOf(earthquake);
                        oldEarthquake = earthquake;
                        renderedEqs.remove(earthquake);
                        renderedEqs.add(0, earthquake);
                        mEarthquakeAdapter.notifyDataSetChanged();
                    }
                }
                markedEarthquake = earthquake;
                break;
            }
        }


        // This series of if else determine which active marker to use depending on the
        // magnitude of the earthquake
        if (markedEarthquake != null) {
            if (markedEarthquake.getMagnitude() < 5) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_49, 90, 126));
            } else if (markedEarthquake.getMagnitude() < 6) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_59, 90, 126));
            } else if (markedEarthquake.getMagnitude() < 6.5) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_64, 90, 126));
            } else if (markedEarthquake.getMagnitude() < 7) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_69, 90, 126));
            } else if (markedEarthquake.getMagnitude() < 7.5) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_74, 90, 126));
            } else if (markedEarthquake.getMagnitude() < 8) {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_79, 90, 126));
            } else {
                marker.setIcon(resizeBitmap(R.drawable.active_pin_8, 90, 126));
            }
        }

        // prohibit marker reselection leading to active marker being removed
        if (oldMarker != null) {
            if (!oldMarker.equals(marker)){
                LatLng oldMarkerPos = oldMarker.getPosition();
                for (Earthquake earthquake : renderedEqs) {
                    LatLng eqPos = new LatLng(earthquake.getLatitude(), earthquake.getLongitude());
                    if (oldMarkerPos.equals(eqPos)) {
                        BitmapDescriptor icon;
                        if (earthquake.getMagnitude() < 5) {
                            icon = resizeBitmap(R.drawable.map_pin_49, 60, 60);
                        } else if (earthquake.getMagnitude() < 6) {
                            icon = resizeBitmap(R.drawable.map_pin_59, 80, 80);
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
                        oldMarker.setIcon(icon);
                        break;
                    }
                }
            }
        }

        oldMarker = marker;

        if (sheetBehavior.getState() == STATE_HIDDEN) {
            sheetBehavior.setState(STATE_COLLAPSED);
        }
    }
}
