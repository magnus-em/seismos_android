package net.seismos.android.seismos.ui.map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.global.Preferences;

public class FiltersActivity extends AppCompatActivity {
    private static final String TAG = "FiltersActivity";

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        Toolbar toolbar = findViewById(R.id.filters_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        preferences = getSharedPreferences(Preferences.PREFERENCES, 0);

        final CrystalRangeSeekbar rangeSeekbar = findViewById(R.id.seekbar);
        final TextView minRange = findViewById(R.id.magRangeMin);
        final TextView maxRange = findViewById(R.id.magRangeMax);

        // change listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minRange.setText(String.valueOf(minValue));
                maxRange.setText(String.valueOf(maxValue));
            }
        });

        // final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("filtersActivity", "max:" + maxValue.toString()+ " min" + minValue.toString());
                preferences.edit()
                        .putString(Preferences.PREF_MIN_MAG, String.valueOf(minValue))
                        .putString(Preferences.PREF_MAX_MAG, String.valueOf(maxValue))
                        .apply();
            }
        });

        rangeSeekbar.setMaxStartValue(Float.parseFloat(preferences.getString(Preferences.PREF_MAX_MAG, "10")));
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
    }

    public void onRadioButtonClicked(View view) {

//        View disableFilter = findViewById(R.id.disableFilterCustom);

        RadioGroup hourDay = findViewById(R.id.hourDayGroup);
        RadioGroup weekMonth = findViewById(R.id.weekMonthGroup);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
