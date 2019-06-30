package net.seismos.android.seismos.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import net.seismos.android.seismos.R;

public class ScheduleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.schedule_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        final View  disableFilter = view.findViewById(R.id.disableFilterSchedule);
        Switch schedSwitch = view.findViewById(R.id.scheduleSwitch);
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

        (view.findViewById(R.id.radioEveryDay)).setOnClickListener((View v) ->
                onRadioButtonClicked(v));
        view.findViewById(R.id.radioCustom).setOnClickListener((View v) ->
                onRadioButtonClicked(v));


    }

    public void onRadioButtonClicked(View view) {

        View disableFilter = view.findViewById(R.id.disableFilterCustom);

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
}
