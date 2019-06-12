package net.seismos.android.seismos.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
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

import java.util.Arrays;
import java.util.List;

public class DashActivity extends AppCompatActivity implements HomeFragment.OnEqGlobeSelectedListener {
    private static final String TAG = "DashActivity";

    int RC_SIGN_IN = 123;



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


        createSignInIntent();


    }

    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firebase_sign_in)
                .setGoogleButtonId(R.id.googleButton)
                .setEmailButtonId(R.id.emailButton)
                .build();


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setTheme(R.style.SignInTheme)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
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
