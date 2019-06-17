package net.seismos.android.seismos.ui.home;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View homeView;

    private SensorManager mSensorManager;
    private Sensor sensor;

    public HomePresenter(HomeContract.View homeView) {
        this.homeView = homeView;
        homeView.setPresenter(this);
    }




    @Override
    public void somethingHappened() {
        // demo method that a View would call to pass on UI
        homeView.showSomething();
    }

    @Override
    public void start() {
            // this will be called in a corresponding lifecycle callback in a View
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }



}
