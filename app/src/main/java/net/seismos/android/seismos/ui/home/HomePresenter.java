package net.seismos.android.seismos.ui.home;

import android.support.v4.app.Fragment;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View homeView;

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
}
