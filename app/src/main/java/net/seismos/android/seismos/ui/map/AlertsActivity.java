package net.seismos.android.seismos.ui.map;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Switch;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.global.Preferences;

public class AlertsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        Toolbar toolbar = findViewById(R.id.alerts_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences preferences = getSharedPreferences(Preferences.PREFERENCES, 0);

        Switch notifSwitch = findViewById(R.id.eqNotification);
        notifSwitch.setChecked(preferences.getBoolean(Preferences.PREF_EQ_NOTIF, true));
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
