package net.seismos.android.seismos.ui.home;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import androidx.lifecycle.Observer;
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
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.maps.model.LatLng;


import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.local.EarthquakeViewModel;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.util.ResUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements HomeContract.View ,
        SensorEventListener {

    private static final String TAG = "HomeFragment";
    private static final int SCHEDULE_RESULT = 101;

    private HomeContract.Presenter mPresenter;

    OnEqGlobeSelectedListener listener;

    public interface OnEqGlobeSelectedListener {
        public void openMapToLatLng(LatLng latLng);
    }

    BarDataSet setHandle;

    private BarChart mChart2;
    private BarChart mChart;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Thread thread;
    private boolean plotData = true;

    private boolean listening = false;




    private ChartTabAdapter chartTabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    SeiEarnedView seiEarnedView;
    EqGlobeView globe1;
    EqGlobeView globe2;
    EqGlobeView globe3;
    EqGlobeView globe4;
    EqGlobeView globe5;
    EqGlobeView globe6;
    EqGlobeView globe7;
    EqGlobeView globe8;
    EqGlobeView globe9;
    EqGlobeView globe10;

    ArrayList<EqGlobeView> globes;

    TextView eqTitle1;
    TextView eqTitle2;
    TextView eqTitle3;
    TextView eqTitle4;
    TextView eqTitle5;
    TextView eqTitle6;
    TextView eqTitle7;
    TextView eqTitle8;
    TextView eqTitle9;
    TextView eqTitle10;

    ArrayList<TextView> titles;

    TextView eqDetail1;
    TextView eqDetail2;
    TextView eqDetail3;
    TextView eqDetail4;
    TextView eqDetail5;
    TextView eqDetail6;
    TextView eqDetail7;
    TextView eqDetail8;
    TextView eqDetail9;
    TextView eqDetail10;

    ArrayList<TextView> details;


    int upgradeCount = 0;

    double progressAngle;

    public EarthquakeViewModel earthquakeViewModel;
    private ArrayList<Earthquake> mTopEarthquakes = new ArrayList<>();



    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnEqGlobeSelectedListener)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        seiEarnedView = root.findViewById(R.id.seiEarnedView);
        HorizontalScrollView scrollView = root.findViewById(R.id.globeScrollView);
        scrollView.setHorizontalScrollBarEnabled(false);
        root.findViewById(R.id.homeDailySeiCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
                animator.setDuration(25000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float progress = animation.getAnimatedFraction()*(1080);
                        if (progress <= 360) {
                            seiEarnedView.updateFirstRing(progress);
                        } else if (progress <= 720) {
                            seiEarnedView.updateSecondRing(progress-360);
                        } else {
                            seiEarnedView.updateThirdRing(progress-720);
                        }

                    }
                });
                animator.start();

            }
        });

        final TextView seiCount = root.findViewById(R.id.homeDailySeiCount);


        globes = new ArrayList<>();

         globe1 = root.findViewById(R.id.EqGlobe1);
        globe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(
                        new LatLng(mTopEarthquakes.get(0).getLatitude(),
                                mTopEarthquakes.get(0).getLongitude())
                );
            }
        });
         globe2 = root.findViewById(R.id.EqGlobe2);
         globe2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.openMapToLatLng(new LatLng(
                         mTopEarthquakes.get(1).getLatitude(),
                         mTopEarthquakes.get(1).getLongitude()
                 ));
             }
         });
         globe3 = root.findViewById(R.id.EqGlobe3);
        globe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(2).getLatitude(),
                        mTopEarthquakes.get(2).getLongitude()
                ));
            }
        });
         globe4 = root.findViewById(R.id.EqGlobe4);
         globe4.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 listener.openMapToLatLng(new LatLng(
                         mTopEarthquakes.get(3).getLatitude(),
                         mTopEarthquakes.get(3).getLongitude()
                 ));
             }
         });

         globe5 = root.findViewById(R.id.EqGlobe5);
        globe5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(4).getLatitude(),
                        mTopEarthquakes.get(4).getLongitude()
                ));
            }
        });

         globe6 = root.findViewById(R.id.EqGlobe6);
        globe6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(5).getLatitude(),
                        mTopEarthquakes.get(5).getLongitude()
                ));
            }
        });

         globe7 = root.findViewById(R.id.EqGlobe7);
        globe7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(6).getLatitude(),
                        mTopEarthquakes.get(6).getLongitude()
                ));
            }
        });

         globe8 = root.findViewById(R.id.EqGlobe8);
        globe8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(7).getLatitude(),
                        mTopEarthquakes.get(7).getLongitude()
                ));
            }
        });

         globe9 = root.findViewById(R.id.EqGlobe9);
        globe9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openMapToLatLng(new LatLng(
                        mTopEarthquakes.get(8).getLatitude(),
                        mTopEarthquakes.get(8).getLongitude()
                ));
            }
        });



        globes.add(globe1);
        globes.add(globe2);
        globes.add(globe3);
        globes.add(globe4);
        globes.add(globe5);
        globes.add(globe6);
        globes.add(globe7);
        globes.add(globe8);
        globes.add(globe9);

        for (EqGlobeView globeView : globes) {
            setupGlobesDepress(globeView);
        }




         titles = new ArrayList<>();

         eqTitle1 = root.findViewById(R.id.EqTitle1);
         titles.add(eqTitle1);
         eqTitle2 = root.findViewById(R.id.EqTitle2);
         titles.add(eqTitle2);
        eqTitle3 = root.findViewById(R.id.EqTitle3);
        titles.add(eqTitle3);
        eqTitle4 = root.findViewById(R.id.EqTitle4);
        titles.add(eqTitle4);
        eqTitle5 = root.findViewById(R.id.EqTitle5);
        titles.add(eqTitle5);
        eqTitle6 = root.findViewById(R.id.EqTitle6);
        titles.add(eqTitle6);
        eqTitle7 = root.findViewById(R.id.EqTitle7);
        titles.add(eqTitle7);
        eqTitle8 = root.findViewById(R.id.EqTitle8);
        titles.add(eqTitle8);
        eqTitle9 = root.findViewById(R.id.EqTitle9);
        titles.add(eqTitle9);



         eqDetail1 = root.findViewById(R.id.EqDetail1);
         eqDetail2 = root.findViewById(R.id.EqDetail2);
         eqDetail3 = root.findViewById(R.id.EqDetail3);
         eqDetail4 = root.findViewById(R.id.EqDetail4);
         eqDetail5 = root.findViewById(R.id.EqDetail5);
         eqDetail6 = root.findViewById(R.id.EqDetail6);
         eqDetail7 = root.findViewById(R.id.EqDetail7);
         eqDetail8 = root.findViewById(R.id.EqDetail8);
         eqDetail9 = root.findViewById(R.id.EqDetail9);


         details = new ArrayList<>();

         details.add(eqDetail1);
         details.add(eqDetail2);
         details.add(eqDetail3);
         details.add(eqDetail4);
         details.add(eqDetail5);
         details.add(eqDetail6);
         details.add(eqDetail7);
         details.add(eqDetail8);
         details.add(eqDetail9);


         viewPager = root.findViewById(R.id.viewPager);
         tabLayout = root.findViewById(R.id.tabLayout);

         chartTabAdapter = new ChartTabAdapter(getFragmentManager());
         chartTabAdapter.addFragment(new ChartFragmentToday(), "Today");
         chartTabAdapter.addFragment(new ChartFragmentWeek(), "Week");
         chartTabAdapter.addFragment(new ChartFragmentMonth(), "Month");

         viewPager.setAdapter(chartTabAdapter);
         tabLayout.setupWithViewPager(viewPager);

         final FloatingActionButton button = root.findViewById(R.id.homePlayPause);


         mChart = root.findViewById(R.id.accelChart1);
         mChart2 = root.findViewById(R.id.accelChart2);

         root.findViewById(R.id.homePlayPause).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (listening) {
                     button.setImageDrawable(getResources().getDrawable(R.drawable.home_play_icon));
                     setHandle.setColor(getResources().getColor(R.color.blueDark));
                     listening = false;
                 } else {
                     button.setImageDrawable(getResources().getDrawable(R.drawable.home_pause_icon));
                     setHandle.setColor(getResources().getColor(R.color.eq74GradientStart));
                     listening = true;
                 }




//                 if (listening) {
//                     button.setImageDrawable(getResources().getDrawable(R.drawable.home_pause_icon));
//                     setHandle.setColor(getResources().getColor(R.color.eq74GradientStart));
//                     upgradeCount++;
//                 } else if (upgradeCount == 1) {
//                     button.setImageDrawable(getResources().getDrawable(R.drawable.home_play_icon));
//                     setHandle.setColor(getResources().getColor(R.color.blueDark));
//                     upgradeCount++;
//                 } else if (upgradeCount == 2) {
//                     button.setImageDrawable(getResources().getDrawable(R.drawable.home_pause_icon));
//                     setHandle.setColor(getResources().getColor(R.color.eq74GradientStart));
//                     upgradeCount = 0;
//                 }

             }
         });


         root.findViewById(R.id.emailButton).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getContext(), ScheduleActivity.class);
                 startActivityForResult(intent, SCHEDULE_RESULT);
             }
         });

         root.findViewById(R.id.upgradeChip).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                 startActivity(intent);
             }
         });

         root.findViewById(R.id.homeDataQuality).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getActivity(), UpgradeActivity.class);
                 startActivity(intent);
             }
         });



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);


        if (mAccelerometer != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }



        // start
        // enable description text
        mChart2.getDescription().setEnabled(false);
        // enable touch gestures
        mChart2.setTouchEnabled(false);
        // enable scaling and dragging
        mChart2.setDragEnabled(false);
        mChart2.setScaleEnabled(false);
        mChart2.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart2.setPinchZoom(false);
        // set an alternative background color
        mChart2.setBackgroundColor(getResources().getColor(R.color.blueBackground));

        // enable description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(false);
        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        // set an alternative background color
        mChart.setBackgroundColor(getResources().getColor(R.color.blueBackground));
        BarData data = new BarData();



        //start
        mChart2.setData(data);
        Legend l2 = mChart2.getLegend();
        l2.setEnabled(false);

        XAxis xl2 = mChart2.getXAxis();
        xl2.setEnabled(false);

        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setAxisMaximum(5f);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setInverted(true);
        leftAxis2.setEnabled(false);

        YAxis rightAxis2 = mChart2.getAxisRight();
        rightAxis2.setDrawZeroLine(false);
        rightAxis2.setEnabled(false);

        mChart2.getAxisLeft().setDrawGridLines(false);
        mChart2.getXAxis().setDrawGridLines(false);
        mChart2.setDrawBorders(false);

        mChart2.setViewPortOffsets(0, 0, 0, 0);

        CustomBarChartRender2 barChartRender2 = new CustomBarChartRender2(mChart2,mChart2.getAnimator(), mChart2.getViewPortHandler());
        barChartRender2.setRadius(10);
        mChart2.setRenderer(barChartRender2);


//        setupGradient2(mChart);

        //end

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaximum(5f);
        leftAxis.setAxisMinimum(0);
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawZeroLine(false);
        rightAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        mChart.setViewPortOffsets(0, 0, 0, 0);

        CustomBarChartRender barChartRender = new CustomBarChartRender(mChart,mChart.getAnimator(), mChart.getViewPortHandler());
        barChartRender.setRadius(10);
        mChart.setRenderer(barChartRender);


//        setupGradient(mChart);




        startPlot();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Retrieve the Earthquake View Model for the parent Activity
        earthquakeViewModel = ViewModelProviders.of(getActivity()).get(EarthquakeViewModel.class);
        // Get the data from the View Model, and observe and changes

        earthquakeViewModel.getSignificantEqs().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(@Nullable List<Earthquake> earthquakes) {
                if (earthquakes != null && earthquakes.size()  != 0) {
                    setEarthquakes(earthquakes);
                }
            }
        });
    }

    public void setEarthquakes(List<Earthquake> earthquakes) {

        for (int i = 0; i < 9; i++) {
            mTopEarthquakes.add(earthquakes.get(i));
        }

        for (int i = 0; i < 9; i++) {
            if (i < mTopEarthquakes.size()) {
                globes.get(i).setEarthquake(mTopEarthquakes.get(i));
                titles.get(i).setText(parseTitle(mTopEarthquakes.get(i)));
                details.get(i).setText(parseDetail(mTopEarthquakes.get(i)));
            } else {
                globes.get(i).setVisibility(View.GONE);
                titles.get(i).setVisibility(View.GONE);
                details.get(i).setVisibility(View.GONE);
            }
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

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

    List<Entry> oldEntries = new ArrayList<Entry>();
    List<Entry> newEntries = new ArrayList<Entry>();

    private void addEntry(SensorEvent event) {




        BarData data = mChart.getData();

        if (data != null) {

            IBarDataSet set = data.getDataSetByIndex(0);

            if (set==null) {
                setHandle = createSet();
                set = setHandle;
                data.addDataSet(set);
            }
            float entryval = (Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2]))/3f;

            entryval = (float)(5 - (5 / (0.5*entryval + 1)));



            entryval += 0.2;

            data.setBarWidth(0.5f);
            data.addEntry(new BarEntry(set.getEntryCount(), entryval), 0);



            data.notifyDataChanged();


            // start

            mChart2.notifyDataSetChanged();

            mChart2.setVisibleXRangeMaximum(50);



            //end
//
            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(50);

            mChart.moveViewToX(data.getEntryCount());
            mChart2.moveViewToX(data.getEntryCount());



//            if (newEntries.size()>80) {
//                oldEntries = newEntries;
//                newEntries.add(new BarEntry(set.getEntryCount(), entryval));
//                AnimateDataSetChanged changer = new AnimateDataSetChanged(100, mChart, oldEntries, newEntries);
//                changer.run();
//            } else {'
//                newEntries.add(new BarEntry(set.getEntryCount(), entryval));
//            }


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
        int height = ResUtil.getInstance().dpToPx(100);
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.gradientDangerEnd),
                getResources().getColor(R.color.gradientDangerStart),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    private void setupGradient2(BarChart chart) {
        Paint paint = chart.getRenderer().getPaintRender();
        int height = ResUtil.getInstance().dpToPx(100);
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.gradientDangerEnd),
                getResources().getColor(R.color.gradientDangerStart),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupGlobesDepress(EqGlobeView button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setAlpha(0.5f);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.setAlpha(1);
                        v.performClick();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        v.setAlpha(1);
                        break;
                    }
                }
                return true;
            }
        });
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
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
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