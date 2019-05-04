package net.seismos.android.seismos.ui.profile;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import net.seismos.android.seismos.R;

import java.util.ArrayList;


public class ProfileFragment extends Fragment implements ProfileContract.View
         {

    private static final String TAG = "ProfileFragment";


    private LineChart mChart;
    private TextView seiCount;


    public ProfileFragment() {}


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mChart =  root.findViewById(R.id.chart1);
        seiCount = root.findViewById(R.id.seiEarnedCount);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // enable description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(getResources().getColor(R.color.blueBackground));
//        mChart.setViewPortOffsets(0, 0, 0, 0);


//        LineData data = new LineData();
//        data.setValueTextColor(Color.WHITE);
//
//        // add empty data
//        mChart.setData(data);

        // get the legend (only possible after setting data)
//        Legend l = mChart.getLegend();
//        l.setEnabled(false);


        XAxis xl = mChart.getXAxis();
        xl.setTextColor(getResources().getColor(R.color.offWhite));
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);;
        xl.setDrawAxisLine(false);
        xl.setDrawLabels(true);
        xl.setValueFormatter(new DayAxisValueFormatter(mChart));
        xl.setGranularityEnabled(true);
        xl.setGranularity(32);
        xl.setTextSize(12f);
        xl.setEnabled(true);



        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setLabelCount(3);
        leftAxis.setEnabled(true);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


        mChart.getLegend().setEnabled(false);


        setData(90, 56000);

    }

    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * (range+1)) + 20;
            values.add(new Entry(i, (float)(0.1 * Math.pow(i*1000, 2))));
        }

        LineDataSet set;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set =  new LineDataSet(values, "Dataset1");
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.2f);
            set.setDrawCircles(false);
            set.setDrawFilled(true);
            set.setLineWidth(6f);
            set.setColor(getResources().getColor(R.color.accentGreen));

            set.setFillDrawable(getResources().getDrawable(R.drawable.profile_chart_gradient));
            set.setHighlightEnabled(false);
            set.setDrawHorizontalHighlightIndicator(false);
            set.setHighLightColor(getResources().getColor(R.color.offWhite));
            set.setHighlightLineWidth(2f);
            LineData data = new LineData(set);



            data.setValueTextSize(9f);
            data.setDrawValues(false);

            mChart.setHighlightPerDragEnabled(true);

            mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    seiCount.setText(Integer.toString( (int) e.getY()));
                }

                @Override
                public void onNothingSelected() {
                    seiCount.setText("420,420");
                }
            });

            mChart.setData(data);
            setupGradient(mChart);
        }
    }

    private void setupGradient(LineChart chart) {
        Paint paint = chart.getRenderer().getPaintRender();
        int height = chart.getHeight();
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, 0, 0, 700,
                getResources().getColor(R.color.accentGreen),
                getResources().getColor(R.color.accentBlue),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }



    @Override
    public void onResume() {
        super.onResume();
        mChart.animateXY(2000, 2000);
        int height = mChart.getHeight();

        Log.d(TAG, Integer.toString(height));

    }



    @Override
    public void showSomething() {
    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {
    }

}
