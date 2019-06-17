package net.seismos.android.seismos.ui.home;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import net.seismos.android.seismos.R;

public class ScheduleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        Toolbar toolbar = findViewById(R.id.schedule_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final View  disableFilter = findViewById(R.id.disableFilterSchedule);


        Switch schedSwitch = findViewById(R.id.scheduleSwitch);
        schedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableFilter.setClickable(false);
                    disableFilter.setVisibility(View.INVISIBLE);
                } else {
                    disableFilter.setClickable(true);
                    disableFilter.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {

        View disableFilter = findViewById(R.id.disableFilterCustom);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioEveryDay:
                if (checked)
                    disableFilter.setVisibility(View.VISIBLE);
                    disableFilter.setClickable(true);
                    break;
            case R.id.radioCustom:
                if (checked)
                    disableFilter.setVisibility(View.INVISIBLE);
                    disableFilter.setClickable(false);
                break;
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
