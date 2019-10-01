package net.seismos.android.seismos.ui.home;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.detection.DetectionService;
import net.seismos.android.seismos.global.GlobalApplicationState;
import net.seismos.android.seismos.util.DepressAnimationUtil;
import net.seismos.android.seismos.util.ResUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.View ,
        SensorEventListener {
    private static final String TAG = "HomeFragment";

    GlobesAdapter globesAdapter;
    FirebaseFirestore db;
    private int earnedToday = 0;
    TextView earnedTodayText;
    private HomeContract.Presenter mPresenter;
    OnEqGlobeSelectedListener listener;
    BarDataSet setHandle;
    private BarChart bottomChart;
    private BarChart topChart;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Thread thread;
    private boolean plotData = true;
    private boolean listening = false;
    private boolean firstEntry = true;


    private ChartTabAdapter chartTabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment todayChart;
    private Fragment weekChart;
    private Fragment monthChart;

    SeiEarnedView seiEarnedView;

    public EarthquakeViewModel earthquakeViewModel;
    private ArrayList<Earthquake> mTopEarthquakes = new ArrayList<>();

    public HomeFragment() { } // required empty public constructor

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnEqGlobeSelectedListener)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        Log.d(TAG, "Firestore listen failed: ", e);
                        return;
                    }

                    Double today = documentSnapshot.getDouble("earnedToday");
                    if (today != null) {
                        earnedToday  = today.intValue();

                            if (earnedTodayText != null)
                                earnedTodayText.setText(Integer.toString(earnedToday));
                            Log.d("TESTING", "updaters called");
                            if (earnedToday<=360) {
                                seiEarnedView.updateFirstRing(earnedToday);
                                seiEarnedView.setRing2Activated(false);
                                seiEarnedView.setRing3Activated(false);
                            } else if (earnedToday<=720) {
                                seiEarnedView.maxFirst();
                                seiEarnedView.setRing2Activated(true);
                                seiEarnedView.setRing3Activated(false);
                                seiEarnedView.updateSecondRing(earnedToday-360);
                            } else if (earnedToday<=1080) {
                                seiEarnedView.maxFirst();
                                seiEarnedView.maxSecond();
                                seiEarnedView.setRing2Activated(true);
                                seiEarnedView.setRing3Activated(true);
                                seiEarnedView.updateThirdRing(earnedToday - 720);
                            }  else {
                                seiEarnedView.setRing2Activated(true);
                                seiEarnedView.setRing3Activated(true);
                                seiEarnedView.maxFirst();
                                seiEarnedView.maxSecond();
                                seiEarnedView.maxThird();
                            }
                    }
                });

        todayChart = new ChartFragmentToday();
        weekChart = new ChartFragmentWeek();
        monthChart = new ChartFragmentMonth();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        seiEarnedView = root.findViewById(R.id.seiEarnedView);


         viewPager = root.findViewById(R.id.viewPager);
         tabLayout = root.findViewById(R.id.tabLayout);

         chartTabAdapter = new ChartTabAdapter(getFragmentManager());
         chartTabAdapter.addFragment(todayChart,"Today");
         chartTabAdapter.addFragment(weekChart, "Week");
         chartTabAdapter.addFragment(monthChart, "Month");

         viewPager.setAdapter(chartTabAdapter);

         tabLayout.setupWithViewPager(viewPager);

        ImageView button = root.findViewById(R.id.recordingButton);
        DepressAnimationUtil.setup(button);

         if (((GlobalApplicationState)(getContextHandle().getApplicationContext())).isRecording()) {
             button.setImageDrawable(getResources().getDrawable(R.drawable.recording));
         } else {
             button.setImageDrawable(getResources().getDrawable(R.drawable.not_recording));
         }

         topChart = root.findViewById(R.id.accelChart1);

         bottomChart = root.findViewById(R.id.accelChart2);

         root.findViewById(R.id.recordingButton).setOnClickListener(v -> {
             if (((GlobalApplicationState)(getContextHandle().getApplicationContext())).isRecording()) {
                 button.setImageDrawable(getResources().getDrawable(R.drawable.not_recording));
                 setHandle.setColor(getResources().getColor(R.color.blueDark));
                 ((GlobalApplicationState)(getContextHandle().getApplicationContext())).setRecording(false);
                 stopService();
             } else {
                 button.setImageDrawable(getResources().getDrawable(R.drawable.recording));
                 setHandle.setColor(getResources().getColor(R.color.eq74GradientStart));
                 ((GlobalApplicationState)(getContextHandle().getApplicationContext())).setRecording(true);
                 startService();
             }
         });

         root.findViewById(R.id.editProfileButton).setOnClickListener((View v) -> {
             Intent intent = new Intent(getContext(), ScheduleActivity.class);
             startActivity(intent);
         });

         root.findViewById(R.id.upgradeChip).setOnClickListener((View v) -> {
                 Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                 startActivity(intent);
         });

         root.findViewById(R.id.homeDataQuality).setOnClickListener((View v) -> {
                 Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                 startActivity(intent);
         });
        return root;
    }

    public void startService() {
        Intent serviceIntent = new Intent(getContext(), DetectionService.class);
        serviceIntent.putExtra("input", "You've earned 37 sei today");
        ContextCompat.startForegroundService(getContext(), serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(getContext(), DetectionService.class);
        getContextHandle().stopService(serviceIntent);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView awardsRecycler = view.findViewById(R.id.awardsRecyclerView);
        RecyclerView globesRecycler = view.findViewById(R.id.globesRecyclerView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        globesRecycler.setLayoutManager(layoutManager);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        awardsRecycler.setLayoutManager(layoutManager);

        globesAdapter = new GlobesAdapter(listener);

        globesRecycler.setAdapter(globesAdapter);
        globesAdapter.setEarthquakes(mTopEarthquakes);
        awardsRecycler.setAdapter(new AwardsAdapter());

        earnedTodayText = view.findViewById(R.id.earnedToday);
        earnedTodayText.setText(Integer.toString(earnedToday));



        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Firestore listen failed: ", e);
                            return;
                        }

                        Double today = documentSnapshot.getDouble("earnedToday");
                        if (today != null) {
                            earnedToday  = today.intValue();
                        }
                    }
                });


        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);


        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        // start
        // enable description text
        bottomChart.getDescription().setEnabled(false);
        // enable touch gestures
        bottomChart.setTouchEnabled(false);
        // enable scaling and dragging
        bottomChart.setDragEnabled(false);
        bottomChart.setScaleEnabled(false);
        bottomChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        bottomChart.setPinchZoom(false);
        // set an alternative background color
        bottomChart.setBackgroundColor(getResources().getColor(R.color.blueBackground));

        // enable description text
        topChart.getDescription().setEnabled(false);
        // enable touch gestures
        topChart.setTouchEnabled(false);
        // enable scaling and dragging
        topChart.setDragEnabled(false);
        topChart.setScaleEnabled(false);
        topChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        topChart.setPinchZoom(false);
        // set an alternative background color
        topChart.setBackgroundColor(getResources().getColor(R.color.blueBackground));
        BarData data = new BarData();



        //start
        bottomChart.setData(data);
        Legend l2 = bottomChart.getLegend();
        l2.setEnabled(false);

        XAxis xl2 = bottomChart.getXAxis();
        xl2.setEnabled(false);

        YAxis leftAxis2 = bottomChart.getAxisLeft();
        leftAxis2.setAxisMaximum(5f);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setInverted(true);
        leftAxis2.setEnabled(false);

        YAxis rightAxis2 = bottomChart.getAxisRight();
        rightAxis2.setDrawZeroLine(false);
        rightAxis2.setEnabled(false);

        bottomChart.getAxisLeft().setDrawGridLines(false);
        bottomChart.getXAxis().setDrawGridLines(false);
        bottomChart.setDrawBorders(false);

        bottomChart.setViewPortOffsets(0, 0, 0, 0);

        CustomBarChartRender2 barChartRender2 = new CustomBarChartRender2(bottomChart, bottomChart.getAnimator(), bottomChart.getViewPortHandler());
        barChartRender2.setRadius(10);
        bottomChart.setRenderer(barChartRender2);


        setupGradient2(bottomChart);

        //end

        // add empty data
        topChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = topChart.getLegend();
        l.setEnabled(false);

        XAxis xl = topChart.getXAxis();
        xl.setEnabled(false);

        YAxis leftAxis = topChart.getAxisLeft();
        leftAxis.setAxisMaximum(5f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setEnabled(false);

        YAxis rightAxis = topChart.getAxisRight();
        rightAxis.setDrawZeroLine(false);
        rightAxis.setEnabled(false);

        topChart.getAxisLeft().setDrawGridLines(false);
        topChart.getXAxis().setDrawGridLines(false);
        topChart.setDrawBorders(false);

        topChart.setViewPortOffsets(0, 0, 0, 0);

        CustomBarChartRender barChartRender = new CustomBarChartRender(topChart, topChart.getAnimator(), topChart.getViewPortHandler());
        barChartRender.setRadius(10);
        topChart.setRenderer(barChartRender);

        setupGradient(topChart);

        startPlot();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Retrieve the Earthquake View Model for the parent Activity
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);
        // Get the data from the View Model, and observe and changes

        Log.d(TAG, "onActivityCreated() called in HomeFragment");

        earthquakeViewModel.getSignificantEqs().observe(this, earthquakes -> {
            if (earthquakes != null && earthquakes.size()  != 0) {
                setEarthquakes(earthquakes);
            }
        });
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {
        mTopEarthquakes = (ArrayList<Earthquake>)earthquakes;
        if (globesAdapter != null) {
            globesAdapter.setEarthquakes(mTopEarthquakes);
        }



    }


    private String parseTitle(Earthquake eq) {
        String result = "M";
        result = result.concat(Double.toString(eq.getMag()));

        return result;
    }


    private String parseDetail(Earthquake eq) {
        Date date = new Date(eq.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");

        return sdf.format(date);
    }


    private void startPlot() {
        if (thread != null) {
            thread.interrupt();
        }

        thread = new Thread(() -> {
            while (true) {
                plotData = true;
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (plotData) {
            addEntry(event);
            plotData = false;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void addEntry(SensorEvent event) {



        BarData data = topChart.getData();

        if (data != null) {

            IBarDataSet set = data.getDataSetByIndex(0);

            if (set==null) {
                setHandle = createSet();
                set = setHandle;
                data.addDataSet(set);
            }
            float entryval = (Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]))/3f;
            entryval = (float)(5 - (5 / (0.5*entryval + 1)));
            entryval += 0.1;
            data.setBarWidth(0.5f);

            if (firstEntry) {
                for (int i = 0; i < 50; i++) {
                    data.addEntry(new BarEntry(set.getEntryCount(), entryval), 0);
                }
                firstEntry =false;
            }

            data.addEntry(new BarEntry(set.getEntryCount(), entryval), 0);
            data.notifyDataChanged();
            // start
            bottomChart.notifyDataSetChanged();
            bottomChart.setVisibleXRangeMaximum(70);
            //end
//
            topChart.notifyDataSetChanged();
            topChart.setVisibleXRangeMaximum(70);
            topChart.moveViewToX(data.getEntryCount());
            bottomChart.moveViewToX(data.getEntryCount());

        }
    }

    private BarDataSet createSet() {
        ArrayList<BarEntry> values = new ArrayList<>();
        BarDataSet set = new BarDataSet(values, "data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

//        set.setColors(Color.rgb(67,67,72), Color.rgb(124,181,236));

        set.setColor(getResources().getColor(R.color.blueDefault));
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        return set;
    }

    private void setupGradient(BarChart chart) {
        Paint paint = chart.getRenderer().getPaintRender();
        int totalh = ResUtil.getInstance().dpToPx(40);
        int endh = ResUtil.getInstance().dpToPx(20);
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, totalh, 0, endh,
                getResources().getColor(R.color.gradientDangerStart),
                getResources().getColor(R.color.gradientDangerEnd),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    private void setupGradient2(BarChart chart) {
        Paint paint = chart.getRenderer().getPaintRender();
        int height = ResUtil.getInstance().dpToPx(20);
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.gradientDangerStart),
                getResources().getColor(R.color.gradientDangerEnd),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    public interface OnEqGlobeSelectedListener {
        void openMapToLatLng(LatLng latLng);
    }


    @Override
    public void onStop() {
        super.onStop();

        if (thread != null) {
            thread.interrupt();
        }
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
            seiEarnedView.restore(earnedToday);

        Log.d("TESTING", "onResume called");






        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firstEntry = true;
    }

    @Override
    public void showSomething() {
        // demo method that can be called by whatever has access to this through HomeContract.View

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getContextHandle() {
        if (getContext()!=null) {
            return getContext();
        } else {
            return null;
        }
    }
}