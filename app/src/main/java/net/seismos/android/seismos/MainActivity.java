package net.seismos.android.seismos;

import android.content.Intent;
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


// TODO implement FAB on mapfragment
// TODO implement earthquake card button on click map focus
// TODO fix shadow issue on the back of the enable button
// TODO listActivity to see all, from earthquake card



public class MainActivity extends AppCompatActivity implements HomeFragment.OnRecentEQSelectedListener{
    private static final String TAG = "MainActivity";



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

        //TODO I'm guessing the reason for the overlapping fragments when you reopen the app after
        // leaving it for a while is because we're not checking iv savedInstanceState is null before
        // adding these. This results in duplicate fragments because android will recreate fragments
        Log.d(TAG, "onCreate called again");
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

        MenuItem alertDemo = menu.findItem(R.id.alert_demo);

        alertDemo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
                startActivity(intent);
                return true;
            }
        });

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
            case "japan":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("japan");
                break;
            case "usa":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("usa");
                break;
            case "nepal":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("nepal");
                break;
            case "chile":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("chile");
                break;
            case "turkey":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("turkey");
                break;
            case "australia":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                mapFragment.updateLocation("australia");
                break;
            case "":
                mFragmentManager.beginTransaction()
                        .hide(active)
                        .show(mapFragment)
                        .commit();
                mNavigation.setSelectedItemId(R.id.navigation_map);
                active = mapFragment;
                break;
        }
    }
}
