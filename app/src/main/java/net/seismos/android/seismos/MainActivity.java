package net.seismos.android.seismos;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

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

    private FirebaseAnalytics mFirebaseAnalytics;

    private List<RecentEq> mRecentEqs;


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

                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "nav_bar_logo_wallet");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "nav_bar_logo_wallet");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "icon");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

    private class GetRecentEqsTask extends AsyncTask<Void, Void, List<RecentEq>> {
        @Override
        protected List<RecentEq> doInBackground(Void... params) {
            List<RecentEq> eqs = new RecentEqsQuery().fetchEqs();

            int i = 0;
            for (RecentEq eq : eqs) {
                Log.i(TAG, "EQ " + i + " stats:" + eq.getTitle() + "Lat: " +
                        eq.getLat() + " Long: " + eq.getLong());
                i++;
            }
            return eqs;
        }

        @Override
        protected void onPostExecute(List<RecentEq> eqs) {
            mRecentEqs = eqs;
        }


    }
}
