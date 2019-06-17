package net.seismos.android.seismos.ui.global;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.home.HomeContract;
import net.seismos.android.seismos.ui.home.HomeFragment;
import net.seismos.android.seismos.ui.home.HomePresenter;
import net.seismos.android.seismos.ui.map.MapContract;
import net.seismos.android.seismos.ui.map.MapFragment;
import net.seismos.android.seismos.ui.map.MapPresenter;
import net.seismos.android.seismos.ui.store.StoreFragment;
import net.seismos.android.seismos.ui.profile.ProfileFragment;
import net.seismos.android.seismos.ui.seismos.SeismosFragment;

public class DashActivity extends AppCompatActivity implements HomeFragment.OnEqGlobeSelectedListener {
    private static final String TAG = "DashActivity";


    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment currentFragment;
    private Fragment homeFragment;
    private Fragment mapFragment;
    private Fragment seismosFragment;
    private Fragment storeFragment;
    private Fragment profileFragment;

    private HomePresenter homePresenter;
    private MapPresenter mapPresenter;

    private  BottomNavigationView mNavigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_item_home:
                    setCurrentFragment(homeFragment);
                    return true;
                case R.id.nav_item_map:
                    setCurrentFragment(mapFragment);
                    return true;
                case R.id.nav_item_seismos:
                    setCurrentFragment(seismosFragment);
                    return true;
                case R.id.nav_item_store:
                    setCurrentFragment(storeFragment);
                    return true;
                case R.id.nav_item_profile:
                    setCurrentFragment(profileFragment);
                    return true;
            }
            return false;
        }
    };

    private void setCurrentFragment(Fragment fragment, int itemId) {
        if (fragment == currentFragment)
            return;
         fragmentManager.beginTransaction()
                 .hide(currentFragment)
                 .show(fragment)
                 .commit();
         currentFragment = fragment;

         mNavigation.setSelectedItemId(itemId);

    }
    private void setCurrentFragment(Fragment fragment) {
        if (fragment == currentFragment)
            return;
        fragmentManager.beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit();
        currentFragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);



        if (savedInstanceState != null) {
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                 if (fragment!=null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }

        homeFragment = HomeFragment.newInstance();
        mapFragment = new MapFragment();
        seismosFragment = SeismosFragment.newInstance();
        storeFragment = StoreFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        homePresenter = new HomePresenter((HomeContract.View) homeFragment);
        mapPresenter = new MapPresenter((MapContract.View)mapFragment);

        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment)
                .hide(homeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, mapFragment)
                    .hide(mapFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, seismosFragment)
                    .hide(seismosFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, storeFragment)
                    .hide(storeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, profileFragment)
                    .hide(profileFragment).commit();

        currentFragment = homeFragment;
        fragmentManager.beginTransaction().show(homeFragment).commit();

        mNavigation =  findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }


    public void openMapToLatLng(LatLng latLng) {
        setCurrentFragment(mapFragment, R.id.nav_item_map);
        mapPresenter.openMapToLatLng(latLng);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
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