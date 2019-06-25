package net.seismos.android.seismos.ui.home;

import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.util.ResUtil;

import java.util.ArrayList;

public class ChartFragmentMonth extends Fragment {


    BarChart chart;

    ChartFragmentMonth() {}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_chart_month, container, false);

        chart = root.findViewById(R.id.barChart);

        chart.setMaxVisibleValueCount(60);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value == 0) {
                    return "1";
                } else if (value == 6) {
                    return "7";
                }
                else return "";
            }
        });
        xAxis.setTextColor(getResources().getColor(R.color.offWhite));
        xAxis.setTextSize(14f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawGridLines(false);

        yAxis.setDrawAxisLine(false);
        yAxis.setEnabled(true);
        yAxis.setLabelCount(2);
        yAxis.setTextColor((getResources().getColor(R.color.offWhite)));
        yAxis.setTextSize(12f);


        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setEnabled(false);

        chart.setDrawGridBackground(false);
        chart.getLegend().setEnabled(false);

        chart.getDescription().setEnabled(false);


        chart.setDrawGridBackground(false);
        chart.getAxisLeft().setDrawGridLines(false);

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            float multi = 11;
            float val = (float) (Math.random()*multi);
            val = val <3?0:val;
            values.add(new BarEntry(i, val));
        }



        BarDataSet set = new BarDataSet(values, "Dataset");

        set.setColors(ColorTemplate.LIBERTY_COLORS);
        set.setDrawValues(false);



        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);

        data.setBarWidth(0.97f);


        chart.setData(data);
        chart.setFitBars(true);

//        chart.animateY(1500);

        CustomBarChartRenderAll barChartRender = new CustomBarChartRenderAll(chart,chart.getAnimator(), chart.getViewPortHandler());
        barChartRender.setRadius(20);
        chart.setRenderer(barChartRender);




        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BarChart chart = view.findViewById(R.id.barChart);
        setupGradient(chart);

    }

    private void setupGradient(BarChart chart) {
        Paint paint = chart.getRenderer().getPaintRender();
        int height = ResUtil.getInstance().dpToPx(240);
//        Log.d(TAG, Integer.toString(height));

        LinearGradient gradient = new LinearGradient(0, height, 0, 0,
                getResources().getColor(R.color.accentBlue),
                getResources().getColor(R.color.accentGreen),
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }
}
