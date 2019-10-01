package net.seismos.android.seismos.ui.seismos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.util.DepressAnimationUtil;

public class EqSafetyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_safety);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView icon = findViewById(R.id.planIcon);
        TextView title = findViewById(R.id.planTitle);
        TextView sub = findViewById(R.id.planSub);
        setAnimAndClick(icon, title, sub, MakeAPlanActivity.class);

        icon = findViewById(R.id.kitIcon);
        title = findViewById(R.id.kitTitle);
        sub = findViewById(R.id.kitSub);
        setAnimAndClick(icon, title, sub, BuildAKitActivity.class);

        icon = findViewById(R.id.homeIcon);
        title = findViewById(R.id.homeTitle);
        sub = findViewById(R.id.homeSub);
        setAnimAndClick(icon, title, sub, SecureYourHomeActivity.class);

        icon = findViewById(R.id.actionIcon);
        title = findViewById(R.id.actionTitle);
        sub = findViewById(R.id.actionSub);
        setAnimAndClick(icon, title, sub, ProtectiveActionActivity.class);

        icon = findViewById(R.id.earthquakeIcon);
        title = findViewById(R.id.earthquakeTitle);
        sub = findViewById(R.id.earthquakeSub);
        setAnimAndClick(icon, title, sub, AfterTheEarthquakeActivity.class);

        icon = findViewById(R.id.aidIcon);
        title = findViewById(R.id.aidTitle);
        sub = findViewById(R.id.aidSub);
        setAnimAndClick(icon, title, sub, FirstAidActivity.class);

        icon = findViewById(R.id.shelterIcon);
        title = findViewById(R.id.shelterTitle);
        sub = findViewById(R.id.shelterSub);
        setAnimAndClick(icon, title, sub, FindShelterActivity.class);

        icon = findViewById(R.id.volunteerIcon);
        title = findViewById(R.id.volunteerTitle);
        sub = findViewById(R.id.volunteerSub);
        setAnimAndClick(icon, title, sub, VolunteerActivity.class);


    }

    private void setAnimAndClick(View icon, View title, View sub, Class<?> cls) {
        DepressAnimationUtil.setup(icon, title, sub);
        icon.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        });
        title.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        });
        sub.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        });

    }


}
