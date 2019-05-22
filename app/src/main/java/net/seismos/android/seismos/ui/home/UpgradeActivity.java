package net.seismos.android.seismos.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.seismos.android.seismos.R;

public class UpgradeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        ViewPager viewPager = findViewById(R.id.upgradeViewPager);
        UpgradeAdapter upgradeAdapter = new UpgradeAdapter(getSupportFragmentManager());
        upgradeAdapter.addFragment(new UpgradeFragment1(), "Seismos Plus");
        upgradeAdapter.addFragment(new UpgradeFragment2(), "Seismos Pro");
        upgradeAdapter.addFragment(new UpgradeFragment3(), "Seismos Supporter");

        viewPager.setAdapter(upgradeAdapter);



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
