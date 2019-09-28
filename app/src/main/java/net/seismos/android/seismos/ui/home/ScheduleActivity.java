package net.seismos.android.seismos.ui.home;

import android.os.Bundle;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.seismos.android.seismos.R;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        final View disableFilter = findViewById(R.id.disableFilterSchedule);
        Switch schedSwitch = findViewById(R.id.scheduleSwitch);
        schedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableFilter.setClickable(false);
                    disableFilter.setVisibility(INVISIBLE);
                } else {
                    disableFilter.setClickable(true);
                    disableFilter.setVisibility(VISIBLE);
                }
            }
        });

        (findViewById(R.id.radioEveryDay)).setOnClickListener((View v) ->
                onRadioButtonClicked(v));
        findViewById(R.id.radioCustom).setOnClickListener((View v) ->
                onRadioButtonClicked(v));
    }

    public void onRadioButtonClicked(View view) {

        View disableFilter = findViewById(R.id.disableFilterCustom);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioEveryDay:
                if (checked)
                    disableFilter.setVisibility(VISIBLE);
                disableFilter.setClickable(true);
                break;
            case R.id.radioCustom:
                if (checked)
                    disableFilter.setVisibility(INVISIBLE);
                disableFilter.setClickable(false);
                break;
        }
    }
}
