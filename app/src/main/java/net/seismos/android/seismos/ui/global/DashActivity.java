package net.seismos.android.seismos.ui.global;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
    private  BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNav =  findViewById(R.id.navigation);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }

    //TODO: this method is broken because it doesn't handle the latlng
    public void openMapToLatLng(LatLng latLng) {
        Navigation.findNavController(this, R.id.nav_host_fragment)
                .navigate(R.id.map);
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
