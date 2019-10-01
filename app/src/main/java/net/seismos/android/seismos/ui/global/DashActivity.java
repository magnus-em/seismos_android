package net.seismos.android.seismos.ui.global;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.home.HomeFragment;
import net.seismos.android.seismos.ui.map.MapFragment;
import net.seismos.android.seismos.ui.profile.ProfileFragment;
import net.seismos.android.seismos.ui.seismos.SeismosFragment;
import net.seismos.android.seismos.ui.store.StoreFragment;

public class DashActivity extends AppCompatActivity implements HomeFragment.OnEqGlobeSelectedListener {

    final String HOME_FRAGMENT = "HOME_FRAGMENT";
    final String MAP_FRAGMENT = "MAP_FRAGMENT";
    final String SEISMOS_FRAGMENT = "SEISMOS_FRAGMENT";
    final String STORE_FRAGMENT = "STORE_FRAGMENT";
    final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";

    HomeFragment homeFragment;
    MapFragment mapFragment;
    SeismosFragment seismosFragment;
    StoreFragment storeFragment;
    ProfileFragment profileFragment;

    private Fragment active;
    private BottomNavigationView nav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);


        if (savedInstanceState==null) {

            homeFragment = new HomeFragment();
            mapFragment = new MapFragment();
            seismosFragment = new SeismosFragment();
            storeFragment = new StoreFragment();
            profileFragment = new ProfileFragment();


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.host_container,homeFragment, HOME_FRAGMENT)
                    .hide(homeFragment)
                    .add(R.id.host_container, mapFragment, MAP_FRAGMENT)
                    .hide(mapFragment)
                    .add(R.id.host_container, seismosFragment, SEISMOS_FRAGMENT)
                    .hide(seismosFragment)
                    .add(R.id.host_container, storeFragment, STORE_FRAGMENT)
                    .hide(storeFragment)
                    .add(R.id.host_container, profileFragment, PROFILE_FRAGMENT)
                    .hide(profileFragment)
                    .show(homeFragment)
                    .commit();

            active = homeFragment;
        }




        nav = findViewById(R.id.bottom_nav);
        setupNavMenu(nav);
    }




    private void setupNavMenu(BottomNavigationView nav) {
        nav.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.homeTitle:
                    getSupportFragmentManager().beginTransaction()
                            .hide(active)
                            .show(homeFragment)
                            .commit();
                    active = homeFragment;
                    break;
                case R.id.map:
                    getSupportFragmentManager().beginTransaction()
                            .hide(active)
                            .show(mapFragment)
                            .commit();
                    active = mapFragment;
                    break;
                case R.id.seismos:
                    getSupportFragmentManager().beginTransaction()
                            .hide(active)
                            .show(seismosFragment)
                            .commit();
                    active = seismosFragment;
                    break;
                case R.id.store:
                    getSupportFragmentManager().beginTransaction()
                            .hide(active)
                            .show(storeFragment)
                            .commit();
                    active = storeFragment;
                    break;
                case R.id.profile:
                    getSupportFragmentManager().beginTransaction()
                            .hide(active)
                            .show(profileFragment)
                            .commit();
                    active = profileFragment;
                    break;
            }
            return true;
        });
    }


    @Override
    public void openMapToLatLng(LatLng latLng) {
        nav.setSelectedItemId(R.id.map);
        mapFragment.navigateToLatLng(latLng);
    }
}
