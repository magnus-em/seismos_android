package net.seismos.android.seismos.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.global.DashActivity;

public class OnboardingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        findViewById(R.id.onboardingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnboardingActivity.this, DashActivity.class));
            }
        });
    }
}
