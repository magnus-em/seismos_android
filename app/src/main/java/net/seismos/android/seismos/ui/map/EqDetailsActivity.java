package net.seismos.android.seismos.ui.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import net.seismos.android.seismos.R;

import org.w3c.dom.Text;

public class EqDetailsActivity extends AppCompatActivity {
    private static final String TAG = "EqDetailsActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq_details);

        Toolbar toolbar = findViewById(R.id.eq_details_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView mag = findViewById(R.id.magText);
        TextView title = findViewById(R.id.titleText);
        TextView date = findViewById(R.id.dateText);
        TextView felt = findViewById(R.id.feltCountText);
        TextView tsunami = findViewById(R.id.tsunamiText);
        TextView alert = findViewById(R.id.alertText);
        TextView types = findViewById(R.id.typesText);
        TextView cdi = findViewById(R.id.cdiText);


        Intent intent = getIntent();

        mag.setText(intent.getStringExtra("mag"));
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        felt.setText(intent.getStringExtra("felt"));
        tsunami.setText(intent.getStringExtra("tsunami"));
        alert.setText(intent.getStringExtra("alert"));
        types.setText(intent.getStringExtra("types"));
        cdi.setText(intent.getStringExtra("cdi"));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
