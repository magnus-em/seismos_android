package net.seismos.android.seismos;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

// TODO query USGS
// TODO implement app bar on certain fragments
// TODO implement charting library
// TODO figure out recentEQglobeview

public class MainActivity extends AppCompatActivity implements HomeFragment.OnRecentEQSelectedListener{

    private static final String TAG = "MainActivity";


    private TextView mTextMessage;

    final Fragment homeFragment = new HomeFragment();
    final MapFragment mapFragment = new MapFragment();
    final Fragment safetyFragment = new SafetyFragment();
    final Fragment walletFragment = new WalletFragment();
    Fragment active = homeFragment;

    final FragmentManager mFragmentManager = getSupportFragmentManager();

    BottomNavigationView mNavigation;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mFragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    Log.i(TAG, "navigation_home pressed");
                    mFragmentManager.executePendingTransactions();
                    return true;
                case R.id.navigation_map:
                    mFragmentManager.beginTransaction().hide(active).show(mapFragment).commit();
                    active = mapFragment;
                    mFragmentManager.executePendingTransactions();
                    return true;
                case R.id.navigation_safety:
                    mFragmentManager.beginTransaction().hide(active).show(safetyFragment).commit();
                    active = safetyFragment;
                    mFragmentManager.executePendingTransactions();
                    return true;
                case R.id.navigation_wallet:
                    mFragmentManager.beginTransaction().hide(active).show(walletFragment).commit();
                    active = walletFragment;
                    mFragmentManager.executePendingTransactions();
                    return true;

            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation =  findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




        /*getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); */

        mFragmentManager.beginTransaction()
                .add(R.id.main_container, walletFragment, "walletFragment")
                .hide(walletFragment)
                .commit();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, safetyFragment, "safetyFragment")
                .hide(safetyFragment)
                .commit();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, mapFragment, "mapFragment")
                .hide(mapFragment)
                .commit();
        mFragmentManager.beginTransaction()
                .add(R.id.main_container, homeFragment, "homeFragment")
                .commit();
        mFragmentManager.executePendingTransactions();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_wallet_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {


        if (active == homeFragment) {
            super.onBackPressed();
        } else {
            mFragmentManager.beginTransaction()
                    .hide(active)
                    .show(homeFragment)
                    .commit();
            mNavigation.setSelectedItemId(R.id.navigation_home);
            active = homeFragment;
        }


    }

    @Override
    public void onRecentEQSelected(String location) {

        switch (location) {
            case "seattle":

                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("seattle");

                break;
            case "hk":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("hk");
                break;
            case "nyc":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("nyc");
                break;
            case "london":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("london");
                break;
        }
    }


}
