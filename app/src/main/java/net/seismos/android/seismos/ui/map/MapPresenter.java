package net.seismos.android.seismos.ui.map;

import net.seismos.android.seismos.ui.map.MapContract.Presenter;

public class MapPresenter implements MapContract.Presenter {

    MapContract.View mapView;

    public MapPresenter(MapContract.View mapView) {
        this.mapView = mapView;
        mapView.setPresenter(this);
    }

    @Override
    public void mapClicked() {
        mapView.toggleBottomSheet();
    }

    @Override
    public void somethingHappened() {

    }

    @Override
    public void start() {

    }
}
