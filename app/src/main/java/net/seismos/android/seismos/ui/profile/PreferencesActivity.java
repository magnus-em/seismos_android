package net.seismos.android.seismos.ui.profile;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;


import net.seismos.android.seismos.R;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = findViewById(R.id.prefs_toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    public static class PrefFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }
    }


}
