package net.seismos.android.seismos.ui.alert;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import net.seismos.android.seismos.R;

public class AlertActivity extends AppCompatActivity {

    TextView mSecAway;
    TextView mKmAway;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        mSecAway = findViewById(R.id.seconds_away);
        mKmAway = findViewById(R.id.km_away);

        new CountDownTimer(24000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mSecAway.setText(Long.toString(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                mSecAway.setText("0");
            }
        }.start();

        new CountDownTimer(23700, 200) {
            @Override
            public void onTick(long millisUntilFinished) {
                mKmAway.setText(Long.toString(millisUntilFinished / 200));
            }
            @Override
            public void onFinish() {
                mKmAway.setText("0");
            }
        }.start();
    }
}
