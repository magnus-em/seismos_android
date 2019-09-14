package net.seismos.android.seismos.ui.alert;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;
import android.widget.TextView;

import net.seismos.android.seismos.R;

public class AlertActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertnew);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        TextView secondsAway = findViewById(R.id.secondsAway);


        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                secondsAway.setText( ""+millisUntilFinished / 1000);
            }

            public void onFinish() {
                secondsAway.setText("0");
            }
        }.start();

    }


}
