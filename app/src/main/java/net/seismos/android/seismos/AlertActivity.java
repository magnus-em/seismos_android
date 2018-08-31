package net.seismos.android.seismos;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AlertActivity extends AppCompatActivity {

    TextView mSecAway;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mSecAway = findViewById(R.id.seconds_away);


        new CountDownTimer(32000, 1000) {

            public void onTick(long millisUntilFinished) {
                mSecAway.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mSecAway.setText("0");
            }

        }.start();
    }
}
