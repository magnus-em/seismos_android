package net.seismos.android.seismos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


// TODO wrap Toolbar in an AppBarLayout



public class MainActivity extends AppCompatActivity implements HomeFragment.OnRecentEQSelectedListener{
    private static final String TAG = "MainActivity";

    private static final String CURRENT_ITEM_KEY = "current_item";
    int mCurrentItem = R.id.navigation_home;


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
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment newFragment = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                     newFragment = new HomeFragment();
//                    break;
//                case R.id.navigation_map:
//                    newFragment = new MapFragment();
//                    break;
//                case R.id.navigation_safety:
//                    newFragment = new SafetyFragment();
//                    break;
//                case R.id.navigation_wallet:
//                    newFragment = new WalletFragment();
//                    break;
//                default: break;
//            }
//
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.main_container, newFragment)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commit();
//            return true;
//        }
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
        if (savedInstanceState == null) {

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

        mNavigation.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                        Log.d(TAG, menuItem.toString());
                    }
                }
        );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_wallet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.alert_demo):
                Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
                startActivity(intent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CURRENT_ITEM_KEY, mCurrentItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestart() {
        Log.d(TAG, "onRestart() called");
        super.onRestart();

    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() called");
        super.onStop();
    }
}
