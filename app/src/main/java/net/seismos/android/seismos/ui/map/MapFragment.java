package net.seismos.android.seismos.ui.map;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

public class MapFragment extends Fragment implements MapContract.View,
        OnMapReadyCallback, EarthquakeRecyclerViewAdapter.OnEqClickListener{
    private static final String TAG = "MapFragment";

    private static final int FILTERS_RESULT = 10;

    MapContract.Presenter mPresenter;

    GoogleMap mMap;
    private static final LatLng SEATTLE = new LatLng(47.6, -122.33);
    private Marker activeMarker = null;

    private BottomSheetBehavior sheetBehavior;
    private RecyclerView mRecyclerView;
    private ArrayList<Earthquake> mEarthquakes = new ArrayList<>();
    private EarthquakeRecyclerViewAdapter mEarthquakeAdapter =
            new EarthquakeRecyclerViewAdapter(mEarthquakes, this);

    public EarthquakeViewModel earthquakeViewModel;

    private float mMinimumMagnitude = 4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        mRecyclerView = root.findViewById(R.id.earthquakeList);
        final View scrolling = root.findViewById(R.id.scrollingIndicator);
        final FloatingActionButton locationFab = root.findViewById(R.id.locationFab);
        locationFab.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.map_location_icon));
        locationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LatLng latLng = new LatLng(Double.parseDouble(getLatitude()), Double.parseDouble(getLongitude()));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(SEATTLE, 1);
                mMap.animateCamera(cameraUpdate);            }
        });

        FrameLayout layoutBottomSheet = root.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == STATE_EXPANDED) {
                    view.setBackground(getResources().getDrawable(R.drawable.nonrounded_bottom_sheet));
                    scrolling.animate().alpha(0).setDuration(200);
                    //scrolling.setVisibility(View.INVISIBLE);
                    locationFab.hide();
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.rounded_bottom_sheet));
                    scrolling.animate().alpha(1).setDuration(200);
//                    scrolling.setVisibility(View.VISIBLE);
                    locationFab.show();
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) { }
        });

        Chip filtersChip = root.findViewById(R.id.filters_chip);
        filtersChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FiltersActivity.class);
                startActivityForResult(intent, FILTERS_RESULT);
            }
        });


        // this is the same as the setOnClickListener because the Close Icon blocks the touch event
        filtersChip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FiltersActivity.class);
                startActivityForResult(intent, FILTERS_RESULT);
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
                        double lat = earthquake.getLatitude();
                        double lon= earthquake.getLongitude();
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lon));
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
                Earthquake markedEarthquake = null;
                LatLng markerPos = marker.getPosition();
                for (Earthquake earthquake : mEarthquakes) {
                    LatLng eqPos = new LatLng(earthquake.getLatitude(), earthquake.getLongitude());
                    if (markerPos.equals(eqPos)) {

                        // prohibit same marker reselection leading to additional identical earthquakes in db
                        if (activeMarker == null) {
                            activeMarker = marker;
                            mEarthquakes.remove(earthquake);
                            mEarthquakes.add(0, earthquake);
                            mEarthquakeAdapter.notifyDataSetChanged();
                        } else {
                            if (!activeMarker.equals(marker)) {
                                mEarthquakes.remove(earthquake);
                                mEarthquakes.add(0, earthquake);
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
                if (activeMarker != null) {
                    if (!activeMarker.equals(marker)){
                        LatLng oldMarkerPos = activeMarker.getPosition();
                        for (Earthquake earthquake : mEarthquakes) {
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
                                activeMarker.setIcon(icon);
                                break;
                            }
                        }
                    }
                }
                activeMarker = marker;

                if (sheetBehavior.getState() == STATE_HIDDEN) {
                    sheetBehavior.setState(STATE_COLLAPSED);
                }
                return false;
            }
        });



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
        intent.putExtra("date", Long.toString(eq.getTime()));
        intent.putExtra("felt", Integer.toString(eq.getFelt()));
        intent.putExtra("tsunami", Integer.toString(eq.getTsunami()));
        intent.putExtra("alert", eq.getAlert());
        intent.putExtra("types", eq.getTypes());
        intent.putExtra("cdi", Double.toString(eq.getCdi()));

        startActivity(intent);
    }
}
