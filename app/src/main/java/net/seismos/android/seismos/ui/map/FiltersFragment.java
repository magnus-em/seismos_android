package net.seismos.android.seismos.ui.map;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.global.Preferences;

import java.text.DecimalFormat;


public class FiltersFragment extends Fragment {

    private SharedPreferences preferences;

    RadioGroup hourDay;
    RadioGroup weekMonth;

    public FiltersFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_filters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        preferences = getActivity().getSharedPreferences(Preferences.PREFERENCES, 0);

        final CrystalSeekbar rangeSeekbar = view.findViewById(R.id.seekbar);
        final TextView minRange = view.findViewById(R.id.minMag);



        rangeSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                DecimalFormat format = new DecimalFormat("#.#");
                minRange.setText(format.format(value.doubleValue()));
            }
        });

        rangeSeekbar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                preferences.edit()
                        .putString(Preferences.PREF_MIN_MAG, String.valueOf(value))
                        .apply();
            }
        });


        rangeSeekbar.setMinStartValue(Float.parseFloat(preferences.getString(Preferences.PREF_MIN_MAG, "5")));
        rangeSeekbar.apply();

        switch (preferences.getString(Preferences.PREF_TIME_FRAME, "week")) {
            case "hour" :
                view.findViewById(R.id.hourRadio).performClick();
                break;
            case "day":
                view.findViewById(R.id.dayRadio).performClick();
                break;
            case "week":
                view.findViewById(R.id.weekRadio).performClick();
                break;
            case "month":
                view.findViewById(R.id.monthRadio).performClick();
                break;
        }

        Switch coverageSwitch = view.findViewById(R.id.coverageSwitch);
        coverageSwitch.setChecked(preferences.getBoolean(Preferences.PREF_NODE_COVERAGE, false));

         hourDay = view.findViewById(R.id.hourDayGroup);
         weekMonth = view.findViewById(R.id.weekMonthGroup);

        RadioButton hour = view.findViewById(R.id.hourRadio);
        hour.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton day = view.findViewById(R.id.dayRadio);
        day.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton week = view.findViewById(R.id.weekRadio);
        week.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton month = view.findViewById(R.id.monthRadio);
        month.setOnClickListener((View v) -> onRadioButtonClicked(v));

    }

    public void onRadioButtonClicked(View view) {

//        View disableFilter = findViewById(R.id.disableFilterCustom);


        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.hourRadio:
                if (checked) {
                    preferences.edit().putString(Preferences.PREF_TIME_FRAME, "hour").apply();
                    weekMonth.clearCheck();
                }
                break;
            case R.id.dayRadio:
                if (checked) {
                    preferences.edit().putString(Preferences.PREF_TIME_FRAME, "day").apply();
                    weekMonth.clearCheck();
                }
                break;
            case R.id.weekRadio:
                if (checked) {
                    preferences.edit().putString(Preferences.PREF_TIME_FRAME, "week").apply();
                    hourDay.clearCheck();
                }
                break;
            case R.id.monthRadio:
                if (checked) {
                    preferences.edit().putString(Preferences.PREF_TIME_FRAME, "month").apply();
                    hourDay.clearCheck();
                }
                break;
        }
    }
}
