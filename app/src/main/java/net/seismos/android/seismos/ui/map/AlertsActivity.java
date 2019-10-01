package net.seismos.android.seismos.ui.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.global.Preferences;
import net.seismos.android.seismos.ui.social.AddFriendActivity;
import net.seismos.android.seismos.ui.social.FriendsAdapter;


public class AlertsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences preferences = getSharedPreferences(Preferences.PREFERENCES, 0);

        Switch notifSwitch = findViewById(R.id.eqNotification);
        notifSwitch.setChecked(preferences.getBoolean(Preferences.PREF_EQ_NOTIF, true));

        notifSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(Preferences.PREF_EQ_NOTIF, isChecked).apply();
        });


        RecyclerView friendsRecycler = findViewById(R.id.friendsRecycler);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));
        friendsRecycler.setAdapter(new FriendsAdapter());

        ImageView addFriends = findViewById(R.id.addFriendsButton);
        addFriends.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddFriendActivity.class);
            startActivity(intent);
        });

        ImageView addZone = findViewById(R.id.addZoneButton);
        addZone.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, AddAlertZoneActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
