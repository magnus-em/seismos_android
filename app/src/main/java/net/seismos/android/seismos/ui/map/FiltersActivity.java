package net.seismos.android.seismos.ui.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import net.seismos.android.seismos.R;

public class FiltersActivity extends AppCompatActivity {
    private static final String TAG = "FiltersActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        Toolbar toolbar = findViewById(R.id.store_details_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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

            }
        });

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
                if (checked)
                    weekMonth.clearCheck();
                break;
            case R.id.dayRadio:
                if (checked)
                   weekMonth.clearCheck();
                break;
            case R.id.weekRadio:
                if (checked)
                    hourDay.clearCheck();
                break;
            case R.id.monthRadio:
                if (checked)
                    hourDay.clearCheck();
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
