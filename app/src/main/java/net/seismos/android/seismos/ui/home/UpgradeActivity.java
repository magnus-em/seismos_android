package net.seismos.android.seismos.ui.home;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import net.seismos.android.seismos.R;

public class UpgradeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        ViewPager viewPager = findViewById(R.id.upgradeViewPager);
        UpgradeAdapter upgradeAdapter = new UpgradeAdapter(getSupportFragmentManager());
        upgradeAdapter.addFragment(new UpgradeFragment1(), "Seismos Pro");
//        upgradeAdapter.addFragment(new UpgradeFragment2(), "Seismos Pro");
        upgradeAdapter.addFragment(new UpgradeFragment3(), "Seismos Patron");

        viewPager.setAdapter(upgradeAdapter);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.gradientBlueHighlightEnd));
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
