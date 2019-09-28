package net.seismos.android.seismos.ui.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.global.Preferences;

import java.text.DecimalFormat;

public class FiltersActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    RadioGroup hourDay;
    RadioGroup weekMonth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        preferences = getSharedPreferences(Preferences.PREFERENCES, 0);

        final CrystalSeekbar rangeSeekbar = findViewById(R.id.seekbar);
        final TextView minRange = findViewById(R.id.minMag);



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
                findViewById(R.id.hourRadio).performClick();
                break;
            case "day":
                findViewById(R.id.dayRadio).performClick();
                break;
            case "week":
                findViewById(R.id.weekRadio).performClick();
                break;
            case "month":
                findViewById(R.id.monthRadio).performClick();
                break;
        }

        Switch coverageSwitch = findViewById(R.id.coverageSwitch);
        coverageSwitch.setChecked(preferences.getBoolean(Preferences.PREF_NODE_COVERAGE, false));

        hourDay = findViewById(R.id.hourDayGroup);
        weekMonth = findViewById(R.id.weekMonthGroup);

        RadioButton hour = findViewById(R.id.hourRadio);
        hour.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton day = findViewById(R.id.dayRadio);
        day.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton week = findViewById(R.id.weekRadio);
        week.setOnClickListener((View v) -> onRadioButtonClicked(v));
        RadioButton month = findViewById(R.id.monthRadio);
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
